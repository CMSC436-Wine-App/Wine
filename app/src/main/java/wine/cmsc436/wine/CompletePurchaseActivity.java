package wine.cmsc436.wine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Set;

import parse.subclasses.Badge;
import parse.subclasses.BadgeDiscount;
import parse.subclasses.Purchase;
import parse.subclasses.PurchaseHistory;
import parse.subclasses.User;
import parse.subclasses.UserBadge;
import parse.subclasses.Wine;


public class CompletePurchaseActivity extends Activity {

    private static PurchaseListAdapter listAdapter;
    private Button checkout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_purchase);
        listAdapter = new PurchaseListAdapter(getApplicationContext());
        ListView lv = ((ListView) findViewById(R.id.purchase_content));
        lv.setAdapter(listAdapter);

        checkout = (Button) findViewById(R.id.purchase_checkout);
        checkout.setText(getString(R.string.complete_purchase, App.currentPurchases.getTotal()));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WinePurchase wp = (WinePurchase) listAdapter.getItem(position);
                BadgeDiscount bd = App.currentPurchases.getDiscountedInfo(wp);
                if (bd != null) {
                    Intent intent = new Intent(CompletePurchaseActivity.this, DiscountOverviewActivity.class);
                    intent.setData(bd.getBadge().getUri());
                    intent.putExtra("discountRate", bd.getDiscountRate());
                    intent.putExtra("wineName", wp.getPurchase().getWine().getName());
                    intent.putExtra("badgeName", bd.getBadge().getName());
                    startActivity(intent);
                } else {
                    Toast.makeText(CompletePurchaseActivity.this, R.string.discount_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        // TODO: When pressing on a list item, show dialog to delete
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.currentPurchases.getTotal().equals("$0.00")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CompletePurchaseActivity.this);
                    builder.setTitle(R.string.complete_purchase_failure);
                    builder.setMessage(R.string.complete_purchase_failure_body)
                            .setPositiveButton(R.string.purchase_dialog_OK, null)
                            .setNegativeButton(R.string.purchase_dialog_cancel, null);
                    builder.create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CompletePurchaseActivity.this);
                    builder.setTitle(R.string.complete_purchase_success);
                    builder.setMessage(getString(R.string.complete_purchase_success_body, App.currentPurchases.getTotal()))
                            .setPositiveButton(R.string.purchase_dialog_OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    completeOrder();
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.purchase_dialog_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.create().show();
                }
            }
        });
    }

    public void completeOrder() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                // Disable the used discounts
                disableUsedDiscounts();
                // Record the Purchase
                savePurchases();
                // Attain the new badges we qualify for
                App.addAvailableWineBadges();
                listAdapter.clear();
            }
        })).start();
    }

    public void disableUsedDiscounts() {
        try {
            Set<WinePurchase> discountedWP = App.currentPurchases.getDiscountedWinePurchases();
            for (WinePurchase wp : discountedWP) {
                Wine w = wp.getPurchase().getWine();
                BadgeDiscount bd = App.currentPurchases.getDiscountedInfo(wp);
                final Badge usedBadge = bd.getBadge();

                ParseQuery<UserBadge> unusedBadges = UserBadge.getWineBadges(w, usedBadge, User.getCurrentUser());
                List<UserBadge> userBadges = unusedBadges.find();
                if (userBadges.size() == 0) {
                    Log.i(App.APPTAG, "Badge being unused not found: " + usedBadge.getName());
                } else {
                    UserBadge ub = userBadges.get(0);
                    ub.setUsed(true);
                    ub.save();
                }
            }
        } catch (ParseException e) {
            Log.i(App.APPTAG, e.getMessage());
        }
    }

    public void savePurchases() {
        try {
            String total = App.currentPurchases.getTotal();
            double purchaseTotal = Double.valueOf(total.substring(total.indexOf("$") + 1, total.length()));
            PurchaseHistory purchaseHistory = new PurchaseHistory(User.getCurrentUser(), purchaseTotal);
            purchaseHistory.save();

            for (int i = 0; i < App.currentPurchases.size(); i++) {
                WinePurchase wp = App.currentPurchases.get(i);
                Purchase p = new Purchase(User.getCurrentUser(), wp.getPurchase().getWine());
                p.setQuantity(wp.getQuantity());
                p.setType(wp.getWineType().toString());

                Double discountedPrice = App.currentPurchases.getDiscountedTotal(wp);
                Double normalPrice = wp.getPrice() * wp.getQuantity();
                if (discountedPrice != null) {
                    p.setPaidPrice(discountedPrice);
                    p.setDiscountApplied(true);
                    p.setBadgeDiscount(App.currentPurchases.getDiscountedInfo(wp));
                } else {
                    p.setPaidPrice(normalPrice);
                    p.setDiscountApplied(false);
                }

                p.setPurchaseHistory(purchaseHistory);
                p.save();
            }
        } catch (ParseException e) {
            Log.i(App.APPTAG, e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete_purchase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove_all_purchases) {
            listAdapter.clear();
            checkout.setText(getString(R.string.complete_purchase, App.currentPurchases.getTotal()));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

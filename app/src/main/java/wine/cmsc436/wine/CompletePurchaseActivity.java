package wine.cmsc436.wine;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import parse.subclasses.Badge;
import parse.subclasses.BadgeDiscount;
import parse.subclasses.MenuItem;
import parse.subclasses.Purchase;
import parse.subclasses.MenuItem;
import parse.subclasses.PurchaseHistory;
import parse.subclasses.User;


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
                WinePurchase wp = (WinePurchase)listAdapter.getItem(position);
                BadgeDiscount bd = App.currentPurchases.getDiscountedInfo(wp);
                if (bd != null) {
                    Intent intent = new Intent(CompletePurchaseActivity.this, DiscountOverviewActivity.class);
                    intent.setData(bd.getBadge().getUri());
                    intent.putExtra("discountRate", bd.getDiscountRate());
                    intent.putExtra("wineName", wp.getPurchase().getWine().getName());
                    intent.putExtra("badgeName", bd.getBadge().getName());
                    startActivity(intent);
                }
                else {
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
                                    listAdapter.clear();
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.purchase_dialog_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    completeOrder();
                                    listAdapter.clear();
                                    finish();
                                }
                            });
                    builder.create().show();
                }
            }
        });
    }

    public void completeOrder() {
        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.saveInBackground();
        for (int i = 0 ; i < App.currentPurchases.size(); i++) {
            WinePurchase wp = App.currentPurchases.get(i);
            for (int j = 0; j < wp.getQuantity(); j++) {
                Purchase p = new Purchase(User.getCurrentUser(), wp.getPurchase().getWine());
                p.setPurchaseHistory(purchaseHistory);
                p.saveInBackground();
            }
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

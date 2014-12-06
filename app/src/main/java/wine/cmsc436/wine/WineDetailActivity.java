package wine.cmsc436.wine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.NumberFormat;
import java.util.List;

import parse.subclasses.Purchase;
import parse.subclasses.User;
import parse.subclasses.Wine;
import parse.subclasses.MenuItem;


public class WineDetailActivity extends ActionBarActivity {

    private Wine selectedWine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_detail);

        final Button glassOrderButton = (Button)findViewById(R.id.glass_order_btn);
        glassOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Purchase p = new Purchase(User.getCurrentUser(), selectedWine);
                // This is gross but i have to do it to save myself from subclassing.
                String glassPrice = glassOrderButton.getText().toString();
                glassPrice = glassPrice.substring(glassPrice.lastIndexOf(":")+3, glassPrice.length());
                App.currentPurchases.add(new WinePurchase(p, WinePurchase.WineType.GLASS, Double.valueOf(glassPrice)));
                showPurchaseDialog();
            }
        });

        final Button bottleOrderButton = (Button)findViewById(R.id.bottle_order_btn);
        bottleOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Purchase p = new Purchase(User.getCurrentUser(), selectedWine);
                String bottlePrice = bottleOrderButton.getText().toString();
                bottlePrice = bottlePrice.substring(bottlePrice.lastIndexOf(":")+3, bottlePrice.length());
                App.currentPurchases.add(new WinePurchase(p, WinePurchase.WineType.BOTTLE, Double.valueOf(bottlePrice)));
                showPurchaseDialog();
            }
        });

        final Button reviewButton = (Button) findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(WineDetailActivity.this, NewWineReview.class);
                reviewIntent.putExtra("wineName", selectedWine.getName());
                reviewIntent.setData(selectedWine.getUri());
                startActivity(reviewIntent);
            }
        });

        final Button viewReviewsButton = (Button)findViewById(R.id.view_reviews_button);
        viewReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Bring up reviews page for this specific Wine
            }
        });

        // Fetch the data about this wine from Parse.
        String wineId = Wine.getObjectId(getIntent().getData());
        GetCallback<Wine> wineGetCallback = new GetCallback<Wine>() {
            @Override
            public void done(Wine wine, ParseException e) {
                if (e != null) {
                    Toast toast = Toast.makeText(WineDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                selectedWine = wine;
                final RelativeLayout wineDescLayout = (RelativeLayout)findViewById(R.id.order_layout);
                final RelativeLayout reviewLayout = (RelativeLayout)findViewById(R.id.review_layout);
                ParseQuery<MenuItem> menuItemParseQuery = MenuItem.getPriceQuery(selectedWine);
                menuItemParseQuery.findInBackground(new FindCallback<MenuItem>() {
                    @Override
                    public void done(List<MenuItem> menuItems, ParseException e) {
                        if (e != null) {
                            Toast toast = Toast.makeText(WineDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        try {
                            MenuItem menuItem = menuItems.get(0);
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            bottleOrderButton.setText(bottleOrderButton.getText() + " " + formatter.format(menuItem.getBottlePrice()));
                            glassOrderButton.setText(glassOrderButton.getText() + " " + formatter.format(menuItem.getGlassPrice()));
                        } catch (IndexOutOfBoundsException ex) {
                            Log.i("MenuItem missing for Wine Id:", selectedWine.getObjectId());
                        }
                        updateView(selectedWine);
                        wineDescLayout.setVisibility(View.VISIBLE);
                        reviewLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        ParseQuery<Wine> wineQuery = Wine.getQuery();
        wineQuery.fromLocalDatastore();
        wineQuery.getInBackground(wineId, wineGetCallback);
   }


    private void showPurchaseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WineDetailActivity.this);
        builder.setTitle(R.string.purchase_dialog_added);
        builder.setMessage(R.string.purchase_dialog_content)
                .setPositiveButton(R.string.purchase_dialog_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WineDetailActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.purchase_dialog_cancel, null)
                .setNeutralButton(R.string.purchase_dialog_checkout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(WineDetailActivity.this, CompletePurchaseActivity.class);
                        WineDetailActivity.this.startActivity(intent);
                    }
                });
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(selectedWine);
    }

    private void updateView(final Wine wine) {
        if (wine != null) {
            TextView nameView = (TextView) findViewById(R.id.wine_name);
            TextView descriptionView = (TextView) findViewById(R.id.wine_description);
            nameView.setText(wine.getName());
            descriptionView.setText(wine.getDescription());
        }
    }
}

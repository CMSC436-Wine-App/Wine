package wine.cmsc436.wine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import parse.subclasses.Purchase;
import parse.subclasses.User;
import parse.subclasses.Restaurant;
import parse.subclasses.Review;
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
                if (selectedWine != null) {
                    Intent wineReviewsIntent = new Intent(WineDetailActivity.this, WineReviewListActivity.class);
                    wineReviewsIntent.setData(selectedWine.getUri());
                    startActivity(wineReviewsIntent);
                }
            }
        });

        final TextView averageRatingsLabel = (TextView) findViewById(R.id.averageRatingsLabel);
        final RatingBar overallRatingBar = (RatingBar) findViewById(R.id.overallRating);
        final RatingBar noseRatingBar = (RatingBar) findViewById(R.id.noseRating);
        final RatingBar colorRatingBar = (RatingBar) findViewById(R.id.colorRating);
        final RatingBar tasteRatingBar = (RatingBar) findViewById(R.id.tasteRating);
        final RatingBar finishRatingBar = (RatingBar) findViewById(R.id.finishRating);
        final TextView sweetnessLabel = (TextView) findViewById(R.id.sweetnessLabel);
        final TextView tanninsLabel = (TextView) findViewById(R.id.tanninsLabel);
        final TextView acidityLabel = (TextView) findViewById(R.id.acidityLabel);
        final TextView bodyLabel = (TextView) findViewById(R.id.bodyLabel);
        final ProgressBar sweetnessProgressBar = (ProgressBar) findViewById(R.id.sweetnessProgressBar);
        final ProgressBar tanninsProgressBar = (ProgressBar) findViewById(R.id.tanninsProgressBar);
        final ProgressBar acidityProgressBar = (ProgressBar) findViewById(R.id.acidityProgressBar);
        final ProgressBar bodyProgressBar = (ProgressBar) findViewById(R.id.bodyProgressBar);
        final TextView firstDescriptor = (TextView) findViewById(R.id.firstDescriptor);
        final TextView secondDescriptor = (TextView) findViewById(R.id.secondDescriptor);
        final TextView thirdDescriptor = (TextView) findViewById(R.id.thirdDescriptor);

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
                ParseQuery<Review> reviewsQuery = Review.getQuery();
                reviewsQuery.whereEqualTo("wine", wine);
                reviewsQuery.findInBackground(new FindCallback<Review>() {
                    @Override
                    public void done(final List<Review> reviews, ParseException e) {
                        if (e != null) {
                            Toast toast = Toast.makeText(WineDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        averageRatingsLabel.setText("Average Ratings ("+reviews.size()+")");
                        float avgOverallRating = 0;
                        float avgNoseRating = 0;
                        float avgColorRating = 0;
                        float avgTasteRating = 0;
                        float avgFinishRating = 0;
                        float avgSweetness = 0;
                        float avgTannins = 0;
                        float avgAcidity = 0;
                        float avgBody = 0;
                        Map<String, Integer> descriptorsCount = new HashMap<String, Integer>();
                        ValueComparator comparator =  new ValueComparator(descriptorsCount);
                        Map<String, Integer> sortedDescriptorsCount = new TreeMap<String, Integer>(comparator);
                        for (Review review : reviews) {
                            avgOverallRating+=review.getRating().floatValue();
                            avgNoseRating+=review.getNoseRating().floatValue();
                            avgColorRating+=review.getColorRating().floatValue();
                            avgTasteRating+=review.getTasteRating().floatValue();
                            avgFinishRating+=review.getFinishRating().floatValue();
                            avgSweetness+=review.getSweetness().floatValue();
                            avgTannins+=review.getTannins().floatValue();
                            avgAcidity+=review.getAcidity().floatValue();
                            avgBody+=review.getBody().floatValue();
                            JSONArray descriptors = review.getDescriptors();
                            if (descriptors != null) {
                                for (int i = 0; i < descriptors.length(); i++) {
                                    try {
                                        String descriptor = (String) descriptors.get(i);
                                        if (descriptorsCount.containsKey(descriptor)) {
                                            descriptorsCount.put(descriptor, descriptorsCount.get(descriptor) + 1);
                                        } else {
                                            descriptorsCount.put(descriptor, 1);
                                        }
                                    } catch (JSONException exception) {
                                        Log.i(App.APPTAG, "invalid JSONObject");
                                    }
                                }
                            }
                        }
                        if (reviews.size() > 0) {
                            // ratings
                            avgOverallRating /= reviews.size();
                            avgNoseRating /= reviews.size();
                            avgColorRating /= reviews.size();
                            avgTasteRating /= reviews.size();
                            avgFinishRating /= reviews.size();
                            overallRatingBar.setRating(avgOverallRating);
                            noseRatingBar.setRating(avgNoseRating);
                            colorRatingBar.setRating(avgColorRating);
                            tasteRatingBar.setRating(avgTasteRating);
                            finishRatingBar.setRating(avgFinishRating);
                            // profile
                            avgSweetness /= reviews.size();
                            avgTannins /= reviews.size();
                            avgAcidity /= reviews.size();
                            avgBody /= reviews.size();
                            sweetnessLabel.setText("Sweetness: "+(int)avgSweetness);
                            tanninsLabel.setText("Tannins: "+(int)avgTannins);
                            acidityLabel.setText("Acidity: "+(int)avgAcidity);
                            bodyLabel.setText("Body: "+(int)avgBody);
                            sweetnessProgressBar.setProgress((int)avgSweetness);
                            tanninsProgressBar.setProgress((int)avgTannins);
                            acidityProgressBar.setProgress((int)avgAcidity);
                            bodyProgressBar.setProgress((int)avgBody);
                            // descriptors
                            sortedDescriptorsCount.putAll(descriptorsCount);
                            List<String> sortedDescriptors = new ArrayList<String>(sortedDescriptorsCount.keySet());
                            if (sortedDescriptors.size() > 0) {
                                String descriptor = sortedDescriptors.get(0);
                                firstDescriptor.setText(descriptor + " (" + descriptorsCount.get(descriptor) + ")");
                            }
                            if (sortedDescriptors.size() > 1) {
                                String descriptor = sortedDescriptors.get(1);
                                secondDescriptor.setText(descriptor + " (" + descriptorsCount.get(descriptor) + ")");
                            }
                            if (sortedDescriptors.size() > 2) {
                                String descriptor = sortedDescriptors.get(2);
                                thirdDescriptor.setText(descriptor + " (" + descriptorsCount.get(descriptor) + ")");
                            }
                        }
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

class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

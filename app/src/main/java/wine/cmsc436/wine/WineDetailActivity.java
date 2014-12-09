package wine.cmsc436.wine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
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


public class WineDetailActivity extends BaseActivity {

    private Wine selectedWine = null;
    private static final int ADD_REVIEW_REQEST = 0;
    public static final int FB_SESSION_RESULT = 101;
    private static final int PICKER_MIN = 1;
    private static final int PICKER_MAX = 100;

    FacebookPost facebookPost;
    private UiLifecycleHelper uiHelper;
    private Intent shareData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        facebookPost = new FacebookPost(this, uiHelper);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        final Button glassOrderButton = (Button)findViewById(R.id.glass_order_btn);
        glassOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String glassPrice = glassOrderButton.getText().toString();
                glassPrice = glassPrice.substring(glassPrice.lastIndexOf(":")+3, glassPrice.length());
                showPurchaseDialog(WinePurchase.WineType.GLASS, Double.valueOf(glassPrice));
//                showPurchaseDialog();
            }
        });

        final Button bottleOrderButton = (Button)findViewById(R.id.bottle_order_btn);
        bottleOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bottlePrice = bottleOrderButton.getText().toString();
                bottlePrice = bottlePrice.substring(bottlePrice.lastIndexOf(":")+3, bottlePrice.length());
                showPurchaseDialog(WinePurchase.WineType.BOTTLE, Double.valueOf(bottlePrice));
//                showPurchaseDialog();
            }
        });

        final Button reviewButton = (Button) findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<Review> reviewsQuery = User.getCurrentUser().getReviews();
                reviewsQuery.whereEqualTo("wine", selectedWine);
                reviewsQuery.getFirstInBackground(new GetCallback<Review>() {
                    @Override
                    public void done(Review review, ParseException e) {
                        if (e != null) {
                            if (e.getCode() != 101) {
                                Toast toast = Toast.makeText(WineDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                                return;
                            } else {
                                // wine not yet reviewed
                                Intent reviewIntent = new Intent(WineDetailActivity.this, NewWineReview.class);
                                reviewIntent.putExtra("wineName", selectedWine.getName());
                                reviewIntent.setData(selectedWine.getUri());
                                startActivityForResult(reviewIntent, ADD_REVIEW_REQEST);
                            }
                        } else {
                            // wine has been reviewed
                            Toast toast = Toast.makeText(WineDetailActivity.this, "You have already reviewed this wine", Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                    }
                });
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
                updateView(selectedWine);
            }
        };

        ParseQuery<Wine> wineQuery = Wine.getQuery();
        wineQuery.fromLocalDatastore();
        wineQuery.getInBackground(wineId, wineGetCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(selectedWine);
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data){

        if (requestCode == FB_SESSION_RESULT){
            // publish permission granted
            facebookPost.showFacebookShareDialog(shareData);
        } else if (requestCode == ADD_REVIEW_REQEST){
            if (resultCode == RESULT_OK){
                shareData = data;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Review submitted.\nShare review on Facebook?")
                        .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Session session = ParseFacebookUtils.getSession();
                                if (session != null) {
                                    if (!session.isPermissionGranted("publish_actions")){
                                        // ask for publish permissions
                                        String[] permissions = {"publish_actions"};
                                        Session.NewPermissionsRequest permissionsRequest = new Session.NewPermissionsRequest(WineDetailActivity.this, Arrays.asList(permissions));
                                        permissionsRequest.setRequestCode(FB_SESSION_RESULT);
                                        session.requestNewPublishPermissions(permissionsRequest);
                                    } else {
                                        // publish permissions already granted
                                        facebookPost.showFacebookShareDialog(shareData);
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Facebook session invalid", Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent data = new Intent();
                                    setResult(RESULT_OK, data);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();
            }
        }

        // facebook request result handler
        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                boolean didComplete = FacebookDialog.getNativeDialogDidComplete(data);
                if (didComplete) {
                    String completionGesture = FacebookDialog.getNativeDialogCompletionGesture(data);
                    String postId = FacebookDialog.getNativeDialogPostId(data);
                    Log.i(App.APPTAG, completionGesture);
                    Log.i(App.APPTAG, postId);
                    if (completionGesture.equals("post")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Review shared", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });

    }

    private void addPurchase(int quantity, WinePurchase.WineType wineType, Double winePrice){
        Purchase p = new Purchase(User.getCurrentUser(), selectedWine);
        App.currentPurchases.add(new WinePurchase(p, wineType, winePrice, quantity));
    }

private void showPurchaseDialog(final WinePurchase.WineType wineType, final Double winePrice) {
    final Dialog dialog = new Dialog(WineDetailActivity.this);
    dialog.setTitle(R.string.purchase_dialog_added);
    dialog.setContentView(R.layout.dialog_purchase_number);
    Button confirmBtn = (Button) dialog.findViewById(R.id.confirmBtn);
    confirmBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
            int quantity = numberPicker.getValue();
            addPurchase(quantity, wineType, winePrice);
//            WineDetailActivity.this.finish();
            dialog.dismiss();
        }
    });
    Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
    cancelBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    });
    Button checkoutBtn = (Button) dialog.findViewById(R.id.checkoutBtn);
    checkoutBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
            int quantity = numberPicker.getValue();
            addPurchase(quantity, wineType, winePrice);
            dialog.dismiss();
            Intent intent = new Intent(WineDetailActivity.this, CompletePurchaseActivity.class);
            WineDetailActivity.this.startActivity(intent);
        }
    });
    final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
    numberPicker.setMaxValue(PICKER_MAX);
    numberPicker.setMinValue(PICKER_MIN);
    numberPicker.setWrapSelectorWheel(false);
    dialog.show();
}

//    private void showPurchaseDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(WineDetailActivity.this);
//        builder.setTitle(R.string.purchase_dialog_added);
//        builder.setMessage(R.string.purchase_dialog_content)
//                .setPositiveButton(R.string.purchase_dialog_OK, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        WineDetailActivity.this.finish();
//                    }
//                })
//                .setNegativeButton(R.string.purchase_dialog_cancel, null)
//                .setNeutralButton(R.string.purchase_dialog_checkout, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(WineDetailActivity.this, CompletePurchaseActivity.class);
//                        WineDetailActivity.this.startActivity(intent);
//                    }
//                });
//        builder.create().show();
//    }

    private void updateView(final Wine wine) {
        if (wine != null) {
            final TextView nameView = (TextView) findViewById(R.id.wine_name);
            final TextView descriptionView = (TextView) findViewById(R.id.wine_description);
            final RelativeLayout wineDescLayout = (RelativeLayout)findViewById(R.id.order_layout);
            final RelativeLayout reviewLayout = (RelativeLayout)findViewById(R.id.review_layout);
            final RelativeLayout ratingsLayout = (RelativeLayout)findViewById(R.id.ratings_layout);
            final RelativeLayout profileLayout = (RelativeLayout)findViewById(R.id.profile_layout);
            final RelativeLayout descriptorLayout = (RelativeLayout)findViewById(R.id.descriptor_layout);
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
            final Button glassOrderButton = (Button)findViewById(R.id.glass_order_btn);
            final Button bottleOrderButton = (Button)findViewById(R.id.bottle_order_btn);

            nameView.setText(wine.getName());
            descriptionView.setText(wine.getDescription());

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
                        bottleOrderButton.setText("Bottle: " + formatter.format(menuItem.getBottlePrice()));
                        glassOrderButton.setText("Glass: " + formatter.format(menuItem.getGlassPrice()));
                    } catch (IndexOutOfBoundsException ex) {
                        Log.i("MenuItem missing for Wine Id:", selectedWine.getObjectId());
                    }
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
                    ratingsLayout.setVisibility(View.VISIBLE);
                    profileLayout.setVisibility(View.VISIBLE);
                    descriptorLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
// http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
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

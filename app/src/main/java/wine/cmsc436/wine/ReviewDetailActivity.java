package wine.cmsc436.wine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import parse.subclasses.Review;
import parse.subclasses.Wine;


public class ReviewDetailActivity extends BaseActivity {

    private Wine selectedReview = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        final RatingBar overallRatingBar = (RatingBar) findViewById(R.id.overallRating);
        final RatingBar noseRatingBar = (RatingBar) findViewById(R.id.noseRating);
        final RatingBar colorRatingBar = (RatingBar) findViewById(R.id.colorRating);
        final RatingBar tasteRatingBar = (RatingBar) findViewById(R.id.tasteRating);
        final RatingBar finishRatingBar = (RatingBar) findViewById(R.id.finishRating);
        final TextView commentView = (TextView) findViewById(R.id.commentView);
        final ProgressBar sweetnessProgressBar = (ProgressBar) findViewById(R.id.sweetnessProgressBar);
        final ProgressBar tanninsProgressBar = (ProgressBar) findViewById(R.id.tanninsProgressBar);
        final ProgressBar acidityProgressBar = (ProgressBar) findViewById(R.id.acidityProgressBar);
        final ProgressBar bodyProgressBar = (ProgressBar) findViewById(R.id.bodyProgressBar);
        final LinearLayout descriptorsList = (LinearLayout) findViewById(R.id.descriptorsList);
        final LinearLayout aromasList = (LinearLayout) findViewById(R.id.aromasList);
        final LinearLayout varietalsList = (LinearLayout) findViewById(R.id.varietalsList);

        // Fetch the data about this review from Parse.
        String reviewId = Review.getObjectId(getIntent().getData());
        GetCallback<Review> reviewGetCallback = new GetCallback<Review>() {
            @Override
            public void done(Review review, ParseException e) {
                // ratings
                overallRatingBar.setRating(review.getRating().floatValue());
                noseRatingBar.setRating(review.getNoseRating().floatValue());
                colorRatingBar.setRating(review.getColorRating().floatValue());
                tasteRatingBar.setRating(review.getTasteRating().floatValue());
                finishRatingBar.setRating(review.getFinishRating().floatValue());
                // comment
                commentView.setText(review.getComment());
                // profile
                sweetnessProgressBar.setProgress(review.getSweetness().intValue());
                tanninsProgressBar.setProgress(review.getTannins().intValue());
                acidityProgressBar.setProgress(review.getAcidity().intValue());
                bodyProgressBar.setProgress(review.getBody().intValue());
                // descriptors
                JSONArray descriptors = review.getDescriptors();
                for (int i = 0; i < descriptors.length(); i++) {
                    try {
                        String descriptor = (String) descriptors.get(i);
                        TextView simpleListItem = (TextView) getLayoutInflater().inflate(R.layout.list_item_simple, null);
                        simpleListItem.setText(descriptor);
                        descriptorsList.addView(simpleListItem);
                    } catch (JSONException exception) {
                        Log.i(App.APPTAG, "invalid JSONObject");
                    }
                }
                // aromas
                JSONArray aromas = review.getAromas();
                for (int i = 0; i < aromas.length(); i++) {
                    try {
                        String aroma = (String) aromas.get(i);
                        TextView simpleListItem = (TextView) getLayoutInflater().inflate(R.layout.list_item_simple, null);
                        simpleListItem.setText(aroma);
                        aromasList.addView(simpleListItem);
                    } catch (JSONException exception) {
                        Log.i(App.APPTAG, "invalid JSONObject");
                    }
                }
                // varietals
                JSONArray varietals = review.getVarietals();
                for (int i = 0; i < varietals.length(); i++) {
                    try {
                        String varietal = (String) varietals.get(i);
                        TextView simpleListItem = (TextView) getLayoutInflater().inflate(R.layout.list_item_simple, null);
                        simpleListItem.setText(varietal);
                        varietalsList.addView(simpleListItem);
                    } catch (JSONException exception) {
                        Log.i(App.APPTAG, "invalid JSONObject");
                    }
                }
            }
        };

        ParseQuery<Review> reviewQuery = Review.getQuery();
        reviewQuery.getInBackground(reviewId, reviewGetCallback);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

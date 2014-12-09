package wine.cmsc436.wine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

import parse.subclasses.Review;
import parse.subclasses.User;

public class ReviewDetailActivity extends BaseActivity {

    private Review selectedReview = null;

    FacebookPost facebookPost;
    private UiLifecycleHelper uiHelper;

    public static final int FB_SESSION_RESULT = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        facebookPost = new FacebookPost(this, uiHelper);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        // Fetch the data about this review from Parse.
        String reviewId = Review.getObjectId(getIntent().getData());
        GetCallback<Review> reviewGetCallback = new GetCallback<Review>() {
            @Override
            public void done(Review review, ParseException e) {
                if (e != null) {
                    Toast toast = Toast.makeText(ReviewDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                selectedReview = review;
                updateView(selectedReview);
            }
        };

        ParseQuery<Review> reviewQuery = Review.getQuery();
        reviewQuery.include("user");
        reviewQuery.getInBackground(reviewId, reviewGetCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(selectedReview);
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
        if (id == R.id.action_share) {
            Session session = ParseFacebookUtils.getSession();
            if (session != null) {
                if (!session.isPermissionGranted("publish_actions")){
                    // ask for publish permissions
                    String[] permissions = {"publish_actions"};
                    Session.NewPermissionsRequest permissionsRequest = new Session.NewPermissionsRequest(this, Arrays.asList(permissions));
                    permissionsRequest.setRequestCode(FB_SESSION_RESULT);
                    session.requestNewPublishPermissions(permissionsRequest);
                } else {
                    // publish permissions already granted
                    Intent shareData = FacebookPost.createDataFromReview(selectedReview);
                    facebookPost.showFacebookShareDialog(shareData);
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Facebook session invalid", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        } else if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data){

        if (requestCode == FB_SESSION_RESULT){
            // publish permission granted
            Intent shareData = FacebookPost.createDataFromReview(selectedReview);
            facebookPost.showFacebookShareDialog(shareData);
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

    private void updateView(final Review review) {
        if (review != null) {
            final TextView userName = (TextView) findViewById(R.id.userName);
            final Button profileBtn = (Button) findViewById(R.id.profileBtn);
            final RatingBar overallRatingBar = (RatingBar) findViewById(R.id.overallRating);
            final RatingBar noseRatingBar = (RatingBar) findViewById(R.id.noseRating);
            final RatingBar colorRatingBar = (RatingBar) findViewById(R.id.colorRating);
            final RatingBar tasteRatingBar = (RatingBar) findViewById(R.id.tasteRating);
            final RatingBar finishRatingBar = (RatingBar) findViewById(R.id.finishRating);
            final TextView commentView = (TextView) findViewById(R.id.commentView);
            final TextView sweetnessLabel = (TextView) findViewById(R.id.sweetnessLabel);
            final TextView tanninsLabel = (TextView) findViewById(R.id.tanninsLabel);
            final TextView acidityLabel = (TextView) findViewById(R.id.acidityLabel);
            final TextView bodyLabel = (TextView) findViewById(R.id.bodyLabel);
            final ProgressBar sweetnessProgressBar = (ProgressBar) findViewById(R.id.sweetnessProgressBar);
            final ProgressBar tanninsProgressBar = (ProgressBar) findViewById(R.id.tanninsProgressBar);
            final ProgressBar acidityProgressBar = (ProgressBar) findViewById(R.id.acidityProgressBar);
            final ProgressBar bodyProgressBar = (ProgressBar) findViewById(R.id.bodyProgressBar);
            final LinearLayout descriptorsList = (LinearLayout) findViewById(R.id.descriptorsList);
            final LinearLayout aromasList = (LinearLayout) findViewById(R.id.aromasList);
            final LinearLayout varietalsList = (LinearLayout) findViewById(R.id.varietalsList);
            // user
            userName.setText(review.getUser().getName());
            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ReviewDetailActivity.this, UserProfile.class);
                    intent.setData(selectedReview.getUser().getUri());
                    ReviewDetailActivity.this.startActivity(intent);
                }
            });
            profileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ReviewDetailActivity.this, UserProfile.class);
                    intent.setData(selectedReview.getUser().getUri());
                    ReviewDetailActivity.this.startActivity(intent);
                }
            });
            // ratings
            overallRatingBar.setRating(review.getRating().floatValue());
            noseRatingBar.setRating(review.getNoseRating().floatValue());
            colorRatingBar.setRating(review.getColorRating().floatValue());
            tasteRatingBar.setRating(review.getTasteRating().floatValue());
            finishRatingBar.setRating(review.getFinishRating().floatValue());
            // comment
            commentView.setText(review.getComment());
            // profile
            sweetnessLabel.setText("Sweetness: "+review.getSweetness().intValue());
            tanninsLabel.setText("Tannins: "+review.getTannins().intValue());
            acidityLabel.setText("Acidity: "+review.getAcidity().intValue());
            bodyLabel.setText("Body: "+review.getBody().intValue());
            sweetnessProgressBar.setProgress(review.getSweetness().intValue());
            tanninsProgressBar.setProgress(review.getTannins().intValue());
            acidityProgressBar.setProgress(review.getAcidity().intValue());
            bodyProgressBar.setProgress(review.getBody().intValue());
            // descriptors
            JSONArray descriptors = review.getDescriptors();
            if (descriptors != null) {
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
            }
            // aromas
            JSONArray aromas = review.getAromas();
            if (aromas != null) {
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
            }
            // varietals
            JSONArray varietals = review.getVarietals();
            if (varietals != null) {
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
            final LinearLayout linearMain = (LinearLayout) findViewById(R.id.linearMain);
            linearMain.setVisibility(View.VISIBLE);
        }
    }
}

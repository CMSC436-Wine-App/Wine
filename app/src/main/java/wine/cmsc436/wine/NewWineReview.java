package wine.cmsc436.wine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import parse.subclasses.Review;
import parse.subclasses.User;
import parse.subclasses.Wine;

/**
 * Created by dylan on 11/19/14.
 */
public class NewWineReview extends BaseActivity {

    private static final int WINE_SELECT = 1;
    private static final int AROMAS_SELECT = 2;
    private static final int VARIETALS_SELECT = 3;
    private static final int PROFILE_SET = 4;
    private static final int DESCRIPTORS_SELECT = 5;

    // XML Layout Features
    TextView selectWineTxt;
    Button selectAromasBtn, selectVarietalsBtn, setProfileBtn, selectDescriptorsBtn;
    RatingBar overallRatingBar, noseRatingBar, colorRatingBar, tasteRatingBar, finishRatingBar;
    EditText description;
    Review review;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_wine_review);

        review = new Review();

        overallRatingBar = (RatingBar) findViewById(R.id.rating_rb);
        noseRatingBar = (RatingBar) findViewById(R.id.noseRating_rb);
        colorRatingBar = (RatingBar) findViewById(R.id.colorRating_rb);
        tasteRatingBar = (RatingBar) findViewById(R.id.tasteRating_rb);
        finishRatingBar = (RatingBar) findViewById(R.id.finishRating_rb);

        description = (EditText) findViewById(R.id.et_description);

        selectWineTxt = (EditText) findViewById(R.id.select_wine_txt);
        selectWineTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewWineReview.this, MenuItemListActivity.class);
                intent.putExtra("removeReviewedWines", true);
                startActivityForResult(intent, WINE_SELECT);
            }
        });

        selectAromasBtn = (Button) findViewById(R.id.select_aromas_btn);
        selectAromasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewWineReview.this, ChecklistActivity.class);
                intent.putExtra("aromas", true);
                if (review.getAromas() != null) {
                    intent.putExtra("jsonArray", review.getAromas().toString());
                }
                startActivityForResult(intent, AROMAS_SELECT);
            }
        });

        selectVarietalsBtn = (Button) findViewById(R.id.select_varietals_btn);
        selectVarietalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewWineReview.this, ChecklistActivity.class);
                intent.putExtra("varietals", true);
                if (review.getVarietals() != null) {
                    intent.putExtra("jsonArray", review.getVarietals().toString());
                }
                startActivityForResult(intent, VARIETALS_SELECT);
            }
        });

        setProfileBtn = (Button) findViewById(R.id.set_profile_btn);
        setProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewWineReview.this, ReviewWineProfileActivity.class);
                intent.putExtra("sweetness", review.getSweetness());
                intent.putExtra("tannins", review.getTannins());
                intent.putExtra("acidity", review.getAcidity());
                intent.putExtra("body", review.getBody());
                startActivityForResult(intent, PROFILE_SET);
            }
        });

        selectDescriptorsBtn = (Button) findViewById(R.id.select_descriptors_btn);
        selectDescriptorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewWineReview.this, ChecklistActivity.class);
                intent.putExtra("descriptors", true);
                if (review.getDescriptors() != null) {
                    intent.putExtra("jsonArray", review.getDescriptors().toString());
                }
                startActivityForResult(intent, DESCRIPTORS_SELECT);
            }
        });

        Intent tempIntent = getIntent();
        if (tempIntent.hasExtra("wineName") && tempIntent.getData() != null) {
            String wineId = Wine.getObjectId(tempIntent.getData());
            String wineName = tempIntent.getStringExtra("wineName");
            selectWineTxt.setText(wineName);
            review.setWine(wineId);
            ((RelativeLayout)findViewById(R.id.childRelative)).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (WINE_SELECT) : {
                if (resultCode == Activity.RESULT_OK) {
                    String wineId = Wine.getObjectId(data.getData());
                    String wineName = data.getStringExtra("wineName");
                    selectWineTxt.setText(wineName);
                    review.setWine(wineId);
                    RelativeLayout childRelative = (RelativeLayout)findViewById(R.id.childRelative);
                    if (!wineId.equals("")) {
                        childRelative.setVisibility(View.VISIBLE);
                        resetLayout();
                    }
                }
                break;
            }
            case (AROMAS_SELECT) : {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> result = data.getStringArrayListExtra("aromas");
                    review.setAromas(new JSONArray(result));
                }
                break;
            }
            case (VARIETALS_SELECT) : {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> result = data.getStringArrayListExtra("varietals");
                    review.setVarietals(new JSONArray(result));
                }
                break;
            }
            case (PROFILE_SET) : {
                if (resultCode == Activity.RESULT_OK) {
                    Integer sweetness = data.getIntExtra("sweetness", 0);
                    Integer tannins = data.getIntExtra("tannins", 0);
                    Integer acidity = data.getIntExtra("acidity", 0);
                    Integer body = data.getIntExtra("body", 0);
                    review.setSweetness(sweetness);
                    review.setTannins(tannins);
                    review.setAcidity(acidity);
                    review.setBody(body);
                }
                break;
            }
            case (DESCRIPTORS_SELECT) : {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> result = data.getStringArrayListExtra("descriptors");
                    review.setDescriptors(new JSONArray(result));
                }
                break;
            }
        }
    }

    private void resetLayout() {
        finishRatingBar.setRating(0);
        tasteRatingBar.setRating(0);
        colorRatingBar.setRating(0);
        noseRatingBar.setRating(0);
        overallRatingBar.setRating(0);
        EditText descriptionText = (EditText)findViewById(R.id.et_description);
        descriptionText.setText("");
        review.setSweetness(0);
        review.setTannins(0);
        review.setAcidity(0);
        review.setBody(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_wine_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            if (review.getWine() == null) {
                Toast.makeText(getApplicationContext(), "Must select a wine", Toast.LENGTH_LONG).show();
                return false;
            }
            ParseQuery<Review> reviewsQuery = User.getCurrentUser().getReviews();
            reviewsQuery.whereEqualTo("wine", review.getWine());
            reviewsQuery.getFirstInBackground(new GetCallback<Review>() {
                @Override
                public void done(Review existingReview, ParseException e) {
                    if (e != null) {
                        if (e.getCode() != 101) {
                            Toast toast = Toast.makeText(NewWineReview.this, e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        } else {
                            // wine not yet reviewed
                            final User user = User.getCurrentUser();
                            review.setComment(description.getText().toString());
                            review.setRating(overallRatingBar.getRating());
                            review.setNoseRating(noseRatingBar.getRating());
                            review.setColorRating(colorRatingBar.getRating());
                            review.setTasteRating(tasteRatingBar.getRating());
                            review.setFinishRating(finishRatingBar.getRating());
                            review.setUser(user);
                            review.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                                        toast.show();
                                        return;
                                    }
                                    user.addReview(review);
                                    user.addReviewedWine(review.getWine());
                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                                                toast.show();
                                                return;
                                            }
                                            Intent data = FacebookPost.createDataFromReview(review);
                                            setResult(RESULT_OK, data);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    } else {
                        // wine has been reviewed
                        Toast toast = Toast.makeText(NewWineReview.this, "You have already reviewed this wine", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

package wine.cmsc436.wine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;

import parse.subclasses.Review;
import parse.subclasses.User;
import parse.subclasses.Wine;

/**
 * Created by dylan on 11/19/14.
 */
public class NewWineReview extends BaseActivity {

    // Local Vars
    private static final String TAG = "CMSC436-Wine-App";
    private String name = "";
    private float rating = 0.0f;
    private ProgressDialog pDialog;

    private static final int WINE_SELECT = 1;
    private static final int AROMAS_SELECT = 2;
    private static final int VARIETALS_SELECT = 3;
    private static final int PROFILE_SET = 4;


    // XML Layout Features
    Button selectWineBtn, selectAromasBtn, selectVarietalsBtn, setProfileBtn;
    RatingBar rbar;
    EditText description;
    Review review;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_wine_review);

        review = new Review();

        rbar = (RatingBar) findViewById(R.id.rb_wine_rating);
        description = (EditText) findViewById(R.id.et_description);

        selectWineBtn = (Button) findViewById(R.id.select_wine_btn);
        selectWineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewWineReview.this, WineListActivity.class);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (WINE_SELECT) : {
                if (resultCode == Activity.RESULT_OK) {
                    String wineId = Wine.getObjectId(data.getData());
                    String wineName = data.getStringExtra("wineName");
                    review.setWine(wineId);
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
        }
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

            final User user = User.getCurrentUser();
            review.setComment(description.getText().toString());
            review.setRating(rbar.getRating());
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
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        e.getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                                return;
                            }
                            Intent data = new Intent();
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    });
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private class GenerateWineReview extends AsyncTask<String, Void, Void>{
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            // Create a progressbar
//            pDialog = new ProgressDialog(NewWineReview.this);
//            // Set progressbar title
//            pDialog.setTitle("Thanks For the Review, Syncing with database...");
//            // Set progressbar message
//            pDialog.setMessage("Syncing...");
//            pDialog.setIndeterminate(false);
//            // Show progressbar
//            pDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//
//            try {
//                // Check that wine is listed & other database information
//
//
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            // Packaging the Review
//            Intent data = new Intent();
//            WineReviewItem.packageIntent(data, wine_name.getText().toString(),
//                    description.getText().toString(),
//                    restaurant_name.getText().toString(),
//                    String.valueOf(rbar.getRating()));
//
//            // Setting result and finishing
//            setResult(RESULT_OK, data);
//            finish();
//            pDialog.dismiss();
//
//        }
//    }

}

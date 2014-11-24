package wine.cmsc436.wine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

/**
 * Created by dylan on 11/19/14.
 */
public class NewWineReview extends Activity {

    // Local Vars
    private static final String TAG = "CMSC436-Wine-App";
    private String name = "";
    private float rating = 0.0f;
    private ProgressDialog pDialog;


    // XML Layout Features
    Button submit;
    RatingBar rbar;
    EditText wine_name, description, restaurant_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wine_review);

        submit = (Button) findViewById(R.id.b_submit_review);

        submit.setOnClickListener(new View.OnClickListener() {
            //Log.i(TAG, "Entered submitButton.OnClickListener.onClick()");


            @Override
            public void onClick(View v) {

                // Linking Java <-- XML
                rbar = (RatingBar) findViewById(R.id.rb_wine_rating);
                wine_name = (EditText) findViewById(R.id.et_wine_name);
                description = (EditText) findViewById(R.id.et_description);
                restaurant_name = (EditText) findViewById(R.id.et_restaurant_name);

                new GenerateWineReview().execute();
            }
        });


    }

    private class GenerateWineReview extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Create a progressbar
            pDialog = new ProgressDialog(NewWineReview.this);
            // Set progressbar title
            pDialog.setTitle("Thanks For the Review, Syncing with database...");
            // Set progressbar message
            pDialog.setMessage("Syncing...");
            pDialog.setIndeterminate(false);
            // Show progressbar
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                // Check that wine is listed & other database information


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Packaging the Review
            Intent data = new Intent();
            WineReviewItem.packageIntent(data, wine_name.getText().toString(),
                    description.getText().toString(),
                    restaurant_name.getText().toString(),
                    String.valueOf(rbar.getRating()));

            // Setting result and finishing
            setResult(RESULT_OK, data);
            finish();
            pDialog.dismiss();

        }
    }

}

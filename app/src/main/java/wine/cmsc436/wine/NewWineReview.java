package wine.cmsc436.wine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
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

    // XML Layout Features
    Button submit;
    RatingBar rbar;
    EditText wine_name, description, restaurant_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wine_review);

        // Linking Java <-- XML
        submit = (Button) findViewById(R.id.b_submit_review);
        rbar = (RatingBar) findViewById(R.id.rb_wine_rating);
        wine_name = (EditText) findViewById(R.id.et_wine_name);
        description = (EditText) findViewById(R.id.et_description);
        restaurant_name = (EditText) findViewById(R.id.et_restaurant_name);




    }
}

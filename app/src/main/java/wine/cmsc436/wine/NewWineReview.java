package wine.cmsc436.wine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * Created by dylan on 11/19/14.
 */
public class NewWineReview extends Activity {

    // Local Vars
    private static final String TAG = "CMSC436-Wine-App";
    private String name = "";
    private float rating = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wine_review);



    }
}

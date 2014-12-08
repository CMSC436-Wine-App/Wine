package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import parse.subclasses.Badge;
import parse.subclasses.BadgeDiscount;
import parse.subclasses.Wine;

/**
 * Created by Adam on 12/7/2014.
 */
public class DiscountOverviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_overview);

        String badgeId = Badge.getObjectId(getIntent().getData());
        String wineName = getIntent().getStringExtra("wineName");
        String badgeName = getIntent().getStringExtra("badgeName");
        double discountRate = getIntent().getDoubleExtra("discountRate", 0);

        TextView wineTV = (TextView)findViewById(R.id.discount_wine_name);
        wineTV.setText(getString(R.string.discount_overview_wine_name, wineName));

        TextView bnTV = (TextView)findViewById(R.id.discount_badge_name);
        bnTV.setText(getString(R.string.discount_overview_badge_name, badgeName));

        TextView drTV = (TextView)findViewById(R.id.discount_badge_rate);
        drTV.setText(getString(R.string.discount_overview_rate, discountRate*100.0));

//        final ParseImageView badgeImage = (ParseImageView)findViewById(R.id.discount_badge_image);
        final ImageView badgeImage = (ImageView)findViewById(R.id.discount_badge_image);

        GetCallback<Badge> badgeGetCallback = new GetCallback<Badge>() {
            @Override
            public void done(Badge badge, ParseException e) {
                if (e == null) {
                    ParseFile imageFile = badge.getPhoto();
//                    if (imageFile != null) {
//                        badgeImage.setParseFile(imageFile);
//                        badgeImage.loadInBackground();
//                    }
                    if (imageFile != null) {
                        Picasso.with(DiscountOverviewActivity.this)
                                .load(imageFile.getUrl())
                                .into(badgeImage);
                    }
                }
                else {
                    Log.i(App.APPTAG, "Error grabbing badge image: " + e.getMessage());
                }
            }
        };

        ParseQuery<Badge> badgeQuery = Badge.getQuery();
        badgeQuery.getInBackground(badgeId, badgeGetCallback);
    }

}

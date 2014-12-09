package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import parse.subclasses.Restaurant;
import parse.subclasses.User;
import parse.subclasses.Wine;

public class WineActivity extends BaseActivity {

    // public Button your_profile, find_bar, review_wine, badges, view_menu, leave_bar, order_food;
    public ImageButton badges, view_menu, order_food;
    private static final String TAG = "CMSC436-Wine-App";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
//        ParseUser.logOut();


        // Profile pic and button ------------------------------------------------------------------

        User user = User.getCurrentUser();

//        ParseImageView photoImageView = (ParseImageView) findViewById(R.id.iv_user_profile_pic);
//        ParseFile imageFile = user.getPhoto();
//        if (imageFile != null) {
//            photoImageView.setParseFile(imageFile);
//            photoImageView.loadInBackground();
//        }
        ImageView photoImageView = (ImageView) findViewById(R.id.iv_user_profile_pic);
        ParseFile imageFile = user.getPhoto();
        if (imageFile != null) {
            Picasso.with(this)
                    .load(imageFile.getUrl())
                    .into(photoImageView);
        }

        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered your_profile.OnClickListener.onClick()");

                Intent profile_intent = new Intent(WineActivity.this, UserProfile.class);
                profile_intent.setData(User.getCurrentUser().getUri());
                WineActivity.this.startActivity(profile_intent);
            }
        });


//        your_profile = (Button) findViewById(R.id.b_your_profile);
//        your_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            Log.i(TAG, "Entered your_profile.OnClickListener.onClick()");
//
//            Intent profile_intent = new Intent(WineActivity.this, UserProfile.class);
//            WineActivity.this.startActivity(profile_intent);
//            }
//        });

        // Badges ------------------------------------------------------------------

        badges = (ImageButton) findViewById(R.id.b_badges);
        badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WineActivity.this, BadgeListActivity.class);
                WineActivity.this.startActivity(intent);
            }
        });


        // Menu ------------------------------------------------------------------

        view_menu = (ImageButton) findViewById(R.id.b_view_menu);
        view_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WineActivity.this, MenuItemListActivity.class);
                Restaurant restaurant = ParseObject.createWithoutData(Restaurant.class, "rSUTXnLDFR");
                intent.setData(restaurant.getUri());
                WineActivity.this.startActivity(intent);
            }
        });

        // Order ------------------------------------------------------------------

        order_food = (ImageButton) findViewById(R.id.b_order_food);
        order_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WineActivity.this, CompletePurchaseActivity.class);
                WineActivity.this.startActivity(intent);
            }
        });


    }
}

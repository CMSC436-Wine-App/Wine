package wine.cmsc436.wine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.ParseObject;

import parse.subclasses.Restaurant;

public class WineActivity extends BaseActivity {

    public Button your_profile, find_bar, review_wine, badges, view_menu, leave_bar, order_wine;
    private static final String TAG = "CMSC436-Wine-App";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
//        ParseUser.logOut();
        your_profile = (Button) findViewById(R.id.b_your_profile);
        your_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.i(TAG, "Entered your_profile.OnClickListener.onClick()");

            Intent profile_intent = new Intent(WineActivity.this, UserProfile.class);
            WineActivity.this.startActivity(profile_intent);
            }
        });

        badges = (Button) findViewById(R.id.b_badges);
        badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WineActivity.this, BadgeListActivity.class);
                WineActivity.this.startActivity(intent);
            }
        });

        view_menu = (Button) findViewById(R.id.b_view_menu);
        view_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WineActivity.this, MenuItemListActivity.class);
                Restaurant restaurant = ParseObject.createWithoutData(Restaurant.class, App.RestaurantID);
                intent.setData(restaurant.getUri());
                WineActivity.this.startActivity(intent);
            }
        });

        order_wine = (Button)findViewById(R.id.b_order_wine);
        order_wine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WineActivity.this, CompletePurchaseActivity.class);
                WineActivity.this.startActivity(intent);
            }
        });

    }
}

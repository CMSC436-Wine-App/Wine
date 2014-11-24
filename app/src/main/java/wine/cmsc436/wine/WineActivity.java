package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import parse.subclasses.Wine;

public class WineActivity extends Activity {

    public Button your_profile, find_bar, review_wine, badges, view_menu, leave_bar, order_food;
    private static final String TAG = "CMSC436-Wine-App";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        find_bar = (Button) findViewById(R.id.b_find_bar);
        find_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WineActivity.this, FindBarActivity.class);
                WineActivity.this.startActivity(intent);
            }
        });

        review_wine = (Button) findViewById(R.id.b_review_a_wine);
        review_wine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WineActivity.this, WineListActivity.class);
                WineActivity.this.startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

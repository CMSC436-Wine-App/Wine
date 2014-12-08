package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import parse.subclasses.Badge;
import parse.subclasses.User;
import parse.subclasses.UserBadge;
import parse.subclasses.Wine;


public class UserBadgeAttrActivity extends Activity {

    UserBadgeAttrListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_badge_attr);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new UserBadgeAttrListAdapter(this);

        ListView lv = (ListView) findViewById(R.id.user_badge_attr_content);
        lv.setAdapter(mAdapter);

        String ubId = UserBadge.getObjectId(getIntent().getData());
        setTitle(getIntent().getStringExtra("title"));


        GetCallback<UserBadge> ubGetCallback = new GetCallback<UserBadge>() {
            @Override
            public void done(UserBadge u, ParseException e) {
                if (e == null) {
                    Badge b = u.getBadge();
                    if (b.getType().equals(App.UBadgeType.WinePurchase.toString())) {
                        ParseQuery<UserBadge> badgeQuery = UserBadge.getQuery();
                        badgeQuery.whereEqualTo("badge", u.getBadge());
                        badgeQuery.whereEqualTo("user", u.getUser());
                        badgeQuery.whereEqualTo("used", false);
                        badgeQuery.findInBackground(new FindCallback<UserBadge>() {
                            @Override
                            public void done(List<UserBadge> userBadges, ParseException e) {
                                for (int i = 0; i < userBadges.size(); i++) {
                                    mAdapter.add(userBadges.get(i).getWine());
                                }
                            }
                        });
                    }
                } else {
                    Log.i(App.APPTAG, "Error grabbing wineInfo: " + e.getMessage());
                }
            }
        };

        ParseQuery<UserBadge> ubQuery = UserBadge.getQuery();
        ubQuery.getInBackground(ubId, ubGetCallback);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_badge_attr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

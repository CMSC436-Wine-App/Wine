package wine.cmsc436.wine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import parse.subclasses.MenuItem;
import parse.subclasses.Purchase;
import parse.subclasses.PurchaseHistory;
import parse.subclasses.Restaurant;
import parse.subclasses.User;
import parse.subclasses.UserBadge;


public class BadgeListActivity extends BaseActivity {

    private BaseAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean isUserBadgeList = getIntent().getBooleanExtra("isUser", false);

        ListView list = (ListView) findViewById(R.id.menu_item_list);

        if (isUserBadgeList) {
            String user = User.getObjectId(getIntent().getData());
            adapter = new UserBadgeListAdapter(this);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserBadge ub = (UserBadge)adapter.getItem(position);
                    Intent intent = new Intent(BadgeListActivity.this, UserBadgeAttrActivity.class);
                    if (ub.getType().equals(App.UBadgeType.WinePurchase.toString())) {
                        intent.putExtra("title", "Wines Discounted");
                    }
                    else if (ub.getType().equals(App.UBadgeType.WineReview.toString())) {
                        intent.putExtra("title", "Wines Reviewed");
                    }
                    else if (ub.getType().equals(App.UBadgeType.PurchaseCount.toString())) {
                        intent.putExtra("title", "Wines Purchased");
                    }
                    intent.setData(ub.getUri());
                    startActivity(intent);
                }
            });

            GetCallback<ParseUser> userGetCallback = new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser u, ParseException e) {
                    if (e == null) {
                        ParseQuery<UserBadge> ubQuery = UserBadge.getQuery();
                        ubQuery.whereEqualTo("user", u);

                        ubQuery.findInBackground(new FindCallback<UserBadge>() {
                            @Override
                            public void done(List<UserBadge> userBadges, ParseException e) {
                                for (int i = 0; i < userBadges.size(); i++) {
                                    ((UserBadgeListAdapter) adapter).add(userBadges.get(i));
                                    Log.i("ASDF", "adding");
                                }
                            }
                        });
                    }
                    else {
                        Log.i(App.APPTAG, "Error grabbing purchaseHistory: " + e.getMessage());
                    }
                }
            };

            ParseQuery<ParseUser> userQuery = User.getQuery();
            userQuery.getInBackground(user, userGetCallback);
        }
        else {
            adapter = new BadgeListAdapter(this);
        }

        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}

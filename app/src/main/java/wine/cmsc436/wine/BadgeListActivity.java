package wine.cmsc436.wine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import parse.subclasses.Badge;
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
            setTitle(getIntent().getStringExtra("title"));
            String user = User.getObjectId(getIntent().getData());

            adapter = new UserBadgeListAdapter(this);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SepOrUserBadge s = (SepOrUserBadge) adapter.getItem(position);
                    if (!s.isSeperator()) {
                        UserBadge ub = s.getUserBadge();

                        Badge b = ub.getBadge();
                        Intent intent = new Intent(BadgeListActivity.this, UserBadgeAttrActivity.class);
                        if (b.getType().equals(App.UBadgeType.WinePurchase.toString())) {
                            intent.putExtra("title", "Wines To Be Discounted");
                        } else if (b.getType().equals(App.UBadgeType.WineReview.toString())) {
                            intent.putExtra("title", "Wines Reviewed");
                        } else if (b.getType().equals(App.UBadgeType.PurchaseCount.toString())) {
                            intent.putExtra("title", "Wines Purchased");
                        }
                        intent.setData(ub.getUri());
                        startActivity(intent);
                    }
                }
            });

            GetCallback<ParseUser> userGetCallback = new GetCallback<ParseUser>() {
                @Override
                public void done(final ParseUser u, ParseException e) {
                    if (e == null) {
                        final ArrayList<SepOrUserBadge> winePurchases = new ArrayList<SepOrUserBadge>();
                        winePurchases.add(new SepOrUserBadge("Wine Purchases"));

                        final ArrayList<SepOrUserBadge> wineReviews = new ArrayList<SepOrUserBadge>();
                        wineReviews.add(new SepOrUserBadge("Wine Reviews"));

                        final ArrayList<SepOrUserBadge> purchaseCount = new ArrayList<SepOrUserBadge>();
                        purchaseCount.add(new SepOrUserBadge("Purchase Count"));

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final ParseQuery<UserBadge> ubQuery = UserBadge.getQuery();
                                    ubQuery.whereEqualTo("user", u);
                                    List<UserBadge> userBadges = ubQuery.find();
                                    Log.i("ASDF", String.valueOf(userBadges.size()));

                                    for (int i = 0; i < userBadges.size(); i++) {
                                        Log.i("ASDF", String.valueOf(userBadges.size()));
                                        String type = userBadges.get(i).getBadge().getType();
                                        if (type.equals(App.UBadgeType.PurchaseCount.toString()))
                                            purchaseCount.add(new SepOrUserBadge(userBadges.get(i)));
                                        else if (type.equals(App.UBadgeType.WinePurchase.toString())) {
                                            if (!userBadges.get(i).isUsed())
                                                winePurchases.add(new SepOrUserBadge(userBadges.get(i)));
                                        } else if (type.equals(App.UBadgeType.WineReview.toString()))
                                            wineReviews.add(new SepOrUserBadge(userBadges.get(i)));
                                    }

                                    BadgeListActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < winePurchases.size(); i++) {
                                                ((UserBadgeListAdapter) adapter).add(winePurchases.get(i));
                                            }

                                            for (int i = 0; i < wineReviews.size(); i++) {
                                                ((UserBadgeListAdapter) adapter).add(wineReviews.get(i));
                                            }

                                            for (int i = 0; i < purchaseCount.size(); i++) {
                                                ((UserBadgeListAdapter) adapter).add(purchaseCount.get(i));
                                            }

                                        }
                                    });
                                } catch (ParseException e) {
                                    Log.i(App.APPTAG, e.getMessage());
                                }
                            }
                        }).start();

                    } else {
                        Log.i(App.APPTAG, "Error grabbing purchaseHistory: " + e.getMessage());
                    }
                }
            };

            ParseQuery<ParseUser> userQuery = User.getQuery();
            userQuery.getInBackground(user, userGetCallback);
        } else {
            adapter = new BadgeListAdapter(this);
        }

        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.List;

import parse.subclasses.Badge;
import parse.subclasses.BadgeDiscount;
import parse.subclasses.Purchase;
import parse.subclasses.PurchaseHistory;


public class PurchaseHistoryOverviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history_overview);

        String phId = PurchaseHistory.getObjectId(getIntent().getData());

        ListView lv = (ListView)findViewById(R.id.purchase_history_overview_content);
        final PurchaseHistoryOverviewListAdapter mAdapter = new PurchaseHistoryOverviewListAdapter(this);
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Purchase p = (Purchase) mAdapter.getItem(position);
                if (p.getDiscountApplied()) {
                    Intent intent = new Intent(PurchaseHistoryOverviewActivity.this, DiscountOverviewActivity.class);
                    BadgeDiscount bd = p.getBadgeDiscount();
                    intent.setData(bd.getBadge().getUri());
                    intent.putExtra("discountRate", bd.getDiscountRate());
                    intent.putExtra("wineName", p.getWine().getName());
                    intent.putExtra("badgeName", bd.getBadge().getName());
                    startActivity(intent);
                } else {
                    Toast.makeText(PurchaseHistoryOverviewActivity.this, R.string.discount_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        GetCallback<PurchaseHistory> phGetCallback = new GetCallback<PurchaseHistory>() {
            @Override
            public void done(PurchaseHistory ph, ParseException e) {
                if (e == null) {
                    ParseQuery<Purchase> purchaseListQuery = Purchase.getPurchaseList(ph);
                    purchaseListQuery.findInBackground(new FindCallback<Purchase>() {
                        @Override
                        public void done(List<Purchase> purchases, ParseException e) {
                            for (int i = 0; i < purchases.size(); i++) {
                                mAdapter.add(purchases.get(i));
                            }
                        }
                    });
                }
                else {
                    Log.i(App.APPTAG, "Error grabbing purchaseHistory: " + e.getMessage());
                }
            }
        };

        ParseQuery<PurchaseHistory> phQuery = PurchaseHistory.getQuery();
        phQuery.getInBackground(phId, phGetCallback);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_purchase_history_overview, menu);
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

        return super.onOptionsItemSelected(item);
    }
}

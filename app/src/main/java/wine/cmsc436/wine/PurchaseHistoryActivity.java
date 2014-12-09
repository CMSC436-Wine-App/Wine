package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import parse.subclasses.PurchaseHistory;
import parse.subclasses.User;


public class PurchaseHistoryActivity extends BaseActivity {

    PurchaseHistoryListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new PurchaseHistoryListAdapter(this, User.getCurrentUser());
        ListView lv = (ListView)findViewById(R.id.purchase_history_content);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PurchaseHistory ph = mAdapter.getItem(position);
                Intent intent = new Intent(PurchaseHistoryActivity.this, PurchaseHistoryOverviewActivity.class);
                intent.setData(ph.getUri());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

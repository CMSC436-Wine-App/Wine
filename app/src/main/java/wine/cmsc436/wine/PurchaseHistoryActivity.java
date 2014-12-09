package wine.cmsc436.wine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import parse.subclasses.PurchaseHistory;
import parse.subclasses.User;


public class PurchaseHistoryActivity extends BaseActivity {

    PurchaseHistoryListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String userId = User.getObjectId(getIntent().getData());
        GetCallback<ParseUser> userGetCallback = new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != User.getCurrentUser()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseHistoryActivity.this);
                    builder.setTitle(R.string.purchase_history_invalid);
                    builder.setMessage(getString(R.string.purchase_history_invalid_body, App.currentPurchases.getTotal()))
                            .setPositiveButton(R.string.purchase_dialog_OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    builder.create().show();
                }
                else {
                    mAdapter = new PurchaseHistoryListAdapter(PurchaseHistoryActivity.this, User.getCurrentUser());
                    ListView lv = (ListView) findViewById(R.id.purchase_history_content);
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
            }
        };
        ParseQuery<ParseUser> userQuery = User.getQuery();
        userQuery.getInBackground(userId, userGetCallback);
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

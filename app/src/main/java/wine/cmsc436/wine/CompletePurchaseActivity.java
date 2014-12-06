package wine.cmsc436.wine;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import parse.subclasses.MenuItem;
import parse.subclasses.Purchase;
import parse.subclasses.MenuItem;


public class CompletePurchaseActivity extends Activity {

    private static PurchaseListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_purchase);
        listAdapter = new PurchaseListAdapter(getApplicationContext());
        ((ListView)findViewById(R.id.purchase_content)).setAdapter(listAdapter);

        Button checkout = (Button)findViewById(R.id.purchase_checkout);

        // TODO: When pressing on a list item, show dialog to delete
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Checkout, display DialogBox showing achieved badges.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete_purchase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove_all_purchases) {
            listAdapter.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import parse.subclasses.Wine;


public class MenuItemListActivity extends BaseActivity {

    private static final int OBJECTS_PER_PAGE = 10;

    private MenuItemListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get adapter
        adapter = new MenuItemListAdapter(this);
        adapter.setObjectsPerPage(OBJECTS_PER_PAGE);
        ListView list = (ListView) findViewById(R.id.wine_list);
        list.setAdapter(adapter);

        // get wines the normal way
//        Wine.getQuery().findInBackground(new FindCallback<Wine>() {
//            @Override
//            public void done(List<Wine> wines, ParseException e) {
//                if (e != null) {
//                    Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
//                    toast.show();
//                    return;
//                }
//
//                if (wines != null) {
//                    adapter.addAll(wines);
//                }
//
//            }
//        });

        // wine detail view
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Wine wine = adapter.getItem(position);
                if (getCallingActivity() == null) {
                    Intent intent = new Intent(MenuItemListActivity.this, WineDetailActivity.class);
                    intent.setData(wine.getUri());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setData(wine.getUri());
                    intent.putExtra("wineName", wine.getName());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}

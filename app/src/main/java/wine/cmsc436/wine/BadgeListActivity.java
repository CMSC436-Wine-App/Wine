package wine.cmsc436.wine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import parse.subclasses.MenuItem;
import parse.subclasses.Restaurant;


public class BadgeListActivity extends BaseActivity {

    private BadgeListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_list);

        // get adapter
        adapter = new BadgeListAdapter(this);
        ListView list = (ListView) findViewById(R.id.menu_item_list);
        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}

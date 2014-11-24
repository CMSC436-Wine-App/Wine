package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import parse.subclasses.MenuItem;
import parse.subclasses.Restaurant;
import parse.subclasses.Wine;


public class MenuItemListActivity extends BaseActivity {

    private MenuItemListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_list);

        String restaurantId = Restaurant.getObjectId(getIntent().getData());

        // get adapter
        adapter = new MenuItemListAdapter(this, restaurantId);
        ListView list = (ListView) findViewById(R.id.menu_item_list);
        list.setAdapter(adapter);

        // menu item detail view
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MenuItem menuItem = adapter.getItem(position);
                // go to wine detail view if it is a wine menu item
                if (menuItem.isWine()) {
                    Intent intent = new Intent(MenuItemListActivity.this, WineDetailActivity.class);
                    intent.setData(menuItem.getWine().getUri());
                    startActivity(intent);
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

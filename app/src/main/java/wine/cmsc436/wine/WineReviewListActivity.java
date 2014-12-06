package wine.cmsc436.wine;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import parse.subclasses.Wine;


public class WineReviewListActivity extends BaseActivity {

    private static final int OBJECTS_PER_PAGE = 10;
    private WineReviewListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_review_list);

        String wineId = Wine.getObjectId(getIntent().getData());

        // get reviews for wine
        adapter = new WineReviewListAdapter(this, wineId);
        adapter.setObjectsPerPage(OBJECTS_PER_PAGE);
        ListView list = (ListView) findViewById(R.id.review_list);
        list.setAdapter(adapter);
    }
}

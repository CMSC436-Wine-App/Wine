package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import parse.subclasses.Wine;


public class WineDetailActivity extends ActionBarActivity {

    private Wine selectedWine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_detail);

        // Fetch the data about this wine from Parse.
        String wineId = Wine.getObjectId(getIntent().getData());
        GetCallback<Wine> wineGetCallback = new GetCallback<Wine>() {
            @Override
            public void done(Wine wine, ParseException e) {
                if (e != null) {
                    Toast toast = Toast.makeText(WineDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                selectedWine = wine;
                updateView(selectedWine);
            }
        };

        ParseQuery<Wine> wineQuery = Wine.getQuery();
        wineQuery.fromLocalDatastore();
        wineQuery.getInBackground(wineId, wineGetCallback);

        Button reviewButton = (Button)findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedWine != null) {
                    Intent reviewIntent = new Intent(WineDetailActivity.this, NewWineReview.class);
                    reviewIntent.putExtra("wineName", selectedWine.getName());
                    reviewIntent.setData(selectedWine.getUri());
                    startActivity(reviewIntent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(selectedWine);
    }

    private void updateView(final Wine wine) {
        if (wine != null) {
            TextView nameView = (TextView) findViewById(R.id.wine_name);
            TextView descriptionView = (TextView) findViewById(R.id.wine_description);
            nameView.setText(wine.getName());
            descriptionView.setText(wine.getDescription());
        }
    }
}

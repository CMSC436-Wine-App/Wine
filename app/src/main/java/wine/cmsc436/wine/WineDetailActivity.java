package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.NumberFormat;
import java.util.List;

import parse.subclasses.Restaurant;
import parse.subclasses.Wine;
import parse.subclasses.MenuItem;


public class WineDetailActivity extends ActionBarActivity {

    private Wine selectedWine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_detail);
        final Button glassOrderButton = (Button)findViewById(R.id.glass_order_btn);
        glassOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final Button bottleOrderButton = (Button)findViewById(R.id.bottle_order_btn);
        bottleOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final Button reviewButton = (Button)findViewById(R.id.review_button);
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
                ParseQuery<MenuItem> menuItemParseQuery = MenuItem.getPriceQuery(selectedWine);
                final RelativeLayout wineDescLayout = (RelativeLayout)findViewById(R.id.order_layout);
                menuItemParseQuery.findInBackground(new FindCallback<MenuItem>() {
                    @Override
                    public void done(List<MenuItem> menuItems, ParseException e) {
                        if (e != null) {
                            Toast toast = Toast.makeText(WineDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        try {
                            MenuItem menuItem = menuItems.get(0);
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            bottleOrderButton.setText(bottleOrderButton.getText() + " " + formatter.format(menuItem.getBottlePrice()));
                            glassOrderButton.setText(glassOrderButton.getText() + " " + formatter.format(menuItem.getGlassPrice()));
                        } catch (IndexOutOfBoundsException ex) {
                            Log.i("MenuItem missing for Wine Id:", selectedWine.getObjectId());
                        }
                        updateView(selectedWine);
                        wineDescLayout.setVisibility(View.VISIBLE);
                        reviewButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        ParseQuery<Wine> wineQuery = Wine.getQuery();
        wineQuery.fromLocalDatastore();
        wineQuery.getInBackground(wineId, wineGetCallback);
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

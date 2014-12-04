package wine.cmsc436.wine;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.List;

import parse.subclasses.*;

/**
 * Created by Ethan on 11/15/2014.
 */
public class App extends Application {

    // Debugging switch
    public static final boolean APPDEBUG = true;

    // Debugging tag for the application
    public static final String APPTAG = "CMSC436-Wine-App";

    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE = "searchDistance";

    private static final float DEFAULT_SEARCH_DISTANCE = 1000.0f;

    private static SharedPreferences preferences;

    private static ConfigHelper configHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(parse.subclasses.Badge.class);
        ParseObject.registerSubclass(parse.subclasses.MenuItem.class);
        ParseObject.registerSubclass(parse.subclasses.Purchase.class);
        ParseObject.registerSubclass(parse.subclasses.Restaurant.class);
        ParseObject.registerSubclass(parse.subclasses.Review.class);
        ParseObject.registerSubclass(parse.subclasses.User.class);
        ParseObject.registerSubclass(parse.subclasses.Wine.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret));
        preferences = getSharedPreferences("wine.cmsc436", Context.MODE_PRIVATE);
        configHelper = new ConfigHelper();
        configHelper.fetchConfigIfNeeded();
        // local storage of all wines
        ParseQuery<Wine> wineQuery = Wine.getQuery();
        // Query for new results from the network.
        wineQuery.findInBackground(new FindCallback<Wine>() {
            public void done(List<Wine> newWines, ParseException e) {
                if (e != null) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                final List<Wine> wines = newWines;
                // Remove the previously cached results.
                ParseObject.unpinAllInBackground("wines", new DeleteCallback() {
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        // Cache the new results.
                        ParseObject.pinAllInBackground("wines", wines);
                    }
                });
            }
        });

    }

    public static float getSearchDistance() {
        return preferences.getFloat(KEY_SEARCH_DISTANCE, DEFAULT_SEARCH_DISTANCE);
    }
    public static void setSearchDistance(float value) {
        preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
    }

    public static ConfigHelper getConfigHelper() {
        return configHelper;
    }
}

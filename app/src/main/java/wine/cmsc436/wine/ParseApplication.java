package wine.cmsc436.wine;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import parse.subclasses.*;

/**
 * Created by Ethan on 11/15/2014.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret));
        ParseObject.registerSubclass(parse.subclasses.Badge.class);
        ParseObject.registerSubclass(parse.subclasses.MenuItem.class);
        ParseObject.registerSubclass(parse.subclasses.Purchase.class);
        ParseObject.registerSubclass(parse.subclasses.Restaurant.class);
        ParseObject.registerSubclass(parse.subclasses.Review.class);
        ParseObject.registerSubclass(parse.subclasses.User.class);
        ParseObject.registerSubclass(parse.subclasses.Wine.class);
    }
}

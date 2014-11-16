package wine.cmsc436.wine;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;

import parse.subclasses.*;

/**
 * Created by Ethan on 11/15/2014.
 */
public class ParseApplication extends Application {

    private static final String PARSE_APP_ID = "VdUdTZEhM9vVNRX9Y2fUzZTZXNGkT0cMxkonazJJ";
    private static final String PARSE_CLIENT_ID = "6VgZMJUF6uD6WRJpEfB3cNINZUj21rgoqL1g88Zo";
    private static final String FB_APP_ID = "1547340548843891";
    private static final String FB_SECRET = "4ed95eba8f272219152525c578731d19";
    private static final String TW_KEY = "EHSoFY38bnZpmpKlvCr4sLBno";
    private static final String TW_SECRET = "JjFtxgETOawt60QGEEkjPSretNSzUJqreLSrG4DbUAy4425I8s";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_ID);
        ParseFacebookUtils.initialize(FB_APP_ID);
        ParseTwitterUtils.initialize(TW_KEY, TW_SECRET);
        ParseObject.registerSubclass(parse.subclasses.Badge.class);
        ParseObject.registerSubclass(parse.subclasses.MenuItem.class);
        ParseObject.registerSubclass(parse.subclasses.Purchase.class);
        ParseObject.registerSubclass(parse.subclasses.Restaurant.class);
        ParseObject.registerSubclass(parse.subclasses.Review.class);
        ParseObject.registerSubclass(parse.subclasses.User.class);
        ParseObject.registerSubclass(parse.subclasses.Wine.class);
    }
}

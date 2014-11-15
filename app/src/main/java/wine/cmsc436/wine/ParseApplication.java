package wine.cmsc436.wine;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Ethan on 11/15/2014.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "VdUdTZEhM9vVNRX9Y2fUzZTZXNGkT0cMxkonazJJ", "6VgZMJUF6uD6WRJpEfB3cNINZUj21rgoqL1g88Zo");
    }
}

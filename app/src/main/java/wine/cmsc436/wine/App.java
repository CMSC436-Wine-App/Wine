package wine.cmsc436.wine;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static WinePurchaseList currentPurchases = new WinePurchaseList();

    public static HashMap<Wine, BadgeDiscount> availBadges = new HashMap<Wine, BadgeDiscount>();

    public static String RestaurantID = "rSUTXnLDFR";

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
        ParseObject.registerSubclass(parse.subclasses.PurchaseHistory.class);
        ParseObject.registerSubclass(parse.subclasses.BadgeDiscount.class);
        ParseObject.registerSubclass(parse.subclasses.UserBadge.class);
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

        // At the beginning of the application we determine what badges the user is
        // eligible for, for whichever wine.
        // TODO: Potential UI lockup since this is on the main ui thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Badge> gainedBadges = addAvailableBadges();
                for (int i = 0; i < gainedBadges.size(); i++) {
                    Log.i("ASDF", "Gained badge: " + gainedBadges.get(i).getName());
                }
                for (Wine w : availBadges.keySet()) {
                    Log.i("ASDF", w.getName());
                }
            }
        }).start();
    }

    public static ArrayList<Badge> addAvailableBadges() {
        final ArrayList<Badge> gainedBadges = new ArrayList<Badge>();

        // Look at all of our purchases for this user
        ParseQuery<Purchase> winePurchases = Purchase.getPurchaseWines(User.getCurrentUser());
        try {
            List<Purchase> purchases = winePurchases.find();
            if (purchases.size() > 0) {
                // For each purchase, get the wine and check if we have a discount rate which
                // we qualify for, for that one already
                for (int i = 0; i < purchases.size(); i++) {
                    final Wine w = purchases.get(i).getWine();
                    // If we have a discountRate already, skip it
                    if (availBadges.containsKey(w))
                        continue;
                    availBadges.put(w, null);

                    // Get how many times we've bought this wine
                    ParseQuery<Purchase> purchaseCount = Purchase.getPurchaseCount(w);
                    List<Purchase> purchasesCount = purchaseCount.find();
                    int numPurchases = purchasesCount.size();
                    Log.i("ASDF", "NUMPURHC: " + String.valueOf(numPurchases));
                    // Get all of the badges we qualify for (regardless if wine) by checking
                    // against number of times we've purchased this wine
                    ParseQuery<Badge> badgesEligible = Badge.getBadgesEligible(numPurchases);
                    List<Badge> badges = badgesEligible.find();
                    Log.i("ASDF", "BADGESIZE: " + String.valueOf(badges.size()));
                    for (int j = 0; j < badges.size(); j++) {
                        // Check for each badge, if it is a wine badge, if we have used it
                        final Badge checkBadge = badges.get(j);
                        ParseQuery<UserBadge> unusedBadges = UserBadge.getUnusedBadges(w, checkBadge, User.getCurrentUser());
                        List<UserBadge> userBadges = unusedBadges.find();
                        // Add the badge, we don't have this one (thus havent used it).
                        if (userBadges.size() == 0) {
                            UserBadge newEntry = new UserBadge(User.getCurrentUser(), w, checkBadge);
                            Log.i("ASDF", "adding: " + gainedBadges.add(newEntry.getBadge()));
                            if (checkBadge.getIsWineBadge()) {
                                // Add it to our local HashMap
                                // Get the discount for it, so we can add it.
                                ParseQuery<BadgeDiscount> badgeDiscounts = BadgeDiscount.getBadgeDiscounts(checkBadge);
                                List<BadgeDiscount> badgeDiscountsList = badgeDiscounts.find();

                                if (badgeDiscountsList.size() > 0) {
                                    BadgeDiscount discountRate = badgeDiscountsList.get(0);
                                    availBadges.put(w, discountRate);
                                } else {
                                    Log.i(App.APPTAG, "Found no badge discounts for badge: " + checkBadge.getName() + "!");
                                }
                                newEntry.setUsed(false);
                            } else {
                                newEntry.setUsed(true);
                            }
                            newEntry.saveInBackground();
                        } else {
                            UserBadge ub = userBadges.get(0);
                            // If the badge we have isn't being used then
                            // add it to our Map and make sure it is used
                            // next
                            if (!ub.isUsed()) {
                                ParseQuery<BadgeDiscount> badgeDiscounts = BadgeDiscount.getBadgeDiscounts(ub.getBadge());
                                List<BadgeDiscount> badgeDiscountsList = badgeDiscounts.find();
                                if (badgeDiscountsList.size() > 0) {
                                    BadgeDiscount discountRate = badgeDiscountsList.get(0);
                                    availBadges.put(w, discountRate);
                                } else {
                                    Log.i(App.APPTAG, "Found no badge discounts for badge: " + checkBadge.getName() + "!");
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            Log.i("ASDF", e.getMessage());
        }
        return gainedBadges;
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

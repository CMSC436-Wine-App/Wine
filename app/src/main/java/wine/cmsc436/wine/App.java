package wine.cmsc436.wine;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parse.subclasses.Badge;
import parse.subclasses.BadgeDiscount;
import parse.subclasses.Purchase;
import parse.subclasses.PurchaseHistory;
import parse.subclasses.Review;
import parse.subclasses.User;
import parse.subclasses.UserBadge;
import parse.subclasses.Wine;

/**
 * Created by Ethan on 11/15/2014.
 */
public class App extends Application {

    // Debugging switch
    public static final boolean APPDEBUG = true;

    // Debugging tag for the application
    public static final String APPTAG = "CMSC436-Wine-App";

    public static enum UBadgeType {WinePurchase, WineReview, PurchaseCount}

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
                    Log.e(APPTAG, e.getMessage());
                }
                final List<Wine> wines = newWines;
                // Remove the previously cached results.
                ParseObject.unpinAllInBackground("wines", new DeleteCallback() {
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(APPTAG, e.getMessage());
                        }
                        // Cache the new results.
                        ParseObject.pinAllInBackground("wines", wines);
                    }
                });
            }
        });

        // At the beginning of the application we determine what badges the user is
        // eligible for, for whichever wine.
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (User.getCurrentUser() != null) {
                    addAvailableWineBadges(UBadgeType.WinePurchase);
                    addAvailableWineBadges(UBadgeType.PurchaseCount);
                    addAvailableWineBadges(UBadgeType.WineReview);
                }
            }
        }).start();
    }

    public static ArrayList<UserBadge> addAvailableWineBadges(UBadgeType ubt) {
        // Look at all of our purchases for this user
        if (ubt == UBadgeType.WinePurchase) {
            availBadges.clear();
            getWinePurchaseBadges();
            return null;
        } else if (ubt == UBadgeType.PurchaseCount) {
            return getWinePurchaseCountBadges();
        } else if (ubt == UBadgeType.WineReview) {
            return getUserReviewBadges();
        }
        return null;
    }

    private static ArrayList<UserBadge> getWinePurchaseCountBadges() {
        ArrayList<UserBadge> newBadges = new ArrayList<UserBadge>();
        try {
            ParseQuery<PurchaseHistory> phQuery = PurchaseHistory.getQuery();
            phQuery.whereEqualTo("user", User.getCurrentUser());
            List<PurchaseHistory> purchases = phQuery.find();

            ParseQuery<Badge> findEligibleBadges = Badge.getQuery();
            findEligibleBadges.whereLessThanOrEqualTo("reqCount", purchases.size());
            findEligibleBadges.whereEqualTo("type", UBadgeType.PurchaseCount.toString());
            List<Badge> eligibleBadges = findEligibleBadges.find();

            ParseQuery<UserBadge> ubExists = UserBadge.getQuery();
            for (int i = 0; i < eligibleBadges.size(); i++) {
                ubExists.whereEqualTo("user", User.getCurrentUser());
                ubExists.whereEqualTo("badge", eligibleBadges.get(i));
                List<UserBadge> existingUB = ubExists.find();
                if (existingUB.isEmpty()) {
                    UserBadge ub = new UserBadge(User.getCurrentUser(), eligibleBadges.get(i));
                    ub.setUsed(true);
                    ub.save();
                    newBadges.add(ub);
                }
            }
        } catch (ParseException e) {
            Log.i(App.APPTAG, e.getMessage());
        }
        return newBadges;
    }

    public static ArrayList<UserBadge> getUserReviewBadges() {
        ArrayList<UserBadge> newBadges = new ArrayList<UserBadge>();
        try {
            ParseQuery<Review> userReviewQuery = Review.getQuery();
            userReviewQuery.whereEqualTo("user", User.getCurrentUser());
            List<Review> userReviews = userReviewQuery.find();

            ParseQuery<Badge> findEligibleBadges = Badge.getQuery();
            findEligibleBadges.whereLessThanOrEqualTo("reqCount", userReviews.size());
            findEligibleBadges.whereEqualTo("type", UBadgeType.WineReview.toString());
            List<Badge> eligibleBadges = findEligibleBadges.find();

            ParseQuery<UserBadge> ubExists = UserBadge.getQuery();
            for (int i = 0; i < eligibleBadges.size(); i++) {
                ubExists.whereEqualTo("user", User.getCurrentUser());
                ubExists.whereEqualTo("badge", eligibleBadges.get(i));
                List<UserBadge> existingUB = ubExists.find();
                if (existingUB.isEmpty()) {
                    UserBadge ub = new UserBadge(User.getCurrentUser(), eligibleBadges.get(i));
                    ub.setUsed(true);
                    ub.save();
                    newBadges.add(ub);
                }
            }
        } catch (ParseException e) {
            Log.i(App.APPTAG, e.getMessage());
        }
        Log.i("ASDF", ""+newBadges);
        return newBadges;
    }

    private static void getWinePurchaseBadges() {
        try {
            ParseQuery<Purchase> winePurchases = Purchase.getPurchaseWines(User.getCurrentUser());
            List<Purchase> purchases = winePurchases.find();
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
                int numPurchases = 0;
                for (int z = 0; z < purchasesCount.size(); z++) {
                    numPurchases += purchasesCount.get(z).getQuantity();
                }
                Log.i("ASDF", "NUMPURHC: " + String.valueOf(numPurchases));
                // Get badges which we qualify for by making numPurchases on Wine w
                ParseQuery<Badge> badgesEligible = Badge.getBadgesEligible(numPurchases, UBadgeType.WinePurchase.toString());
                List<Badge> badges = badgesEligible.find();
                Log.i("ASDF", "BADGESIZE: " + String.valueOf(badges.size()));
                for (int j = 0; j < badges.size(); j++) {
                    // Check for each badge, if it is a wine badge, if we have used it
                    final Badge checkBadge = badges.get(j);
                    ParseQuery<UserBadge> unusedBadges = UserBadge.getWineBadges(w, checkBadge, User.getCurrentUser());
                    List<UserBadge> userBadges = unusedBadges.find();
                    // Add the badge, we don't have this one (thus havent used it).
                    if (userBadges.size() == 0) {
                        UserBadge newEntry = new UserBadge(User.getCurrentUser(), w, checkBadge);
                        // Add it to our local HashMap
                        // Get the discount for it, so we can add it.
                        ParseQuery<BadgeDiscount> badgeDiscounts = BadgeDiscount.getBadgeDiscounts(checkBadge);
                        List<BadgeDiscount> badgeDiscountsList = badgeDiscounts.find();

                        if (badgeDiscountsList.size() > 0) {
                            BadgeDiscount discountRate = badgeDiscountsList.get(0);
                            if (availBadges.put(w, discountRate) == null) {
                                newEntry.setUsed(false);
                            } else {
                                newEntry.setUsed(true);
                            }
                        } else {
                            Log.i(App.APPTAG, "Found no badge discounts for badge: " + checkBadge.getName() + "!");
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
        } catch (ParseException e) {
            Log.i(App.APPTAG, e.getMessage());
        }
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

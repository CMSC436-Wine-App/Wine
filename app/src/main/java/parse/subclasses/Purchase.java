package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import wine.cmsc436.wine.App;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Purchase")
public class Purchase extends ParseObject {

    private static final String WINE = "wine";
    private static final String USER = "user";
    private static final String REST = "restaurant";
    private static final String PURCHASEHISTORY = "purchaseHistory";
    private static final String QUANTITY = "quantity";
    private static final String PAIDPRICE = "paidPrice";
    private static final String TYPE = "type";
    private static final String DISCOUNTAPPLIED = "discountApplied";
    private static final String BADGEDISCOUNT = "badgeDiscount";

    public Purchase() {  }

    public Purchase(ParseUser user, Wine wine) {
        setUser(user);
        setWine(wine);
        Restaurant restaurant = ParseObject.createWithoutData(Restaurant.class, App.RestaurantID);
        setRestaurant(restaurant);
    }

    public void setDiscountApplied(boolean b) {
        put(DISCOUNTAPPLIED, b);
    }

    public boolean getDiscountApplied() {
        return getBoolean(DISCOUNTAPPLIED);
    }

    public void setBadgeDiscount(BadgeDiscount bd) {
        put(BADGEDISCOUNT, bd);
    }

    public BadgeDiscount getBadgeDiscount() {
        return (BadgeDiscount)getParseObject(BADGEDISCOUNT);
    }

    public void setType(String type) {
        put(TYPE, type);
    }

    public String getType() {
        return getString(TYPE);
    }

    public void setPaidPrice(Double paidPrice) {
        put(PAIDPRICE, paidPrice);
    }

    public double getPaidPrice() {
        return getDouble(PAIDPRICE);
    }

    public void setQuantity(int quantity) {
        put(QUANTITY, quantity);
    }

    public int getQuantity() {
        return getInt(QUANTITY);
    }

    public void setPurchaseHistory(PurchaseHistory purchaseHistory) {
        put(PURCHASEHISTORY, purchaseHistory);
    }

    public PurchaseHistory getPurchaseHistory() {
        return (PurchaseHistory)getParseObject(PURCHASEHISTORY);
    }

    public Wine getWine() {
        return (Wine) getParseObject(WINE);
    }
    public void setWine(Wine wine) {
        put(WINE, wine);
    }

    public static ParseQuery<Purchase> getPurchaseCount(Wine wine) {
        Restaurant restaurant = ParseObject.createWithoutData(Restaurant.class, App.RestaurantID);
        return ParseQuery.getQuery(Purchase.class).whereEqualTo("wine", wine);
    }

    public static ParseQuery<Purchase> getPurchaseWines(User user) {
        Restaurant restaurant = ParseObject.createWithoutData(Restaurant.class, App.RestaurantID);
        return ParseQuery.getQuery(Purchase.class)
                .whereEqualTo("user", user)
                .whereEqualTo(REST, restaurant);
    }

    public Restaurant getRestaurant() {
        return (Restaurant) getParseObject(REST);
    }
    public void setRestaurant(Restaurant restaurant) {
        put(REST, restaurant);
    }

    public User getUser() {
        return (User) getParseUser(USER);
    }
    public void setUser(ParseUser user) {
        put(USER, user);
    }
    public void setUser(String userId) {
        put(USER, ParseObject.createWithoutData(ParseUser.class, userId));
    }

    public static ParseQuery<Purchase> getQuery() {
        return ParseQuery.getQuery(Purchase.class);
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "purchase";

    public Uri getUri() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME);
        builder.path(URI_PATH+"/" + getObjectId());
        return builder.build();
    }

    public static String getObjectId(Uri uri) {
        List<String> path = uri.getPathSegments();
        if (path.size() != 2 || !URI_PATH.equals(path.get(0))) {
            throw new RuntimeException("Invalid URI for "+URI_PATH+": " + uri);
        }
        return path.get(1);
    }
}

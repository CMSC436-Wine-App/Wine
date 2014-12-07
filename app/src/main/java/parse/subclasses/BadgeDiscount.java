package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import wine.cmsc436.wine.App;

/**
 * Created by Adam on 12/6/2014.
 */
@ParseClassName("BadgeDiscount")
public class BadgeDiscount extends ParseObject {

    private static final String BADGE = "badge";
    private static final String DISCOUNTRATE = "discountRate";
    private static final String REST = "restaurant";

    public BadgeDiscount() {  }

    public double getDiscountRate() {
        return getNumber(DISCOUNTRATE).doubleValue();
    }

    public Badge getBadge() {
        return (Badge)getParseObject(BADGE);
    }

    public static ParseQuery<BadgeDiscount> getBadgeDiscounts(Badge badge) {
        Restaurant restaurant = ParseObject.createWithoutData(Restaurant.class, App.RestaurantID);
        return ParseQuery.getQuery(BadgeDiscount.class)
                .whereEqualTo(BADGE, badge)
                .whereEqualTo(REST, restaurant)
                .orderByDescending(DISCOUNTRATE);
    }


    public static ParseQuery<BadgeDiscount> getQuery() {
        return ParseQuery.getQuery(BadgeDiscount.class);
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "BadgeDiscount";

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

package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("MenuItem")
public class MenuItem extends ParseObject {

    private static final String REST = "restaurant";
    private static final String WINE = "wine";
    private static final String IS_SOLD_OUT = "isSoldOut";

    public MenuItem() {

    }

    public boolean getIsSoldOut() {
        return getBoolean(IS_SOLD_OUT);
    }
    public void setIsSoldOut(boolean isSoldOut) {
        put(IS_SOLD_OUT, isSoldOut);
    }

    public Wine getWine() {
//        return ParseObject.createWithoutData(Wine.class, getParseObject("wine").getObjectId());
        return (Wine) getParseObject(WINE);
    }
    public void setWine(Wine wine) {
        put(WINE, wine);
    }
    public void setWine(String wineId) {
        put(WINE, ParseObject.createWithoutData(Wine.class, wineId));
    }

    public Restaurant getRestaurant() {
//        return ParseObject.createWithoutData(Restaurant.class, getParseObject("restaurant").getObjectId());
        return (Restaurant) getParseObject(REST);
    }
    public void setRestaurant(Restaurant restaurant) {
        put(REST, restaurant);
    }
    public void setRestaurant(String restId) {
        put(REST, ParseObject.createWithoutData(Restaurant.class, restId));
    }

    public static ParseQuery<MenuItem> getQuery() {
        return ParseQuery.getQuery(MenuItem.class);
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "menuItem";
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

package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {

    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String MENU_ITEMS = "menuItems";

    public Restaurant() {

    }

    public String getName() {
        return getString(NAME);
    }
    public void setName(String name) {
        put(NAME, name);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(LOCATION);
    }
    public void setLocation(ParseGeoPoint location) {
        put(LOCATION, location);
    }

    public ParseRelation<MenuItem> getMenuItemsRelation() {
        return getRelation(MENU_ITEMS);
    }
    public ParseQuery<MenuItem> getMenuItems() {
        return getMenuItemsRelation().getQuery();
    }
    public void addMenuItem(MenuItem menuItem) {
        getMenuItemsRelation().add(menuItem);
    }
    public void addMenuItem(String menuItemId) {
        getMenuItemsRelation().add(ParseObject.createWithoutData(MenuItem.class, menuItemId));
    }
    public void removeMenuItem(MenuItem menuItem) {
        getMenuItemsRelation().remove(menuItem);
    }
    public void removeMenuItem(String menuItemId) {
        getMenuItemsRelation().remove(ParseObject.createWithoutData(MenuItem.class, menuItemId));
    }

    public static ParseQuery<Restaurant> getQuery() {
        return ParseQuery.getQuery(Restaurant.class);
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "restaurant";
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

package parse.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

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

    public static ParseQuery<Restaurant> getAllRestaurants() {
        ParseQuery<Restaurant> restaurantQuery = ParseQuery.getQuery(Restaurant.class);
        return restaurantQuery;
    }
}

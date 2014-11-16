package parse.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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
    
}

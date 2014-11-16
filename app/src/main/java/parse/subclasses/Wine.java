package parse.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Wine")
public class Wine extends ParseObject {

    private static final String NAME = "name";
    private static final String DESC = "description";
    private static final String PRICE = "price";
    private static final String PHOTO = "photo";

    public Wine() {

    }

    public String getName() {
        return getString(NAME);
    }
    public void setName(String name) {
        put(NAME, name);
    }

    public String getDescription() {
        return getString(DESC);
    }
    public void setDescription(String description) {
        put(DESC, description);
    }

    public double getPrice() {
        return getDouble(PRICE);
    }
    public void setPrice(double price) {
        put(PRICE, price);
    }

    public ParseFile getPhoto() {
        return getParseFile(PHOTO);
    }
    public void setPhoto(ParseFile photo) {
        put(PHOTO, photo);
    }

    public static ParseQuery<Wine> getAllWine() {
        ParseQuery<Wine> wineQuery = ParseQuery.getQuery(Wine.class);
        return wineQuery;
    }
}

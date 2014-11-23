package parse.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Purchase")
public class Purchase extends ParseObject {

    private static final String WINE = "wine";
    private static final String USER = "user";

    public Purchase() {

    }

    public Wine getWine() {
//        return ParseObject.createWithoutData(Wine.class, getParseObject("wine").getObjectId());
        return (Wine) getParseObject(WINE);
    }
    public void setWine(Wine wine) {
        put(WINE, wine);
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
}

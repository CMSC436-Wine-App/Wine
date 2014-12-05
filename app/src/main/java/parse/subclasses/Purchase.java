package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Purchase")
public class Purchase extends ParseObject {

    private static final String WINE = "wine";
    private static final String USER = "user";

    public Purchase() {  }

    public Purchase(ParseUser user, Wine wine, Restaurant rest) {
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

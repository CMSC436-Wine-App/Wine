package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import wine.cmsc436.wine.App;

/**
 * Created by Adam on 12/6/2014.
 */
@ParseClassName("PurchaseHistory")
public class PurchaseHistory extends ParseObject {

    private static final String USER = "user";
    private static final String PURCHASETOTAL = "purchaseTotal";

    public PurchaseHistory() { }

    public PurchaseHistory(User user, Double purchaseTotal) {
        setUser(user);
        setPurchaseTotal(purchaseTotal);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    public User getUser() {
        return (User)getParseObject(USER);
    }

    public void setPurchaseTotal(Double purchaseTotal) {
        put(PURCHASETOTAL, purchaseTotal);
    }

    public double getPurchaseTotal() {
        return getDouble(PURCHASETOTAL);
    }

    public static ParseQuery<PurchaseHistory> getQuery() {
        return ParseQuery.getQuery(PurchaseHistory.class);
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "PurchaseHistory";

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

package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import wine.cmsc436.wine.App;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Badge")
public class Badge extends ParseObject {

    private static final String DESC = "description";
    private static final String NAME = "name";
    private static final String PHOTO = "photo";
    private static final String ISWINEBADGE = "isWineBadge";

    public Badge() {

    }

    public String getDescription() {
        String description = getString(DESC);
        if (description == null) return "";
        return description;
    }
    public void setDescription(String description) {
        put(DESC, description);
    }

    public boolean getIsWineBadge() {
        return getBoolean(ISWINEBADGE);
    }

    public static ParseQuery<Badge> getBadgesEligible(int purchaseCount) {
        Restaurant restaurant = ParseObject.createWithoutData(Restaurant.class, App.RestaurantID);
        return ParseQuery.getQuery(Badge.class)
                .whereLessThanOrEqualTo("reqCount", purchaseCount)
                .whereEqualTo(ISWINEBADGE, true)
                .orderByDescending("reqCount");
    }

    public String getName() {
        String name = getString(NAME);
        if (name == null) return "";
        return name;
    }
    public void setName(String name) {
        put(NAME, name);
    }

    public ParseFile getPhoto() {
        return getParseFile(PHOTO);
    }
    public void setPhoto(ParseFile photo) {
        put(PHOTO, photo);
    }

    public static ParseQuery<Badge> getQuery() {
        return ParseQuery.getQuery(Badge.class);
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "badge";
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

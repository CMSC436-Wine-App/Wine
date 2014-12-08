package parse.subclasses;

import android.net.Uri;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
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
    private static final String TYPE = "type";

    public Badge() { }

    public void setType(String t) {
        put(TYPE, t);
    }

    public String getType() {
        return getString(TYPE);
    }

    public String getDescription() {
        String description = getString(DESC);
        if (description == null) return "";
        return description;
    }
    public void setDescription(String description) {
        put(DESC, description);
    }

    public static ParseQuery<Badge> getBadgesEligible(int purchaseCount, String t) {
        Restaurant restaurant = ParseObject.createWithoutData(Restaurant.class, App.RestaurantID);
        return ParseQuery.getQuery(Badge.class)
                .whereLessThanOrEqualTo("reqCount", purchaseCount)
                .whereEqualTo(TYPE, t)
                .orderByDescending("reqCount");
    }

    public String getName() {
        try {
            String name = fetchIfNeeded().getString(NAME);
            if (name == null) return "";
            return name;
        } catch (ParseException e) {
            return "";
        }
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

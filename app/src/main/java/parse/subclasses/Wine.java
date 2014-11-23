package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

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

    public static ParseQuery<Wine> getQuery() {
        return ParseQuery.getQuery(Wine.class);
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "wine";
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

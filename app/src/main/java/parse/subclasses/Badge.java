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
@ParseClassName("Badge")
public class Badge extends ParseObject {

    private static final String WINE = "wine";
    private static final String PHOTO = "photo";

    public Badge() {

    }

    public Wine getWine() {
//        return ParseObject.createWithoutData(Wine.class, getParseObject("wine").getObjectId());
        return (Wine) getParseObject(WINE);
    }
    public void setWine(Wine wine) {
        put(WINE, wine);
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

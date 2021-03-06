package parse.subclasses;

import android.net.Uri;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import wine.cmsc436.wine.App;

/**
 * Created by Adam on 12/6/2014.
 */
@ParseClassName("UserBadge")
public class UserBadge extends ParseObject {

    private static final String BADGE = "badge";
    private static final String USER = "user";
    private static final String WINE = "wine";
    private static final String ISUSED = "used";

    public UserBadge() {
    }

    public UserBadge(User user, Wine wine, Badge badge) {
        setUser(user);
        setWine(wine);
        setBadge(badge);
    }

    public UserBadge(User user, Badge badge) {
        setUser(user);
        setBadge(badge);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    public User getUser() {
        return (User) getParseObject(USER);
    }

    public void setWine(Wine wine) {
        put(WINE, wine);
    }

    public void setUsed(boolean b) {
        put(ISUSED, b);
    }

    public boolean isUsed() {
        return getBoolean(ISUSED);
    }

    public Wine getWine() {
        return (Wine) getParseObject(WINE);
    }

    public void setBadge(Badge badge) {
        put(BADGE, badge);
    }

    public Badge getBadge() {
        try {
            return (Badge)fetchIfNeeded().getParseObject(BADGE);
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ParseQuery<UserBadge> getWineBadges(Wine wine, Badge badge, User user) {
        return ParseQuery.getQuery(UserBadge.class)
                .whereEqualTo(WINE, wine)
                .whereEqualTo(BADGE, badge)
                .whereEqualTo(USER, user);
    }


    public static ParseQuery<UserBadge> getQuery() {
        return ParseQuery.getQuery(UserBadge.class);
    }


    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "BadgeDiscount";

    @Override
    public boolean equals(Object o) {
        // TODO: This could break things
        if (o instanceof UserBadge) {
            UserBadge other = (UserBadge)o;
            if (other.getBadge().getName().equals(getBadge().getName()))
                return true;
        }
        return false;
    }


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

package parse.subclasses;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Collection;
import java.util.List;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Review")
public class Review extends ParseObject {

    private static final String SWEET = "sweetness";
    private static final String TANN = "tannins";
    private static final String ACID = "acidity";
    private static final String BODY = "body";
    private static final String AROMAS = "aromas";
    private static final String VARI = "varietals";
    private static final String COMMENT = "comment";
    private static final String RATING = "rating";
    private static final String SCORE = "score";
    private static final String USER = "user";
    private static final String WINE = "wine";
    private static final String REST = "restaurant";

    public Review() {
//        setSweetness(0);
//        setTannins(0);
//        setAcidity(0);
//        setBody(0);
//        setUser(User.getCurrentUser());
    }

    public Number getSweetness() {
        Number sweetness = getNumber(SWEET);
        if (sweetness == null) return 0;
        return sweetness;
    }
    public void setSweetness(Number sweetness) {
        put(SWEET, sweetness);
    }
    public Number getTannins() {
        Number tannins = getNumber(TANN);
        if (tannins == null) return 0;
        return tannins;
    }
    public void setTannins(Number tannins) {
        put(TANN, tannins);
    }
    public Number getAcidity() {
        Number acidity = getNumber(ACID);
        if (acidity == null) return 0;
        return acidity;
    }
    public void setAcidity(Number acidity) {
        put(ACID, acidity);
    }
    public Number getBody() {
        Number body = getNumber(BODY);
        if (body == null) return 0;
        return body;
    }
    public void setBody(Number body) {
        put(BODY, body);
    }

    public JSONArray getAromas() {
        return getJSONArray(AROMAS);
    }
    public void setAromas(JSONArray aromas) {
        put(AROMAS, aromas);
    }
    public void addAroma(String aroma) {
        add(AROMAS, aroma);
    }
    public void addAllAroma(Collection<String> aromas) {
        addAll(AROMAS, aromas);
    }
    public void addUniqueAroma(String aroma) {
        addUnique(AROMAS, aroma);
    }
    public void addAllUniqueAroma(Collection<String> aromas) {
        addAllUnique(AROMAS, aromas);
    }

    public JSONArray getVarietals() {
        return getJSONArray(VARI);
    }
    public void setVarietals(JSONArray varietals) {
        put(VARI, varietals);
    }
    public void addVarietal(String varietal) {
        add(VARI, varietal);
    }
    public void addAllVarietal(Collection<String> varietals) {
        addAll(VARI, varietals);
    }
    public void addUniqueVarietal(String varietal) {
        addUnique(VARI, varietal);
    }
    public void addAllUniqueVarietal(Collection<String> varietals) {
        addAllUnique(VARI, varietals);
    }

    public String getComment() {
        return getString(COMMENT);
    }
    public void setComment(String comment) {
        put(COMMENT, comment);
    }

    public float getRating() {
        return (float)getDouble(RATING);
    }
    public void setRating(float rating) {
        put(RATING, rating);
    }

    public int getScore() {
        return getInt(SCORE);
    }
    public void setScore(int score) {
        put(SCORE, score);
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

    public static ParseQuery<Review> getQuery() {
        return ParseQuery.getQuery(Review.class);
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "review";
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

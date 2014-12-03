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

    private static final String TAGS = "descriptors";
    private static final String NOSE_RATING = "noseRating";
    private static final String COLOR_RATING = "colorRating";
    private static final String TASTE_RATING = "tasteRating";
    private static final String FINISH_RATING = "finishRating";
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

    public JSONArray getDescriptors() {
        return getJSONArray(TAGS);
    }
    public void setDescriptors(JSONArray descriptors) {
        put(TAGS, descriptors);
    }
    public void addDescriptor(String descriptor) {
        add(TAGS, descriptor);
    }
    public void addAllDescriptor(Collection<String> descriptors) {
        addAll(TAGS, descriptors);
    }
    public void addUniqueDescriptor(String descriptor) {
        addUnique(TAGS, descriptor);
    }
    public void addAllUniqueDescriptor(Collection<String> descriptors) {
        addAllUnique(TAGS, descriptors);
    }

    public Number getNoseRating() {
        Number noseRating = getNumber(NOSE_RATING);
        if (noseRating == null) return 0;
        return noseRating;
    }
    public void setNoseRating(Number noseRating) { put(NOSE_RATING, noseRating); }
    public Number getColorRating() {
        Number colorRating = getNumber(COLOR_RATING);
        if (colorRating == null) return 0;
        return colorRating;
    }
    public void setColorRating(Number colorRating) { put(COLOR_RATING, colorRating); }
    public Number getTasteRating() {
        Number tasteRating = getNumber(TASTE_RATING);
        if (tasteRating == null) return 0;
        return tasteRating;
    }
    public void setTasteRating(Number tasteRating) { put(TASTE_RATING, tasteRating); }
    public Number getFinishRating() {
        Number finishRating = getNumber(FINISH_RATING);
        if (finishRating == null) return 0;
        return finishRating;
    }
    public void setFinishRating(Number finishRating) { put(FINISH_RATING, finishRating); }

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
        String comment = getString(COMMENT);
        if (comment == null) return "";
        return comment;
    }
    public void setComment(String comment) {
        put(COMMENT, comment);
    }

    public Number getRating() {
        Number rating = getNumber(RATING);
        if (rating == null) return 0;
        return rating;
    }
    public void setRating(Number rating) {
        put(RATING, rating);
    }

    public Number getScore() {
        Number score = getNumber(SCORE);
        if (score == null) return 0;
        return score;
    }
    public void setScore(Number score) {
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

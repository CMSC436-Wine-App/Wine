package parse.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Review")
public class Review extends ParseObject {

    private static final String COMMENT = "comment";
    private static final String RATING = "rating";
    private static final String SCORE = "score";
    private static final String USER = "user";
    private static final String WINE = "wine";
    private static final String REST = "restaurant";

    public Review() {

    }

    public String getComment() {
        return getString(COMMENT);
    }
    public void setComment(String comment) {
        put(COMMENT, comment);
    }

    public int getRating() {
        return getInt(RATING);
    }
    public void setRating(int rating) {
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
}

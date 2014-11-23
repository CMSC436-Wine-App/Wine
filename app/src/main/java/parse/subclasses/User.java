package parse.subclasses;

import android.app.Activity;
import android.net.Uri;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseClassName;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("_User")
public class User extends ParseUser {

    private static final String EMAIL_VERIFIED = "emailVerified";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String NAME = "name";
    private static final String PURCHASES = "purchases";
    private static final String REVIEWS = "reviews";
    private static final String BADGES = "badges";

    private static final Collection<String> FB_PERM = Arrays.asList(ParseFacebookUtils.Permissions.User.ABOUT_ME, ParseFacebookUtils.Permissions.User.EMAIL);

    public User() {

    }

    public boolean getEmailVerified() {
        return getBoolean(EMAIL_VERIFIED);
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }
    public void setFirstName(String firstName) {
        put(FIRST_NAME, firstName);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }
    public void setLastName(String lastName) {
        put(LAST_NAME, lastName);
    }
    public String getName() {
    return getString(NAME);
}
    public void setName(String lastName) {
        put(NAME, lastName);
    }

    private ParseRelation<Purchase> getPurchasesRelation() {
        return getRelation(PURCHASES);
    }
    public ParseQuery<Purchase> getPurchases() {
        return getPurchasesRelation().getQuery();
    }
    public void addPurchase(Purchase purchase) {
        getPurchasesRelation().add(purchase);
    }
    public void addPurchase(String purchaseId) {
        getPurchasesRelation().add(ParseObject.createWithoutData(Purchase.class, purchaseId));
    }
    public void removePurchase(Purchase purchase) {
        getPurchasesRelation().remove(purchase);
    }
    public void removePurchase(String purchaseId) {
        getPurchasesRelation().remove(ParseObject.createWithoutData(Purchase.class, purchaseId));
    }

    private ParseRelation<Review> getReviewsRelation() {
        return getRelation(REVIEWS);
    }
    public ParseQuery<Review> getReviews() {
        return getReviewsRelation().getQuery();
    }
    public void addReview(Review review) {
        getReviewsRelation().add(review);
    }
    public void addReview(String reviewId) {
        getReviewsRelation().add(ParseObject.createWithoutData(Review.class, reviewId));
    }
    public void removeReview(Review review) {
        getReviewsRelation().remove(review);
    }
    public void removeReview(String reviewId) {
        getReviewsRelation().remove(ParseObject.createWithoutData(Review.class, reviewId));
    }

    private ParseRelation<Badge> getBadgesRelation() {
        return getRelation(BADGES);
    }
    public ParseQuery<Badge> getBadges() {
        return getBadgesRelation().getQuery();
    }
    public void addBadge(Badge badge) {
        getBadgesRelation().add(badge);
    }
    public void addBadge(String badgeId) {
        getBadgesRelation().add(ParseObject.createWithoutData(Badge.class, badgeId));
    }
    public void removeBadge(Badge badge) {
        getBadgesRelation().remove(badge);
    }
    public void removeBadge(String badgeId) {
        getBadgesRelation().remove(ParseObject.createWithoutData(Badge.class, badgeId));
    }

    public static void signUpUser(String username, String password, String email, String firstName, String lastName, SignUpCallback callback) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setName(firstName + " " + lastName);

        user.signUpInBackground(callback);
    }

    public static boolean isUserLoggedIn() {
        return ParseUser.getCurrentUser() != null;
    }

    public static void facebookLogIn(Activity activity, LogInCallback callback) {
        ParseFacebookUtils.logIn(FB_PERM, activity, callback);
    }

    public static boolean isFacebookLinked() {
        return ParseFacebookUtils.isLinked(ParseUser.getCurrentUser());
    }

    public static void linkFacebook(Activity activity, SaveCallback callback) {
        ParseFacebookUtils.link(ParseUser.getCurrentUser(), activity, callback);
    }

    public static void unlinkFacebook(Activity activity, SaveCallback callback) {
        ParseFacebookUtils.unlinkInBackground(ParseUser.getCurrentUser(), callback);
    }

    public static void twitterLogIn(Activity activity, LogInCallback callback) {
        ParseTwitterUtils.logIn(activity, callback);
    }

    public static boolean isTwitterLinked() {
        return ParseTwitterUtils.isLinked(ParseUser.getCurrentUser());
    }

    public static void linkTwitter(Activity activity, SaveCallback callback) {
        ParseTwitterUtils.link(ParseUser.getCurrentUser(), activity, callback);
    }

    public static void unlinkTwitter(Activity activity, SaveCallback callback) {
        ParseTwitterUtils.unlinkInBackground(ParseUser.getCurrentUser(), callback);
    }

    public static void guestLogIn(LogInCallback callback) {
        ParseAnonymousUtils.logIn(callback);
    }

    public static boolean isGuestUser() {
        return ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser());
    }

    private static final String SCHEME = "wineApp";
    private static final String URI_PATH = "user";
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

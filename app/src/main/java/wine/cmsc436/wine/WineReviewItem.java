package wine.cmsc436.wine;

import android.content.Intent;

/**
 * Created by dylan on 11/19/14.
 */
public class WineReviewItem {

    public String rating = "";
    public String descript = "";
    public String restaurant_name = "";
    public String name = "TEST-WINE";

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getDescript() {

        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    WineReviewItem(Intent intent){
        rating = (intent.getStringExtra("RATING")).toString();
        descript = intent.getStringExtra("DESCRIPTION");
        restaurant_name = intent.getStringExtra("RESTAURANT_NAME");

        name = intent.getStringExtra("NAME");
    }

    public static void packageIntent(Intent intent, String name, String description, String restaurant_name, String rating){
        intent.putExtra("RATING", rating);
        intent.putExtra("DESCRIPTION", description);
        intent.putExtra("RESTAURANT_NAME", restaurant_name);
        intent.putExtra("NAME", name);

    }



}

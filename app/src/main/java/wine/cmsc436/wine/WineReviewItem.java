package wine.cmsc436.wine;

/**
 * Created by dylan on 11/19/14.
 */
public class WineReviewItem {

    public float rating = 3.5f;
    public String name = "TEST-WINE";


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

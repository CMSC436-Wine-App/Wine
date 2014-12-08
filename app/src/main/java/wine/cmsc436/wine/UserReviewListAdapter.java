package wine.cmsc436.wine;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import parse.subclasses.Review;
import parse.subclasses.Restaurant;
import parse.subclasses.User;

/**
 * Created by Ethan on 11/23/2014.
 */
public class UserReviewListAdapter extends ParseQueryAdapter<Review> {

    public UserReviewListAdapter(Context context) {
        super(context, new QueryFactory<Review>() {
            public ParseQuery create() {
                ParseQuery<Review> reviewQuery = Review.getQuery();
                reviewQuery.whereEqualTo("user", User.getCurrentUser());
                reviewQuery.orderByDescending("createdAt");
                reviewQuery.include("wine");
                return reviewQuery;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(Review review, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_user_wine_review, null);
        }

        super.getItemView(review, v, parent);

        TextView nameTextView = (TextView) v.findViewById(R.id.tv_wine_name);
        nameTextView.setText(review.getWine().getName());

        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.r_wine_rating);
        ratingBar.setRating(review.getRating().floatValue());

//        ParseImageView photoImageView = (ParseImageView) v.findViewById(R.id.iv_wine_thumbnail);
//        ParseFile imageFile = review.getWine().getPhotoSmall();
//        if (imageFile != null) {
//            photoImageView.setParseFile(imageFile);
//            photoImageView.loadInBackground();
//        }
        ImageView photoImageView = (ImageView) v.findViewById(R.id.iv_wine_thumbnail);
        ParseFile imageFile = review.getWine().getPhotoSmall();
        if (imageFile != null) {
            Picasso.with(getContext())
                    .load(imageFile.getUrl())
                    .into(photoImageView);
        }

        return v;
    }

}

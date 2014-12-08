package wine.cmsc436.wine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

import parse.subclasses.Review;
import parse.subclasses.User;
import parse.subclasses.Wine;

/**
 * Created by dylan on 11/19/14.
 */
public class WineReviewListAdapter extends ParseQueryAdapter<Review> {
    public WineReviewListAdapter(Context context, final Wine wine) {
        super(context, new ParseQueryAdapter.QueryFactory<Review>() {
            public ParseQuery create() {
                ParseQuery<Review> reviewQuery = Review.getQuery();
                reviewQuery.whereEqualTo("wine", wine);
                reviewQuery.orderByDescending("createdAt");
                reviewQuery.include("wine");
                reviewQuery.include("user");
                return reviewQuery;
            }
        });
    }
    public WineReviewListAdapter(Context context, final String wineId) {
        super(context, new ParseQueryAdapter.QueryFactory<Review>() {
            public ParseQuery create() {
                ParseQuery<Review> reviewQuery = Review.getQuery();
                reviewQuery.whereEqualTo("wine", ParseObject.createWithoutData(Wine.class, wineId));
                reviewQuery.orderByDescending("createdAt");
                reviewQuery.include("wine");
                reviewQuery.include("user");
                return reviewQuery;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(Review review, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_wine_review, null);
        }

        super.getItemView(review, v, parent);

        TextView userTextView = (TextView) v.findViewById(R.id.tv_user_name);
        userTextView.setText(review.getUser().getName());

        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.r_wine_rating);
        ratingBar.setRating(review.getRating().floatValue());

        TextView commentTextView = (TextView) v.findViewById(R.id.tv_review_comment);
        commentTextView.setText(review.getComment());

        return v;
    }
}

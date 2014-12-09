package wine.cmsc436.wine;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import parse.subclasses.Badge;
import parse.subclasses.BadgeDiscount;
import parse.subclasses.Restaurant;

/**
 * Created by Ethan on 11/23/2014.
 */
public class BadgeListAdapter extends ParseQueryAdapter<Badge> {

    String discount_string = "";

    public BadgeListAdapter(Context context) {
        super(context, new QueryFactory<Badge>() {
            public ParseQuery create() {
                ParseQuery<Badge> badgeQuery = Badge.getQuery();
                badgeQuery.orderByDescending("isWineBadge");

                return badgeQuery;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(final Badge badge, View v, ViewGroup parent) {



        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_item_badge, null);
        }

        final View myview = v;

        super.getItemView(badge, v, parent);

        // Add and download the image
//        ParseImageView photoImageView = (ParseImageView) v.findViewById(R.id.badge_photo);
//        ParseFile imageFile = badge.getPhoto();
//        if (imageFile != null) {
//            photoImageView.setParseFile(imageFile);
//            photoImageView.loadInBackground();
//        }
        ImageView photoImageView = (ImageView) v.findViewById(R.id.badge_photo);
        ParseFile imageFile = badge.getPhoto();
        if (imageFile != null) {
            Picasso.with(getContext())
                    .load(imageFile.getUrl())
                    .into(photoImageView);
        }


        discount_string = "";
        if (badge.getType().equals(App.UBadgeType.WinePurchase.toString())) {


            ParseQuery<BadgeDiscount> query = ParseQuery.getQuery("BadgeDiscount");
            query.whereEqualTo("badge", badge);
            query.findInBackground(new FindCallback<BadgeDiscount>() {
                @Override
                public void done(List<BadgeDiscount> badgeDiscounts, com.parse.ParseException e) {
                    if (e == null) {
                        Log.d("badge", "Retrieved " + badgeDiscounts.size() + " badge");
                        double discount_rate = badgeDiscounts.get(0).getDiscountRate();
                        discount_rate = discount_rate * 100;
                        discount_string = "Your next purchase will be discounted " + String.valueOf(discount_rate) + "% from this badge.";

                        TextView nameTextView = (TextView) myview.findViewById(R.id.badge_name);
                        nameTextView.setText(badge.getName());

                        TextView descTextView = (TextView) myview.findViewById(R.id.badge_description);
                        descTextView.setText(badge.getDescription()+ ". " + discount_string);

                    } else {

                    }
                }
            });

        } else {
            TextView nameTextView = (TextView) v.findViewById(R.id.badge_name);
            nameTextView.setText(badge.getName());

            TextView descTextView = (TextView) v.findViewById(R.id.badge_description);
            descTextView.setText(badge.getDescription());

        }

        return v;
    }

}

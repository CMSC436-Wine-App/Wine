package wine.cmsc436.wine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.squareup.picasso.Picasso;

import parse.subclasses.Badge;
import parse.subclasses.Restaurant;

/**
 * Created by Ethan on 11/23/2014.
 */
public class BadgeListAdapter extends ParseQueryAdapter<Badge> {

    public BadgeListAdapter(Context context) {
        super(context, new QueryFactory<Badge>() {
            public ParseQuery create() {
                ParseQuery<Badge> badgeQuery = Badge.getQuery();
                return badgeQuery;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(Badge badge, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_item_badge, null);
        }

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

        TextView nameTextView = (TextView) v.findViewById(R.id.badge_name);
        nameTextView.setText(badge.getName());

        TextView descTextView = (TextView) v.findViewById(R.id.badge_description);
        descTextView.setText(badge.getDescription());

        return v;
    }

}

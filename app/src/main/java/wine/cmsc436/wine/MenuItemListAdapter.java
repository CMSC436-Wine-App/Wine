package wine.cmsc436.wine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.squareup.picasso.Picasso;

import parse.subclasses.Review;
import parse.subclasses.User;
import parse.subclasses.Wine;

public class MenuItemListAdapter extends ParseQueryAdapter<Wine> {

    public MenuItemListAdapter(Context context, final boolean removeReviewedWines) {
        super(context, new ParseQueryAdapter.QueryFactory<Wine>() {
            public ParseQuery create() {
                ParseQuery<Wine> wineQuery = Wine.getQuery();
                if (removeReviewedWines) {
                    ParseQuery<Wine> reviewedWinesQuery = User.getCurrentUser().getReviewedWines();
                    wineQuery.whereDoesNotMatchKeyInQuery("objectId", "objectId", reviewedWinesQuery);
                } else {
                    wineQuery.fromLocalDatastore();
                }
                return wineQuery;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(Wine wine, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_item_wine, null);
        }

        super.getItemView(wine, v, parent);

        // Add and download the image
//        ParseImageView photoImageView = (ParseImageView) v.findViewById(R.id.wine_photo);
//        ParseFile imageFile = wine.getPhotoSmall();
//        if (imageFile != null) {
//            photoImageView.setParseFile(imageFile);
//            photoImageView.loadInBackground();
//        }
        ImageView photoImageView = (ImageView) v.findViewById(R.id.wine_photo);
        ParseFile imageFile = wine.getPhotoSmall();
        if (imageFile != null) {
            Picasso.with(getContext())
                    .load(imageFile.getUrl())
                    .into(photoImageView);
        }

        // Add the title view
        TextView nameTextView = (TextView) v.findViewById(R.id.wine_name);
        nameTextView.setText(wine.getName());

        return v;
    }

}

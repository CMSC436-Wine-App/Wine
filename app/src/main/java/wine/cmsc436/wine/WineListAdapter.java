package wine.cmsc436.wine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import parse.subclasses.Wine;

/**
 * Created by Ethan on 11/23/2014.
 */
// the normal way
//public class WineListAdapter extends ArrayAdapter<Wine> {
//
//    private LayoutInflater inflater;
//
//    public WineListAdapter(Context context) {
//        super(context, 0);
//        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        ViewHolder holder;
//
//        // If a view hasn't been provided inflate on
//        if (view == null) {
//            view = inflater.inflate(R.layout.list_item_wine, parent, false);
//            // Cache view components into the view holder
//            holder = new ViewHolder();
//            holder.wineListLayout = (LinearLayout) view.findViewById(R.id.wine_item);
//            holder.wineName = (TextView) view.findViewById(R.id.wine_name);
//            holder.winePhoto = (ParseImageView) view.findViewById(R.id.wine_photo);
//            // Tag for lookup later
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//
//        final Wine wine = getItem(position);
//        final TextView wineName = holder.wineName;
//        final ParseImageView photo = holder.winePhoto;
//
//        wineName.setText(wine.getName());
//        photo.setParseFile(wine.getPhoto());
//        photo.loadInBackground();
//
//        return view;
//    }
//
//    private static class ViewHolder {
//        LinearLayout wineListLayout;
//        TextView wineName;
//        ParseImageView winePhoto;
//    }
//}

// the parse way
public class WineListAdapter extends ParseQueryAdapter<Wine> {

    public WineListAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Wine>() {
            public ParseQuery create() {
                ParseQuery<Wine> wineQuery = Wine.getQuery();
                wineQuery.fromLocalDatastore();
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
        ParseImageView photoImageView = (ParseImageView) v.findViewById(R.id.wine_photo);
        ParseFile imageFile = wine.getPhoto();
        if (imageFile != null) {
            photoImageView.setParseFile(imageFile);
            photoImageView.loadInBackground();
        }

        // Add the title view
        TextView nameTextView = (TextView) v.findViewById(R.id.wine_name);
        nameTextView.setText(wine.getName());

        return v;
    }

}

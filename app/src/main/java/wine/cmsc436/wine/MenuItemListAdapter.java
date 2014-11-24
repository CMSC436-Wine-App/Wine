package wine.cmsc436.wine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import parse.subclasses.MenuItem;
import parse.subclasses.Restaurant;
import parse.subclasses.Wine;

/**
 * Created by Ethan on 11/23/2014.
 */
public class MenuItemListAdapter extends ParseQueryAdapter<MenuItem> {

    public MenuItemListAdapter(Context context, final String restaurantId) {
        super(context, new QueryFactory<MenuItem>() {
            public ParseQuery create() {
                ParseQuery<MenuItem> menuItemQuery = MenuItem.getQuery();
                menuItemQuery.whereEqualTo("restaurant", ParseObject.createWithoutData(Restaurant.class, restaurantId));
                menuItemQuery.include("wine");
                return menuItemQuery;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(MenuItem menuItem, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_item_menu_item, null);
        }

        super.getItemView(menuItem, v, parent);

        // Add and download the image
        ParseImageView photoImageView = (ParseImageView) v.findViewById(R.id.menu_item_photo);
        ParseFile imageFile = menuItem.getWine().getPhoto();
        if (imageFile != null) {
            photoImageView.setParseFile(imageFile);
            photoImageView.loadInBackground();
        }

        // Add the title view
        TextView nameTextView = (TextView) v.findViewById(R.id.menu_item_name);
        nameTextView.setText(menuItem.getWine().getName());

        return v;
    }

}

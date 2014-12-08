package wine.cmsc436.wine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;

import java.util.ArrayList;

import parse.subclasses.Badge;
import parse.subclasses.UserBadge;
import parse.subclasses.Wine;

/**
 * Created by Adam on 12/8/2014.
 */
public class UserBadgeAttrListAdapter extends BaseAdapter {
    ArrayList<Wine> wines = null;
    Context mContext = null;

    public UserBadgeAttrListAdapter(Context mContext) {
        this.mContext = mContext;
        wines = new ArrayList<Wine>();
    }

    @Override
    public int getCount() {
        return wines.size();
    }

    @Override
    public Object getItem(int position) {
        return wines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Wine w) {
        wines.add(w);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Wine wine = (Wine) getItem(position);

        RelativeLayout wcLayout = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = new View(mContext);
            convertView = inflater.inflate(R.layout.list_item_wine, null);
        } else
            convertView = inflater.inflate(R.layout.list_item_wine, null);

        wcLayout = new RelativeLayout(mContext);
        wcLayout.addView(convertView);

        // Add and download the image
        ParseImageView photoImageView = (ParseImageView) convertView.findViewById(R.id.wine_photo);
        ParseFile imageFile = wine.getPhotoSmall();
        if (imageFile != null) {
            photoImageView.setParseFile(imageFile);
            photoImageView.loadInBackground();
        }

        // Add the title view
        TextView nameTextView = (TextView) convertView.findViewById(R.id.wine_name);
        nameTextView.setText(wine.getName());

        return wcLayout;
    }
}

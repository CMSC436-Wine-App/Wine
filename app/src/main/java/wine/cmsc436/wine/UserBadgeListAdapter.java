package wine.cmsc436.wine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import parse.subclasses.Badge;
import parse.subclasses.Purchase;
import parse.subclasses.User;
import parse.subclasses.UserBadge;
import parse.subclasses.Wine;

/**
 * Created by Adam on 12/8/2014.
 */
public class UserBadgeListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<UserBadge> userBadges;
    public static HashMap<UserBadge, ArrayList<Wine>> ubWines;

    public UserBadgeListAdapter(Context mContext) {
        this.mContext = mContext;
        userBadges = new ArrayList<UserBadge>();
        ubWines = new HashMap<UserBadge, ArrayList<Wine>>();
    }

    @Override
    public int getCount() {
        return userBadges.size();
    }

    @Override
    public Object getItem(int position) {
        return userBadges.get(position);
    }

    public boolean add(UserBadge ub) {
        // We only care about the badges we haven't used yet.
        if (ub.getType().equals(App.UBadgeType.WinePurchase.toString()) && !ub.isUsed()) {
            if (!ubWines.containsKey(ub)) {
                ubWines.put(ub, new ArrayList<Wine>());
            }
            ubWines.get(ub).add(ub.getWine());

            if (!userBadges.contains(ub)) {
                userBadges.add(ub);
                notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserBadge userBadge = (UserBadge) getItem(position);

        RelativeLayout wcLayout = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = new View(mContext);
            convertView = inflater.inflate(R.layout.list_item_badge, null);
        } else
            convertView = inflater.inflate(R.layout.list_item_badge, null);

        wcLayout = new RelativeLayout(mContext);
        wcLayout.addView(convertView);

        Badge badge = userBadge.getBadge();

        // Add and download the image
//        ParseImageView photoImageView = (ParseImageView) convertView.findViewById(R.id.badge_photo);
//        ParseFile imageFile = badge.getPhoto();
//        if (imageFile != null) {
//            photoImageView.setParseFile(imageFile);
//            photoImageView.loadInBackground();
//        }
        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.badge_photo);
        ParseFile imageFile = badge.getPhoto();
        if (imageFile != null) {
            Picasso.with(mContext)
                    .load(imageFile.getUrl())
                    .into(photoImageView);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.badge_name);
        nameTextView.setText(badge.getName());

        TextView descTextView = (TextView) convertView.findViewById(R.id.badge_description);
        descTextView.setText(badge.getDescription());

        return wcLayout;
    }

}

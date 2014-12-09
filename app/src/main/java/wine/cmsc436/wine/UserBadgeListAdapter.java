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
    private ArrayList<SepOrUserBadge> userBadges;

    public UserBadgeListAdapter(Context mContext) {
        this.mContext = mContext;
        userBadges = new ArrayList<SepOrUserBadge>();
    }

    @Override
    public int getCount() {
        return userBadges.size();
    }

    @Override
    public Object getItem(int position) {
        return userBadges.get(position);
    }

    public boolean add(SepOrUserBadge s) {
        if (s.isSeperator()) {
            userBadges.add(s);
            return true;
        } else {
            UserBadge ub = s.getUserBadge();
            Badge b = ub.getBadge();

            // We have to do it this way because parse doesn't have
            // distinct queries..
            if (!userBadges.contains(s)) {
                userBadges.add(s);
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
        SepOrUserBadge s = (SepOrUserBadge) getItem(position);

        RelativeLayout wcLayout = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (s.isSeperator()) {
            if (convertView == null) {
                convertView = new View(mContext);
                convertView = inflater.inflate(R.layout.badge_list_seperator, null);
            } else
                convertView = inflater.inflate(R.layout.badge_list_seperator, null);


            TextView sepText = (TextView) convertView.findViewById(R.id.seperator_text);
            sepText.setText(s.getSepText());

            wcLayout = new RelativeLayout(mContext);
            wcLayout.addView(convertView);
        } else {
            UserBadge userBadge = s.getUserBadge();

            if (convertView == null) {
                convertView = new View(mContext);
                convertView = inflater.inflate(R.layout.list_item_badge, null);
            } else
                convertView = inflater.inflate(R.layout.list_item_badge, null);

            wcLayout = new RelativeLayout(mContext);
            wcLayout.addView(convertView);

            Badge badge = userBadge.getBadge();

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
        }

        return wcLayout;
    }

}

package wine.cmsc436.wine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 11/19/14.
 */
public class WineReviewListAdapter extends BaseAdapter {
    // Local Variables
    private final List<WineReviewItem> mItems = new ArrayList<WineReviewItem>();
    private final Context mContext;
        private static final String TAG = "CMSC436-Wine-App";


    // Constructor
    public WineReviewListAdapter(Context context) {
        mContext = context;
    }


    public void add(WineReviewItem new_wine){
        mItems.add(new_wine);
        notifyDataSetChanged();
    }

    // Checks if Location is already in list
    public boolean unique_review(WineReviewItem item){

        for (WineReviewItem l : mItems){
            if (l.getName().equals(item.getName())){
                // There is already a review of yours under the same wine name
                // TODO Make edit feature for wines you have already reviewed
                return false;
            }
        }
        return true;
    }

    @Override // Returns number of Reviews
    public int getCount() { return mItems.size(); }

    @Override // Returns the Review at the current Position
    public Object getItem(int pos) { return mItems.get(pos); }

    @Override // Returns the items Position
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the current WineReviewItem
        final WineReviewItem wineItem = (WineReviewItem) getItem(position);

        RelativeLayout itemLayout = (RelativeLayout) LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_wine_review, parent, false);

        TextView wine_name = (TextView) itemLayout.findViewById(R.id.tv_wine_name);
        wine_name.append(wineItem.getName());

        RatingBar rating = (RatingBar) itemLayout.findViewById(R.id.r_wine_rating);
        rating.setRating(Float.valueOf(wineItem.getRating()));

        return itemLayout;
    }
}

package wine.cmsc436.wine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import parse.subclasses.Purchase;
import parse.subclasses.PurchaseHistory;
import parse.subclasses.Review;
import parse.subclasses.User;

/**
 * Created by Adam on 12/7/2014.
 */
public class PurchaseHistoryListAdapter extends ParseQueryAdapter<PurchaseHistory> {

    private Context mContext;

    public PurchaseHistoryListAdapter(Context context, final User user) {
        super(context, new QueryFactory<PurchaseHistory>() {
            public ParseQuery create() {
                ParseQuery<PurchaseHistory> phQuery = PurchaseHistory.getQuery();
                phQuery.whereEqualTo("user", user);
                phQuery.orderByDescending("createdAt");
                return phQuery;
            }
        });
        mContext = context;
    }

    @Override
    public View getItemView(PurchaseHistory purchaseHistory, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.purchase_history_item, null);
        }

        super.getItemView(purchaseHistory, v, parent);

        TextView phDate = (TextView) v.findViewById(R.id.purchase_history_date);
        phDate.setText(purchaseHistory.getCreatedAt().toString());

        TextView phTotal = (TextView) v.findViewById(R.id.purchase_history_total);
        phTotal.setText(mContext.getString(R.string.purchase_history_total, purchaseHistory.getPurchaseTotal()));

        return v;
    }

}

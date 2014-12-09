package wine.cmsc436.wine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import parse.subclasses.PurchaseHistory;
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
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy @ hh:mm aaa");
        phDate.setText(sdf.format(purchaseHistory.getCreatedAt()));

        // Double 4
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String total = formatter.format(purchaseHistory.getPurchaseTotal());
        TextView phTotal = (TextView) v.findViewById(R.id.purchase_history_total);
        phTotal.setText(mContext.getString(R.string.purchase_history_total, total));

        return v;
    }

}

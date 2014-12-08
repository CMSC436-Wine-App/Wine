package wine.cmsc436.wine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import parse.subclasses.Purchase;

/**
 * Created by Adam on 12/7/2014.
 */
public class PurchaseHistoryOverviewListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Purchase> purchases;

    public PurchaseHistoryOverviewListAdapter(Context context) {
        mContext = context;
        purchases = new ArrayList<Purchase>();
    }

    @Override
    public int getCount() {
        return purchases.size();
    }

    @Override
    public Object getItem(int position) {
        return purchases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Purchase p) {
        purchases.add(p);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Purchase p = (Purchase) getItem(position);

        RelativeLayout wcLayout = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = new View(mContext);
            convertView = inflater.inflate(R.layout.purchase_item, null);
        } else
            convertView = inflater.inflate(R.layout.purchase_item, null);

        wcLayout = new RelativeLayout(mContext);
        wcLayout.addView(convertView);

        TextView wineName = (TextView) convertView.findViewById(R.id.purchase_name);
        TextView type = (TextView) convertView.findViewById(R.id.purchase_type);
        TextView price = (TextView) convertView.findViewById(R.id.purchase_price);

        wineName.setText(p.getWine().getName() + " (" + String.valueOf(p.getQuantity()) + ")");
        type.setText(p.getType().toString());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        if (p.getDiscountApplied()) {
            price.setText(formatter.format(p.getPaidPrice()));
            price.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        else
            price.setText(formatter.format(p.getPaidPrice()));

        return wcLayout;
    }
}

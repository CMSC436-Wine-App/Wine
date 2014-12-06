package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;

import parse.subclasses.Purchase;

/**
 * Created by Adam on 12/6/2014.
 */
public class PurchaseListAdapter extends BaseAdapter {

    private final Context mContext;

    public PurchaseListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return App.currentPurchases.size();
    }

    @Override
    public Object getItem(int position) {
        return App.currentPurchases.get(position);
    }

    public void add(WinePurchase wp) {
        App.currentPurchases.add(wp);
        notifyDataSetChanged();
    }

    public void clear() {
        App.currentPurchases.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WinePurchase wp = (WinePurchase) getItem(position);

        RelativeLayout wcLayout = null;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = new View(mContext);
            convertView = inflater.inflate(R.layout.purchase_item, null);
        }
        else
            convertView = inflater.inflate(R.layout.purchase_item, null);

        wcLayout = new RelativeLayout(mContext);
        wcLayout.addView(convertView);

        TextView wineName = (TextView)convertView.findViewById(R.id.purchase_name);
        TextView type = (TextView)convertView.findViewById(R.id.purchase_type);
        TextView price = (TextView)convertView.findViewById(R.id.purchase_price);

        wineName.setText(wp.getPurchase().getWine().getName() + " (" + String.valueOf(wp.getQuantity()) + ")");
        type.setText(wp.getWineType().toString());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        price.setText(formatter.format(wp.getPrice()));

        return wcLayout;
    }
}

package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Adam on 12/6/2014.
 */
public class WinePurchaseList {

    private ArrayList<WinePurchase> currentPurchases = null;

    public WinePurchaseList() {
        currentPurchases = new ArrayList<WinePurchase>();
    }

    public void add(WinePurchase wp) {
        boolean incQ = false;
        for (int i = 0; i < currentPurchases.size() && !incQ; i++) {
            if (currentPurchases.get(i).equals(wp)) {
                currentPurchases.get(i).incQuantity();
                incQ = true;
            }
        }

        if (!incQ)
            currentPurchases.add(wp);
    }

    public void clear() {
        currentPurchases.clear();
    }

    public String getTotal() {
        double total = 0;

        for (int i = 0; i < currentPurchases.size(); i++) {
            total += currentPurchases.get(i).getPrice() * currentPurchases.get(i).getQuantity();
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(total);
    }

    public int size() {
        return currentPurchases.size();
    }

    public WinePurchase get(int idx) {
        return currentPurchases.get(idx);
    }

}

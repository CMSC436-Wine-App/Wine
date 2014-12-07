package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import parse.subclasses.Badge;
import parse.subclasses.BadgeDiscount;
import parse.subclasses.Purchase;
import parse.subclasses.User;
import parse.subclasses.UserBadge;
import parse.subclasses.Wine;

/**
 * Created by Adam on 12/6/2014.
 */
public class WinePurchaseList {

    private ArrayList<WinePurchase> currentPurchases = null;
    private HashMap<WinePurchase, Double> discountedTotal = null;
    private HashMap<WinePurchase, BadgeDiscount> discountedInfo = null;

    public WinePurchaseList() {
        currentPurchases = new ArrayList<WinePurchase>();
        discountedTotal = new HashMap<WinePurchase, Double>();
        discountedInfo = new HashMap<WinePurchase, BadgeDiscount>();
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

    public Double getDiscountedTotal(WinePurchase wp) {
        return discountedTotal.get(wp);
    }

    public BadgeDiscount getDiscountedInfo(WinePurchase wp) {
        return discountedInfo.get(wp);
    }

    public Set<WinePurchase> getDiscountedWinePurchases() {
        return discountedInfo.keySet();
    }

    public void clear() {
        currentPurchases.clear();
        discountedTotal.clear();
        discountedInfo.clear();
    }

    public String getTotal() {
        double total = 0;

        for (int i = 0; i < currentPurchases.size(); i++) {
            Wine w = currentPurchases.get(i).getPurchase().getWine();
            if (App.availBadges.get(w) != null) {
                final Badge usedBadge = App.availBadges.get(w).getBadge();

                // We only discount one wine of this type at this rate
                double discount = currentPurchases.get(i).getPrice() - (currentPurchases.get(i).getPrice() * App.availBadges.get(w).getDiscountRate());
                double rest = currentPurchases.get(i).getPrice() * (currentPurchases.get(i).getQuantity()-1);
                total += discount + rest;
                discountedTotal.put(currentPurchases.get(i), discount+rest);
                discountedInfo.put(currentPurchases.get(i), App.availBadges.get(w));
            }
            else {
                total += currentPurchases.get(i).getPrice() * currentPurchases.get(i).getQuantity();
            }
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

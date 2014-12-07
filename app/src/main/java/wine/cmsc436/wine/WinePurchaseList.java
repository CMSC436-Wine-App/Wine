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

import parse.subclasses.Badge;
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

    public WinePurchaseList() {
        currentPurchases = new ArrayList<WinePurchase>();
        discountedTotal = new HashMap<WinePurchase, Double>();
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

    public void clear() {
        currentPurchases.clear();
    }

    public String getTotal() {
        // TODO: Account for badge discounts and come up with a way to return
        // which badges were used for what wines.
        double total = 0;

        for (int i = 0; i < currentPurchases.size(); i++) {
            Wine w = currentPurchases.get(i).getPurchase().getWine();
            if (App.availBadges.get(w) != null) {
                final Badge usedBadge = App.availBadges.get(w).getBadge();

                // We will now mark this badge as used
                ParseQuery<UserBadge> unusedBadges = UserBadge.getUnusedBadges(w, usedBadge, User.getCurrentUser());
                unusedBadges.findInBackground(new FindCallback<UserBadge>() {
                    @Override
                    public void done(List<UserBadge> userBadges, ParseException e) {
                        if (userBadges.size() == 0) {
                            Log.i(App.APPTAG, "Badge being unused not found: " + usedBadge.getName());
                        }
                        else {
                            /*
                            UserBadge ub = userBadges.get(0);
                            ub.setUsed(true);
                            ub.saveInBackground();
                            */
                        }
                    }
                });
                // We only discount one wine at this rate
                double discount = currentPurchases.get(i).getPrice() - (currentPurchases.get(i).getPrice() * App.availBadges.get(w).getDiscountRate());
                double rest = currentPurchases.get(i).getPrice() * (currentPurchases.get(i).getQuantity()-1);
                total += discount + rest;
                discountedTotal.put(currentPurchases.get(i), discount+rest);
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

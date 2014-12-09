package wine.cmsc436.wine;

import android.util.Log;

import parse.subclasses.Purchase;

/**
 * Created by Adam on 12/6/2014.
 */
public class WinePurchase {

    public static enum WineType { BOTTLE, GLASS };

    private Purchase p = null;
    private WineType wt = null;
    private double price = 0;
    private int quantity = 0;

    public WinePurchase(Purchase p, WineType wt, double price) {
        this.p = p;
        this.wt = wt;
        this.price = price;
        quantity = 1;
    }

    public WinePurchase(Purchase p, WineType wt, double price, int quantity) {
        this.p = p;
        this.wt = wt;
        this.price = price;
        this.quantity = quantity;
    }

    public Purchase getPurchase() {
        return p;
    }

    public WineType getWineType() {
        return wt;
    }

    public double getPrice() {
        return price;
    }

    public void incQuantity() {
        quantity += 1;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WinePurchase) {
            WinePurchase other = (WinePurchase)o;
            if (!other.getPurchase().getWine().getName().equals(p.getWine().getName()))
                return false;
            if (other.getPrice() != price)
                return false;
            if (other.getWineType() != wt)
                return false;

            return true;
        }
        return false;
    }

    public String toString() {
        String str = getPurchase().getWine().getName() + " - " + String.valueOf(getPrice()) + " - " + getWineType().toString();
        return str;
    }

}

package wine.cmsc436.wine;

import parse.subclasses.UserBadge;

/**
 * Created by Adam on 12/8/2014.
 */
public class SepOrUserBadge {

    private boolean isSeperator = false;
    private String sepText = "";
    private UserBadge userBadge = null;

    public SepOrUserBadge(UserBadge userBadge) {
        this.userBadge = userBadge;
        isSeperator = false;
    }

    public SepOrUserBadge(String sepText) {
        this.sepText = sepText;
        isSeperator = true;
    }

    public void setIsSeperator(boolean b) {
        isSeperator = b;
    }

    public boolean isSeperator() {
        return isSeperator;
    }

    public UserBadge getUserBadge() {
        return userBadge;
    }

    public String getSepText() {
        return sepText;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SepOrUserBadge) {
            SepOrUserBadge other = (SepOrUserBadge) o;
            if (isSeperator || other.isSeperator)
                return false;
            if (userBadge.getBadge().getName().equals(other.getUserBadge().getBadge().getName()))
                return true;
        }
        return false;
    }

}

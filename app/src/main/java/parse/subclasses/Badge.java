package parse.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Ethan on 11/16/2014.
 */
@ParseClassName("Badge")
public class Badge extends ParseObject {

    private static final String WINE = "wine";
    private static final String PHOTO = "photo";

    public Badge() {

    }

    public Wine getWine() {
//        return ParseObject.createWithoutData(Wine.class, getParseObject("wine").getObjectId());
        return (Wine) getParseObject(WINE);
    }
    public void setWine(Wine wine) {
        put(WINE, wine);
    }

    public ParseFile getPhoto() {
        return getParseFile(PHOTO);
    }
    public void setPhoto(ParseFile photo) {
        put(PHOTO, photo);
    }
}

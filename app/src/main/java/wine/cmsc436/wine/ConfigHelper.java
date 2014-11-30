package wine.cmsc436.wine;

import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ethan on 11/22/2014.
 */
public class ConfigHelper {
    private ParseConfig config;
    private long configLastFetchedTime;

    public void fetchConfigIfNeeded() {
        final long configRefreshInterval = 60 * 60; // 1 hour

        if (config == null ||
                System.currentTimeMillis() - configLastFetchedTime > configRefreshInterval) {
            // Set the config to current, just to load the cache
            config = ParseConfig.getCurrentConfig();

            // Set the current time, to flag that the operation started and prevent double fetch
            ParseConfig.getInBackground(new ConfigCallback() {
                @Override
                public void done(ParseConfig parseConfig, ParseException e) {
                    if (e == null) {
                        // Yay, retrieved successfully
                        config = parseConfig;
                        configLastFetchedTime = System.currentTimeMillis();
                    } else {
                        // Fetch failed, reset the time
                        configLastFetchedTime = 0;
                    }
                }
            });
        }
    }

    public List<String> getAromas() {
        final List<String> defaultList = Arrays.asList("Almonds","Anise","Apple","Apricot","Artichoke","Bacon","Banana","Black Pepper","Blackberry","Blackcurrant","Butter","Cabbage","Cashew","Cedar","Cheese","Cherry","Chocolate","Cinnamon","Citrus","Cloves","Coconut","Coffee","Cream","Dill","Dry fruit","Earth","Elder","Eucalyptus","Figs","Floral","Gooseberry","Grape","Grapefruit","Grass","Green Pepper","Hay","Herbs","Honey","Kiwi","Leather","Lemon","Licorice","Lime","Linden","Lychee","Mandarin","Mango","Melon","Mint","Mulberry","Mushrooms","Musk","Nutmeg","Nutty","Oak","Passion Fruit","Peach","Pear","Pineapple","Plums","Raisins","Raspberry","Rose","Spice","Strawberry","Sulphur","Tar","Toast","Tobacco","Vanilla","Violets","Walnut","Wood","Yeast");
        List<String> configList = config.getList("aromas");
        if (configList == null) {
            return defaultList;
        }
        return configList;
    }

    public List<String> getPriceTypes() {
        final List<String> defaultList = Arrays.asList("Taste","Glass","Half Bottle","Bottle","Magnum","Case");
        List<String> configList = config.getList("priceTypes");
        if (configList == null) {
            return defaultList;
        }
        return configList;
    }

    public List<String> getRegions() {
        final List<String> defaultList = Arrays.asList("Alexander Valley","Argentina","Austrailia","Bordeaux","Burgundy","California","Carneros","Central Coast","Champagne","Chile","Columbia Valley","France","Italy","Lodi","Mendoza","Napa Valley","North Coast","Paso Robles","Piedmont","Rioja","Sonoma Coast","Sonoma County","Spain","Russian River Valley","Tuscany","United States","Veneto","Williamette Valley");
        List<String> configList = config.getList("regions");
        if (configList == null) {
            return defaultList;
        }
        return configList;
    }

    public List<String> getStyles() {
        final List<String> defaultList = Arrays.asList("Red","White","Sparkling","Rose","Dessert","Fortified");
        List<String> configList = config.getList("styles");
        if (configList == null) {
            return defaultList;
        }
        return configList;
    }

    public List<String> getVarietals() {
        final List<String> defaultList = Arrays.asList("Barbera","Cabernet Sauvignon","Chardonnay","Gewurztraminer","Grenache","Malbec","Merlot","Moscato","Muscat","Pinot Grigio","Pinot Noir","Red","Riesling","Rose","Sangiovese","Sauvignon Blanc","Shiraz","Syrah","Tempranillo","White","Zinfandel");
        List<String> configList = config.getList("varietlas");
        if (configList == null) {
            return defaultList;
        }
        return configList;
    }
}

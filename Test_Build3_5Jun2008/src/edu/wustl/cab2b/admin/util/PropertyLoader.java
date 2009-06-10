package edu.wustl.cab2b.admin.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is useful for locale specific things like date
 * @author atul_jawale
 */
public class PropertyLoader {

    /**
     * @param fileName Name of the bundle file
     * @param key The key to be used
     * @return The value for the given key in given resource file
     */
    public static String getPropertyValue(String fileName, String key) {

        ResourceBundle labels = ResourceBundle.getBundle(fileName, Locale.ENGLISH);
        String str = labels.getString(key);
        return str;
    }

}

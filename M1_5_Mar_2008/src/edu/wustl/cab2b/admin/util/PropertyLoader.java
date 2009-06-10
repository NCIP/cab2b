/**
 * 
 */
package edu.wustl.cab2b.admin.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author atul_jawale
 *
 */
public class PropertyLoader {

    /*
     * 
     * @param fileName
     * @param key
     * @return
     */
    public static String getPropertyValue(String fileName, String key) {

        ResourceBundle labels = ResourceBundle.getBundle(fileName,Locale.ENGLISH);
        String str = labels.getString(key);
        return str;
    }

}

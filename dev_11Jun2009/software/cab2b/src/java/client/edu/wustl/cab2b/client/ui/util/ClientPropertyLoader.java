/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.util;

import java.util.Properties;

import edu.wustl.cab2b.common.util.Utility;

/**
 * @author gautam_shetty
 */
public class ClientPropertyLoader {
    private static Properties properties = Utility.getPropertiesFromFile("client.properties");

    /**
     * @return all the names of identity providers to show in drop down of login
     *         frame
     */
    public static String[] getGridTypes() {
        String allNames = properties.getProperty("grid.type");
        String[] idPNames = allNames.split(",");
        return idPNames;
    }
}
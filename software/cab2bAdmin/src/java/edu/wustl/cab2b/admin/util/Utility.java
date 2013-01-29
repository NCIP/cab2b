/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.util;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityHomeInterface;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;

/**
 * This class is for general utility methods that admin needs
 * @author hrishikesh_rajpathak
 */
public class Utility {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(Utility.class);

    /**
     * Adds path to cache.
     * @param curatedPath
     */
    public static void addPathToCab2bCache(CuratedPath curatedPath) {
        UtilityBusinessInterface utilityBean = getUtilityBean();
        try {
            utilityBean.addCuretedPathToCache(curatedPath);
        } catch (RemoteException e) {
            logger.error("Error refreshing cache" + e.getMessage(), e);
            throw new RuntimeException("Error refreshing path and entity cache." + e.getMessage(), e);
        }
    }

    /**
     * This method refreshes the caB2B client cache from admin module. Whatever
     * changes the admin has done are directly reflected to client cache without
     * the need to restart the server. This update is done when admin logs out.
     */
    public static void refreshCab2bCache() {
        UtilityBusinessInterface utilityBean = getUtilityBean();
        try {
            utilityBean.refreshCache();
        } catch (RemoteException e) {
            logger.error("Error refreshing path and entity cache" + e.getMessage(), e);
            throw new RuntimeException("Error refreshing path and entity cache." + e.getMessage(), e);
        }

    }

    private static UtilityBusinessInterface getUtilityBean() {
        UtilityBusinessInterface utilityBean = (UtilityBusinessInterface) Locator.getInstance().
                    locate(EjbNamesConstants.UTILITY_BEAN,UtilityHomeInterface.class);
        return utilityBean;
    }
}

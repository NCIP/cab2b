package edu.wustl.cab2b.admin.util;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityHomeInterface;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;

/**
 * @author hrishikesh_rajpathak
 * 
 * This class is for general utility methods that admin needs
 */
public class Utility {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(Utility.class);

    /**
     * This method refreshes the caB2B client cache from admin module. Whatever changes the admin has done are directly reflected to client
     * cache without the need to restart the server.
     * 
     * @param refreshPathFinderCache If true, PathFinder cache is updated along with entity cache. If false, category cache is updated along 
     * with entity cache. 
     */
    public static void refreshCab2bCache(boolean refreshPathFinderCache) {
        UtilityBusinessInterface utilityBean = getUtilityBean();
        try {
            if (refreshPathFinderCache) {
                utilityBean.refreshPathAndEntityCache(true);
            } else {
                utilityBean.refreshCategoryAndEntityCache();
            }
        } catch (RemoteException e) {
            logger.error("Error refreshing cache", e);
        }
    }

    public static void addPathToCab2bCache(CuratedPath curatedPath) {
        UtilityBusinessInterface utilityBean = getUtilityBean();
        try {
            utilityBean.addCuretedPathToCache(curatedPath);
        } catch (RemoteException e) {
            logger.error("Error refreshing cache", e);
        }
    }

    /**
     * This method refreshes the caB2B client cache from admin module. Whatever changes the admin has done are directly reflected to client
     * cache without the need to restart the server. This update is done when admin logs out. 
     */
    public static void refreshCab2bCache() {
        UtilityBusinessInterface utilityBean = getUtilityBean();
        try {
            utilityBean.refreshPathAndEntityCache(true);
        } catch (RemoteException e) {
            logger.error("Error refreshing path and entity cache", e);
        }
        try {
            utilityBean.refreshCategoryAndEntityCache();
        } catch (RemoteException e) {
            logger.error("Error refreshing category cache", e);
        }
    }

    private static UtilityBusinessInterface getUtilityBean() {
        UtilityBusinessInterface utilityBean = (UtilityBusinessInterface) Locator.getInstance().locate(
                                                                                                       EjbNamesConstants.UTILITY_BEAN,
                                                                                                       UtilityHomeInterface.class);
        return utilityBean;
    }
}

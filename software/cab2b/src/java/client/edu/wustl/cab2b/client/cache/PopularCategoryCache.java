/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.cache;

import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.category.CategoryPopularity;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.category.CategoryBusinessInterface;
import edu.wustl.cab2b.common.ejb.category.CategoryHomeInterface;

/**
 * @author hrishikesh_rajpathak
 *
 */
public class PopularCategoryCache {

    private static PopularCategoryCache popularCategoryCache = null;

    private static Collection<CategoryPopularity> popularCategories;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(CommonUtils.class);

    /**
     * Method to get singleton instance of PopularCategoryCache
     * 
     * @return singleton object of PopularCategoryCache
     */
    public static synchronized PopularCategoryCache getInstance() {
        if (popularCategoryCache == null) {
            popularCategoryCache = new PopularCategoryCache();
        }
        return popularCategoryCache;
    }

    /**
     *  Private constructor for singleton class
     */
    private PopularCategoryCache() {
        cachePopularCategories();
    }

    /**
     * Gets all the popular categories from database and sets it as the value of "popularCategoriesCollection"
     * 
     */
    private void cachePopularCategories() {
        CategoryBusinessInterface categoryOperations = (CategoryBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                    EjbNamesConstants.CATEGORY_BEAN,
                                                                                                                    CategoryHomeInterface.class);
        try {
            popularCategories = categoryOperations.getPopularCategories();
        } catch (RemoteException e) {
            logger.error(e.getStackTrace());
            CommonUtils.handleException(e, NewWelcomePanel.getMainFrame(), true, true, true, true);
        }
    }

    /**
     * @return Returns the popularCategoriesCollection.
     */
    public Collection<CategoryPopularity> getPopularCategories() {
        return popularCategories;
    }

}

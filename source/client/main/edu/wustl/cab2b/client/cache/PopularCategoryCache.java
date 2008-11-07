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

    private static Collection<CategoryPopularity> popularCategoriesCollection;

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
        getPopularCategories();
    }

    /**
     * Gets all the popular categories from database and sets it as the value of "popularCategoriesCollection"
     * 
     */
    private void getPopularCategories() {
        CategoryBusinessInterface categoryOperations = (CategoryBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                    EjbNamesConstants.CATEGORY_BEAN,
                                                                                                                    CategoryHomeInterface.class);
        Collection<CategoryPopularity> popularCategories = null;
        try {
            popularCategories = categoryOperations.getPopularCategories();
        } catch (RemoteException e) {
            logger.error(e.getStackTrace());
            CommonUtils.handleException(e, NewWelcomePanel.getMainFrame(), true, true, true, true);
        }
        setPopularCategoriesCollection(popularCategories);
    }

    /**
     * @return Returns the popularCategoriesCollection.
     */
    public Collection<CategoryPopularity> getPopularCategoriesCollection() {
        return popularCategoriesCollection;
    }

    /**
     * @param popularCategoriesCollection The popularCategoriesCollection to set.
     */
    private void setPopularCategoriesCollection(Collection<CategoryPopularity> popularCategoriesCollection) {
        PopularCategoryCache.popularCategoriesCollection = popularCategoriesCollection;
    }

}

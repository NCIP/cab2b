package edu.wustl.cab2b.server.datacategory;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.List;

import edu.wustl.cab2b.common.category.DataCategory;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

public class DataCategoryOperations extends DefaultBizLogic {

    /**
     * Hibernate DAO Type to use.
     */
    private static final int DAO_TYPE = Constants.HIBERNATE_DAO;

    /**
     * Saves the input datacategory to database.
     * 
     * @param dataCategory DataCategory to save.
     * @throws RemoteException EBJ specific Exception
     */
    public void saveDataCategory(DataCategory dataCategory) {
        try {
            insert(dataCategory, DAO_TYPE);
        } catch (BizLogicException e) {
            throw new RuntimeException(ErrorCodeConstants.CATEGORY_SAVE_ERROR);
        } catch (UserNotAuthorizedException e) {
            throw new RuntimeException(ErrorCodeConstants.CATEGORY_SAVE_ERROR);
        }
    }

    
    
    
    
    /**
     * Returns all the datacategories availble in the system.
     * 
     * @return List of all datacategories.
     */
    public List<DataCategory> getAllDataCategories(Connection connection) {
        List<DataCategory> allCategories = null;
        try {
            allCategories = retrieve(DataCategory.class.getName());
        } catch (DAOException e) {
            throw new RuntimeException("Error in fetching category", e, ErrorCodeConstants.CATEGORY_RETRIEVE_ERROR);
        }
        for (Category category : allCategories) {
          //  new CategoryOperations().postRetrievalProcess(category, EntityCache.getInstance(), connection);
        }
        return allCategories;
    }
    
}

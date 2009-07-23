package edu.wustl.cab2b.common.ejb.category;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.category.CategoryPopularity;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * Business Interface for category operations.
 * @author Srinath K
 * @author Chandrakant Talele
 */
public interface CategoryBusinessInterface extends BusinessInterface {
    /**
     * Saves the input category to database.
     * @param category Category to save.
     * @throws RemoteException EBJ specific Exception
     */
    void saveCategory(Category category) throws RemoteException;

    /**
     * @param entityId Category Entity Id.
     * @return Category for corresponding to given category entity id. 
     * @throws RemoteException EBJ specific Exception
     */
    Category getCategoryByEntityId(Long entityId) throws RemoteException;

    /**
     * @param categoryId Id of the category
     * @return The Category for given id.
     * @throws RemoteException EBJ specific Exception
     */
    Category getCategoryByCategoryId(Long categoryId) throws RemoteException;

    /**
     * @param category Input Category for which all source classes are to be found.
     * @return Set of all entities present in given categoty.
     * @throws RemoteException EBJ specific Exception
     */
    Set<EntityInterface> getAllSourceClasses(Category category) throws RemoteException;

    /**   
     * Returns all the categories which don't have any super category.
     * @return List of root categories.
     * @throws RemoteException EBJ specific Exception
     */
    List<EntityInterface> getAllRootCategories() throws RemoteException;

    /**   
     * Returns all the categories present in system.
     * @return List of categories.
     * @throws RemoteException EBJ specific Exception
     */
    List<Category> getAllCategories() throws RemoteException;

    /**
     * Returns all the attributes present in category.
     * @param category
     * @return set of attributes
     * @throws RemoteException
     */
    Set<AttributeInterface> getAllSourceAttributes(Category category) throws RemoteException;

    /**
     * Returns all the categories with their popularity count
     * @return collection of CategoryPopularity objects
     * @throws RemoteException
     */
    Collection<CategoryPopularity> getPopularCategories() throws RemoteException;
    
    MultiModelCategory getMultiModelCategoryById(Long mmcID) throws RemoteException;
    
    MultiModelCategory getMultiModelCategoryByEntity(EntityInterface entity) throws  RemoteException;
}

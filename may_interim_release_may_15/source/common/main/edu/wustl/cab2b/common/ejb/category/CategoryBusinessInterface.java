package edu.wustl.cab2b.common.ejb.category;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.BusinessInterface;
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
    public void saveCategory(Category category) throws RemoteException;

    /**
     * @param entityId Category Entity Id.
     * @return Category for corresponding to given category entity id. 
     * @throws RemoteException EBJ specific Exception
     */
    public Category getCategoryByEntityId(Long entityId) throws RemoteException;

    /**
     * @param categoryId Id of the category
     * @return The Category for given id.
     * @throws RemoteException EBJ specific Exception
     */
    public Category getCategoryByCategoryId(Long categoryId) throws RemoteException;

    /**
     * @param category Input Category for which all source classes are to be found.
     * @return Set of all entities present in given categoty.
     * @throws RemoteException EBJ specific Exception
     */
    public Set<EntityInterface> getAllSourceClasses(Category category) throws RemoteException;
    
    /**   
     * Returns all the categories which don't have any super category.
     * @return List of root categories.
     * @throws RemoteException EBJ specific Exception
     */
    public List<EntityInterface> getAllRootCategories() throws RemoteException;
}

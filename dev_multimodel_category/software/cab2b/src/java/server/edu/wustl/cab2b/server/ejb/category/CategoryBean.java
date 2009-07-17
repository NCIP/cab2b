package edu.wustl.cab2b.server.ejb.category;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.category.CategoryPopularity;
import edu.wustl.cab2b.common.ejb.category.CategoryBusinessInterface;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.category.PopularCategoryOperations;
import edu.wustl.cab2b.server.category.multimodelcategory.MultiModelCategoryOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.common.querysuite.metadata.category.Category;

public class CategoryBean extends AbstractStatelessSessionBean implements CategoryBusinessInterface {
    private static final long serialVersionUID = -3272465753615056953L;

    /**
     * Saves the input category to database.
     * 
     * @param category Category to save.
     * @throws RemoteException EBJ specific Exception
     */
    public void saveCategory(Category category) throws RemoteException {
        new CategoryOperations().saveCategory(category);
    }

    /**
     * @param entityId Category Entity Id.
     * @return Category for corresponding to given category entity id.
     * @throws RemoteException EBJ specific Exception
     */
    public Category getCategoryByEntityId(Long entityId) throws RemoteException {
        return new CategoryOperations().getCategoryByEntityId(entityId);
    }

    /**
     * @param categoryId Id of the category
     * @return The Category for given id.
     * @throws RemoteException EBJ specific Exception
     */
    public Category getCategoryByCategoryId(Long categoryId) throws RemoteException {
        return new CategoryOperations().getCategoryByCategoryId(categoryId);
    }

    /**
     * @param category Input Category for which all source classes are to be
     *            found.
     * @return Set of all entities present in given categoty.
     */
    public Set<EntityInterface> getAllSourceClasses(Category category) throws RemoteException {
        return CategoryCache.getInstance().getAllSourceClasses(category);
    }

    /**
     * Returns all the categories which don't have any super category.
     * 
     * @return List of root categories.
     * @throws RemoteException EBJ specific Exception
     */
    public List<EntityInterface> getAllRootCategories() throws RemoteException {
        return new CategoryOperations().getAllRootCategories();
    }

    /**
     * Returns all the categories
     * 
     * @return List of categories.
     * @throws RemoteException EBJ specific Exception
     */
    public List<Category> getAllCategories() throws RemoteException {
        return CategoryCache.getInstance().getCategories();
    }

    /**
     * Returns all the source attribues
     * 
     * @return Set of source attributes
     * @throws RemoteException EBJ specific Exception
     */
    public Set<AttributeInterface> getAllSourceAttributes(Category category) throws RemoteException {
        return CategoryCache.getInstance().getAllSourceAttributes(category);
    }

    /**
     * Returns all PopularCategory objects.
     * 
     * @return Collection of root PopularCategory.
     * @throws RemoteException EBJ specific Exception
     */
    public Collection<CategoryPopularity> getPopularCategories() throws RemoteException {
        return new PopularCategoryOperations().getPopularityForAllCategories();
    }

    public MultiModelCategory getMultiModelCategoryById(Long mmcID) throws RemoteException {
        return new MultiModelCategoryOperations().getMultiModelCategoryById(mmcID);
    }
    
    public MultiModelCategory getMultiModelCategoryByEntityId(Long entityID) throws  RemoteException{
        return new MultiModelCategoryOperations().getMultiModelCategoryByEntityId(entityID);
    }
}
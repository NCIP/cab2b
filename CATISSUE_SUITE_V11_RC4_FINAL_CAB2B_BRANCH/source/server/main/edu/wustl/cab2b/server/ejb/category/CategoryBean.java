/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.category;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.category.CategoryPopularity;
import edu.wustl.cab2b.common.ejb.category.CategoryBusinessInterface;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.category.PopularCategoryOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.util.ConnectionUtil;
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
        Connection con = ConnectionUtil.getConnection();
        try {
            return new CategoryOperations().getCategoryByEntityId(entityId, con);
        } finally {
            ConnectionUtil.close(con);
        }
    }

    /**
     * @param categoryId Id of the category
     * @return The Category for given id.
     * @throws RemoteException EBJ specific Exception
     */
    public Category getCategoryByCategoryId(Long categoryId) throws RemoteException {
        Connection con = ConnectionUtil.getConnection();
        try {
            return new CategoryOperations().getCategoryByCategoryId(categoryId, con);
        } finally {
            ConnectionUtil.close(con);
        }
    }

    /**
     * @param category Input Category for which all source classes are to be
     *            found.
     * @return Set of all entities present in given categoty.
     */
    public Set<EntityInterface> getAllSourceClasses(Category category) throws RemoteException {
        Connection con = ConnectionUtil.getConnection();
        try {
            return CategoryCache.getInstance(con).getAllSourceClasses(category);
        } finally {
            ConnectionUtil.close(con);
        }
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
        Connection con = ConnectionUtil.getConnection();
        try {
            return CategoryCache.getInstance(con).getCategories();
        } finally {
            ConnectionUtil.close(con);
        }
    }

    /**
     * Returns all the source attribues
     * 
     * @return Set of source attributes
     * @throws RemoteException EBJ specific Exception
     */
    public Set<AttributeInterface> getAllSourceAttributes(Category category) throws RemoteException {
        Connection con = ConnectionUtil.getConnection();
        try {
            return CategoryCache.getInstance(con).getAllSourceAttributes(category);
        } finally {
            ConnectionUtil.close(con);
        }

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
}
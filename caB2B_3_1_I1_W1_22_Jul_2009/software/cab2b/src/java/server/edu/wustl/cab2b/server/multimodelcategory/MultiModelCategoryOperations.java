package edu.wustl.cab2b.server.multimodelcategory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

public class MultiModelCategoryOperations {

    /**
     * Saves the MultiModelCategory to database.
     * 
     * @param category
     *            MultiModelCategory to save.
     */
    public void saveMultiModelCategory(MultiModelCategory mmCategory) {
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler =
                    new HibernateDatabaseOperations<MultiModelCategory>(session);
            dbHandler.insertOrUpdate(mmCategory);
        } catch (Exception e) {
            deleteSubCategories(mmCategory.getCategories());
            throw new RuntimeException("Unable to save MultiModelCategory", e, ErrorCodeConstants.MULTIMODEL_CATEGORY_SAVE_ERROR);
        } finally {
            session.close();
        }
    }

    /**
     * Saves the MultiModelCategory to database.
     * 
     * @param category
     *            Category to save.
     */
    public MultiModelCategory getMultiModelCategoryById(Long mmcID) {
        MultiModelCategory multiModelCategory = null;
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler =
                    new HibernateDatabaseOperations<MultiModelCategory>(session);
            multiModelCategory = dbHandler.retrieveById(MultiModelCategoryImpl.class.getName(), mmcID);
        } finally {
            session.close();
        }
        postRetrievalProcess((MultiModelCategoryImpl) multiModelCategory);

        return multiModelCategory;
    }

    /**
     * Returns all the MultiModelCategories available in the system.
     * 
     * @param connection
     *            Connection object reference
     * @return List of all categories.
     */
    public List<MultiModelCategory> getAllMultiModelCategories() {
        List<MultiModelCategory> allCategories = null;
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler =
                    new HibernateDatabaseOperations<MultiModelCategory>(session);
            allCategories = dbHandler.retrieve(MultiModelCategoryImpl.class.getName());
        } finally {
            session.close();
        }

        for (MultiModelCategory category : allCategories) {
            postRetrievalProcess((MultiModelCategoryImpl) category);
        }
        return allCategories;
    }

    private void postRetrievalProcess(MultiModelCategoryImpl multiModelCategory) {
        Map<Long, CategorialAttribute> categorialAttributeMap = generateMap(multiModelCategory);

        for (MultiModelAttribute multiModelAttribute : multiModelCategory.getMultiModelAttributes()) {
            Collection<CategorialAttribute> categorialAttributes = multiModelAttribute.getCategorialAttributes();
            for (CategorialAttribute categorialAttribute : categorialAttributes) {
                CategorialAttribute catAttrFromMap = categorialAttributeMap.get(categorialAttribute.getId());

                categorialAttribute.setCategorialClass(catAttrFromMap.getCategorialClass());
                categorialAttribute.setCategoryAttribute(catAttrFromMap.getCategoryAttribute());
                categorialAttribute.setDeCategoryAttributeId(catAttrFromMap.getDeCategoryAttributeId());
                categorialAttribute.setDeSourceClassAttributeId(catAttrFromMap.getDeSourceClassAttributeId());
                categorialAttribute.setSourceClassAttribute(catAttrFromMap.getSourceClassAttribute());
            }
        }
    }

    private Map<Long, CategorialAttribute> generateMap(MultiModelCategoryImpl multiModelCategory) {
        Map<Long, CategorialAttribute> categorialAttributeMap = new HashMap<Long, CategorialAttribute>();
        for (Category category : multiModelCategory.getCategories()) {
            categorialAttributeMap.putAll(generateMap(category.getRootClass()));
        }
        return categorialAttributeMap;
    }

    private Map<Long, CategorialAttribute> generateMap(CategorialClass categorialClass) {
        Map<Long, CategorialAttribute> categorialAttributeMap = new HashMap<Long, CategorialAttribute>();

        for (CategorialAttribute categorialAttribute : categorialClass.getCategorialAttributeCollection()) {
            categorialAttributeMap.put(categorialAttribute.getId(), categorialAttribute);
        }

        for (CategorialClass childCategorialClass : categorialClass.getChildren()) {
            categorialAttributeMap.putAll(generateMap(childCategorialClass));
        }

        return categorialAttributeMap;
    }

    /**
     * Saves the MultiModelCategory to database.
     * 
     * @param category
     *            Category to save.
     */
    public void updateMultiModelCategory(MultiModelCategory mmCategory) {
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler =
                    new HibernateDatabaseOperations<MultiModelCategory>(session);
            dbHandler.update(mmCategory);
        } finally {
            session.close();
        }
    }

    /**
     * This method deletes the MultiModelCategory form the database.
     * 
     * @param category Category to be deleted.
     */
    public void deleteMultiModelCategory(MultiModelCategory mmCategory) {
        deleteSubCategories(mmCategory.getCategories());

        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler =
                    new HibernateDatabaseOperations<MultiModelCategory>(session);
            dbHandler.delete(mmCategory);
        } finally {
            session.close();
        }
    }

    /**
     * This method deletes the sub-categories of a the MultiModelCategory
     * @param subCategories
     */
    private void deleteSubCategories(Collection<Category> subCategories) {
        CategoryOperations categoryOperations = new CategoryOperations();
        for (Category subCategory : subCategories) {
            categoryOperations.deleteCategory(subCategory);
        }
    }

    /**
     * This method returns the MultiModelCategory for the given corresponding DE entity id.
     * @param entityID
     * @return
     */
    public MultiModelCategory getMultiModelCategoryByEntityId(Long entityID) {
        List<MultiModelCategory> mmCategories = null;
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler =
                    new HibernateDatabaseOperations<MultiModelCategory>(session);
            mmCategories = dbHandler.retrieve(MultiModelCategoryImpl.class.getName(), "entityId", entityID);
        } finally {
            session.close();
        }

        MultiModelCategory multiModelCategory = getMultiModelCategory(mmCategories);
        postRetrievalProcess((MultiModelCategoryImpl) multiModelCategory);

        return multiModelCategory;
    }

    private MultiModelCategory getMultiModelCategory(List<MultiModelCategory> mmCategories) {
        MultiModelCategory mmCategory = null;
        if (mmCategories != null && !mmCategories.isEmpty()) {
            if (mmCategories.size() == 1) {
                mmCategory = mmCategories.get(0);
            } else {
                throw new RuntimeException("Problem in code; probably db schema");
            }
        }
        return mmCategory;
    }
}

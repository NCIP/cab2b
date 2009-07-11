package edu.wustl.cab2b.server.category.multimodelcategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

public class MultiModelCategoryOperations extends DefaultBizLogic {

    private EntityCache cache;

    private Map<Long, CategorialAttribute> categorialIdToObject;

    public MultiModelCategoryOperations() {
        cache = EntityCache.getInstance();
    }

    /**
     * Saves the MultiModelCategory to database.
     * 
     * @param category
     *            MultiModelCategory to save.
     */
    public void saveCategory(MultiModelCategory mmCategory) {
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler = new HibernateDatabaseOperations<MultiModelCategory>(
                    session);
            dbHandler.insertOrUpdate(mmCategory);
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
    public MultiModelCategory getMultiModelCategoryById(Long categoryId) {
        MultiModelCategory multiModelCategory;
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler = new HibernateDatabaseOperations<MultiModelCategory>(
                    session);
            multiModelCategory = dbHandler.retrieveById(MultiModelCategoryImpl.class.getName(), categoryId);
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
    public List<MultiModelCategory> getAllMultiModelCategoriesCategories() {
        List<MultiModelCategory> allCategories = null;
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler = new HibernateDatabaseOperations<MultiModelCategory>(
                    session);
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
        long deEntityId = multiModelCategory.getMmultiModelCategoryDeEntityId();
        multiModelCategory.setEntity(cache.getEntityById(deEntityId));
        long modelGrpId = multiModelCategory.getModelGrpId();
        ModelGroupOperations modelGrpOPerations = new ModelGroupOperations();
        multiModelCategory.setApplicationGroup(modelGrpOPerations.getModelGroupById(modelGrpId));
        multiModelCategory.setEntity(cache.getEntityById(multiModelCategory.getMmultiModelCategoryDeEntityId()));

        mapIdToCategorialObjets(multiModelCategory);

    }

    private void mapIdToCategorialObjets(MultiModelCategoryImpl multiModelCategory) {

        categorialIdToObject = new HashMap<Long, CategorialAttribute>();
        for (Category category : multiModelCategory.getCategories()) {
            doMappingForCategorialClass(category.getRootClass());

            for (MultiModelAttribute multiModelAttribute : multiModelCategory.getMultiModelAttributes()) {
                for (CategorialAttribute categorialAttribute : multiModelAttribute.getCategorialAttributes()) {
                    CategorialAttribute receivedFromCategorycategorialAttribute = categorialIdToObject.get(categorialAttribute.getId());
                    categorialAttribute.setCategorialClass(receivedFromCategorycategorialAttribute .getCategorialClass());
                    categorialAttribute.setCategoryAttribute(receivedFromCategorycategorialAttribute.getSourceClassAttribute());
                    categorialAttribute.setDeCategoryAttributeId(receivedFromCategorycategorialAttribute.getDeCategoryAttributeId());
                    categorialAttribute.setDeSourceClassAttributeId(receivedFromCategorycategorialAttribute.getDeSourceClassAttributeId());
                    categorialAttribute.setSourceClassAttribute(receivedFromCategorycategorialAttribute.getSourceClassAttribute());
                }
            }
        }
    }

    private void doMappingForCategorialClass(CategorialClass categorialClass) {

        for (CategorialAttribute categorialAttribute : categorialClass.getCategorialAttributeCollection()) {
            categorialIdToObject.put(categorialAttribute.getId(), categorialAttribute);
        }
        for (CategorialClass childCategorialClass : categorialClass.getChildren()) {
            doMappingForCategorialClass(childCategorialClass);
        }
    }

    /**
     * Saves the MultiModelCategory to database.
     * 
     * @param category
     *            Category to save.
     */
    public void updateCategory(MultiModelCategory mmCategory) {
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler = new HibernateDatabaseOperations<MultiModelCategory>(
                    session);
            dbHandler.update(mmCategory);
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
    public void deleteCategory(MultiModelCategory mmCategory) {
        Session session = null;
        try {
            session = HibernateUtil.newSession();
            HibernateDatabaseOperations<MultiModelCategory> dbHandler = new HibernateDatabaseOperations<MultiModelCategory>(
                    session);
            dbHandler.delete(mmCategory);
        } finally {
            session.close();
        }
    }
}

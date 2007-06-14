package edu.wustl.cab2b.server.cache;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.cache.CompareUtil;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * This class is used to cache the Entity and its Attribute objects.
 * @author Chandrakant Talele
 * @author gautam_shetty
 * @author Rahul Ner
 */
public class EntityCache extends AbstractEntityCache {
     private static final long serialVersionUID = 1234567890L;

//    /**
//     * The EntityCache object. Needed for singleton
//     */
//    protected static EntityCache entityCache = null;

    /**
     * @return the singleton instance of the EntityCache class.
     */
    public static synchronized EntityCache getInstance() {
        if (entityCache == null) {
            entityCache = new EntityCache();
        }
        return (EntityCache) entityCache;
    }

    /**
     * 
     */
    public EntityCache() {
        super();
    }

    protected Collection<EntityGroupInterface> getCab2bEntityGroups() {
       
        return DynamicExtensionUtility.getCab2bEntityGroups();
    }
    /**
     * Returns the Entity objects whose source classes fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose source classes fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getCategories(Collection<EntityInterface> patternEntityCollection) {
        MatchedClass matchedClass = new MatchedClass();
        CategoryOperations categoryOperations = new CategoryOperations();
        for (Category category : categories) {
            Set<EntityInterface> classesInCategory = categoryOperations.getAllSourceClasses(category);

            for (EntityInterface classInCategory : classesInCategory) {
                for (EntityInterface patternEntity : patternEntityCollection) {
                    if (CompareUtil.compare(classInCategory, patternEntity)) {
                        long deEntityID = category.getDeEntityId();
                        EntityInterface entityInterface = getEntityById(deEntityID);
                        matchedClass.getEntityCollection().add(entityInterface);
                    }
                }
            }
        }
        return matchedClass;
    }

    /**
     * Returns the Entity objects whose attributes's source classes fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose attributes's source classes fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getCategoriesAttributes(Collection<AttributeInterface> patternAttributeCollection) {
        MatchedClass matchedClass = new MatchedClass();

        CategoryOperations categoryOperations = new CategoryOperations();
        for (Category category : categories) {
            Set<AttributeInterface> attributesInCategory = categoryOperations.getAllSourceAttributes(category);
            for (AttributeInterface attributeInCategory : attributesInCategory) {
                for (AttributeInterface patternAttribute : patternAttributeCollection) {
                    if (CompareUtil.compare(attributeInCategory, patternAttribute)) {
                        long deEntityID = category.getDeEntityId();
                        EntityInterface entityInterface = getEntityById(deEntityID);
                        matchedClass.getEntityCollection().add(entityInterface);
                    }
                }
            }
        }
        return matchedClass;
    }
    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.cache.AbstractEntityCache#getAllCategories()
     */
    @Override
    protected List<Category> getAllCategories() {
        CategoryOperations catOp = new CategoryOperations();
        return catOp.getAllCategories();
    }
}
package edu.wustl.cab2b.server.category;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author chandrakant_talele
 *
 */
public class CategoryCache {
    private static CategoryCache cache;

    private List<Category> categories;

    /**
     * This is Map with KEY: category and VALUE: set of classes used in forming the category 
     */
    private Map<Long, Set<EntityInterface>> categoryVsClasseSet;

    /**
     * This is Map with KEY: category and VALUE: set of attributes used in forming the category 
     */
    private Map<Long, Set<AttributeInterface>> categoryVsAttributeSet;

    public static synchronized CategoryCache getInstance(Connection conn) {
        if (cache == null) {
            CategoryCache cacheInstance = new CategoryCache();
            cacheInstance.init(conn);
            cache = cacheInstance;
        }
        return cache;
    }

    private CategoryCache() {

    }

    private void init(Connection conn) {
        CategoryOperations categoryOperations = new CategoryOperations();
        categories = categoryOperations.getAllCategories(conn);
        categoryVsClasseSet = new HashMap<Long, Set<EntityInterface>>(categories.size());
        categoryVsAttributeSet = new HashMap<Long, Set<AttributeInterface>>(categories.size());
        for (Category category : categories) {
            addCategoryToCache(category);
        }

    }

    public List<Category> getCategories() {
        return new ArrayList<Category>(categories);
    }

    public Set<EntityInterface> getAllSourceClasses(Category category) {
        return new HashSet<EntityInterface>(categoryVsClasseSet.get(category.getId())); //avoid modification from outside
    }

    public Set<AttributeInterface> getAllSourceAttributes(Category category) {
        return new HashSet<AttributeInterface>(categoryVsAttributeSet.get(category.getId())); //avoid modification from outside
    }

    private void addCategoryToCache(Category category) {
        CategoryOperations categoryOperations = new CategoryOperations();
        categoryVsClasseSet.put(category.getId(), categoryOperations.getAllSourceClasses(category));
        categoryVsAttributeSet.put(category.getId(), categoryOperations.getAllSourceAttributes(category));
    }

    public void addCategory(Category category) {
        addCategoryToCache(category);
        EntityCache.getInstance().refreshCache();
    }

}

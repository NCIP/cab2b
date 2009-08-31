package edu.wustl.cab2b.server.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author chandrakant_talele
 * 
 */
public class CategoryCache {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(CategoryCache.class);

    private static CategoryCache cache;

    private List<Category> categories;

    /** Entity Id to category */
    protected Map<Long, Category> entityIdToCategory = new HashMap<Long, Category>();

    /** category id to category */
    protected Map<Long, Category> categoryIdToCategory = new HashMap<Long, Category>();

    /**
     * This is Map with KEY: category and VALUE: set of classes used in forming
     * the category
     */
    private Map<Long, Set<EntityInterface>> categoryVsClasseSet;

    /**
     * This is Map with KEY: category and VALUE: set of attributes used in
     * forming the category
     */
    private Map<Long, Set<AttributeInterface>> categoryVsAttributeSet;

    /**
     * Returns reference to CategoryCache for given Connection object.
     * @param conn 
     * @return Reference to CategoryCache
     */
    public static synchronized CategoryCache getInstance() {
        if (cache == null) {
            CategoryCache cacheInstance = new CategoryCache();
            cacheInstance.init();
            cache = cacheInstance;
        }
        return cache;
    }

    private CategoryCache() {
    }

    private void init() {
        CategoryOperations categoryOperations = new CategoryOperations();
        categories = categoryOperations.getAllCategories();
        categoryVsClasseSet = new HashMap<Long, Set<EntityInterface>>(categories.size());
        categoryVsAttributeSet = new HashMap<Long, Set<AttributeInterface>>(categories.size());
        for (Category category : categories) {
            addCategoryToCache(category);
            categoryIdToCategory.put(category.getId(), category);
            entityIdToCategory.put(category.getDeEntityId(), category);
        }
    }

    /**
     * Returns List of categories
     * @return List of categories
     */
    public List<Category> getCategories() {
        return new ArrayList<Category>(categories);
    }

    /**
     * Returns all source classes
     * @param category
     * @return Set of all source classes
     */
    public Set<EntityInterface> getAllSourceClasses(Category category) {
        return new HashSet<EntityInterface>(categoryVsClasseSet.get(category.getId())); // avoid modification from outside
    }

    /**
     * Returns all source attributes
     * @param category
     * @return Set of all source attributes
     */
    public Set<AttributeInterface> getAllSourceAttributes(Category category) {
        return new HashSet<AttributeInterface>(categoryVsAttributeSet.get(category.getId())); // avoid modification from outside
    }

    private void addCategoryToCache(Category category) {
        CategoryOperations categoryOperations = new CategoryOperations();
        categoryVsClasseSet.put(category.getId(), categoryOperations.getAllSourceClasses(category));
        categoryVsAttributeSet.put(category.getId(), categoryOperations.getAllSourceAttributes(category));
    }

    /**
     * Adds a given category
     * @param category
     */
    public void addCategory(Category category) {
        addCategoryToCache(category);
        EntityCache.getInstance().refreshCache();
    }

    /**
     * Returns a category. 
     * @param id
     * @return A reference to Category
     */
    public Category getCategoryById(Long id) {
        Category category = categoryIdToCategory.get(id);
        if (category == null) {
            throw new RuntimeException("Category with given id not found.");
        }
        return category;
    }

    /**
     * Returns category by entity Id.
     * @param id Entity Id
     * @return A reference to Category
     */
    public Category getCategoryByEntityId(Long id) {
        Category category = entityIdToCategory.get(id);
        if (category == null) {
            throw new RuntimeException("Category with given entity id not found.");
        }
        return category;
    }

    /**
     * Refreshes Category cache 
     * @param connection
     */
    public void refreshCategoryCache() {
        logger.info("Refreshing Category cahce...");
        init();
    }
}

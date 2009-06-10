package edu.wustl.cab2b.server.cache;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.querysuite.metadata.category.Category;

public class CategoryCache {
    /**
     * Map with key as category id and value as category.
     */
    protected Map<Long, Category> categoryIdToCategory = new HashMap<Long, Category>();

    /**
     * Map with key as category's entity id and value as category.
     */
    protected Map<Long, Category> entityIdToCategory = new HashMap<Long, Category>();

    private static CategoryCache categoryCache = null;

    private CategoryCache() {

    }

    public static synchronized CategoryCache getInstance() {
        if (categoryCache == null) {
            categoryCache = new CategoryCache();
            categoryCache.cacheCategories();
        }
        return categoryCache;
    }

    public Category getCategoryById(Long id) {
        Category category = categoryIdToCategory.get(id);
        if (category == null) {
            throw new RuntimeException("Category with given id not found.");
        }
        return category;
    }

    public Category getCategoryByEntityId(Long id) {
        Category category = entityIdToCategory.get(id);
        if (category == null) {
            throw new RuntimeException("Category with given entity id not found.");
        }
        return category;
    }

    private void cacheCategories() {
        Connection conn = ConnectionUtil.getConnection();
        List<Category> categories = null;
        try {
            categories = new CategoryOperations().getAllCategories(conn);
        } finally {
            ConnectionUtil.close(conn);
        }
        for (Category category : categories) {
            categoryIdToCategory.put(category.getId(), category);
            entityIdToCategory.put(category.getDeEntityId(), category);
        }
    }
}

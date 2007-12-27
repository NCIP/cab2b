package edu.wustl.cab2b.common.category;

import edu.wustl.common.querysuite.metadata.category.Category;

public class DataCategory extends Category {
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor
     */
    public DataCategory() {

    }

    /**
     * DataCategory constructor  
     * @param category
     */
    public DataCategory(Category category) {
        this.setCategoryEntity(category.getCategoryEntity());
        this.setDeEntityId(category.getDeEntityId());
        this.setParentCategory(category.getParentCategory());
        this.setRootClass(category.getRootClass());
        this.setSubCategories(category.getSubCategories());
    }

}

package edu.wustl.cab2b.common.queryengine.result;

import edu.wustl.common.querysuite.metadata.category.Category;

public class CategoryResult<C extends ICategorialClassRecord>
        extends
        QueryResult<C> implements ICategoryResult<C> {

    private static final long serialVersionUID = 4066934601413768459L;

    private Category category;

    protected CategoryResult(Category category) {
        super(category.getCategoryEntity());
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}

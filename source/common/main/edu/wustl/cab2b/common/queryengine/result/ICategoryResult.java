package edu.wustl.cab2b.common.queryengine.result;

import edu.wustl.common.querysuite.metadata.category.Category;

public interface ICategoryResult<C extends ICategorialClassRecord> extends
        IQueryResult<C> {
    Category getCategory();
}

package edu.wustl.cab2b.common.queryengine.result;

import java.util.List;
import java.util.Map;

import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * Represents the results when a category is the desired output.
 * @author srinath_k
 */
public interface ICategoryResult extends IQueryResult {
    Category getCategory();

    /**
     * @return map with key as url and value as the records for the root entity
     *         of the category.
     */
    Map<String, List<ICategorialClassRecord>> getUrlToRootRecords();
}

package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;

import edu.wustl.common.querysuite.queryobject.IBaseQueryObject;

/**
 * Represents the results from query engine.
 * @author srinath_k
 */
public interface IQueryResult extends IBaseQueryObject, Serializable {
    /**
     * Indicates the type of the results, which is dependent on the desired
     * output of the query.
     * @return <code>true</code> if the results are for a category;
     *         <code>false</code> if results are for a class.
     * @see edu.wustl.cab2b.common.queryengine.ICab2bQuery#getOutputEntity()
     */
    boolean isCategoryResult();
}

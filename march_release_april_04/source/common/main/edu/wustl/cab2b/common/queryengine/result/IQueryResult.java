package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.IBaseQueryObject;

/**
 * Represents the results from query engine.
 * @author srinath_k
 */
public interface IQueryResult extends IBaseQueryObject, Serializable {

    List<AttributeInterface> getAttributes();

    /**
     * Returns map with key as the service url, and value as the records (a 2-d
     * matrix with rows as records and columns as attributes' values' String
     * representation) obtained from that service.
     * @return the results.
     */
    Map<String, String[][]> getAllRecords();
}

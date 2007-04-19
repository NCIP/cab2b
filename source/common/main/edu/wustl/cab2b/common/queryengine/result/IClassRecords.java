package edu.wustl.cab2b.common.queryengine.result;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * Represents the results when the desired output is a class.
 * @author srinath_k
 */
public interface IClassRecords extends IQueryResult {

    /**
     * @return the attributes of the class for which values are present.
     */
    List<AttributeInterface> getAttributes();

    /**
     * Returns map with key as the service url, and value as the records (a 2-d
     * matrix with rows as records and columns as attributes' values' String
     * representation) obtained from that service.
     * @return the results.
     */
    Map<String, String[][]> getAllRecords();
}
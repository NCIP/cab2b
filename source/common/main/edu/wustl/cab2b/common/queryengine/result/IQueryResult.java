package edu.wustl.cab2b.common.queryengine.result;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.IBaseQueryObject;

/**
 * Represents the results from query engine. It is a wrapper around a
 * {@link Map} with key as the service url and value as a {@link List} of
 * records obtained from that service.
 * 
 * @author srinath_k
 * @param <R> represents the actual type of records contained in the query
 *            result.
 */
public interface IQueryResult<R extends IRecord> extends IBaseQueryObject {
    /**
     * An unmodifiable {@link Map} with key as the service url and value as a
     * {@link List} of records obtained from that service.
     * 
     * @return unmodifiable map.
     */
    Map<String, List<R>> getRecords();

    void addRecord(String url, R record);

    void addRecords(String url, List<R> records);

    List<R> addUrl(String url);

    EntityInterface getOutputEntity();
}

package edu.wustl.cab2b.common.queryengine.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * @author Srinath, deepak_shingan
 * @param <R>
 */
public class QueryResult<R extends IRecord> implements IQueryResult<R> {
    private static final long serialVersionUID = -6100202491215936158L;

    /**
     * Record map
     * key   :service-url
     * value : List of records
     */
    private Map<String, List<R>> records;

    /**
     * Query result output entity
     */
    private EntityInterface outputEntity;

    /**
     * Collection for failed query URLs
     */
    private Set<FQPUrlStatus> failedQueryUrls = new HashSet<FQPUrlStatus>();

    /**
     *
     * @param outputEntity
     */
    protected QueryResult(EntityInterface outputEntity) {
        this.records = new HashMap<String, List<R>>();
        this.outputEntity = outputEntity;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.querysuite.queryobject.IBaseQueryObject#getId()
     */
    public final Long getId() {
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.querysuite.queryobject.IBaseQueryObject#setId(java.lang.Long)
     */
    public final void setId(Long id) {

    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.result.IQueryResult#getRecords()
     */
    public Map<String, List<R>> getRecords() {
        return Collections.unmodifiableMap(records);
    }

    /**
     * Returns records for specified URL
     * @param url
     * @return List<R>
     */
    private List<R> getRecordsForUrl(String url) {
        List<R> val = records.get(url);
        if (val == null) {
            val = new ArrayList<R>();
            records.put(url, val);
        }
        return val;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.result.IQueryResult#addRecord(java.lang.String, edu.wustl.cab2b.common.queryengine.result.IRecord)
     */
    public void addRecord(String url, R record) {
        getRecordsForUrl(url).add(record);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.result.IQueryResult#addRecords(java.lang.String, java.util.List)
     */
    public void addRecords(String url, List<R> records) {
        List<R> existingRecords = getRecordsForUrl(url);
        existingRecords.addAll(records);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.result.IQueryResult#addUrl(java.lang.String)
     */
    public List<R> addUrl(String url) {
        return getRecordsForUrl(url);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.result.IQueryResult#getOutputEntity()
     */
    public EntityInterface getOutputEntity() {
        return outputEntity;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.result.IQueryResult#getFailedURLs()
     */
    public Set<FQPUrlStatus> getFQPUrlStatus() {
        return failedQueryUrls;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.queryengine.result.IQueryResult#setFailedURLs(java.util.Collection)
     */
    public void setFQPUrlStatus(Collection<FQPUrlStatus> failedQueryUrls) {
        this.failedQueryUrls.addAll(failedQueryUrls);
    }

    public Boolean getIsSystemGenerated() {
        return null;
    }

    public void setIsSystemGenerated(Boolean isSystemGenerated) {

    }
}

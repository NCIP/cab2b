package edu.wustl.cab2b.common.queryengine.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

public class QueryResult<R extends IRecord> implements IQueryResult<R> {
    private static final long serialVersionUID = -6100202491215936158L;

    private Map<String, List<R>> records;

    private EntityInterface outputEntity;
    
    protected QueryResult(EntityInterface outputEntity) {
        this.records = new HashMap<String, List<R>>();
        this.outputEntity = outputEntity;
    }

    final public Long getId() {
        return null;
    }
    
    final public void setId(Long id) {
        
    }

    public Map<String, List<R>> getRecords() {
        return Collections.unmodifiableMap(records);
    }

    private List<R> getRecordsForUrl(String url) {
        List<R> val;
        if (records.containsKey(url)) {
            val = records.get(url);
        } else {
            val = new ArrayList<R>();
            records.put(url, val);
        }
        return val;
    }

    public void addRecord(String url, R record) {
        getRecordsForUrl(url).add(record);
    }

    public void addRecords(String url, List<R> records) {
        List<R> existingRecords = getRecordsForUrl(url);
        existingRecords.addAll(records);
    }

    public List<R> addUrl(String url) {
        return getRecordsForUrl(url);
    }

    public EntityInterface getOutputEntity() {
        return outputEntity;
    }

}

package edu.wustl.cab2b.common.ejb.queryengine.querystatus;

import java.util.Date;
import java.util.List;

/**
 * @author chetan_pundhir
 *
 */
public class QueryStatus {

    private long queryStatusId;

    private long userId;

    private long queryId;

    private long parentId;

    private String conditions;

    private Date startTime;

    private Date endTime;

    private char status;

    private List<URLStatus> urlStatus; 

    private long resultCount;

    private String message;

    private String description;

    /**
     * @return long
     */
    private long getQueryStatusId() {
        return queryStatusId;
    }
    
    private void setQueryStatusId(long id) {
        queryStatusId = id;
    }
    /**
     * @return long
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return long
     */
    public long getQueryId() {
        return queryId;
    }

    /**
     * @param queryId
     */
    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    /**
     * @return long
     */
    public long getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return String
     */
    public String getConditions() {
        return conditions;
    }

    /**
     * @param conditions
     */
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    /**
     * @return Date
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Date
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return char
     */
    public char getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(char status) {
        this.status = status;
    }

    /**
     * @return List<URLStatus>
     */
    public List<URLStatus> getUrlStatus() {
        return urlStatus;
    }

    /**
     * @param urlStatus
     */
    public void setUrlStatus(List<URLStatus> urlStatus) {
        this.urlStatus = urlStatus;
    }

    /**
     * @return long
     */
    public long getResultCount() {
        return resultCount;
    }

    /**
     * @param resultCount
     */
    public void setResultCount(long resultCount) {
        this.resultCount = resultCount;
    }

    /**
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
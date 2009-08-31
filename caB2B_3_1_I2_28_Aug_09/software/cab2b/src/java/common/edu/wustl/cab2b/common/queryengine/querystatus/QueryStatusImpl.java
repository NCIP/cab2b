package edu.wustl.cab2b.common.queryengine.querystatus;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * This class is the Hibernate class for Query_Status table. 
 * It extends AbstractStatusImpl for properties like : ID, Status, Message, ResultCount, Description
 * @author gaurav_mehta
 */
public class QueryStatusImpl extends AbstractStatusImpl implements QueryStatus {
    private static final long serialVersionUID = 1L;

    /** This is the USer object who has executed the query in background */
    private UserInterface user;

    /** This is the query Object which is being executed in background */
    private ICab2bQuery query;

    /** These are the query conditions of the query executing in background */
    private String queryConditions;

    /** The time when query started executing */
    private Date queryStartTime;

    /** The time when query was completed due to any of the reasons : 
     * 1.Query is completed
     * 2.User aborts the Query
     * 3.Server died while executing Query
     */
    private Date queryEndTime;

    /** This is the reference of child Queries being executed. This is not null in case of:
     * 1. MultiModel Category query
     * 2. Keyword query  */
    private Set<QueryStatus> childrenQueryStatus;

    /** This is the set of all urls selected for a query */
    private Set<URLStatus> urlStatus = null;

    /** The name of the file in which the query results are going to be saved */
    private String fileName;

    private Boolean visible;

    /**
     * Gets the user object who had executed the query
     * @return the user
     * @hibernate.property name="user" column="USER_ID" type="edu.wustl.cab2b.common.user.User" not-null="true"
     */
    public UserInterface getUser() {
        return user;
    }

    /**
     * sets the user object who has executed the query
     * @param user the user to set
     */
    public void setUser(UserInterface user) {
        this.user = user;
    }

    /**
     * Returns the query object of query which is being executed in background
     * @return the query
     * @hibernate.property name="query" column="QUERY_ID" type="edu.wustl.cab2b.common.queryengine.Cab2bQuery" not-null="true"
     */
    public ICab2bQuery getQuery() {
        return query;
    }

    /**
     * Sets the query object of query which is being executed in background
     * @param query the query to set
     */
    public void setQuery(ICab2bQuery query) {
        this.query = query;
    }

    /**
     * Returns the Query conditions of query being executed. 
     * @return the queryConditions
     * @hibernate.property name="queryConditions" column="CONDITIONS" type="String" length="1024" not-null="true"
     */
    public String getQueryConditions() {
        return queryConditions;
    }

    /**
     * Sets the Query conditions of query being executed
     * @param queryConditions the queryConditions to set
     */
    public void setQueryConditions(String queryConditions) {
        this.queryConditions = queryConditions;
    }

    /**
     * Returns the starting time of Query being executed
     * @return the queryStartTime
     * @hibernate.property name="queryStartTime" column="START_TIME" type="java.util.date" not-null="true"
     */
    public Date getQueryStartTime() {
        return queryStartTime;
    }

    /**
     * Sets the starting time of Query being executed
     * @param queryStartTime the queryStartTime to set
     */
    public void setQueryStartTime(Date queryStartTime) {
        this.queryStartTime = queryStartTime;
    }

    /**
     * Returns the end time of Query being executed
     * @return the queryEndTime
     * @hibernate.property name="queryEndTime" column="END_TIME" type="java.util.date" not-null="true"
     */
    public Date getQueryEndTime() {
        return queryEndTime;
    }

    /**
     * Sets the end time of Query being executed
     * @param queryEndTime the queryEndTime to set
     */
    public void setQueryEndTime(Date queryEndTime) {
        this.queryEndTime = queryEndTime;
    }

    /**
     * Returns the set of QueryStatus for childQueries
     * @return the childrenQueryStatus
     * @hibernate.property name="childrenQueryStatus" column="PARENT_ID" 
     * type="edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus" not-null="true"
     */
    public Set<QueryStatus> getChildrenQueryStatus() {
        if (childrenQueryStatus == null) {
            childrenQueryStatus = new HashSet<QueryStatus>();
        }
        return childrenQueryStatus;
    }

    /**
     * Sets the set of QueryStatus for childQueries
     * @param childrenQueryStatus the childrenQueryStatus to set
     */
    public void setChildrenQueryStatus(Set<QueryStatus> childrenQueryStatus) {
        this.childrenQueryStatus = childrenQueryStatus;
    }

    /**
     * Returns the set of URLStatus for Query being executed
     * @return the urlStatus
     * @hibernate.property name="urlStatus" column="URL_ID" 
     * type="edu.wustl.cab2b.common.queryengine.querystatus.URLStatus" not-null="true"
     */
    public Set<URLStatus> getUrlStatus() {
        if (urlStatus == null) {
            urlStatus = new HashSet<URLStatus>();
        }
        return urlStatus;
    }

    /**
     * Sets the set of URLStatus for Query being executed
     * @param urlStatus the urlStatus to set
     */
    public void setUrlStatus(Set<URLStatus> urlStatus) {
        this.urlStatus = urlStatus;
    }

    /**
     * Returns the name of file in which the query results is going to be saved
     * @return the fileName
     * @hibernate.property name="fileName" column="FILENAME" type="String" length="100" not-null="true"
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the name of file in which the query results is going to be saved
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns whether the Query Status is visible or not
     * @return Boolean visible
     * @hibernate.property name="isVisible" column="VISIBLE" type="boolean" not-null="true" default="false"
     */
    public Boolean isVisible() {
        return visible;
    }

    /**
     * Sets whether the Query Status should be visible or not
     * @param visible
     */
    public void setVisible(Boolean visible) {
        this.visible = visible;

    }
}
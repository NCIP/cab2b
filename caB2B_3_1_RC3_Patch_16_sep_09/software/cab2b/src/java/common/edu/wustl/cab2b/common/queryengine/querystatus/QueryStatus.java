/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.querystatus;

import java.util.Date;
import java.util.Set;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * This is the Interface class for QueryStatusImpl concrete class
 * @author gaurav_mehta
 */
public interface QueryStatus extends AbstractStatus {

    /** Gets the User who had executed the query 
     * @return {@link UserInterface}
     */
    public UserInterface getUser();

    /** sets the user object who has executed the query 
     * @param user
     */
    public void setUser(UserInterface user);

    /** Gets the Query being executed 
     * @return {@link ICab2bQuery}
     */
    public ICab2bQuery getQuery();

    /** Sets the query object of query which is being executed in background 
     * @param query
     */
    public void setQuery(ICab2bQuery query);

    /** Gets the Query conditions of query being executed 
     * @return query conditions
     */
    public String getQueryConditions();

    /** Sets the Query conditions of query being executed 
     * @param queryConditions
     */
    public void setQueryConditions(String queryConditions);

    /** Gets the time when query was started executing 
     * @return query Start Date and Time
     */
    public Date getQueryStartTime();

    /** Sets the starting time of Query being executed 
     * @param queryStartTime
     */
    public void setQueryStartTime(Date queryStartTime);

    /** Gets the time when query was completed 
     * @return query end date and time
     */
    public Date getQueryEndTime();

    /** Sets the end time of Query being executed 
     * @param queryEndTime
     */
    public void setQueryEndTime(Date queryEndTime);

    /** Gets the query object of child queries 
     * @return {@link Set}
     */
    public Set<QueryStatus> getChildrenQueryStatus();

    /** Sets the set of QueryStatus for childQueries 
     * @param childrenQueryStatus
     */
    public void setChildrenQueryStatus(Set<QueryStatus> childrenQueryStatus);

    /** Gets the url object for urls of the query 
     * @return {@link Set}
     */
    public Set<URLStatus> getUrlStatus();

    /** Sets the set of URLStatus for Query being executed 
     * @param urlStatus
     */
    public void setUrlStatus(Set<URLStatus> urlStatus);

    /** Gets the file name in which the query results will be stored 
     * @return name of the file in which the results will be stored
     */
    public String getFileName();

    /** Sets the name of file in which the query results is going to be saved 
     * @param fileName
     */
    public void setFileName(String fileName);
    
    /** Returns whether the Query Status is visible or not 
     * @return {@link Boolean}
     */
    public Boolean isVisible();
    
    /** Sets whether the Query Status should be visible or not 
     * @param visible
     */
    public void setVisible(Boolean visible);
}

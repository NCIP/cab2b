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
public interface QueryStatus extends AbstarctStatus {

    /** Gets the User who had executed the query */
    public UserInterface getUser();

    /** sets the user object who has executed the query */
    public void setUser(UserInterface user);

    /** Gets the Query being executed */
    public ICab2bQuery getQuery();

    /** Sets the query object of query which is being executed in background */
    public void setQuery(ICab2bQuery query);

    /** Gets the Query conditions of query being executed */
    public String getQueryConditions();

    /** Sets the Query conditions of query being executed */
    public void setQueryConditions(String queryConditions);

    /** Gets the time when query was started executing */
    public Date getQueryStartTime();

    /** Sets the starting time of Query being executed */
    public void setQueryStartTime(Date queryStartTime);

    /** Gets the time when query was completed */
    public Date getQueryEndTime();

    /** Sets the end time of Query being executed */
    public void setQueryEndTime(Date queryEndTime);

    /** Gets the query object of child queries */
    public Set<QueryStatus> getChildrenQueryStatus();

    /** Sets the set of QueryStatus for childQueries */
    public void setChildrenQueryStatus(Set<QueryStatus> childrenQueryStatus);

    /** Gets the url object for urls of the query */
    public Set<URLStatus> getUrlStatus();

    /** Sets the set of URLStatus for Query being executed */
    public void setUrlStatus(Set<URLStatus> urlStatus);

    /** Gets the file name in which the query results will be stored */
    public String getFileName();

    /** Sets the name of file in which the query results is going to be saved */
    public void setFileName(String fileName);
    
    /** Returns whether the Query Status is visible or not */
    public Boolean isVisible();
    
    /** Sets whether the Query Status should be visible or not */
    public void setVisible(Boolean visible);
}

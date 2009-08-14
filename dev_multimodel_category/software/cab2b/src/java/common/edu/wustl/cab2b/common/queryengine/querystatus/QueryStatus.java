/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.querystatus;

import java.util.Collection;
import java.util.Date;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * This is the Interface class for QueryStatusImpl concrete class
 * @author gaurav_mehta
 */
public interface QueryStatus extends AbstarctStatus {
    
    /** Gets the User who had executed the query */
    public UserInterface getUser();
    
    /** Gets the Query being executed */
    public ICab2bQuery getQuery();
    
    /** Gets the Query conditions of query being executed */
    public String getQueryConditions();
    
    /** Gets the time when query was started executing */
    public Date getQueryStartTime();
    
    /** Gets the time when query was completed */
    public Date getQueryEndTime();
    
    /** Gets the query object of child queries */
    public Collection<QueryStatus> getChildrenQueryStatus();
    
    /** Gets the url object for urls of the query */
    public Collection<URLStatusImpl> getUrlStatus();
    
    /** Gets the file name in which the query results will be stored */
    public String getFileName();

}

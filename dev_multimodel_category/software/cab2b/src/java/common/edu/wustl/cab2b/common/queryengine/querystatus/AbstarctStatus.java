/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.querystatus;

/**
 * @author gaurav_mehta
 *
 */
public interface AbstarctStatus {
    
    /** Gets the URL ID */
    public Long getId();
    
    /** Gets the URL Status */
    public String getStatus();
    
    /** Gets the result count for the url */
    public Integer getResultCount();
    
    /** Gets the message if any set for the url */
    public String getMessage();
    
    /** Gets the description if any for the url */
    public String getDescription();

}

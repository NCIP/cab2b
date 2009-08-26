/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.querystatus;

/**
 * @author gaurav_mehta
 *
 */
public interface AbstractStatus {
    
    public static final String Processing = "Processing";

    public static final String Complete = "Complete";

    public static final String Complete_With_Error = "Complete With Error";
    
    public static final String FAILED = "Failed";

    
    /** Gets the URL ID */
    public Long getId();
    
    /** Gets the URL Status */
    public String getStatus();
    
    /** Sets the status */
    public void setStatus(String status);
    
    /** Gets the result count for the url */
    public Integer getResultCount();
    
    /** Sets the result count of Query or per URL */
    public void setResultCount(Integer resultCount);
    
    /** Gets the message if any set for the url */
    public String getMessage();
    
    /** Sets the message of Query or URL */
    public void setMessage(String message);
    
    /** Gets the description if any for the url */
    public String getDescription();
    
    /** Sets the description of Query or URL */
    public void setDescription(String description);
}

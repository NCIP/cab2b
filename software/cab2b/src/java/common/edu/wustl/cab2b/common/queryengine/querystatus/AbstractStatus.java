/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.querystatus;

/**
 * @author gaurav_mehta
 * This class is the Abstract class which is implemented by Query Status and URl Status
 */
public interface AbstractStatus {
    
    /** This is the constant for Processing */
    public static final String Processing = "Processing";

    /** This is the constant for Complete */
    public static final String Complete = "Complete";

    /** This is the constant for Complete with Error */
    public static final String Complete_With_Error = "Complete With Error";
    
    /** This is the constant for Failed */
    public static final String FAILED = "Failed";
    
    /** This is the constant for Aborted */
    public static final String ABORTED = "Aborted";
    
    /** This is the constant for Suspended */
    public static final String SUSPENDED = "Suspended";
    

    
    /** Gets the URL ID
     * @return Id 
     */ 
    public Long getId();
    
    /** Gets the URL Status
     * @return Status 
     */
    public String getStatus();
    
    /** Sets the status 
    * @param status
    */ 
    public void setStatus(String status);
    
    /** Gets the result count for the url 
     * @return result count
     */
    public Integer getResultCount();
    
    /** Sets the result count of Query or per URL 
     *@param resultCount
     */
    public void setResultCount(Integer resultCount);
    
    /** Gets the message if any set for the url 
     * @return message
     */
    public String getMessage();
    
    /** Sets the message of Query or URL 
     * @param message
     */
    public void setMessage(String message);
    
    /** Gets the description if any for the url 
     * @return description
     */
    public String getDescription();
    
    /** Sets the description of Query or URL
     * @param description 
     */
    public void setDescription(String description);
}

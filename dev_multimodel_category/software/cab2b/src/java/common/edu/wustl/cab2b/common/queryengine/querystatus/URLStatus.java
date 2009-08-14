/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.querystatus;

import edu.wustl.cab2b.common.user.ServiceURLInterface;

/**
 * This is the Interface class for URLStatusImpl concrete class
 * @author gaurav_mehta
 */
public interface URLStatus extends AbstarctStatus{
    
    /** Gets the ServiceURLInterface object for that url */
    public ServiceURLInterface getUrl();
    
    /** sets the ServiceURL object for a url in a query */
    public void setUrl(ServiceURLInterface url);
}

/**
 * 
 */
package edu.wustl.cab2b.common.queryengine.querystatus;

/**
 * This is the Interface class for URLStatusImpl concrete class
 * @author gaurav_mehta
 */
public interface URLStatus extends AbstractStatus {

    /** Gets the ServiceURLInterface object for that url */
    public String getUrl();

    /** sets the ServiceURL object for a url in a query */
    public void setUrl(String url);
}

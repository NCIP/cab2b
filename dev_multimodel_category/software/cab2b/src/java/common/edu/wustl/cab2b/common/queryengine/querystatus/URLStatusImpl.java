package edu.wustl.cab2b.common.queryengine.querystatus;

import edu.wustl.cab2b.common.user.ServiceURLInterface;

/**
 * This class is the Hibernate class for URL_STATUS 
 * It extends AbstractStatusImpl for properties like : ID, Status, Message, ResultCount, Description
 * @author gaurav_mehta
 */
public class URLStatusImpl extends AbstractStatusImpl implements URLStatus{

    private static final long serialVersionUID = 1L;

    /** This is the reference of serviceURL object for urls in a query */
    private ServiceURLInterface url;

    /**
     * @return the serviceURL object of a url in a query
     * @hibernate.property name="url" column="URL" type="edu.wustl.cab2b.common.user.ServiceURLInterface" not-null="true"
     */
    public ServiceURLInterface getUrl() {
        return url;
    }

    /**
     * sets the ServiceURL object for a url in a query
     * @param url the url to set
     */
    public void setUrl(ServiceURLInterface url) {
        this.url = url;
    }
}
/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.querystatus;

/**
 * This class is the Hibernate class for URL_STATUS 
 * It extends AbstractStatusImpl for properties like : ID, Status, Message, ResultCount, Description
 * @author gaurav_mehta
 */
public class URLStatusImpl extends AbstractStatusImpl implements URLStatus {

    private static final long serialVersionUID = 1L;

    /** This is the reference of serviceURL object for urls in a query */
    private String url;

    /**
     * @return the serviceURL object of a url in a query
     * @hibernate.property name="url" column="URL" type="edu.wustl.cab2b.common.user.ServiceURLInterface" not-null="true"
     */
    public String getUrl() {
        return url;
    }

    /**
     * sets the ServiceURL object for a url in a query
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
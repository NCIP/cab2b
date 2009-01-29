package edu.wustl.cab2b.common.user;

import java.io.Serializable;

/**
 * @author Hrishikesh Rajpathak
 * @hibernate.class table="CAB2B_SERVICE_URL"
 */
public class ServiceURL implements ServiceURLInterface, Serializable {
    private static final long serialVersionUID = 1L;

    private Long urlId;

    private String urlLocation;

    private String entityGroupName;

    private boolean isAdminDefined;

    private String hostingCenterName;

    private String description;

    /**
     * @return Returns the entityGroupId.
     * 
     * @hibernate.property column="ENTITY_GROUP_NAME" type="string"
     *                     length="1024" not-null="true"
     */
    public String getEntityGroupName() {
        return entityGroupName;
    }

    /**
     * @param entityGroupId
     *            The entityGroupId to set.
     */
    public void setEntityGroupName(String entityGroupName) {
        this.entityGroupName = entityGroupName;
    }

    /**
     * @hibernate.id name="urlId" column="URL_ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="URL_ID_SEQ"
     */
    public Long getUrlId() {
        return urlId;
    }

    /**
     * @param urlId
     *            The urlId to set.
     */
    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    /**
     * @return Returns the urlLocation.
     * 
     * @hibernate.property column="URL" type="string" length="1024"
     *                     not-null="true"
     */
    public String getUrlLocation() {
        return urlLocation;
    }

    /**
     * @param urlLocation
     *            The urlLocation to set.
     */
    public void setUrlLocation(String urlLocation) {
        this.urlLocation = urlLocation;
    }

    /**
     * @return Returns the isAdminDefined.
     * 
     * @hibernate.property column="ADMIN_DEFINED" type="boolean" not-null="true"
     */
    public boolean isAdminDefined() {
        return isAdminDefined;
    }

    /**
     * @param isAdminDefined
     *            The isAdminDefined to set.
     */
    public void setAdminDefined(boolean isAdminDefined) {
        this.isAdminDefined = isAdminDefined;
    }

    /**
     * @param description
     *        Sets the description of the URL  
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     * @return Description of the URL
     * @hibernate.id name="description" column="DESCRIPTION" type="String" not-null="false" 
     * @see edu.wustl.cab2b.common.user.ServiceURLInterface#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param hostingCenterName
     *        sets the Hosting Center Name of the URL  
     */
    public void setHostingCenterName(String hostingCenterName) {
        this.hostingCenterName = hostingCenterName;
    }

    /**
     * @return Hosting Center Name of the URL
     * @hibernate.id name="hostingCenterName" column="HOSTING_CENTER_NAME" type="String" length="254" not-null="false"
     * @see edu.wustl.cab2b.common.user.ServiceURLInterface#getHostingCenterName()
     */
    public String getHostingCenterName() {
        return hostingCenterName;
    }

    @Override
    public boolean equals(Object arg) {
        if (this == arg) {
            return true;
        }

        boolean isEqual = false;
        if (null != arg && arg instanceof ServiceURL) {
            ServiceURL serviceURL = (ServiceURL) arg;
            if (urlLocation != null && serviceURL.getUrlLocation().equals(urlLocation)) {
                isEqual = true;
            }
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        if (urlLocation != null) {
            hashCode += 7 * urlLocation.hashCode();
        }
        return hashCode;
    }

}

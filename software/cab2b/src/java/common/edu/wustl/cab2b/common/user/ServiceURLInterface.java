/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.user;

/**
 * @author gaurav_mehta
 *
 */
/**
 * @author gaurav_mehta
 *
 */
public interface ServiceURLInterface {

    /**
     * Gets the Domain Model Name. This is same as Entity Group Long/Short Name
     * @return Domain Model Name
     */
    String getDomainModel();
    
    /**
     * Gets the version of Domain Model.
     * @return version
     */
    String getVersion();
    
    /**
     * Gets the Entity Group Name. It contains both Entity Group Name and Version
     * @return Entity Group Name
     */
    String getEntityGroupName();
    
    /**
     * Sets the Entity Group Name.
     * @param entityGroupName
     */
    void setEntityGroupName(String entityGroupName);

    /**
     * @return Returns the urlLocation.
     */
    String getUrlLocation();

    /**
     * Sets the url Location. This is same as the URL
     * @param urlLocation
     */
    void setUrlLocation(String urlLocation);
    
    /**
     * Returns whether the Service Instance is admin defined or not.
     * @return Returns the isAdminDefined.
     */
    boolean isAdminDefined();
    
    /**
     * Sets whether the instance is admin defined or not
     * @param isAdminDefined
     */
    void setAdminDefined(boolean isAdminDefined);

    /**
     * @return Returns the hosting center name.
     */
    String getHostingCenter();
    /**
     * @return Returns the hosting Center Name. But Do not use this method as it is used only by Hibernate. 
     */
    String getHostingResearchCenter();

    /**
     * Sets the Hosting Center Name.
     * @param hostingCenterName
     */
    void setHostingCenter(String hostingCenterName);
    
    /**
     * @return Returns the Service description.
     */
    String getDescription();
    
    /**
     * @return Returns the Service Description. But Do not use this method as it is used only by Hibernate.
     */
    String getServiceDescription();
    
    /**
     * Sets the service description
     * @param description
     */
    void setDescription(String description);
    
    /**
     * @return Returns whether ServiceInstance is configured by user or not
     */
    boolean isConfigured();
    
    /**
     * Sets whether the instance is configured by user or not
     * @param isAlive
     */
    void setConfigured(final boolean isAlive);
    
    /**
     * @return Hosting Center Short Name
     */
    String getHostingCenterShortName();
    
    /**
     * Sets the Hosting Center SHort Name
     * @param hostingCenterShortName
     */
    void setHostingCenterShortName(String hostingCenterShortName);
    
    /**
     * @return Contact Name of person to be contacted for further reference
     */
    String getContactName();
    
    /**
     * Sets the Contact Person Name
     * @param contactName
     */
    void setContactName(String contactName);
    
    /**
     * @return Mail Id of the person to be contacted
     */
    String getContactMailId();
    
    /**
     * Sets the Contact person Email Id
     * @param contactMailId
     */
    void setContactMailId(String contactMailId);
    
}

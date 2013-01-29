/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.user;

import java.io.Serializable;

import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.logger.Log4jLogger;

import org.apache.log4j.Logger;


/**
 * @author Hrishikesh Rajpathak
 * @author Gaurav Mehta
 * @hibernate.class table="CAB2B_SERVICE_URL"
 * This claas is a HO of CAB2B_SERVICE_URL.
 */

public class ServiceURL implements ServiceURLInterface, Serializable {
    private static final long serialVersionUID = 1L;
    
	private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ServiceURL.class);


    /** Unique Identifier for URLs  */
    private Long urlId;

    /** Stores URLs */
    private String urlLocation;

    /** Tells Whether URL is configured by Admin or not */
    private boolean isAdminDefined;

    /** Stores the Hosting Center Name. This variable is used by everyone and is not mapped to database */
    private String hostingCenter;

    /** It also gives the Hosting Center Name but it is mapped with database and nobody other than Hibernate uses it */
    private String hostingResearchCenter;

    /** Stores description for a URL but not mapped with database*/
    private String description;

    /** Stores description for a URL but mapped with database and used by Hibernate only */
    private String serviceDescription;

    /** Tells whether the URL is configured for that user */
    private boolean isConfigured;

    /** Stores the Domain Model Name associated with URL */
    private String domainModel;

    /** Stores the version of the domain model */
    private String version;
    
    /** Stores the Name of the person to be contacted */
    private String contactName;
    
    /** Stores the E-Mail ID of the person to be contacted */
    private String contactMailId;
    
    /** Stores the short name for Hosting center */
    private String hostingCenterShortName;

    /**
     * Gives the Hosting Center Name associated with the URL
     * @return hostingCenterName
     * @hibernate.id name="hostingResearchCenter" column="HOSTING_CENTER" type="String" length="254" not-null="false"
     */
    public String getHostingResearchCenter() {
        return hostingResearchCenter;
    }

    /**
     * Sets the Hosting Center Name associated with the URL
     * Only Hibernate uses it for its internal use.
     * @param hostingResearchCenter
     */
    private void setHostingResearchCenter(String hostingResearchCenter) {
        this.hostingResearchCenter = hostingResearchCenter;
    }

    /**
     * Gives the Description if any for the URL
     * @return description
     * @hibernate.id name="modelDescription" column="DESCRIPTION" type="String" not-null="false"
     */
    public String getServiceDescription() {
        return serviceDescription;
    }

    /**
     * Sets the Description if any for the URL
     * Only Hibernate uses it for its internal use
     * @param modelDescription
     */
    private void setServiceDescription(String modelDescription) {
        this.serviceDescription = modelDescription;
    }

    /**
     * Gives the Domain model associated with the URL
     * @return domainModelName
     * @hibernate.property column="DOMAIN_MODEL_NAME" type="string"
     *                     length="1024" not-null="true"
     */
    public String getDomainModel() {
        return domainModel;
    }

    /**
     * sets the Domain model associated with the URL
     * @param domainModel
     * sets the domain model
     */
    public void setDomainModel(String domainModel) {
        this.domainModel = domainModel;
    }

    /**
     * Gives the version of the Domain model associated with the URL
     * @return version
     * @hibernate.property column="version" type="string"
     *                     length="1024" not-null="true"
     */
    public String getVersion() {
        return version;
    }

    /**
     * sets the version of the Domain model associated with the URL
     * @param version
     * sets the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * It returns the EntityGroupName. This method is kept in order to minimize the number of changes to be made
     * for other classes using it. It still returns the EntityGroupName which was earlier a column in database
     * @return Returns the entityGroupId.
     */
    public String getEntityGroupName() {
        String modelName = getDomainModel();
        String versionNumber = getVersion();
        if (modelName != null && version != null) {
            return Utility.createModelName(modelName, versionNumber);
        } else {
            return "";
        }
    }

    /**
     * This method is also retained to have minimum impact of changes in database to user.
     * It is not mapped to database. It sets Domain Model and version internally by taking the EntityGroupName
     * @param entityGroupId
     * The entityGroupId to set.
     */
    public void setEntityGroupName(String entityGroupName) {
        String[] entityNameTokens = entityGroupName.split("_v");
        setDomainModel(entityNameTokens[0]);
        setVersion(entityNameTokens[1]);
    }

    /**
     * Getter for UrlId. It is auto generated.
     * @hibernate.id name="urlId" column="URL_ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="URL_ID_SEQ"
     */
    public Long getUrlId() {
        return urlId;
    }

    /**
     * Setter for UrlId.
     * @param urlId
     *            The urlId to set.
     */
    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    /**
     * Getter for URL
     * @return Returns the urlLocation.
     * @hibernate.property column="URL" type="string" length="1024"
     *                     not-null="true"
     */
    public String getUrlLocation() {
        return urlLocation;
    }

    /**
     * Setter for URL
     * @param urlLocation
     *            The urlLocation to set.
     */
    public void setUrlLocation(String urlLocation) {
        this.urlLocation = urlLocation;
    }

    /**
     * Getter for isAdminDefined
     * @return Returns the isAdminDefined.
     * @hibernate.property column="ADMIN_DEFINED" type="boolean" not-null="true"
     */
    public boolean isAdminDefined() {
        return isAdminDefined;
    }

    /**
     * Setter for isAdminDefined
     * @param isAdminDefined
     *            The isAdminDefined to set.
     */
    public void setAdminDefined(boolean isAdminDefined) {
        this.isAdminDefined = isAdminDefined;
    }
    
    /**
     * This method gets the Name of contact Person
     * @hibernate.property column="CONTACT_NAME" type="string" length="1024" not-null="true"
     * @return contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * This method sets the Name of contact person
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * This method gets the E-Mail id of contact person
     * @hibernate.property column="CONTACT_EMAIL" type="String" length="1024" not-null="true"
     * @return contact email id
     */
    public String getContactMailId() {
        return contactMailId;
    }

    /**
     * This method sets the E-Mail Id of contact person
     * @param contactMailId
     */
    public void setContactMailId(String contactMailId) {
        this.contactMailId = contactMailId;
    }
    
    /**
     * This method gets the short name for Hosting Research Center
     * @hibernate.property column="HOSTING_CENTER_SHORT_NAME" type="String" length="1024" not-null="true"
     * @return hosting Center Short Name
     */
    public String getHostingCenterShortName() {
        return hostingCenterShortName;
    }

    /**
     * This method sets the hosting center short name
     * @param hostingCenterShortName
     */
    public void setHostingCenterShortName(String hostingCenterShortName) {
        this.hostingCenterShortName = hostingCenterShortName;
    }
    
    /**
     * This is public method which sets ModelDescription internally, which is mapped to database
     * @param description Sets the description of the URL
     */
    public void setDescription(String description) {
        setServiceDescription(description);
    }

    /**
     * this function returns description based on 4 conditions
     * 1. If HC = null && Desc != null : Description : actual description
     * 2. If HC != null && Desc != null : URL : actual description
     * 3. If HC = null && DESC = null : nothing
     * 4. If HC != null && DESC = null :URL

     * @return Description of the URL
     * @see edu.wustl.cab2b.common.user.ServiceURLInterface#getDescription()
     */
    public String getDescription() {
        hostingCenter = getHostingResearchCenter();
        description = getServiceDescription();

        if (hostingCenter == null && description != null && description.length() != 0) {
            return "Description : " + description;
        } else if (hostingCenter != null && hostingCenter.length() != 0 && description != null
                && description.length() != 0) {
            return urlLocation + " : " + description;
        } else if (hostingCenter == null && description == null) {
            return "";
        } else if (hostingCenter != null && hostingCenter.length() != 0 && description == null) {
            return urlLocation;
        }
        return "";
    }

    /**
     * sets Hosting center name
     * @param hostingCenterName
     *        sets the Hosting Center Name of the URL
     */
    public void setHostingCenter(String hostingCenterName) {
        setHostingResearchCenter(hostingCenterName);
    }

    /**
     * this function returns URL if no hosting center name is associated with that URL else the original Hosting Center Name is returned.
     * @return Hosting Center Name of the URL
     * @see edu.wustl.cab2b.common.user.ServiceURLInterface#getHostingCenter()
     */
    public String getHostingCenter() {
        hostingCenter = getHostingResearchCenter();
        if (hostingCenter != null && hostingCenter.length() != 0) {
            return hostingCenter;
        }
        return urlLocation;
    }

    /**
     * returns whether URL has been configured for that user or not
     * @return .
     */
    public boolean isConfigured() {
        return this.isConfigured;
    }

    /**
     * sets the URL as configured for the User
     * @param isAlive The isAlive to set.
     */
    public void setConfigured(final boolean isAlive) {
        this.isConfigured = isAlive;
    }

    /**
     * This method is used for Struts framework only. 
     * As Struts Tag "logic" does not recognize isConfigured as a getter method for isconfigured
     * This method is used by Struts Tag "logic".
     * It is used in cab2bWebApp where user configured Service Instances are shown selected by default on JSP page. 
     * @return boolean value same as isConfigured(); 
     */
    public boolean getIsConfigured() {
        return isConfigured();
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

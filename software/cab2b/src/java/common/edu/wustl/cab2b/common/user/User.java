/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Hrishikesh Rajpathak
 * @hibernate.class table="CAB2B_USER"
 * 
 */
public class User implements UserInterface, Serializable {
    private static final long serialVersionUID = -4517981233726527311L;

    /**
     * Collection of service urls configured for the user. 
     */
    private Collection<ServiceURLInterface> serviceURLCollection;

    /**
     * Database user ID
     */
    private Long userId;

    /**
     * Database user name
     */
    private String userName;

    /**
     * password field.
     */
    private String password;

    /**
     * Flag to identify admin status of user. 
     */
    private boolean isAdmin;

    /**
     * Default constructor 
     */
    public User() {
        super();
    }

    /**
     * @param userName
     * @param password
     * @param isAdmin
     */
    public User(String userName, String password, boolean isAdmin) {
        this();
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * @hibernate.id name="userId" column="USER_ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="USER_ID_SEQ"
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return Returns the userName.
     * 
     * @hibernate.property column="NAME" type="string" length="254"
     *                     not-null="true"
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return Returns all urls configured for the user
     * 
     * @hibernate.set name="serviceURLCollection" cascade="save-update" lazy="false" inverse="false" table="CAB2B_USER_URL_MAPPING"
     * @hibernate.collection-key column="USER_ID" 
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.user.ServiceURL" column="SERVICE_URL_ID"
     */
    public Collection<ServiceURLInterface> getServiceURLCollection() {
        if (serviceURLCollection == null) {
            serviceURLCollection = new HashSet<ServiceURLInterface>();
        }
        return serviceURLCollection;
    }

    /**
     * @param serviceURLCollection The serviceURLCollection to set.
     */
    public void setServiceURLCollection(Collection<ServiceURLInterface> serviceURLCollection) {
        this.serviceURLCollection = serviceURLCollection;
    }

    /**
     * @return Returns the password.
     * 
     * @hibernate.property column="PASSWORD" type="string" length="30" not-null="false"
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Adds service url as a configured for the user object.
     * @param serviceURL
     */
    public void addServiceURL(ServiceURLInterface serviceURL) {
        getServiceURLCollection().add(serviceURL);
    }

    /**
     * Removes service url from a configured url collection for the user object. 
     * @param serviceURL
     */
    public void removeServiceURL(ServiceURLInterface serviceURL) {
        getServiceURLCollection().remove(serviceURL);
    }

    /**
     * @return Returns the isAdmin.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin
     *            The isAdmin to set.
     * 
     * @hibernate.property column="IS_ADMIN" type="boolean" not-null="true"
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg) {
        if (this == arg) {
            return true;
        }

        boolean isEqual = false;
        if (null != arg && arg instanceof User) {
            User user = (User) arg;

            if (userName != null && user.getUserName().equals(userName)) {
                isEqual = true;
            }
        }
        return isEqual;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        if (userName != null) {
            hashCode += 7 * userName.hashCode();
        }
        return hashCode;
    }
}

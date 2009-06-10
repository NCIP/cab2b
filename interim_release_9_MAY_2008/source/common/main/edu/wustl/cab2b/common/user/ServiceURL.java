package edu.wustl.cab2b.common.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;

/**
 * @author Hrishikesh Rajpathak
 * @hibernate.class table="CAB2B_SERVICE_URL"
 * @hibernate.cache usage="read-write"
 */
public class ServiceURL implements ServiceURLInterface, Serializable {
    private static final long serialVersionUID = 1L;

    private Long urlId;

    private String urlLocation;

    private String entityGroupName;

    private EntityGroupInterface entityGroupInterface;

    private Collection<UserInterface> userCollection = new HashSet<UserInterface>();

    private boolean isAdminDefined;

    public ServiceURL() {
        super();
    }

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
     * @return Returns the entityGroupInterface.
     */
    public EntityGroupInterface getEntityGroupInterface() {
        return entityGroupInterface;
    }

    /**
     * @param entityGroupInterface
     *            The entityGroupInterface to set.
     */
    public void setEntityGroupInterface(EntityGroupInterface entityGroupInterface) {
        this.entityGroupInterface = entityGroupInterface;
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
     * 
     * @hibernate.set name="userCollection" cascade="none" lazy="false"
     *                inverse="true" table="CAB2B_USER_URL_MAPPING"
     * @hibernate.collection-key column="SERVICE_URL_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.user.User"
     *                                    column="USER_ID"
     * @hibernate.cache usage="read-write"
     */
    public Collection<UserInterface> getUserCollection() {
        return userCollection;
    }

    /**
     * @param userCollection
     *            The userCollection to set.
     */
    public void setUserCollection(Collection<UserInterface> userCollection) {
        this.userCollection = userCollection;
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

    public void addUser(User user) {
        if (user != null) {
            userCollection.add(user);
        }
    }

    public void removeUser(User user) {
        if (user != null) {
            userCollection.remove(user);
        }
    }
}

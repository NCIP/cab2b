/**
 * 
 */
package edu.wustl.cab2b.common.modelgroup;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;

/**
 * This class is the HO for Model Group. It contains all the getters and setters for the member variables.
 * @author gaurav_mehta
 *
 */
public class ModelGroup implements ModelGroupInterface {

    /** Unique Identifier for Model Group*/
    private Long modelGroupId;

    /** Name of the Model Group*/
    private String modelGroupName;

    /** Whether that Model Group is Secured or not*/
    private boolean isSecured;

    /** Description for the Model Group */
    private String modelDescription;

    /** Concatenated list of Entity Group Name's which belongs to that Model Group*/
    private String entityGroupNames;

    /**
     * @return
     * @hibernate.id name="modelGroupId" column="MODEL_ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="MODEL_GROUP_ID_SEQ"
     */
    public Long getModelGroupId() {
        return modelGroupId;
    }

    /**
     * Sets the Model Group Id. Used by Hibernate as Id is auto generate
     * @param modelGroupId
     */
    private void setModelGroupId(Long modelGroupId) {
        this.modelGroupId = modelGroupId;
    }

    /**
     * @return Model Group Name
     * @hibernate.property name="modelName" column="NAME" type="string" length="30" not-null="true"
     */
    public String getModelGroupName() {
        return modelGroupName;
    }

    /**
     * Sets the Model Group Name
     * @param modelName
     */
    public void setModelGroupName(String modelName) {
        this.modelGroupName = modelName;
    }

    /**
     * @return whetehr that Model Group is secured or not
     * @hibernate.property name="isSecured" column="SECURED" type="boolean" not-null="true"
     */
    public boolean isSecured() {
        return isSecured;
    }

    /**
     * Sets whether that Model Group is secured or not
     * @param isSecured
     */
    public void setSecured(boolean isSecured) {
        this.isSecured = isSecured;
    }

    /**
     * Gets the Model Group Description
     * @return ModelGroupDescription
     * @hibernate.property name="modelDescription" column="DESCRIPTION" type="String" length="1024" not-null="true"
     */
    public String getModelDescription() {
        return modelDescription;
    }

    /**
     * Sets the description for the model group
     * @param modelDescription
     */
    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    /**
     * @return String of Entity Group Name's of Model Group
     * @hibernate.property name="entityGroupINames" column="ENTITY_GROUP_NAMES" type="" not-null="true"
     */
    private String getEntityGroupNames() {
        return entityGroupNames;
    }

    /**
     * Sets the Entity Group Name's of a Model Group
     * @param entityGroupIDs
     */
    private void setEntityGroupNames(String entityGroupIDs) {
        this.entityGroupNames = entityGroupIDs;
    }

    /**
     * This method is for converting Concatenated String of Entity Group Id's to List of Entity Group
     * @return List of Entity Group
     */
    public List<EntityGroupInterface> getEntityGroupList() {
        AbstractEntityCache entityCache = AbstractEntityCache.getCache();
        List<EntityGroupInterface> entityGroup = new ArrayList<EntityGroupInterface>();

        StringTokenizer stringTokens = new StringTokenizer(getEntityGroupNames(), "###");

        while (stringTokens.hasMoreTokens()) {
            entityGroup.add(entityCache.getEntityGroupByName(stringTokens.nextToken()));
        }
        return entityGroup;
    }

    /**
     * Sets the Entity Group List by converting the List into Sting of Entity Group Id's with delimiter "###"
     * @param entityGroupList
     */
    public void setEntityGroupList(List<EntityGroupInterface> entityGroupList) {
        StringBuffer entityGroupName = new StringBuffer();
        if (entityGroupList != null) {
            for (EntityGroupInterface entityGroup : entityGroupList) {
                entityGroupName.append(entityGroup.getName());
                entityGroupName.append("###");
            }
            setEntityGroupNames(entityGroupName.toString());
        }
    }
}

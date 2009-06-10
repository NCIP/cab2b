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

    /** Concatenated list of Entity Group Id's which belongs to that Model Group*/
    private String entityGroupIDs;

    /**
     * @return
     * @hibernate.id name="modelGroupId" column="MODEL_ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="MODEL_GROUP_ID_SEQ"
     */
    Long getModelGroupId() {
        return modelGroupId;
    }

    /**
     * Sets the Model Group Id. Used by Hibernate as Id is auto generate
     * @param modelGroupId
     */
    void setModelGroupId(Long modelGroupId) {
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
     * @return String of Entity Group Id's of Model Group
     * @hibernate.property name="entityGroupIDs" column="ENTITY_GROUP_IDS" type="" not-null="true"
     */
    private String getEntityGroupIDs() {
        return entityGroupIDs;
    }

    /**
     * Sets the Entity Group Id's of a Model Group
     * @param entityGroupIDs
     */
    private void setEntityGroupIDs(String entityGroupIDs) {
        this.entityGroupIDs = entityGroupIDs;
    }

    /**
     * This method is for converting Concatenated String of Entity Group Id's to List of Entity Group
     * @return List of Entity Group
     */
    public List<EntityGroupInterface> getEntityGroupList() {
        AbstractEntityCache entityCache = AbstractEntityCache.getCache();
        List<EntityGroupInterface> entityGroup = new ArrayList<EntityGroupInterface>();

        StringTokenizer stringTokens = new StringTokenizer(getEntityGroupIDs(), "###");

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
        StringBuffer entityGroupId = new StringBuffer();

        for (EntityGroupInterface entityGroup : entityGroupList) {
            entityGroupId.append(entityGroup.getName());
            entityGroupId.append("###");
        }
        setEntityGroupIDs(entityGroupId.toString());
    }

}

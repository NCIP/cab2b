package edu.wustl.cab2b.common.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * A class to group experiments under it.
 * @hibernate.joined-subclass table="CAB2B_EXPERIMENT_GROUP"
 * @hibernate.joined-subclass-key column="EXG_ID"
 * @author 
 */
public class ExperimentGroup extends AdditionalMetadata implements java.io.Serializable {

    private static final long serialVersionUID = 1234567890L;

    private ExperimentGroup parentGroup;

    /**
     * @return Returns the parent experiment group.
     * @hibernate.many-to-one column="PARENT_EXG_ID" class="edu.wustl.cab2b.common.domain.ExperimentGroup" constrained="true" lazy="false"
     */
    public ExperimentGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(ExperimentGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    /**
     * Collection of Experiments belonging to this Group.
     */
    private Collection<Experiment> experimentCollection = new HashSet<Experiment>();

    /**
     * Returns a collection of experiments grouped under this group.
     * @return collection of experiments grouped under this group.
     * @hibernate.set name="experimentCollection" table="CAB2B_EXP_GRP_MAPPING" cascade="none" inverse="true" lazy="false"
     * @hibernate.collection-key column="EXG_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.domain.Experiment" column="EXP_ID"
     */
    public Collection<Experiment> getExperimentCollection() {
        return experimentCollection;
    }

    /**
     * Sets the Experiment Collection.
     * @param experimentCollection
     */
    public void setExperimentCollection(Collection<Experiment> experimentCollection) {
        this.experimentCollection = experimentCollection;
    }

    private Collection<ExperimentGroup> childrenGroupCollection = new HashSet<ExperimentGroup>();

    /**
     * Returns a collection of experiment groups under this experiment group.
     * @return collection of experiment groups under this experiment group.
     * @hibernate.set name="childrenGroupCollection" table="CAB2B_EXPERIMENT_GROUP"  cascade="save-update" inverse="true" lazy="false"
     * @hibernate.collection-key column="PARENT_EXG_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.cab2b.common.domain.ExperimentGroup"
     */
    public Collection<ExperimentGroup> getChildrenGroupCollection() {
        return childrenGroupCollection;
    }

    public void setChildrenGroupCollection(Collection<ExperimentGroup> childrenGroupCollection) {
        this.childrenGroupCollection = childrenGroupCollection;
    }

    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof ExperimentGroup) {
            ExperimentGroup c = (ExperimentGroup) obj;
            Long thisId = getId();

            if (thisId != null && thisId.equals(c.getId())) {
                eq = true;
            }

        }
        return eq;
    }

    public int hashCode() {
        int h = 0;

        if (getId() != null) {
            h += getId().hashCode();
        }

        return h;
    }

}

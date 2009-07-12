package edu.wustl.cab2b.common.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * A class which represents a generic experiment.
 * @hibernate.joined-subclass table="CAB2B_EXPERIMENT"
 * @hibernate.joined-subclass-key column="EXP_ID"
 * @author 
 */
public class Experiment extends AdditionalMetadata implements java.io.Serializable {

    private static final long serialVersionUID = 1234567890L;

    private Collection<ExperimentGroup> experimentGroupCollection = new HashSet<ExperimentGroup>();

    private Collection<DataListMetadata> dataListMetadataCollection = new HashSet<DataListMetadata>();

    
    /**
     * Returns a collection of experiment groups to which this experiment belongs to.
     * @return a collection of experiment groups to which this experiment belongs to.
     * 
     * @hibernate.set name="experimentGroupCollection" table="CAB2B_EXP_GRP_MAPPING" cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="EXP_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.domain.ExperimentGroup" column="EXG_ID"
     */
    public Collection<ExperimentGroup> getExperimentGroupCollection() {
        return experimentGroupCollection;
    }

    /**
     * Sets the experiment group collection.
     * @param experimentGroupCollection
     */
    public void setExperimentGroupCollection(Collection<ExperimentGroup> experimentGroupCollection) {
        this.experimentGroupCollection = experimentGroupCollection;
    }

    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof Experiment) {
            Experiment c = (Experiment) obj;
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

    /**
     * Returns a collection of experiment groups to which this experiment belongs to.
     * @return a collection of experiment groups to which this experiment belongs to.
     * @hibernate.set name="dataListMetadataCollection" table="CAB2B_EXP_DLMETADATA_MAPPING" cascade="save-update" lazy="false"
     * @hibernate.collection-key column="EXP_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.domain.DataListMetadata" column="DL_ID"
     */
    public Collection<DataListMetadata> getDataListMetadataCollection() {
        return dataListMetadataCollection;
    }

    /**
     * @param dataListMetadataCollection
     */
    public void setDataListMetadataCollection(Collection<DataListMetadata> dataListMetadataCollection) {
        this.dataListMetadataCollection = dataListMetadataCollection;
    }

    /**
     * @param dataListMetadata
     */
    public void addDataListMetadata(DataListMetadata dataListMetadata) {
        dataListMetadataCollection.add(dataListMetadata);
    }

    public String toString() {
        return name;
    }
}

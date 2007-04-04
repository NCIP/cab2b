package edu.wustl.cab2b.common.domain;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

 /**
  * A class which represents a generic experiment.
  * @hibernate.joined-subclass table="experiment"
  * @hibernate.joined-subclass-key column="EXP_ID"
  * @author 
  */
public class Experiment extends AdditionalMetadata implements java.io.Serializable {
    
	private static final long serialVersionUID = 1234567890L;


    private java.util.Collection experimentGroupCollection = 
        new java.util.HashSet();
    
    private java.util.Collection dataListMetadataCollection = 
        new java.util.HashSet();
    
    /**
     * Returns a collection of experiment groups to which this experiment belongs to.
     * @return a collection of experiment groups to which this experiment belongs to.
     * @hibernate.set name="experimentGroupCollection" table="expgrpmapping" cascade="save-update" inverse="false"
     * @hibernate.collection-key column="EXP_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.domain.ExperimentGroup" column="EXG_ID"
     */
    public java.util.Collection getExperimentGroupCollection() {
        try {
            if (experimentGroupCollection.size() == 0) {
            }
        } catch (Exception e) {
            ApplicationService applicationService = 
                ApplicationServiceProvider.getApplicationService();
            try {


                edu.wustl.cab2b.common.domain.Experiment thisIdSet = 
                    new edu.wustl.cab2b.common.domain.Experiment();
                thisIdSet.setId(this.getId());
                java.util.Collection resultList = 
                    applicationService.search("edu.wustl.cab2b.common.domain.ExperimentGroup", 
                                              thisIdSet);
                experimentGroupCollection = resultList;
                return resultList;


            } catch (Exception ex) {
                System.out.println("Experiment:getExperimentGroupCollection throws exception ... ...");
                ex.printStackTrace();
            }
        }
        return experimentGroupCollection;
    }

    /**
     * Sets the experiment group collection.
     * @param experimentGroupCollection
     */
    public void setExperimentGroupCollection(java.util.Collection experimentGroupCollection) {
        this.experimentGroupCollection = experimentGroupCollection;
    }


    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof Experiment) {
            Experiment c = (Experiment)obj;
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
     * @hibernate.set name="dataListMetadataCollection" table="DLExpMap" cascade="save-update"
     * @hibernate.collection-key column="EXP_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.domain.DataListMetadata" column="DL_ID"
     */
	public java.util.Collection getDataListMetadataCollection()
	{
		return dataListMetadataCollection;
	}

	
	public void setDataListMetadataCollection(java.util.Collection dataListMetadataCollection)
	{
		this.dataListMetadataCollection = dataListMetadataCollection;
	}


}


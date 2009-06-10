package edu.wustl.cab2b.common.domain;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;


/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * A concrete class which represents a microarray virtual experiment.
 * @hibernate.joined-subclass table="microarrayexperiment"
 * @hibernate.joined-subclass-key column="MEX_ID"
 * @author 
 */
public class MicroarrayExperiment extends Experiment implements java.io.Serializable {
    
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Collection of Microarray chips.
	 */
    private java.util.Collection microarrayChipCollection = 
        new java.util.HashSet();
    
    /**
     * Returns a collection of microarray chips which belong to this experiment.
     * @return collection of microarray chips which belong to this experiment.
     * @hibernate.set name="microarrayChipCollection" table="microexpchipmap" cascade="save-update" inverse="true"
     * @hibernate.collection-key column="MEX_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.domain.MicroarrayChip" column="MIC_ID"
     */
    public java.util.Collection getMicroarrayChipCollection() {
        try {
            if (microarrayChipCollection.size() == 0) {
            }
        } catch (Exception e) {
            ApplicationService applicationService = 
                ApplicationServiceProvider.getApplicationService();
            try {


                edu.wustl.cab2b.common.domain.MicroarrayExperiment thisIdSet = 
                    new edu.wustl.cab2b.common.domain.MicroarrayExperiment();
                thisIdSet.setId(this.getId());
                java.util.Collection resultList = 
                    applicationService.search("edu.wustl.cab2b.common.domain.MicroarrayChip",  //"edu.wustl.cab2b.expressionData.MicroarrayChip"
                                              thisIdSet);
                microarrayChipCollection = resultList;
                return resultList;


            } catch (Exception ex) {
                System.out.println("MicroarrayExperiment:getMicroarrayChipCollection throws exception ... ...");
                ex.printStackTrace();
            }
        }
        return microarrayChipCollection;
    }

    /**
     * Sets microarray chip collection.
     * @param microarrayChipCollection
     */
    public void setMicroarrayChipCollection(java.util.Collection microarrayChipCollection) {
        this.microarrayChipCollection = microarrayChipCollection;
    }


    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof MicroarrayExperiment) {
            MicroarrayExperiment c = (MicroarrayExperiment)obj;
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

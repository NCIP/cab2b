package edu.wustl.cab2b.common.domain;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

 /**
   * A class representing a Microarray chip.
   * @hibernate.class table="microarraychip"
   * @author 
   */
public class MicroarrayChip implements java.io.Serializable {
    
	private static final long serialVersionUID = 1234567890L;

	/**
	 * A unique identifier.
	 */
    protected java.lang.Long id;
    
    /**
     * Returns the identifier assigned to MicroarrayChip.
     * 
     * @hibernate.id name="id" column="MIC_ID" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="MICROARRAY_CHIP_SEQ"
     * @return a unique id assigned to a microarray chip.
     */
    public java.lang.Long getId() {
        return id;
    }
    
    /**
     * Sets identifier.
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     * Description for the chip.
     */
    protected java.lang.String description;
    
    /**
     * Returns the description for the microarray chip.
     * @hibernate.property name="description" type="string" column="MIC_DESCRIPTION" length="200"
     * @return description for the microarray chip.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Sets description for the chip.
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Chip label.
     */
    protected java.lang.String label;
    
    /**
     * Returns the label of the microarray chip.
     * @return String representing the label of the Microarray chip.
     * @hibernate.property name="label" type="string"
     * column="MIC_LABEL" length="50"
     */
    public java.lang.String getLabel() {
        return label;
    }
    
    /**
     * Sets the chip label.
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    /**
     * Array design type of the chip.
     */
    private edu.wustl.cab2b.common.domain.ArrayDesign arrayDesign;
    
    /**
     * Returns the Array Design for this microarray chip.
     * @return array design for this microarray chip.
     * @hibernate.many-to-one column="ARD_ID" class="edu.wustl.cab2b.common.domain.ArrayDesign"
     */
    public edu.wustl.cab2b.common.domain.ArrayDesign getArrayDesign() {

    	if(arrayDesign != null)
    	{
    		return arrayDesign;
    	}
//        ApplicationService applicationService = 
//            ApplicationServiceProvider.getApplicationService();
//        edu.wustl.cab2b.common.domain.MicroarrayChip thisIdSet = 
//            new edu.wustl.cab2b.common.domain.MicroarrayChip();
//        thisIdSet.setId(this.getId());
//        System.out.println(applicationService+" <<-- applicationService >><< thisIdSet -->> "+thisIdSet+" this.getId "+this.getId());
//        try {
//            java.util.List resultList = 
//                applicationService.search("edu.wustl.cab2b.common.domain.ArrayDesign",  //"edu.wustl.cab2b.annotation.ArrayDesign"
//                                          thisIdSet);
//            if (resultList != null && resultList.size() > 0) {
//                arrayDesign = 
//                        (edu.wustl.cab2b.common.domain.ArrayDesign)resultList.get(0);
//            }
//
//        } catch (Exception ex) {
//            System.out.println("MicroarrayChip:getArrayDesign throws exception ... ...");
//            ex.printStackTrace();
//        }
        return null;


    }

    /**
     * Sets the array design type.
     * @param arrayDesign
     */    		
    public void setArrayDesign(edu.wustl.cab2b.common.domain.ArrayDesign arrayDesign) {
        this.arrayDesign = arrayDesign;
    }

    /**
     * A collection of microarray experiments this chip belongs to.
     */
    private java.util.Collection microarrayExperimentCollection = 
        new java.util.HashSet();
    
    /**
     * Returns a collection of experiments to which this chip belongs to.
     * @return collection of experiments to which this chip belongs to.
     * @hibernate.set name="microarrayExperimentCollection" table="microexpchipmap" cascade="none" inverse="false"
     * @hibernate.collection-key column="MIC_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.domain.MicroarrayExperiment" column="MEX_ID"
     */
    public java.util.Collection getMicroarrayExperimentCollection() {
        try {
            if (microarrayExperimentCollection.size() == 0) {
            }
        } catch (Exception e) {
            ApplicationService applicationService = 
                ApplicationServiceProvider.getApplicationService();
            try {


                edu.wustl.cab2b.common.domain.MicroarrayChip thisIdSet = 
                    new edu.wustl.cab2b.common.domain.MicroarrayChip();
                thisIdSet.setId(this.getId());
                java.util.Collection resultList = 
                    applicationService.search("edu.wustl.cab2b.common.domain.MicroarrayExperiment", 
                                              thisIdSet);
                microarrayExperimentCollection = resultList;
                return resultList;


            } catch (Exception ex) {
                System.out.println("MicroarrayChip:getMicroarrayExperimentCollection throws exception ... ...");
                ex.printStackTrace();
            }
        }
        return microarrayExperimentCollection;
    }

    /**
     * Sets the microarray experiment collection.
     * @param microarrayExperimentCollection
     */
    public void setMicroarrayExperimentCollection(java.util.Collection microarrayExperimentCollection) {
        this.microarrayExperimentCollection = microarrayExperimentCollection;
    }


    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof MicroarrayChip) {
            MicroarrayChip c = (MicroarrayChip)obj;
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

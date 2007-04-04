package edu.wustl.cab2b.common.domain;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

 /**
  * A class which represents an array design.
  * @hibernate.class table="arraydesign"
  * @author 
  */
public class ArrayDesign implements java.io.Serializable {
    
	private static final long serialVersionUID = 1234567890L;

	/**
	 * A unique identifier.
	 */
    protected java.lang.Long id;
    
    
    /**
     * Returns the identifier assigned to ArrayDesign.
     * 
     * @hibernate.id name="id" column="ARD_ID" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="ARRAY_DESIGN_SEQ"
     * @return a unique id assigned to the array design.
     */
    public java.lang.Long getId() {
        return id;
    }
    
    /**
     * Set the identifier.
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     * Array design name. 
     */
    protected java.lang.String name;
    
    /**
     * Returns the name of the array design.
     * @hibernate.property name="name" type="string" column="ARD_NAME" length="50"
     * @return name of the array design.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Sets the array design name.
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * A collection of microarray chips with this array design type.
     */
    private java.util.Collection microarrayChipCollection = 
        new java.util.HashSet();
    
    /**
     * Returns a collection of microarray chips which is of this design type.
     * @return collection of microarray chips which is of this design type.
     * @hibernate.set name="microarrayChipCollection" table="ArrayDesign" inverse="true"
     * @hibernate.collection-key column="ARD_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.cab2b.common.domain.MicroarrayChip"
     */
    public java.util.Collection getMicroarrayChipCollection() {
        try {
            if (microarrayChipCollection.size() == 0) {
            }
        } catch (Exception e) {
            ApplicationService applicationService = 
                ApplicationServiceProvider.getApplicationService();
            try {


                edu.wustl.cab2b.common.domain.ArrayDesign thisIdSet = 
                    new edu.wustl.cab2b.common.domain.ArrayDesign();
                thisIdSet.setId(this.getId());
                java.util.Collection resultList = 
                    applicationService.search("edu.wustl.cab2b.common.domain.MicroarrayChip", 
                                              thisIdSet);
                microarrayChipCollection = resultList;
                return resultList;


            } catch (Exception ex) {
                System.out.println("ArrayDesign:getMicroarrayChipCollection throws exception ... ...");
                ex.printStackTrace();
            }
        }
        return microarrayChipCollection;
    }

    /**
     * Sets the microarray chip collection.
     * @param microarrayChipCollection
     */
    public void setMicroarrayChipCollection(java.util.Collection microarrayChipCollection) {
        this.microarrayChipCollection = microarrayChipCollection;
    }


    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof ArrayDesign) {
            ArrayDesign c = (ArrayDesign)obj;
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

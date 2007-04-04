package edu.wustl.cab2b.common.domain;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

 /**
  * A class to group experiments under it.
  * @hibernate.joined-subclass table="experimentgroup"
  * @hibernate.joined-subclass-key column="EXG_ID"
  * @author 
  */
public class ExperimentGroup extends AdditionalMetadata implements java.io.Serializable {
    
	private static final long serialVersionUID = 1234567890L;


    private edu.wustl.cab2b.common.domain.ExperimentGroup parentGroup;
    
    /**
     * @return Returns the parent experiment group.
     * @hibernate.many-to-one column="PARENT_EXG_ID" class="edu.wustl.cab2b.common.domain.ExperimentGroup" constrained="true"
     */
    public edu.wustl.cab2b.common.domain.ExperimentGroup getParentGroup() {
    	if(parentGroup != null)
    	{
    		return parentGroup;
    	}
//        ApplicationService applicationService = 
//            ApplicationServiceProvider.getApplicationService();
//        edu.wustl.cab2b.common.domain.ExperimentGroup thisIdSet = 
//            new edu.wustl.cab2b.common.domain.ExperimentGroup();
//        thisIdSet.setId(this.getId());
//        System.out.println("ExperimentGroup:: getParentGroup:: thisIdSet >> "+thisIdSet.getName());
//        try {
//            java.util.List resultList = 
//                applicationService.search("edu.wustl.cab2b.common.domain.ExperimentGroup", 
//                                          thisIdSet);
//            if (resultList != null && resultList.size() > 0) {
//                parentGroup = 
//                        (edu.wustl.cab2b.common.domain.ExperimentGroup)resultList.get(0);
//            }
//
//        } catch (Exception ex) {
//            System.out.println("ExperimentGroup:getParentGroup throws exception ... ...");
//            ex.printStackTrace();
//        }
        return null;
    }

    public void setParentGroup(edu.wustl.cab2b.common.domain.ExperimentGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    /**
     * Collection of Experiments belonging to this Group.
     */
    private java.util.Collection experimentCollection = 
        new java.util.HashSet();
    
    /**
     * Returns a collection of experiments grouped under this group.
     * @return collection of experiments grouped under this group.
     * @hibernate.set name="experimentCollection" table="expgrpmapping" cascade="none" inverse="true" 
     * @hibernate.collection-key column="EXG_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.cab2b.common.domain.Experiment" column="EXP_ID"
     */
    public java.util.Collection getExperimentCollection() {
        try {
            if (experimentCollection.size() == 0) {
            }
        } catch (Exception e) {
            ApplicationService applicationService = 
                ApplicationServiceProvider.getApplicationService();
            try {


                edu.wustl.cab2b.common.domain.ExperimentGroup thisIdSet = 
                    new edu.wustl.cab2b.common.domain.ExperimentGroup();
                thisIdSet.setId(this.getId());
                java.util.Collection resultList = 
                    applicationService.search("edu.wustl.cab2b.common.domain.Experiment", thisIdSet);
                experimentCollection = resultList;
                return resultList;


            } catch (Exception ex) {
                System.out.println("ExperimentGroup:getExperimentCollection throws exception ... ...");
                ex.printStackTrace();
            }
        }
        return experimentCollection;
    }

    /**
     * Sets the Experiment Collection.
     * @param experimentCollection
     */
    public void setExperimentCollection(java.util.Collection experimentCollection) {
        this.experimentCollection = experimentCollection;
    }


    private java.util.Collection childrenGroupCollection = 
        new java.util.HashSet();
    
    /**
     * Returns a collection of experiment groups under this experiment group.
     * @return collection of experiment groups under this experiment group.
     * @hibernate.set name="childrenGroupCollection" table="experimentgroup"  cascade="save-update" inverse="true" lazy="false"
     * @hibernate.collection-key column="PARENT_EXG_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.cab2b.common.domain.ExperimentGroup"
     */
    public java.util.Collection getChildrenGroupCollection() {
        try {
            if (childrenGroupCollection.size() == 0) {
            }
        } catch (Exception e) {
            ApplicationService applicationService = 
                ApplicationServiceProvider.getApplicationService();
            try {


                edu.wustl.cab2b.common.domain.ExperimentGroup thisIdSet = 
                    new edu.wustl.cab2b.common.domain.ExperimentGroup();
                thisIdSet.setId(this.getId());
                java.util.Collection resultList = 
                    applicationService.search("edu.wustl.cab2b.common.domain.ExperimentGroup", 
                                              thisIdSet);
                childrenGroupCollection = resultList;
                return resultList;


            } catch (Exception ex) {
                System.out.println("ExperimentGroup:getChildrenGroupCollection throws exception ... ...");
                ex.printStackTrace();
            }
        }
        return childrenGroupCollection;
    }

    public void setChildrenGroupCollection(java.util.Collection childrenGroupCollection) {
        this.childrenGroupCollection = childrenGroupCollection;
    }


    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof ExperimentGroup) {
            ExperimentGroup c = (ExperimentGroup)obj;
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

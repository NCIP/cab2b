/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;

/**
 * @author gaurav_mehta
 *
 */
public class ModelGroupBizLogic {
    
    List<ModelGroupInterface> allModelGroupsList = new ArrayList<ModelGroupInterface>();
    
    /**
     * This method returns all the modelGroup present in database. 
     * @return List<ModelGroupInterface>
     */
    public List<ModelGroupInterface> getAllModelGroups() {
        List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
        modelGroups =  new ModelGroupOperations().getAllModelGroups();
        return modelGroups;
    }

    
    /**
     * This is method is not used anymore but initially it was required. 
     * This method is kept just in case it is required in future.
     * It returns the list of secured model groups from database 
     * @return List<ModelGroupInterface>
     */
    public List<ModelGroupInterface> getAllSecuredModelGroups() {
        List<ModelGroupInterface> securedModelGroups = new ArrayList<ModelGroupInterface>();
        securedModelGroups =  new ModelGroupOperations().getAllSecuredModelGroups();
        return securedModelGroups;
    }
    
    /**
     * This is method is not used anymore but initially it was required. 
     * This method is kept just in case it is required in future.
     * It returns the list of non secured model groups from database 
     * @return List<ModelGroupInterface>
     */
    public List<ModelGroupInterface> getAllNonSecuredModelGroups() {
        List<ModelGroupInterface> nonSecuredModelGroups = new ArrayList<ModelGroupInterface>();
        nonSecuredModelGroups =  new ModelGroupOperations().getAllNonSecuredModelGroups();
        return nonSecuredModelGroups;
    }
    
    /**
     * This method gets the list of EntityGroup for the particular model group
     * @param modelGroupName
     * @return list<EntityGroupInterface>
     */
    public List<EntityGroupInterface> getEntityGroupsForModel(String modelGroupName) {
        List<EntityGroupInterface> entityGroups = new ArrayList<EntityGroupInterface>();
        entityGroups = new ModelGroupOperations().getEntityGroupsForModel(modelGroupName);
        return entityGroups;
    }

}

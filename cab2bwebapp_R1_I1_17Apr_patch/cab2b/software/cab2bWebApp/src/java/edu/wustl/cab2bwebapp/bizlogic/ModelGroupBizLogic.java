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
    
    public List<ModelGroupInterface> getAllModelGroups() {
        List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
        modelGroups =  new ModelGroupOperations().getAllModelGroups();
        return modelGroups;
    }

    public List<ModelGroupInterface> getAllSecuredModelGroups() {
        List<ModelGroupInterface> securedModelGroups = new ArrayList<ModelGroupInterface>();
        securedModelGroups =  new ModelGroupOperations().getAllSecuredModelGroups();
        return securedModelGroups;
    }
    
    public List<ModelGroupInterface> getAllNonSecuredModelGroups() {
        List<ModelGroupInterface> nonSecuredModelGroups = new ArrayList<ModelGroupInterface>();
        nonSecuredModelGroups =  new ModelGroupOperations().getAllNonSecuredModelGroups();
        return nonSecuredModelGroups;
    }
    
    public List<EntityGroupInterface> getEntityGroupsForModel(String modelGroupName) {
        List<EntityGroupInterface> entityGroups = new ArrayList<EntityGroupInterface>();
        entityGroups = new ModelGroupOperations().getEntityGroupsForModel(modelGroupName);
        return entityGroups;
    }

}

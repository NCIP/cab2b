/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.server.modelgroup;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.modelgroup.ModelGroup;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

/**
 * This class performs operations related to the Model Group 
 * and provides some very important methods which directly gets 
 * data from database and returns required information about Model Group.
 * @author gaurav_mehta 
 */
public class ModelGroupOperations extends DefaultBizLogic {

    /**
     * Returns list of <code>EntityGroupInterface</code> for given modelGroupName. 
     * @param modelGroupName
     * @return List<EntityGroupInterface>
     */
    public List<EntityGroupInterface> getEntityGroupsForModel(String modelGroupName) {
        List<EntityGroupInterface> entityGroups = new ArrayList<EntityGroupInterface>();
        try {
            List<ModelGroupInterface> modelGroups =
                    retrieve(ModelGroup.class.getName(), "modelGroupName", modelGroupName);
            for (ModelGroupInterface modelGroup : modelGroups) {
                entityGroups.addAll(modelGroup.getEntityGroupList());
            }
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001));
        }
        return entityGroups;
    }
    
   /**
     * This method returns List of all ModelGroupInterface present in database 
     * @return List<ModelGroupInterface>
     */
    public List<ModelGroupInterface> getAllModelGroups() {
        List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
        try {
            modelGroups = retrieve(ModelGroupInterface.class.getName());
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001);
        }
        return modelGroups;
    }

    /**
     * This method returns List of all ModelGroupInterface present in database 
     * @return List<ModelGroupInterface>
     */
    public ModelGroupInterface getModelGroupById(Long modelGroupId) {
        List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
        try {
            modelGroups = retrieve(ModelGroupInterface.class.getName(), "modelGroupId", modelGroupId);
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001);
        }
        ModelGroupInterface modelGrp = getModelGroupFromList(modelGroups);
        return modelGrp;
    }

    private ModelGroupInterface getModelGroupFromList(List<ModelGroupInterface> modelGroups) {
        if (modelGroups == null || modelGroups.isEmpty()) {
            return null;
        }
        if (modelGroups.size() > 1) {
            throw new RuntimeException("Problem in code; probably db schema");
        }
        return (ModelGroupInterface) modelGroups.get(0);
    }

    /**
     * This method returns List of all Secured Model Groups from database. This method is not used as of now
     * @return List<ModelGroupInterface>
     */
    public List<ModelGroupInterface> getAllSecuredModelGroups() {
        List<ModelGroupInterface> securedModelGroups = new ArrayList<ModelGroupInterface>();
        try {
            securedModelGroups = retrieve(ModelGroupInterface.class.getName(), "secured", Boolean.TRUE);
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001);
        }
        return securedModelGroups;
    }

    /**
     * This method returns the List of all non secured Model Groups from database
     * @return List<ModelGroupInterface>
     */
    public List<ModelGroupInterface> getAllNonSecuredModelGroups() {
        List<ModelGroupInterface> nonSecuredModelGroups = new ArrayList<ModelGroupInterface>();
        try {
            nonSecuredModelGroups = retrieve(ModelGroupInterface.class.getName(), "secured", Boolean.FALSE);
        } catch (DAOException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001);
        }
        return nonSecuredModelGroups;
    }

    /**
     * This method saves the Model Group into database 
     * @param modelGroup
     * @throws BizLogicException 
     * @throws UserNotAuthorizedException 
     * @throws BizLogicException 
     * @throws UserNotAuthorizedException 
     */
    public void saveModelGroup(ModelGroupInterface modelGroup) {
        try {
            insert(modelGroup, Constants.HIBERNATE_DAO);
        } catch (UserNotAuthorizedException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_002);
        } catch (BizLogicException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_005);
        }
    }

    /**
     * This method updates the currently existing Model Group in database. This feature is yet to be implemented in any application
     * Just providing its API for future use
     * @param updatedModelGroup
     */
    public void updateModelGroup(ModelGroupInterface updatedModelGroup) {
        try {
            update(updatedModelGroup, Constants.HIBERNATE_DAO);
        } catch (UserNotAuthorizedException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_004);
        } catch (BizLogicException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_007);
        }
    }

    /**
     * This method deletes the existing Model Group from database. This feature is yet to be implemented in any application
     * Just providing its API for future use
     * @param modelGroup
     */
    public void deleteModelGroup(ModelGroupInterface modelGroup) {
        try {
            delete(modelGroup, Constants.HIBERNATE_DAO);
        } catch (UserNotAuthorizedException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_003);
        } catch (BizLogicException e) {
            throw new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_006);
        }
    }
}

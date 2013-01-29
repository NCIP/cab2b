/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.admin.bizlogic;

import java.util.ArrayList;
import java.util.Collection;

import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;

/**
 * @author gaurav_mehta
 *
 */
public class ModelGroupBizLogic {

    /**
     * This method saves the model group in database
     * @param modelGroup
     * @return boolean value depending on whether it was able to save it successfully or not.
     * @throws BizLogicException 
     * @throws UserNotAuthorizedException 
     * @throws BizLogicException 
     * @throws UserNotAuthorizedException 
     */
    public void saveModelGroup(ModelGroupInterface modelGroup) throws UserNotAuthorizedException,
            BizLogicException {
        new ModelGroupOperations().saveModelGroup(modelGroup);
    }

    public Collection<String> getAllModelGroupsName() {
        Collection<ModelGroupInterface> modelGroups = new ModelGroupOperations().getAllModelGroups();
        Collection<String> modelGroupName = new ArrayList<String>();
        for (ModelGroupInterface modelGroup : modelGroups) {
            modelGroupName.add(modelGroup.getModelGroupName());
        }
        return modelGroupName;
    }
}

/**
 * 
 */
package edu.wustl.cab2b.admin.bizlogic;

import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;

/**
 * @author gaurav_mehta
 *
 */
public class ModelGroupBizLogic {
    
    /**
     * This method saves the model group in database
     * @param modelGroup
     * @return boolean value depending on whether it was able to save it successfully or not.
     */
    public boolean saveModelGroup (ModelGroupInterface modelGroup) {
        return new ModelGroupOperations().saveModelGroup(modelGroup);
    }

}

/**
 * 
 */
package edu.wustl.cab2b.server.modelgroup;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author gaurav_mehta
 *
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
            List<ModelGroupInterface> modelGroups = retrieve(ModelGroupInterface.class.getName(),
                                                             "modelGroupName", modelGroupName);
            for (ModelGroupInterface modelGroup : modelGroups) {
                entityGroups.addAll(modelGroup.getEntityGroupList());
            }
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001));
        }
        return entityGroups;
    }

    public List<ModelGroupInterface> getAllModelGroups() {
        List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
        try {
            modelGroups = retrieve(ModelGroupInterface.class.getName());
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001));
        }
        return modelGroups;
    }

    public List<ModelGroupInterface> getAllSecuredModelGroups() {
        List<ModelGroupInterface> securedModelGroups = new ArrayList<ModelGroupInterface>();
        try {
            securedModelGroups = retrieve(ModelGroupInterface.class.getName(), "secured", Boolean.TRUE);
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001));
        }
        return securedModelGroups;
    }

    public List<ModelGroupInterface> getAllNonSecuredModelGroups() {
        List<ModelGroupInterface> nonSecuredModelGroups = new ArrayList<ModelGroupInterface>();
        try {
            nonSecuredModelGroups = retrieve(ModelGroupInterface.class.getName(), "secured", Boolean.FALSE);
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.MG_001));
        }
        return nonSecuredModelGroups;
    }
}

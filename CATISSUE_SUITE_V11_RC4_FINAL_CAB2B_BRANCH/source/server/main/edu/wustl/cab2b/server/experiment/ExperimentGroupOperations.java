/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.experiment;

import java.util.List;

import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;

public class ExperimentGroupOperations extends DefaultBizLogic {
    /**
     * Hibernate DAO Type to use.
     */
    int daoType = Constants.HIBERNATE_DAO;

    public ExperimentGroup addExperimentGroup(Object expGrp) throws BizLogicException, UserNotAuthorizedException {
        insert(expGrp, daoType);
        return (ExperimentGroup) expGrp;
    }

    public ExperimentGroup getExperimentGroup(Long identifier) throws DAOException {
        List expGrpList = retrieve("ExperimentGroup", "id", identifier);
        ExperimentGroup expGrp = null;
        if (!expGrpList.isEmpty()) {
            expGrp = (ExperimentGroup) expGrpList.get(0);
        }

        return expGrp;
    }

    /**
     * A callback validation function.
     */
    protected boolean validate(Object obj, DAO dao, String operation) throws DAOException {
        ExperimentGroup exp = ((ExperimentGroup) obj);
        Validator validator = new Validator();
        if (exp == null) {
            throw new DAOException("Null parameter passed instead of Experiment Group");
        }

        if (validator.isEmpty(exp.getName())) {
            throw new DAOException("Experiment group name empty");
        }
        return true;
    }

    /**
     * @param parentExperimentGroupId
     * @param experimentGroup
     * @return
     * @throws DAOException
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     */
    public ExperimentGroup addExperimentGroup(Long parentExperimentGroupId, ExperimentGroup experimentGroup,
                                              String dref) throws DAOException, BizLogicException,
            UserNotAuthorizedException {
        ExperimentGroup parentExperimentGroup = getExperimentGroup(parentExperimentGroupId);
        experimentGroup.setParentGroup(parentExperimentGroup);

        try {
            experimentGroup.setUserId(UserOperations.getGlobusCredential(dref).getIdentity());
        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }
        return addExperimentGroup(experimentGroup);
    }
}

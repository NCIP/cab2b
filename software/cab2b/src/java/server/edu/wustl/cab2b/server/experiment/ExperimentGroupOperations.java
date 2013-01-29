/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;

import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateUtility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;

/**
 * 
 * @author lalit_chand, rahul_ner
 *
 */
public class ExperimentGroupOperations extends DefaultBizLogic {

    /**
     * Hibernate DAO Type to use.
     */
    int daoType = Constants.HIBERNATE_DAO;

    /**
     * Adds experiment group
     * @param expGrp
     * @return
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     */
    public ExperimentGroup addExperimentGroup(Object expGrp) throws BizLogicException, UserNotAuthorizedException {
        insert(expGrp, daoType);
        return (ExperimentGroup) expGrp;
    }

    /**
     * Returns experiment group
     * @param identifier
     * @return
     * @throws DAOException
     */
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
     * @param obj
     * @param dao
     * @param operation
     * @return
     * @throws DAOException
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
     * Adds experiment group
     * @param parentExperimentGroupId
     * @param experimentGroup
     * @return
     * @throws DAOException
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     */
    public ExperimentGroup addExperimentGroup(Long parentExperimentGroupId, ExperimentGroup experimentGroup)
            throws DAOException, BizLogicException, UserNotAuthorizedException {
        ExperimentGroup parentExperimentGroup = getExperimentGroup(parentExperimentGroupId);
        experimentGroup.setParentGroup(parentExperimentGroup);

        return addExperimentGroup(experimentGroup);
    }

    /**
     * This method returns false if ExperimentGroup with given name is not present in the database.
     * It returns false otherwise.
     * @param name
     * @return
     */
    public boolean isExperimentGroupByNamePresent(String name) {
        List<Object> values = new ArrayList<Object>(1);
        values.add(name);

        Collection<ExperimentGroup> results = null;
        try {
            results = HibernateUtility.executeHQL("getExperimentGroupIdByName", values);
        } catch (HibernateException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.EX_004));
        }

        boolean present = false;
        if (results != null && results.size() == 1) {
            present = true;
        }
        return present;
    }
}

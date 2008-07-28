package edu.wustl.cab2b.server.ejb.experiment;

import java.rmi.RemoteException;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.experiment.ExperimentGroupBusinessInterface;
import edu.wustl.cab2b.server.experiment.ExperimentGroupOperations;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * A class containing experiment group related business logic.
 * 
 * @author chetan_bh
 * 
 */

@Remote(ExperimentGroupBusinessInterface.class)
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ExperimentGroupSessionBean implements ExperimentGroupBusinessInterface {

    private static final long serialVersionUID = -5216059908579963838L;

    public ExperimentGroup addExperimentGroup(Object expGrp) throws BizLogicException, UserNotAuthorizedException,
            RemoteException {
        return (new ExperimentGroupOperations()).addExperimentGroup(expGrp);
    }

    public ExperimentGroup getExperimentGroup(Long identifier) throws DAOException, RemoteException {
        return (new ExperimentGroupOperations()).getExperimentGroup(identifier);
    }

    /**
     * @throws DAOException
     * @see edu.wustl.cab2b.common.experiment.ExperimentGroupBusinessInterface#addExperimentGroup(java.lang.Long,
     *      edu.wustl.cab2b.common.domain.ExperimentGroup)
     */
    public ExperimentGroup addExperimentGroup(Long parentExperimentGroupId, ExperimentGroup experimentGroup)
            throws BizLogicException, UserNotAuthorizedException, RemoteException, DAOException {
        return (new ExperimentGroupOperations()).addExperimentGroup(parentExperimentGroupId, experimentGroup);
    }

}

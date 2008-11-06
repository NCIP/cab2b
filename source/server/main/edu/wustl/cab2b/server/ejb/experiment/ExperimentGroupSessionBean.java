package edu.wustl.cab2b.server.ejb.experiment;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.experiment.ExperimentGroupBusinessInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.experiment.ExperimentGroupOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * A class containing experiment group related business logic.
 * @author chetan_bh
 *
 */
public class ExperimentGroupSessionBean extends AbstractStatelessSessionBean implements
        ExperimentGroupBusinessInterface {

    private static final long serialVersionUID = -5216059908579963838L;

	/**
	 * Adds a experiment group 
	 * @param expGrp
	 * @return Reference to ExperimentGroup
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * @throws RemoteException
	 */
    public ExperimentGroup addExperimentGroup(Object expGrp) throws BizLogicException, UserNotAuthorizedException,
            RemoteException {
        return (new ExperimentGroupOperations()).addExperimentGroup(expGrp);
    }


    /**
     * Returns ExperimentGroup reference
     * @param identifier
     * @return
     * @throws DAOException
     * @throws RemoteException
     */
    public ExperimentGroup getExperimentGroup(Long identifier) throws DAOException, RemoteException {
        return (new ExperimentGroupOperations()).getExperimentGroup(identifier);
    }
    
    /**
     * Adds a experiment group to a given parent
     * @param parentExperimentGroupId
     * @param experimentGroup
     * @param dref
     * @param idP
     * @return Reference to ExperimentGroup
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws RemoteException
     * @throws DAOException 
     * @see edu.wustl.cab2b.common.experiment.ExperimentGroupBusinessInterface#addExperimentGroup(java.lang.Long, edu.wustl.cab2b.common.domain.ExperimentGroup)
     */
    public ExperimentGroup addExperimentGroup(Long parentExperimentGroupId, ExperimentGroup experimentGroup,
                                              String dref, String idP) throws BizLogicException,
            UserNotAuthorizedException, RemoteException, DAOException {

        try {
            UserOperations.getGlobusCredential(dref, idP).getIdentity();

        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }

        return (new ExperimentGroupOperations()).addExperimentGroup(parentExperimentGroupId, experimentGroup);
    }

}

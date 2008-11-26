package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

public interface ExperimentGroupBusinessInterface extends BusinessInterface {

	/**
	 * Adds a experiment group 
	 * @param expGrp
	 * @return Reference to ExperimentGroup
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * @throws RemoteException
	 */
    /*public ExperimentGroup addExperimentGroup(Object expGrp) throws BizLogicException, UserNotAuthorizedException,
            RemoteException;
    */
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
     */
    public ExperimentGroup addExperimentGroup(Long parentExperimentGroupId, ExperimentGroup experimentGroup,
                                              String dref, String idP) throws BizLogicException,
            UserNotAuthorizedException, RemoteException, DAOException;

    /**
     * Returns ExperimentGroup reference
     * @param identifier
     * @return
     * @throws DAOException
     * @throws RemoteException
     */
  //  public ExperimentGroup getExperimentGroup(Long identifier) throws DAOException, RemoteException;

}

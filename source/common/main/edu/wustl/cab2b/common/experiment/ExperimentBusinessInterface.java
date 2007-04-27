package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

public interface ExperimentBusinessInterface extends BusinessInterface {
	
	/**
	 * @param exp
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * @throws RemoteException
	 */
	public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException, RemoteException;
    
     /**
     * @param experimentGroupId
     * @param experiment
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws DAOException
     * @throws RemoteException 
     */
    public void addExperiment(Long experimentGroupId, Experiment experiment) throws BizLogicException,
     UserNotAuthorizedException, DAOException, RemoteException;
	
	/**
	 * @param exp
	 * @param srcExpGrp
	 * @param tarExpGrp
	 * @throws DAOException
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * @throws RemoteException
	 */
	public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws DAOException, BizLogicException, UserNotAuthorizedException, RemoteException;
	
	/**
	 * @param exp
	 * @param tarExpGrp
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * @throws RemoteException
	 */
	public void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException, RemoteException;
	
	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 * @throws RemoteException
	 */
	public Vector getExperimentHierarchy() throws ClassNotFoundException, DAOException, RemoteException;
	
	/**
	 * @param id
	 * @return
	 * @throws RemoteException
	 * @throws DAOException
	 */
	public Experiment getExperiment(Long id) throws RemoteException, DAOException;
	
	/**
	 * @param exp
	 * @return
	 * @throws RemoteException
	 */
	public Set<EntityInterface> getDataListEntityNames(Experiment exp) throws RemoteException;
}

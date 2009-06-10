package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;
import java.util.Vector;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

public interface ExperimentBusinessInterface extends BusinessInterface {
	
	public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException, RemoteException;
	
	public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws DAOException, BizLogicException, UserNotAuthorizedException, RemoteException;
	
	public void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException, RemoteException;
	
	public Vector getExperimentHierarchy() throws ClassNotFoundException, DAOException, RemoteException;
	
	
}

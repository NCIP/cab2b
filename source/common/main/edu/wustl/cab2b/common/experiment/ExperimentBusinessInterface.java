package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

public interface ExperimentBusinessInterface extends BusinessInterface {
	
	public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException, RemoteException;
	
	public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws DAOException, BizLogicException, UserNotAuthorizedException, RemoteException;
	
	public void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException, RemoteException;
	
	public Vector getExperimentHierarchy() throws ClassNotFoundException, DAOException, RemoteException;
	
	public Experiment getExperiment(Long id) throws RemoteException, DAOException;
	
	public Set<EntityInterface> getDataListEntityNames(Experiment exp) throws RemoteException;
}

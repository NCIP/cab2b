package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
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
    * get a set of root entities for an experiment where each root entity represents a datalist 
    * @param exp the experiment
    * @return set of root entities for an experiment where each root entity represents a datalist
    */
   public Set<EntityInterface> getDataListEntitySet(Experiment exp) throws RemoteException;
	
	
	/**
	 * save the given data as a data category
	 * @param title the title for the category
	 * @param attributes list of attributes needed for the  new entity
	 * @param data the data to be saved
	 * @return the newly saved entity
	 */
	public EntityInterface saveDataCategory(String title, List<AttributeInterface> attributes, Object[][] data) throws RemoteException;
	
	
}

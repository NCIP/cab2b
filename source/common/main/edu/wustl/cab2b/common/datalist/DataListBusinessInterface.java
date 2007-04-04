package edu.wustl.cab2b.common.datalist;

import java.rmi.RemoteException;
import java.util.List;

import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;


public interface DataListBusinessInterface extends BusinessInterface
{
	
	/**
	 * Saves the data list.
	 * @param dataList
	 * @throws UserNotAuthorizedException
	 * @throws RemoteException
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws BizLogicException 
	 * @throws DAOException 
	 */
	public Long saveDataList(DataList dataList) throws RemoteException, DynamicExtensionsApplicationException, DynamicExtensionsSystemException, DAOException, BizLogicException, UserNotAuthorizedException;

	/**
	 * Retrieves annotation information for all the data lists stored.
	 * @return
	 * @throws RemoteException
	 * @throws DynamicExtensionsSystemException 
	 * @throws ClassNotFoundException 
	 * @throws DAOException 
	 */
	public List<DataListMetadata> retrieveAllDataListMetadata() throws RemoteException, DynamicExtensionsSystemException, DAOException, ClassNotFoundException;

	/**
	 * Returns a data list along with annotation.
	 * @param dataListId
	 * @return a data list.
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * @throws RemoteException
	 */
	public DataList retreiveDataList(Long dataListId) throws BizLogicException, UserNotAuthorizedException, RemoteException;
	
}

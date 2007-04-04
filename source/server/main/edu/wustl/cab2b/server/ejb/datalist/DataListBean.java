
package edu.wustl.cab2b.server.ejb.datalist;

import java.rmi.RemoteException;
import java.util.List;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.server.datalist.DataListOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

public class DataListBean extends AbstractStatelessSessionBean 
						implements DataListBusinessInterface
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * @see DataListBusinessInterface#retrieveAllDataListMetadata()
	 */
	public List<DataListMetadata> retrieveAllDataListMetadata() throws BizLogicException,
			UserNotAuthorizedException, RemoteException, DAOException, ClassNotFoundException
	{
		return (new DataListOperations()).retrieveAllDataListMetadata();
	}

	/**
	 * @see DataListBusinessInterface#retreiveDataList(Long)
	 */
	public DataList retreiveDataList(Long dataListId) throws BizLogicException,
			UserNotAuthorizedException, RemoteException
	{
		return (new DataListOperations()).retreiveDataList(dataListId);
	}

	/**
	 * @throws RemoteException 
	 * @throws ClassNotFoundException 
	 * @see DataListBusinessInterface#saveDataList(DataList)
	 */
	public Long saveDataList(DataList dataList) throws BizLogicException,
			UserNotAuthorizedException, DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, DAOException, RemoteException, ClassNotFoundException
	{
		return (new DataListOperations()).save(dataList);
	}
}

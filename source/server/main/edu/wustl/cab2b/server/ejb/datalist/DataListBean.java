
package edu.wustl.cab2b.server.ejb.datalist;

import java.rmi.RemoteException;
import java.util.List;

import edu.common.dynamicextensions.entitymanager.EntityRecord;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.server.datalist.DataListOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;

/**
 * This class has methods to perform various oprations on data list,
 *  like save, retrieve operations on data list and its metadata, etc. 
 * 
 * @author chetan_bh
 */
public class DataListBean extends AbstractStatelessSessionBean 
						implements DataListBusinessInterface
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * @see DataListBusinessInterface#retrieveAllDataListMetadata()
	 */
	public List<DataListMetadata> retrieveAllDataListMetadata() throws 
			RemoteException
	{
		return (new DataListOperations()).retrieveAllDataListMetadata();
	}

	/**
	 * @see DataListBusinessInterface#retreiveDataList(Long)
	 */
	public DataList retrieveDataList(Long dataListId) throws RemoteException
	{
		return (new DataListOperations()).retrieveDataList(dataListId);
	}
	/**
	 * @see DataListBusinessInterface#retrieveDataListMetadata(Long)
	 */
	public DataListMetadata retrieveDataListMetadata(Long id) throws RemoteException
	{
		return (new DataListOperations()).retrieveDataListMetadata(id);
	}
	
	/**
	 * @see DataListBusinessInterface#saveDataList(DataList)
	 */
	public Long saveDataList(DataList dataList) throws RemoteException
	{
		return (new DataListOperations()).save(dataList);
	}

	public EntityRecordResultInterface getEntityRecord(Long entityId) throws RemoteException {
		return (new DataListOperations()).getEntityRecord(entityId);
	}
	
}

package edu.wustl.cab2b.common.datalist;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.IdName;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

public interface DataListBusinessInterface extends BusinessInterface {

    /**
     * Saves the data list.
     * @param dataList
     * @return data list id.
     * @throws RemoteException
     */
    public DataListMetadata saveDataList(IDataRow rootDataRow, DataListMetadata datalistMetadata)
            throws RemoteException;

    /**
     * Retrieves annotation information for all the data lists stored.
     * @return list of all available data list metadata.
     * @throws RemoteException
     */
    public List<DataListMetadata> retrieveAllDataListMetadata() throws RemoteException;

    /**
     * Retrives annotation for given datalist id.
     * @param id
     * @return data list metadata
     * @throws RemoteException
     */
    public DataListMetadata retrieveDataListMetadata(Long id) throws RemoteException;

    /**
     * Returns a data list along with annotation.
     * @param dataListId
     * @return a data list.
     * @throws RemoteException
     */
    public DataList retrieveDataList(Long dataListId) throws RemoteException;

    public List<IRecord> getEntityRecord(Long entityId) throws RemoteException;

    public DataListMetadata saveDataCategory(IDataRow rootRecordDataRow, DataListMetadata dataListMetadata,
                                             List<AttributeInterface> oldAttribute,
                                             List<AttributeInterface> newAttributes) throws RemoteException, CheckedException;

    public DataListMetadata saveCustomDataCategory(IdName rootEntityId,
                                                   Collection<AttributeInterface> selectedAttributeList, Long id,
                                                   String string, Experiment experiment) throws RemoteException,
            CheckedException;

}

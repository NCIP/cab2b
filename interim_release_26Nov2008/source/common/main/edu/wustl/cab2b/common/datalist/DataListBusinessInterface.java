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

/**
 * 
 * @author lalit_chand, deepak_shingan, chetan_bh, hrishikesh_rajpathak
 *
 */
public interface DataListBusinessInterface extends BusinessInterface {

    /**
     * Saves the data list.
     * @param dataList
     * @param dref -Its serialized User Credential in String
     * @return data list id.
     * @throws RemoteException
     */
    public DataListMetadata saveDataList(IDataRow rootDataRow, DataListMetadata datalistMetadata, String dref,
                                         String selectedIdentityProvider) throws RemoteException;

    /**
     * Retrieves annotation information for all the data lists stored.
     * @return list of all available data list metadata.
     * @throws RemoteException
     */
    public List<DataListMetadata> retrieveAllDataListMetadata(String dref, String selectedIdentityProvider)
            throws RemoteException;

    /**
     * Retrieves annotation for given datalist id.
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

    /**
     * Returns entity list.
     * @param entityId
     * @return
     * @throws RemoteException
     */
    public List<IRecord> getEntityRecord(Long entityId) throws RemoteException;

    /**
     * Returns saved data category.
     * @param rootRecordDataRow
     * @param dataListMetadata
     * @param oldAttribute
     * @param newAttributes
     * @param dref - it is serialized User Credential in String
     * @return Saved data category
     * @throws RemoteException
     * @throws CheckedException
     */
    public DataListMetadata saveDataCategory(IDataRow rootRecordDataRow, DataListMetadata dataListMetadata,
                                             List<AttributeInterface> oldAttribute,
                                             List<AttributeInterface> newAttributes, String dref,
                                             String selectedIdentityProvider) throws RemoteException,
            CheckedException;

    /**
     * Returns saved custom data category
     * @param rootEntityId
     * @param selectedAttributeList
     * @param string
     * @param experiment
     * @param dref
     * @param selectedIdentityProvider
     * @return Saved custom data category
     * @throws RemoteException
     * @throws CheckedException
     */
    public DataListMetadata saveCustomDataCategory(IdName rootEntityId,
                                                   Collection<AttributeInterface> selectedAttributeList,
                                                   String string, Experiment experiment, String dref,
                                                   String selectedIdentityProvider) throws RemoteException,
            CheckedException;

}

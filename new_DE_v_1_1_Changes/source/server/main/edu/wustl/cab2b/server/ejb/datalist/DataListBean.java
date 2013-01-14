/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.datalist;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.IdName;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.datalist.DataListMetadataOperations;
import edu.wustl.cab2b.server.datalist.DataListOperationsController;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.util.UserUtility;

/**
 * This class has methods to perform various operations on data list, like save,
 * retrieve operations on data list and its metadata, etc.
 * 
 * @author chetan_bh
 */
public class DataListBean extends AbstractStatelessSessionBean implements DataListBusinessInterface {
    private static final long serialVersionUID = 1234567890L;

    /**
     * Retrieves annotation information for all the data lists stored.
     * 
     * @param dref
     * @param selectedIdentityProvider
     * @return list of all available data list metadata.
     * @throws RemoteException
     * @see DataListBusinessInterface#retrieveAllDataListMetadata()
     */
    public List<DataListMetadata> retrieveAllDataListMetadata(String serializedDCR, String gridType)
            throws RemoteException {
        String userName = UserUtility.getUsersGridId(serializedDCR, gridType);
        return new DataListMetadataOperations().retrieveAllDataListMetadata(userName);
    }

    /**
     * Returns a data list along with annotation.
     * @param dataListId
     * @return a data list.
     * @throws RemoteException
     * @see DataListBusinessInterface#retreiveDataList(Long)
     */
    public DataList retrieveDataList(Long dataListId) throws RemoteException {
        // TODO
        return null;
    }

    /**
     * Retrieves annotation for given datalist id.
     * @param id
     * @return data list metadata
     * @throws RemoteException
     * @see DataListBusinessInterface#retrieveDataListMetadata(Long)
     */
    public DataListMetadata retrieveDataListMetadata(Long id) throws RemoteException {
        return new DataListMetadataOperations().retrieveDataListMetadata(id);
    }

    /**
     * Saves the data list.
     * @param rootDataRow
     * @param datalistMetadata
     * @param dref -Its serialized User Credential in String
     * @param serializedRef - client serialized reference
     * @param idP - type of grid
     * @return data list id.
     * @throws RemoteException
     * @see DataListBusinessInterface#saveDataList(DataList)
     */
    public DataListMetadata saveDataList(IDataRow rootDataRow, DataListMetadata datalistMetadata,
                                         String serializedDCR, String gridType) throws RemoteException {
        Long userId = UserUtility.getLocalUserId(serializedDCR, gridType);
        datalistMetadata.setUserId(userId);

        return new DataListOperationsController().saveDataList(rootDataRow, datalistMetadata);
    }

    /**
     * Returns entity list.
     * @param entityId
     * @return entity record
     * @throws RemoteException
     */
    public List<IRecord> getEntityRecord(Long entityId) throws RemoteException {
        return new DataListOperationsController().getEntityRecords(entityId);
    }

    /**
     * Returns saved data category.
     * @param rootRecordDataRow
     * @param dataListMetadata
     * @param oldAttribute
     * @param newAttributes
     * @param serializedRef
     * @param idP
     * @return Saved data category
     * @throws RemoteException
     * @throws CheckedException
     */
    public DataListMetadata saveDataCategory(IDataRow rootRecordDataRow, DataListMetadata dataListMetadata,
                                             List<AttributeInterface> oldAttribute,
                                             List<AttributeInterface> newAttributes, String serializedDCR,
                                             String gridType) throws RemoteException, CheckedException {
        Long userId = UserUtility.getLocalUserId(serializedDCR, gridType);
        dataListMetadata.setUserId(userId);

        return new DataListOperationsController().saveDataCategory(rootRecordDataRow, dataListMetadata,
                                                                   oldAttribute, newAttributes);
    }

    /**
     * Returns saved custom data category
     * @param rootEntityId
     * @param selectedAttributeList
     * @param string
     * @param experiment
     * @param dref
     * @param idP
     * @param selectedIdentityProvider
     * @return Saved custom data category
     * @throws RemoteException
     * @throws CheckedException
     */
    public DataListMetadata saveCustomDataCategory(IdName rootEntityId,
                                                   Collection<AttributeInterface> selectedAttributeList,
                                                   String string, Experiment experiment, String serializedDCR,
                                                   String gridType) throws RemoteException, CheckedException {
        Long userId = UserUtility.getLocalUserId(serializedDCR, gridType);
        return new DataListOperationsController().saveCustomDataCategory(rootEntityId, selectedAttributeList,
                                                                         string, experiment, userId);
    }

    /**
     * This method returns false if datalist with given name is not present in the database.
     * It returns false otherwise.
     * @param name
     * @return
     */
    public boolean isDataListByNamePresent(String name) {
        return new DataListOperationsController().isDataListByNamePresent(name);
    }
}

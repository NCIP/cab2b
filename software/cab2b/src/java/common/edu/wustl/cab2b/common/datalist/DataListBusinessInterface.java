/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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
 * @author lalit_chand, deepak_shingan, chetan_bh, hrishikesh_rajpathak, chetan_patil
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
    DataListMetadata saveDataList(IDataRow rootDataRow, DataListMetadata datalistMetadata, String dref)
            throws RemoteException;

    /**
     * Retrieves annotation information for all the data lists stored.
     * @return list of all available data list metadata.
     * @throws RemoteException
     */
    List<DataListMetadata> retrieveAllDataListMetadata(String dref) throws RemoteException;

    /**
     * Retrieves annotation for given datalist id.
     * @param id
     * @return data list metadata
     * @throws RemoteException
     */
    DataListMetadata retrieveDataListMetadata(Long id) throws RemoteException;

    /**
     * Returns a data list along with annotation.
     * @param dataListId
     * @return a data list.
     * @throws RemoteException
     */
    DataList retrieveDataList(Long dataListId) throws RemoteException;

    /**
     * Returns entity list.
     * @param entityId
     * @return
     * @throws RemoteException
     */
    List<IRecord> getEntityRecord(Long entityId) throws RemoteException;

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
    DataListMetadata saveDataCategory(IDataRow rootRecordDataRow, DataListMetadata dataListMetadata,
                                      List<AttributeInterface> oldAttribute,
                                      List<AttributeInterface> newAttributes, String dref) throws RemoteException,
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
    DataListMetadata saveCustomDataCategory(IdName rootEntityId,
                                            Collection<AttributeInterface> selectedAttributeList, String string,
                                            Experiment experiment, String dref) throws RemoteException,
            CheckedException;

    /**
     * This method returns false if datalist with given name is not present in the database.
     * It returns false otherwise.
     * @param name
     * @return
     */
    boolean isDataListByNamePresent(String name) throws RemoteException;
}

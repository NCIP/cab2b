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
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.datalist.DataListMetadataOperations;
import edu.wustl.cab2b.server.datalist.DataListOperationsController;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * This class has methods to perform various operations on data list, like save,
 * retrieve operations on data list and its metadata, etc.
 * 
 * @author chetan_bh
 */
public class DataListBean extends AbstractStatelessSessionBean implements DataListBusinessInterface {
    private static final long serialVersionUID = 1234567890L;

    /**
     * @see DataListBusinessInterface#retrieveAllDataListMetadata()
     */
    public List<DataListMetadata> retrieveAllDataListMetadata(String dref, String selectedIdentityProvider)
            throws RemoteException {

        String userId = null;
        try {
            userId = UserOperations.getGlobusCredential(dref, selectedIdentityProvider).getIdentity();
        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }
        return new DataListMetadataOperations().retrieveAllDataListMetadata(userId);
    }

    /**
     * @see DataListBusinessInterface#retreiveDataList(Long)
     */
    public DataList retrieveDataList(Long dataListId) throws RemoteException {
        // TODO
        return null;
    }

    /**
     * @see DataListBusinessInterface#retrieveDataListMetadata(Long)
     */
    public DataListMetadata retrieveDataListMetadata(Long id) throws RemoteException {
        return new DataListMetadataOperations().retrieveDataListMetadata(id);
    }

    /**
     * @see DataListBusinessInterface#saveDataList(DataList)
     */
    public DataListMetadata saveDataList(IDataRow rootDataRow, DataListMetadata datalistMetadata, String dref,
                                         String selectedIdentityProvider) throws RemoteException {
        String userId = null;
        try {
            userId = UserOperations.getGlobusCredential(dref, selectedIdentityProvider).getIdentity();

        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }

        datalistMetadata.setUserId(userId);
        return new DataListOperationsController().saveDataList(rootDataRow, datalistMetadata);

    }

    public List<IRecord> getEntityRecord(Long entityId) throws RemoteException {
        return new DataListOperationsController().getEntityRecords(entityId);
    }

    public DataListMetadata saveDataCategory(IDataRow rootRecordDataRow, DataListMetadata dataListMetadata,
                                             List<AttributeInterface> oldAttribute,
                                             List<AttributeInterface> newAttributes, String serializedRef,
                                             String selectedIdentityProvider) throws RemoteException,
            CheckedException {
        String userId = null;
        try {
            userId = UserOperations.getGlobusCredential(serializedRef, selectedIdentityProvider).getIdentity();

        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }

        dataListMetadata.setUserId(userId);
        return new DataListOperationsController().saveDataCategory(rootRecordDataRow, dataListMetadata,
                                                                   oldAttribute, newAttributes);
    }

    public DataListMetadata saveCustomDataCategory(IdName rootEntityId,
                                                   Collection<AttributeInterface> selectedAttributeList,
                                                   String string, Experiment experiment, String dref,
                                                   String selectedIdentityProvider) throws RemoteException,
            CheckedException {

        String userId = null;
        try {
            userId = UserOperations.getGlobusCredential(dref, selectedIdentityProvider).getIdentity();

        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }

        return new DataListOperationsController().saveCustomDataCategory(rootEntityId, selectedAttributeList,
                                                                         string, experiment, userId);
    }
}

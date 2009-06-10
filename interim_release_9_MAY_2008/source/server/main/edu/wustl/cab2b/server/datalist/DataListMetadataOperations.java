package edu.wustl.cab2b.server.datalist;

import java.util.List;

import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

public class DataListMetadataOperations extends DefaultBizLogic {
    /**
     * Returns a data list metadata.
     * 
     * @param id
     * @return
     */
    public DataListMetadata retrieveDataListMetadata(Long id) {
        List<DataListMetadata> results = null;
        try {
            results = retrieve(DataListMetadata.class.getName(), "id", id);
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_RETRIEVE_ERROR));
        }
        if (results != null && results.size() > 0)
            return results.get(0);
        return null;
    }

    /**
     * Returns a list of all available data list metadata.
     * 
     * @return list of data list metadata.
     */
    public List<DataListMetadata> retrieveAllDataListMetadata() {
        List<DataListMetadata> allDataList = null;

        try {
            allDataList = (List<DataListMetadata>) retrieve(DataListMetadata.class.getName());
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_RETRIEVE_ERROR));
        }
        return allDataList;
    }

    /**
     * Saves data list metadata.
     * 
     * @see DataListBusinessInterface#saveDataListMetadata(DataListMetadata)
     */
    public Long saveDataListMetadata(DataListMetadata datalistMetadata) {
        try {
            insert(datalistMetadata, Constants.HIBERNATE_DAO);
        } catch (BizLogicException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_SAVE_ERROR));
        } catch (UserNotAuthorizedException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_SAVE_ERROR));
        }
        return datalistMetadata.getId();
    }

}

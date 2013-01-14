/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;

import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateUtility;
import edu.wustl.common.util.global.Constants;

/**
 * 
 * @author lalit_chand, srinath_k
 *
 */
public class DataListMetadataOperations extends DefaultBizLogic {
    /**
     * Returns a data list metadata.
     * 
     * @param id
     * @return Datalist metadata
     */
    public DataListMetadata retrieveDataListMetadata(Long id) {
        List<DataListMetadata> results = null;
        try {
            results = retrieve(DataListMetadata.class.getName(), "id", id);
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_RETRIEVE_ERROR));
        }
        if (results != null && results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    /**
     * Returns a list of all available data list metadata.
     * 
     * @param userId
     * @return list of data list metadata.
     */
    public List<DataListMetadata> retrieveAllDataListMetadata(String userId) {
        List<DataListMetadata> allDataList = null;

        try {
            allDataList = (List<DataListMetadata>) retrieve(DataListMetadata.class.getName(), "USER_ID", userId);
        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_RETRIEVE_ERROR));
        }
        return allDataList;
    }

    /**
     * Saves data list metadata.
     * 
     * @param datalistMetadata 
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

    /**
     * This method returns false if datalist with given name is not present in the database.
     * It returns false otherwise.
     * @param name
     * @return
     */
    public boolean isDataListByNamePresent(String name) {
        List<Object> values = new ArrayList<Object>(1);
        values.add(name);

        Collection<DataListMetadata> results = null;
        try {
            results = HibernateUtility.executeHQL("getDataListIdByName", values);
        } catch (HibernateException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_RETRIEVE_ERROR));
        }

        boolean present = false;
        if (results != null && results.size() == 1) {
            present = true;
        }
        return present;
    }
}

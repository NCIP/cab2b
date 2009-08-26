package edu.wustl.cab2b.server.queryengine.querystatus;

import static edu.wustl.common.util.global.Constants.AND_JOIN_CONDITION;
import static edu.wustl.common.util.global.Constants.HIBERNATE_DAO;

import java.util.Collection;
import java.util.List;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * Class for saving/updating query status properties.  
 * @author  Chetan_pundhir
 */
public class QueryURLStatusOperations extends DefaultBizLogic {

    /**
     * This method inserts the query status object into database
     * @param queryStatus
     */
    public void insertQueryStatus(QueryStatus queryStatus) {
        try {
            insert(queryStatus, HIBERNATE_DAO);
        } catch (BizLogicException e) {
            throw new RuntimeException("Unable to save the QueryStatus", e, ErrorCodeConstants.QS_001);
        } catch (UserNotAuthorizedException e) {
            throw new RuntimeException("Unable to save the QueryStatus", e, ErrorCodeConstants.QS_005);
        }
    }

    /**
     * This method fetches all query status object for given user and query status is visible 
     * @param user
     * @return
     */
    public Collection<QueryStatus> getAllQueryStatusByUser(UserInterface user) {
        String[] columnNames = { "user", "visible" };
        String[] columnConditions = { "=", "=" };
        Object[] columnValues = { user, Boolean.TRUE };
        Collection<QueryStatus> allQueryStatus = null;
        try {
            allQueryStatus =
                    retrieve(QueryStatus.class.getName(), columnNames, columnConditions, columnValues,
                             AND_JOIN_CONDITION);
        } catch (DAOException e) {
            throw new RuntimeException(ErrorCodeConstants.QS_002);
        }
        return allQueryStatus;
    }

    /**
     * This method returns Query Status for given Query Status ID
     * @param id
     * @return
     */
    public QueryStatus getQueryStatusById(Long id) {
        List<QueryStatus> allQueryStatus = null;
        try {
            allQueryStatus = retrieve(QueryStatus.class.getName(), "id", id);
        } catch (DAOException e) {
            throw new RuntimeException(ErrorCodeConstants.QS_002);
        }
        QueryStatus queryStatus = null;
        if (allQueryStatus != null && allQueryStatus.size() == 1) {
            queryStatus = (QueryStatus) allQueryStatus.get(0);
        } else {
            throw new RuntimeException("No Query Status present for given Query Status Id");
        }
        return queryStatus;
    }

    /**
     * This method deletes the given Query Status object from database.
     * @param queryStatus
     */
    public void deleteQueryStatus(QueryStatus queryStatus) {
        try {
            delete(queryStatus, HIBERNATE_DAO);
        } catch (BizLogicException e) {
            throw new RuntimeException(ErrorCodeConstants.QS_003);
        } catch (UserNotAuthorizedException e) {
            throw new RuntimeException(ErrorCodeConstants.QS_005);
        }
    }

    /**
     * This method updates the given Query Status object in database
     * @param queryStatus
     */
    public void updateQueryStatus(QueryStatus queryStatus) {
        try {
            update(queryStatus, HIBERNATE_DAO);
        } catch (BizLogicException e) {
            throw new RuntimeException(ErrorCodeConstants.QS_004);
        } catch (UserNotAuthorizedException e) {
            throw new RuntimeException(ErrorCodeConstants.QS_005);
        }
    }
}
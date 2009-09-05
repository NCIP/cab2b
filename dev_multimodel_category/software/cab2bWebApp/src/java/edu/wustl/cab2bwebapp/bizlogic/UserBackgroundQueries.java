/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.JbossPropertyLoader;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryBizLogic;

/**
 * Class for doing operations related to background query execution.
 * @author deepak_shingan
 *
 */
public class UserBackgroundQueries {
    /**
     * Directory for saving exported/CSV files.
     */
    public static final String EXPORT_CSV_DIR = JbossPropertyLoader.getExportedResultsPath();

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(UserBackgroundQueries.class);
    
    private static UserBackgroundQueries ref = null;
    /**
     * Map of user to queries executing in background. 
     */
    private Map<UserInterface, Set<QueryBizLogic>> userToBackgroundQueries;

    static {
        //Refreshing every query background query periodically.
        QueryStatusUpdater.refreshQueryStatus();
    }

    /**
     * private constructor for singleton class. 
     */
    private UserBackgroundQueries() {
        userToBackgroundQueries = Collections.synchronizedMap(new HashMap<UserInterface, Set<QueryBizLogic>>());
    }

    /**
     * @return UserBackgroundQueries
     */
    public static UserBackgroundQueries getInstance() {
        if (ref == null) {
            ref = new UserBackgroundQueries();
        }
        return ref;
    }

    /*(non-Javadoc)
    * @see java.lang.Object#clone()*/
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Adds currently running query for background execution for the current user and updating properties in database.
     */
    public synchronized void addBackgroundQuery(UserInterface user, QueryBizLogic queryBizLogic) {
        if (queryBizLogic == null) {
            return;
        }
        //Update and saving to database.
        QueryStatus qStatus = queryBizLogic.getStatus();
        qStatus.setVisible(Boolean.TRUE);
        new QueryURLStatusOperations().updateQueryStatus(qStatus);
        synchronized (userToBackgroundQueries) {
            //Adding to user Vs background query map.
            Set<QueryBizLogic> bizLogicSet = userToBackgroundQueries.get(user);
            if (bizLogicSet == null) {
                bizLogicSet = new HashSet<QueryBizLogic>();
                userToBackgroundQueries.put(user, bizLogicSet);
            }
            bizLogicSet.add(queryBizLogic);
        }
    }

    /**
     * Call update in database for all queries executing in background. 
     */
    public synchronized void updateAllBackgroundQueryStatus() {
        synchronized (userToBackgroundQueries) {
            Set<UserInterface> users = userToBackgroundQueries.keySet();
            if (users != null) {
                for (UserInterface user : users) {
                    updateBackgroundQueryStatusForUser(user);
                }
            }
        }
    }

    /**
     * Call update in database for all queries executing in background for the given user.
     * @param user
     */
    public synchronized void updateBackgroundQueryStatusForUser(UserInterface user) {
        synchronized (userToBackgroundQueries) {
            Set<QueryBizLogic> querieLogics = userToBackgroundQueries.get(user);
            if (querieLogics != null) {
                for (QueryBizLogic queryBizLogic : querieLogics) {
                    QueryStatus status = queryBizLogic.getStatus();
                    //If query is complete Write results into CSV file and update map with file name.
                    if (isBackGroundQueryComplete(status)) {
                        String fileName = queryBizLogic.exportToCSV();
                        status.setFileName(fileName);
                        status.setQueryEndTime(new Date());
                        logger.debug(EXPORT_CSV_DIR + File.separator + fileName);
                    }
                    //updating query status in database.
                    new QueryURLStatusOperations().updateQueryStatus(status);
                }
            }
        }
        removeCompletedQueriesFromBackground(user);
    }

    /**
     * Removes all background completed queries from memomry. 
     * @param user
     */
    public void removeCompletedQueriesFromBackground(UserInterface user) {
        synchronized (userToBackgroundQueries) {
            Set<QueryBizLogic> queryLogics = userToBackgroundQueries.get(user);
            if (queryLogics != null && !queryLogics.isEmpty()) {
                Set<QueryBizLogic> completedQueries = new HashSet<QueryBizLogic>();
                for (QueryBizLogic queryBizLogic : queryLogics) {
                    QueryStatus status = queryBizLogic.getStatus();
                    //If query is complete write results into map and remove it's reference from map.
                    if (isBackGroundQueryComplete(status)) {
                        logger.info("Removing query " + queryBizLogic.getStatus().getQuery().getName()
                                + " for user" + queryBizLogic.getStatus().getUser().getUserName());
                        completedQueries.add(queryBizLogic);
                    }
                }
                queryLogics.removeAll(completedQueries);
            }

            if (queryLogics == null || queryLogics.isEmpty()) {
                logger.info("Removing user " + user.getUserName());
                //No queries running in background for the selected user.
                //remove user from map list               
                userToBackgroundQueries.remove(user);
            }
        }
    }

    /**
     * Returns true if background query is complete else returns false. 
     * @param status
     * @return
     */
    private boolean isBackGroundQueryComplete(QueryStatus status) {
        if ((status.getStatus().equals(AbstractStatus.Complete) || status.getStatus()
            .equals(AbstractStatus.Complete_With_Error))
                && (status.getFileName() != null || status.getStatus().equals(AbstractStatus.FAILED))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns set QueryStatus for live queries working in background.
     * @param user
     * @return
     */
    public Set<QueryStatus> getBackgroundQueriesForUser(UserInterface user) {
        synchronized (userToBackgroundQueries) {
            Set<QueryBizLogic> queryLogics = userToBackgroundQueries.get(user);
            Set<QueryStatus> qSet = new HashSet<QueryStatus>();
            if (queryLogics != null) {
                qSet = new HashSet<QueryStatus>(queryLogics.size());
                for (QueryBizLogic qBizLogic : queryLogics) {
                    qSet.add(qBizLogic.getStatus());
                }
            }
            return qSet;
        }
    }
}

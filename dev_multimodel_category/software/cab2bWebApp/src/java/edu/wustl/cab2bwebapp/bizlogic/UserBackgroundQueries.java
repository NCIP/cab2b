/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryBizLogic;

/**
 * @author deepak_shingan
 *
 */
public class UserBackgroundQueries {
    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(UserBackgroundQueries.class);

    /**
     * Map of user to queries executing in background. 
     */
    private Map<UserInterface, Set<QueryBizLogic>> userToBackgroundQueries = null;

    private static UserBackgroundQueries ref = null;

    /**
     * Directory for saving exported/CSV files.
     */
    public static final String EXPORT_CSV_DIR =
            System.getProperty("user.home") + File.separator + "cab2bExportFiles";

    /**
     * Time interval for updating database for queries executing in background. 
     */
    public static final int BACKGROUND_QUERY_REFRESH_TIME_INTERVAL = 5;

    static {
        //Creating directory for saving CSV files on server.
        new File(EXPORT_CSV_DIR).mkdir();

        //Refreshing every query background query periodically.
        QueryStatusUpdater.refreshQueryStatus();
    }

    /**
     * private constructor for singleton class. 
     */
    private UserBackgroundQueries() {
        userToBackgroundQueries = new HashMap<UserInterface, Set<QueryBizLogic>>();
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
    public void addBackgroundQuery(UserInterface user, QueryBizLogic queryBizLogic) {
        //Update and saving to database.
        QueryStatus qStatus = queryBizLogic.getStatus();
        qStatus.setVisible(Boolean.TRUE);
        new QueryURLStatusOperations().updateQueryStatus(qStatus);

        //Adding to user Vs background query map.
        Set<QueryBizLogic> bizLogicSet = userToBackgroundQueries.get(user);
        if (bizLogicSet == null) {
            bizLogicSet = new HashSet<QueryBizLogic>();
            userToBackgroundQueries.put(user, bizLogicSet);
        }
        bizLogicSet.add(queryBizLogic);
    }

    /**
     * Call update in database for all queries executing in background. 
     */
    public synchronized void updateAllBackgroundQueryStatus() {
        Set<UserInterface> users = userToBackgroundQueries.keySet();
        if (users != null) {
            for (UserInterface user : users) {
                updateBackgroundQueryStatusForUser(user);
            }
        }
    }

    /**
     * Call update in database for all queries executing in background for the given user.
     * @param user
     */
    public synchronized void updateBackgroundQueryStatusForUser(UserInterface user) {

        Set<QueryBizLogic> querieLogics = userToBackgroundQueries.get(user);
        if (querieLogics != null) {
            for (QueryBizLogic queryBizLogic : querieLogics) {
                QueryStatus status = queryBizLogic.getStatus();

                //If query is complete Write results into CSV file and update map with file name.
                if (status.getStatus().equals(AbstractStatus.Complete)
                        || status.getStatus().equals(AbstractStatus.Complete_With_Error)) {
                    try {
                        String fileName = queryBizLogic.exportToCSV();
                        status.setFileName(fileName);
                        status.setQueryEndTime(new Date());
                        logger.info(EXPORT_CSV_DIR + File.separator + fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error while saving CSV file.", ErrorCodeConstants.IO_0001);
                    }
                }
                //updating query status in database.
                new QueryURLStatusOperations().updateQueryStatus(status);
            }
        }
        removeCompletedQueriesFromBackground(user);
    }

    /**
     * Removes all background completed queries from memomry. 
     * @param user
     */
    private void removeCompletedQueriesFromBackground(UserInterface user) {
        Set<QueryBizLogic> querieLogics = userToBackgroundQueries.get(user);
        if (querieLogics != null) {
            for (QueryBizLogic queryBizLogic : querieLogics) {
                QueryStatus status = queryBizLogic.getStatus();

                //If query is complete Write results into map and remove it's reference from map.
                if (status.getStatus().equals(AbstractStatus.Complete)
                        || status.getStatus().equals(AbstractStatus.Complete_With_Error)) {
                    logger.info("Removing :" + queryBizLogic.getStatus().getQuery().getName() + " from user "
                            + user.getUserName() + " mapping list.");
                    querieLogics.remove(queryBizLogic);
                }
                //updating query status in database.
                new QueryURLStatusOperations().updateQueryStatus(status);
            }
        } else {
            //No queries running in background for the selected user.
            //Remove user from map list
            logger
                .info("No queries running in background for the selected user.Removing user object (if present) from user to background query mapping.");
            userToBackgroundQueries.remove(user);
        }
    }

}

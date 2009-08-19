/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.server.initializer.PollingTask;
import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;

/**
 * @author deepak_shingan
 *
 */
public class QueryStatusUpdater {
    private final static Logger logger = Logger.getLogger(QueryStatusUpdater.class);

    /** It allows only one time entry into the thread for refreshing query status database */
    private static AtomicBoolean isStarted = new AtomicBoolean(false);

    public static void refreshQueryStatus() {
        if (!isStarted.getAndSet(true)) {
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    logger.info("Refreshing Database for background queries Started..........");
                    UserBackgroundQueries.getInstance().updateAllBackgroundQueryStatus();
                    logger.info("Database has been refreshed and is in Sync with background queries status");
                }
            };
            PollingTask.submitTask(timerTask, 10);
        }
    }
}

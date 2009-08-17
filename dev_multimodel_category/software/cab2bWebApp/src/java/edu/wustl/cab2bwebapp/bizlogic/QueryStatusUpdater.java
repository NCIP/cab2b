/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

/**
 * @author deepak_shingan
 *
 */
public class QueryStatusUpdater {
    private final static Logger logger = Logger.getLogger(QueryStatusUpdater.class);

    /** It allows only one time entry into the thread for refreshing ServiceURL database */
    private static AtomicBoolean isStarted = new AtomicBoolean(false);

    public void refreshQueryStatus() {
        if (!isStarted.getAndSet(true)) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    logger.info("Refreshing Database for background queries Started..........");
                    UserBackgroundQueries.getInstance().updateAllBackgroundQueryStatus();
                    logger.info("Database has been refreshed and is in Sync with background queries status");
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 6000 * 10);
        }
    }
}

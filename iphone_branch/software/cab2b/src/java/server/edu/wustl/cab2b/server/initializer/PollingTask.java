/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.server.initializer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author gaurav_mehta
 * This class is for those tasks which required to be executed after certain period of time
 *
 */
public class PollingTask {

    /**
     * This method accepts Timer task and time interval in minutes after which they are to be called again.
     * @param task
     * @param timeInterval
     */
    public static void submitTask(TimerTask task, int timeInterval) {
        Timer timer = new Timer();
        timeInterval = timeInterval * 60000;
        timer.scheduleAtFixedRate(task,0, timeInterval);
    }
}

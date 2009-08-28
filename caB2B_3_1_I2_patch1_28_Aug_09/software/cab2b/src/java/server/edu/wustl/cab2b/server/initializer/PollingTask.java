/**
 * 
 */
package edu.wustl.cab2b.server.initializer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author gaurav_mehta
 *
 */
public class PollingTask {

    public static void submitTask(TimerTask task, int timeInterval) {
        Timer timer = new Timer();
        timeInterval = timeInterval * 60000;
        timer.scheduleAtFixedRate(task,0, timeInterval);
    }
}

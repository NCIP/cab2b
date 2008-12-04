package edu.wustl.cab2b.server.util;

import java.util.Timer;
import java.util.TimerTask;

import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class TimerUtill {

    private static boolean flag = false;

    private static boolean flagTimer = false;

    private static final org.apache.log4j.Logger logger = Logger.getLogger(TimerUtill.class);

    public static void initilizeSync() {
        synchronized (TimerUtill.class) {
            if (!flag) {
                logger.info("Synchronizing 1st time...");
                Utility.syncGlobusCredential();
                flag = true;
            } else if (!flagTimer) {
                synchronizeCert();
            }
        }

    }

    private static void synchronizeCert() {
        //Creates a thread for synchronizing .globus credential for server in every 10 hr
        flagTimer = true;

        final org.apache.log4j.Logger logger = Logger.getLogger(TimerTask.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                logger.info("Thread started for synchronizing .glous credential ");
                Utility.syncGlobusCredential();
            }
        };
        timer.scheduleAtFixedRate(task, 3600000 * 1, 3600000 * 10);
    }

}

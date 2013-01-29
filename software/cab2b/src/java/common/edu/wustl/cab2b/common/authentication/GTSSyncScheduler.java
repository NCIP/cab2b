/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.authentication;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.util.logger.Logger;

public class GTSSyncScheduler {
    private static final org.apache.log4j.Logger logger = Logger.getLogger(GTSSyncScheduler.class);

    private static AtomicBoolean isStarted = new AtomicBoolean(false);

    public static void initializeScheduler(final long start, final long interval) {
        if (!isStarted.getAndSet(true)) {
            synchronizeCert(start, interval);
        }
    }

    private static void synchronizeCert(final long start, final long interval) {
        //Creates a thread for synchronizing .globus credential for server in every 10 hr
        final org.apache.log4j.Logger logger = Logger.getLogger(TimerTask.class);

        TimerTask task = new TimerTask() {
            public void run() {
                logger.info("Thread started for synchronizing glous credential ");
                try {
                    GTSSynchronizer.generateGlobusCertificate();
                } catch (RuntimeException e) {
                    logger.error("Cannot synchronize with trust fabric: " + e.getMessage(), e);
                    throw e;
                }
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, start, interval);
    }
}

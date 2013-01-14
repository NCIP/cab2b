/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class TimerUtill {

    private static boolean flag = false;

    private static boolean flagTimer = false;

    private static final org.apache.log4j.Logger logger = Logger.getLogger(TimerUtill.class);

    public static synchronized void initilizeSync() {
        if (!flag) {
            logger.info("Synchronizing 1st time...");
            syncGlobusCredential();
            flag = true;
        } else if (!flagTimer) {
            synchronizeCert();
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
                syncGlobusCredential();
            }
        };
        timer.scheduleAtFixedRate(task, 3600000 * 1, 3600000 * 10);
    }

    /**
     * This method sync the globus credential of server
     */
    private static void syncGlobusCredential() {
        try {
            Utility.generateGlobusCertificate("Production");
        } catch (RuntimeException e) {
            logger.error("Cannot Create Production Grid Certificate", e);
        }

        File certDir = gov.nih.nci.cagrid.common.Utils.getTrustedCerificatesDirectory();
        File tempDir = new File(System.getProperty("user.home") + File.separator + "temp_dir");
        try {
            FileUtils.copyDirectory(certDir, tempDir);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to copy CA certificates to [user.home]/.globus",
                    ErrorCodeConstants.CDS_003);
        }

        try {
            Utility.generateGlobusCertificate("Training");
        } catch (RuntimeException e) {
            logger.error("Cannot Create Training Grid Certificate", e);
        }

        try {
            FileUtils.copyDirectory(tempDir, certDir);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to copy CA certificates to [user.home]/.globus",
                    ErrorCodeConstants.CDS_003);
        }
        
        try {
            FileUtils.deleteDirectory(tempDir);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}

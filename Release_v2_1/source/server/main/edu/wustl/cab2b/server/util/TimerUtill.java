package edu.wustl.cab2b.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

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
        File commonDir = new File(System.getProperty("user.home") + File.separator + "commonDirCert");
        commonDir.mkdir();

        try {
            Utility.generateGlobusCertificate("Production");
        } catch (RuntimeException re) {
            logger.error("Cannot Create Production Grid Certificate", re);
        }
        copyToCommonPlace(commonDir);

        try {
            Utility.generateGlobusCertificate("Training");
        } catch (RuntimeException re) {
            logger.error("Cannot Create Training Grid Certificate", re);
        }

        File dir = gov.nih.nci.cagrid.common.Utils.getTrustedCerificatesDirectory();
        if (commonDir != null && commonDir.isDirectory()) {
            for (File files : commonDir.listFiles()) {
                copyFiles(files, dir);
            }
        }
    }

    private static void copyToCommonPlace(File commonDir) {
        File dir = gov.nih.nci.cagrid.common.Utils.getTrustedCerificatesDirectory();
        if (dir != null && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                copyFiles(file, commonDir);
            }
        }
    }

    private static void copyFiles(File file, File commonDir) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(file.getPath() + " file not found", ErrorCodeConstants.CDS_016);
        }

        File dest = new File(commonDir + File.separator + file.getName());
        OutputStream out = null;
        try {
            out = new FileOutputStream(dest);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(dest.getPath() + " file not found", ErrorCodeConstants.CDS_016);
        }

        byte[] buf = new byte[1024];
        int len = 0;
        try {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to copy CA certificates to [user.home]/.globus",
                    ErrorCodeConstants.CDS_003);
        }
    }

}

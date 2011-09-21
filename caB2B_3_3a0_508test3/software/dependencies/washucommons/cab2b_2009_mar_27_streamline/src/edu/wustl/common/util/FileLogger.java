package edu.wustl.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger<T> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy H_m_s_S");

    private final String basicFilePath;

    private static final String LOG_FILE_NAME_PREFIX = "file";

    private int i = 1;

    private final boolean logEnabled;

    private final String logDir;

    public FileLogger(boolean createBaseDir, boolean createSubDir) {
        String baseDirStr = getBaseDir();
        File basedir = new File(baseDirStr);
        if (createBaseDir) {
            basedir.mkdir();
        }
        logEnabled = basedir.exists();
        if (logEnabled) {
            if (createSubDir) {
                logDir = baseDirStr + "/" + getCurrentTime();
                new File(logDir).mkdir();
                basicFilePath = logDir + "/" + getLogFileNamePrefix();
            } else {
                logDir = baseDirStr;
                basicFilePath = logDir + "/" + getLogFileNamePrefix() + "_" + getCurrentTime();
            }
        } else {
            basicFilePath = null;
            logDir = null;
        }
    }

    private String getCurrentTime() {
        Date currDate = new Date(System.currentTimeMillis());
        return dateFormat.format(currDate);
    }

    public boolean log(T t) throws IOException {
        if (logEnabled && isLogEnabled()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getLogFilePath()));
            writer.write(t.toString());
            writer.close();
            return true;
        }
        return false;
    }

    private String getLogFilePath() {
        return basicFilePath + "_" + i++ + "." + getLogFileExtension();
    }

    protected String getBaseDir() {
        return System.getProperty("user.home") + "/file_log";
    }

    protected String getLogFileNamePrefix() {
        return LOG_FILE_NAME_PREFIX;
    }

    protected String getLogFileExtension() {
        return "log";
    }

    protected boolean isLogEnabled() {
        return logEnabled;
    }

    protected final String getLogDir() {
        return logDir;
    }
}

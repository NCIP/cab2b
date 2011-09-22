package edu.wustl.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import junit.framework.TestCase;

public class FileLoggerTest extends TestCase {
    private static class TestLogger extends FileLogger<String> {
        private boolean disableLog = false;

        TestLogger(boolean createBaseDir, boolean createSubDir) {
            super(createBaseDir, createSubDir);
        }

        @Override
        protected String getBaseDir() {
            return "temp";
        }

        @Override
        protected boolean isLogEnabled() {
            if (disableLog) {
                return false;
            }
            return super.isLogEnabled();
        }

        void disableLogging() {
            disableLog = true;
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        deleteBaseDir();
    }

    private File getBaseDir() {
        return new File(getBaseDirStr());
    }

    private String getBaseDirStr() {
        return "temp";
    }

    public void testConstructor() throws Exception {
        deleteBaseDir();
        TestLogger logger = new TestLogger(false, false);
        assertFalse(logger.isLogEnabled());
        assertFalse(logger.log("foo"));
        assertFalse(getBaseDir().exists());

        getBaseDir().mkdir();
        logger = new TestLogger(false, false);
        assertTrue(logger.isLogEnabled());

        deleteBaseDir();
        logger = new TestLogger(true, false);
        assertTrue(getBaseDir().exists());
        assertTrue(logger.isLogEnabled());
        assertEquals(getBaseDirStr(), logger.getBaseDir());

        deleteBaseDir();
        logger = new TestLogger(false, true);
        assertFalse(logger.isLogEnabled());
        logger = new TestLogger(true, true);
        assertTrue(getBaseDir().exists());
        assertTrue(logger.isLogEnabled());
        assertFalse(getBaseDirStr().equals(logger.getLogDir()));
        assertTrue(logger.getLogDir().startsWith(getBaseDirStr()));
    }

    public void testLogging() throws Exception {
        deleteBaseDir();
        TestLogger logger = new TestLogger(true, false);

        assertTrue(logger.log("foo"));
        File file = new File(logger.getLogDir()).listFiles()[0];
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s = reader.readLine();
        assertEquals("foo", s);
        assertNull(reader.readLine());
        reader.close();
    }

    private void deleteBaseDir() {
        deleteDir(getBaseDir());
    }

    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
            }
            dir.delete();
        }
    }
}

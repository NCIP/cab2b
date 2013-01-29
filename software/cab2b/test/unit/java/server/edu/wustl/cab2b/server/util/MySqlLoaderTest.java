/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.server.path.PathConstants.FIELD_SEPARATOR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

import junit.framework.TestCase;
import edu.wustl.cab2b.server.path.PathConstants;

/**
 * @author chandrakant_talele
 */
public class MySqlLoaderTest extends TestCase {
    private static Connection con = TestConnectionUtil.getConnection();
    private static String tableName = "T_"+System.currentTimeMillis();
    private File file = null;

    @Override
    protected void setUp() throws Exception {
        String createTableSQL = "create table "+tableName+" (ID BIGINT(38) NOT NULL, NAME VARCHAR(10) NULL,PRIMARY KEY (ID))";
        SQLQueryUtil.executeUpdate(createTableSQL, con);
    }

    public void testMySqlLoader() {
        final int size = 2;
        assertTrue(true);
        String str = "SomeName";
        String home = System.getProperty("user.home");
        file = new File(home, "GeneratedFromMySqlLoaderTest.txt");
        String fileName = file.getAbsolutePath();
        file.delete();
        try {
            file.createNewFile();
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            for (int i = 1; i < size; i++) {
                fileWriter.write(Long.toString(i));
                fileWriter.write(FIELD_SEPARATOR);
                fileWriter.write(str + i);
                fileWriter.write("\n");
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Unable to write to file " + fileName, false);
        }
        String columns = "(ID,NAME)";
        
        new MySqlLoader().loadDataFromFile(con, fileName, columns, tableName, null,PathConstants.FIELD_SEPARATOR);

        String selectSQL = "SELECT ID,NAME FROM "+tableName;
        int recordCount = 0;
        String[][] rs = null;

        rs = SQLQueryUtil.executeQuery(selectSQL, con);

        for (int i = 0; i < rs.length; i++) {
            recordCount++;
            Long id = Long.parseLong(rs[i][0]);
            String name = (String) rs[i][1];
            assertTrue(id > 0 && id < size);
            assertTrue(name.startsWith(str));
        }
        assertEquals(size-1, recordCount);
        file.delete();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        if (file != null) {
            file.delete();
        }
        //SQLQueryUtil.executeUpdate("DROP table "+tableName, con);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        TestConnectionUtil.close(con);
    }
}

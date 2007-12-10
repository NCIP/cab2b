package edu.wustl.cab2b.server.util;

import java.sql.Connection;

import junit.framework.TestCase;
import edu.wustl.common.util.logger.Logger;

/**
 * Test class to test {@link SQLQueryUtil}
 * To Run this test case a started server even though it is JUnit
 * @author Chandrakant Talele
 */
public class SQLQueryUtilTest extends TestCase {
    private static Connection con = TestUtil.getConnection();

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        Logger.configure();
        String createTableSQL = "create table TEST_TABLE (ID BIGINT(38) NOT NULL, NAME VARCHAR(10) NULL,PRIMARY KEY (ID))";// insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
        SQLQueryUtil.executeUpdate(createTableSQL, con);
    }

    /**
     * This method tests functionality provided by {@link SQLQueryUtil}
     */
    public void testSQLQueryUtil() {
        String insertDataSQL = "insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
        int res = -1;

        res = SQLQueryUtil.executeUpdate(insertDataSQL, con);

        assertEquals(4, res);

        String selectSQL = "SELECT id,name FROM test_table WHERE name like ?";
        int recordCount = 0;
        String[][] rs = null;

        rs = SQLQueryUtil.executeQuery(selectSQL, con, "%A%");

        for (int i = 0; i < rs.length; i++) {

            recordCount++;
            Long id = Long.parseLong(rs[i][0]);
            String name = (String) rs[i][1];
            assertTrue(id != 4);
            assertTrue(name.indexOf((char) 'A') != -1);
        }

        assertTrue(recordCount == 3);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        SQLQueryUtil.executeUpdate("DROP table TEST_TABLE", con);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        TestUtil.close(con);
    }
}

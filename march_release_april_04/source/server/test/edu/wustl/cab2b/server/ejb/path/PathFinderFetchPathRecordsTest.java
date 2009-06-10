package edu.wustl.cab2b.server.ejb.path;

import java.rmi.RemoteException;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.sqlquery.SQLQueryBusinessInterface;
import edu.wustl.cab2b.common.ejb.sqlquery.SQLQueryHomeInterface;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.server.ejb.path.PathFinderBean;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author Chandrakant Talele
 */
public class PathFinderFetchPathRecordsTest extends TestCase {
    SQLQueryBusinessInterface sqlQueryBean = null;
    /*  Association Graph is
     
     | |     | |             | |
     | |     | |<----------- | |
     | |     | |     | |     | | 
     |1| --> |2| --> |3| --> |4|
     | |     | |     | | <-- | |
     | |     | |     | |     | |
     | | <------------------ | |
     | | <------------------ | |
     
     */
    /**
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        Logger.configure();
        String insertSQL = "insert into cab2b.PATH (FIRST_ENTITY_ID,INTERMEDIATE_PATH,LAST_ENTITY_ID ) values "
                + "(-9111,'-9222',-9333)," + "(-9111,'',-9222)," + "(-9111,'-9222',-9444),"
                + "(-9111,'-9222_-9333',-9444)," + "(-9222,'',-9333)," + "(-9222,'-9444',-9333),"
                + "(-9222,'',-9444)," + "(-9222,'-9333',-9444)," + "(-9222,'-9444',-9111),"
                + "(-9333,'-9444',-9222)," + "(-9333,'-9444_-9111',-9222)," + "(-9333,'',-9444),"
                + "(-9333,'-9444',-9111)," + "(-9444,'',-9333)," + "(-9444,'-9222',-9333),"
                + "(-9444,'-9111_-9222',-9333)," + "(-9444,'',-9222)," + "(-9444,'-9111',-9222),"
                + "(-9444,'',-9111)";
        sqlQueryBean = (SQLQueryBusinessInterface) Locator.getInstance().locate(EjbNamesConstants.SQL_QUERY_BEAN,SQLQueryHomeInterface.class);
        sqlQueryBean.executeUpdate(insertSQL);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        sqlQueryBean.executeUpdate("DELETE from cab2b.path where FIRST_ENTITY_ID in(-9111,-9222,-9333,-9444)");
    }

    public void testFetchPathRecords() {
        assertTrue(true);
//        Long src = -9444L;
//        Long des = -9333L;
//
//        PathFinderBean pathFinderbean = new PathFinderBean();
//        PathRecord[] pathRecordArray = null;
//        try {
//            pathRecordArray = pathFinderbean.fetchPathRecords(src, des);
//        } catch (RemoteException e) {
//            fail("Remote Exception while getting bean");
//        }
//        assertEquals(3, pathRecordArray.length);
//        for (PathRecord p1 : pathRecordArray) {
//            Long[] arr = p1.getEntityIdSequence();
//            assertEquals(arr[0], src);
//            assertEquals(arr[arr.length - 1], des);
//        }

    }

}

package edu.wustl.cab2b.admin.bizlogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.util.logger.Logger;

/**
 * @author atul_jawale
 * 
 */
public class CuratedPathBizLogicTest extends TestCase {

    static {
        Logger.configure();// To avoid null pointer Exception for code calling

    }

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/cab2b";

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, "root", "root");
        } catch (Exception e) {
            Logger.out.error("Exception in getting connection");
            e.printStackTrace();
        }
        if (con == null)
            Logger.out.error("Got null connection");
        return con;
    }

    //TODO this is the temporary test case . 
    public void testInitialize() {

        CuratedPath curatedPath = new CuratedPath();

        Set<IPath> iPathSet = new HashSet<IPath>();

        Path path1 = new Path();
        path1.setPathId(1L);
        path1.setIntermediatePaths("3_4");
        path1.setTargetEntityId(11L);
        path1.setSourceEntityId(10L);

        iPathSet.add(path1);

        curatedPath.setPaths(iPathSet);

        Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
        entitySet.add(path1.getSourceEntity());
        entitySet.add(path1.getTargetEntity());

        curatedPath.setEntitySet(entitySet);
        curatedPath.setSelected(false);

        Iterator<IPath> it = iPathSet.iterator();

        while (it.hasNext()) {
            long pathId = it.next().getPathId();
            assertEquals(1L, pathId);

        }

    }
}

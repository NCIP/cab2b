
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.bizlogic.PathBizLogic;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.util.logger.Logger;

/**
 * @author atul_jawale
 *
 */
public class CuratedPathBizLogicTest extends TestCase {

    static {
        Logger.configure();// To avoid null pointer Exception for code calling logger statements.
    }

    public static Connection getConnection() {
        //        String ip = "lcoal");
        //        String port = serverProperties.getProperty("database.server.port");
        //        String name = serverProperties.getProperty("database.name");
        //        String userName = serverProperties.getProperty("database.username");
        //        String password = serverProperties.getProperty("database.password");
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

    public void testSaveCuratedPath() {
        CuratedPath curatedPath = new CuratedPath();
        curatedPath.setSelected(false);

        Path path = new Path();
        path.setPathId(1L);
        path.setIntermediatePaths("1_75");
        path.setTargetEntityId(60L);
        path.setSourceEntityId(2L);
        curatedPath.addPath(path);

        Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
        entitySet.add(path.getSourceEntity());
        entitySet.add(path.getTargetEntity());
        curatedPath.setEntitySet(entitySet);

        try {
            new PathBizLogic().saveCuratedPath(curatedPath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    public void testGetCuratePathById() {
        ICuratedPath path = new PathBizLogic().getCuratePathById(334L);
        assertEquals(333L,path.getCuratedPathId());
    }

}

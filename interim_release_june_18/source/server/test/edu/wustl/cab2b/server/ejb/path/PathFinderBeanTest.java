package edu.wustl.cab2b.server.ejb.path;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.cactus.ServletTestCase;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.common.ejb.path.PathFinderHomeInterface;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.common.locator.LocatorException;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.logger.Logger;

public class PathFinderBeanTest extends ServletTestCase {
    PathFinderBusinessInterface pathFinder;

    /**
     * Override setUp() method from ServletTestCase. 
     */
    protected void setUp() {
        Logger.configure();
        try {
            pathFinder = (PathFinderBusinessInterface) Locator.getInstance().locate(EjbNamesConstants.PATH_FINDER_BEAN,PathFinderHomeInterface.class);
        } catch (LocatorException locExp) {
            fail("Cannot locate PathFinderBean");
        }
    }

    public void testGetAllPossiblePaths() {
        EntityInterface participant = getEntityByName("edu.wustl.catissuecore.domain.Participant");
        EntityInterface pmi = getEntityByName("edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier");
        List<IPath> paths = null;
        /*------------------------------*/
        //        try {
        //            String url = "jdbc:mysql://localhost:3306/cab2b";
        //            String driver = "com.mysql.jdbc.Driver";
        //            String userName = "root";
        //            String password = "";
        //            Class.forName(driver).newInstance();
        //            Connection con = DriverManager.getConnection(url, userName, password);
        //            paths = PathFinder.getInstance().getAllPossiblePaths(participant, pmi, con);
        //        } catch (Exception e) {
        //            fail("Remote Exception was not expected");
        //        }
        /*------------------------------*/

        try {
            paths = pathFinder.getAllPossiblePaths(participant, pmi);
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Remote Exception was not expected");
        }
        /*------------------------------*/
        assertEquals(2, paths.size());
        for (IPath path : paths) {
            assertEquals(participant, path.getSourceEntity());
            assertEquals(pmi, path.getTargetEntity());
        }
    }

    private EntityInterface getEntityByName(String name) {
        EntityManagerInterface em = EntityManager.getInstance();
        EntityInterface entity = null;
        try {
            entity = em.getEntityByName(name);
        } catch (DynamicExtensionsSystemException e) {
            e.printStackTrace();
            fail("getEntityByName() failed for Entity name : " + name);
        } catch (DynamicExtensionsApplicationException e) {
            e.printStackTrace();
            fail("getEntityByName() failed for Entity name : " + name);
        }
        if (entity == null) {
            fail("getEntityByName() failed for Entity name : " + name);
        }
        return entity;
    }
}
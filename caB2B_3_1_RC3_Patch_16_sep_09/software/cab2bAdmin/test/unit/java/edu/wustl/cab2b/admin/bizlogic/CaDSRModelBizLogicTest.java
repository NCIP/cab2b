package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.admin.beans.CaDSRModelDetailsBean;
import edu.wustl.cab2b.admin.beans.LoadModelResult;
import edu.wustl.cab2b.common.util.Utility;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

/**
 * @author hrishikesh_rajpathak
 *
 */
public class CaDSRModelBizLogicTest extends TestCase {

    public void testGetProjectsDisplayDetails() {
        List<CaDSRModelDetailsBean> projectDetails = null;
        CaDSRModelBizLogic modelBizLogic = new MockCaDSRModelBizLogic();
        try {
            projectDetails = modelBizLogic.getProjectsDisplayDetails();
        } catch (RemoteException e) {
            e.printStackTrace();
            assertFalse(true);
        }
        assertTrue(projectDetails.size() > 0);

        CaDSRModelDetailsBean proj = projectDetails.get(0);
        String name = proj.getLongName();
        assertNotNull(name);
        assertTrue(name.length() > 0);
        assertNotNull(proj.getId());
        // check sorting
        if (projectDetails.size() >= 3) {
            String name0 = projectDetails.get(0).getLongName();
            String name2 = projectDetails.get(2).getLongName();
            assertTrue(name0.compareTo(name2) < 0);
        }
    }

    public void testGetDomainModel() {
        DomainModel model = new CaDSRModelBizLogic().getDomainModel("GeneConnect", null, null);
        assertNotNull(model);
        assertTrue(model.getProjectLongName().equals("GeneConnect"));
    }
    public void testGetDomainModelNullModel() {
        LoadModelResult res = new LoadModelResult(null, false, null, null);
        DomainModel model = new CaDSRModelBizLogic().getDomainModel("FoobarChandrakant", null, res);
        assertNull(model);
    }
}

class MockCaDSRModelBizLogic extends CaDSRModelBizLogic {
    @Override
    Collection<EntityGroupInterface> getCab2bEntityGroups() {
        EntityGroupInterface entityGroup = new EntityGroup();
        entityGroup.setId(new Long(1));
        entityGroup.setLongName("TestEntityGroup1");
        entityGroup.setDescription("Test Description1");
        entityGroup.setVersion("Test Ver1");
        entityGroup.setName(Utility.createModelName(entityGroup.getLongName(), entityGroup.getVersion()));

        EntityGroupInterface entityGroupTwo = new EntityGroup();
        entityGroupTwo.setId(new Long(2));
        entityGroupTwo.setLongName("TestEntityGroup2");
        entityGroupTwo.setDescription("Test Description2");
        entityGroupTwo.setVersion("Test Ver2");
        entityGroupTwo.setName(Utility.createModelName(entityGroupTwo.getLongName(), entityGroupTwo.getVersion()));

        EntityGroupInterface entityGroupThree = new EntityGroup();
        entityGroupThree.setId(new Long(3));
        entityGroupThree.setLongName("TestEntityGroup3");
        entityGroupThree.setDescription("Test Description3");
        entityGroupThree.setVersion("Test Ver3");
        entityGroupThree.setName(Utility.createModelName(entityGroupThree.getLongName(),
                                                         entityGroupThree.getVersion()));

        Collection<EntityGroupInterface> egCollection = new HashSet<EntityGroupInterface>();
        egCollection.add(entityGroup);
        egCollection.add(entityGroupTwo);
        egCollection.add(entityGroupThree);
        return egCollection;
    }
}

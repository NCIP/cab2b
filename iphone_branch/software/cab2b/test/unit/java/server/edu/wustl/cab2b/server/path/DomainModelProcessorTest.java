/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.util.IdGenerator;
import edu.wustl.cab2b.server.util.InheritanceUtil;
import edu.wustl.cab2b.server.util.TestUtil;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

public class DomainModelProcessorTest extends TestCase {
    static DomainObjectFactory deFactory = DomainObjectFactory.getInstance();

    static IdGenerator idGenerator = new IdGenerator(0L);

    public void testMarkInheritedAttributes() {
        DomainModelProcessor p = new DomainModelProcessor();
        EntityInterface e1 = TestUtil.getEntity("GenomicIdentifier", "id");
        EntityInterface e2 = TestUtil.getEntity("mRNA", "id");
        EntityInterface e3 = TestUtil.getEntity("GeneBankmRNA", "id");
        e2.setParentEntity(e1);
        e3.setParentEntity(e2);
        EntityGroupInterface eg = deFactory.createEntityGroup();
        eg.addEntity(e3);
        p.markInheritedAttributes(eg);

        for (EntityInterface e : eg.getEntityCollection()) {
            if (e.getName().equals("GenomicIdentifier")) {
                AttributeInterface a = e.getAttributeCollection().iterator().next();
                assertFalse(InheritanceUtil.isInherited(a));
            } else if (e.getName().equals("mRNA")) {
                AttributeInterface a = e.getAttributeCollection().iterator().next();
                assertTrue(InheritanceUtil.isInherited(a));
            } else if (e.getName().equals("GeneBankmRNA")) {
                AttributeInterface a = e.getAttributeCollection().iterator().next();
                assertTrue(InheritanceUtil.isInherited(a));
            }
        }
    }

    public void testLoadParentVsChildrenMapNoInheritance() {
        DomainObjectFactory deFactory = DomainObjectFactory.getInstance();
        DomainModelProcessor p = new DomainModelProcessor();
        Map<EntityInterface, Integer> entityVsIndex = new HashMap<EntityInterface, Integer>();
        entityVsIndex.put(deFactory.createEntity(), 0);
        entityVsIndex.put(deFactory.createEntity(), 1);
        entityVsIndex.put(deFactory.createEntity(), 2);

        p.setEntityVsIndex(entityVsIndex);
        Map<Integer, Set<Integer>> map = p.getReplicationNodes();
        assertEquals(0, map.size());
    }

    public void testLoadParentVsChildrenMapWithInheritance() {

        DomainModelProcessor p = new DomainModelProcessor();
        Map<EntityInterface, Integer> entityVsIndex = new HashMap<EntityInterface, Integer>();

        EntityInterface parent = getEntity();
        EntityInterface child1 = getEntity();

        child1.setParentEntity(parent);

        EntityInterface child2 = getEntity();
        child2.setParentEntity(parent);

        EntityInterface child11 = getEntity();
        child11.setParentEntity(child1);

        entityVsIndex.put(parent, 0);
        entityVsIndex.put(child1, 1);
        entityVsIndex.put(child2, 2);
        entityVsIndex.put(child11, 11);

        p.setEntityVsIndex(entityVsIndex);
        Map<Integer, Set<Integer>> map = p.getReplicationNodes();
        assertEquals(2, map.size());
        assertEquals(2, map.get(0).size());

        HashSet<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);

        assertEquals(set, map.get(0));
        assertEquals(1, map.get(1).size());
        assertEquals(new Integer(11), map.get(1).iterator().next());
    }

    public void testWithInheritance() {
        String appName = "TestApplication";
        String name = "projectLongName_v1";
        String longName = "projectLongName";
        DomainModel model = DomainModelParserTest.getModel();
        model.setProjectLongName(longName);
        model.setProjectVersion("1");
        DomainModelParser p = new DomainModelParser(model);
        DomainModelProcessor processor = null;
        try {
            processor = new MockDomainModelProcessor(p, appName);
        } catch (DynamicExtensionsSystemException e) {
            e.printStackTrace();
        } catch (DynamicExtensionsApplicationException e) {
            e.printStackTrace();
        }
        EntityGroupInterface eg = processor.getEntityGroup();
        assertEquals(appName, eg.getShortName());
        assertEquals(name, eg.getName());
        assertEquals(longName, eg.getLongName());
        assertEquals(5, eg.getEntityCollection().size());
    }

    EntityInterface getEntity() {
        EntityInterface entity = deFactory.createEntity();
        entity.setId(idGenerator.getNextId());
        return entity;
    }

    public void testGetAdjacencyMatrix() {
        String appName = "TestApplication";
        String longName = "projectLongName";
        DomainModel model = DomainModelParserTest.getModel();
        model.setProjectLongName(longName);
        DomainModelParser p = new DomainModelParser(model);
        DomainModelProcessor processor = null;
        try {
            processor = new MockDomainModelProcessor(p, appName);
        } catch (DynamicExtensionsSystemException e) {
            e.printStackTrace();
        } catch (DynamicExtensionsApplicationException e) {
            e.printStackTrace();
        }
        boolean[][] matrix = processor.getAdjacencyMatrix();

        List<Long> list = processor.getEntityIds();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                long src = list.get(i);
                long des = list.get(j);

                if ((src == 5L && des == 11L) || (src == 11L && des == 5L)) {
                    assertTrue(matrix[i][j]);
                } else {
                    assertFalse(matrix[i][j]);
                }

            }
        }
    }
}

class MockDomainModelProcessor extends DomainModelProcessor {
    public MockDomainModelProcessor(DomainModelParser parser, String applicationName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
        super(parser, applicationName);
    }

    EntityGroupInterface saveEntityGroup(EntityGroupInterface eg) {
        long id = 1;
        for (EntityInterface e : eg.getEntityCollection()) {
            e.setId(id++);
            for (AbstractAttributeInterface a : e.getAbstractAttributeCollection()) {
                a.setId(id++);
            }
        }
        return eg;
    }
}

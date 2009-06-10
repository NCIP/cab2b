package edu.wustl.cab2b.server.path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.util.IdGenerator;
import edu.wustl.cab2b.server.util.InheritanceUtil;
import edu.wustl.cab2b.server.util.TestUtil;
import edu.wustl.common.util.logger.Logger;

public class DomainModelProcessorTest extends TestCase {
    static DomainObjectFactory deFactory = DomainObjectFactory.getInstance();

    static IdGenerator idGenerator = new IdGenerator(0L);

    @Override
    protected void setUp() throws Exception {
        Logger.configure();
    }

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

    EntityInterface getEntity() {
        EntityInterface entity = deFactory.createEntity();
        entity.setId(idGenerator.getNextId());
        return entity;
    }
}

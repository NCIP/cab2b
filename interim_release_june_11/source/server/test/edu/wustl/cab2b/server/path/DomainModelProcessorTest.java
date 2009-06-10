package edu.wustl.cab2b.server.path;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.util.InheritanceUtil;
import edu.wustl.cab2b.server.util.TestUtil;
import edu.wustl.common.util.logger.Logger;

public class DomainModelProcessorTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        Logger.configure();
    }

    public void testMarkInheritedAttributes() {
        DomainObjectFactory deFactory = DomainObjectFactory.getInstance();
        DomainModelProcessor p = new DomainModelProcessor();
        EntityInterface e1 = TestUtil.getEntity("GenomicIdentifier", "id");
        EntityInterface e2 = TestUtil.getEntity("mRNA", "id");
        EntityInterface e3 = TestUtil.getEntity("GeneBankmRNA", "id");
        e2.setParentEntity(e1);
        e3.setParentEntity(e2);
        EntityGroupInterface eg = deFactory.createEntityGroup();
        eg.addEntity(e3);
        p.markInheritedAttributes(eg);
        
        for(EntityInterface e : eg.getEntityCollection()) {
            if(e.getName().equals("GenomicIdentifier")) {
                AttributeInterface a = e.getAttributeCollection().iterator().next();
                assertFalse(InheritanceUtil.isInherited(a));
            } else if(e.getName().equals("mRNA")) {
                AttributeInterface a = e.getAttributeCollection().iterator().next();
                assertTrue(InheritanceUtil.isInherited(a));
            } else if(e.getName().equals("GeneBankmRNA")) {
                AttributeInterface a = e.getAttributeCollection().iterator().next();
                assertTrue(InheritanceUtil.isInherited(a));
            }
        }
    }
}

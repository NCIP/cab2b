package edu.wustl.cab2b.server.util;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

public class InheritanceUtilTest extends TestCase {
    public void testGetActualAttributeForInheritance() {
        EntityInterface e1 = TestUtil.getEntity("GenomicIdentifier", "id", null);
        EntityInterface e2 = TestUtil.getEntity("mRNA", "id", e1);
        EntityInterface e3 = TestUtil.getEntity("GeneBankmRNA", "id", e2);

        AttributeInterface attr = InheritanceUtil.getActualAttribute(e3.getAttributeCollection().iterator().next());
        assertEquals("GenomicIdentifier", attr.getEntity().getName());
        assertEquals("id", attr.getName());
    }

    public void testGetActualAttribute() {
        EntityInterface e3 = TestUtil.getEntity("GeneBankmRNA", "id", null);

        AttributeInterface attr = InheritanceUtil.getActualAttribute(e3.getAttributeCollection().iterator().next());
        assertEquals("GeneBankmRNA", attr.getEntity().getName());
        assertEquals("id", attr.getName());
    }

    public void testGetActualAssociation() {
        AssociationInterface association = DomainObjectFactory.getInstance().createAssociation();
        association.setId(1L);
        AssociationInterface res = InheritanceUtil.getActualAassociation(association);
        assertEquals(association, res);
    }

    public void testGetActualAssociationForInheritance() {
        AssociationInterface association = DomainObjectFactory.getInstance().createAssociation();
        association.setId(1L);
        TestUtil.markInherited(association);
        try {
            InheritanceUtil.getActualAassociation(association);
            fail("expecting exception, but not thrown");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }
}

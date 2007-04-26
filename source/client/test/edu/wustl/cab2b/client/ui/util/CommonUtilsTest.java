package edu.wustl.cab2b.client.ui.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author Chandrakant Talele
 */
public class CommonUtilsTest extends TestCase {
    public void testGetIdAttributeIndexFromAttributes() {
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
        int index = CommonUtils.getIdAttributeIndexFromAttributes(attributes);
        assertEquals(-1, index);
    }

    public void testGetIdAttributeIndexFromAttributesWithId() {
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
        attributes.add(getAttr("name"));
        attributes.add(getAttr("id"));
        int index = CommonUtils.getIdAttributeIndexFromAttributes(attributes);
        assertEquals(1, index);
    }

    public void testGetIdAttributeIndexFromAttributesWithoutId() {
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
        attributes.add(getAttr("name"));
        int index = CommonUtils.getIdAttributeIndexFromAttributes(attributes);
        assertEquals(-1, index);
    }

    private AttributeInterface getAttr(String name) {
        AttributeInterface attr = DomainObjectFactory.getInstance().createStringAttribute();
        attr.setName(name);
        return attr;
    }
}

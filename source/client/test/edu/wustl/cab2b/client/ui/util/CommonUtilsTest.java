package edu.wustl.cab2b.client.ui.util;

import java.util.ArrayList;
import java.util.BitSet;
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

    public void testGetCountofOnBits() {
        BitSet bitSet = new BitSet();
        bitSet.set(0);
        bitSet.set(1);
        bitSet.set(5);
        int res = CommonUtils.getCountofOnBits(bitSet);
        assertEquals(3, res);
    }

    public void testGetCountofOnBitsAllFalse() {
        BitSet bitSet = new BitSet();
        int res = CommonUtils.getCountofOnBits(bitSet);
        assertEquals(0, res);
    }

    public void testCapitalizeString() {
        assertEquals("Gene", CommonUtils.capitalizeFirstCharacter("gene"));
    }

    public void testCapitalizeStringAlreadyCapital() {
        assertEquals("Gene", CommonUtils.capitalizeFirstCharacter("Gene"));
    }

    public void testSplitStringWithTextQualifier() {
        ArrayList<String> res = CommonUtils.splitStringWithTextQualifier("\"prat,ibha\", \"fdf\"vishaldhok\"",
                                                                        '"', ',');
        System.out.println(res.get(1)); 
        assertEquals(3, res.size());
        assertEquals("prat,ibha", res.get(0));
        assertEquals(" fdf", res.get(1));
        assertEquals("\"vishaldhok", res.get(2));
    }

    private AttributeInterface getAttr(String name) {
        AttributeInterface attr = DomainObjectFactory.getInstance().createStringAttribute();
        attr.setName(name);
        return attr;
    }
}

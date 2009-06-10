package edu.wustl.cab2b.client.ui.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.server.util.TestUtil;

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
        attributes.add(TestUtil.getAttribute("name"));
        attributes.add(TestUtil.getAttribute("id"));
        int index = CommonUtils.getIdAttributeIndexFromAttributes(attributes);
        assertEquals(1, index);
    }

    public void testGetIdAttributeIndexFromAttributesWithoutId() {
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
        attributes.add(TestUtil.getAttribute("name"));
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
        assertEquals(3, res.size());
        assertEquals("prat,ibha", res.get(0));
        assertEquals(" fdf", res.get(1));
        assertEquals("\"vishaldhok", res.get(2));
    }
    public void testCountCharacterIn() {
        int count = CommonUtils.countCharacterIn("abcdefgha" + "n", 'a');
        assertEquals(count, 2);
    }

    public void testCountCharacterInNotPresent() {
        int count = CommonUtils.countCharacterIn("abcdefghan", 'z');
        assertEquals(count, 0);
    }
    public void testRemoveContinuousSpaceCharsAndTrimNoChange() {
        String str = "someString";
        String res = CommonUtils.removeContinuousSpaceCharsAndTrim(str);
        assertEquals(str, res);
    }

    public void testRemoveContinuousSpaceCharsAndTrim() {
        String str = "  gene       \t\t     anno       ";
        String res = CommonUtils.removeContinuousSpaceCharsAndTrim(str);
        assertEquals("gene anno", res);
    }

    public void testSearchNodeForNull() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        Integer userObject = 3;
        DefaultMutableTreeNode res = CommonUtils.searchNode(rootNode, userObject);
        assertNull(res);
    }

    public void testSearchNode() {
        Integer userObject = new Integer(2);
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode();
        node1.setUserObject(userObject);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        rootNode.add(node1);
        DefaultMutableTreeNode res = CommonUtils.searchNode(rootNode, userObject);
        assertEquals(node1, res);
        assertEquals(userObject, res.getUserObject());
    }
}

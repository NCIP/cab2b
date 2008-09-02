package edu.wustl.cab2b.client.ui.util;

import static edu.wustl.cab2b.common.util.Constants.TYPE_CATEGORY;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
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


    public void testSplitStringWithTextQualifier() {
        ArrayList<String> res = CommonUtils.splitStringWithTextQualifier("\"chetan, patil\", is,  subordinate,of,\"rahul ner\"",
                                                                         '"', ',');
        assertEquals(5, res.size());
        assertEquals("chetan, patil", res.get(0));
        assertEquals("subordinate", res.get(2));        
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
    
    public void testInitializeResources() {
        try {
            CommonUtils.initializeResources();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to initialize resources");
        }
        String msg = ErrorCodeHandler.getErrorMessage(ErrorCodeConstants.DB_0001);
        assertTrue(msg.length() != 0);
    }
    
    public void testSetHome() {
        CommonUtils.setHome();
        File cab2bHome = new File(System.getProperty("user.home"), "cab2b");
        assertEquals(cab2bHome.getAbsolutePath(),System.getProperty("cab2b.home"));
    }
    
    public void testEscapeString() {
        assertEquals("\"aaaaa,bbbbb,ccccc\"",CommonUtils.escapeString("aaaaa,bbbbb,ccccc"));
    }
    
    public void testGetMaximumLabelDimension() {
        AttributeInterface a1 = TestUtil.getAttribute("entity",1L,"id",2L);
        AttributeInterface a2 = TestUtil.getAttribute("entity",3L,"geneSourceName",4L);
        AttributeInterface a3 = TestUtil.getAttribute("entity",5L,"name",6L);
        
        DynamicExtensionUtility.addTaggedValue(a2.getEntity(), TYPE_CATEGORY, TYPE_CATEGORY); 
        
        List<AttributeInterface> list = new ArrayList<AttributeInterface>(2);
        list.add(a1);
        list.add(a2);
        list.add(a3);
        
        Dimension d = CommonUtils.getMaximumLabelDimension(list);
        assertEquals(new Dimension(110,15),d);
    }
}

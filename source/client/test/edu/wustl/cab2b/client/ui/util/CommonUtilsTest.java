package edu.wustl.cab2b.client.ui.util;

import static edu.wustl.cab2b.common.util.Constants.TYPE_CATEGORY;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.tree.DefaultMutableTreeNode;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.exception.RuntimeException;
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
        ArrayList<String> res = CommonUtils.splitStringWithTextQualifier(
                                                                         "\"chetan, patil\", is,  subordinate,of,\"rahul ner\"",
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
        assertEquals(cab2bHome.getAbsolutePath(), System.getProperty("cab2b.home"));
    }

    public void testEscapeString() {
        assertEquals("\"aaaaa,bbbbb,ccccc\"", CommonUtils.escapeString("aaaaa,bbbbb,ccccc"));
    }

    public void testGetMaximumLabelDimension() {
        AttributeInterface a1 = TestUtil.getAttribute("entity", 1L, "id", 2L);
        AttributeInterface a2 = TestUtil.getAttribute("entity", 3L, "geneSourceName", 4L);
        AttributeInterface a3 = TestUtil.getAttribute("entity", 5L, "name", 6L);

        DynamicExtensionUtility.addTaggedValue(a2.getEntity(), TYPE_CATEGORY, TYPE_CATEGORY);

        List<AttributeInterface> list = new ArrayList<AttributeInterface>(2);
        list.add(a1);
        list.add(a2);
        list.add(a3);

        Dimension d = CommonUtils.getMaximumLabelDimension(list);
        assertEquals(new Dimension(110, 15), d);
    }

    public void testGetRelativeDimension() {
        Dimension referenceDimnesion = new Dimension(200, 300);
        float percentageWidth = 0.5f;
        float percentageHeight = 0.5f;
        Dimension result = CommonUtils.getRelativeDimension(referenceDimnesion, percentageWidth, percentageHeight);
        assertEquals(150d, result.getHeight());
        assertEquals(100d, result.getWidth());
    }

    public void testGetDagImageMap() {
        Map<DagImages, Image> imageMap = CommonUtils.getDagImageMap();

        DagImages[] keys = { DagImages.SelectIcon, DagImages.selectMOIcon, DagImages.ArrowSelectIcon, DagImages.ArrowSelectMOIcon, DagImages.ParenthesisIcon, DagImages.ParenthesisMOIcon, DagImages.DocumentPaperIcon, DagImages.PortImageIcon };
        assertEquals(keys.length, imageMap.size());
        for (DagImages key : keys) {
            assertTrue(imageMap.containsKey(key));
        }
    }

    public void testDisableAllComponent() {
        Container[] components = { new JPanel(), new JPanel(), new JButton(), new JButton() };
        components[1].setEnabled(true);
        components[2].setEnabled(true);

        JFrame container = new JFrame();
        for (Container component : components) {
            container.add(component);
        }
        CommonUtils.disableAllComponent(container);
        check(container);

    }

    private void check(Container container) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            assertFalse(container.getComponent(i).isEnabled());
            if (container.getComponent(i) instanceof Container)
                check((Container) container.getComponent(i));
        }
    }

    public void testHandleExceptionForChecked() {
        CommonUtils.initializeResources();
        Exception exception = new CheckedException("From testcase", new IllegalArgumentException(),
                ErrorCodeConstants.DB_0001);
        Component parentComponent = null;
        boolean shouldShowErrorDialog = false;
        boolean shouldLogException = false;
        boolean shouldPrintExceptionInConsole = false;
        boolean shouldKillApp = false;

        CommonUtils.handleException(exception, parentComponent, shouldShowErrorDialog, shouldLogException,
                                    shouldPrintExceptionInConsole, shouldKillApp);
    }

    public void testHandleExceptionForWrappedChecked() {
        CommonUtils.initializeResources();
        Exception exception = new RuntimeException(new CheckedException("From testcase",
                new IllegalArgumentException(), ErrorCodeConstants.DB_0001));
        Component parentComponent = null;
        boolean shouldShowErrorDialog = false;
        boolean shouldLogException = true;
        boolean shouldPrintExceptionInConsole = false;
        boolean shouldKillApp = false;

        CommonUtils.handleException(exception, parentComponent, shouldShowErrorDialog, shouldLogException,
                                    shouldPrintExceptionInConsole, shouldKillApp);
    }

    public void testHandleExceptionForCab2bRuntime() {
        CommonUtils.initializeResources();
        Exception exception = new edu.wustl.cab2b.common.exception.RuntimeException("From Testcase",
                ErrorCodeConstants.DB_0001);
        Component parentComponent = null;
        boolean shouldShowErrorDialog = false;
        boolean shouldLogException = false;
        boolean shouldPrintExceptionInConsole = true;
        boolean shouldKillApp = false;

        CommonUtils.handleException(exception, parentComponent, shouldShowErrorDialog, shouldLogException,
                                    shouldPrintExceptionInConsole, shouldKillApp);
    }

    public void testHandleExceptionForNullPointer() {
        CommonUtils.initializeResources();
        Exception exception = new NullPointerException();
        Component parentComponent = null;
        boolean shouldShowErrorDialog = false;
        boolean shouldLogException = false;
        boolean shouldPrintExceptionInConsole = true;
        boolean shouldKillApp = false;

        CommonUtils.handleException(exception, parentComponent, shouldShowErrorDialog, shouldLogException,
                                    shouldPrintExceptionInConsole, shouldKillApp);
    }

    public void testIsNameValidFalse() {
        assertFalse(CommonUtils.isNameValid(""));
    }

    public void testIsNameValidTrue() {
        assertTrue(CommonUtils.isNameValid("SomeName"));
    }

    public void testGetComponentByName() {

        String name = "CompName";
        JButton b = new JButton();
        b.setName(name);

        JRootPane frame = new JRootPane();
        frame.add(new JButton());
        frame.add(b);

        Component comp = CommonUtils.getComponentByName(frame, name);
        assertEquals(b, comp);
    }

    public void testGetErrorMessage() {
        final String errorMessage = "An unknown internal error ocurred at CDS while delegating the credentials";

        RuntimeException e = new RuntimeException(errorMessage, ErrorCodeConstants.CDS_006);
        String message = CommonUtils.getErrorMessage(e);

        assertEquals(errorMessage, message);
    }

}

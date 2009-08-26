package edu.wustl.cab2b.server.path;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.server.util.TestUtil;

/**
 * @author chandrakant_talele
 */
public class InterModelConnectionTest extends TestCase {

    private final Long leftAttrId = 1L;

    private final Long leftEnId = 2L;

    private final Long rightAttrId = 3L;

    private final Long rightEnId = 4L;

    private final AttributeInterface leftAttr = TestUtil.getAttribute("C1", leftEnId, "A1", leftAttrId);

    private final AttributeInterface rightAttr = TestUtil.getAttribute("C2", rightEnId, "A2", rightAttrId);

    private final InterModelConnection con = new InterModelConnection(leftAttr, rightAttr);

    public void testConstructor() {
        assertEquals(leftAttrId, con.getLeftAttributeId());
        assertEquals(leftEnId, con.getLeftEntityId());
        assertEquals(rightAttrId, con.getRightAttributeId());
        assertEquals(rightEnId, con.getRightEntityId());
    }

    public void testMirror() {
        InterModelConnection mirror = con.mirror();
        assertEquals(mirror.getRightAttributeId(), con.getLeftAttributeId());
        assertEquals(mirror.getRightEntityId(), con.getLeftEntityId());
        assertEquals(mirror.getLeftAttributeId(), con.getRightAttributeId());
        assertEquals(mirror.getLeftEntityId(), con.getRightEntityId());

    }

    public void testEqualsHashCode() {
        InterModelConnection con2 = new InterModelConnection(leftAttr, rightAttr);
        InterModelConnection mirror = con2.mirror();

        assertEquals(con, con2);

        Map<InterModelConnection, String> m = new HashMap<InterModelConnection, String>(3);
        m.put(con, "con");
        m.put(con2, "con2");
        m.put(mirror, "mirror");

        assertEquals(2, m.size());
    }

    public void testToString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Left Entity : " + leftEnId);
        buff.append("\tLeft Attribute : " + leftAttrId);
        buff.append("\tRight Entity : " + rightEnId);
        buff.append("\tRight Attribute : " + rightAttrId);
        assertEquals(buff.toString(), con.toString());
    }
}
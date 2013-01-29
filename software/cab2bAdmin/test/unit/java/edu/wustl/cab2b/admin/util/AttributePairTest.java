/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.util;

import static edu.wustl.cab2b.common.util.Constants.TYPE_CATEGORY;
import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;

/**
 * @author chandrakant_talele
 *
 */
public class AttributePairTest extends TestCase {
    /**
     * Test Equals
     */
    public void testEquals() {
        AttributePair pair1 = getPair();
        AttributePair pair2 = new AttributePair(pair1.getAttribute2(), pair1.getAttribute1());
        assertTrue(pair1.equals(pair2));
    }

    public void testMatchFactor() {
        AttributePair pair = getPair();
        pair.setMatchFactor(MatchFactor.ATTRIBUTE_CONCEPT_CODE);
        assertEquals(MatchFactor.ATTRIBUTE_CONCEPT_CODE, pair.getMatchFactor());
    }

    public void testToString() {
        AttributePair pair = getPair();
        pair.setMatchFactor(MatchFactor.ATTRIBUTE_CONCEPT_CODE);

        String expected = "Attribute 1 of Class1 and Attribute 2 of Class2 have same attribute concept code";
        assertEquals(expected, pair.toString());
    }

    public void testToStringManualConnect() {
        AttributePair pair = getPair();
        pair.setMatchFactor(MatchFactor.MANUAL_CONNECT);

        String expected = "Attribute 1 of Class1 and Attribute 2 of Class2 are manually connected";
        assertEquals(expected, pair.toString());
    }

    public void testGetDisplayValue() {
        AttributePair pair = getPair();
        pair.setMatchFactor(MatchFactor.ATTRIBUTE_CONCEPT_CODE);

        String expected = "<i>ATTRIBUTE 1</i> of <i>Class1</i> and <i>ATTRIBUTE 2</i> of <i>Class2</i> have same attribute concept code";
        assertEquals(expected, pair.getDisplayValue());
    }

    public void testGetDisplayValueManualConnect() {
        AttributePair pair = getPair();
        pair.setMatchFactor(MatchFactor.MANUAL_CONNECT);

        String expected = "<i>ATTRIBUTE 1</i> of <i>Class1</i> and <i>ATTRIBUTE 2</i> of <i>Class2</i> are manually connected";
        assertEquals(expected, pair.getDisplayValue());
    }

    private AttributePair getPair() {
        DomainObjectFactory f = DomainObjectFactory.getInstance();
        EntityInterface e1 = f.createEntity();
        DynamicExtensionUtility.addTaggedValue(e1, TYPE_CATEGORY, TYPE_CATEGORY);
        e1.setName("Class1");

        AttributeInterface a1 = f.createIntegerAttribute();
        a1.setId(7L);
        a1.setName("Attribute 1");
        a1.setEntity(e1);
        e1.addAbstractAttribute(a1);

        EntityInterface e2 = f.createEntity();
        DynamicExtensionUtility.addTaggedValue(e2, TYPE_CATEGORY, TYPE_CATEGORY);
        e2.setName("Class2");

        AttributeInterface a2 = f.createIntegerAttribute();
        a2.setId(4L);
        a2.setName("Attribute 2");
        a2.setEntity(e2);
        e2.addAbstractAttribute(a2);

        AttributePair pair = new AttributePair(a1, a2);
        return pair;
    }
}

package edu.wustl.cab2b.server.path;

import java.util.Collection;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.ObjectAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import gov.nih.nci.cagrid.metadata.common.Enumeration;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.ValueDomain;
import gov.nih.nci.cagrid.metadata.common.ValueDomainEnumerationCollection;

/**
 * Test class for {@link edu.wustl.cab2b.server.path.DomainModelParser}
 * @author Chandrakant Talele
 */
public class DataTypeTest extends TestCase {
    private static String pkg = "edu.common.dynamicextensions.domain.";
    public void testForAttributeType() {
        assertEquals(DataType.INTEGER, DataType.get("number"));
        assertEquals(DataType.INTEGER, DataType.get("java.lang.integer"));
        assertEquals(DataType.INTEGER, DataType.get("int"));
        assertEquals(DataType.STRING, DataType.get("java.lang.string"));
        assertEquals(DataType.STRING, DataType.get("string"));
        assertEquals(DataType.STRING, DataType.get("alphanumeric"));
        assertEquals(DataType.STRING, DataType.get("character"));
        assertEquals(DataType.DATE, DataType.get("java.util.date"));
        assertEquals(DataType.DATE, DataType.get("date"));
        assertEquals(DataType.FLOAT, DataType.get("java.lang.float"));
        assertEquals(DataType.FLOAT, DataType.get("float"));
        assertEquals(DataType.BOOLEAN, DataType.get("java.lang.boolean"));
        assertEquals(DataType.BOOLEAN, DataType.get("boolean"));
        assertEquals(DataType.LONG, DataType.get("java.lang.long"));
        assertEquals(DataType.LONG, DataType.get("long"));
        assertEquals(DataType.DOUBLE, DataType.get("java.lang.double"));
        assertEquals(DataType.DOUBLE, DataType.get("double"));
        assertEquals(DataType.OBJECT, DataType.get("3d_array"));
        assertEquals(DataType.SHORT, DataType.get("short"));
        assertEquals(DataType.SHORT, DataType.get("java.lang.short"));
    }

    public void testForIntegerAttribute() {
        Integer integer = 1;
        PermissibleValueInterface pv = getAttribute("int", "IntegerAttributeTypeInformation", integer);
        assertTrue(pv instanceof IntegerValueInterface);
        assertEquals(integer.intValue(), ((IntegerValueInterface) pv).getValue().intValue());
    }
    public void testForShortAttribute() {
        Short num = 1;
        PermissibleValueInterface pv = getAttribute("short", "ShortAttributeTypeInformation", num);
        assertTrue(pv instanceof ShortValueInterface);
        assertEquals(num.shortValue(), ((ShortValueInterface) pv).getValue().intValue());
    }
    public void testForStringAttribute() {
        String str = "val";
        PermissibleValueInterface pv = getAttribute("string", "StringAttributeTypeInformation", str);
        assertTrue(pv instanceof StringValueInterface);
        assertEquals(str, ((StringValueInterface) pv).getValue());
    }

    public void testForDateAttribute() {
        PermissibleValueInterface pv = getAttribute("date", "DateAttributeTypeInformation", "");
        assertTrue(pv instanceof DateValueInterface);
    }

    public void testForFloatAttribute() {
        Float f = 2.0f;
        PermissibleValueInterface pv = getAttribute("float", "FloatAttributeTypeInformation", f);
        assertTrue(pv instanceof FloatValueInterface);
        assertEquals(f, ((FloatValueInterface) pv).getValue());
    }

    public void testForBooleanAttribute() {
        Boolean b = true;
        PermissibleValueInterface pv = getAttribute("boolean", "BooleanAttributeTypeInformation", b);
        assertTrue(pv instanceof BooleanValueInterface);
        assertEquals(b, ((BooleanValueInterface) pv).getValue());
    }

    public void testForLongAttribute() {
        Long l = 12312L;
        PermissibleValueInterface pv = getAttribute("long", "LongAttributeTypeInformation", l);
        assertTrue(pv instanceof LongValueInterface);
        assertEquals(l, ((LongValueInterface) pv).getValue());
    }

    public void testForDoubleAttribute() {
        Double d = 233.8d;
        PermissibleValueInterface pv = getAttribute("double", "DoubleAttributeTypeInformation", d);
        assertTrue(pv instanceof DoubleValueInterface);
        assertEquals(d, ((DoubleValueInterface) pv).getValue());
    }

    public void testForObjectAttribute() {
        UMLAttribute umlAttribute = getUmlAttribute("");
        AttributeInterface attribute = DataType.get("3D").createAttribute(umlAttribute);
        assertTrue(attribute.getAttributeTypeInformation() instanceof ObjectAttributeTypeInformation);
    }

    private PermissibleValueInterface getAttribute(String type, String expectedType, Object pv) {
        UMLAttribute umlAttr = getUmlAttribute(pv.toString());
        AttributeInterface attr = DataType.get(type).createAttribute(umlAttr);

        assertEquals(pkg + expectedType, attr.getAttributeTypeInformation().getClass().getName());

        DataElementInterface de = attr.getAttributeTypeInformation().getDataElement();
        assertTrue(de instanceof UserDefinedDEInterface);
        UserDefinedDEInterface ude = (UserDefinedDEInterface) de;
        Collection<PermissibleValueInterface> pvCollection = ude.getPermissibleValueCollection();
        assertEquals(1, pvCollection.size());
        return pvCollection.iterator().next();
    }

    private UMLAttribute getUmlAttribute(String permissibleVal) {
        Enumeration[] arr = new Enumeration[1];
        arr[0] = new Enumeration();
        arr[0].setPermissibleValue(permissibleVal);

        ValueDomainEnumerationCollection collection = new ValueDomainEnumerationCollection();
        collection.setEnumeration(arr);

        ValueDomain valueDomain = new ValueDomain();
        valueDomain.setEnumerationCollection(collection);

        UMLAttribute attr = new UMLAttribute();
        attr.setValueDomain(valueDomain);

        return attr;
    }
}

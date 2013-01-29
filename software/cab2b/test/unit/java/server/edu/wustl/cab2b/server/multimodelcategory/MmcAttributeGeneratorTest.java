/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.multimodelcategory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.wustl.cab2b.common.multimodelcategory.bean.MultiModelAttributeBean;

public class MmcAttributeGeneratorTest extends TestCase {
    DomainObjectFactory f = DomainObjectFactory.getInstance();

    public void testAttributeOfDifferentType() {

        MultiModelAttributeBean mmaBean = new MultiModelAttributeBean();
        AttributeInterface a1 = f.createBooleanAttribute();
        AttributeInterface a2 = f.createIntegerAttribute();

        mmaBean.addSelectedAttribute(a1);
        mmaBean.addSelectedAttribute(a2);
        AttributeInterface a = MmcAttributeGenerator.getAttribute(mmaBean);
        assertTrue(a.getAttributeTypeInformation() instanceof StringAttributeTypeInformation);
    }

    public void testAttributeOfSameTypeNoPVs() {

        MultiModelAttributeBean mmaBean = new MultiModelAttributeBean();
        AttributeInterface a1 = f.createIntegerAttribute();
        AttributeInterface a2 = f.createIntegerAttribute();

        mmaBean.addSelectedAttribute(a1);
        mmaBean.addSelectedAttribute(a2);
        AttributeInterface a = MmcAttributeGenerator.getAttribute(mmaBean);
        assertTrue(a.getAttributeTypeInformation() instanceof IntegerAttributeTypeInformation);
        DataElementInterface de = a.getAttributeTypeInformation().getDataElement();
        UserDefinedDE userDefinedDE = (UserDefinedDE) de;
        assertNull(userDefinedDE);
    }

    public void testAttributeOfSameTypeDifferentPVs() {

        MultiModelAttributeBean mmaBean = new MultiModelAttributeBean();

        Set<String> permissibleValues1 = new HashSet<String>(3);
        permissibleValues1.add("Male");
        permissibleValues1.add("Female");
        permissibleValues1.add("Unknown");

        Set<String> permissibleValues = new HashSet<String>(3);
        permissibleValues.add("M");
        permissibleValues.add("F");
        permissibleValues.add("U");
        AttributeInterface a1 = getAttrWithPvs(permissibleValues1);
        AttributeInterface a2 = getAttrWithPvs(permissibleValues);

        mmaBean.addSelectedAttribute(a1);
        mmaBean.addSelectedAttribute(a2);
        AttributeInterface a = MmcAttributeGenerator.getAttribute(mmaBean);
        DataElementInterface de = a.getAttributeTypeInformation().getDataElement();
        UserDefinedDEInterface userDefined = (UserDefinedDEInterface) de;
        assertEquals(userDefined.getPermissibleValueCollection().size(),6);
    }

    public void testAttributeOfSameTypeSamePVs() {

        MultiModelAttributeBean mmaBean = new MultiModelAttributeBean();

        Set<String> permissibleValues = new HashSet<String>(3);
        permissibleValues.add("Male");
        permissibleValues.add("Female");
        permissibleValues.add("Unknown");

        AttributeInterface a1 = getAttrWithPvs(permissibleValues);
        AttributeInterface a2 = getAttrWithPvs(permissibleValues);

        mmaBean.addSelectedAttribute(a1);
        mmaBean.addSelectedAttribute(a2);
        AttributeInterface a = MmcAttributeGenerator.getAttribute(mmaBean);
        DataElementInterface de = a.getAttributeTypeInformation().getDataElement();
        assertNotNull(de);
        assertTrue(de instanceof UserDefinedDEInterface);
        Collection<PermissibleValueInterface> pvs = ((UserDefinedDEInterface) de).getPermissibleValueCollection();
        assertNotNull(pvs);
        assertTrue(pvs.size() == 3);
        Set<String> actualpv = new HashSet<String>(3);
        for (PermissibleValueInterface pv : pvs) {
            actualpv.add(pv.getValueAsObject().toString());
        }
        assertEquals(permissibleValues, actualpv);
    }
    
    public void testAttributeOfBooleanTypeSamePV() {
        MultiModelAttributeBean mmaBean = new MultiModelAttributeBean();

        Set<Boolean> permissibleValues = new HashSet<Boolean>(2);
        permissibleValues.add(Boolean.TRUE);
        permissibleValues.add(Boolean.FALSE);

        AttributeInterface a1 = getAttrWithBooleanPvs(f.createBooleanAttribute(),permissibleValues);
        AttributeInterface a2 = getAttrWithBooleanPvs(f.createBooleanAttribute(),permissibleValues);

        mmaBean.addSelectedAttribute(a1);
        mmaBean.addSelectedAttribute(a2);
        
        AttributeInterface a = MmcAttributeGenerator.getAttribute(mmaBean);
        DataElementInterface de = a.getAttributeTypeInformation().getDataElement();
        assertNotNull(de);
        assertTrue(de instanceof UserDefinedDEInterface);
        Collection<PermissibleValueInterface> pvs = ((UserDefinedDEInterface) de).getPermissibleValueCollection();
        assertNotNull(pvs);
        assertTrue(pvs.size() == 2);
        Set<Boolean> actualpv = new HashSet<Boolean>(2);
        for (PermissibleValueInterface pv : pvs) {
            actualpv.add((Boolean)pv.getValueAsObject());
            assertTrue(pv instanceof BooleanValueInterface);
        }
        assertEquals(permissibleValues, actualpv);
        
    }

    public void testAttributeOfDifferentTypeSamePVs() {

        MultiModelAttributeBean mmaBean = new MultiModelAttributeBean();

        Set<String> permissibleValues = new HashSet<String>(3);
        permissibleValues.add("Male");
        permissibleValues.add("Female");
        permissibleValues.add("Unknown");

        AttributeInterface a1 = getAttrWithPvs(permissibleValues);
        AttributeInterface a2 = getAttrWithPvs(f.createBooleanAttribute(), permissibleValues);

        mmaBean.addSelectedAttribute(a1);
        mmaBean.addSelectedAttribute(a2);
        AttributeInterface a = MmcAttributeGenerator.getAttribute(mmaBean);
        assertTrue(a.getAttributeTypeInformation() instanceof StringAttributeTypeInformation);
        DataElementInterface de = a.getAttributeTypeInformation().getDataElement();
        assertNull(de);
    }
    
    private AttributeInterface getAttrWithPvs(Set<String> arr) {
        AttributeInterface a1 = f.createStringAttribute();
        UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
        for (String e : arr) {
            StringValueInterface value = f.createStringValue();
            value.setValue(e);
            userDefinedDE.addPermissibleValue(value);
        }
        a1.getAttributeTypeInformation().setDataElement(userDefinedDE);
        return a1;
    }

    private AttributeInterface getAttrWithPvs(AttributeInterface a1, Set<String> arr) {

        UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
        for (String e : arr) {
            StringValueInterface value = f.createStringValue();
            value.setValue(e);
            userDefinedDE.addPermissibleValue(value);
        }
        a1.getAttributeTypeInformation().setDataElement(userDefinedDE);
        return a1;
    }
    
    private AttributeInterface getAttrWithBooleanPvs(AttributeInterface a1, Set<Boolean> arr) {

        UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
        for (Boolean e : arr) {
            BooleanValueInterface value = f.createBooleanValue();
            value.setValue(e);
            userDefinedDE.addPermissibleValue(value);
        }
        a1.getAttributeTypeInformation().setDataElement(userDefinedDE);
        return a1;
    }
}

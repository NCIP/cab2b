package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.common.util.Constants.CAB2B_ENTITY_GROUP;

import java.util.Collection;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;

/**
 * @author Chandrakant Talele
 */
public class DynamicExtensionUtilityTest extends TestCase {
    DomainObjectFactory fact = DomainObjectFactory.getInstance();

    public void testSetSemanticMetadata() {

        AttributeInterface attrib = fact.createStringAttribute();
        SemanticMetadata[] semanticMetadataArr = new SemanticMetadata[1];

        semanticMetadataArr[0] = new SemanticMetadata();
        semanticMetadataArr[0].setConceptCode("conceptcode");
        semanticMetadataArr[0].setConceptName("conceptName");

        DynamicExtensionUtility.setSemanticMetadata(attrib, semanticMetadataArr);
        assertEquals(1, attrib.getSemanticPropertyCollection().size());

        SemanticPropertyInterface sp = (SemanticPropertyInterface) attrib.getSemanticPropertyCollection().iterator().next();
        assertEquals(semanticMetadataArr[0].getConceptCode(), sp.getConceptCode());
        assertEquals(semanticMetadataArr[0].getConceptName(), sp.getTerm());
        assertEquals(0, sp.getSequenceNumber());

    }

    public void testSetSemanticMetadataNull() {
        AttributeInterface attrib = fact.createStringAttribute();
        DynamicExtensionUtility.setSemanticMetadata(attrib, null);
        assertEquals(0, attrib.getSemanticPropertyCollection().size());
    }

    public void testAddTaggedValue() {
        AttributeInterface attrib = fact.createStringAttribute();
        DynamicExtensionUtility.addTaggedValue(attrib, "key", "value");
        assertEquals(1, attrib.getTaggedValueCollection().size());
        TaggedValueInterface tag = attrib.getTaggedValueCollection().iterator().next();
        assertEquals("key", tag.getKey());
        assertEquals("value", tag.getValue());
    }

    public void testCreateEntityGroup() {
        EntityGroupInterface eg = DynamicExtensionUtility.createEntityGroup();
        assertEquals(1, eg.getTaggedValueCollection().size());
        TaggedValueInterface tag = eg.getTaggedValueCollection().iterator().next();
        assertEquals(CAB2B_ENTITY_GROUP, tag.getKey());
        assertEquals(CAB2B_ENTITY_GROUP, tag.getValue());
    }

    public void testGetNewRole() {
        AssociationType type = AssociationType.ASSOCIATION;
        String name = "someName";
        Cardinality min = Cardinality.ONE;
        Cardinality max = Cardinality.MANY;
        RoleInterface role = DynamicExtensionUtility.getNewRole(type, name, min, max);
        verifyRole(role, type, name, min, max);
    }

    public void testCreateNewOneToManyAsso() {
        EntityInterface src = fact.createEntity();
        src.setId(1224L);
        EntityInterface tgt = fact.createEntity();
        tgt.setId(123524L);
        AssociationInterface assoc = DynamicExtensionUtility.createNewOneToManyAsso(src, tgt);

        String name = "AssociationName_1";
        assertEquals(name, assoc.getName());
        assertEquals(AssociationDirection.SRC_DESTINATION, assoc.getAssociationDirection());
        assertEquals(src, assoc.getEntity());
        assertEquals(tgt, assoc.getTargetEntity());

        Cardinality zero = Cardinality.ZERO;
        Cardinality one = Cardinality.ONE;
        Cardinality many = Cardinality.MANY;
        AssociationType type = AssociationType.CONTAINTMENT;

        String srcName = "source_role_" + name;
        String tgtName = "target_role_" + name;

        verifyRole(assoc.getSourceRole(), type, srcName, one, one);
        verifyRole(assoc.getTargetRole(), type, tgtName, zero, many);
    }

    public void testGetAttributeCopy() {
        AttributeInterface[] arr = { fact.createStringAttribute(), fact.createIntegerAttribute(), fact.createLongAttribute(), fact.createDoubleAttribute(), fact.createFloatAttribute(), fact.createDateAttribute(), fact.createBooleanAttribute() };
        for (int i = 0; i < arr.length; i++) {
            arr[i].setName("name" + i);
            arr[i].setDescription("Description" + i);
            AttributeInterface attr = DynamicExtensionUtility.getAttributeCopy(arr[i]);
            assertEquals(arr[i].getClass(), attr.getClass());
            assertEquals(arr[i].getName(), attr.getName());
            assertEquals(arr[i].getDescription(), attr.getDescription());
        }
    }

    public void testGetAttributeCopyWithPV() {
        StringValueInterface value = fact.createStringValue();
        value.setValue("val_");

        UserDefinedDEInterface userDefinedDE = fact.createUserDefinedDE();
        userDefinedDE.addPermissibleValue(value);

        AttributeInterface attrib = fact.createStringAttribute();
        attrib.getAttributeTypeInformation().setDataElement(userDefinedDE);

        AttributeInterface attr = DynamicExtensionUtility.getAttributeCopy(attrib);
        assertEquals(attrib.getClass(), attr.getClass());
        UserDefinedDEInterface res = (UserDefinedDEInterface) attr.getAttributeTypeInformation().getDataElement();
        assertEquals(1, res.getPermissibleValueCollection().size());
        assertEquals("val_", res.getPermissibleValueCollection().iterator().next().getValueAsObject());

    }

    public void testGetAttributeCopyWithCustomName() {
        AttributeInterface arr = fact.createStringAttribute();
        AttributeInterface attr = DynamicExtensionUtility.getAttributeCopy(arr, "name");
        assertEquals("name", attr.getName());

    }

    public void testGetAttributeCopyWithConceptCode() {
        AttributeInterface attrib = fact.createStringAttribute();
        SemanticPropertyInterface semanticProp = fact.createSemanticProperty();
        semanticProp.setTerm("term");
        semanticProp.setConceptCode("conceptCode");
        attrib.addSemanticProperty(semanticProp);

        AttributeInterface res = DynamicExtensionUtility.getAttributeCopy(attrib);
        Collection<SemanticPropertyInterface> coll = res.getSemanticPropertyCollection();
        assertEquals(1, coll.size());

        SemanticPropertyInterface prop = coll.iterator().next();

        assertEquals("conceptCode", prop.getConceptCode());
        assertEquals("term", prop.getTerm());

    }

    public void testCloneRole() {
        String name = "RoleName";
        Cardinality one = Cardinality.ONE;
        Cardinality many = Cardinality.MANY;
        AssociationType type = AssociationType.CONTAINTMENT;

        RoleInterface role = fact.createRole();
        role.setAssociationsType(type);
        role.setName(name);
        role.setMaximumCardinality(many);
        role.setMinimumCardinality(one);

        RoleInterface res = DynamicExtensionUtility.cloneRole(role);
        verifyRole(res, type, name, one, many);
    }

    private void verifyRole(RoleInterface roleToVerify, AssociationType associationType, String name,
                            Cardinality minCard, Cardinality maxCard) {
        assertEquals(associationType, roleToVerify.getAssociationsType());
        assertEquals(maxCard, roleToVerify.getMaximumCardinality());
        assertEquals(minCard, roleToVerify.getMinimumCardinality());
        assertEquals(name, roleToVerify.getName());
    }

}

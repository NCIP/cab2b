/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.common.util.Constants.TYPE_DERIVED;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.Path;

public class TestUtil {
   
    private static DomainObjectFactory deFactory = DomainObjectFactory.getInstance();

    public static EntityInterface getEntity(String entityName, String attrName, EntityInterface parent) {
        AttributeInterface a = getAttribute(attrName);

        EntityInterface e1 = deFactory.createEntity();
        e1.setName(entityName);
        if (parent != null) {
            e1.setParentEntity(parent);
            markInherited(a);
        }
        e1.addAttribute(a);
        return e1;
    }

    public static EntityInterface getEntity(String entityName, String attrName) {
        AttributeInterface a = getAttribute(attrName);
        EntityInterface e1 = getEntity(entityName);
        e1.addAttribute(a);
        a.setEntity(e1);
        return e1;
    }
    public static AttributeInterface getAttribute(String entityName, long entityId, String attrName,long attrId) {
        EntityInterface e = getEntity(entityName,attrName);
        AttributeInterface a = e.getAttributeCollection().iterator().next();
        e.setId(entityId);
        a.setId(attrId);
        return a;
    }
    public static EntityInterface getEntity(String entityName) {
        EntityInterface e1 = deFactory.createEntity();
        e1.setName(entityName);
        return e1;
    }

    public static EntityInterface getEntity(String entityName, long id) {
        EntityInterface e1 = deFactory.createEntity();
        e1.setId(id);
        e1.setName(entityName);
        return e1;
    }

    public static EntityGroupInterface getEntityGroup(String name, long id) {
        EntityGroupInterface e1 = deFactory.createEntityGroup();
        e1.setId(id);
        e1.setName(name);
        e1.setLongName(name);
        return e1;
    }

    public static void markInherited(AbstractMetadataInterface owner) {
        TaggedValueInterface taggedValue = deFactory.createTaggedValue();
        taggedValue.setKey(TYPE_DERIVED);
        taggedValue.setValue(TYPE_DERIVED);
        owner.addTaggedValue(taggedValue);
    }

    public static AssociationInterface getAssociation(String srcEntityName, String targetEntityName) {
        EntityInterface src = getEntity(srcEntityName);
        EntityInterface target = getEntity(targetEntityName);
        return getAssociation(src,target);
    }
    public static AssociationInterface getAssociation(EntityInterface src, EntityInterface target) {

        AssociationInterface association = deFactory.createAssociation();
        association.setName("AssociationName_" + src.getAssociationCollection().size() + 1);
        association.setEntity(src);
        src.addAssociation(association);
        association.setSourceRole(getRole("srcRole"));
        association.setTargetEntity(target);
        association.setTargetRole(getRole("tgtRole"));
        association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
        return association;
    }
    private static RoleInterface getRole(String roleName) {
        RoleInterface role = deFactory.createRole();
        role.setAssociationsType(Constants.AssociationType.ASSOCIATION);
        role.setName(roleName);
        role.setMaximumCardinality(Constants.Cardinality.MANY);
        role.setMinimumCardinality(Constants.Cardinality.MANY);
        return role;
    }

    public static AttributeInterface getAttribute(String name) {
        AttributeInterface a = deFactory.createStringAttribute();
        a.setName(name);
        return a;
    }
    public static EntityInterface getEntityWithGrp(String groupName, String entityName, String attrName) {
        AttributeInterface a = getAttribute(attrName);
        EntityInterface e1 = getEntity(entityName);
        e1.addAttribute(a);
        a.setEntity(e1);
        
        EntityGroupInterface eg = DomainObjectFactory.getInstance().createEntityGroup();
        eg.setName(groupName);
        eg.addEntity(e1);
        
        e1.addEntityGroupInterface(eg);
        return e1;
    }
    public static EntityInterface getEntityWithCaB2BGrp(String groupName, String entityName, String attrName) {
        AttributeInterface a = getAttribute(attrName);
        EntityInterface e1 = getEntity(entityName);
        e1.addAttribute(a);
        a.setEntity(e1);
        
        EntityGroupInterface eg = DomainObjectFactory.getInstance().createEntityGroup();
        DynamicExtensionUtility.addTaggedValue(eg,edu.wustl.cab2b.common.util.Constants.CAB2B_ENTITY_GROUP, edu.wustl.cab2b.common.util.Constants.CAB2B_ENTITY_GROUP);
        eg.setName(groupName);
        eg.addEntity(e1);
        
        e1.addEntityGroupInterface(eg);
        return e1;
    }
    public static Path getPath(IAssociation association) {
        List<IAssociation> allAssociation = new ArrayList<IAssociation>(1);
        allAssociation.add(association);
        return new Path(association.getSourceEntity(), association.getTargetEntity(), allAssociation);
    }
}
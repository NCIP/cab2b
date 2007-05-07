package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants.DE_0003;
import static edu.wustl.cab2b.common.util.Constants.CAB2B_ENTITY_GROUP;

import java.util.Date;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.SemanticAnnotatableInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.path.PathConstants;
import edu.wustl.common.querysuite.queryobject.DataType;
import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;

/**
 * This class decides whether to create a storage table for entity or not based on {@link edu.wustl.cab2b.server.path.PathConstants#CREATE_TABLE_FOR_ENTITY}
 * To create a table for entity set this to TRUE before calling this code else set it to false.
 * @author Chandrakant Talele
 */
public class DynamicExtensionUtility {

    /**
     * Persist given entity using dynamic extension APIs.
     * Whether to create dataspace (table) for this entity or not is decided by {@link PathConstants.CREATE_TABLE_FOR_ENTITY} 
     * @param entity Entity to persist.
     * @return The saved entity
     */
    public static EntityInterface persistEntity(EntityInterface entity) {
        EntityManagerInterface entityManager = EntityManager.getInstance();
        try {
            if (PathConstants.CREATE_TABLE_FOR_ENTITY) {
                entity = entityManager.persistEntity(entity);
            } else {
                entity = entityManager.persistEntityMetadata(entity, false);
            }
        } catch (DynamicExtensionsSystemException e) {
            throw new RuntimeException("Unable to persist Entity in Dynamic Extension", e,
                    ErrorCodeConstants.DE_0002);
        } catch (DynamicExtensionsApplicationException e) {
            throw new RuntimeException("Unable to persist Entity in Dynamic Extension", e,
                    ErrorCodeConstants.DE_0002);
        }
        return entity;
    }

    /**
     * Persist given entity Group using dynamic extension APIs.
     * Whether to store only metadata for this entity Group is decided by {@link PathConstants.CREATE_TABLE_FOR_ENTITY} 
     * @param entityGroup entity Group to persist.
     * @return The saved entity Group
     */
    public static EntityGroupInterface persistEntityGroup(EntityGroupInterface entityGroup) {
        EntityManagerInterface entityManager = EntityManager.getInstance();
        try {
            if (PathConstants.CREATE_TABLE_FOR_ENTITY) {
                entityGroup = entityManager.persistEntityGroup(entityGroup);
            } else {
                entityGroup = entityManager.persistEntityGroupMetadata(entityGroup);
            }
        } catch (DynamicExtensionsSystemException e) {
            throw new RuntimeException("Unable to persist Entity Group in Dynamic Extension", e,
                    ErrorCodeConstants.DE_0001);
        } catch (DynamicExtensionsApplicationException e) {
            throw new RuntimeException("Unable to persist Entity Group in Dynamic Extension", e,
                    ErrorCodeConstants.DE_0001);
        }

        return entityGroup;
    }

    /**
     * Returns the Entity for given Identifier
     * @param id Id of the entity
     * @return Actual Entity for given id.
     */
    public static EntityInterface getEntityById(Long id) {
        try {
            return EntityManager.getInstance().getEntityByIdentifier(id);
        } catch (DynamicExtensionsSystemException e) {
            throw new RuntimeException("Expected associations are not found in the database", e, DE_0003);
        } catch (DynamicExtensionsApplicationException e) {
            throw new RuntimeException("Expected associations are not found in the database", e, DE_0003);
        }
    }

    /**
     * Returns the Association for given Identifier
     * @param id Id of the Association
     * @return Actual Association for given id.
     */
    public static AssociationInterface getAssociationById(Long id) {
        try {
            AssociationInterface association = EntityManager.getInstance().getAssociationByIdentifier(id);
            return association;
        } catch (DynamicExtensionsSystemException e) {
            throw new RuntimeException("Expected associations are not found in the database", e, DE_0003);
        } catch (DynamicExtensionsApplicationException e) {
            throw new RuntimeException("Expected associations are not found in the database", e, DE_0003);
        }
    }

    /**
     * Returns the entity group of given entity Id
     * @param id Entity Id
     * @return Returns parent Entity Group
     */
    public static EntityGroupInterface getEntityGroupForEntityId(Long id) {
        EntityInterface entity = getEntityById(id);
        return Utility.getEntityGroup(entity);
    }

    /**
     * Returns the AttributeInterface whose name matches with given "attributeName" and whose parent name matches with "entityName" 
     * @param entityName name of parent entity.
     * @param attributeName name of required attribute.
     * @return Returns Attribute satisfying given conditions. 
     */
    public static AttributeInterface getAttribute(String entityName, String attributeName) {
        try {
            return EntityManager.getInstance().getAttribute(entityName, attributeName);
        } catch (DynamicExtensionsSystemException e) {
            throw new RuntimeException("Expected attribute not found in database : " + entityName + "-"
                    + attributeName, e, ErrorCodeConstants.DE_0003);
        } catch (DynamicExtensionsApplicationException e) {
            throw new RuntimeException("Expected attribute not found in database : " + entityName + "-"
                    + attributeName, e, ErrorCodeConstants.DE_0003);
        }

    }

    /**
     * Stores the SemanticMetadata to the owner which can be class or attribute
     * @param owner EntityInterface OR AttributeInterface
     * @param semanticMetadataArr Semantic Metadata array to set.
     */
    public static void setSemanticMetadata(SemanticAnnotatableInterface owner,
                                           SemanticMetadata[] semanticMetadataArr) {
        if (semanticMetadataArr == null) {
            return;
        }
        DomainObjectFactory deFactory = DomainObjectFactory.getInstance();
        for (int i = 0; i < semanticMetadataArr.length; i++) {
            SemanticPropertyInterface semanticProp = deFactory.createSemanticProperty();
            semanticProp.setSequenceNumber(i);
            semanticProp.setConceptCode(semanticMetadataArr[i].getConceptCode());
            semanticProp.setTerm(semanticMetadataArr[i].getConceptName());
            owner.addSemanticProperty(semanticProp);
        }
    }

    /**
     * Creates a tagged value and adds it to the owner.
     * @param owner Owner of the tagged value
     * @param key Key to be used for tagging
     * @param value Actual value of the tag.
     */
    public static void addTaggedValue(AbstractMetadataInterface owner, String key, String value) {
        DomainObjectFactory deFactory = DomainObjectFactory.getInstance();
        TaggedValueInterface taggedValue = deFactory.createTaggedValue();
        taggedValue.setKey(key);
        taggedValue.setValue(value);
        owner.addTaggedValue(taggedValue);
    }

    /**
     * This method searches for is there any entity group present with given name. 
     * If present it returns the entity group.If not present then it creates a entity group with given name.
     * Saves it, and returns the saved Entity Group.
     * @param name Name of the entity group.
     * @return Entity group with  given name
     */
    public static EntityGroupInterface getEntityGroupByName(String name) {
        EntityGroupInterface entityGroup = null;
        try {
            entityGroup = EntityManager.getInstance().getEntityGroupByName(name);
        } catch (DynamicExtensionsSystemException e) {
            throw new RuntimeException("Got System exception from Dynamic Extnsion while fetching entity group",
                    e, ErrorCodeConstants.DB_0001);
        } catch (DynamicExtensionsApplicationException e) {
            throw new RuntimeException("Got System exception from Dynamic Extnsion while fetching entity group",
                    e, ErrorCodeConstants.DB_0001);
        }
        if (entityGroup == null) {
            entityGroup = createEntityGroup();
            entityGroup.setShortName(name);
            entityGroup.setName(name);
            entityGroup.setLongName(name);
            entityGroup.setDescription(name);
            DynamicExtensionUtility.persistEntityGroup(entityGroup);
        }
        return entityGroup;
    }

    /**
     * creates a new entity group, and tags it to identify caB2b entity group.
     * @return EntityGroupInterface newly created unsaved entity group.
     */
    public static EntityGroupInterface createEntityGroup() {
        EntityGroupInterface entityGroup = DomainObjectFactory.getInstance().createEntityGroup();
        addTaggedValue(entityGroup, CAB2B_ENTITY_GROUP, CAB2B_ENTITY_GROUP);
        return entityGroup;
    }
    /**
     * Copies the attribute's description, semantic metadata and associated permissible values.
     * Sets the given name to copied attribute
     * @param source Attribute to copy
     * @param name New name to be given to copied attribute
     * @return The copied attribute
     */
    public static AttributeInterface getAttributeCopy(AttributeInterface source, String name) {
        AttributeInterface attribute = getAttributeCopy(source);
        attribute.setName(name);
        return attribute;
    }
    /**
     * Copies the attribute's name,description, semantic metadata and associated permissible values
     * @param source Attribute to copy
     * @return The cloned attribute
     */
    public static AttributeInterface getAttributeCopy(AttributeInterface source) {
        DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
        AttributeInterface attribute = null;
        DataType type = Utility.getDataType(source.getAttributeTypeInformation());
        DataElementInterface dataEle = source.getAttributeTypeInformation().getDataElement();
        switch (type) {
            case String:
                attribute = domainObjectFactory.createStringAttribute();
                if (dataEle instanceof UserDefinedDEInterface) {
                    UserDefinedDEInterface userDefinedDE = domainObjectFactory.createUserDefinedDE();
                    for (PermissibleValueInterface val : ((UserDefinedDEInterface) dataEle).getPermissibleValueCollection()) {
                        StringValueInterface value = domainObjectFactory.createStringValue();
                        value.setValue((String) val.getValueAsObject());
                        userDefinedDE.addPermissibleValue(value);
                    }
                    attribute.getAttributeTypeInformation().setDataElement(
                                                                           userDefinedDE);
                }
                break;

            case Double:
                attribute = domainObjectFactory.createDoubleAttribute();
                if (dataEle instanceof UserDefinedDEInterface) {
                    UserDefinedDEInterface userDefinedDE = domainObjectFactory.createUserDefinedDE();
                    for (PermissibleValueInterface val : ((UserDefinedDEInterface) dataEle).getPermissibleValueCollection()) {
                        DoubleValueInterface value = domainObjectFactory.createDoubleValue();
                        value.setValue((Double) val.getValueAsObject());
                        userDefinedDE.addPermissibleValue(value);
                    }
                    attribute.getAttributeTypeInformation().setDataElement(
                                                                           userDefinedDE);
                }
                break;

            case Integer:
                attribute = domainObjectFactory.createIntegerAttribute();
                if (dataEle instanceof UserDefinedDEInterface) {
                    UserDefinedDEInterface userDefinedDE = domainObjectFactory.createUserDefinedDE();
                    for (PermissibleValueInterface val : ((UserDefinedDEInterface) dataEle).getPermissibleValueCollection()) {
                        IntegerValueInterface value = domainObjectFactory.createIntegerValue();
                        value.setValue((Integer) val.getValueAsObject());
                        userDefinedDE.addPermissibleValue(value);
                    }
                    attribute.getAttributeTypeInformation().setDataElement(
                                                                           userDefinedDE);
                }
                break;

            case Date:
                attribute = domainObjectFactory.createDateAttribute();
                if (dataEle instanceof UserDefinedDEInterface) {
                    UserDefinedDEInterface userDefinedDE = domainObjectFactory.createUserDefinedDE();
                    for (PermissibleValueInterface val : ((UserDefinedDEInterface) dataEle).getPermissibleValueCollection()) {
                        DateValueInterface value = domainObjectFactory.createDateValue();
                        value.setValue((Date) val.getValueAsObject());
                        userDefinedDE.addPermissibleValue(value);
                    }
                    attribute.getAttributeTypeInformation().setDataElement(
                                                                           userDefinedDE);
                }
                break;

            case Float:
                attribute = domainObjectFactory.createFloatAttribute();
                if (dataEle instanceof UserDefinedDEInterface) {
                    UserDefinedDEInterface userDefinedDE = domainObjectFactory.createUserDefinedDE();
                    for (PermissibleValueInterface val : ((UserDefinedDEInterface) dataEle).getPermissibleValueCollection()) {
                        FloatValueInterface value = domainObjectFactory.createFloatValue();
                        value.setValue((Float) val.getValueAsObject());
                        userDefinedDE.addPermissibleValue(value);
                    }
                    attribute.getAttributeTypeInformation().setDataElement(
                                                                           userDefinedDE);
                }
                break;

            case Boolean:
                attribute = domainObjectFactory.createBooleanAttribute();
                if (dataEle instanceof UserDefinedDEInterface) {
                    UserDefinedDEInterface userDefinedDE = domainObjectFactory.createUserDefinedDE();
                    for (PermissibleValueInterface val : ((UserDefinedDEInterface) dataEle).getPermissibleValueCollection()) {
                        BooleanValueInterface value = domainObjectFactory.createBooleanValue();
                        value.setValue((Boolean) val.getValueAsObject());
                        userDefinedDE.addPermissibleValue(value);
                    }
                    attribute.getAttributeTypeInformation().setDataElement(
                                                                           userDefinedDE);
                }
                break;

            case Long:
                attribute = domainObjectFactory.createLongAttribute();
                if (dataEle instanceof UserDefinedDEInterface) {
                    UserDefinedDEInterface userDefinedDE = domainObjectFactory.createUserDefinedDE();
                    for (PermissibleValueInterface val : ((UserDefinedDEInterface) dataEle).getPermissibleValueCollection()) {
                        LongValueInterface value = domainObjectFactory.createLongValue();
                        value.setValue((Long) val.getValueAsObject());
                        userDefinedDE.addPermissibleValue(value);
                    }
                    attribute.getAttributeTypeInformation().setDataElement(
                                                                           userDefinedDE);
                }
                break;

        }
        
        attribute.setDescription(source.getDescription());
        copySemanticProperties(source, attribute);
        return attribute;
    }
    /**
     * Stores the SemanticMetadata to the owner which can be class or attribute
     * @param owner
     *            EntityInterface OR AttributeInterface
     * @param semanticMetadataArr
     *            Semantic Metadata array to set.
     */
    private static void copySemanticProperties(AbstractMetadataInterface copyFrom,
                                AbstractMetadataInterface copyTo) {
        DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
        for (SemanticPropertyInterface p : copyFrom.getSemanticPropertyCollection()) {
            SemanticPropertyInterface semanticProp = domainObjectFactory.createSemanticProperty();
            semanticProp.setTerm(p.getTerm());
            semanticProp.setConceptCode(p.getConceptCode());
            copyTo.addSemanticProperty(semanticProp);
        }
    }

}
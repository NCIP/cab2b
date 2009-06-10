package edu.wustl.cab2b.server.category;

import static edu.wustl.cab2b.common.util.Constants.CATEGORY_ENTITY_GROUP_NAME;
import static edu.wustl.cab2b.common.util.Constants.TYPE_CATEGORY;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
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
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.queryobject.DataType;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Chandrakant Talele
 */
public class PersistCategory {

    /**
     * Category entity which will be saved by this class.
     */
    private EntityInterface categoryEntity;

    /**
     * This map is used to populate the Id of newly created attribute.
     */
    private Map<String, CategorialAttribute> nameVsCategorialAttr = new HashMap<String, CategorialAttribute>();

    /**
     * @param inputCategory
     *            The inputCategory for which a Category object is to be
     *            created.
     * @param parentCategry
     *            Parent category of the returned Categoty. This will be null
     *            for root category.
     * @return Returns the Category for given parameters.
     */
    public Category persistCategory(InputCategory inputCategory,
                                    Category parentCategry) {
        categoryEntity = DomainObjectFactory.getInstance().createEntity();
        List<EntityInterface> subCategoryEntities = new ArrayList<EntityInterface>();
        Category category = new Category();
        category.setParentCategory(parentCategry);

        Set<Category> subCategories = new HashSet<Category>();
        for (InputCategory subCategory : inputCategory.getSubCategories()) {
            PersistCategory p = new PersistCategory();
            subCategories.add(p.persistCategory(subCategory, category));
            subCategoryEntities.add(p.getCategoryEntity());
        }
        category.setSubCategories(subCategories);

        CategorialClass rootClass = getCategorialClass(
                                                       inputCategory.getRootCategorialClass(),
                                                       null, category);
        category.setRootClass(rootClass);
//TODO is display name is needed for category ?? 
        categoryEntity.setName(inputCategory.getName());
        categoryEntity.setDescription(inputCategory.getDescription());
        categoryEntity.addEntityGroupInterface(getCategoryEntityGroup());
        DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
        for (EntityInterface subCategoryEntity : subCategoryEntities) {
            AssociationInterface association = domainObjectFactory.createAssociation();
            association.setEntity(categoryEntity);
            association.setSourceRole(getRole());
            association.setTargetEntity(subCategoryEntity);
            association.setTargetRole(getRole());
            association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
            categoryEntity.addAssociation(association);
        }
        DynamicExtensionUtility.addTaggedValue(categoryEntity, TYPE_CATEGORY, TYPE_CATEGORY);
        categoryEntity = DynamicExtensionUtility.persistEntity(categoryEntity);
        category.setDeEntityId(categoryEntity.getId());

        for (AttributeInterface attr : categoryEntity.getAttributeCollection()) {
            String attributeName = attr.getName();
            nameVsCategorialAttr.get(attributeName).setDeCategoryAttributeId(
                                                                             attr.getId());
        }
        return category;

    }

    /**
     * @param inputCategorialClass
     *            The inputCategory for which a Category object is to be
     *            CategorialClass.
     * @param parent
     *            Parent CategorialClass of the returned CategorialClass.
     * @param category
     *            The owner Category
     * @return CategorialClass for given parameters.
     */
    private CategorialClass getCategorialClass(
                                               InputCategorialClass inputCategorialClass,
                                               CategorialClass parent,
                                               Category category) {
        CategorialClass categorialClass = new CategorialClass();
        categorialClass.setParent(parent);
        categorialClass.setCategory(category);

        Set<CategorialAttribute> categorialAttributeSet = new HashSet<CategorialAttribute>();

        for (InputCategorialAttribute inputCategorialAttribute : inputCategorialClass.getAttributeList()) {
            String name = inputCategorialAttribute.getDisplayName();
            AttributeInterface originalAttr = inputCategorialAttribute.getDynamicExtAttribute();
            categorialClass.setDeEntityId(originalAttr.getEntity().getId());

            CategorialAttribute categorialAttribute = new CategorialAttribute();
            categorialAttribute.setCategorialClass(categorialClass);
            categorialAttribute.setDeSourceClassAttributeId(originalAttr.getId());
            categorialAttributeSet.add(categorialAttribute);

            nameVsCategorialAttr.put(name, categorialAttribute);
            categoryEntity.addAttribute(getCopy(originalAttr, name));
        }

        categorialClass.setCategorialAttributeCollection(categorialAttributeSet);
        categorialClass.setPathFromParentId(inputCategorialClass.getPathFromParent());

        Set<CategorialClass> childrenCategorialClass = new HashSet<CategorialClass>();
        for (InputCategorialClass childInputCategorialClass : inputCategorialClass.getChildren()) {
            childrenCategorialClass.add(getCategorialClass(
                                                           childInputCategorialClass,
                                                           categorialClass,
                                                           category));
        }

        categorialClass.setChildren(childrenCategorialClass);
        return categorialClass;
    }

    /**
     * @return The category entity group.
     */
    private EntityGroupInterface getCategoryEntityGroup() {
        EntityGroupInterface entityGroup = DynamicExtensionUtility.getEntityGroupByName(CATEGORY_ENTITY_GROUP_NAME);
        return entityGroup;
    }

    /**
     * @param source
     * @param name
     * @return
     */
    private AttributeInterface getCopy(AttributeInterface source, String name) {
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
        attribute.setName(name);
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
    void copySemanticProperties(AbstractMetadataInterface copyFrom,
                                AbstractMetadataInterface copyTo) {
        DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
        for (SemanticPropertyInterface p : copyFrom.getSemanticPropertyCollection()) {
            SemanticPropertyInterface semanticProp = domainObjectFactory.createSemanticProperty();
            semanticProp.setTerm(p.getTerm());
            semanticProp.setConceptCode(p.getConceptCode());
            copyTo.addSemanticProperty(semanticProp);
        }
    }

    /**
     * @return Returns the categoryEntity.
     */
    public EntityInterface getCategoryEntity() {
        return categoryEntity;
    }

    /**
     * @return Returns the RoleInterface used to connect two categories.
     */
    private RoleInterface getRole() {
        RoleInterface role = DomainObjectFactory.getInstance().createRole();
        role.setAssociationsType(Constants.AssociationType.ASSOCIATION);
        role.setName("SYS_" + System.currentTimeMillis());
        role.setMaximumCardinality(Constants.Cardinality.ONE);
        role.setMinimumCardinality(Constants.Cardinality.ONE);
        return role;
    }

    /**
     * @param fileNames the complete path of the files.
     */
    public static void persistCategories(String[] fileNames) {
        for (String fileName : fileNames) {
            InputCategory inputCategory = new CategoryXmlParser().getInputCategory(fileName);
            Category category = new PersistCategory().persistCategory(
                                                                      inputCategory,
                                                                      null);
            new CategoryOperations().saveCategory(category);
            Logger.out.info("Stored Category : " + inputCategory.getName());
        }
    }

    public static void main(String[] args) {
        Logger.configure();
        // String folderPath = "E:/Carcinogenic/caB2B/Categories/from Chandu/";
        String folderPath = "E:/Carcinogenic/caB2B/chandu KT/categories/";

        String[] arr = { "Gene Annotation.xml", "Genomic Identifiers.xml", "Literature-based Gene Association.xml", "Microarray Annotation.xml", "Orthologus Gene.xml" };
        // String[] arr = { "Srinath testCat.xml" };
        System.out.println("=============================================================");
        for (String xmlFileName : arr) {
            String filePath = folderPath + xmlFileName;
            InputCategory inputCategory = new CategoryXmlParser().getInputCategory(filePath);
            Category category = new PersistCategory().persistCategory(
                                                                      inputCategory,
                                                                      null);
            new CategoryOperations().saveCategory(category);
            System.out.println("Stored Category : " + xmlFileName);
        }
        System.out.println("foooo");
    }
}
package edu.wustl.cab2b.server.category;

import static edu.wustl.cab2b.common.util.Constants.CATEGORY_ENTITY_GROUP_NAME;
import static edu.wustl.cab2b.common.util.Constants.TYPE_CATEGORY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author Chandrakant Talele
 */
public class PersistCategory {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(PersistCategory.class);

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
    public Category persistCategory(InputCategory inputCategory, Category parentCategry) {
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

        CategorialClass rootClass = getCategorialClass(inputCategory.getRootCategorialClass(), null, category);
        category.setRootClass(rootClass);
        categoryEntity.setName(inputCategory.getName());
        categoryEntity.setDescription(inputCategory.getDescription());
        categoryEntity.setEntityGroup(getCategoryEntityGroup());
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
            nameVsCategorialAttr.get(attributeName).setDeCategoryAttributeId(attr.getId());
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
    private CategorialClass getCategorialClass(InputCategorialClass inputCategorialClass, CategorialClass parent,
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
            categoryEntity.addAttribute(DynamicExtensionUtility.getAttributeCopy(originalAttr, name));
        }

        categorialClass.setCategorialAttributeCollection(categorialAttributeSet);
        categorialClass.setPathFromParentId(inputCategorialClass.getPathFromParent());

        Set<CategorialClass> childrenCategorialClass = new HashSet<CategorialClass>();
        for (InputCategorialClass childInputCategorialClass : inputCategorialClass.getChildren()) {
            childrenCategorialClass.add(getCategorialClass(childInputCategorialClass, categorialClass, category));
        }

        categorialClass.setChildren(childrenCategorialClass);
        return categorialClass;
    }

    /**
     * @return The category entity group.
     */
    private EntityGroupInterface getCategoryEntityGroup() {
        EntityGroupInterface entityGroup = null;
        try {
            entityGroup = EntityManager.getInstance().getEntityGroupByName(CATEGORY_ENTITY_GROUP_NAME);
        } catch (DynamicExtensionsSystemException e) {
            throw new RuntimeException(
                    "Got System exception from Dynamic Extension while fetching category entity group", e,
                    ErrorCodeConstants.DB_0001);
        } catch (DynamicExtensionsApplicationException e) {
            throw new RuntimeException(
                    "Got System exception from Dynamic Extension while fetching category entity group", e,
                    ErrorCodeConstants.DB_0001);
        }
        if (entityGroup == null) {
            entityGroup = DynamicExtensionUtility.createEntityGroup();
            entityGroup.setShortName(CATEGORY_ENTITY_GROUP_NAME);
            entityGroup.setName(CATEGORY_ENTITY_GROUP_NAME);
            entityGroup.setLongName(CATEGORY_ENTITY_GROUP_NAME);
            entityGroup.setDescription(CATEGORY_ENTITY_GROUP_NAME);
            DynamicExtensionUtility.markMetadataEntityGroup(entityGroup);
            entityGroup = DynamicExtensionUtility.persistEntityGroup(entityGroup);
        }
        return entityGroup;
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
            Category category = new PersistCategory().persistCategory(inputCategory, null);
            new CategoryOperations().saveCategory(category);
            logger.info("Stored Category : " + inputCategory.getName());
        }
    }
    // DON'T delete this code. It is used to load categories from backend 
    //    public static void main(String[] args) {
    //        Logger.configure();
    //        // String folderPath = "E:/Carcinogenic/caB2B/Categories/from Chandu/";
    //        String folderPath = "E:/Carcinogenic/caB2B/chandu KT/categories/";
    //
    //        String[] arr = { "Gene Annotation.xml", "Genomic Identifiers.xml", "Literature-based Gene Association.xml", "Microarray Annotation.xml", "Orthologus Gene.xml" };
    //        // String[] arr = { "Srinath testCat.xml" };
    //         
    //        for (String xmlFileName : arr) {
    //            String filePath = folderPath + xmlFileName;
    //            InputCategory inputCategory = new CategoryXmlParser().getInputCategory(filePath);
    //            Category category = new PersistCategory().persistCategory(
    //                                                                      inputCategory,
    //                                                                      null);
    //            new CategoryOperations().saveCategory(category);
    //        }
    //    }
}
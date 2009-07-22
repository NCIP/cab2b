package edu.wustl.cab2b.server.multimodelcategory;

import static edu.wustl.cab2b.common.util.Constants.MMC_ENTITY_GROUP_NAME;
import static edu.wustl.cab2b.common.util.Constants.MULTIMODELCATEGORY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.cab2b.common.multimodelcategory.bean.MultiModelAttributeBean;
import edu.wustl.cab2b.common.multimodelcategory.bean.MultiModelCategoryBean;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.category.InputCategorialAttribute;
import edu.wustl.cab2b.server.category.InputCategorialClass;
import edu.wustl.cab2b.server.category.InputCategory;
import edu.wustl.cab2b.server.category.PersistCategory;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.metadata.path.IPath;

public class PersistMultiModelCategory {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(PersistMultiModelCategory.class);

    private MultiModelCategoryBean mmcBean;

    private Set<EntityInterface> entities;

    private Map<EntityInterface, Collection<IPath>> sourceEntityToPaths;

    private Map<EntityInterface, Collection<InputCategorialAttribute>> entityInputCatagorialAttributeMap;

    private Map<Long, CategorialAttribute> attributeIDToCatAttr;

    private Map<MultiModelAttribute, Collection<Long>> multiModelAttributeToSelectedAttributeIds;

    private Set<MultiModelAttribute> multiModelAttributes;

    /**
     * This method assumes that MultiModelCategoryBean is well populated, so this class can create InputCategory objects.
     * @param mmcBean
     * @return
     */
    public MultiModelCategory persistMMC(MultiModelCategoryBean mmcBean) {
        this.mmcBean = mmcBean;

        MultiModelCategory multiModelCategory = initialize();

        attributeIDToCatAttr = new HashMap<Long, CategorialAttribute>();
        Collection<Category> categories = new ArrayList<Category>();
        PersistCategory persistCategory = new PersistCategory();

        CategoryOperations categoryOperations = new CategoryOperations();
        Collection<InputCategory> inputCategories = createInputCategories();
        for (InputCategory inCategory : inputCategories) {
            logger.debug("Saving category: " + inCategory);
            Category category = persistCategory.persistCategory(inCategory, null);
            mapAIToCatAttr(category.getRootClass());
            categoryOperations.saveCategory(category);
            categories.add(category);
            logger.debug("Saved category id: " + category.getDeEntityId());
        }
        addCategorialAttrsToMMA();
        multiModelCategory.setCategories(categories);
        multiModelCategory.setMultiModelAttributes(multiModelAttributes);

        return multiModelCategory;
    }

    private void addCategorialAttrsToMMA() {
        for (MultiModelAttribute multiModelAttr : multiModelAttributes) {
            for (Long attribute : multiModelAttributeToSelectedAttributeIds.get(multiModelAttr)) {
                multiModelAttr.addCategorialAttribute(attributeIDToCatAttr.get(attribute));
            }
        }
    }

    /**
     * Recursive call so all children are processed. This Maps AttributeInterface to CategorialAttribute. So mmaToSelectedAI can be used, to derive MMA -> CategorialAttribute 
     * @param catClass
     */
    private void mapAIToCatAttr(CategorialClass catClass) {

        for (CategorialAttribute catAttr : catClass.getCategorialAttributeCollection()) {
            Long ai = catAttr.getDeSourceClassAttributeId();
            attributeIDToCatAttr.put(ai, catAttr);
        }
        //recursive call, so children CA's also mapped.
        for (CategorialClass childCatClass : catClass.getChildren()) {
            mapAIToCatAttr(childCatClass);
        }
    }

    private MultiModelCategory initialize() {
        MultiModelCategory multiModelCategory = createMultiModelCategory();

        entities = new HashSet<EntityInterface>(entityInputCatagorialAttributeMap.keySet());
        Set<EntityInterface> targetEntities = initEntityPathMapAndGetTargetEntities();
        //After removing all targets, 'entities' will only contain root categories
        entities.removeAll(targetEntities);

        createInputCategories();

        return multiModelCategory;
    }

    private MultiModelCategory createMultiModelCategory() {
        EntityInterface mmcEntity = initMMCEntity();

        MultiModelCategory multiModelCategory = new MultiModelCategoryImpl();
        multiModelCategory.setApplicationGroup(mmcBean.getApplicationGroup());
        multiModelCategory.setEntity(mmcEntity);

        return multiModelCategory;
    }

    private EntityInterface initMMCEntity() {
        EntityInterface mmcEntity = DomainObjectFactory.getInstance().createEntity();
        mmcEntity.setName(mmcBean.getName());
        mmcEntity.setDescription(mmcBean.getDescription());
        mmcEntity.setCreatedDate(new Date());
        mmcEntity.addEntityGroupInterface(getMMCEntityGroup());

        TaggedValueInterface taggedValue = DomainObjectFactory.getInstance().createTaggedValue();
        taggedValue.setKey(MULTIMODELCATEGORY);
        taggedValue.setValue(MULTIMODELCATEGORY);
        mmcEntity.addTaggedValue(taggedValue);

        initAttributes(mmcEntity);

        mmcEntity = DynamicExtensionUtility.persistEntity(mmcEntity);
        EntityCache.getInstance().addEntityToCache(mmcEntity);

        return mmcEntity;
    }

    private void initAttributes(EntityInterface mmcEntity) {
        assert mmcBean != null;
        assert mmcBean.getMultiModelAttributes() != null;

        entityInputCatagorialAttributeMap = new HashMap<EntityInterface, Collection<InputCategorialAttribute>>();
        multiModelAttributes = new HashSet<MultiModelAttribute>();
        multiModelAttributeToSelectedAttributeIds = new HashMap<MultiModelAttribute, Collection<Long>>();

        Collection<MultiModelAttributeBean> mmaBeans = mmcBean.getMultiModelAttributes();
        for (MultiModelAttributeBean mmaBean : mmaBeans) {
            AttributeInterface deAttribute = DomainObjectFactory.getInstance().createStringAttribute();
            deAttribute.setName(mmaBean.getName());
            deAttribute.setDescription(mmaBean.getDescription());

            mmcEntity.addAttribute(deAttribute);

            MultiModelAttribute multiModelAttribute = new MultiModelAttributeImpl();
            multiModelAttribute.setVisible(mmaBean.isVisible());
            multiModelAttribute.setAttribute(deAttribute);

            Collection<Long> selectedAttributesIds = new ArrayList<Long>();
            for (AttributeInterface selectedAttribute : mmaBean.getSelectedAttributes()) {
                selectedAttributesIds.add(selectedAttribute.getId());

                InputCategorialAttribute inputCatagorialAttribute = new InputCategorialAttribute();
                StringBuffer displayName = new StringBuffer(mmaBean.getName()).append('_').append(
                                                                                                  selectedAttribute.getName());
                inputCatagorialAttribute.setDisplayName(displayName.toString());
                inputCatagorialAttribute.setDynamicExtAttribute(selectedAttribute);

                EntityInterface entity = selectedAttribute.getEntity();
                Collection<InputCategorialAttribute> inputCatagorialAttributes = entityInputCatagorialAttributeMap.get(entity);
                if (inputCatagorialAttributes == null) {
                    inputCatagorialAttributes = new ArrayList<InputCategorialAttribute>();
                    entityInputCatagorialAttributeMap.put(entity, inputCatagorialAttributes);
                }
                inputCatagorialAttributes.add(inputCatagorialAttribute);
            }
            multiModelAttributeToSelectedAttributeIds.put(multiModelAttribute, selectedAttributesIds);
            multiModelAttributes.add(multiModelAttribute);
        }
    }

    /**
     * This method retrieves the MMC_Entity_Group if exists otherwise creates a new one.
     * @return
     */
    private EntityGroupInterface getMMCEntityGroup() {
        EntityGroupInterface entityGroup = null;
        try {
            entityGroup = EntityManager.getInstance().getEntityGroupByName(MMC_ENTITY_GROUP_NAME);
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
            entityGroup.setShortName(MMC_ENTITY_GROUP_NAME);
            entityGroup.setName(MMC_ENTITY_GROUP_NAME);
            entityGroup.setLongName(MMC_ENTITY_GROUP_NAME);
            entityGroup.setDescription(MMC_ENTITY_GROUP_NAME);
            DynamicExtensionUtility.markMetadataEntityGroup(entityGroup);
            entityGroup = DynamicExtensionUtility.persistEntityGroup(entityGroup);
        }
        return entityGroup;
    }

    private Collection<InputCategory> createInputCategories() {
        assert entities != null;

        Collection<InputCategory> inputCategories = new ArrayList<InputCategory>();
        for (EntityInterface rootEntity : entities) {
            //Create InputCategorialClass
            InputCategorialClass rootClass = new InputCategorialClass();
            rootClass.setAttributeList(new ArrayList<InputCategorialAttribute>(
                    entityInputCatagorialAttributeMap.get(rootEntity)));
            rootClass.setChildren(processOutPaths(rootEntity));
            rootClass.setPathFromParent(-1);

            //Create InputCategory
            InputCategory inputCategory = new InputCategory();

            StringBuffer name = new StringBuffer(mmcBean.getName()).append('_').append(rootEntity.getName());
            inputCategory.setName(name.toString());

            StringBuffer description = new StringBuffer(mmcBean.getDescription()).append('_').append(
                                                                                                     rootEntity.getDescription());
            inputCategory.setDescription(description.toString());

            inputCategory.setRootCategorialClass(rootClass);
            inputCategory.setSubCategories(new ArrayList<InputCategory>());
            inputCategories.add(inputCategory);
        }

        return inputCategories;
    }

    /**
     * Recursive method instantiate Complete hierarchy for CategorialClass.
     */
    private InputCategorialClass createInputCategorialClass(IPath outPath) {
        EntityInterface targetEntity = outPath.getTargetEntity();

        InputCategorialClass inputCategorialClass = new InputCategorialClass();
        inputCategorialClass.setPathFromParent(outPath.getPathId());
        Collection<InputCategorialAttribute> catAttributes = entityInputCatagorialAttributeMap.get(targetEntity);
        ArrayList<InputCategorialAttribute> inputCatAttrs = new ArrayList<InputCategorialAttribute>(catAttributes);
        inputCategorialClass.setAttributeList(inputCatAttrs);
        inputCategorialClass.setChildren(processOutPaths(targetEntity));

        return inputCategorialClass;
    }

    private List<InputCategorialClass> processOutPaths(EntityInterface sourceEntity) {
        assert sourceEntityToPaths != null;

        List<InputCategorialClass> childrenICCs = new ArrayList<InputCategorialClass>();
        Collection<IPath> outPaths = sourceEntityToPaths.get(sourceEntity);
        if (outPaths != null) {
            for (IPath path : outPaths) {
                InputCategorialClass iccChild = createInputCategorialClass(path);
                childrenICCs.add(iccChild);
            }
        }
        return childrenICCs;
    }

    private Set<EntityInterface> initEntityPathMapAndGetTargetEntities() {
        assert entities != null;

        sourceEntityToPaths = new HashMap<EntityInterface, Collection<IPath>>();

        Set<EntityInterface> targetEntities = new HashSet<EntityInterface>();
        Collection<IPath> paths = mmcBean.getPaths();
        for (IPath path : paths) {
            EntityInterface source = path.getSourceEntity();
            EntityInterface target = path.getTargetEntity();
            targetEntities.add(target);

            Collection<IPath> associationPaths = sourceEntityToPaths.get(source);
            if (associationPaths == null) {
                associationPaths = new ArrayList<IPath>();
                sourceEntityToPaths.put(source, associationPaths);
            }
            associationPaths.add(path);
        }

        return targetEntities;
    }

    public static void main(String[] args) {
        MultiModelCategoryBean mmcBean = new MultiModelCategoryXmlParser().
        getMultiModelCategory("E:/Workspace/cab2b_MMC/software/cab2b/src/conf/MMC2.xml");
        MultiModelCategory mmCategory = new PersistMultiModelCategory().persistMMC(mmcBean);

        //save in database
        MultiModelCategoryOperations operations = new MultiModelCategoryOperations();
        operations.saveMultiModelCategory(mmCategory);

        //delete from database
        //MultiModelCategory mmCategory = operations.getMultiModelCategoryById(1L);
        //operations.deleteMultiModelCategory(mmCategory);

        System.out.println(mmCategory.getEntity().getName());
    }
}

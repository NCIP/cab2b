package edu.wustl.cab2b.server.multimodelcategory;

import static edu.wustl.cab2b.common.util.Constants.MMC_ENTITY_GROUP_NAME;
import static edu.wustl.cab2b.common.util.Constants.MULTIMODELCATEGORY;

import java.io.File;
import java.io.IOException;
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
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttributeImpl;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategoryImpl;
import edu.wustl.cab2b.common.multimodelcategory.bean.MultiModelAttributeBean;
import edu.wustl.cab2b.common.multimodelcategory.bean.MultiModelCategoryBean;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.category.InputCategorialAttribute;
import edu.wustl.cab2b.server.category.InputCategorialClass;
import edu.wustl.cab2b.server.category.InputCategory;
import edu.wustl.cab2b.server.category.PersistCategory;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.dbManager.DBUtil;

/**
 * This class takes in the MultiModelCategoryBean and generates the MultiModelCategogry out of it and finally
 * persists it into database. MultiModelCategoryBean is expected to get populated from UI where user/admin selects
 * or sets the appropriate values.
 * 
 * @author chetan_patil
 * @author rajesh_vyas
 * 
 */
public class PersistMultiModelCategory {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(PersistMultiModelCategory.class);

    private MultiModelCategoryBean mmcBean;

    private Set<EntityInterface> entities;

    private Map<EntityInterface, Collection<IPath>> sourceEntityToPaths;

    private Map<EntityInterface, Collection<InputCategorialAttribute>> entityInputCatagorialAttributeMap;

    private Map<Long, CategorialAttribute> attributeIDToCatAttr;

    private Map<MultiModelAttribute, Collection<Long>> multiModelAttributeToSelectedAttributeIds;

    private Set<MultiModelAttribute> multiModelAttributes;

    /**
     * Default constructor
     */
    public PersistMultiModelCategory() {
        attributeIDToCatAttr = new HashMap<Long, CategorialAttribute>();
        sourceEntityToPaths = new HashMap<EntityInterface, Collection<IPath>>();
        entityInputCatagorialAttributeMap = new HashMap<EntityInterface, Collection<InputCategorialAttribute>>();
        multiModelAttributes = new HashSet<MultiModelAttribute>();
        multiModelAttributeToSelectedAttributeIds = new HashMap<MultiModelAttribute, Collection<Long>>();
    }

    /**
     * This method assumes that MultiModelCategoryBean is well populated, so this class can create InputCategory
     * objects.
     * 
     * @param mmcBean
     * @return
     */
    public MultiModelCategory persistMMC(MultiModelCategoryBean mmcBean) {
        this.mmcBean = mmcBean;

        MultiModelCategory multiModelCategory = initialize();

        PersistCategory persistCategory = new PersistCategory();
        CategoryOperations categoryOperations = new CategoryOperations();

        Collection<InputCategory> inputCategories = createInputCategories();
        Collection<Category> categories = new ArrayList<Category>(inputCategories.size());
        for (InputCategory inCategory : inputCategories) {
            logger.debug("Saving category: " + inCategory);
            Category category = persistCategory.persistCategory(inCategory, null, true);
            category.setSystemGenerated(Boolean.TRUE);
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

    /**
     * This method adds categorial attributes form category saved into MultiModelAttributes. Basically, it maps the
     * attributes of categories saved to MultiModelAttribute.
     */
    private void addCategorialAttrsToMMA() {
        for (MultiModelAttribute multiModelAttr : multiModelAttributes) {
            for (Long attribute : multiModelAttributeToSelectedAttributeIds.get(multiModelAttr)) {
                multiModelAttr.addCategorialAttribute(attributeIDToCatAttr.get(attribute));
            }
        }
    }

    /**
     * This method is recursively called so that all children are processed. It generates map of
     * AttributeInterface->CategorialAttribute, so mmaToSelectedAI can be used, to derive MMA->CategorialAttribute
     * 
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

    /**
     * This method initializes and generates MultiModelCategory
     * 
     * @return
     */
    private MultiModelCategory initialize() {
        MultiModelCategory multiModelCategory = createMultiModelCategory();

        entities = new HashSet<EntityInterface>(entityInputCatagorialAttributeMap.keySet());
        Set<EntityInterface> targetEntities = initEntityPathMapAndGetTargetEntities();

        //After removing all targets, 'entities' will only contain root categories
        entities.removeAll(targetEntities);

        return multiModelCategory;
    }

    /**
     * This method creates MultiModelCategory
     * 
     * @return
     */
    private MultiModelCategory createMultiModelCategory() {
        EntityInterface mmcEntity = initMMCEntity();

        MultiModelCategory multiModelCategory = new MultiModelCategoryImpl();
        multiModelCategory.setApplicationGroup(mmcBean.getApplicationGroup());
        multiModelCategory.setEntity(mmcEntity);

        return multiModelCategory;
    }

    /**
     * This method creates DynamicExtension's entity for MultiModelCategory
     * 
     * @return
     */
    private EntityInterface initMMCEntity() {
        EntityInterface mmcEntity = DomainObjectFactory.getInstance().createEntity();
        mmcEntity.setName(mmcBean.getName());
        mmcEntity.setDescription(mmcBean.getDescription());
        mmcEntity.setCreatedDate(new Date());
        mmcEntity.addEntityGroupInterface(getMMCEntityGroup());

        DynamicExtensionUtility.addTaggedValue(mmcEntity, MULTIMODELCATEGORY, MULTIMODELCATEGORY);
        initAttributes(mmcEntity);

        mmcEntity = DynamicExtensionUtility.persistEntity(mmcEntity);
        EntityCache.getInstance().addEntityToCache(mmcEntity);

        return mmcEntity;
    }

    /**
     * This method initializes the attributes into MultiModelCategory's DE entity.
     * 
     * @param mmcEntity
     */
    private void initAttributes(EntityInterface mmcEntity) {
        assert mmcBean != null;
        assert mmcBean.getMultiModelAttributes() != null;

        Collection<MultiModelAttributeBean> mmaBeans = mmcBean.getMultiModelAttributes();
        for (MultiModelAttributeBean mmaBean : mmaBeans) {
            AttributeInterface deAttribute = MmcAttributeGenerator.getAttribute(mmaBean);
            deAttribute.setName(mmaBean.getName());
            deAttribute.setDescription(mmaBean.getDescription());

            mmcEntity.addAttribute(deAttribute);

            MultiModelAttribute multiModelAttribute = new MultiModelAttributeImpl();
            multiModelAttribute.setVisible(mmaBean.isVisible());
            multiModelAttribute.setAttribute(deAttribute);

            Collection<AttributeInterface> selectedMMCAttributes = mmaBean.getSelectedAttributes();
            Collection<Long> selectedAttributesIds = new ArrayList<Long>(selectedMMCAttributes.size());
            for (AttributeInterface selectedAttribute : selectedMMCAttributes) {
                selectedAttributesIds.add(selectedAttribute.getId());

                InputCategorialAttribute inputCatagorialAttribute = new InputCategorialAttribute();
                StringBuffer displayName =
                        new StringBuffer(mmaBean.getName()).append('_').append(selectedAttribute.getName());
                inputCatagorialAttribute.setDisplayName(displayName.toString());
                inputCatagorialAttribute.setDynamicExtAttribute(selectedAttribute);

                EntityInterface entity = selectedAttribute.getEntity();
                Collection<InputCategorialAttribute> inputCatagorialAttributes =
                        entityInputCatagorialAttributeMap.get(entity);
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
     * 
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

    /**
     * This method generates input categories which are needed for saving actual categories
     * 
     * @return
     */
    private Collection<InputCategory> createInputCategories() {
        assert entities != null;

        Collection<InputCategory> inputCategories = new ArrayList<InputCategory>(entities.size());
        for (EntityInterface rootEntity : entities) {
            //Create InputCategorialClass
            InputCategorialClass rootClass = new InputCategorialClass();
            rootClass.setAttributeList(new ArrayList<InputCategorialAttribute>(entityInputCatagorialAttributeMap
                .get(rootEntity)));
            rootClass.setChildren(processOutPaths(rootEntity));
            rootClass.setPathFromParent(-1);

            //Create InputCategory
            InputCategory inputCategory = new InputCategory();

            StringBuffer name = new StringBuffer(mmcBean.getName()).append('_').append(rootEntity.getName());
            inputCategory.setName(name.toString());

            StringBuffer description =
                    new StringBuffer(mmcBean.getDescription()).append('_').append(rootEntity.getDescription());
            inputCategory.setDescription(description.toString());

            inputCategory.setRootCategorialClass(rootClass);
            inputCategory.setSubCategories(new ArrayList<InputCategory>());
            inputCategories.add(inputCategory);
        }

        return inputCategories;
    }

    /**
     * This method recursively instantiate complete hierarchy for CategorialClass.
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

    /**
     * This method processes out going paths
     * 
     * @param sourceEntity
     * @return
     */
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

    /**
     * This method initializes Entity->Paths map and returns the set of target entities.
     * 
     * @return
     */
    private Set<EntityInterface> initEntityPathMapAndGetTargetEntities() {
        assert entities != null;

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

    /**
     * This method takes the path where where all the multi-model category XML files are stored and convert them to
     * MultiModelCategory objects to saves them into database.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            throw new java.lang.RuntimeException(
                    "Please specify the path to directory where multi model category XMLs are stored.");
        } else if (!(new File(args[0])).isDirectory()) {
            throw new IllegalArgumentException("The directory specified in invalid. Please correct the path.");
        }

        PathFinder.getInstance(DBUtil.getConnection());
        MultiModelCategoryOperations operations = new MultiModelCategoryOperations();

        //save in database
        File mmcDir = new File(args[0]);
        File[] mmcFiles = mmcDir.listFiles();
        MultiModelCategoryXmlParser parser = new MultiModelCategoryXmlParser();
        for (File mmcFile : mmcFiles) {
            try {
                MultiModelCategoryBean mmcBean = parser.getMultiModelCategory(mmcFile);
                MultiModelCategory mmCategory = new PersistMultiModelCategory().persistMMC(mmcBean);
                operations.saveMultiModelCategory(mmCategory);
            } catch (Exception e) {
                System.out.println("Cannot parse and load " + mmcFile.getAbsolutePath() + "\nRoot cause: "
                        + e.getMessage());
            }
        }

        //delete from database
        //MultiModelCategory mmCategory = operations.getMultiModelCategoryById(1L);
        //System.out.println(mmCategory.getEntity().getName());
        //operations.deleteMultiModelCategory(mmCategory);
    }
}

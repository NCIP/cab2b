package edu.wustl.cab2b.server.category.multimodelcategory;

import static edu.wustl.cab2b.common.util.Constants.CATEGORY_ENTITY_GROUP_NAME;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

    private Collection<EntityInterface> entities;

    private Collection<EntityInterface> targets;

    private Map<EntityInterface, Collection<IPath>> entityToPaths;

    private Map<EntityInterface, Collection<InputCategorialAttribute>> entityToCatAttributes;

    private Map<Long, CategorialAttribute> attributeIDToCatAttr;

    private Collection<MultiModelAttribute> multiModelAttrs;

    private MultiModelCategoryBean inputMultiModelCategoryBean;

    private HashMap<MultiModelAttribute, Collection<Long>> multiModelAttributeToSelectedAttributeIds;

    private EntityInterface multiModelCategoryEntity;

    private MultiModelCategoryImpl multiModelCategory;

    private Map<String, MultiModelAttributeImpl> attrNameToMultiModelAttribute;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(PersistMultiModelCategory.class);

    /*
     * THis assumes MultiModelCategoryBean is well populated, so this class can create InputCategory objects.
     */
    public MultiModelCategory persistMMC(MultiModelCategoryBean inputMultiModelCategoryBean) {
        this.inputMultiModelCategoryBean = inputMultiModelCategoryBean;
        initialize();
        attributeIDToCatAttr = new HashMap<Long, CategorialAttribute>();
        Collection<Category> categories = new ArrayList<Category>();
        PersistCategory persistCategory = new PersistCategory();
        for (InputCategory inCategory : createInputCategories()) {
            logger.debug("Saving category: " + inCategory);
            Category category = persistCategory.persistCategory(inCategory, null);
            mapAIToCatAttr(category.getRootClass());
            new CategoryOperations().saveCategory(category);
            categories.add(category);
            logger.debug("Saved category id: " + category.getDeEntityId());
        }
        addCategorialAttrsToMMA();
        multiModelCategory.setCategories(categories);
        multiModelCategory.setMultiModelAttributes(multiModelAttrs);

        return multiModelCategory;
    }

    private void addCategorialAttrsToMMA() {
        for (MultiModelAttribute multiModelAttr : multiModelAttrs) {
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

    private void initialize() {
        initMultiModelCategory();
        entities = entityToCatAttributes.keySet();
        initEntityPaths();

        //After removing all targets, 'entities' will only contain root categories
        entities.removeAll(targets);
        createInputCategories();
    }

    private void initMultiModelCategory() {
        multiModelCategoryEntity = DomainObjectFactory.getInstance().createEntity();
        multiModelCategoryEntity.setName(inputMultiModelCategoryBean.getName());
        multiModelCategoryEntity.setDescription(inputMultiModelCategoryBean.getDescription());
        multiModelCategoryEntity.addEntityGroupInterface(getEntityGrpInterface());

        initAttributes();

        multiModelCategoryEntity = DynamicExtensionUtility.persistEntity(multiModelCategoryEntity);
        EntityCache.getInstance().addEntityToCache(multiModelCategoryEntity);
        multiModelCategory = new MultiModelCategoryImpl();
        multiModelCategory.setApplicationGroup(inputMultiModelCategoryBean.getApplicationGroup());
        multiModelCategory.setMmultiModelCategoryDeEntityId(multiModelCategoryEntity.getId());

        for (AttributeInterface mmAttribute : multiModelCategoryEntity.getAllAttributes()) {
            attrNameToMultiModelAttribute.get(mmAttribute.getName()).setAttributeId(mmAttribute.getId());
        }
    }

    private EntityGroupInterface getEntityGrpInterface() {
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
            //entityGroup.addTaggedValue();
        }
        return entityGroup;
    }

    private Collection<InputCategory> createInputCategories() {
        assert entities != null;

        Collection<InputCategory> inputCategories = new ArrayList<InputCategory>();
        for (EntityInterface rootEntity : entities) {
            //Create ICC
            InputCategorialClass rootClass = new InputCategorialClass();
            rootClass.setAttributeList(new ArrayList<InputCategorialAttribute>(
                    entityToCatAttributes.get(rootEntity)));
            rootClass.setChildren(processOutPaths(rootEntity));
            rootClass.setPathFromParent(-1);
            //Create InputCategory
            InputCategory inputCategory = new InputCategory();
            String name = inputMultiModelCategoryBean.getName() + "_" + rootEntity.getName();
            inputCategory.setName(name);
            String desc = inputMultiModelCategoryBean.getDescription() + "_" + rootEntity.getDescription();
            inputCategory.setDescription(desc);
            inputCategory.setRootCategorialClass(rootClass);
            inputCategory.setSubCategories(new ArrayList<InputCategory>());
            inputCategories.add(inputCategory);
        }

        return inputCategories;
    }

    //recursive method instantiate Complete hierarchy for CategorialClass.
    private InputCategorialClass createInputCategorialClass(IPath outPath) {
        EntityInterface targetEntity = outPath.getTargetEntity();
        InputCategorialClass icc = new InputCategorialClass();
        icc.setPathFromParent(outPath.getPathId());
        //add ICA's
        icc.setAttributeList(new ArrayList<InputCategorialAttribute>(entityToCatAttributes.get(targetEntity)));
        //process out paths
        icc.setChildren(processOutPaths(targetEntity));
        return icc;
    }

    private List<InputCategorialClass> processOutPaths(EntityInterface sourceEntity) {
        assert entityToPaths != null;

        Collection<IPath> outPaths = entityToPaths.get(sourceEntity);
        List<InputCategorialClass> childrenICCs = new ArrayList<InputCategorialClass>();
        for (IPath path : outPaths) {
            InputCategorialClass iccChild = createInputCategorialClass(path);
            childrenICCs.add(iccChild);
        }
        return childrenICCs;
    }

    private void initAttributes() {
        assert inputMultiModelCategoryBean != null;
        assert inputMultiModelCategoryBean.getMultiModelAttributes() != null;

        entityToCatAttributes = new HashMap<EntityInterface, Collection<InputCategorialAttribute>>();
        //Collection<MultiModelAttribute> mmAttributes = new ArrayList<MultiModelAttribute>();
        multiModelAttrs = new HashSet<MultiModelAttribute>();
        multiModelAttributeToSelectedAttributeIds = new HashMap<MultiModelAttribute, Collection<Long>>();

        attrNameToMultiModelAttribute = new HashMap<String, MultiModelAttributeImpl>();
        for (MultiModelAttributeBean attributeBean : inputMultiModelCategoryBean.getMultiModelAttributes()) {
            Collection<Long> attributes = new ArrayList<Long>();
            MultiModelAttributeImpl mma = new MultiModelAttributeImpl();
            mma.setVisible(attributeBean.isVisible());
            AttributeInterface mmAttributeInterface = DomainObjectFactory.getInstance().createStringAttribute();
            mmAttributeInterface.setName(attributeBean.getName());
            mmAttributeInterface.setDescription(attributeBean.getDescription());
            mma.setAttribute(mmAttributeInterface);
            multiModelCategoryEntity.addAttribute(mmAttributeInterface);
            attrNameToMultiModelAttribute.put(attributeBean.getName(), mma);
            for (AttributeInterface attribute : attributeBean.getSelectedAttributes()) {
                attributes.add(attribute.getId());
                EntityInterface entity = attribute.getEntity();
                InputCategorialAttribute inputCatAttribute = new InputCategorialAttribute();
                String displayName = attributeBean.getName() + "_" + attribute.getName();
                inputCatAttribute.setDisplayName(displayName);
                inputCatAttribute.setDynamicExtAttribute(attribute);
                Collection<InputCategorialAttribute> entityAttrs = entityToCatAttributes.get(entity);
                if (entityAttrs == null) {
                    entityAttrs = new ArrayList<InputCategorialAttribute>();
                    entityToCatAttributes.put(entity, entityAttrs);
                }
                entityAttrs.add(inputCatAttribute);
            }
            multiModelAttributeToSelectedAttributeIds.put(mma, attributes);
            multiModelAttrs.add(mma);
        }
    }

    private void initEntityPaths() {
        assert entities != null;

        entityToPaths = new HashMap<EntityInterface, Collection<IPath>>();
        for (EntityInterface entity : entities) {
            Collection<IPath> assocPaths = new ArrayList<IPath>();
            entityToPaths.put(entity, assocPaths);
        }
        targets = new ArrayList<EntityInterface>();
        Collection<IPath> paths = inputMultiModelCategoryBean.getPaths();
        for (IPath path : paths) {
            EntityInterface source = path.getSourceEntity();
            EntityInterface target = path.getTargetEntity();
            targets.add(target);
            Collection<IPath> assocPaths = entityToPaths.get(source);
            if (assocPaths == null) {
                assocPaths = new ArrayList<IPath>();
                entityToPaths.put(source, assocPaths);
            }
            assocPaths.add(path);
        }
    }

    public static void main(String[] args) {
        MultiModelCategoryXmlParser xmlParser = new MultiModelCategoryXmlParser();
        MultiModelCategoryBean mmcBean = xmlParser.getMultiModelCategory(args[0]);
        PersistMultiModelCategory persistMultiModelCategory = new PersistMultiModelCategory();

        //save in database
        MultiModelCategoryOperations operations = new MultiModelCategoryOperations();
        //operations.saveCategory(persistMultiModelCategory.persistMMC(mmcBean));

        List<MultiModelCategory> categoryImpl = operations.getAllMultiModelCategoriesCategories();
        System.out.println(categoryImpl);
    }
}

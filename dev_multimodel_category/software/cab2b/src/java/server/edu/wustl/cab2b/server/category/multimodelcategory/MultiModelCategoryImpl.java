package edu.wustl.cab2b.server.category.multimodelcategory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * 
 * @author rajesh_vyas
 * @hibernate.class table="CAB2B_MMC"
 * @hibernate.cache usage="read-write"
 */
public class MultiModelCategoryImpl implements MultiModelCategory {

    private Long id;

    private Long multiModelCategoryDeEntityId;

    private EntityInterface multiModelEntity;

    private ModelGroupInterface appGroup;

    private Long modelGrpId;

    private String categoryIds;

    private Collection<MultiModelAttribute> multiModelAttributes;

    private Collection<Category> categories;

    public MultiModelCategoryImpl() {
        multiModelAttributes = new HashSet<MultiModelAttribute>();
        categories = new HashSet<Category>();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addMultiModelAttribute(MultiModelAttribute multiModelAttribute) {
        multiModelAttributes.add(multiModelAttribute);
    }

    public ModelGroupInterface getApplicationGroup() {
        return appGroup;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public Category getCategory(int index) {
        return ((List<Category>) categories).get(index);
    }

    public EntityInterface getEntity() {
        return multiModelEntity;
    }

    public MultiModelAttribute getMultiModelAttribute(int index) {
        return ((List<MultiModelAttribute>) multiModelAttributes).get(index);
    }

    /**
     * @hibernate.set name="multiModelAttributes" cascade="all-delete-orphan" inverse="false" 
     *                lazy="false" table="CAB2B_MMA" 
     * @hibernate.collection-key column="MMC_ID"
     * @hibernate.collection-one-to-many
     *      class="edu.wustl.cab2b.server.category.multimodelcategory.MultiModelAttributeImpl" 
     */
    public Collection<MultiModelAttribute> getMultiModelAttributes() {
        return multiModelAttributes;
    }

    public void setApplicationGroup(ModelGroupInterface applicationGroup) {
        appGroup = applicationGroup;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories.addAll(categories);
    }

    public void setEntity(EntityInterface Entity) {
        multiModelEntity = Entity;
    }

    public void setMultiModelAttributes(Collection<MultiModelAttribute> multiModelAttributes) {
        this.multiModelAttributes.addAll(multiModelAttributes);
    }

    /**
     * @hibernate.property  name="multiModelCategoryDeEntityId" column="DE_ENTITY_ID"
     * type="long" length="30" not-null="false"
     * @return
     */
    public Long getMmultiModelCategoryDeEntityId() {
        return multiModelCategoryDeEntityId;
    }

    public void setMmultiModelCategoryDeEntityId(Long mmcDeEntityId) {
        this.multiModelCategoryDeEntityId = mmcDeEntityId;
        this.multiModelEntity = EntityCache.getInstance().getEntityById(mmcDeEntityId);
    }

    /**
     * @hibernate.property column="MODEL_GRP_ID" type="long" not-null="false"
     * @return
     */
    public Long getModelGrpId() {
        return appGroup.getModelGroupId();
    }

    public void setModelGrpId(Long modelGrpId) {
        this.modelGrpId = modelGrpId;
        ModelGroupOperations modelGrpOpr = new ModelGroupOperations();
        appGroup = modelGrpOpr.getModelGroupById(modelGrpId);
    }

    /**
     * @hibernate.id name="id" column="MMC_ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="ID_SEQ"
     * @return
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @hibernate.property name="categoryIds" column="CATEGORY_IDS" type="string" length="254"  not-null="true"
     * @return
     */
    private String getCategoryIds() {
        StringBuffer delimIds = new StringBuffer();
        for (Category ca : categories) {
            delimIds.append(ca.getId()).append("_");
        }

        categoryIds = delimIds.toString();
        return categoryIds;
    }

    private void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;

        //Populate only while retrieving from database else leave it as it is. 
        if (categories.isEmpty()) {
            String[] categoryIdsStrs = categoryIds.split("_");

            for (String catString : categoryIdsStrs) {
                CategoryCache categoryCache = CategoryCache.getInstance();
                Category category = categoryCache.getCategoryById(Long.parseLong(catString));
                categories.add(category);
            }
        }
    }
}

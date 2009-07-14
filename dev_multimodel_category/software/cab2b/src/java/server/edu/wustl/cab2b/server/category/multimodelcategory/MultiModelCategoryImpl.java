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

    private EntityInterface entity;

    private ModelGroupInterface applicationGroup;

    private Collection<MultiModelAttribute> multiModelAttributes;

    private Collection<Category> categories;

    public MultiModelCategoryImpl() {
        multiModelAttributes = new HashSet<MultiModelAttribute>();
        categories = new HashSet<Category>();
    }

    /**
     * @hibernate.id name="id" column="ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="ID_SEQ"
     * @return
     */
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public EntityInterface getEntity() {
        return entity;
    }

    public void setEntity(EntityInterface Entity) {
        entity = Entity;
    }

    public ModelGroupInterface getApplicationGroup() {
        return applicationGroup;
    }

    public void setApplicationGroup(ModelGroupInterface applicationGroup) {
        this.applicationGroup = applicationGroup;
    }

    public void addMultiModelAttribute(MultiModelAttribute multiModelAttribute) {
        multiModelAttributes.add(multiModelAttribute);
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

    public void setMultiModelAttributes(Collection<MultiModelAttribute> multiModelAttributes) {
        this.multiModelAttributes.addAll(multiModelAttributes);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public Category getCategory(int index) {
        return ((List<Category>) categories).get(index);
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories.addAll(categories);
    }

    /**
     * @hibernate.property  name="entityId" column="ENTITY_ID"
     * type="long" length="30" not-null="false"
     * @return
     */
    private Long getEntityId() {
        return entity.getId();
    }

    private void setEntityId(Long entityId) {
        this.entity = EntityCache.getInstance().getEntityById(entityId);
    }

    /**
     * @hibernate.property name="applicationGroupId" column="APP_GROUP_ID" type="long" not-null="false"
     * @return
     */
    private Long getApplicationGroupId() {
        return applicationGroup.getModelGroupId();
    }

    private void setApplicationGroupId(Long applicationGroupId) {
        applicationGroup = new ModelGroupOperations().getModelGroupById(applicationGroupId);
    }

    /**
     * @hibernate.property name="categoryIds" column="CATEGORY_IDS" type="string" length="254"  not-null="true"
     * @return
     */
    private String getCategoryIds() {
        StringBuffer categoryIds = new StringBuffer();
        int index = categories.size();
        for (Category category : categories) {
            categoryIds.append(category.getId());
            if (--index > 0) {
                categoryIds.append('_');
            }
        }

        return categoryIds.toString();
    }

    /**
     * Hibernate uses this method to set the category ids
     * @param categoryIDs
     */
    private void setCategoryIds(String categoryIDs) {
        if (categories.isEmpty()) {
            String[] tokenIDs = categoryIDs.split("_");

            CategoryCache categoryCache = CategoryCache.getInstance();
            for (String categoryId : tokenIDs) {
                Category category = categoryCache.getCategoryById(Long.parseLong(categoryId));
                categories.add(category);
            }
        }
    }

}

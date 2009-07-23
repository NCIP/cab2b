package edu.wustl.cab2b.common.multimodelcategory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * 
 * @author rajesh_vyas
 * @hibernate.class table="CAB2B_MMC"
 * @hibernate.cache usage="read-write"
 */
public class MultiModelCategoryImpl implements MultiModelCategory {

    private static final long serialVersionUID = -8582301416038664078L;

    private Long id;

    private EntityInterface entity;

    private ModelGroupInterface applicationGroup;

    private Collection<MultiModelAttribute> multiModelAttributes;

    private Collection<Category> categories;

    private String categoryIds;

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

    /**
     * @hibernate
     * @hibernate.id name="applicationGroup" column="MODEL_GROUP_ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="ID_SEQ"
     * @return
     */
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
     * @hibernate.property name="categoryIds" column="CATEGORY_IDS" type="string" length="254"  not-null="true"
     * @return
     */
    public String getCategoryIds() {
        if (!categories.isEmpty()) {
            StringBuffer categoryIds = new StringBuffer();
            int index = categories.size();
            for (Category category : categories) {
                categoryIds.append(category.getId());
                if (--index > 0) {
                    categoryIds.append('_');
                }
            }
            this.categoryIds = categoryIds.toString();
        }

        return this.categoryIds;
    }

    /**
     * Hibernate uses this method to set the category ids
     * @param categoryIDs
     */
    public void setCategoryIds(String categoryIDs) {
        this.categoryIds = categoryIDs;
    }

    @Override
    public String toString() {
        return entity.getName();
    }

}

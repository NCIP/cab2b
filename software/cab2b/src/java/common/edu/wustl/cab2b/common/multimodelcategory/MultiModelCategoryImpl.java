/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.multimodelcategory;

import java.util.Collection;
import java.util.HashSet;

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
     * @return
     */
    public ModelGroupInterface getApplicationGroup() {
        return applicationGroup;
    }

    public void setApplicationGroup(ModelGroupInterface applicationGroup) {
        this.applicationGroup = applicationGroup;
    }

    public void addMultiModelAttribute(MultiModelAttribute multiModelAttribute) {
        getMultiModelAttributes().add(multiModelAttribute);
    }

    /**
     * @hibernate.set name="multiModelAttributes" cascade="all-delete-orphan" inverse="false" 
     *                lazy="false" table="CAB2B_MMA" 
     * @hibernate.collection-key column="MMC_ID"
     * @hibernate.collection-one-to-many
     *      class="edu.wustl.cab2b.server.category.multimodelcategory.MultiModelAttributeImpl" 
     */
    public Collection<MultiModelAttribute> getMultiModelAttributes() {
        if (multiModelAttributes == null) {
            multiModelAttributes = new HashSet<MultiModelAttribute>();
        }
        return multiModelAttributes;
    }

    public void setMultiModelAttributes(Collection<MultiModelAttribute> multiModelAttributes) {
        this.multiModelAttributes = multiModelAttributes;
    }

    public void addCategory(Category category) {
        getCategories().add(category);
    }

    public Collection<Category> getCategories() {
        if (categories == null) {
            categories = new HashSet<Category>();
        }
        return categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    /**
     * @hibernate.property name="categoryIds" column="CATEGORY_IDS" type="string" length="254"  not-null="true"
     * @return
     */
    public String getCategoryIds() {
        if (!getCategories().isEmpty()) {
            StringBuffer categoryIds = new StringBuffer();
            int index = getCategories().size();
            for (Category category : getCategories()) {
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

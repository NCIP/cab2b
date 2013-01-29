/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.category.AbstractCategory;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author 
 *
 * @hibernate.joined-subclass table="DATA_CATEGORY"
 * @hibernate.joined-subclass-key column="ID" 
 */
public class DataCategory extends AbstractCategory<DataCategorialClass, DataCategory> implements Serializable {

    private String name;
    
    private String description;
    
    
    public DataCategory() {

    }

    /**
     * This constructor is used to create a DataCategory Object From Category Object.
     * It copies all the values from category object to new datacategory object.
     * @param category
     */
    public DataCategory(Category category) {

        DataCategorialClass dataCategorialClass = new DataCategorialClass(category.getRootClass());
        dataCategorialClass.setCategory(this);
        this.setRootClass(dataCategorialClass);
        Set<DataCategory> subDataCategorySet = new HashSet<DataCategory>();
        Set<Category> subCategorySet = category.getSubCategories();
        for (Category subCategory : subCategorySet) {
            DataCategory newSubDataCategory = new DataCategory(subCategory);
            newSubDataCategory.setParentCategory(this);
            subDataCategorySet.add(newSubDataCategory);
        }
        this.setSubCategories(subDataCategorySet);

    }

    /**
     * This method returns the set of all the entityInterface object whose attributes are there in the
     * CategorialAttribute collection of this datacategory. 
     * @return 
     */
    public void getEntitySet(Set<EntityInterface> entitySet) {
        DataCategorialClass dataCategorialClass = this.getRootClass();
        EntityInterface entity = dataCategorialClass.getCategorialClassEntity();
        entitySet.add(entity);
        Set<DataCategorialClass> childrenCategorialCLass = dataCategorialClass.getChildren();
        for (DataCategorialClass childCategorialClass : childrenCategorialCLass) {
            entity = childCategorialClass.getCategorialClassEntity();
            entitySet.add(entity);

        }

        return;
    }
    
    
    
    
    /**
     * @return Returns the description.
     * @hibernate.property name="description" column="description" type="string" length="255" unsaved-value="null"  
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the name.
     * @hibernate.property name="name" column="name" type="string" length="255"  unique="true"
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns set of all the attributeInterface in the DataCategory.
     */
    public Set<DataCategorialAttribute> getDataCategorialAttributeCollection()
    {
        Set<DataCategorialAttribute> attributeCollection = new HashSet<DataCategorialAttribute>();
        DataCategorialClass rootCategorialClass = this.getRootClass();
        Set<DataCategorialAttribute> attributeSet =  rootCategorialClass.getCategorialAttributeCollection();
        attributeCollection.addAll(attributeSet);
        attributeSet.clear();
        Set<DataCategorialClass> childrenCategorialCLass = rootCategorialClass.getChildren();
        for (DataCategorialClass childCategorialClass : childrenCategorialCLass) {
            attributeSet = childCategorialClass.getCategorialAttributeCollection();
            attributeCollection.addAll(attributeSet);
            attributeSet.clear();
          }

        return attributeCollection;
    }
    
    /**
     * @return the rootClass
     * 
     * @hibernate.many-to-one column="ROOT_CLASS_ID" class="edu.wustl.cab2b.common.category.DataCategorialClass" unique="true" cascade="all" lazy="false"
     */
    public DataCategorialClass getRootClass()
    {
        return rootClass;
    }
}

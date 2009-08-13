/**
 * 
 */
package edu.wustl.cab2b.common.multimodelcategory;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author chetan_patil
 *
 */
public interface MultiModelCategory {
    
    EntityInterface getEntity();
    
    void setEntity(EntityInterface Entity);
    
    Collection<MultiModelAttribute> getMultiModelAttributes();
    
    void setMultiModelAttributes(Collection<MultiModelAttribute> multiModelAttributes);
    
    MultiModelAttribute getMultiModelAttribute(int index);
    
    void addMultiModelAttribute(MultiModelAttribute multiModelAttribute);
    
    ModelGroupInterface getApplicationGroup();
    
    void setApplicationGroup(ModelGroupInterface applicationGroup);
    
    Collection<Category> getCategories();
    
    Category getCategory(int index);
    
    void setCategories(Collection<Category> categories);
    
    void addCategory(Category category);

}

/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.multimodelcategory;

import java.io.Serializable;
import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author chetan_patil
 *
 */
public interface MultiModelCategory extends Serializable {

    Long getId();

    EntityInterface getEntity();

    void setEntity(EntityInterface Entity);

    Collection<MultiModelAttribute> getMultiModelAttributes();

    void setMultiModelAttributes(Collection<MultiModelAttribute> multiModelAttributes);

    void addMultiModelAttribute(MultiModelAttribute multiModelAttribute);

    ModelGroupInterface getApplicationGroup();

    void setApplicationGroup(ModelGroupInterface applicationGroup);

    Collection<Category> getCategories();

    void setCategories(Collection<Category> categories);

    void addCategory(Category category);

}

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

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;

/**
 * @author chetan_patil
 *
 */
public interface MultiModelAttribute extends Serializable {
    
    Long getId();

    AttributeInterface getAttribute();

    void setAttribute(AttributeInterface attribute);

    Collection<CategorialAttribute> getCategorialAttributes();

    void setCategorialAttributes(Collection<CategorialAttribute> categorialAttributes);

    void addCategorialAttribute(CategorialAttribute categorialAttribute);

    Boolean isVisible();

    void setVisible(Boolean isVisible);

}

/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.multimodelcategory.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author chetan_patil
 *
 */
public class MultiModelAttributeBean implements Serializable {
    private static final long serialVersionUID = 1102217842111447910L;

    private String name;

    private String description;

    private Collection<AttributeInterface> selectedAttributes = new ArrayList<AttributeInterface>();

    private boolean isVisible = true;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the selectedAttributes
     */
    public Collection<AttributeInterface> getSelectedAttributes() {
        return selectedAttributes;
    }

    public AttributeInterface getSelectedAttribute(int index) {
        return ((List<AttributeInterface>) selectedAttributes).get(index);
    }

    /**
     * @param selectedAttributes the selectedAttributes to set
     */
    public void setSelectedAttributes(Collection<AttributeInterface> selectedAttributes) {
        this.selectedAttributes = selectedAttributes;
    }

    /**
     * This method adds the given attribute to the collection of the selected attributes
     * @param attribute
     */
    public void addSelectedAttribute(AttributeInterface attribute) {
        this.selectedAttributes.add(attribute);
    }

    /**
     * @return the isVisible
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @param isVisible the isVisible to set
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

}

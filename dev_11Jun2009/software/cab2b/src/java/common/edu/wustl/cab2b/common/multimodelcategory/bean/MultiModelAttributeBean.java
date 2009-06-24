/**
 * 
 */
package edu.wustl.cab2b.common.multimodelcategory.bean;

import java.util.ArrayList;
import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author chetan_patil
 *
 */
public class MultiModelAttributeBean {
    
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

    /**
     * @param selectedAttributes the selectedAttributes to set
     */
    public void setSelectedAttributes(Collection<AttributeInterface> selectedAttributes) {
        this.selectedAttributes = selectedAttributes;
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

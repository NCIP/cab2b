/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.category;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * This is private class to this package.
 * It is used to represents the category XML's Attribute tag.
 * @author Chandrakant Talele
 */
public class InputCategorialAttribute {
    /**
     * Display name to use in case of repetition of names
     */
    private String displayName;

    /**
     * Dynamic Extension Attribute.
     */
    private AttributeInterface dynamicExtAttribute;

    /**
     * @return Returns the displayName.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName The displayName to set.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return Returns the dynamicExtAttributet.
     */
    public AttributeInterface getDynamicExtAttribute() {
        return dynamicExtAttribute;
    }

    /**
     * @param dynamicExtAttribute The dynamicExtAttributet to set.
     */
    public void setDynamicExtAttribute(AttributeInterface dynamicExtAttribute) {
        this.dynamicExtAttribute = dynamicExtAttribute;
    }
}

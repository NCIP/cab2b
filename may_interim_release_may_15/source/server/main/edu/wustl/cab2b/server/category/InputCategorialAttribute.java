package edu.wustl.cab2b.server.category;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * This is private class to this package.
 * It is used to reprents the category XML's Attribute tag.
 * @author Chandrakant Talele
 */
class InputCategorialAttribute {
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
     * @param dynamicExtAttributet The dynamicExtAttributet to set.
     */
    public void setDynamicExtAttribute(AttributeInterface dynamicExtAttribute) {
        this.dynamicExtAttribute = dynamicExtAttribute;
    }
}

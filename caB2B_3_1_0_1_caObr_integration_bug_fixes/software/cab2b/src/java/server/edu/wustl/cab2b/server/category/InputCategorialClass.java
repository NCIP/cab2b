package edu.wustl.cab2b.server.category;

import java.util.List;

/**
 * This is private class to this package.
 * It is used to reprents the category XML's CategorialClass tag.
 * @author Chandrakant Talele
 */
public class InputCategorialClass {
    /**
     * All attributes belonging to this the InputCategorialClass.
     */
    private List<InputCategorialAttribute> attributeList;

    /**
     * Path from parent InputCategorialClass to this InputCategorialClass
     */
    private long pathFromParent;

    /**
     * All the childrens of this InputCategorialClass.
     */
    private List<InputCategorialClass> children;

    /**
     * @return Returns the attributeList.
     */
    public List<InputCategorialAttribute> getAttributeList() {
        return attributeList;
    }

    /**
     * @param attributeList The attributeList to set.
     */
    public void setAttributeList(List<InputCategorialAttribute> attributeList) {
        this.attributeList = attributeList;
    }

    /**
     * @return Returns the children.
     */
    public List<InputCategorialClass> getChildren() {
        return children;
    }

    /**
     * @param children The children to set.
     */
    public void setChildren(List<InputCategorialClass> children) {
        this.children = children;
    }

    /**
     * @return Returns the pathFromParent.
     */
    public long getPathFromParent() {
        return pathFromParent;
    }

    /**
     * @param pathFromParent The pathFromParent to set.
     */
    public void setPathFromParent(long pathFromParent) {
        this.pathFromParent = pathFromParent;
    }
}
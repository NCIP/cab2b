/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.IOutputEntity;

/**
 * @author prafull_kadam imeplementation class for IOutputEntity.
 */
public class OutputEntity extends QueryEntity implements IOutputEntity {

    private static final long serialVersionUID = -823732241107299703L;

    private List<AttributeInterface> selectedAttributes = new ArrayList<AttributeInterface>();

    /**
     * To instanciate OutputEntity object.
     * 
     * @param entityInterface The Dynamic Extension entity reference associated
     *            with this object.
     */
    public OutputEntity(EntityInterface entityInterface) {
        super(entityInterface);
    }

    /**
     * @return List of Dynamic Extenstion attributes that will be shown in the
     *         Output tree.
     * @see edu.wustl.common.querysuite.queryobject.IOutputEntity#getSelectedAttributes()
     */
    public List<AttributeInterface> getSelectedAttributes() {
        return selectedAttributes;
    }

    /**
     * @param selectedAttributesIndices the attribute list to be set.
     * @see edu.wustl.common.querysuite.queryobject.IOutputEntity#setSelectedAttributes(java.util.List)
     */
    public void setSelectedAttributes(List<AttributeInterface> selectedAttributesIndices) {
        this.selectedAttributes = selectedAttributesIndices;
    }

    /**
     * To check whether two objects are equal.
     * 
     * @param obj reference to the object to be checked for equality.
     * @return true if entityInterface & attributes of object are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && this.getClass() == obj.getClass()) {
            OutputEntity theObj = (OutputEntity) obj;
            Set<AttributeInterface> attributeSet = new HashSet<AttributeInterface>(selectedAttributes);
            Set<AttributeInterface> theAttributeSet = new HashSet<AttributeInterface>(theObj.selectedAttributes);
            return attributeSet.equals(theAttributeSet) && super.equals(obj);
        }
        return false;
    }

    /**
     * To get the HashCode for the object. It will be calculated based on
     * entityInterface.
     * 
     * @return The hash code value for the object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // TODO check this.
        return super.hashCode();
    }

    /**
     * @return String representation of this object
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[OutputEntity: " + super.toString() + "]";
    }

}

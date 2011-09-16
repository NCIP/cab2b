package edu.common.dynamicextensions.entitymanager;

import java.io.Serializable;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;

/**
 * This class represents a meatadata of the entity records.
 * The class contains the list of all the attributes and associations for which records are obtained.
 * 
 * @author Rahul Ner
 * @author Vishvesh Mulay
 */
public class EntityRecordMetadata implements Serializable {

    /**
     * Serial Version Unique Identifier
     */
    private static final long serialVersionUID = -552600540977483821L;

    /**
     * List of attributes and associations
     */
    private List<? extends AbstractAttributeInterface> attributeList;

    /**
     * @return List<AbstractAttributeInterface> List of attributes  and associations
     */
    public List<? extends AbstractAttributeInterface> getAttributeList() {
        return attributeList;
    }

    /**
     * @param attributeList list to set.
     */
    public void setAttributeList(List<? extends AbstractAttributeInterface> attributeList) {
        this.attributeList = attributeList;
    }

}

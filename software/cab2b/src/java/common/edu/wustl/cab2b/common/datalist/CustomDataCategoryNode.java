/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.datalist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * 
 * @author Rahul Ner
 */
public class CustomDataCategoryNode implements Serializable {

    /**
     * Serial Version Unique Identifier
     */
    private static final long serialVersionUID = -552600540977483821L;

    private List<AttributeInterface> attributeList;

    private Map<AssociationInterface, CustomDataCategoryNode> associationDetails;

    public CustomDataCategoryNode() {
        attributeList = new ArrayList<AttributeInterface>();
        associationDetails = new HashMap<AssociationInterface, CustomDataCategoryNode>();
    }

    public void addAssociation(AssociationInterface association, CustomDataCategoryNode associationMetadata) {
        associationDetails.put(association, associationMetadata);
    }

    public void addAttribute(AttributeInterface attribute) {
        attributeList.add(attribute);
    }

    public List<AttributeInterface> getAttributeList() {
        return attributeList;
    }

    public Set<AssociationInterface> getAssociationList() {
        return associationDetails.keySet();
    }

    public CustomDataCategoryNode getAssociationDetails(AssociationInterface association) {
        return associationDetails.get(association);
    }

}

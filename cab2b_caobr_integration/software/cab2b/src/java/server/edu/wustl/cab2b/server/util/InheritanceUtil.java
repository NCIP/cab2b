/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.common.util.Constants.CONNECTOR;

import java.util.Arrays;
import java.util.Collections;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;

/**
 * @author Chandrakant Talele
 */
public class InheritanceUtil {
    /**
     * Checks whether passed attribute/association is inheriated.
     * @param abstractAttribute
     *            Attribute/Association to check.
     * @return TRUE if it is inherited else returns FALSE
     */
    public static boolean isInherited(
                                      AbstractAttributeInterface abstractAttribute) {
        
        return Utility.isInherited(abstractAttribute);
    }

    /**
     * Returns actual attribute if passed attribute is a derieved one. Else
     * returns the passed attribute
     * @param attribute
     *            Attribute for which actual attribute is expected.
     * @return The actual attribute
     */
    public static AttributeInterface getActualAttribute(
                                                        AttributeInterface attribute) {
        if (!isInherited(attribute)) {
            return attribute;
        }
        EntityInterface parent = attribute.getEntity().getParentEntity();
        String attributeName = attribute.getName();
        while (true) {
            for (AttributeInterface attributeFromParent : parent.getAttributeCollection()) {
                if (attributeName.equals(attributeFromParent.getName())) {
                    if (isInherited(attributeFromParent)) {
                        parent = parent.getParentEntity();
                        break;
                    } else {
                        return attributeFromParent;
                    }
                }
            }
        }
    }

    /**
     * Returns actual association if passed association is a derieved one. Else
     * returns the passed association
     * @param association
     *            Attribute for which actual association is expected.
     * @return The actual association
     */
    public static AssociationInterface getActualAassociation(
                                                             AssociationInterface association) {
        if (!isInherited(association)) {
            return association;
        }
        String originalAssociationPointer = "";
        for (TaggedValueInterface tag : association.getTaggedValueCollection()) {
            if (tag.getKey().equals(Constants.ORIGINAL_ASSOCIATION_POINTER)) {
                originalAssociationPointer = tag.getValue();
                break;
            }
        }
        if (originalAssociationPointer.equals("")) {
            throw new RuntimeException(
                    "Inconsistent database state !!! Found one inherited association without, tagged value having key as Constants.ORIGINAL_ASSOCIATION_POINTER");
        }

        if (association.getIsSystemGenerated()) {
            originalAssociationPointer = reverse(originalAssociationPointer);
        }
        AssociationInterface actualAssociation = EntityCache.getInstance().getAssociationByUniqueStringIdentifier(
                                                                                                                  originalAssociationPointer);
        return actualAssociation;
    }

    private static String reverse(String originalAssociationPointer) {
        String[] tokens = originalAssociationPointer.split(CONNECTOR);
        return Utility.concatStrings(tokens[3], tokens[2], tokens[1], tokens[0]);
    }

    public static String generateUniqueId(AssociationInterface association) {
        return Utility.generateUniqueId(association);
    }

  
}

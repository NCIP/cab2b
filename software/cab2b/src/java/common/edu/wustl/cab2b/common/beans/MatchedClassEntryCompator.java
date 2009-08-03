package edu.wustl.cab2b.common.beans;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.beans.MatchedClassEntry.MatchCause;
import edu.wustl.cab2b.common.util.EntityInterfaceComparator;
import edu.wustl.cab2b.common.util.Utility;

/**
 * A comparator for the {@link MatchedClassEntry}.
 * It checks in following order
 * 
 * 1. User defined Category Name
 * 2. User defined Category 
 * 3. Category 
 *    Name
 *    Description
 *    SemanticProperty
 * 4. Attribute
 *    Name
 *    Description
 *    SemanticProperty
 * 5. PermissibleValue
 *    Name
 *    SemanticProperty
 * 
 * @author rahul_ner
 */
public class MatchedClassEntryCompator implements Comparator<MatchedClassEntry> {
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        EntityInterface entity1 = entry1.getMatchedEntity();
        EntityInterface entity2 = entry2.getMatchedEntity();
        // If both the entities are categories then compare their names and return values accordingly
        if(Utility.isCategory(entity1) || Utility.isCategory(entity2)) {
            return new EntityInterfaceComparator().compare(entity1, entity2);
        } else {
            return compareEntityName(entry1, entry2);
        }
    }

    /**
     * @param one
     * @param two
     * @return
     */
    private int getResultForNull(Integer one, Integer two) {
        if (two == null) {
            return -1;
        } else {
            if (one == null) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareEntityName(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.positionOf(MatchCause.EntityName), entry2.positionOf(MatchCause.EntityName));
        if (result != 0) {
            return result;
        }

        result = entry1.positionOf(MatchCause.EntityName).compareTo(entry2.positionOf(MatchCause.EntityName));

        return (result == 0) ? compareEntityDescription(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareEntityDescription(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.positionOf(MatchCause.EntityDescription), entry2.positionOf(MatchCause.EntityDescription));
        if (result != 0) {
            return result;
        }
        result = entry1.positionOf(MatchCause.EntityDescription).compareTo(entry2.positionOf(MatchCause.EntityDescription));

        return (result == 0) ? compareEntitySP(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareEntitySP(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.positionOf(MatchCause.EntitySemanticProperty),
                                      entry2.positionOf(MatchCause.EntitySemanticProperty));
        if (result != 0) {
            return result;
        }
        result = entry1.positionOf(MatchCause.EntitySemanticProperty).compareTo(entry2.positionOf(MatchCause.EntitySemanticProperty));

        return (result == 0) ? compareAttributeName(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareAttributeName(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.positionOf(MatchCause.AttributeName), entry2.positionOf(MatchCause.AttributeName));
        if (result != 0) {
            return result;
        }
        result = entry1.positionOf(MatchCause.AttributeName).compareTo(entry2.positionOf(MatchCause.AttributeName));

        return (result == 0) ? compareAttributeDescription(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareAttributeDescription(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.positionOf(MatchCause.AttributeDescription),
                                      entry2.positionOf(MatchCause.AttributeDescription));
        if (result != 0) {
            return result;
        }
        result = entry1.positionOf(MatchCause.AttributeDescription).compareTo(entry2.positionOf(MatchCause.AttributeDescription));

        return (result == 0) ? compareAttributeSP(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareAttributeSP(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.positionOf(MatchCause.AttributeSemanticProperty),
                                      entry2.positionOf(MatchCause.AttributeSemanticProperty));
        if (result != 0) {
            return result;
        }
        result = entry1.positionOf(MatchCause.AttributeSemanticProperty).compareTo(
                                                                         entry2.positionOf(MatchCause.AttributeSemanticProperty));

        return (result == 0) ? comparePVName(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int comparePVName(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.positionOf(MatchCause.PermissibleValueName), entry2.positionOf(MatchCause.PermissibleValueName));
        if (result != 0) {
            return result;
        }
        result = entry1.positionOf(MatchCause.PermissibleValueName).compareTo(entry2.positionOf(MatchCause.PermissibleValueName));

        return (result == 0) ? comparePVSP(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int comparePVSP(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.positionOf(MatchCause.PermissibleSemanticProperty),
                                      entry2.positionOf(MatchCause.PermissibleSemanticProperty));
        if (result != 0) {
            return result;
        }
        result = entry1.positionOf(MatchCause.PermissibleSemanticProperty).compareTo(entry2.positionOf(MatchCause.PermissibleSemanticProperty));
        return result;
    }

}

package edu.wustl.cab2b.common.beans;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.Utility;

/**
 * A comparator for the {@link MatchedClassEntry}. It chks in following order
 * 
 * 1. User defined Category Name 2. User defined Category 3. Category Name
 * Description SemanticProperty 4. Attribute Name Description SemanticProperty
 * 5. PermissibleValue Name SemanticProperty
 * 
 * @author Rahul Ner
 * 
 */
public class MatchedClassEntryCompator implements Comparator<MatchedClassEntry> {

    /**
     * 
     */
    private static final int NOT_A_CATEGORY = 2;

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(MatchedClassEntry entry1, MatchedClassEntry entry2) {

        int result = compareForCategory(entry1, entry2);
        return result == NOT_A_CATEGORY ? compareEntityName(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareForCategory(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        EntityInterface entity1 = entry1.getMatchedEntity();
        EntityInterface entity2 = entry2.getMatchedEntity();
        String className1 = Utility.parseClassName(entity1.getName());
        String className2 = Utility.parseClassName(entity2.getName());
        // If both the entitUtility.ies are categories then compare their names
        // and
        // return values accordingly
        if ((true == Utility.isCategory(entity1)) && (true == Utility.isCategory(entity2))) {
            return (className1.compareToIgnoreCase(className2));
        } else if (true == Utility.isCategory(entity1)) { // if first entity
            // is category
            // return it as
            // smaller one
            return -1;
        } else if (true == Utility.isCategory(entity2)) { // else return as
            // greater one
            return 1;
        } else {
            return NOT_A_CATEGORY;
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
        int result = getResultForNull(entry1.getEntityNamePosition(), entry2.getEntityNamePosition());
        if (result != 0) {
            return result;
        }

        result = entry1.getEntityNamePosition().compareTo(entry2.getEntityNamePosition());

        return (result == 0) ? compareEntityDescription(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareEntityDescription(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.getEntityDescriptionPosition(), entry2.getEntityDescriptionPosition());
        if (result != 0) {
            return result;
        }
        result = entry1.getEntityDescriptionPosition().compareTo(entry2.getEntityDescriptionPosition());

        return (result == 0) ? compareEntitySP(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareEntitySP(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.getEntitySemanticPropertyPosition(), entry2
                .getEntitySemanticPropertyPosition());
        if (result != 0) {
            return result;
        }
        result = entry1.getEntitySemanticPropertyPosition().compareTo(entry2.getEntitySemanticPropertyPosition());

        return (result == 0) ? compareAttributeName(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareAttributeName(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.getAttributeNamePosition(), entry2.getAttributeNamePosition());
        if (result != 0) {
            return result;
        }
        result = entry1.getAttributeNamePosition().compareTo(entry2.getAttributeNamePosition());

        return (result == 0) ? compareAttributeDescription(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareAttributeDescription(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.getAttributeDescriptionPosition(), entry2
                .getAttributeDescriptionPosition());
        if (result != 0) {
            return result;
        }
        result = entry1.getAttributeDescriptionPosition().compareTo(entry2.getAttributeDescriptionPosition());

        return (result == 0) ? compareAttributeSP(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int compareAttributeSP(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.getAttributeSemanticPropertyPosition(), entry2
                .getAttributeSemanticPropertyPosition());
        if (result != 0) {
            return result;
        }
        result = entry1.getAttributeSemanticPropertyPosition().compareTo(
                entry2.getAttributeSemanticPropertyPosition());

        return (result == 0) ? comparePVName(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int comparePVName(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.getPvNamePosition(), entry2.getPvNamePosition());
        if (result != 0) {
            return result;
        }
        result = entry1.getPvNamePosition().compareTo(entry2.getPvNamePosition());

        return (result == 0) ? comparePVSP(entry1, entry2) : result;
    }

    /**
     * @param entry1
     * @param entry2
     * @return
     */
    private int comparePVSP(MatchedClassEntry entry1, MatchedClassEntry entry2) {
        int result = getResultForNull(entry1.getPvSemanticPropertyPosition(), entry2
                .getPvSemanticPropertyPosition());
        if (result != 0) {
            return result;
        }
        result = entry1.getPvSemanticPropertyPosition().compareTo(entry2.getPvSemanticPropertyPosition());
        return result;
    }

}

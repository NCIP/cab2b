package edu.wustl.cab2b.common.cache;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.wustl.cab2b.common.beans.MatchedClassEntry;

/**
 * @author Chandrakant Talele
 * @author Rahul Ner
 */
public class CompareUtil {

    // TODO copied from edu.wustl.cab2b.common.util.Utility
    /**
     * Compares whether given searchPattern is present in passed searchString.
     * If it is present returns the position where match found. Otherwise it
     * returns -1.
     * 
     * @param searchPattern
     * @param searchString
     * @return The position where match found, otherwise returns -1.
     */
    public static int indexOfRegEx(String searchPattern, String searchString) {
        Pattern pat = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(searchString);
        int position = -1;

        if (mat.find()) {
            position = mat.start();
        }
        return position;
    }

    // END COPY
    /**
     * Compares given pattern entity with the cachedEntity in following order 1.
     * Name 2. Description 3. SemanticProperty If any of the above field matches
     * then, {@link MatchedClassEntry} is created for that entity. The position
     * where the match was found is set into the {@link MatchedClassEntry}. It
     * is used to sort the entire search result
     * 
     * @param cachedEntity
     * @param patternEntity
     * @return
     */
    public static MatchedClassEntry compare(EntityInterface cachedEntity, EntityInterface patternEntity) {
        MatchedClassEntry matchedClassEntry = null;
        int index = -1;

        if (patternEntity.getName() != null && cachedEntity.getName() != null) {
            String patternName = patternEntity.getName();
            String className = cachedEntity.getName();
            String onlyClassName = className.substring(className.lastIndexOf(".") + 1, className.length());

            index = indexOfRegEx(patternName, onlyClassName);
            if (index != -1) {
                matchedClassEntry = new MatchedClassEntry(cachedEntity);
                matchedClassEntry.setEntityNamePosition(index);
            }
        }

        if (matchedClassEntry == null && patternEntity.getDescription() != null
                && cachedEntity.getDescription() != null) {
            String patternDescription = patternEntity.getDescription();

            index = indexOfRegEx(patternDescription, cachedEntity.getDescription());
            if (index != -1) {
                matchedClassEntry = new MatchedClassEntry(cachedEntity);
                matchedClassEntry.setEntityDescriptionPosition(index);
            }
        }

        if (matchedClassEntry == null) {
            index = compare(cachedEntity.getSemanticPropertyCollection(), patternEntity
                    .getSemanticPropertyCollection());

            if (index != -1) {
                matchedClassEntry = new MatchedClassEntry(cachedEntity);
                matchedClassEntry.setEntitySemanticPropertyPosition(index);
            }

        }

        return matchedClassEntry;
    }

    /**
     * Compares given pattern SemanticProperty with the Cached SemanticProperty
     * for the concept code If it matched, return the position where the match
     * was found.
     * 
     * @param cachedSemanticProperty
     * @param patternSemanticProperty
     * @return
     */
    public static int compare(SemanticPropertyInterface cachedSemanticProperty,
            SemanticPropertyInterface patternSemanticProperty) {
        int index = -1;
        if (cachedSemanticProperty.getConceptCode() != null) {
            // TODO this null check is bcoz caTissue
            // has some permissible values without
            // concept codes. This will never be the case with models from cDSR
            if (patternSemanticProperty.getConceptCode() != null) {
                String patternConceptCode = patternSemanticProperty.getConceptCode();

                index = indexOfRegEx(patternConceptCode, cachedSemanticProperty.getConceptCode());
            }
        }
        return index;
    }

    /**
     * Comapares given pattern attribute with the cached attribute in following
     * order 1. Name 2. Description 3. SemanticProperty If any of the above
     * field matches then, {@link MatchedClassEntry} is created for the entity
     * of that attribute. The position where the match was found is set into the
     * {@link MatchedClassEntry}. It is used to sort the entire search result
     * 
     * @param cachedAttribute
     * @param patternAttribute
     * @return
     */
    public static MatchedClassEntry compare(AttributeInterface cachedAttribute, AttributeInterface patternAttribute) {
        MatchedClassEntry matchedClassEntry = null;
        int index = -1;

        if (patternAttribute.getName() != null && cachedAttribute.getName() != null) {
            String patternName = patternAttribute.getName();

            index = indexOfRegEx(patternName, cachedAttribute.getName());
            if (index != -1) {
                matchedClassEntry = new MatchedClassEntry(cachedAttribute.getEntity());
                matchedClassEntry.setAttributeNamePosition(index);
            }

        }

        if (matchedClassEntry == null && patternAttribute.getDescription() != null
                && cachedAttribute.getDescription() != null) {
            String patternDescription = patternAttribute.getDescription();

            index = indexOfRegEx(patternDescription, cachedAttribute.getDescription());
            if (index != -1) {
                matchedClassEntry = new MatchedClassEntry(cachedAttribute.getEntity());
                matchedClassEntry.setAttributeDescriptionPosition(index);
            }
        }

        if (matchedClassEntry == null) {
            index = compare(cachedAttribute.getSemanticPropertyCollection(), patternAttribute
                    .getSemanticPropertyCollection());

            if (index != -1) {
                matchedClassEntry = new MatchedClassEntry(cachedAttribute.getEntity());
                matchedClassEntry.setAttributeSemanticPropertyPosition(index);
            }

        }
        return matchedClassEntry;
    }

    /**
     * Compares given pattern SemanticProperties with the cached
     * SemanticProperties. returns the postion of first matched concept code.
     * 
     * @param cachedProperties
     * @param patternProperties
     * @return
     */
    private static int compare(Collection<SemanticPropertyInterface> cachedProperties,
            Collection<SemanticPropertyInterface> patternProperties) {
        int index = -1;

        if (patternProperties != null && cachedProperties != null) {

            for (SemanticPropertyInterface patternSemanticProperty : patternProperties) {
                for (SemanticPropertyInterface cachedSemanticProperty : cachedProperties) {
                    index = compare(cachedSemanticProperty, patternSemanticProperty);
                    if (index != -1) {
                        break;
                    }
                }
            }
        }
        return index;
    }

    /**
     * Comapares given pattern PermissibleValue with the cached PermissibleValue
     * in following order 1. Name 2. SemanticProperty If any of the above field
     * matches then, {@link MatchedClassEntry} is created for the entity of that
     * PermissibleValue. The position where the match was found is set into the
     * {@link MatchedClassEntry}. It is used to sort the entire search result
     * 
     * @param cachedPermissibleValue
     * @param patternPermissibleValue
     * @param cachedEntity
     * @return
     */
    public static MatchedClassEntry compare(PermissibleValueInterface cachedPermissibleValue,
            PermissibleValueInterface patternPermissibleValue, EntityInterface cachedEntity) {
        MatchedClassEntry matchedClassEntry = null;
        int index = -1;

        if (patternPermissibleValue.getValueAsObject() != null
                && cachedPermissibleValue.getValueAsObject() != null) {
            String patternPermissibleString = patternPermissibleValue.getValueAsObject().toString();
            String cachedPermissibleString = cachedPermissibleValue.getValueAsObject().toString();
            index = indexOfRegEx(patternPermissibleString, cachedPermissibleString);
            if (index != -1) {
                matchedClassEntry = new MatchedClassEntry(cachedEntity);
                matchedClassEntry.setPvNamePosition(index);
            }

        }

        if (matchedClassEntry == null) {
            index = compare(cachedPermissibleValue.getSemanticPropertyCollection(), patternPermissibleValue
                    .getSemanticPropertyCollection());

            if (index != -1) {
                matchedClassEntry = new MatchedClassEntry(cachedEntity);
                matchedClassEntry.setPvSemanticPropertyPosition(index);
            }

        }
        return matchedClassEntry;
    }

}
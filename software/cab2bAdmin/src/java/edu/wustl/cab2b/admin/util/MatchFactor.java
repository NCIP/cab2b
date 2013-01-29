/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.util;

import edu.wustl.cab2b.common.exception.RuntimeException;

/**
 * enumeration for matching the factor
 * @author srinath_k
 */
public enum MatchFactor {
    /** enumeration for public id based match factor */
    PUBLIC_ID("Public ID"),
    /** enumeration for attribute based match factor */
    ATTRIBUTE_CONCEPT_CODE("attribute concept code"),
    /** enumeration for class concept code based match factor */
    CLASS_CONCEPT_CODE("class concept code"),
    /** enumeration for manual connect based match factor */
    MANUAL_CONNECT("manually connected");
    private String value;

    private MatchFactor(String value) {
        this.value = value;
    }

    /**
     * @return The string value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value String value
     * @return corresponding match factor
     */
    public static MatchFactor getMatchFactor(String value) {
        for (MatchFactor factor : MatchFactor.values()) {
            if (factor.getValue().equalsIgnoreCase(value)) {
                return factor;
            }
        }
        throw new RuntimeException("No MatchFactor found for : " + value);
    }
};
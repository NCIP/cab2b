/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.queryengine;

/**
 * @author chetan_patil
 *
 */
public enum QueryType {
    ANDed("ANDed"), ORed("ORed");

    private String type;

    private QueryType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}

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

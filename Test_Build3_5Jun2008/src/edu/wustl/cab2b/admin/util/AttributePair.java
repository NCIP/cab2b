package edu.wustl.cab2b.admin.util;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.util.Utility;

/**
 * A pair of attributes used in defining an inter-model connection.
 * @author srinath_k
 */
public class AttributePair {
    public enum MatchFactor {
        PUBLIC_ID("Public ID"), ATTRIBUTE_CONCEPT_CODE("attribute concept code"), CLASS_CONCEPT_CODE(
                "class concept code"), MANUAL_CONNECT("manually connected");

        private String value;

        private MatchFactor(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    };

    private AttributeInterface attribute1;

    private AttributeInterface attribute2;

    private MatchFactor matchFactor;

    private String displayValue;

    public AttributePair(AttributeInterface attribute1, AttributeInterface attribute2) {
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
    }

    public AttributeInterface getAttribute1() {
        return attribute1;
    }

    public AttributeInterface getAttribute2() {
        return attribute2;
    }

    /**
     * Two <tt>AttributePair's x</tt> and <tt>y</tt> are equal if and only
     * if <br>
     * 
     * <pre><tt>
     * (x.attribute1.equals(y.attribute1) &amp;&amp; x.attribute2.equals(y.attribute2))
     *         || (x.attribute1.equals(y.attribute2) &amp;&amp; x.attribute2.equals(y.attribute1))
     * </tt></pre>
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AttributePair)) {
            return false;
        }
        AttributePair o = (AttributePair) obj;
        return (attribute1.equals(o.attribute1) && attribute2.equals(o.attribute2))
                || (attribute1.equals(o.attribute2) && attribute2.equals(o.attribute1) && matchFactor == o.matchFactor);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(attribute1).append(attribute2).append(matchFactor).toHashCode();
    }

    /**
     * @param matchFactor the matchFactor to set
     */
    public void setMatchFactor(MatchFactor matchFactor) {
        this.matchFactor = matchFactor;
    }

    /**
     * @return the matchFactor
     */
    public MatchFactor getMatchFactor() {
        return matchFactor;
    }

    /**
     * @param matchFactor the matchFactor to set
     */
    public String toString() {
        String entityName1 = Utility.getDisplayName(attribute1.getEntity());
        String entityName2 = Utility.getDisplayName(attribute2.getEntity());

        StringBuilder description = new StringBuilder();
        description.append(attribute1.getName()).append(" of ").append(entityName1).append(" and ").append(
                                                                                                           attribute2.getName()).append(
                                                                                                                                        " of ").append(
                                                                                                                                                       entityName2);
        if (MatchFactor.MANUAL_CONNECT == matchFactor) {
            description.append(" are ").append(matchFactor.getValue());
        } else {
            description.append(" have same ").append(matchFactor.getValue());
        }
        return description.toString();
    }

    /**
     * @return the displayString
     */
    public String getDisplayValue() {
        String entityName1 = Utility.getDisplayName(attribute1.getEntity());
        String entityName2 = Utility.getDisplayName(attribute2.getEntity());

        StringBuilder description = new StringBuilder();
        description.append("<i>").append(attribute1.getName().toUpperCase()).append("</i> of <i>").append(
                                                                                                          entityName1).append(
                                                                                                                              "</i> and <i>").append(
                                                                                                                                                     attribute2.getName().toUpperCase()).append(
                                                                                                                                                                                                "</i> of <i>").append(
                                                                                                                                                                                                                      entityName2);
        if (MatchFactor.MANUAL_CONNECT == matchFactor) {
            description.append("</i> are ").append(matchFactor.getValue());
        } else {
            description.append("</i> have same ").append(matchFactor.getValue());
        }

        displayValue = description.toString();
        return displayValue;
    }

}

/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.util;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.util.Utility;

/**
 * A pair of attributes used in defining an inter-model connection.
 * 
 * @author srinath_k
 */
public class AttributePair {

	private AttributeInterface attribute1;

	private AttributeInterface attribute2;

	private MatchFactor matchFactor;

	private String displayValue;

	/**
	 * Constructor
	 * 
	 * @param attribute1
	 *            First attribute
	 * @param attribute2
	 *            Second attribute
	 */
	public AttributePair(AttributeInterface attribute1,
			AttributeInterface attribute2) {
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
	}

	/**
	 * @return First attribute
	 */
	public AttributeInterface getAttribute1() {
		return attribute1;
	}

	/**
	 * @return second attribute
	 */
	public AttributeInterface getAttribute2() {
		return attribute2;
	}

	/**
	 * Two <tt>AttributePair's x</tt> and <tt>y</tt> are equal if and only if <br>
	 * 
	 * <pre>
	 * &lt;tt&gt;
	 * (x.attribute1.equals(y.attribute1) &amp;&amp; x.attribute2.equals(y.attribute2))
	 *         || (x.attribute1.equals(y.attribute2) &amp;&amp; x.attribute2.equals(y.attribute1))
	 * &lt;/tt&gt;
	 * </pre>
	 * 
	 * @param obj
	 *            object to compare
	 * @return TRUE if passes object is equals to this
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
		return (attribute1.equals(o.attribute1) && attribute2
				.equals(o.attribute2))
				|| (attribute1.equals(o.attribute2)
						&& attribute2.equals(o.attribute1) && matchFactor == o.matchFactor);
	}

	/**
	 * @return hash code
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(attribute1).append(attribute2)
				.append(matchFactor).toHashCode();
	}

	/**
	 * @param matchFactor
	 *            the matchFactor to set
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
	 * @return The string representation
	 */
	public String toString() {
		String entityName1 = Utility.getDisplayName(attribute1.getEntity());
		String entityName2 = Utility.getDisplayName(attribute2.getEntity());

		StringBuilder description = new StringBuilder();
		description.append(attribute1.getName()).append(" of ").append(
				entityName1);
		description.append(" and ");
		description.append(attribute2.getName()).append(" of ").append(
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
		description.append("<i>").append(attribute1.getName().toUpperCase());
		description.append("</i> of <i>").append(entityName1);
		description.append("</i> and <i>").append(
				attribute2.getName().toUpperCase());
		description.append("</i> of <i>").append(entityName2);
		if (MatchFactor.MANUAL_CONNECT == matchFactor) {
			description.append("</i> are ").append(matchFactor.getValue());
		} else {
			description.append("</i> have same ")
					.append(matchFactor.getValue());
		}

		displayValue = description.toString();
		return displayValue;
	}
}
/**
 * 
 */

package edu.wustl.common.querysuite.queryobject.util;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * Interface for InheritanceUtils method.
 * @author prafull_kadam
 *
 */
public interface InheritanceUtilsInterface
{

	/**
	 * TO check whether given attribute is inherited or not.
	 * @param abstractAttribute reference to AbstractAttributeInterface
	 * @return true if attribute is inherited.
	 */
	boolean isInherited(AbstractAttributeInterface abstractAttribute);

	/**
	 * Returns actual attribute if passed attribute is a derieved one. Else
	 * returns the passed attribute
	 * @param attribute Attribute for which actual attribute is expected.
	 * @return The actual attribute
	 */
	AttributeInterface getActualAttribute(AttributeInterface attribute);

	/**
	 * Returns actual association if passed association is a derieved one. Else
	 * returns the passed association
	 * @param association Attribute for which actual association is expected.
	 * @return The actual association
	 */
	AssociationInterface getActualAassociation(AssociationInterface association);
}

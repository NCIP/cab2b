/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domain.SemanticAnnotatableInterface;

/**
 * When the value domain for an attribute is user defined,the data element object is of type CaDSRDE 
 * and this object contains a collection of permissible values.  
 * @author sujay_narkar

 */
public interface PermissibleValueInterface extends SemanticAnnotatableInterface
{

	/**
	 * This method returns the unique identifier.
	 * @return the unique identifier.
	 */
	Long getId();

	/**
	 * This method returns the value of DateValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	Object getValueAsObject();

}

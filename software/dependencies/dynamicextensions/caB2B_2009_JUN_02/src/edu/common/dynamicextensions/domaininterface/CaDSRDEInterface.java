/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

/**
 * The permissible values for an attribute may come from different sources such as caDSR or may be user defnied.
 * If the values will come from caDSR then DataElement is of type CaDSRDE.This interface stores public id from caDSR
 * for the attribute.
 * @author sujay_narkar
 *
 */
public interface CaDSRDEInterface extends DataElementInterface
{

	/**
	 * This method returns the public identifier.
	 * @hibernate.property name="publicId" type="string" column="PUBLIC_ID" 
	 * @return the public identifier.
	 */
	String getPublicId();

	/**
	 * This method sets the public identifier.
	 * @param publicId the public identifier to be set.
	 */
	void setPublicId(String publicId);
	
}

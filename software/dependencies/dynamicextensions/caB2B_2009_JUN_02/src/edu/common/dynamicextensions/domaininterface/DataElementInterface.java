/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

/**
 * Data element represent the source of the permissible values for a primitive attribute.
 * Data element is an abstract class.The data element may be of type caDSRDE or of type user defined.
 * @author sujay_narkar
 */
public interface DataElementInterface extends DynamicExtensionBaseDomainObjectInterface
{
    
	/**
	 * This method returns the unique identifier of DataElement.
	 * @return the unique identifier of DataElement.
	 */
    Long getId();
    
    /**
	 * This method sets the unique identifier of DataElement.
	 * @param id the unique identifier to be set.
	 */
	void setId(Long id);
    
}

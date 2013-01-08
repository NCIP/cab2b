/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ByteArrayValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_BARR_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"   
 * @author sujay_narkar
 *
 */
public class ByteArrayValue extends PermissibleValue implements ByteArrayValueInterface
{
	/**
	 * Sserial Version Unique Identifier
	 */
	private static final long serialVersionUID = -5535531891478617220L;
	
	/**
	 * This method returns the value of ByteArrayValue downcasted to the Object.
	 * @return the value of ByteArrayValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return null;
	}

}

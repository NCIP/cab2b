/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.CaDSRDEInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * This Class represents the values form CaDSR
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_CADSRDE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"   
 */
public class CaDSRDE extends DataElement implements CaDSRDEInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -5908788817639522056L;
	
	/**
	 * Public Identifier from CaDSR.
	 */
	protected String publicId;

	/**
	 * This method returns the public identifier.
	 * @hibernate.property name="publicId" type="string" column="PUBLIC_ID" 
	 * @return the public identifier of the CaDSR.
	 */
	public String getPublicId()
	{
		return publicId;
	}

	/**
	 * This method sets the public identifier.
	 * @param publicId the public identifier to be set.
	 */
	public void setPublicId(String publicId)
	{
		this.publicId = publicId;
	}
	
}

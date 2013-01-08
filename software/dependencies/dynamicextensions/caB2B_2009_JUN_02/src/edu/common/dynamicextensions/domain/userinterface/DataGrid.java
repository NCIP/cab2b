/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DataGridInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATA_GRID" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DataGrid extends Control implements DataGridInterface
{

	/**
	 * Empty Constructor
	 */
	public DataGrid()
	{
	}

	

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface)
	{
	}


	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return "";
	}


	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return "&nbsp;";
	}

}
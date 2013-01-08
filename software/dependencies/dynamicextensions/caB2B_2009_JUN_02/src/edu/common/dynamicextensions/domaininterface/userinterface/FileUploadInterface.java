/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Nov 3, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.domaininterface.userinterface;


/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface FileUploadInterface extends ControlInterface  
{
	/**
	 * 
	 * @return Number of columns (size of text box shown on UI)
	 */
	public Integer getColumns();
	/**
	 * 
	 * @param columns  Number of columns (size of text box shown on UI)
	 */
	public void setColumns(Integer columns);
}

/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import java.util.List;


/**
 * @author vijay_pande
 * Interface to declare operations for DataView.
 */
public interface IDataView extends IResultView
{
	/**
	 * Method to return list of output attributes
	 * @return outputAttributeList List of objects of type IOutputAttribute
	 */
	public List<IOutputAttribute> getOutputAttributeList();
	
	/**
	 * Method to set list of output attributes
	 * @param outputAttributeList List of objects of type IOutputAttribute
	 */
	public  void setOutputAttributeList(List<IOutputAttribute> outputAttributeList);
}

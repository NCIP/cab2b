/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import java.util.List;

import edu.wustl.common.querysuite.queryobject.IDataView;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;


/**
 * @author vijay_pande
 * Class for data view to show results for data query
 */
public class DataView extends ResultView implements IDataView
{
	/**
	 * List of object of type IOutputAttribute for data result view
	 */
	private List<IOutputAttribute> outputAttributeList;

	
	/** 
	 * Method to get list of output attributes
	 * @return outputAttributeList List of objects of type IOutputAttribute
	 */
	public List<IOutputAttribute> getOutputAttributeList()
	{
		return outputAttributeList;
	}

	/** 
	 * Method to set list of output attributes
	 * @param outputAttributeList List of objects of type IOutputAttribute
	 */
	public void setOutputAttributeList(List<IOutputAttribute> outputAttributeList)
	{
		this.outputAttributeList = outputAttributeList;
	}
}

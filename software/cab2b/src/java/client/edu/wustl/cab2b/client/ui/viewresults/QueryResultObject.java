/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import java.util.List;

import edu.wustl.cab2b.common.datalist.IDataRow;

public class QueryResultObject 
{

	private List<IDataRow> m_resultElements;
	private List<IDataRow> m_childElements;
	
	public QueryResultObject(List<IDataRow> resultElements, List<IDataRow> childElements)
	{
		m_resultElements = resultElements;
		m_childElements =childElements;
	}
	
	public List<IDataRow> getResults()
	{
		return m_resultElements;
	}
	
	public List<IDataRow> getChilds()
	{
		return m_childElements;
	}
}

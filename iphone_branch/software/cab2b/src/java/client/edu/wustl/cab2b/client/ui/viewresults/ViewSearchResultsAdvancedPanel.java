/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;

/**
 * This panel shows the search result in more detailed and comprehensive way.
 * 
 * @author chetan_bh
 */
public class ViewSearchResultsAdvancedPanel extends Cab2bPanel
{
	IQueryResult queryResult;
	
	public ViewSearchResultsAdvancedPanel(IQueryResult queryResult)
	{
		this.queryResult = queryResult;
		initGUI();
	}
	
	private void initGUI()
	{
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}

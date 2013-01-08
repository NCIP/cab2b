/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import java.util.List;

/**
 * This interface is implemented by each panel that can eb displayed in 
 * data list detailed section.
 * 
 * It provides methods to convert data of the panel to csv format.
 *  
 * 
 * @author Rahul Ner
 *
 */
public interface DataListDetailedPanelInterface {

    List<String> getSelectedRowNames();

    List<String> getSelectedColumnNames();

    Object[][] getSelectedData();
}

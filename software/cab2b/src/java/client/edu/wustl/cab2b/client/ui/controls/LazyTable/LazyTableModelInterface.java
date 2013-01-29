/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.LazyTable;

import javax.swing.table.TableModel;

/**
 * Table model that may not have complete data a time.
 * It should have a referene of the data LazyDataSourceInterface that will fetch the data when
 * it is required.
 * 
 * @author rahul_ner
 */
public interface LazyTableModelInterface extends TableModel {
    
    

}

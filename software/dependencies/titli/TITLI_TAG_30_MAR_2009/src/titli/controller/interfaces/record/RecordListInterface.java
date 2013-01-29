/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package titli.controller.interfaces.record;

import java.util.List;

import titli.controller.Name;

/**
 * a list of records
 * @author Juber Patel
 *
 */
public interface RecordListInterface extends List<RecordInterface>
{
	/**
	 * @return the tableName
	 */
	Name getTableName();
	

	/**
	 * @return the timeTaken to retrieve these records
	 */
	double getTimeTaken(); 
	


}

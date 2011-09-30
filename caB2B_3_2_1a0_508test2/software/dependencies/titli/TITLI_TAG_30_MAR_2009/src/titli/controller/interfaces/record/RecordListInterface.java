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

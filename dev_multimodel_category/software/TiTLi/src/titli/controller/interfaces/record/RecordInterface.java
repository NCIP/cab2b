/**
 * 
 */
package titli.controller.interfaces.record;



import java.util.Map;

import titli.controller.interfaces.ColumnInterface;
import titli.controller.interfaces.TableInterface;

/**
 * A record from a single table
 * A RecordInterface is fetched when fetch is requested on a match     
 * @author Juber Patel
 *
 */
public interface RecordInterface 
{
	/**
	 * Get the corresponding table
	 * @return the table for the record
	 */
	TableInterface getTable();
	
	/**
	 * The "Column" => "Value" Map 
	 * @return a linked hashmap that contains columns and column values of the record as key-value pairs
	 */
	Map<ColumnInterface, String> getColumnMap();
	
	/**
	 * Get the time taken in seconds to fetch this record
	 * @return the time taken in seconds to fetch this record
	 */
	double getTimeTaken();
	
	
	/**
	 * Get the string representation of the record
	 * @return the string representation of the record
	 */
	String toString();
	
}

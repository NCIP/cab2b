/**
 * 
 */
package titli.controller.interfaces;

import java.util.List;
import java.util.Map;

import titli.controller.Name;


/**
 * Represents table metadata like table name, columns etc.
 * @author Juber Patel
 *
 */
public interface TableInterface
{
	/**
	 * returns the name of the table
	 * @return the name of the table
	 */
	Name getName();
	
	
	/**
	 * Get the name of the database in which the table belongs
	 * @return the name of the table
	 */
	Name getDatabaseName();
	
	
	/**
	 * Get the unique key set for the table
	 * @return the unique key set for the table
	 */
	List<Name> getUniqueKey();
	
	
	/**
	 * Get the number of columns in the table
	 * @return the number of columns in the table
	 */
	int getNumberOfColumns();
	
	
	/**
	 * Get the column specified by name 
	 * @param name the name of the column
	 * @return the corresponding column
	 */
	ColumnInterface getColumn(Name name);
	
	
	/**
	 * Get a map of  "column name" => "column"
	 * @return a map of  "column name => "column"
	 */
	Map<Name, ColumnInterface> getColumns();
	
	
		
}

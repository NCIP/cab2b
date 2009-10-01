/**
 * 
 */
package titli.controller.interfaces;

import java.util.Map;

import titli.controller.Name;


/**
 * Represents database metadata
 * @author Juber Patel
 *
 */
public interface DatabaseInterface
{

	/**
	 * get the name of the database
	 * @return return the name of the database
	 */
	Name getName();
	
	
	/**
	 * Get the number of tables in the database
	 * @return the number of tables in the database
	 */
	int getNumberOfTables();
	
	
	
	/**
	 * return the table specified by the name 
	 * @param name the name of the table
	 * @return the table
	 */
	TableInterface getTable(Name name);
	
	
	/**
	 * Get a map of all the tables in the database
	 * @return a map of all the table names and tables in the database
	 */
	Map<Name, TableInterface> getTables();
	
	
	
}

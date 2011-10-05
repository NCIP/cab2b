/**
 * 
 */
package titli.controller.interfaces;

import java.util.Map;

import titli.controller.Name;
import titli.controller.interfaces.record.RecordInterface;
import titli.model.fetch.TitliFetchException;

/**
 * A Document in the index that matched the given search string
 * A MatchInterface only deals with the index and not with the databases  
 * @author juber Patel
 *
 */
public interface MatchInterface
{
	/**
	 * Get the table in which the matched record belongs
	 * @return the table
	 */	
	Name getTableName();
	
	/**
	 * Get the database in which the matched record belongs
	 * @return the database
	 */	
	Name getDatabaseName();
	
	/**
	 * get the "column name" => "column value" map of the unique key set of the record
	 * @return the "column name" => "column value" map of the unique key set of the record
	 */
	Map<Name, String> getUniqueKeys();
	
	/**
	 * 
	 * @return the string representaion of the match
	 */
	String toString();
	
	/**
	 * Fetch the record corresponding to the match
	 * @return the record corresponding to the match
	 * @throws TitliFetchException if problems occur
	 */
	RecordInterface fetch() throws TitliFetchException;

	
	
}

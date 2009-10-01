/**
 * 
 */
package titli.controller.interfaces;

import java.io.File;
import java.util.Map;

import titli.controller.Name;
import titli.model.TitliException;


/**
 * The main interface. This interface is used to initialize Titli, index databases and fire searches 
 * @author Juber Patel
 *
 */
public interface TitliInterface
{
	
	/**
	 * Get the number of databases 
	 * @return the number of databases
	 */
	int getNumberOfDatabases();
	
	/**
	 * Get the database specified byname 
	 * @param dbName the name of the database
	 * @return the database
	 */
	DatabaseInterface getDatabase(Name dbName);
	
	/**
	 * 
	 * @return the list of databases
	 */
	Map<Name, DatabaseInterface> getDatabases();
	
	/**
	 * get the index location as found in the property file
	 * @return the index location as found in the property file
	 */
	File getIndexLocation();
	
	
	
	
	
	/****************            index methods    *********************/  
	
	/**
	 * index all the databases from the scratch
	 * @throws TitliException if problems occur 
	 *
	 */
	void index() throws TitliException;
	
	/**
	 * index from the scratch the specified database
	 * @param databaseName the database name
	 * @throws TitliException if problems occur
	 */
	void index(Name databaseName) throws TitliException;
	
	
	/**
	 * index from the scratch the specified table of the specified database
	 * @param databaseName the database name
	 * @param tableName the table name
	 * @throws TitliException if problems occur
	 */
	void index(Name databaseName, Name tableName) throws TitliException;
	
	
	
	/**
	 *get the index refresher 
	 * @return the index refresher
	 */
	IndexRefresherInterface getIndexRefresher();
	
	
	
	
	
	/****************            search methods     *********************/
	
	
	/**
	 *serach the specified query 
	 * @param query the search string for which the search is to be performed
	 * @return the list of matches found
	 * @throws TitliException if problems occur
	 */
	MatchListInterface search(String query) throws TitliException;
	
	
}

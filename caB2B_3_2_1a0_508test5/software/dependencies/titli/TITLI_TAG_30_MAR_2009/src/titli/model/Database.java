/**
 * 
 */
package titli.model;



import java.util.LinkedHashMap;
import java.util.Map;

import titli.controller.Name;
import titli.controller.interfaces.DatabaseInterface;
import titli.controller.interfaces.TableInterface;


/**
 * Represents database metadata
 * @author Juber Patel
 *
 */
public class Database implements DatabaseInterface 
{
	private Name name;
	private Map<Name, Table> tables;
	
	/**
	 * 
	 * @param name name of the database
	 * @param tables the list of tables which are part of the databases
	 */
	public Database(Name name, Map<Name, Table> tables) 
	{
		this.name = name;
		this.tables = tables;
		
	}

	/**
	 * get the name of the database
	 * @return return the name of the database
	 */
	public Name getName()
	{
		return name;
	}
	
	
	/**
	 * Get the number of tables in the database
	 * @return the number of tables in the database
	 */
	public int getNumberOfTables()
	{
		return tables.size();
	}
	
	
	/**
	 * return the table specified by name  
	 * @param name the name of the table
	 * @return the table
	 */
	public TableInterface getTable(Name name)
	{
		return tables.get(name);
	}
	
	
	/**
	 * Get a map of all the tables in the database
	 * @return a map of all the table names and tables in the database
	 */
	public Map<Name, TableInterface> getTables()
	{
		return new LinkedHashMap<Name, TableInterface>(tables);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @param args args to main
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

}

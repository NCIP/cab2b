/**
 * TiTLi business model
 */
package titli.model;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import titli.controller.Name;
import titli.controller.interfaces.ColumnInterface;
import titli.controller.interfaces.TableInterface;


/**
 * Represents table metadata like table name, columns etc.
 * @author Juber Patel 
 *
 */
public class Table implements TableInterface
{
	private Name name;
	private Name databaseName;
	private List<Name> uniqueKey;
	private Map<Name, Column> columns;
	
	/**
	 * 
	 * @param name name of the table
	 * @param databaseName the name of the database which the table belongs to
	 * @param uniqueKey unique key set for the table
	 * @param columns the list of columns that are to be part of this table
	 */
	Table(Name name, Name databaseName, List<Name> uniqueKey,   Map<Name, Column> columns)
	{
		this.name = name;
		this.databaseName = databaseName;
		this.uniqueKey = new ArrayList<Name> (uniqueKey);
		this.columns =  columns;
		
		
	}
	
	
	/**
	 * returns the name of the table
	 * @return the name of the table
	 */
	public Name getName()
	{
		return name;
		
	}
	
	
	/**
	 * returns the name of the table
	 * @return the name of the table
	 */
	public Name getDatabaseName()
	{
		return databaseName;
		
	}
	
	
	/**
	 * 
	 * @return the unique key set for the table
	 */
	public List<Name> getUniqueKey()
	{
		return new ArrayList<Name>(uniqueKey); 
	}
	
	/**
	 * Get the number of columns in the table
	 * @return the number of columns in the table
	 */
	public int getNumberOfColumns()
	{
		return columns.size();
	}
	
	
	
	
	/**
	 * Get the column specified name 
	 * @param name the name of the column
	 * @return the corresponding column
	 */
	public ColumnInterface getColumn(Name name)
	{
		return columns.get(name);
	}
	
	
	/**
	 * Get a map of  "column name" => "column"
	 * @return a map of  "column name => "column"
	 */
	public Map<Name, ColumnInterface> getColumns()
	{
		return new LinkedHashMap<Name, ColumnInterface>(columns);
		
	}
	
	
	
	
	/**
	 * @param args to main
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}

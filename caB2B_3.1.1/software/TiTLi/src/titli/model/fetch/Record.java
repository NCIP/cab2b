/**
 * 
 */
package titli.model.fetch;


import java.util.LinkedHashMap;
import java.util.Map;

import titli.controller.interfaces.ColumnInterface;
import titli.controller.interfaces.TableInterface;
import titli.controller.interfaces.record.RecordInterface;
import titli.model.Column;
import titli.model.Table;

/**
 * @author Juber Patel
 *
 */
public class Record implements RecordInterface
{
	private Table table;
	private Map<Column, String> columnMap;
	private double time;
	private String self;
	
	/**
	 * 
	 * @param table the table in which the record belongs
	 * @param columnMap a column => value map
	 * @param time the time taken to fetch the record from the database
	 */
	public Record(Table table, Map<Column, String> columnMap, double time)
	{
		this.columnMap = columnMap;
		this.table = table;
		this.time = time;
		
	}

	
	/* (non-Javadoc)
	 * @see titli.controller.interfaces.record.RecordInterface#getColumnMap()
	 */
	
	/**
	 * The "Column" => "Value" Map 
	 * @return a linked hashmap that contains columns and column values of the record as key-value pairs
	 */
	public Map<ColumnInterface, String> getColumnMap()
	{
		return new LinkedHashMap<ColumnInterface, String>(columnMap);
	}

	
	/* (non-Javadoc)
	 * @see titli.controller.interfaces.record.RecordInterface#getTable()
	 */
	
	/**
	 * Get the table in which the record belongs
	 * @return the table in which the record belongs
	 */
	public TableInterface getTable() 
	{
		return table;
	}

	
	/**
	 * 
	 * @return the time taken in seconds to fetch this record
	 */
	public double getTimeTaken()
	{
		return time;
	}


	/**
	 * Get the string representation of the record
	 * @return the string representation of the record
	 */
	public String toString()
	{
		if(self==null)
		{
			StringBuilder string = new StringBuilder();
			
			string.append("\nDATABASE : "+table.getDatabaseName());
			string.append("   TABLE : "+table.getName()+"\n");
			
			for(Column column : columnMap.keySet())
			{
				string.append(column.getName()+" : "+columnMap.get(column)+"  ");
			}
			
			self = new String(string);
			
		}
		
		return self;
	}
	
}

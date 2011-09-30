/**
 * 
 */
package titli.model.fetch;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import titli.controller.Name;
import titli.model.Column;
import titli.model.Database;
import titli.model.RDBMSReader;
import titli.model.Table;
import titli.model.TitliException;
import titli.model.search.Match;
import titli.model.search.ResultGroup;



/**
 * the class that will fetch the actual matching records from the databases
 * the future of Fetcher depends on what kind of outputs are expected
 * @author Juber Patel
 *
 */
public class Fetcher
{
	private RDBMSReader reader;
	private Statement fetchstmt;
	
	/**
	 * 
	 * @param dbReader the reader for which the fetcher should be built
	 * @throws TitliFetchException if problems occur in fetch connection
	 */
	public Fetcher(RDBMSReader dbReader) throws TitliFetchException
	{
		reader = dbReader;
		try
		{
			fetchstmt = reader.getFetchConnection().createStatement();
		}
		catch(SQLException e)
		{
			throw new TitliFetchException("TITLI_S_013", "Problem getting fetch Statement", e);
			
		}
		
		
		
	}

	
	/**
	 *  
	 * @return the corresponding database
	 * @throws TitliException if problems occur
	 */
	public Database getDatabase() throws TitliException
	{
		return reader.getDatabase();
	}
	
	
	/**
	 * fetch the actual records from the database
	 * @param match the match for which to fetch the records
	 * @return the record corresponding to the specified match
	 * @throws TitliFetchException if problems occur
	 * 
	 */
	public Record fetch(Match match) throws TitliFetchException 
	{
		
		Record record = null;
		ResultSet rs=null;		
			
		try
		{
			long start = new Date().getTime();
			
			
			Table table = (Table)getDatabase().getTable(match.getTableName());
			
			//System.out.println(match.getQueryString());
			rs = fetchstmt.executeQuery(match.getQueryString());
			rs.next();
			
			Map<Column, String> columnMap = getColumnMap(rs, table);
			
			long end = new Date().getTime();
			double time = (end-start)/1000.0;
			
			//create the record
			record = new Record(table, columnMap, time); 
			
		}
		catch(SQLException e)
		{
			throw new TitliFetchException("TITLI_S_014", "Problem in fetching records", e);
		}
		catch(TitliException e)
		{
			throw new TitliFetchException("TITLI_S_015", "Problem in getting Database object", e);
		}
		finally
		{
			try 
			{
				rs.close();
			}
			catch (SQLException e) 
			{
				throw new TitliFetchException("00", "problem closing result set", e);
			}
		}
		return record;
		
	}	
			
	
	/**
	 * 
	 * @param group the result group
	 * @return the list of records for the given result group
	 * @throws TitliFetchException if problems occur
	 */
	public RecordList fetch(ResultGroup group) throws TitliFetchException
	{
		RecordList recordList=null;
		
		ArrayList<Record> records = new ArrayList<Record>(); 
		
		long start = new Date().getTime();
		
		ResultSet rs = null;
		try
		{
			
			Table table = (Table)getDatabase().getTable(group.getTableName());
			
			//System.out.println(group.getQueryString());
			rs = fetchstmt.executeQuery(group.getQueryString());
						
			while(rs.next())
			{
				
				Map<Column, String> columnMap = getColumnMap(rs, table);
				
				//add the record to the list
				records.add(new Record(table, columnMap, 0));
			}
			
			long end = new Date().getTime();
			double time = (end-start)/1000.0;
			
			
			recordList = new RecordList(group.getTableName(),records,time);
			
			//System.out.println("\n");
			rs.close();
			
		}
		catch(SQLException e)
		{
			throw new TitliFetchException("TITLI_S_016", "Problem in fetching records", e);
		}
		catch(TitliException e)
		{
			throw new TitliFetchException("TITLI_S_017", "Problem in getting Database object", e);
		}
		finally
		{
			try 
			{
				rs.close();
			}
			catch (SQLException e) 
			{
				throw new TitliFetchException("00", "problem closing result set", e);
			}
		}
		
		
		return recordList;
		
	}
	
	
	
	/**
	 * get the column map corresponding to the current row of the resultset
	 * @param rs the result set
	 * @param table the table
	 * @return the column map
	 * @throws TitliFetchException if SQLException occurs
	 */
	private Map<Column, String> getColumnMap(ResultSet rs, Table table) throws TitliFetchException
	{
		Map<Column, String> columnMap = new LinkedHashMap<Column, String>();
		
		ResultSetMetaData rsmd = null;
		
		try
		{
			rsmd = rs.getMetaData();
		} 
		catch (SQLException e) 
		{
			throw new TitliFetchException("TITLI_S_018", "Unable to get metadata", e);
		}
		
		int columns = 0;
		try
		{
			columns = rsmd.getColumnCount();
		} 
		catch (SQLException e)
		{
			throw new TitliFetchException("TITLI_S_019", "Unable to get column count", e);
		}
		
		//System.out.println("Database : "+match.getDatabaseName()+"  Table : " +match.getTableName());
		
		//populate the column map
		for(int i=1; i<=columns; i++)
		{
			//System.out.print(rsmd.getColumnName(i)+" : "+rs.getString(i)+"  ");
			
			String columnName = null;
			try
			{
				columnName = rsmd.getColumnName(i);
			} 
			catch (SQLException e) 
			{
				throw new TitliFetchException("TITLI_S_020", "Unable to get name for column "+i, e);
			}
							
			Column column = (Column)table.getColumn(new Name(columnName));
			
			try 
			{
				columnMap.put(column, rs.getString(i));
			}
			catch (SQLException e) 
			{
				throw new TitliFetchException("TITLI_S_021", "Unable to get value for column "+i, e);
			}
		}
		
		return columnMap;
		
	}

}

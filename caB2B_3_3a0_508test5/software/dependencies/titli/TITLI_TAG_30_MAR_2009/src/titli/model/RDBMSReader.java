/**
 * 
 */
package titli.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import titli.controller.Name;



/**
 * The class that handles the connections to the database etc.
 * It has the index, search and fetch methods 
 * @author Juber Patel
 *
 */
public class RDBMSReader 
{
	/**
	 * the url to connect to the database. It contains the IP,port and database name
	 */
	private String url;
	
	/**
	 * the type of the database like oracle, mysql etc
	 */
	private String dbType;
	
	/**
	 * the database name
	 */
	private Name dbName;
	
	/**
	 * the user name
	 */
	private String username;
	
	/**
	 * the password
	 */
	private String password;
	
	/**
	 * the connection used by the Indexer
	 */
	private Connection indexConnection;
	
	/**
	 * the connection used by the Fetcher
	 */
	private Connection fetchConnection;
	
	/**
	 * the database object on which this RDBMSReader works
	 */
	private Database database;
	
	/**
	 * the tables with these prefixes should not be indexed or fetched
	 */
	private List<Name> invisiblePrefixes;
	
	/**
	 * the tables with these names should not be indexed or fetched
	 */
	private List<Name> invisibleTables;
	
	
	/**
	 * 
	 * @param props the properties file containing information for connection to database
	 * @throws TitliException if problems occur 
	 *
	 *
	 */
	public RDBMSReader(Properties props) throws TitliException 
	{
		initSQL(props);
		initInvisibleTablesLists(props);
		
	}
	
	
	/**
	 * 
	 * @return the database for the reader
	 * @throws TitliException if problems occur
	 */
	public Database getDatabase() throws TitliException
	{
		//build the database metadata
		if(database==null)
		{
			Map<Name, Table> tables = new LinkedHashMap<Name, Table> ();
			
			try
			{
				DatabaseMetaData dbmd = indexConnection.getMetaData();
				Statement stmt = indexConnection.createStatement();
				List<Name> tableList = getTables(indexConnection);
							
				//for each table
				for(Name tableName : tableList)
				{
					List<Name> uniqueKey = new ArrayList<Name>();
					Map<Name, Column> columns = new LinkedHashMap<Name, Column>();
					
					//get unique keys
					ResultSet keys = dbmd.getPrimaryKeys(null, null, tableName.toString());
					
					//add unique keys
					while(keys.next())
					{
						String key = keys.getString("COLUMN_NAME");
						uniqueKey.add(new Name(key));
						
					}
					
					keys.close();
					
					if(uniqueKey.size()==0)
					{
						System.out.println("table  "+tableName+" does not have unique key ! Skipping...");
						continue;
					}
					
					//fire a dummy query to get table metadata
					//String query = "select * from "+tableName+" where "+uniqueKey.get(0)+" = null";
					//System.out.println(query);
					try
					{
						String query = "select * from "+tableName+" where "+uniqueKey.get(0)+" =null";
						System.out.println(query);
						ResultSet useless = stmt.executeQuery(query);
						
						ResultSetMetaData tablemd = useless.getMetaData();
						
						int numcols = tablemd.getColumnCount();
						//for each column
						for(int i=1;i<=numcols;i++)
						{
							Name columnName = new Name(tablemd.getColumnName(i));
							String columnType = tablemd.getColumnTypeName(i);
							
							columns.put(columnName, new Column(columnName, columnType, tableName));
							
						}
						
						useless.close();
						
						//add the table to the list
						tables.put(tableName, new Table(tableName, dbName, uniqueKey, columns));
					}
					catch(SQLException e)
					{
						System.out.println("problem with "+tableName);
					}
						
				}
				
				stmt.close();
			
				database = new Database(dbName, tables);
				
				//System.out.println("Meta Data created  No of Tables : "+database.getNumberOfTables());
				
			}
			catch(SQLException e)
			{
				throw new TitliException("TITLI_S_002", "SQL problem while creating Database object for "+dbName, e);
			}
			
			
		}
		
		return database;
	}
	
	
	/**
	 * return the connection to be used by an indexer
	 * @return the connection
	 */
	public Connection getIndexConnection()
	{
		return indexConnection;
	}
	
	/**
	 * return the connection to be used by a fetcher
	 * @return the connection
	 * 
	 */
	public Connection getFetchConnection()
	{
		return fetchConnection;
	}
	
	
	
	
		
	
	/**
	 * close the database connections
	 * 
	 */
	protected void finalize() 
	{
		try
		{
			indexConnection.close();
			fetchConnection.close();
		}
		catch(SQLException e)
		{
			System.out.println("SQL Exception while trying to close index and search connections in Database.finalize() !");
			e.printStackTrace();
		}
			
	}
	
	
	/**
	 * initialise the database related parts of this object
	 * @param props the properties
	 * @throws TitliException if problems occur in sql connections
	 */
	private void initSQL(Properties props) throws TitliException
	{
		dbName = new Name(props.getProperty("jdbc.database"));
		
		dbType = props.getProperty("jdbc."+dbName+".type");
		url = props.getProperty("jdbc."+dbName+".url");
		username = props.getProperty("jdbc."+dbName+".username");
		password = props.getProperty("jdbc."+dbName+".password");
		
			
		//System.out.println("Reader created for "+dbName+"...");
	
		try
		{
			indexConnection = DriverManager.getConnection(url, username, password);
			fetchConnection = DriverManager.getConnection(url, username, password);
			//System.out.println("Connection to the database successful...");
			
			
		}
		catch(SQLException e)
		{
			throw new TitliException("TITLI_S_001", "SQL Connection problems while trying to get index and search connections ", e);
		}
	}
	
	
	/**
	 * initialise the lists of invisible tables and invisible table prefixes 
	 * @param props the properties 
	 */
	private void initInvisibleTablesLists(Properties props)
	{
		invisiblePrefixes = new ArrayList<Name>();
		invisibleTables = new ArrayList<Name>();
		
		//populate lists of tables NOT to be indexed
		Scanner s = new Scanner(props.getProperty("titli."+dbName+".noindex.prefix"));
		
		s.useDelimiter(TitliConstants.PROPERTIES_FILE_DELIMITER_PATTERN);
		while(s.hasNext())
		{
			Name tablePrefix = new Name(s.next());
			//add both upper case and lower case names to avoid case-insensitivity problem
			invisiblePrefixes.add(tablePrefix);
			//invisiblePrefixes.add(tablePrefix.toUpperCase());
			//invisiblePrefixes.add(tablePrefix.toLowerCase());
		}
		
		s = new Scanner(props.getProperty("titli."+dbName+".noindex.table"));
		s.useDelimiter(TitliConstants.PROPERTIES_FILE_DELIMITER_PATTERN);
		while(s.hasNext())
		{
			Name tableName = new Name(s.next());
			//add both upper case and lower case names to avoid case-insensitivity problem
			invisibleTables.add(tableName);
			//invisibleTables.add(tableName.toUpperCase());
			//invisibleTables.add(tableName.toLowerCase());
		}
		
	}
	
	
	/**
	 * whether the given table is visible to TiTLi
	 * @param tableName the table
	 * @return true if this table is to be indexed and searched otherwise false
	 */
	private boolean isVisible(Name tableName)
	{
		if(invisibleTables.contains(tableName))
		{
			return false;
		}
		
		for(Name prefix : invisiblePrefixes)
		{
			if(tableName.startsWith(prefix))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * get a list of table names in the database
	 * @param connection the connection to the database
	 * @return a list of table names in the database
	 * @throws TitliException if problems exist
	 */
	private List<Name> getTables(Connection connection) throws TitliException
	{
		List<Name> tableList = new ArrayList<Name>();
		try
		{
			//special case for oracle; the common code does not work
			if(dbType.equals(TitliConstants.DBTYPE_ORACLE))
			{
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("select TABLE_NAME from USER_TABLES");
				while(rs.next())
				{
					Name tableName = new Name(rs.getString("TABLE_NAME"));
					
					//	ignore invisible tables
					if(!isVisible(tableName))
					{
						continue;
					}
					
					tableList.add(tableName);
				}
				
				rs.close();
				stmt.close();
				
			}
			else
			{
				DatabaseMetaData dbmd = connection.getMetaData();
				ResultSet rs = dbmd.getTables(null, null, null, new String[] {"TABLE"});
				
				while(rs.next())
				{
					Name tableName = new Name(rs.getString("TABLE_NAME"));
					
					//	ignore invisible tables
					if(!isVisible(tableName))
					{
						continue;
					}
					
					tableList.add(tableName);
				}
				
				rs.close();
			}
			
		}
		catch(SQLException e)
		{
			throw new TitliException("TITLI_S_001", "SQL Connection problems while trying to get list of tables ", e);
		}
			
		return tableList;
	}
	
	
	
	/**
	 * @param args args to main
	 */
	public static void main(String[] args)
	{
		
		
		//do not open unless you want to index !
		
		
		
		//r.fetch(r.search("Pari?"));
		//r.fetch(r.search("temple"));
		//r.fetch(r.search("istan*"));
		//r.fetch(r.search("Istanbul"));
		//r.fetch(r.search("P*tan"));
		//r.fetch(r.search("I????"));
		//r.fetch(r.search("8796"));
		//r.fetch(r.search( "temple  AND NOT table:actor"  ));
		//r.fetch(r.search("Pari~"));
		//r.fetch(r.search("+new AND NOT table:(city OR film)" ));
		
		
		//r.fetch(r.search("ajay*"));
		
		

	}

}


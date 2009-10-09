/**
 *this package is the top level package 
 */
package titli.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import titli.controller.Name;
import titli.controller.interfaces.DatabaseInterface;
import titli.controller.interfaces.IndexRefresherInterface;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.ResultGroupInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.fetch.Fetcher;
import titli.model.fetch.TitliFetchException;
import titli.model.index.IndexRefresher;
import titli.model.index.Indexer;
import titli.model.search.MatchList;
import titli.model.search.Searcher;
import titli.model.search.TitliSearchException;


/**
 * The main runner class. It's a singleton
 * @author Juber Patel
 *
 */
public final class Titli implements TitliInterface
{
	/**
	 * the only instance of this class
	 */
	private static Titli instance;
	
	/**
	 * a map of "database name" => "database object"
	 */
	private Map<Name, Database> databases;
	
	/**
	 * a map of "database name" => "indexer object" 
	 */
	private Map<Name ,Indexer> indexers;
	
	/**
	 * a map of "database name" => "fetcher object"
	 */
	private Map<Name, Fetcher> fetchers;
	
	
	/**
	 * the index refresher used to refresh indexes
	 */
	private IndexRefresherInterface indexRefresher;
	
	File indexLocation;
	
	
	/**
	 * private constructor for singleton behavaiour
	 * @throws TitliException if problems occur  
	 */
	private Titli() throws TitliException
	{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(TitliConstants.PROPERTIES_FILE);
		Properties props = new Properties();
		
		try
		{
			props.load(in);
			in.close();
		}
		catch(IOException e)
		{
			throw new TitliException("TITLI_S_001", "I/O problem while opening titli.properties", e);
		}
		
		initSystemProperties(props);
		
		initResources(props);
		
		//set the column references
		//setReferences("world41", "E:/juber/workspace/TiTLi/titli/model/world_joins");
		//setReferences("sakila", "E:/juber/workspace/TiTLi/titli/model/sakila_joins");
		
	}
	
	
	/**
	 * 
	 * @return the only instance of Titli
	 * @throws TitliException if problems occur
	 */
	public static synchronized TitliInterface getInstance() throws TitliException
	{
		
		if(instance==null)
		{
			instance = new Titli();
		}
		
		return instance;
	}
	
	
	/**
	 * Get the number of databases 
	 * @return the number of databases
	 */
	public int getNumberOfDatabases()
	{
		return databases.size();
	}
	
	
	/**
	 * 
	 * @return the corresponding database
	 */
	public Map<Name, DatabaseInterface> getDatabases()
	{
		return new LinkedHashMap<Name, DatabaseInterface>(databases);
	}

	
	/**
	 * Get the database specified byname 
	 * @param dbName the name of the database
	 * @return the database
	 */
	public DatabaseInterface getDatabase(Name dbName)
	{
		return databases.get(dbName);
	}
	
	/**
	 * get the index location as found in the property file
	 * @return the index location as found in the property file
	 */
	public File getIndexLocation()
	{
		return indexLocation;
	}
	
	
	/**
	 * index all the databases from the scratch
	 * @throws TitliException if problems occur
	 *
	 */
	public void index() throws TitliException
	{
		//index all databases
		for(Name dbName : databases.keySet())
		{
			//Database db = databases.get(dbName);
			
			//System.out.println("Creating indexer for "+dbName);
			Indexer indexer = indexers.get(dbName);
			indexer.index();
		}
		
	}
	
	
	/**
	 * index from the scratch the specified database
	 * @param databaseName the database name
	 * @throws TitliException if problems occur
	 */
	public void index(Name databaseName) throws TitliException
	{
		Indexer indexer = indexers.get(databaseName);
		indexer.index();
		
	}
	
	
	/**
	 * index from the scratch the specified table of the specified database
	 * @param databaseName the database name
	 * @param tableName the table name
	 * @throws TitliException if problems occur
	 */
	public void index(Name databaseName, Name tableName) throws TitliException
	{
		Indexer indexer = indexers.get(databaseName);
		indexer.index(tableName);
		
	}
	
	
	/**
	 * 
	 * @param query the search string for which the search is to be performed
	 * @return the list of matches found
	 * @throws TitliException if problems occur
	 */
	public MatchListInterface search(String query) throws TitliException
	{
			Searcher searcher = new Searcher(databases, fetchers);
				
			MatchList matches =searcher.search(query);
			searcher.close();
			
			return matches;
	}
	
	
	/**
	 *get the index refresher 
	 * @return the index refresher
	 */
	public IndexRefresherInterface getIndexRefresher()
	{
		return indexRefresher;
	}
	
	
	/**
	 * initialise System properties that should be avilable everywhere
	 * @param props the properties
	 */
	private void initSystemProperties(Properties props)
	{
		//set system properties
		System.setProperty(TitliConstants.JDBC_DRIVERS, props.getProperty(TitliConstants.JDBC_DRIVERS));
				
	}
	
	
	/**
	 * initialise the maps, lists etc. that will be used for titli
	 * @param props the properties
	 * @throws TitliException if problems occur
	 */
	private void initResources(Properties props) throws TitliException
	{

		//initialize the class fields 
		databases = new LinkedHashMap<Name, Database>();
		indexers = new LinkedHashMap<Name, Indexer> ();
		fetchers = new LinkedHashMap<Name, Fetcher> ();
		indexRefresher = new IndexRefresher(indexers);
		indexLocation = new File(props.getProperty(TitliConstants.TITLI_INDEX_LOCATION));
		
		//read database names
		Scanner s =new Scanner(props.getProperty(TitliConstants.JDBC_DATABASES));
		s.useDelimiter(TitliConstants.PROPERTIES_FILE_DELIMITER_PATTERN);
		while(s.hasNext())
		{
			Name dbName = new Name(s.next());
			
			//make db reader
			RDBMSReader reader = createRDBMSReader(dbName, props);
			
			//create Indexer for the reader
			indexers.put(dbName, new Indexer(reader));
			
			//create Fetcher for the reader
			fetchers.put(dbName, new Fetcher(reader));
			
			//add DatabaseInterface to the list
			databases.put(dbName, reader.getDatabase());
		}
		
	}

	
	/**
	 * create an RDBMSReader for given name and properties
	 * @param dbName name of the database
	 * @param props the properties related to the reader
	 * @return the newly created RDBMSReader
	 * @throws TitliException if problems occur
	 */
	private RDBMSReader createRDBMSReader(Name dbName, Properties props) throws TitliException
	{
		Properties dbProps = new Properties();
		
		String propName = "jdbc.database";
		dbProps.setProperty(propName, dbName.toString());
				
		propName = "jdbc."+dbName+".type";
		dbProps.setProperty(propName, props.getProperty(propName));
		
		propName = "jdbc."+dbName+".url";
		dbProps.setProperty(propName, props.getProperty(propName));
		
		propName = "jdbc."+dbName+".username";
		dbProps.setProperty(propName, props.getProperty(propName));
		
		propName = "jdbc."+dbName+".password";
		dbProps.setProperty(propName, props.getProperty(propName));
		
		propName = "titli."+dbName+".noindex.prefix";
		dbProps.setProperty(propName, props.getProperty(propName));
		
		propName = "titli."+dbName+".noindex.table";
		dbProps.setProperty(propName, props.getProperty(propName));
		
		return new RDBMSReader(dbProps);
		
		
	}
	
	
	/**
	 * 
	 * @param dbName the name of the database
	 * @param location the location of joins file
	 * @throws TitliException if problems occur
	 *
	 */
	private void setReferences(Name dbName, String location) throws TitliException
	{
		Database db = (Database)getDatabase(dbName);
		
		Scanner scanner=null;
		
		try
		{
			scanner = new Scanner(new File(location));
		}
		catch (FileNotFoundException e)
		{
			throw new TitliException("UNDEFINED", "the joins file not found : "+location, e);
		}
		
		while(scanner.hasNext())
		{
			String first = scanner.next();
			String second = scanner.next();
			
			int dot = first.indexOf(".");
			
			Name firstTable = new Name(first.substring(0,dot));
			Name firstColumn = new Name (first.substring(dot+1));
			
			
			dot = second.indexOf(".");
			
			Name secondTable = new Name(second.substring(0,dot));
			Name secondColumn = new Name(second.substring(dot+1));
			
			Column c1 = (Column)db.getTable(firstTable).getColumn(firstColumn);
			
			c1.setReferredColumn((Column)db.getTable(secondTable).getColumn(secondColumn));
			
			
					
		}
		
	}
	
	
	
	
	
	/**
	 * 
	 * @param args args for main
	 * @throws TitliException if problems occur
	 * 
	 */
	public static void main(String[] args) throws TitliException
	{
		TitliInterface titli=null;
		try
		{
			titli = Titli.getInstance();
		}
		catch(TitliException e)
		{
			System.out.println(e+"\n"+e.getCause());
		}
		
		
		
		long start = new Date().getTime();
		
		
		/*
		try 
		{
			titli.index("catissuecore41");
		}
		catch (TitliIndexException e) 
		{
			System.out.println(e+"\n"+e.getCause());
		}
		*/
		
		long end = new Date().getTime();
		
		
		System.out.println("Indexing took "+(end-start)/1000.0+" seconds");
		
		
		MatchListInterface  matchList=null;
		try 
		{
			
			matchList =titli.search("f*");
		}
		catch (TitliSearchException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  //AND (table:(+countrylanguage))");
		
		//MatchListInterface  matchList =titli.search("kalmykia");
		
		
		
		start = new Date().getTime();
		
		//MatchListInterface  matchList =titli.search("+united +states ");
		
		end = new Date().getTime();
		
		for(Map.Entry<Name, ResultGroupInterface> e : matchList.getSortedResultMap().entrySet())
		{
			//if(e.getKey().equals("catissue_participant"))
			//{	
				try
				{
					System.out.println(e.getValue().fetch());
				}
				catch (TitliFetchException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//}
			
			
		}
		
		System.out.println("\n\nMatches : "+matchList.size()+"   Time : "+matchList.getTimeTaken()+" seconds   Time :  "+(end-start)/1000.0);
		
		
		//Fetcher.fetch(titli.search("Temple"),titli.dbReaders);
		//Fetcher.fetch(titli.search("ajay"),titli.dbReaders);
		//Fetcher.fetch(titli.search("pari~"),titli.dbReaders);
		
		//MatchList matchList = titli.search("ajay");
		//titli.fetch(matchList);
		
		//querying a remote databse : cab2b on Vishvesh's machine
		//MatchList matchList = titli.search("1298_1150_1372");
		//titli.fetch(matchList);
		
		//MatchList matchList = titli.search("tilburg");
		//titli.fetch(matchList);
		
	}
	

}

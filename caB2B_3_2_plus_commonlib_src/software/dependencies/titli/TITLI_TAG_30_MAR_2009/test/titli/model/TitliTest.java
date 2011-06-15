/**
 * 
 */
package titli.model;


import static org.junit.Assert.*;


import java.io.File;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import titli.controller.Name;
import titli.controller.interfaces.DatabaseInterface;
import titli.controller.interfaces.MatchInterface;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.ResultGroupInterface;
import titli.controller.interfaces.SortedResultMapInterface;
import titli.controller.interfaces.TableInterface;
import titli.controller.interfaces.TitliInterface;
import titli.controller.interfaces.record.RecordInterface;
import titli.model.util.IndexUtility;

//Eclipse is using JUnit4.1. No need to run from outside !
//And @Ignore Tag is also working !!


/**
 * The class to test the Titli Model functionality
 * @author Juber Patel
 *
 */
public class TitliTest
{

	private static TitliInterface titli;
	
	/**
	 * @throws TitliException if
	 *  
	 */
	@BeforeClass
	public static void beforeClass() throws TitliException 
	{
		titli = Titli.getInstance();
	}
	
	
	/**
	 * 
	 */
	@AfterClass
	public static void afterClass()  
	{
		
	}

	
	
	
	/**
	 * test the set parameters, variables and properties
	 */
	@Test
	public void constructorTest()
	{
		assertNotSame("No databases read !!", titli.getNumberOfDatabases(),0);
		
		assertNotNull("JDBC Drivers String Empty !!", System.getProperty(TitliConstants.JDBC_DRIVERS));
		
		//assertNotNull("Index Location String Empty !!", System.getProperty(TitliConstants.TITLI_INDEX_LOCATION));
		
		
	}
	
	
	/**
	 * test the indexing functionality
	 * @throws TitliException if
	 *
	 */
	//@Ignore("Don't do it everytime !")
	@Test
	public void indexTest() throws TitliException
	{
		titli.index();
		
		//check if directories have been created for all databases and tables
		
		Map<Name, DatabaseInterface>databases = titli.getDatabases();
		//for each database
		for(Name dbName : databases.keySet())
		{
			File dbDir = IndexUtility.getIndexDirectoryForDatabase(dbName);
			assertTrue("index directory for database "+dbName+" does not exist !!", dbDir.exists());
			
			Map<Name, TableInterface> tables = titli.getDatabase(dbName).getTables();
			//for each table
			for(Name tableName : tables.keySet())
			{
				File tableDir = IndexUtility.getIndexDirectoryForTable(dbName, tableName);
				assertTrue("index directory for table "+tableName+" in database "+dbName+" does not exist !!", tableDir.exists());
				
			}
		}
		
	}
	
	/**
	 * test search functionality
	 * @throws TitliException if
	 *
	 */
	@Test
	public void searchTest() throws TitliException
	{
		MatchListInterface matchList = titli.search("m*");
		for(MatchInterface match : matchList)
		{
			System.out.println(match);
		}
	}
	
	/**
	 * test fetching functionality
	 * @throws TitliException if
	 */
	@Test
	public void fetchTest() throws TitliException
	{
			MatchListInterface matchList = titli.search("m*");
			SortedResultMapInterface sortedResult = matchList.getSortedResultMap();
			for(ResultGroupInterface group : sortedResult.values())
			{
				System.out.println(group.fetch());
			}
		
	}
	
	
	/**
	 * 
	 * test the search and fecth functionality
	 * @throws TitliException if
	 * 
	 */
	@Test(timeout=5000)
	public void searchAndFetchTest() throws TitliException 
	{
		//MatchList matchList =titli.search("new +bombay");
		//titli.fetch(matchList);
		
		//Fetcher.fetch(titli.search("Temple"),titli.dbReaders);
		//Fetcher.fetch(titli.search("ajay"),titli.dbReaders);
		//Fetcher.fetch(titli.search("pari~"),titli.dbReaders);
		
		MatchListInterface matchList;
		matchList = titli.search("new +bombay");
	
		for(MatchInterface match : matchList)
		{
			RecordInterface record = match.fetch();
			
			System.out.println(record);
		}
	
		
		//querying a remote databse : cab2b on Vishvesh's machine
		//MatchList matchList = titli.search("1298_1150_1372");
		//titli.fetch(matchList);
		
		//MatchList matchList = titli.search("tilburg");
		//titli.fetch(matchList);
		
	}
	
	
}

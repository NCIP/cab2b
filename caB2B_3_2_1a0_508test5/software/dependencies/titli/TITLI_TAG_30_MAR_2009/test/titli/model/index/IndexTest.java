/**
 * 
 */
package titli.model.index;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import titli.controller.Name;
import titli.controller.interfaces.DatabaseInterface;
import titli.controller.interfaces.TableInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.IndexUtility;

/**
 * @author Juber Patel
 *
 */
public class IndexTest
{
	private static TitliInterface titli;
	
	/**
	 * 
	 * @throws TitliException if
	 */
	@BeforeClass
	public static void beforeClass() throws TitliException
	{
		titli = Titli.getInstance();
	}
	
	/**
	 * 
	 *
	 */
	@AfterClass
	public static void afterClass()
	{
		
	}
	
	
	/**
	 * index all the databases mentioned in titli.properties
	 * @throws TitliException if
	 *
	 */
	@Test
	public void indexAllDatabases() throws TitliException
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
	 * index a valid database
	 * @throws TitliException if
	 * 
	 *
	 */
	@Test
	public void indexValidDatabase() throws TitliException
	{
		Name dbName = new Name("catissuecore41");
		titli.index(dbName);
				
		//check if directories for all indexable tables have been created
			
		Map<Name, TableInterface> tables = titli.getDatabase(dbName).getTables();
		//for each table
		for(Name tableName : tables.keySet())
		{
			File tableDir = IndexUtility.getIndexDirectoryForTable(dbName, tableName);
			assertTrue("index directory for table "+tableName+" in database "+dbName+" does not exist !!", tableDir.exists());
			
		}
	}
	
	
	/**
	 * index an invalid database
	 * @throws TitliException if
	 *
	 */
	@Test(expected=NullPointerException.class)
	public void indexInvalidDatabase() throws TitliException
	{
		Name dbName = new Name("garbage");
		
		try
		{
			titli.index(dbName);
		}
		//the index direcotry must not exist for this database
		finally
		{
			File dbDir = IndexUtility.getIndexDirectoryForDatabase(dbName);
			assertFalse("directory created for invalid database "+dbName+" !!",dbDir.exists());
		}
	
	}
	
	
	/**
	 * index a valid table 
	 * @throws TitliException if
	 * 
	 */
	@Test
	public void indexValidTable() throws TitliException
	{
		Name dbName = new Name("catissuecore41");
		Name tableName = new Name("CATISSUE_INSTITUTION"); 
		titli.index(dbName, tableName);
		
		File tableDir = IndexUtility.getIndexDirectoryForTable(dbName, tableName);
		assertTrue("index directory for table "+tableName+" in database "+dbName+" does not exist !!", tableDir.exists());
		
	}
	
	
	/**
	 * index an invalid table 
	 * @throws TitliException if
	 * 
	 */
	@Test(expected=NullPointerException.class)
	public void indexInvalidTable() throws TitliException
	{
		Name dbName = new Name("catissuecore11");
		Name tableName = new Name("catissue_garbage"); 
		
		try
		{
			titli.index(dbName, tableName);
		}
		//the index direcotry must not exist for this table
		finally
		{
			File tableDir = IndexUtility.getIndexDirectoryForTable(dbName, tableName);
			assertFalse("directory created for invalid table "+tableName+" in database "+dbName+" !!", tableDir.exists());
		}
	
	}
	
	
}

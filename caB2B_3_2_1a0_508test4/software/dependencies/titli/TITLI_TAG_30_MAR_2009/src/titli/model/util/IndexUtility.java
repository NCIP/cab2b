/**
 * 
 */
package titli.model.util;

import java.io.File;

import titli.controller.Name;
import titli.model.Titli;
import titli.model.TitliConstants;
import titli.model.TitliException;

/**
 * @author Juber Patel
 *
 */
public class IndexUtility 
{
	/**
	 * get the directory corresponding to the index of the specified table
	 * @param dbName the database name
	 * @param tableName the table name
	 * @return the File object corresponding to the table index directory 
	 * @throws TitliException if problems occur
	 */
	public static File getIndexDirectoryForTable(Name dbName, Name tableName) throws TitliException
	{
		File indexDir = Titli.getInstance().getIndexLocation();
		
		File dbDir = new File(indexDir, dbName+TitliConstants.INDEX_DIRECTORY_SUFFIX);
		return new File(dbDir, tableName.toString().toLowerCase()+TitliConstants.INDEX_DIRECTORY_SUFFIX);
		
	}
	
		
	/**
	 * get the directory corresponding to the index of the specified database
	 * @param dbName the database name
	 * @return the File object corresponding to the table index directory 
	 * @throws TitliException if problems occur
	 */
	public static File getIndexDirectoryForDatabase(Name dbName) throws TitliException
	{
		File indexDir = Titli.getInstance().getIndexLocation();
		deleteIndexDirectories(indexDir);
		return new File(indexDir, dbName+TitliConstants.INDEX_DIRECTORY_SUFFIX);
	}
	
	/**
	 * Delete the index directories that are already present
	 * @param indexDir
	 */
	private static void deleteIndexDirectories(File indexDir)
	{
		File[] listOfFiles = indexDir.listFiles();
		if(listOfFiles != null)
		{
			for(int i=0;i<listOfFiles.length;i++)
			{
				if(listOfFiles[i].isDirectory())
				{
					deleteIndexDirectories(listOfFiles[i]);
				}
				else
				{
					listOfFiles[i].delete();
				}
			}
		}
		indexDir.delete();
	}


	/**
	 * @param args args for main
	 * @throws TitliException if problems occur
	 */
	public static void main(String[] args) throws TitliException 
	{
		try
		{
			Titli.getInstance();
		}
		catch (TitliException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(IndexUtility.getIndexDirectoryForDatabase(new Name("db4")));

	}

}

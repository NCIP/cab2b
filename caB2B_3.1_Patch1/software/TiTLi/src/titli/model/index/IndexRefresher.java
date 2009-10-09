/**
 * 
 */
package titli.model.index;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;

import titli.controller.Name;
import titli.controller.RecordIdentifier;
import titli.controller.interfaces.IndexRefresherInterface;
import titli.controller.interfaces.MatchInterface;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.IndexUtility;

/**
 * @author Juber Patel
 *
 */
public class IndexRefresher implements IndexRefresherInterface 
{
	
	private Map<Name, Indexer> indexers;
	
	/**
	 * default constructor
	 * @param indexers the map of indexers
	 * @throws TitliException if problems occur
	 *
	 */
	public IndexRefresher(Map<Name, Indexer> indexers) throws TitliException
	{
		this.indexers = indexers;
	}
	
	
	/**
	 * insert the record identified by parameters into the index
	 * the unique key for the table
	 * @param identifier the record identifier
	 * @throws TitliException if problems occur
	 */
	public void insert(RecordIdentifier identifier) throws TitliException
	{
		Indexer indexer = indexers.get(identifier.getDbName());
		try 
		{
			indexer.index(identifier.getTableName(), identifier.getUniqueKey());
		}
		catch (TitliIndexException e) 
		{
			throw new TitliIndexRefresherException("TITLI_S_030", "problem while indexing record for database  :"+identifier.getDbName()+" table : "+identifier.getTableName(), e);
		}
		
	}
	
	/**
	 * update the record identified by parameters from in index
	 * the unique key for the table
	 * @param identifier the record identifier
	 * @throws TitliException if problems occur
	 */
	public void update(RecordIdentifier identifier) throws TitliException
	{
		delete(identifier);
		insert(identifier);
		
	}
	
	
	/**
	 * delete the record identified by parameters from the index 
	 * the unique key for the table
	 * @param identifier the record identifier
	 * @throws TitliException if problems occur
	 */
	public void delete(RecordIdentifier identifier) throws TitliException
	{
		File indexDir = IndexUtility.getIndexDirectoryForTable(identifier.getDbName(), identifier.getTableName());
		
		try
		{
			IndexReader reader = IndexReader.open(indexDir);
			int co = 0;
			for (Name column : identifier.getUniqueKey().keySet())
			{
				Term t = new Term(column.toString(), identifier.getUniqueKey().get(column));
				co += reader.deleteDocuments(t);
			}
			reader.close();
			System.out.println("Document deleted " + co);
		}
		catch (IOException e)
		{
			throw new TitliIndexRefresherException("TITLI_S_030",
			        "problem while creating index reader for database  :" + identifier.getDbName()
			                + " table : " + identifier.getTableName(), e);
		}
	}
	
	
	/**
	 * check if a record with given unique key values already in the index  
	 * @param identifier the record identifier
	 * @return true if this record is already indexed otherwise false
	 * @throws TitliException if problems occur
	 */
	public boolean isIndexed(RecordIdentifier identifier) throws TitliException
	{
		boolean isIndexed=false;
		File indexDir = IndexUtility.getIndexDirectoryForTable(identifier.getDbName(), identifier.getTableName());
		IndexReader reader;
		
		try 
		{
			FSDirectory dir = FSDirectory.getDirectory(indexDir, false);
			reader = IndexReader.open(dir);
		}
		catch (IOException e) 
		{
			throw new TitliIndexRefresherException("TITLI_S_030", "problem while creating index reader for database  :"+identifier.getDbName()+" table : "+identifier.getTableName(), e);
		}
		
		int maxDoc = reader.maxDoc();
		Document doc=null;
		int i;
		
		//find the doc with given columns and values
		for(i=0; i<maxDoc; i++)
		{
			try 
			{
				//ignore documents marked deleted
				if(reader.isDeleted(i))
				{
					continue;	
				}
								
				doc = reader.document(i);
			}
			catch (IOException e) 
			{
				throw new TitliIndexRefresherException("TITLI_S_030", "problem reading document from the index reader for database  :"+identifier.getDbName()+" table : "+identifier.getTableName(), e);
			}
			
			//this is not the doc we are looking for
			if(identifier.matches(doc))
			{
				isIndexed=true;
				break;
			}
			
		}
		
		try 
		{
			reader.close();
		}
		catch (IOException e) 
		{
			throw new TitliIndexRefresherException("TITLI_S_030", "problem closing reader for database  :"+identifier.getDbName()+" table : "+identifier.getTableName(), e);
		}
		
		return isIndexed;
		
		
	}
	
	
	/**
	 * insert the list of records identified the list of Record identifiers 
	 * the unique key for the table
	 * @param identifiers the list record identifiers
	 * @throws TitliException if problems occur
	 */
	public void insert(List<RecordIdentifier> identifiers) throws TitliException
	{
		//check if document already exists in the index
		removeIndexed(identifiers);
		
		for(RecordIdentifier identifier : identifiers)
		{
			insert(identifier);
		}
		
	}
	
	
	/**
	 * remove from the list the record identifiers whose records are already indexed
	 * @param identifiers the list of record identifiers
	 */
	public void removeIndexed(List<RecordIdentifier> identifiers)
	{
		
	}
	

	/**
	 * 
	 * @param args args for main
	 * @throws TitliException if problems occur
	 */
	public static void main(String[] args) throws TitliException
	{
		TitliInterface titli = null;
		IndexRefresherInterface refresher;
		
		
		try
		{
			titli = Titli.getInstance();
			titli.index();
			refresher = titli.getIndexRefresher();
			
			LinkedHashMap<Name, String> uniqueKey = new LinkedHashMap<Name, String>();
			uniqueKey.put(new Name("IDENTIFIER"), "7");
			
			RecordIdentifier identifier = new RecordIdentifier(new Name("db29"), new Name("catissue_specimen"), uniqueKey);
			
			long start = new Date().getTime();
			
			
			refresher.update(identifier);
			
			long end = new Date().getTime();
			
			System.out.println("refresh time : "+(end-start)/1000.0);
		}
		catch (TitliException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		MatchListInterface matchList = null;
		try 
		{
			matchList = titli.search("new york");
			
			for(MatchInterface match : matchList)
			{
				System.out.println(match.fetch());
			}
			
			/*
			SortedResultMapInterface map = matchList.getSortedResultMap();
			System.out.println("Total matches : "+map.size());
			
			for(ResultGroupInterface groupInterface : map.values())
			{
				ResultGroup group = (ResultGroup)groupInterface;
				
				try 
				{
					System.out.println(group.fetch());
					System.out.println(group.getNumberOfMatches());
				}
				catch (TitliFetchException e) 
				{
					System.out.println(e);
				}
				
				
			}*/
		}
		catch (TitliException e)
		{
			System.out.println(e+"\n"+e.getCause());
		}  
		
	}

}

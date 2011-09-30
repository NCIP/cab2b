/**
 * 
 */
package titli.model.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;

import titli.controller.Name;
import titli.controller.interfaces.TableInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Database;
import titli.model.Table;
import titli.model.Titli;
import titli.model.TitliConstants;
import titli.model.TitliException;
import titli.model.fetch.Fetcher;


/**
 * performs search according to given criteria 
 * @author Juber Patel
 *
 */
public class Searcher
{
	private MultiSearcher ms;
	private Map<Name, Fetcher> fetchers;
	
	
	/**
	 * setup a multiseracher for the given databases
	 * @param databases the list of databases to be searched 
	 * @param fetchers a map of fetchers so that appropriate Fetcher is attached to each match
	 * @throws TitliException if problems occur
	 */
	public Searcher(Map<Name, Database> databases, Map<Name, Fetcher> fetchers) throws TitliException
	{
		this.fetchers = fetchers;
		
		initMultiSearcher(databases);
			
	}
	
	
	/**
	 * search for the given query using the index
	 * @param searchString the user query to be searched 
	 * @return a list of matches
	 * @throws TitliSearchException if problems occur
	 * 
	 */
	public MatchList search(String searchString) throws TitliSearchException 
	{
		
		//System.out.println("Searching for " +searchString+"...");
		Analyzer analyzer = new StandardAnalyzer();
				
		QueryParser qp = new QueryParser(TitliConstants.DOCUMENT_CONTENT_FIELD, analyzer);
		Query query=null;
		
		try
		{
			query = qp.parse(searchString);
		}
		catch(ParseException e)
		{
			throw new TitliSearchException("TITLI_S_003", "Serach Query Parsing Exception", e);
		}
		
		
		long start = new Date().getTime();
		
		Hits hits = null;
		
		try
		{
			//search for the query
			hits = ms.search(query);
		}
		catch(IOException e)
		{
			throw new TitliSearchException("TITLI_S_022", "I/O problem ", e);
		}
			
		long end = new Date().getTime();
		
		double time = (end-start)/1000.0;
		
		MatchList matchList = new MatchList(time, fetchers);
		
		int listLength = hits.length();
		
		Fetcher fetcher=null;
		
		//build the match list	
		for(int i=0;i<listLength;i++)
		{
			Document document = null;
			try
			{
				document = hits.doc(i);
			}
			catch(IOException e)
			{
				throw new TitliSearchException("TITLI_S_023", "I/O problem ", e);
				
			}
			
			fetcher = fetchers.get(new Name(document.get(TitliConstants.DOCUMENT_DATABASE_FIELD)));
			
			matchList.add(new Match(document, fetcher));
						
		}
		
		//System.out.println("\n The search took " + (end-start)/1000.0 + " seconds");
		//System.out.println("\n Found "+listLength+" matches");
		
		//System.out.println("\nThe matches are : ");
		
		/*
		for(MatchInterface match : matchList)
		{
			System.out.println(match);
			System.out.println(match.getQuerystring()+"\n");
		}*/
		
		//System.out.println("\n The search took " + (end-start)/1000.0 + " seconds\n");
		
		return matchList;
		
	}

	
	
	/**
	 * close all the searchers
	 * @throws TitliSearchException if problems occur
	 *
	 */
	public void close() throws TitliSearchException
	{
		try
		{
			ms.close();
		}
		catch(IOException e)
		{
			throw new TitliSearchException("TITLI_S_024", "I/O problem ", e);
		}
		
			
	}
	
	
	/**
	 * initialize the multisearcher from the map of databases
	 * @param databases the map of available databases
	 * @throws TitliException if problems occur
	 */
	private void initMultiSearcher(Map<Name, Database> databases) throws TitliException 
	{
		ArrayList<IndexSearcher> searcherList;
		
		try
		{
			TitliInterface titli = Titli.getInstance();
			//the current directory
			File indexDir = titli.getIndexLocation();
			
			//create the multisearcher
			searcherList = new ArrayList<IndexSearcher> (); 
			
			//for each database
			for(Database db : databases.values())
			{
				File dbDir = new File(indexDir, db.getName()+TitliConstants.INDEX_DIRECTORY_SUFFIX);
				
				Map<Name, TableInterface> tables = db.getTables();
				
				//for each table
				for(TableInterface tableInterface :  tables.values())
				{
					Table table = (Table)tableInterface;
					
					FSDirectory fsDir=null;
					try
					{
						File tableDir = new File(dbDir, table.getName().toString().toLowerCase()+TitliConstants.INDEX_DIRECTORY_SUFFIX);
										
						fsDir = FSDirectory.getDirectory(tableDir,false);
						searcherList.add(new IndexSearcher(fsDir));
					}
					catch(IOException e)
					{
						throw new TitliSearchException("TITLI_S_025", "I/O problem involving "+fsDir, e);
					}
						
				}
				
			}
			
			IndexSearcher[] searchers = searcherList.toArray(new IndexSearcher[0]);		
			
			ms = new MultiSearcher(searchers);
		}
		catch(IOException e)
		{
			throw new TitliSearchException("TITLI_S_026", "I/O problem ", e);
		}
		
	}

	
}

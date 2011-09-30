/**
 * 
 */
package titli.model.index;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import titli.controller.Name;
import titli.controller.interfaces.ColumnInterface;
import titli.controller.interfaces.TableInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Column;
import titli.model.Database;
import titli.model.RDBMSReader;
import titli.model.Table;
import titli.model.Titli;
import titli.model.TitliConstants;
import titli.model.TitliException;
import titli.model.util.IndexUtility;
import titli.model.util.TitliIndexMapper;




/**
 * @author Juber Patel
 *
 */
public class Indexer
{
	private RDBMSReader reader;
	private Statement indexstmt;
	private Map<String,Map> documentMap = new Hashtable<String,Map>();
	private final String TITLIDOC = "titliDoc";
	private final String CONTENTLIST = "contentList";
	
	 public Indexer(){}
	/**
	 * 
	 * @param dbReader The RDBMSReader on which to build the Indexer
	 * @throws TitliIndexException if problems occur
	 */ 
	public Indexer(RDBMSReader dbReader) throws TitliIndexException 
	{
		reader = dbReader;
		
		try
		{
		
			//create the database index directory
			indexstmt = reader.getIndexConnection().createStatement();
		}
		catch(SQLException e)
		{
			throw new TitliIndexException("TITLI_S_004", "problem while trying to get index Statement ", e);
			
		}
	}
	
	/**
	 *  
	 * @return the corresponding database
	 * @throws TitliIndexException if problems occur
	 * 
	 */
	public Database getDatabase() throws TitliIndexException 
	{
		try
		{
			return reader.getDatabase();
		}
		catch(TitliException e)
		{
			throw new TitliIndexException("TITLI_S_006", "Problem in getting Database object", e);
		}
		
	}
	
	
	/**
	 * index from the scratch
	 * @throws TitliException if problems occur
	 *
	 */
	public void index() throws TitliException
	{
		File indexDir;
		try
		{
			indexDir = IndexUtility.getIndexDirectoryForDatabase(reader.getDatabase().getName());
		}
		catch (TitliException e) 
		{
			throw new TitliIndexException("TITLI_S_32", "can't get database", e); 
		}
		
		indexDir.mkdirs();
		
		Database database;
		try
		{
			database = reader.getDatabase();
		}
		catch(TitliException e)
		{
			throw new TitliIndexException("TITLI_S_007", "Problem in getting Database object", e);
		}
		
		for(TableInterface table : database.getTables().values())
		{
			documentMap = new Hashtable();
			indexTable((Table)table);	
		}
		
		//long end = new Date().getTime();
		
		//System.out.println("Congrats ! indexing completed successfully !");
		//System.out.println("\nIndexing database "+database.getName()+" took "+(end-start)/1000.0+" seconds");
			
	}
	
	
	/**
	 * index from the sratch the specified table
	 * @param tableName the table name
	 * @throws TitliException if problems occur
	 */
	public void index(Name tableName) throws TitliException
	{
		Database database;
		try
		{
			database = reader.getDatabase();
		}
		catch(TitliException e)
		{
			throw new TitliIndexException("TITLI_S_008", "Problem in getting Database object", e);
		}
		
		Table table = (Table)database.getTable(tableName);
		indexTable(table);
	}

	
	/**
	 * index the record represented by the parameters
	 * @param tableName the table name
	 * @param uniqueKey the map of unique key column => value
	 * @throws TitliIndexException if problems occur
	 */
	public void index(Name tableName, Map<Name, String>uniqueKey) throws TitliIndexException
	{
		Table table =null;
		documentMap = new Hashtable();
		StringBuilder query;
		
		try
		{
			//get the table
			table = (Table)reader.getDatabase().getTable(tableName);
		} 
		catch (TitliException e) 
		{
			throw new TitliIndexException("TITLI", "problem getting Table "+tableName, e);
		}
			
		ResultSet rs = null;
		try
		{
			StringBuilder queryClause = TitliIndexMapper.getInstance().returnJoinMapping(tableName.toString());
			int i= queryClause.lastIndexOf("AND");
			if(i!=-1)
			{
				queryClause.delete(i, i+3);
			}
			
			String uniqueColumnName = new String();
			
			//build the SQL query
			int whereIndex = queryClause.indexOf("WHERE");
			if(whereIndex == -1)
			{
				query = new StringBuilder(queryClause+" where ");
			}
			else
			{
				query = new StringBuilder(queryClause+" AND ");
			}
			String tableAlias = TitliIndexMapper.getInstance().getAliasFor(tableName.toString());
			if(tableAlias == null)
			{
				tableAlias = tableName.toString();
			}
			for(Name column : uniqueKey.keySet())
			{
				uniqueColumnName = column.toString();
				query.append(tableAlias+"."+column+"='"+uniqueKey.get(column)+"' AND ");
			}
			
			//remove last AND
			query.delete(query.lastIndexOf("AND"), query.length());
			query.append(" order by "+tableAlias+"."+uniqueColumnName);
			//execute query
			rs = indexstmt.executeQuery(query.toString());
			
			//if result set is empty just abort
			if(!rs.next())
			{
				rs.close();
				return;
			}
		}
		catch (SQLException e) 
		{
			throw new TitliIndexException("TITLI", "problem executing SQL query on  "+tableName, e);
		}
		catch (Exception e)
		{
			throw new TitliIndexException("TITLI", "problem in execution ", e);
		}
		
		IndexWriter indexWriter;
		
		try
		{
			File tableDir = IndexUtility.getIndexDirectoryForTable(reader.getDatabase().getName(), tableName);
			Directory dir = FSDirectory.getDirectory(tableDir,false);
			indexWriter = new IndexWriter(dir, new StandardAnalyzer(), false);
			
			//make the document
			//Document doc = makeDocument(rs, table,indexWriter);
			
			do
			{
				makeDocument(rs, table,indexWriter);
			}
			while(rs.next());
			Set<String> keySet = documentMap.keySet();
			Iterator<String> iterator = keySet.iterator();
			 if(iterator.hasNext())
			 {
				 String keyString = iterator.next();
				 Map documentValueMap = documentMap.get(keyString);
				 Document document = (Document)documentValueMap.get(TITLIDOC);
				 indexWriter.addDocument(document);
			 }	
			
			//indexWriter.addDocument(doc);
			indexWriter.close();
			dir.close();
			rs.close();
		}
		catch (TitliException e) 
		{
			throw new TitliIndexException("TITLI", "problem getting database  "+tableName, e);
		}
		catch (IOException e) 
		{
			throw new TitliIndexException("TITLI", "problem creating index writer for  "+tableName, e);
		}
		catch (SQLException e) 
		{
			throw new TitliIndexException("TITLI", "SQL problem", e);
		}
		
	}
	
	
	/**
	 * index the given table
	 * @param table the table to be indexed
	 * @throws TitliException if problems occur
	 * 
	 */
	private void indexTable(Table table) throws TitliException 
	{
			
		//long start = new Date().getTime();
		
		File tableIndexDir = IndexUtility.getIndexDirectoryForTable(table.getDatabaseName(), table.getName());
		String query=null;
		
		try
		{
			//RAMDirectory does not have a method to flush to the hard disk ! this is  bad !
			//RAMDirectory indexDir = new RAMDirectory(tableIndexDir);
			Directory dir = FSDirectory.getDirectory(tableIndexDir,true);
			
			//	specify the index directory
			IndexWriter indexWriter = new IndexWriter(dir, new StandardAnalyzer(), true);
			indexWriter.setMergeFactor(TitliConstants.INDEX_MERGE_FACTOR);
			indexWriter.setMaxBufferedDocs(TitliConstants.INDEX_MAX_BUFFERED_DOCS);
			
			//System.out.println("executing :   "+"SELECT * FROM  "+table.getName());
			
			query = getExtendedQuery(table);
			
			ResultSet rs = indexstmt.executeQuery(query);
			
			while(rs.next())
			{
				//this is for compatibility with Nutch Parsers
				//RDBMSRecordParser parser = new RDBMSRecordParser(rs);
				//String content = parser.getParse(new Content()).getText();
				
				//indexWriter.addDocument(makeDocument(rs, table));
				makeDocument(rs,table,indexWriter);		
			}	
			
			Set<String> keySet = documentMap.keySet();
			Iterator<String> iterator = keySet.iterator();
			if(iterator.hasNext())
			{
				String keyString = iterator.next();
				Map documentValueMap = documentMap.get(keyString);
				Document document = (Document)documentValueMap.get(TITLIDOC);
				indexWriter.addDocument(document);
			}
			
			indexWriter.optimize();
			indexWriter.close();
			dir.close();
			
			rs.close();	
			
			IndexReader reader=null;
			try 
			{
				reader = IndexReader.open(tableIndexDir);
			}
			catch (IOException e) 
			{
				//throw new TitliIndexRefresherException("TITLI_S_030", "problem while creating index reader for database  :"+identifier.getDbName()+" table : "+identifier.getTableName(), e);
			}
			
			int maxDoc = reader.maxDoc();
			Document doc=null;
			
			int i;
			
			//find the doc with given columns and values
			for(i=0; i<maxDoc; i++)
			{
				try 
				{					
					doc = reader.document(i);
				}
				catch (IOException e) 
				{
					//throw new TitliIndexRefresherException("TITLI_S_030", "problem reading document from the index reader for database  :"+identifier.getDbName()+" table : "+identifier.getTableName(), e);
				}
			}
				
		}
		catch(IOException e)
		{
			throw new TitliIndexException("TITLI_S_009", "I/O problem with "+tableIndexDir, e);
		}
		catch(SQLException e)
		{
			throw new TitliIndexException("TITLI_S_010", "SQL problem while executing "+query, e);
		}
			
	}
	
	
				
	/**
	 * make document to be indexed from current record
	 * 
	 * @param rs the corresponding resultset
	 * @param table the table of the corresponding record
	 * @return a Document for the record that can be added to the index
	 * @throws TitliIndexException if problems occur
	 * 
	 */
	private void makeDocument(ResultSet rs, Table table,IndexWriter indexWriter) throws TitliIndexException
	{
		Document doc = new Document();
		try 
		{		
			List<String> contentList = new ArrayList<String>(); 
			Map documentContentMap = new Hashtable();
			String uniqueValue = new String();
			
			int numberOfColumns = rs.getMetaData().getColumnCount();
			
			List<Name> uniqueKey = table.getUniqueKey();
			
			//Add the columns in the list
			for(int i=1; i<=numberOfColumns; i++)
			{
				if(!uniqueKey.toString().contains(rs.getMetaData().getColumnName(i)))
				{
					contentList.add(rs.getString(i));
				}
			}	
			
			for(Name key : uniqueKey)
			{				
				String value = rs.getString(key.toString());
				uniqueValue = value;
				
				if(value==null)
				{
					value="null";
				}
			
				if(documentMap.size()!=0)
				{
					//If the documentMap doesn't contain the key as value then retrieve the data from the map 
					//to add the document to indexWriter and empty the map
					if(!documentMap.containsKey(value))
					{
						Set<String> keySet = documentMap.keySet();
						Iterator<String> iterator = keySet.iterator();
						String keyString = iterator.next();
						Map documentMapValue = documentMap.get(keyString);
						Document document = (Document)documentMapValue.get(TITLIDOC);
						List<String> tempContentList = (List<String>)documentMapValue.get(CONTENTLIST);
						StringBuilder contentField = new StringBuilder("");
						for(String contents : tempContentList)
						{
							contentField.append(" ");
							contentField.append(contents);
						}
						
						document.removeField(TitliConstants.DOCUMENT_CONTENT_FIELD);
						document.add(new Field(TitliConstants.DOCUMENT_CONTENT_FIELD, contentField.toString(),Field.Store.NO, Field.Index.TOKENIZED));
						
						indexWriter.addDocument(document);
						documentMap = new Hashtable();
						documentContentMap = new Hashtable();
						doc.add(new Field(key.toString(), value, Field.Store.YES, Field.Index.NO));
					}
				}
				else
				{
					doc.add(new Field(key.toString(), value, Field.Store.YES, Field.Index.NO));
				}
			}
			//If documentMap is empty, just add the data to it
			if(documentMap.size()==0)
			{
				doc.add(new Field(TitliConstants.DOCUMENT_DATABASE_FIELD, reader.getDatabase().getName().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
				doc.add(new Field(TitliConstants.DOCUMENT_TABLE_FIELD, table.getName().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
				StringBuilder contentField = new StringBuilder("");
				for(String contents : contentList)
				{
					contentField.append(" ");
					contentField.append(contents);
				}
				doc.add(new Field(TitliConstants.DOCUMENT_CONTENT_FIELD, contentField.toString(),Field.Store.NO, Field.Index.TOKENIZED));
				
				documentContentMap.put(TITLIDOC, doc);
				documentContentMap.put(CONTENTLIST, contentList);
				documentMap.put(uniqueValue, documentContentMap);
			}
			else
			{
				Map documentMapValue = documentMap.get(uniqueValue);
				Document tempDoc = (Document)documentMapValue.get(TITLIDOC);
				List<String> tempContentList = (List<String>)documentMapValue.get(CONTENTLIST);
				StringBuilder finalContent = new StringBuilder("");
				for(String contents : contentList)
				{
					if(!tempContentList.contains(contents))
					{
						tempContentList.add(contents);
					}
				}
				for(String tempContent : tempContentList)
				{
					finalContent.append(" ");
					finalContent.append(tempContent);
				}
				documentMapValue.remove(CONTENTLIST);
				documentMapValue.put(CONTENTLIST,tempContentList);
				
				tempDoc.removeField(TitliConstants.DOCUMENT_CONTENT_FIELD);
				tempDoc.add(new Field(TitliConstants.DOCUMENT_CONTENT_FIELD, finalContent.toString(),Field.Store.NO, Field.Index.TOKENIZED));
				
				documentMapValue.remove(TITLIDOC);
				documentMapValue.put(TITLIDOC, tempDoc);
				
				documentMap = new Hashtable();
				documentMap.put(uniqueValue, documentMapValue);
			}
		}
		catch(SQLException e)
		{
			throw new TitliIndexException("TITLI_S_011", "SQL problem while trying to access a record from the result set", e);
		}
		catch(TitliException e)
		{
			throw new TitliIndexException("TITLI_S_012", "Problem in getting Database object", e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}			
	}
	
	
	
	
	/**
	 * Get the query that will return a resultset consisting of all the fields of the table as well as of the tables refrenced through joins with this table
	 * @param table the table for which to produce the extended query
	 * @return the query that will return a resultset consisting of all the fields of the table as well as of the tables refrenced through joins with this table 
	 */
	
	private String getExtendedQuery(Table table) 
	{
		StringBuilder fromClause = new StringBuilder();
		StringBuilder whereClause = new StringBuilder(" WHERE ");
		StringBuilder orderByClause = new StringBuilder(" ORDER BY ");
		StringBuilder queryString = new StringBuilder();
		
		int orderByLength = orderByClause.length();
		int whereLength = whereClause.length();
		
		Map<Name, ColumnInterface> columns = table.getColumns();
		
		for(Name columnName : columns.keySet())
		{
			Column column = (Column)table.getColumn(columnName);
			
			Column column2 = column.getReferredColumn();
			
			//column refers to another column
			if(column2!=null)
			{
				Name anotherTable = column2.getTableName();
				Name anotherColumn = column2.getName();
	
				fromClause.append(anotherTable+", ");
				whereClause.append(table.getName()+"."+column.getName()+"="+anotherTable+"."+anotherColumn+" AND ");
			}
		}
		
		try 
		{
			queryString = TitliIndexMapper.getInstance().returnJoinMapping(table.getName().toString());
			
			String orderByString = TitliIndexMapper.getInstance().getOrderByClause(table.getName().toString());
			orderByClause.append(orderByString);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		//remove the last "AND"
		int i= whereClause.lastIndexOf("AND");
		if(i!=-1)
		{
			whereClause.delete(i, i+3);
		}
		
		i= queryString.lastIndexOf("AND");
		if(i!=-1)
		{
			queryString.delete(i, i+3);
		}
		
		String query;
		
		if(orderByClause.length() == orderByLength)
			orderByClause = new StringBuilder();
		//Don't add whereClause  if nothing is appended to it
		if(whereClause.length()==whereLength)
		{
			query = queryString+" "+orderByClause;
		}
		else
		{	
			query = queryString+" "+whereClause+orderByClause;
		}
		
		System.out.println("Extended Query : "+query);
		
		return query;
		
	}
	
	
	public static void main(String args[]) throws TitliException
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
			
		try 
		{
			titli.index(new Name("db29"));
		}
		catch (TitliIndexException e) 
		{
			System.out.println(e+"\n"+e.getCause());
		}
		
		
		long end = new Date().getTime();
		
		
		/*System.out.println("Indexing took "+(end-start)/1000.0+" seconds");
		System.out.println("uniqueValueList : ");
		for(String uniqueValue : uniqueValueList)
		{
			System.out.println(uniqueValueList+" ");
		}*/
		
		/*MatchListInterface  matchList=null;
		try 
		{	
			matchList =titli.search("Bladder neck");
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
		*/
		//System.out.println("\n\nMatches : "+matchList.size()+"   Time : "+matchList.getTimeTaken()+" seconds   Time :  "+(end-start)/1000.0);
		
		//new Indexer().getContainmentIdentifier("catissue_specimen_char", "17");
	}


}

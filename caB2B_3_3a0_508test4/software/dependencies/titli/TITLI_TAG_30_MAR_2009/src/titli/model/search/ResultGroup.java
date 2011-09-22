/**
 * 
 */
package titli.model.search;

import java.util.Map;
import java.util.Map.Entry;

import titli.controller.Name;
import titli.controller.interfaces.MatchInterface;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.ResultGroupInterface;
import titli.controller.interfaces.record.RecordListInterface;
import titli.model.fetch.Fetcher;
import titli.model.fetch.RecordList;
import titli.model.fetch.TitliFetchException;

/**
 * 
 * @author Juber Patel
 *
 */
public class ResultGroup implements ResultGroupInterface 
{
	
	private Name tableName;
	private MatchList matchList;
	private Fetcher fetcher;
	private RecordList recordList;
	

	/**
	 * 
	 * @param tableName the table for which to create the result group
	 * @param fetcher the fetcher for this table
	 */
	public ResultGroup(Name tableName, Fetcher fetcher)
	{
		this.tableName = tableName;
		this.fetcher = fetcher;
		
		this.matchList = new MatchList();
		
	}
	
	/**
	 * 
	 * @param match the match to be added to the group
	 */
	public void addMatch(MatchInterface match)
	{
		
		matchList.add(match);
		
	}
	
	
	/**
	 * return the record list
	 * @return the corresponding record list
	 * @throws TitliFetchException if problems occur
	 */
	public RecordListInterface fetch() throws TitliFetchException 
	{
		if(recordList==null)
		{
			recordList = fetcher.fetch(this);
		}
		
		return recordList;
	}

	
	/**
	 * get the match list for matches from this table
	 * @return the match list for matches from this table
	 */
	public MatchListInterface getMatchList() 
	{
		return new MatchList(matchList);
		
	}

	/**
	 * get the number of matches in this table
	 * @return the number of matches in this table
	 */
	public int getNumberOfMatches() 
	{
		return matchList.size();
	}

	/**
	 * 
	 * @return the table name
	 */
	public Name getTableName() 
	{
		return tableName;
	}

	/**
	 * get the SQL query that will return the records corresponding to this result group
	 * @return the SQL query
	 */
	public String getQueryString()
	{
		StringBuilder query =new StringBuilder("SELECT * FROM "+getTableName()+" WHERE ");
		
		for(MatchInterface match : matchList)
		{
			StringBuilder subquery = new StringBuilder(" (");
			
			match = (Match)match;
			
			Map<Name, String> keys = match.getUniqueKeys();
			
			for(Entry e : keys.entrySet())
			{
				subquery.append(e.getKey()+"="+e.getValue()+" AND ");
			}
			
			//remove the last AND
			subquery.delete(subquery.lastIndexOf("AND"), subquery.length());
			
			//add thje closing bracket
			subquery.append(") ");
			
			//append with an OR
			query.append(subquery+" OR ");
		}
		
		//reomve last OR
		query.delete(query.lastIndexOf("OR"), query.length());
		
		return query.toString();
		
	}
	
}

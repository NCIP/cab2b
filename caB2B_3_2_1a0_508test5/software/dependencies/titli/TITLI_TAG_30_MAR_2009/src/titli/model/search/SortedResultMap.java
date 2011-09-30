/**
 * 
 */
package titli.model.search;

import java.util.LinkedHashMap;
import java.util.Map;

import titli.controller.Name;
import titli.controller.interfaces.MatchInterface;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.ResultGroupInterface;
import titli.controller.interfaces.SortedResultMapInterface;
import titli.model.fetch.Fetcher;

/**
 * this is a map of "table name" => "the group of matches from the table"
 * @author Juber Patel
 *
 */
public class SortedResultMap extends LinkedHashMap<Name, ResultGroupInterface>	implements SortedResultMapInterface
{
	/**
	 * 
	 * @param matches matches on which to create the sortd result map
	 * @param fetchers a map of fetchers
	 */
	public SortedResultMap(MatchListInterface matches, Map<Name, Fetcher> fetchers)
	{
		super();
		
		initGroups(matches, fetchers);
		
	}
	
	
	/**
	 * initialize the result groups by table name and populate them with corresponding matches 
	 * @param matches the matches
	 * @param fetchers the fetchers
	 */
	private void initGroups(MatchListInterface matches, Map<Name, Fetcher> fetchers) 
	{
		for(MatchInterface match : matches)
		{
			//get the right group for the match
			ResultGroup group = (ResultGroup)get(match.getTableName());
			
			//if it does not exist, create the group for the match
			if(group==null)
			{
				Name tableName = match.getTableName();
				
				group = new ResultGroup(tableName, fetchers.get(match.getDatabaseName()));
				
				put(tableName, group);
			}
			
			group.addMatch(match);
			
		}
		
	}
	
}

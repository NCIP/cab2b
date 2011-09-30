/**
 * 
 */
package titli.model.search;

import java.util.ArrayList;
import java.util.Map;

import titli.controller.Name;
import titli.controller.interfaces.MatchInterface;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.ResultGroupInterface;
import titli.controller.interfaces.SortedResultMapInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.fetch.Fetcher;
import titli.model.fetch.TitliFetchException;


/**
 * @author Juber Patel
 *
 */
public class MatchList extends ArrayList<MatchInterface> implements MatchListInterface
{

	private double time;
	private Map<Name, Fetcher> fetchers;
	private SortedResultMap resultMap;
	
	/**
	 * the constructor
	 * @param time time taken to perform the search and get the matchlist
	 * @param fetchers a map of fetchers
	 */
	public MatchList(double time, Map<Name, Fetcher> fetchers)
	{
		super();
		
		this.time = time; 
		this.fetchers = fetchers;
		
	}

	/**
	 * constructor for matchlist of a result group
	 *
	 */
	public MatchList()
	{
		super();
		
	}

	
	/**
	 * the constructor
	 * @param matchList the matchList to be copied 
	 */
	public MatchList(MatchList matchList)
	{
		super(matchList);
		
		this.time = matchList.getTimeTaken(); 
		
	}

	

	/**
	 * Get the number of matches in the matchlist
	 * @return the number of matches in the matchlist
	 */
	public int getNumberOfMatches()
	{
		return size();
	}


	/**
	  * Get the time taken for searching the query string and all the matches
	  * @return time taken in seconds
	  */
	 public double getTimeTaken()
	 {
		return time;
	}
	 
	 
	 /**
	  * get the sorted result map for the given table name
	  * @return the sorted result map for the given table name
	  */ 	 
	public SortedResultMapInterface getSortedResultMap()
	{
		if(resultMap == null)
		{
			resultMap = new SortedResultMap(this, fetchers);
			
		}
		
		return resultMap;
		
		
	}
	
	
	
	/**
	 * 
	 *  @param args args for main
	 * @throws TitliException if problems occur
	 */
	public static void main(String[] args) throws TitliException
	{
		 
		
		
		TitliInterface titli=null;
		try
		{
			titli = Titli.getInstance();
		}
		catch (TitliException e) 
		{
			System.out.println(e);
			System.exit(0);
			
			
		}
		
		//titli.index();
		
		MatchListInterface matchList = null;
		try 
		{
			matchList = titli.search("mit*");
		}
		catch (TitliSearchException e) 
		{
			System.out.println(e+"\n"+e.getCause());
		}  
		
		SortedResultMapInterface map = matchList.getSortedResultMap();
		
		for(ResultGroupInterface groupInterface : map.values())
		{
			ResultGroup group = (ResultGroup)groupInterface;
			
			try 
			{
				System.out.println(group.fetch());
			}
			catch (TitliFetchException e) 
			{
				System.out.println(e);
			}
			
			
		}
	
	}
	 
}




/**
 * 
 */
package titli.model.fetch;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import titli.controller.interfaces.MatchInterface;
import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.ResultGroupInterface;
import titli.controller.interfaces.SortedResultMapInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;

/**
 * @author Juber Patel
 *
 */
public class FetchTest
{
	private static TitliInterface titli;
	
	/**
	 * @throws TitliException if
	 * 
	 *
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
	 * @throws TitliException if
	 * 
	 *
	 */
	@Test
	public void fetchSingleRecord() throws TitliException
	{
		MatchListInterface matchList = titli.search("l*");
		
		for(MatchInterface match : matchList)
		{
			System.out.println(match.fetch());
		}
	}
	
	/**
	 * 
	 * @throws TitliException if
	 */
	@Test
	public void fetchGroupOfRecords() throws TitliException
	{
		MatchListInterface matchList = titli.search("a*");
		
		SortedResultMapInterface sortedMap = matchList.getSortedResultMap();
		
		for(ResultGroupInterface resultGroup : sortedMap.values())
		{
			System.out.println(resultGroup.fetch());
		}
	}

}

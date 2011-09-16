/**
 * 
 */
package titli.model.search;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import titli.controller.interfaces.MatchListInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;

/**
 * @author Juber Patel
 *
 */
public class SearchTest
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
	public void matchNotFound() throws TitliException
	{
		MatchListInterface matchList = titli.search("garbage");
		
		assertEquals("Match found where it should not be !!",matchList.size(), 0);
	}
	
	
	/**
	 * @throws TitliException if
	 * 
	 *
	 */
	@Test
	public void oneMatchFound() throws TitliException
	{
		MatchListInterface matchList = titli.search("linda");
		
		assertEquals("Matches found not equal to 1  !!",matchList.size(), 1);
	}
	
	
	/**
	 * @throws TitliException if
	 * 
	 *
	 */
	@Test
	public void multipleMatchesFound() throws TitliException
	{
		MatchListInterface matchList = titli.search("m*");
		
		assertNotSame("0 matches found !!",matchList.size(), 0);
		assertNotSame("Only 1 match found !!",matchList.size(), 1);
	}
	
	
	

}

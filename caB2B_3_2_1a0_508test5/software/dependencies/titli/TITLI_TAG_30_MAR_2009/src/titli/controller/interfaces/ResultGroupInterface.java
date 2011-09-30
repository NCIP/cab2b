/**
 * 
 */
package titli.controller.interfaces;

import titli.controller.Name;
import titli.controller.interfaces.record.RecordListInterface;
import titli.model.fetch.TitliFetchException;

/**
 * The interface that represents a group of matches from the same table
 * @author Juber Patel
 *
 */
public interface ResultGroupInterface
{
	
	/**
	 * 
	 * @return the table name
	 */
	Name getTableName();
	
	/**
	 * get the number of matches in this table
	 * @return the number of matches in this table
	 */
	int getNumberOfMatches();
	
	
	/**
	 * return the record list
	 * @return the corresponding record list
	 * @throws TitliFetchException if problems occur
	 */
	RecordListInterface fetch() throws TitliFetchException;
	
	/**
	 * get the match list for matches from this table
	 * @return the match list for matches from this table
	 */
	MatchListInterface getMatchList();
	
	

}

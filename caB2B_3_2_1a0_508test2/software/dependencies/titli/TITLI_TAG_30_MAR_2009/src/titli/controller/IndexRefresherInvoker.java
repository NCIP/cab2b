/**
 * 
 */
package titli.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * the abstract class that conatins much of the functionality reqiured for refreshing the index in batches
 * in a way that separates the task of refreshing the index from the the task of identifying refrashable records.
 * The only abstract method is pullRefreshableRecords()
 *  
 * @author Juber Patel
 *
 */
public abstract class IndexRefresherInvoker
{
	private List<RecordIdentifier> insertableList;
	private List<RecordIdentifier> deletableList;
	private List<RecordIdentifier> updatableList;
	
	
	/**
	 * 
	 *
	 */
	public IndexRefresherInvoker()
	{
		insertableList = new ArrayList<RecordIdentifier>();
		deletableList = new ArrayList<RecordIdentifier>();
		updatableList = new ArrayList<RecordIdentifier>();
	}
	
	
	/**
	 * 
	 *This abstract method should pull the records that should be inserted, deleted or updated
	 *The RecordIdentifier objects should be added to insertableList,
	 *deletableList and updatableList as required 
	 */
	public abstract void pullRefreshableRecords();
	
	
	/**
	 *refresh the indexes ie insert, delete and update identified records 
	 *this method should be called whenver updating of indexes is desired
	 */
	public void refresh()
	{
		pullRefreshableRecords();
		
		
		
	}


	/**
	 * @return the deletableList
	 */
	public List<RecordIdentifier> getDeletableList()
	{
		return deletableList;
	}


	/**
	 * @return the insertableList
	 */
	public List<RecordIdentifier> getInsertableList() 
	{
		return insertableList;
	}


	/**
	 * @return the updatableList
	 */
	public List<RecordIdentifier> getUpdatableList() 
	{
		return updatableList;
	}
	
}

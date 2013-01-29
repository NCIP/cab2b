/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.pagination;

import java.util.EventObject;

/**
 * Unlike ListSelectionEvent PageSelectionEvemt need not have
 *  private int firstIndex;
 *  private int lastIndex;
 *  
 *  Since, user can't select multiple thins in one shot
 *  For each click, one PageSelectionEvent is created and fired, 
 *  Or multiple selection is not possible/allowed for one mouse click.
 *  
 * @author chetan_bh
 *
 */

public class PageSelectionEvent extends EventObject{
	
	/**
	 * Index to unambigiously indetify an element in a pagination.  
	 */
	private PageElementIndex elementIndex;
	
	/**
	 * Index for a page.
	 */
	private String pageIndex;
	
	/**
	 * Index for an element in a page.
	 */
	private int indexInPage;
	
	public PageSelectionEvent(Object source, String pageIndex, int indexInPage) {
		super(source);
		this.pageIndex = pageIndex;
		this.indexInPage = indexInPage;
		
		this.elementIndex = new PageElementIndex(this.pageIndex, this.indexInPage);
	}
	
	public PageSelectionEvent(Object source, PageElementIndex pageElementIndex)
	{
		super(source);
		
		this.elementIndex = pageElementIndex;
		this.pageIndex = elementIndex.getPageIndex();
		this.indexInPage = elementIndex.getIndexInPage();
	}
	
	/**
	 * Returns page index. 
	 * @return
	 */
	public String getPageIndex()
	{
		return pageIndex;
	}
	
	/**
	 * Returns an elements index in the page.
	 * @return
	 */
	public int getIndexInPage()
	{
		return indexInPage;
	}
	
	/**
	 * Returns the <code>PageElementIndex</code> object associated with this event.
	 * @return
	 */
	public PageElementIndex getIndex()
	{
		return elementIndex;
	}
	
}

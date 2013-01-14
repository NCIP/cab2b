/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.pagination;

/**
 * Each page element belongs to a page in a collection of pages.
 * 
 * pageIndex is a variale which tells us to which a particular element belongs,
 * but it won't tell the location of the element in that page.
 * 
 * For this indexInPage index is needed.
 * 
 * Hence two indexex are needed to precisely find the location of a 
 * page element in the page datastructure.
 * 
 * 
 * @author chetan_bh
 */
public class PageElementIndex {
	
	/**
	 * Index for a page.
	 */
	String pageIndex;
	
	/**
	 * Index for an element in a page.
	 */
	int indexInPage;
	
	public PageElementIndex(String pageIndex, int indexInPage)
	{
		this.pageIndex = pageIndex;
		this.indexInPage = indexInPage;
	}
	
	/**
	 * Returns the element's index in the page. 
	 * @return
	 */
	public int getIndexInPage() {
		return indexInPage;
	}
	
	/**
	 * Sets the element's index in the page. 
	 * @param indexInPage
	 */
	public void setIndexInPage(int indexInPage) {
		this.indexInPage = indexInPage;
	}
	
	/**
	 * Returns the page's index.
	 * @return
	 */
	public String getPageIndex() {
		return pageIndex;
	}
	
	/**
	 * Sets the page's index.
	 * @param pageIndex
	 */
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	/**
	 * Returns the string representation of PageElementIndex object.
	 */
	public String toString()
	{
		return pageIndex+","+indexInPage;
	}
	
}

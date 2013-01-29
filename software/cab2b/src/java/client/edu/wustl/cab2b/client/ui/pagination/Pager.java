/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.pagination;

import java.util.Vector;

/**
 * An interface to different types of pager.
 * All Pagers should implement this interface, thus abstracting the
 * paging details from the <code>PaginationModel</code>.
 * 
 * @author chetan_bh
 */
public interface Pager {
	
	/**
	 * Returns a page given a page index.
	 * A page is a <code>Collection</code> of <code>PageElement</code>s.
	 * 
	 * @param index in the page indices collection.
	 * @return a page.
	 */
	public Vector<PageElement> getPage(String index);
	
	/**
	 * Returns the current page index.
	 * @return current page index.
	 */
	public String getCurrentPageIndex();
	
	/**
	 * Returns true if the pager has more elements, after the current page index, else false.
	 * @return true, if more pages are available.
	 */
	public boolean hasNextPage();
	
	/**
	 * Returns true if the pager has more elements, before the current page index, else false.
	 * @return true if more pages are available.
	 */
	public boolean hasPreviousPage();
	
	/**
	 * Returns the first page in the paged collection in the pager. 
	 * @return
	 */
	public Vector<PageElement> firstPage();
	
	/**
	 * Returns the next page to the current page index in the pager.
	 * @return next page.
	 */
	public Vector<PageElement> nextPage();
	
	/**
	 * Returns the previous page to the current page index in the pager.
	 * @return previous page.
	 */
	public Vector<PageElement> previousPage();
	
	//public Vector nextPageIndices();
	
	//public Vector previousPageIndices();
	
	/**
	 * Returns the page indices needed to access the pages.
	 * @return page indices.
	 */
	public Vector<String> getAllPageIndices();
	
	/**
	 * Returns elements per page parameter.
	 * @return
	 */
	public int getElementsPerPage();
	
	/**
	 * Sets the elements per page parameter.
	 * @param elementsPerPage
	 */
	public void setElementsPerPage(int elementsPerPage);
	
	/**
	 * Returns the page element given an page element index.
	 * @param pageEleIndex
	 * @return
	 */
	public PageElement getPageElement(PageElementIndex pageEleIndex);
	
	/**
	 * Returns pager name.
	 * @return
	 */
	public String getPagerName();
	
}

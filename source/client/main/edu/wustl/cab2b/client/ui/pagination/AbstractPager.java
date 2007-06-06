package edu.wustl.cab2b.client.ui.pagination;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * Abstract class implementing some common methods in Pager.
 * 
 * Note : Ideas for implementing 2-level pager. 
 * 
 * 1) Abstract pager should contain second-level pager, this pager is
 *    always a Numeric Pager. 
 * 
 * 2) User/Programmer doesn't have any control over the 2-level pager.
 * 
 * 3) PageElementIndex needs to be changed to contain one more index 
 *    apart from the two existing now.
 *    a) PageIndex
 *    b) SubPage Index
 *    c) Index in SubPage // Needs more thought on this.
 *     
 * 4) PageSelectionModel also needs to be changed accordingly(especially BitSet-related things)
 *    PageSelectionEvent, PageSelectionListener needs to be updated.
 * 
 * 5) The method signature of the methods in Pager interface will change slightly, esplacially 
 *    getPage() and similar methods.  
 * 
 * 6) JPagination will contain one more JPageBar for sub page bar, need to add action listener for this.
 * 
 * 7) The currentPage pointer in the PaginationModel(in turn in Pager) needs to take into account now 
 *    the 2-level pager's existence. 
 * 
 * 8) Create a final method called subPage() in AbstractPager which gets called automatically for all pages 
 *    where condition 
 *    
 *    1) Pager type is non-numeric, AND
 *    2) Page size of a particular page is more than elementsPerPage parameter.
 *    
 *    The job of subPage method is to sub page (meaning to break this, already broken page into smaller pages, 
 *    this is what is called second level paging). This method is final because this should not be overridden 
 *    by methods in subclasses. This may be protected or private but not public, since this should not be
 *    called by programmer, rather by the associated classes. 
 *    
 *    The AbstrctPager's private method 
 * 
 * @author chetan_bh
 * 
 */
public abstract class AbstractPager implements Pager {
	
	
	/*
	 * This pageMap datastructure needs to be modified a little.
	 * Right now it is a Map of String->Vector<PageElement>.
	 * 
	 * But after the Second level pagination it will be
	 * Map of String->Map<Map> value Map is from String->Vector<PageElement>
	 * 
	 * Key set in the outer map are page indices of first level paging of full user data, 
	 * Key set in the inner maps are page indices of the second level paging of first level page.
	 * 
	 */
	Map<String,Vector> pageMap;
	
	// TODO A vector of pageIndices is needed because the pageMap keyset is not ordered,
	// but page Indices is a ordered list.
	Vector<String> pageIndices; // A vector of Strings which are key to pageMap.
	
	int numberOfElements;
	
	Vector<PageElement> rawData;
	
	// TODO this is a configurable parameter.
	/**
	 * Elements per page attribute means different for different pagers,
	 * 
	 * For numeric pager it is a very impoertant parameter, all pages except the last page
	 * should mandatorily have exaclty the number of specified number of elements per page.
	 * 
	 * For non-numeric pager this rule applies only for pages which have more than specified 
	 * number of elements per page, i.e for secondary pagination.
	 * 
	 * So getter setter methods and property change handler code, for this attribute should 
	 * go to respective pagers.
	 */
	int elementsPerPage;
	
	int numberOfPages;
	
	int currentPageIndex = -1;
	
	String currentPageIndexStr = "";
	
	public AbstractPager(PageElement[] elements)
	{
		this(convertToVector(elements));
	}
	
	public AbstractPager(Vector<PageElement> data)
	{
		this(data, PaginationConstants.DEFAULT_ELEMENTS_PER_PAGE);
	}
	
	public AbstractPager(Vector<PageElement> data, int elementsPerPage)
	{
		this.rawData = data;  // Added on 28/11/06 this is needed for setElementsPerPAge setter method,
		                      // otherwise this method has no access to rawData;
		this.elementsPerPage = elementsPerPage;
		numberOfElements = data.size();
		//Logger.out.debug("number of elements "+numberOfElements);
		numberOfPages = (int)Math.ceil(numberOfElements / (double)elementsPerPage);
		//Logger.out.debug("number Of Pages needed ==>> "+numberOfPages);
		pageIndices = new Vector<String>();
		pageMap = page(data);
		//Logger.out.info("pageMap.keySet() "+pageMap.keySet());
	}
	
	//TODO page index starts from 1 unlike standard JAVA indexing which starts from 0.
	protected Map<String, Vector> page(Vector<PageElement> data)
	{
		Map<String,Vector> returner = new HashMap<String,Vector>();
		Iterator<PageElement> elementsIter = data.iterator();
		
		int pageCounter = 1;
		int elementsCounter = 0;
		String currentPage = "";
		Vector<PageElement> page = new Vector<PageElement>();
		
		while(elementsIter.hasNext())
		{
			PageElement pageElement = elementsIter.next();
			if(elementsCounter == elementsPerPage)
			{
				currentPage = ""+pageCounter;
				pageIndices.add(currentPage);
				returner.put(currentPage, page);
				page = new Vector<PageElement>();
				elementsCounter = 0;
				pageCounter++;
			}
			page.add(pageElement);
			elementsCounter++;
		}
		currentPage = ""+pageCounter;
		pageIndices.add(currentPage);     // again page indices need to be broken by numeric pager.
		returner.put(currentPage, page);  // TODO last page added to the pageMap.
		
		//Logger.out.info("pageIndices "+pageIndices);
		
		return returner;
	}
	
	/*
	 * TODO Yet to Implement.
	 * May be this method needs to be called automatically from page() method in Abstract classes or from page()
	 * method overridden in subclasses like in Alphabetic pager.
	 */
	protected final Map<String, Vector> subPage(Vector<PageElement> subData)
	{
		Map<String,Vector> returner = new HashMap<String,Vector>();
		
		
		
		return returner;
	}
	
	/**
	 * A utility method to convert array of page element to Vector of page element. 
	 * @param elements
	 * @return
	 */
	private static Vector<PageElement> convertToVector(PageElement[] elements)
	{
		Vector<PageElement> returner = new Vector<PageElement>();
		for(int i = 0; i < elements.length; i++)
		{
			returner.add(elements[i]);
		}
		return returner;
	}
	
	/**
	 * Returns a page.
	 */
	public Vector<PageElement> getPage(String index)
	{
		Vector<PageElement> page = pageMap.get(index);
		int requestedPageIndex = -1;
		try{
			requestedPageIndex = Integer.parseInt(index);
		}catch(NumberFormatException nfe)
		{
			requestedPageIndex = pageIndices.indexOf(index);
			requestedPageIndex++;
		}
		currentPageIndexStr = index;
		currentPageIndex = --requestedPageIndex;
		
		return page;
	}
	
	/**
	 * Returns true if there a pages next to current page, else false.
	 */
	public boolean hasNextPage()
	{
		if(currentPageIndex < (pageIndices.size()-1))
			return true;
		return false;
	}
	
	/**
	 * returns true if there are pages back to current page, else false.
	 */
	public boolean hasPreviousPage()
	{
		if(currentPageIndex > 0)
			return true;
		return false;
	}
	
	/**
	 * Returns the first page in the current pagination.
	 */
	public Vector<PageElement> firstPage()
	{
		//Logger.out.info("firstPage requested");
		currentPageIndex = 0;
		currentPageIndexStr = pageIndices.get(currentPageIndex);
		//currentPageIndexStr = "1"; // TODO A bug was threr related to this hardcoding.
		String index = pageIndices.get(currentPageIndex);
		return pageMap.get(index);
	}
	
	/**
	 * Returns the page next to the current page.
	 */
	public Vector<PageElement> nextPage()
	{
		//Logger.out.info("nextPage :: currentPageIndex "+currentPageIndex);
		currentPageIndex++;
		//Logger.out.info("after "+currentPageIndex);
		String index = pageIndices.get(currentPageIndex);
		currentPageIndexStr = index;
		Vector<PageElement> nextPage = pageMap.get(index);
		
		return nextPage;
	}
	
	/**
	 * Returns the page previous to the current page. 
	 */
	public Vector<PageElement> previousPage()
	{
		//Logger.out.info("previousPage :: currentPageIndex "+currentPageIndex);
		currentPageIndex--;
		//Logger.out.info("after "+currentPageIndex);
		String index = pageIndices.get(currentPageIndex);
		currentPageIndexStr = index;
		Vector<PageElement> previousPage = pageMap.get(index);

		return previousPage;
	}
	
//	/**
//	 * This method should be deleted here, and moved to PageBarModel
//	 */
//	public Vector nextPageIndices() {
//		return pageIndices;
//	}

//	public Vector previousPageIndices() {
//		// This should actually return one part of Page Indices.
//		return pageIndices;
//	}
	
	/**
	 * Returns a collection of page indices.
	 */
	public Vector<String> getAllPageIndices()
	{
		return pageIndices;
	}
	
	public String toString()
	{
		return pageMap.toString();
	}
	
	/**
	 * This method is needed for PageBarModel to decide on the next and previous
	 * pageIndices in needed or not, to update fresh set of page indices.
	 */
	public String getCurrentPageIndex()
	{
		return currentPageIndexStr;
	}
	
	/**
	 * Returns the page element in a particular page, else null..
	 */
	public PageElement getPageElement(PageElementIndex pageElemIndex)
	{
		PageElement returner = null;
		
		String pageIndex = pageElemIndex.getPageIndex();
		int indexInPage = pageElemIndex.getIndexInPage();
		
		Vector page = (Vector)pageMap.get(pageIndex);
		// TODO this is work around for BitSet not having same lenght as the page size
		// TODO handle null in the calling function.
		if(indexInPage < page.size())
			returner = (PageElement)page.get(indexInPage);
		else
			returner = null;
		
		
		return returner;
	}
	
}

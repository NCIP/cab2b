package edu.wustl.cab2b.client.ui.pagination;

import java.util.Vector;

import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author chetan_bh
 */
public class PaginationModel implements Pager {
	
	@Deprecated
	int paginationType;
	
	/**
	 * Unmodified user data.
	 */
	Vector<PageElement> rawPageData;
	
	/**
	 * A pager, which performs the actual pagination of the user data. 
	 */
	Pager pager;
	
	public PaginationModel()
	{
		this(null);
	}
	
	public PaginationModel(Vector<PageElement> elements)
	{
		this(elements, new NumericPager(elements));
	}
	
	public PaginationModel(Vector<PageElement> elements, Pager pager)
	{
		this.rawPageData = elements;
		this.pager = pager;
	}
	
	// TODO this function is complicated. not working now
	/**
	 * After changing the pager the entire pagination component shoul get refreshed
	 * So, this has to fire an event and JPageBar, JPagination should have registerred to this
	 * event to listen then they should get respective updated data and refresh their UI.
	 *
	 * This functionality is little complicated, needs good design.
	 */
	public void changePager(Pager pager)
	{
		//pager = pager;
	}
	
	/**
	 * Returns the requested page.
	 */
	public Vector<PageElement> getPage(String index) {
		return pager.getPage(index);
	}
	
	/**
	 * Returns the page, next to the current page.
	 */
	public Vector<PageElement> nextPage() {
		return pager.nextPage();
	}
	
	//TODO not needed here, this function is moved to PageBarModel
//	public Vector nextPageIndices() {
//		return pager.nextPageIndices();
//	}
	
	/**
	 * Returns the page, privious to the current page.
	 */
	public Vector<PageElement> previousPage() {
		return pager.previousPage();
	}
	
//	public Vector previousPageIndices() {
//		return pager.previousPageIndices();
//	}
	
	/**
	 * Returns true if there are pages after the current page.
	 */
	public boolean hasNextPage() {
		return pager.hasNextPage();
	}
	
	/**
	 * Returns true if there are pages before the current page.
	 */
	public boolean hasPreviousPage()
	{
		return pager.hasPreviousPage();
	}
	
	/**
	 * Returns the first page.
	 */
	public Vector<PageElement> firstPage() {
		return pager.firstPage();
	}
	
	/**
	 * Returns a collection of page indices, used to display in the page bar.
	 */
	public Vector<String> getAllPageIndices() {
		return pager.getAllPageIndices();
	}
	
	/**
	 * Returns the current page index.
	 */
	public String getCurrentPageIndex()
	{
		return pager.getCurrentPageIndex();
	}
	
	/**
	 * Returns elements per page parameter.
	 */
	public int getElementsPerPage()
	{
		return pager.getElementsPerPage();
	}
	
	/**
	 * Sets elements per page parameter.
	 */
	public void setElementsPerPage(int newValue)
	{
		pager.setElementsPerPage(newValue);
	}
	
	/**
	 * Returns the PageElement for the given page element index.
	 */
	public PageElement getPageElement(PageElementIndex pageEleIndex)
	{
		return pager.getPageElement(pageEleIndex);
	}
	
	/**
	 * Returns pager name.
	 */
	public String getPagerName()
	{
		return pager.getPagerName();
	}
	
	
	public static void main(String[] args) {
		
		Logger.configure("");
		
		Vector<PageElement> elements = new Vector<PageElement>();
		
		String alphabets = "ABCDEFGHHJASDGEWYVCBZXHhfdaADA";
		for(int i = 0; i < alphabets.length(); i++)
		{
			PageElement element = new PageElementImpl();
			element.setDisplayName(alphabets.charAt(i)+"-123");
			elements.add(element);
		}
		PaginationModel pageModel = new PaginationModel(elements, new AlphabeticPager(elements));
		Logger.out.debug("pagination mmodel . pager "+pageModel.pager);
		Logger.out.debug("pageModel "+pageModel.hasNextPage());
		while(pageModel.hasNextPage())
		{
			Vector page = pageModel.nextPage();
			Logger.out.info("page "+page);
		}
		
	}
	
}

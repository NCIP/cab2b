package edu.wustl.cab2b.client.ui.pagination;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import edu.wustl.common.util.logger.Logger;
/**
 * Alphabetic pager.
 * 
 * @author chetan_bh
 */
public class AlphabeticPager extends AbstractPager {

	private String pagerName = PaginationConstants.ALPHABETIC_PAGER; 
	
	Pager subPager;

	public AlphabeticPager(Vector<PageElement> data) {
		super(data);
	}

	public AlphabeticPager(Vector<PageElement> data, int elementsPerPage) {
		super(data, elementsPerPage);
	}

	// TODO return
	protected Map<String, Vector> page(Vector<PageElement> data)
	{
		Map<String,Vector> returner = new HashMap<String,Vector>();
		Iterator<PageElement> elementsIter = data.iterator();

		String currentPage = "";

		while(elementsIter.hasNext())
		{
			PageElement pageElement = elementsIter.next();
			String displayName = pageElement.getDisplayName();
			char firstChar = displayName.charAt(0);
			currentPage = Character.toString(firstChar);
			// To take care of case insensitive categorization of data.
			currentPage =  currentPage.toUpperCase();
			//Logger.out.debug("returner "+returner);
			Vector<PageElement> page = returner.get(currentPage);
			if(page == null)
			{
				page = new Vector<PageElement>();
				page.add(pageElement);
				returner.put(currentPage, page);
				pageIndices.add(currentPage);
			}else  // here check for numeric paging
			{
				page.add(pageElement);
			}
			numberOfPages = pageIndices.size();
		}
		// Alphabetic sorting of page indices Vector needed.
		Collections.sort(pageIndices);
		return returner;
	}
	
	 
	public int getElementsPerPage()
	{
		return elementsPerPage;
	}
	
	// TODO Yet to implement this method fully.
	// This will set elements per page for second level Numeric pager.
	public void setElementsPerPage(int elementsPerPage)
	{
		this.elementsPerPage = elementsPerPage;
	}
	
	public String getPagerName()
	{
		return pagerName;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<PageElement> elements = new Vector<PageElement>();

		String alphabets = "AbCDEfGHHJAsdGEWYVCBZXhSHhhasHqwHhHH";
		for(int i = 0; i < alphabets.length(); i++)
		{
			PageElement element = new PageElementImpl();
			element.setDisplayName(alphabets.charAt(i)+"-123");
			elements.add(element);
		}
		AlphabeticPager alphaPager = new AlphabeticPager(elements);

		Logger.out.info("pages "+alphaPager);
	}

}

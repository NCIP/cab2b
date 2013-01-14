/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.pagination;

import java.util.HashMap;
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
    private int currentPageIndex = -1;

    private String currentPageIndexStr = "";
    
    /**
     * This pageMap datastructure needs to be modified a little.
     * Right now it is a Map of String->Vector<PageElement>.
     * 
     * But after the Second level pagination it will be
     * Map of String->Map<Map<String, Vector<PageElement>>.
     * 
     * Key set in the outer map are page indices of first level paging of full user data, 
     * Key set in the inner maps are page indices of the second level paging of first level page.
     */
    protected Map<String, Vector<PageElement>> pageMap;

    /** 
     * TODO A vector of pageIndices is needed because the pageMap keyset is not ordered, 
     * but page Indices is a ordered list. 
     */
    protected Vector<String> pageIndices; // A vector of Strings which are key to pageMap.

    /** A vector of rawData*/
    protected Vector<PageElement> rawData;

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
    protected int elementsPerPage;

    /** number of Pages*/
    protected int numberOfPages;

    /**
     * @param elements
     */
    public AbstractPager(PageElement[] elements) {
        this(convertToVector(elements));
    }

    /**
     * @param data
     */
    public AbstractPager(Vector<PageElement> data) {
        this(data, PaginationConstants.DEFAULT_ELEMENTS_PER_PAGE);
    }

    /**
     * @param rawData
     * @param elementsPerPage
     */
    public AbstractPager(Vector<PageElement> rawData, int elementsPerPage) {
        // This is needed for setElementsPerPAge setter method, otherwise this method has no access to rawData
        this.rawData = rawData;
        this.elementsPerPage = elementsPerPage;

        int numberOfElements = rawData.size();
        numberOfPages = (int) Math.ceil(numberOfElements / (double) elementsPerPage);
        pageIndices = new Vector<String>();
        pageMap = getPageMap(rawData);
    }

    //TODO page index starts from 1 unlike standard JAVA indexing which starts from 0.
    /** Returns the Page Map for given Page Element
     * @param rawData
     * @return
     */
    protected Map<String, Vector<PageElement>> getPageMap(Vector<PageElement> rawData) {
        Map<String, Vector<PageElement>> returner = new HashMap<String, Vector<PageElement>>();
        int pageCounter = 1;
        int elementsCounter = 0;
        String currentPage = "";

        Vector<PageElement> page = new Vector<PageElement>();
        for (PageElement pageElement : rawData) {
            if (elementsCounter == elementsPerPage) {
                currentPage = "" + pageCounter;
                pageIndices.add(currentPage);
                returner.put(currentPage, page);
                page = new Vector<PageElement>();
                elementsCounter = 0;
                pageCounter++;
            }
            page.add(pageElement);
            elementsCounter++;
        }
        currentPage = "" + pageCounter;
        pageIndices.add(currentPage); // again page indices need to be broken by numeric pager.
        returner.put(currentPage, page); // TODO last page added to the pageMap.

        return returner;
    }

    /**
     * TODO Yet to Implement.
     * May be this method needs to be called automatically from page() method in Abstract classes or from page()
     * method overridden in subclasses like in Alphabetic pager.
     * @param subData
     * @return
     */
    protected final Map<String, Vector> subPage(Vector<PageElement> subData) {
        Map<String, Vector> returner = new HashMap<String, Vector>();

        return returner;
    }

    /**
     * A utility method to convert array of page element to Vector of page element. 
     * @param elements
     * @return
     */
    private static Vector<PageElement> convertToVector(PageElement[] elements) {
        Vector<PageElement> returner = new Vector<PageElement>();
        for (int i = 0; i < elements.length; i++) {
            returner.add(elements[i]);
        }
        return returner;
    }

    /**
     * Returns a page.
     * @param index of page
     * @return Page
     */
    public Vector<PageElement> getPage(String index) {
        Vector<PageElement> page = pageMap.get(index);
        int requestedPageIndex = -1;
        try {
            requestedPageIndex = Integer.parseInt(index);
        } catch (NumberFormatException nfe) {
            requestedPageIndex = pageIndices.indexOf(index);
            requestedPageIndex++;
        }
        currentPageIndexStr = index;
        currentPageIndex = --requestedPageIndex;

        return page;
    }

    /**
     * Returns true if there a pages next to current page, else false.
     * @return Boolean Value
     */
    public boolean hasNextPage() {
        boolean hasNextPage = false;
        if (currentPageIndex < (pageIndices.size() - 1)) {
            hasNextPage = true;
        }
        return hasNextPage;
    }

    /**
     * returns true if there are pages back to current page, else false.
     * @return Boolean value
     */
    public boolean hasPreviousPage() {
        boolean hasPreviousPage = false;
        if (currentPageIndex > 0) {
            hasPreviousPage = true;
        }
        return hasPreviousPage;
    }

    /**
     * Returns the first page in the current pagination.
     * @return First Page
     */
    public Vector<PageElement> firstPage() {
        currentPageIndex = 0;
        currentPageIndexStr = pageIndices.get(currentPageIndex);
        String index = pageIndices.get(currentPageIndex);
        return pageMap.get(index);
    }

    /**
     * Returns the page next to the current page.
     * @return Next Page
     */
    public Vector<PageElement> nextPage() {
        currentPageIndex++;
        String index = currentPageIndexStr = pageIndices.get(currentPageIndex);

        return pageMap.get(index);
    }

    /**
     * Returns the page previous to the current page. 
     * @return Previous Page
     */
    public Vector<PageElement> previousPage() {
        currentPageIndex--;
        String index = currentPageIndexStr = pageIndices.get(currentPageIndex);

        return pageMap.get(index);
    }

    /**
     * Returns a collection of page indices.
     * @return All Page indices
     */
    public Vector<String> getAllPageIndices() {
        return pageIndices;
    }

    /**
     * @see java.lang.Object#toString()
     * Overrides toString
     */
    @Override
    public String toString() {
        return pageMap.toString();
    }

    /**
     * This method is needed for PageBarModel to decide on the next and previous
     * pageIndices in needed or not, to update fresh set of page indices.
     * @return Current Page Index
     */
    public String getCurrentPageIndex() {
        return currentPageIndexStr;
    }

    /**
     * Returns the page element in a particular page, else null..
     * @param pageElemIndex
     * @return Page Element or Null
     */
    public PageElement getPageElement(PageElementIndex pageElemIndex) {
        String pageIndex = pageElemIndex.getPageIndex();
        int indexInPage = pageElemIndex.getIndexInPage();

        PageElement returner = null;
        Vector<PageElement> page = pageMap.get(pageIndex);
        // TODO this is work around for BitSet not having same length as the page size.
        if (indexInPage < page.size()) {
            returner = page.get(indexInPage);
        }

        return returner;
    }

}

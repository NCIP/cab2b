package edu.wustl.cab2b.client.ui.pagination;

import java.util.Collections;
import java.util.HashMap;
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

    //private Pager subPager;

    /**
     * @param data
     */
    public AlphabeticPager(Vector<PageElement> data) {
        super(data);
    }

    /**
     * @param data
     * @param elementsPerPage
     */
    public AlphabeticPager(Vector<PageElement> data, int elementsPerPage) {
        super(data, elementsPerPage);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.pagination.AbstractPager#getPageMap(java.util.Vector)
     * @param data
     * @return Page map info
     */
    protected Map<String, Vector<PageElement>> getPageMap(Vector<PageElement> data) {
        Map<String, Vector<PageElement>> pageMap = new HashMap<String, Vector<PageElement>>();
        String currentPage = "";

        for (PageElement pageElement : data) {
            String displayName = pageElement.getDisplayName();
            char firstChar = displayName.charAt(0);
            currentPage = Character.toString(firstChar);
            // To take care of case insensitive categorization of data.
            currentPage = currentPage.toUpperCase();

            Vector<PageElement> page = pageMap.get(currentPage);
            if (page == null) {
                page = new Vector<PageElement>();
                page.add(pageElement);
                pageMap.put(currentPage, page);
                pageIndices.add(currentPage);
            } else {// here check for numeric paging
                page.add(pageElement);
            }
            numberOfPages = pageIndices.size();
        }

        // Alphabetic sorting of page indices Vector needed.
        Collections.sort(pageIndices);
        return pageMap;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.pagination.Pager#getElementsPerPage()
     * @return elements Per page
     */
    public int getElementsPerPage() {
        return elementsPerPage;
    }

    // TODO Yet to implement this method fully.
    // This will set elements per page for second level Numeric pager.
    /**
     * @see edu.wustl.cab2b.client.ui.pagination.Pager#setElementsPerPage(int)
     * @param elementsPerPage
     */
    public void setElementsPerPage(int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.pagination.Pager#getPagerName()
     * @return Page Name
     */
    public String getPagerName() {
        return pagerName;
    }
}

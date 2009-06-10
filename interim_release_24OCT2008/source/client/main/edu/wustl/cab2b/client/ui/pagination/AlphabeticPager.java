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

    public AlphabeticPager(Vector<PageElement> data) {
        super(data);
    }

    public AlphabeticPager(Vector<PageElement> data, int elementsPerPage) {
        super(data, elementsPerPage);
    }

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

    public int getElementsPerPage() {
        return elementsPerPage;
    }

    // TODO Yet to implement this method fully.
    // This will set elements per page for second level Numeric pager.
    public void setElementsPerPage(int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }

    public String getPagerName() {
        return pagerName;
    }
}

package edu.wustl.cab2b.client.ui.pagination;

import java.util.Vector;

/**
 * Performs Numeric pagination of the page elements.
 * This functionality is implemented in AbstractPager class.
 * 
 * Hence, this class actually does nothing. 
 * 
 * @author chetan_bh
 */
public class NumericPager extends AbstractPager {
    private String pagerName = PaginationConstants.NUMERIC_PAGER;

    public NumericPager(Vector<PageElement> data) {
        super(data);
    }

    public NumericPager(Vector<PageElement> data, int elementsPerPage) {
        super(data, elementsPerPage);
    }

    public NumericPager(PageElement[] elements) {
        super(elements);
    }

    public int getElementsPerPage() {
        return elementsPerPage;
    }

    public void setElementsPerPage(int newValue) {
        if (this.elementsPerPage != newValue && newValue > 0) {
            this.elementsPerPage = newValue;
            pageIndices = new Vector<String>();
            pageMap = this.getPageMap(rawData);
            numberOfPages = pageIndices.size();
        }
    }

    /**
     * Returns the pager name.
     */
    public String getPagerName() {
        return pagerName;
    }
}
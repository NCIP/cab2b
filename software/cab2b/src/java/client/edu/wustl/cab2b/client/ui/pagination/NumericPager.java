/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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

    /**
     * @param data
     */
    public NumericPager(Vector<PageElement> data) {
        super(data);
    }

    /**
     * @param data
     * @param elementsPerPage
     */
    public NumericPager(Vector<PageElement> data, int elementsPerPage) {
        super(data, elementsPerPage);
    }

    /**
     * @param elements
     */
    public NumericPager(PageElement[] elements) {
        super(elements);
    }

    /**
     * @return no of elements per page
     * @see edu.wustl.cab2b.client.ui.pagination.Pager#getElementsPerPage()
     */
    public int getElementsPerPage() {
        return elementsPerPage;
    }

    /**
     * Sets no of elements per page
     * @param newValue
     * @see edu.wustl.cab2b.client.ui.pagination.Pager#setElementsPerPage(int)
     */
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
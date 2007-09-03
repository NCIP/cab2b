package edu.wustl.cab2b.client.ui.pagination;

import java.util.Vector;

/**
 * This class should maintain a Map of pageIndex to CustomBitSet
 * This map is empty initially, as and when user makes a selection in a page
 * the selected elements pageIndex is inserted into this map as a key, the value 
 * for this key will be a CustomBitSet with indexInPage bitset set to true indicating 
 * that an element is elected in this particular page.
 *
 *  
 * @author chetan_bh
 */
public class DefaultPageSelectionModel implements PageSelectionModel {
    private int selectionMode = MULTIPLE_SELECTION;

    //private boolean selectAll = false;

    /** Reference needed to keep the selections consistent with the 
     * property changes like elementsPerPage, etc. */
    JPagination pagination;

    public DefaultPageSelectionModel(JPagination newPagination) {
        pagination = newPagination;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public boolean isSelectedIndex(String pageIndex, int index) {
        return false;
    }

    public boolean isSelectionEmpty() {
        if (getSelectionCount() == 0)
            return true;
        return false;
    }

    // TODO after setting selection mode appropraite Event should be fired to all 
    // listeners, one among many listeners is the JPagination listener.
    public void setSelectionMode(int selectionMode) {
        switch (selectionMode) {
            case SINGLE_SELECTION:
            case MULTIPLE_SELECTION:
                this.selectionMode = selectionMode;
                break;
            default:
                throw new IllegalArgumentException("invalid selection mode");
        }
    }

    /**
     * Returns the count of selected page elements in all pages.
     */
    public int getSelectionCount() {
        int noOfSelections = 0;
        final PaginationModel paginationModel = pagination.getPaginationModel();
        final Vector<String> currentAllPageIndices = paginationModel.getAllPageIndices();

        for (String index : currentAllPageIndices) {
            final Vector<PageElement> pageElementList = paginationModel.getPage(index);

            for (PageElement pageElement : pageElementList) {
                if (pageElement.isSelected())
                    noOfSelections++;
            }
        }

        return noOfSelections;
    }

    /**
     * Following APIs should fire appropriate events to all listener.
     * One among many listeners is the JPagination component, accordingly 
     * it should update its UI.  
     */
    public void clearAll() {
        selectClearAll(false);
    }

    /**
     * Select All is not simple because it has to get all available pageIndices
     * and the size of each page.
     */
    public void selectAll() {
        selectClearAll(true);
    }

    /**
     * Inverts selection accross all pages.
     */
    public void invertAll() {
        invertAllSelections();
    }

    private void selectClearAll(boolean value) {
        final PaginationModel paginationModel = pagination.getPaginationModel();
        final Vector<String> currentAllPageIndices = paginationModel.getAllPageIndices();

        for (String index : currentAllPageIndices) {
            final Vector<PageElement> pageElementList = paginationModel.getPage(index);

            if (pageElementList != null) {
                for (PageElement pageElement : pageElementList) {
                    pageElement.setSelected(value);
                }
            }
        }
    }

    private void invertAllSelections() {
        final PaginationModel paginationModel = pagination.getPaginationModel();
        final Vector<String> currentAllPageIndices = paginationModel.getAllPageIndices();

        for (String index : currentAllPageIndices) {
            Vector<PageElement> pageElementList = paginationModel.getPage(index);

            if (pageElementList != null) {
                for (PageElement pageElement : pageElementList) {
                    if (pageElement.isSelected()) {
                        pageElement.setSelected(false);
                    } else {
                        pageElement.setSelected(true);
                    }
                }
            }
        }
    }

    /**
     * Clears all the selections in the current displayed page.
     */
    public void clearPage(String pageIndex) {
        final PaginationModel paginationModel = pagination.getPaginationModel();
        final Vector<PageElement> pageEelementList = paginationModel.getPage(pageIndex);
        for (PageElement pageElement : pageEelementList) {
            pageElement.setSelected(false);
        }
    }

    /**
     * Selects all the selections in the current page displayed. 
     */
    public void selectPage(String pageIndex) {
        final PaginationModel paginationModel = pagination.getPaginationModel();
        Vector<PageElement> pageEelementList = paginationModel.getPage(pageIndex);
        for (PageElement pageElement : pageEelementList) {
            pageElement.setSelected(true);
        }
    }

    /**
     * Inverts the selections in the current page.
     */
    public void invertPageSelection(String pageIndex) {
        final PaginationModel paginationModel = pagination.getPaginationModel();
        final Vector<PageElement> pageEelementList = paginationModel.getPage(pageIndex);
        for (PageElement pageElement : pageEelementList) {
            if (pageElement.isSelected()) {
                pageElement.setSelected(false);
            } else {
                pageElement.setSelected(true);
            }
        }
    }

    /**
     * Returns a collection of selected page element's page indices.
     */
    public Vector<PageElementIndex> getSelectedPageIndices() {
        final PaginationModel paginationModel = pagination.getPaginationModel();
        final Vector<String> currentAllPageIndices = paginationModel.getAllPageIndices();

        Vector<PageElementIndex> selectedPageIndices = new Vector<PageElementIndex>();
        for (String index : currentAllPageIndices) {
            Vector<PageElement> pageElementList = paginationModel.getPage(index);
            for (int i = 0; i < pageElementList.size(); i++) {
                PageElement element = pageElementList.get(i);
                if (element.isSelected()) {
                    selectedPageIndices.add(new PageElementIndex(index, i));
                }
            }
        }

        // This is a fix to a bug which arises because while iterating through
        // the page by using getPage() the current index gets set the maximun page index, 
        // but the page bar model has a different set of page indices as the current (broken) page indice 
        // hence we should ensure that after the above opration be should some how (in a good way) bring bact 
        // the current page index in the AbstractPager to the the old value by iterating backwards .
        // This may not be the right way to do it, But its working fine now.
        final String oldPageIndex = paginationModel.getCurrentPageIndex();
        for (int i = currentAllPageIndices.size() - 1; i >= 0; i--) {
            String index = currentAllPageIndices.get(i);
            if (index.equalsIgnoreCase(oldPageIndex)) {
                paginationModel.getPage(index);
                break;
            }
            // This is just to ensure that we call getPage() which sets the index supplied which is actually
            // drementing as you can notice, and stops when old page index is found.
            paginationModel.getPage(index);
        }

        return selectedPageIndices;
    }
}
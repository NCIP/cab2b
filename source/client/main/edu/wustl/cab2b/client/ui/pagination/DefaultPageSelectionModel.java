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

    //protected EventListenerList listenerList = new EventListenerList();

    boolean selectAll = false;

    /** Reference needed to keep the selections consistent with the 
     * property changes like elementsPerPage, etc. */
    JPagination pagination;

    public DefaultPageSelectionModel(JPagination newPagination) {
        pagination = newPagination;
    }

    //	public void addPageSelectionListener(PageSelectionListener l) {
    //		listenerList.add(PageSelectionListener.class, l);
    //	}
    //	
    //	public void removePageSelectionListener(PageSelectionListener l) {
    //		listenerList.remove(PageSelectionListener.class, l);
    //	}

    public int getSelectionMode() {
        return selectionMode;
    }

    public boolean isSelectedIndex(String pageIndex, int index) {
        //		if(selectionsMap != null)
        //		{
        //			CustomBitSet pageCustomBitSet = selectionsMap.get(pageIndex);
        //			if(pageCustomBitSet == null)
        //				return false;
        //			if(pageCustomBitSet.get(index))
        //				return true;
        //		}
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
        Vector<String> currentAllPageIndices = pagination.getPaginationModel().getAllPageIndices();
        for (String index : currentAllPageIndices) {
            Vector<PageElement> elements = pagination.getPaginationModel().getPage(index);

            for (PageElement element : elements) {
                if (element.isSelected())
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
        Vector<String> currentAllPageIndices = pagination.getPaginationModel().getAllPageIndices();

        for (String index : currentAllPageIndices) {
            Vector<PageElement> pageElements = pagination.getPaginationModel().getPage(index);

            if (pageElements != null) {
                for (PageElement element : pageElements) {
                    element.setSelected(value);
                }
            }
        }
    }

    private void invertAllSelections() {
        Vector<String> currentAllPageIndices = pagination.getPaginationModel().getAllPageIndices();

        for (String index : currentAllPageIndices) {
            Vector<PageElement> pageElements = pagination.getPaginationModel().getPage(index);

            if (pageElements != null) {
                for (int i = 0; i < pageElements.size(); i++) {
                    PageElement element = pageElements.get(i);
                    //boolean value = pageCustomBitSet.get(i);
                    boolean value = element.isSelected();
                    if (value == true) {
                        element.setSelected(false);
                    } else {
                        element.setSelected(true);
                    }
                }
            }
        }
    }

    /**
     * Clears all the selections in the current displayed page.
     */
    public void clearPage(String pageIndex) {

        Vector<PageElement> elements = pagination.getPaginationModel().getPage(pageIndex);
        for (PageElement element : elements) {
            element.setSelected(false);
        }

    }

    /**
     * Selects all the selections in the current page displayed. 
     */
    public void selectPage(String pageIndex) {

        Vector<PageElement> elements = pagination.getPaginationModel().getPage(pageIndex);
        for (PageElement element : elements) {
            element.setSelected(true);
        }
    }

    /**
     * Inverts the selections in the current page.
     */
    public void invertPageSelection(String pageIndex) {

        Vector<PageElement> elements = pagination.getPaginationModel().getPage(pageIndex);
        for (PageElement element : elements) {
            if (element.isSelected()) {
                element.setSelected(false);
            } else {
                element.setSelected(true);
            }
        }
    }

    /**
     * Returns a collection of selected page element's page indices.
     */
    public Vector<PageElementIndex> getSelectedPageIndices() {
        Vector<PageElementIndex> selectedPageIndices = new Vector<PageElementIndex>();
        String oldPageIndex = pagination.getPaginationModel().getCurrentPageIndex();

        Vector<String> currentAllPageIndices = pagination.getPaginationModel().getAllPageIndices();

        for (String index : currentAllPageIndices) {
            Vector<PageElement> elements = pagination.getPaginationModel().getPage(index);
            for (int i = 0; i < elements.size(); i++) {
                PageElement element = elements.get(i);
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
        for (int i = currentAllPageIndices.size() - 1; i >= 0; i--) {
            String index = currentAllPageIndices.get(i);
            if (index.equalsIgnoreCase(oldPageIndex)) {
                pagination.getPaginationModel().getPage(index);
                break;
            }
            // This is just to ensure that we call getPage() which sets the index supplied which is actually
            // drementing as you can notice, and stops when old page index is found.
            pagination.getPaginationModel().getPage(index);
        }

        return selectedPageIndices;
    }
}
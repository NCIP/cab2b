/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ui.pagination;

import java.util.Iterator;
import java.util.Vector;

/**
 * Model for page bar.
 * 
 * @author chetan_bh 
 */
public class PageBarModel {

    /** Indices to display per view. */
    int indicesPerView = PaginationConstants.DEFAULT_INDICES_PER_VIEW;

    /** A collection of set of page indices.  */
    Vector<Vector<String>> brokenPageIndices;

    /** Current page index. */
    int currentPageIndices = -1;

    /** Count of broken page indices. */
    int noOfBrokenPageIndices = 0;

    public PageBarModel(Vector fullPageIndices) {
        brokenPageIndices = breakFullIndices(fullPageIndices);
    }

    /** Returns true if there is next set of indices avalable, false otherwise. */
    public boolean hasNextIndices() {
        if (currentPageIndices == brokenPageIndices.size() - 1)
            return false;
        return true;
    }

    /** Returns true if there is previous set of indices available, false otherwise. */
    public boolean hasPreviousIndices() {
        if (currentPageIndices == 0)
            return false;
        return true;
    }

    /** Returns next set of page indices. */
    public Vector<String> nextIndices() {
        return brokenPageIndices.get(++currentPageIndices);
    }

    /** Returns previous set of page indices. */
    public Vector<String> previousIndices() {
        return brokenPageIndices.get(--currentPageIndices);
    }
    
    public String toString() {
        return brokenPageIndices.toString();
    }

    /**
     * Returns a vector of vector of paghe indices.
     * @param fullIndices full set of indices.
     * @return 
     */
    private Vector<Vector<String>> breakFullIndices(Vector fullIndices) {
        Vector<Vector<String>> returner = new Vector<Vector<String>>();
        Iterator indexIter = fullIndices.iterator();

        int counter = 0;
        Vector<String> subIndices = new Vector<String>();

        while (indexIter.hasNext()) {
            String index = (String) indexIter.next();

            if (counter == indicesPerView) {
                counter = 0;
                noOfBrokenPageIndices++;
                returner.add(subIndices);
                subIndices = new Vector<String>();
            }
            subIndices.add(index);
            counter++;
        }
        returner.add(subIndices);
        return returner;
    }

}

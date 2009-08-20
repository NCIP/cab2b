package edu.wustl.cab2bwebapp.util;

import java.util.Comparator;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;

/**
 * @author chetan_pundhir
 * This class sorts the saved searches alphabetically
 */
public class SavedSearchComparator implements Comparator<ICab2bQuery> {

    /**
     * @param obj1
     * @param obj2
     * @return value depending on whether the 2 values are smaller or greater
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(ICab2bQuery obj1, ICab2bQuery obj2) {
        return obj1.getName().compareTo(obj2.getName());
    }
}
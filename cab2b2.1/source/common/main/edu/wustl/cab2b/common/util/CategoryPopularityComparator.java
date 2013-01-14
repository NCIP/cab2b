/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import java.util.Comparator;

import edu.wustl.cab2b.common.category.CategoryPopularity;

/**
 * To compare to CategoryPopularity objects
 * 
 * @author hrishikesh_rajpathak
 *
 */
public class CategoryPopularityComparator implements Comparator<CategoryPopularity> {

    /**
     * Compare method from Comparator interface
     * 
     * @param popCat1 CategoryPopularity object
     * @param popCat2 CategoryPopularity object
     * @return result on the basis of popularity count.
     */
    public int compare(CategoryPopularity popCat1, CategoryPopularity popCat2) {

        if (popCat1.getPopularity() < popCat2.getPopularity()) {
            return 1;
        } else {
            return -1;
        }
    }
}

/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.Comparator;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author gaurav_mehta
 *
 */
public class ResultComparator implements Comparator<Map<AttributeInterface, Object>> {

    /**
     * @param arg1
     * @param arg2
     * @return integer value depending on value of compare
     */
    public int compare(Map<AttributeInterface, Object> arg1, Map<AttributeInterface, Object> arg2) {
        int noOfColumns1 = arg1.size();
        int noOfColumns2 = arg2.size();

        return noOfColumns2 - noOfColumns1;
    }

}

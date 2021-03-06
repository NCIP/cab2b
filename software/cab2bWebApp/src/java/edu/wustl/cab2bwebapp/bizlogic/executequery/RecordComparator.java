/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.Comparator;
import java.util.Map;

import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * @author gaurav_mehta
 *
 */
public class RecordComparator extends SpreadsheetTransformer implements Comparator<IRecord> {

    /**
     * @param recordVsCount
     */
    public RecordComparator(Map<IRecord, Integer> recordVsCount) {
        this.recordVsCount = recordVsCount;
    }

    /**
     * @param o1
     * @param o2
     * @return integer value depending on value of compare
     */
    public int compare(IRecord o1, IRecord o2) {
        if (!recordVsCount.isEmpty()) {
            int depth1 = recordVsCount.get(o1);
            int depth2 = recordVsCount.get(o2);
            return depth2 - depth1;
        }
        return 0;
    }

}

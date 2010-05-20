/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * @author gaurav_mehta
 *
 */
abstract public class SpreadsheetTransformer {

    Map<IRecord, Integer> recordVsCount = new HashMap<IRecord, Integer>();
}

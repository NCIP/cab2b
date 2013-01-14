/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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

/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;

public class PermissibleValueComparator implements Comparator<PermissibleValueInterface> {

	public int compare(PermissibleValueInterface permissibleValue1, PermissibleValueInterface permissibleValue2) {
		String value1 = permissibleValue1.getValueAsObject().toString();
		String value2 = permissibleValue2.getValueAsObject().toString();
		return (value1.compareToIgnoreCase(value2));
	}

}

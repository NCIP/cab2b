/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * Comparator for EntityInterface needed for metadata search 
 * @author chandrakant_talele
 */
public class EntityInterfaceComparator implements Comparator<EntityInterface> {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(EntityInterface entity1, EntityInterface entity2) {
		String className1 = edu.wustl.common.util.Utility.parseClassName(entity1.getName());
		String className2 = edu.wustl.common.util.Utility.parseClassName(entity2.getName());
		// If both the entities are categories then compare their names and return values accordingly
		if (Utility.isCategoryOrMMC(entity1) && Utility.isCategoryOrMMC(entity2)) {
			return (className1.compareToIgnoreCase(className2));
		} else if (Utility.isCategoryOrMMC(entity1)) { // if first entity is category return it as smaller one
			return -1; 
		} else if (Utility.isCategoryOrMMC(entity2)) { // else return as greater one
			return 1;
		} else { //If both the entities are classes then compare their names and return values accordingly
			return (className1.compareTo(className2));
		}
	}
}

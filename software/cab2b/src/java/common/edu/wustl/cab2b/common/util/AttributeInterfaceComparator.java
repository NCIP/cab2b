/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * This class implements Comparator for Attributes. It compares attributes on basis of their names.
 * 
 * @author chetan_patil
 */
public class AttributeInterfaceComparator implements Comparator<AttributeInterface> {
	private static final org.apache.log4j.Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AttributeInterfaceComparator.class);


	/**
	 * This method compares the given attributes by name
	 * @param attribute1
	 * @param attribute2
	 * 
	 * @return -1 if name of first attribute is smaller
	 *          0 if both attribute has same name
	 *          1 if name of first attribute is greater
	 */
	public int compare(AttributeInterface attribute1, AttributeInterface attribute2) {
		logger.info("JJJ comparing attributes"+attribute1.getId()+attribute1.getName()+" to "+attribute2.getId()+attribute2.getName());
		return (attribute1.getName().compareToIgnoreCase(attribute2.getName()));
	}

}

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
		return (attribute1.getName().compareToIgnoreCase(attribute2.getName()));
	}

}

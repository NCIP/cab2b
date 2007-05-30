package edu.wustl.cab2b.common.util;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public class AttributeInterfaceComparator implements Comparator<AttributeInterface> {

	public int compare(AttributeInterface attribute1, AttributeInterface attribute2) {
		return (attribute1.getName().compareToIgnoreCase(attribute2.getName()));
	}

}

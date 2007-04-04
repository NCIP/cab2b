package edu.wustl.cab2b.client.ui.controls;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public class AttributeInterfaceComparator implements Comparator {

	public int compare(Object attribute1, Object attribute2)
	{
		AttributeInterface  attribute11 = (AttributeInterface)attribute1;
		AttributeInterface  attribute22 = (AttributeInterface)attribute2;
		return (attribute11.getName().compareToIgnoreCase(attribute22.getName()));
	}

}

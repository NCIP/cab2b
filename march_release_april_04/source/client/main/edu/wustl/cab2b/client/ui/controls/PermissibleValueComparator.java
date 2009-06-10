package edu.wustl.cab2b.client.ui.controls;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;

public class PermissibleValueComparator implements Comparator {

	public int compare(Object permissibleValue1, Object permissibleValue2) 
	{
		//		 TODO Auto-generated method stub
		PermissibleValueInterface  permissibleValue11 = (PermissibleValueInterface)permissibleValue1;
		PermissibleValueInterface  permissibleValue22 = (PermissibleValueInterface)permissibleValue2;
		String value1 = permissibleValue11.getValueAsObject().toString();
		String value2 = permissibleValue22.getValueAsObject().toString();
		return (value1.compareToIgnoreCase(value2));
	}

}

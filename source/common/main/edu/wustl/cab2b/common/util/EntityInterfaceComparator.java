package edu.wustl.cab2b.common.util;

import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

public class EntityInterfaceComparator implements Comparator {

	public int compare(Object entity1, Object entity2)
	{
		EntityInterface  entity11 = (EntityInterface)entity1;
		EntityInterface  entity22 = (EntityInterface)entity2;
		String className1 = edu.wustl.common.util.Utility.parseClassName(entity11.getName());
		String className2 = edu.wustl.common.util.Utility.parseClassName(entity22.getName());
		// If both the entities are categories then compare
		// their names and return values accordingly
		if((true == Utility.isCategory(entity11))&&(true == Utility.isCategory(entity22)))
		{	
			return (className1.compareToIgnoreCase(className2));
		}
		//	if first entity is category return it as smaller one
		else if(true == Utility.isCategory(entity11))
		{
			return -1;
		}
		else if(true == Utility.isCategory(entity22))// else return as greater one
		{
			return 1;
		}
		// If both the entities are classes then compare
		// their names and return values accordingly
		else 
		{
			return (className1.compareTo(className2));
		}
		
		
	}

}

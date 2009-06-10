/**
 * <p>Title: MyHashMap class>
 * <p>Description:	MyHashMap extends the java.util.HashMap class.
 * The get(Object key) method in the HashMap is overridden in this class
 * and the collection of values for the key passed are returned.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.common.util.Utility;

/**
 * MyHashMap extends the java.util.HashMap class.
 * The get(Object key) method in the HashMap is overridden in this class
 * and the collection of values for the key passed are returned.
 * @author gautam_shetty
 */
public class MyHashMap extends HashMap implements Serializable
{
    
    /**
     * Returns the collection of values to which the specified key is mapped.
     */
    public Object get(Object keys)
    {
        MatchedClass matchedClass = new MatchedClass();
        
        Iterator sourceIterator = keySet().iterator();
        
        Collection keyCollection = (Collection) keys;
        Iterator targetIterator = keyCollection.iterator();
       
        while(targetIterator.hasNext())
        {
        	Object targetObject=targetIterator.next();
        	if(targetObject instanceof IAttribute)
        	{
        		while (sourceIterator.hasNext())
        		{
        			IAttribute sourceAttribute = (IAttribute) sourceIterator.next();
                   	if (sourceAttribute.newEquals(targetObject))
        			{
        				matchedClass.getMatchedAttributeCollection().add(sourceAttribute);
        				matchedClass.getEntityCollection().add((EntityInterface)super.get(sourceAttribute));
        			}
        					
        		}
        	}
        
        	else
        	{
        		if(targetObject instanceof IStringPermissibleValue)
	        	{
	        		while (sourceIterator.hasNext())
	        		{
	        			IStringPermissibleValue sourcePV = (IStringPermissibleValue) sourceIterator.next();
	        			IStringPermissibleValue targetPV= (IStringPermissibleValue) targetObject;
	        			if (sourcePV.newEquals(targetPV))
	        			{
	        				matchedClass.getEntityCollection().add((EntityInterface)super.get(sourcePV));
	        			}
	        				
	        		}
	        	}
        	}
        }
          
        return matchedClass;
    }
}


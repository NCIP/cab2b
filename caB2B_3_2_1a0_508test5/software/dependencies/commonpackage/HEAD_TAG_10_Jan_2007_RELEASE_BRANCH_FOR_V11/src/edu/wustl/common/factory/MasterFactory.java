/**
 * <p>Title: MasterFactory Class>
 * <p>Description:	This class returns the factory class object whose class name is 
 * provided to it.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
  * Created on April 04, 2006
 */

package edu.wustl.common.factory;

import edu.wustl.common.util.Utility;


/**
 * This class returns the factory class object whose class name is 
 * provided to it.
 * @author gautam_shetty
 */
public class MasterFactory
{
    /**
     * Returns the object of factory class whose class name is passed.
     * @param className The class name whose object is to be instantiated.
     * @return the object of factory class whose class name is passed.
     */
   public static Object getFactory(String factoryName)
   {
       Object factoryObject = Utility.getObject(factoryName);
       return factoryObject;
   }
}

/**
 * <p>Title: caB2BFactory Class>
 * <p>Description:  Factory which creates an instance of class 
 * for the class name passed.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.common.factory;

import edu.wustl.common.util.Utility;

/**
 * Factory which creates an instance of class for the class name passed.
 * @author gautam_shetty
 */
public class caB2BFactory
{
    
    /**
     * Reference of caB2BFactory.
     */
    private static caB2BFactory clientFactory;
    
    /**
     * Default private constructor.
     * Required for singleton implementation. 
     */
    private caB2BFactory()
    {
    }
    
    /**
     * Returns an instance of caB2BFactory.
     * @return an instance of caB2BFactory.
     */
    public static caB2BFactory getInstance()
    {
        if (clientFactory == null)
            clientFactory = new caB2BFactory();
        
        return clientFactory;
    }
    
    /**
     * Returns the object of the class whose name is passed.
     * @param className The class name.
     * @return the object of the class whose name is passed.
     */
    public Object getObjectofClass(String className)
    {
        return Utility.getObject(className);
    }
}
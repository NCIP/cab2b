package edu.wustl.common.querysuite.factory;

import edu.wustl.common.util.Utility;

public class CommonObjectFactory
{
	/**
     * Reference of common factory.
     */
    private static CommonObjectFactory clientFactory;
    
    /**
     * Default private constructor.
     * Required for singleton implementation. 
     */
    private CommonObjectFactory()
    {
    }
    
    /**
     * Returns an instance of caB2BFactory.
     * @return an instance of caB2BFactory.
     */
    public static CommonObjectFactory getInstance()
    {
        if (clientFactory == null)
            clientFactory = new CommonObjectFactory();
        
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

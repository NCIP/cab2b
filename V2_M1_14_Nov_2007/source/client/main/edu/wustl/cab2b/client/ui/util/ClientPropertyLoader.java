package edu.wustl.cab2b.client.ui.util;

import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author gautam_shetty
 */
public class ClientPropertyLoader extends ApplicationProperties
{
    static
    {
        ClientPropertyLoader.initBundle("client");
    }
    
    /**
     * Returns the path finder class name from the client properties file.
     * @return the path finder class name from the client properties file.
     */
    public static String getPathFinderClassName()
    {
        return getValue("pathFinder.className");
    }
}
/**
 * 
 */
package edu.wustl.cab2b.admin.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.util.logger.Logger;

/**
 * @author atul_jawale
 * This listener cache the entity on the application initialization
 */
public class Cab2bAdminContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent arg0) {

    }

    /**
     * This method get called on the application initialization and caches the entity from the server. 
     */
    public void contextInitialized(ServletContextEvent arg0) {
        Logger.configure();//Logger.configure("");
        EntityCache.getInstance();
    }

}

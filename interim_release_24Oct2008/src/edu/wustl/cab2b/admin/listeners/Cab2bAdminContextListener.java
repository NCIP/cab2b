/**
 * 
 */
package edu.wustl.cab2b.admin.listeners;

import java.sql.Connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.util.ConnectionUtil;

/**
 * @author atul_jawale This listener cache the entity on the application
 *         initialization
 */
public class Cab2bAdminContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent arg0) {

    }

    /**
     * This method get called on the application initialization and caches the
     * entity from the server.
     */
    public void contextInitialized(ServletContextEvent arg0) {
        EntityCache.getInstance();
        Connection conn = ConnectionUtil.getConnection();
        PathFinder.getInstance(conn);
        ConnectionUtil.close(conn);
    }

}

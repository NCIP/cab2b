package edu.wustl.cab2bwebapp.listener;

import java.sql.Connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.cab2b.common.authentication.GTSSyncScheduler;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.util.ConnectionUtil;

/**
 * @author gaurav_mehta
 * This is the ContextListener class which is invoked by container
 * It calls the method for synchronizing the Globus certificates 
 *
 */
public class cab2bWebAppContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        final long start = 0L; //Start right away
        final long interval = 3600000 * 10; //10 hours
        GTSSyncScheduler.initializeScheduler(start, interval);

        EntityCache.getInstance();
        Connection connection = ConnectionUtil.getConnection();

        try {
            PathFinder.getInstance(connection);
        } finally {
            ConnectionUtil.close(connection);
        }
        CategoryCache.getInstance();
    }

}

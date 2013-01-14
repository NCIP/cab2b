/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.initializer;

import java.sql.Connection;

import edu.wustl.cab2b.common.authentication.GTSSyncScheduler;
import edu.wustl.cab2b.server.cache.DatalistCache;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.serviceurl.IndexServiceOperations;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gaurav_mehta
 *
 */
public class ApplicationInitializer {

    private static ApplicationInitializer contextListener = null;

    private ApplicationInitializer() {
        initialize();
    }

    public static ApplicationInitializer getInstance() {
        if (contextListener == null) {
            contextListener = new ApplicationInitializer();
        }
        return contextListener;
    }

    private void initialize() {
        Logger.configure("caB2B.logger");
        EntityCache.getInstance();
        final long start = 0L; //Start right away
        final long interval = 3600000 * 10; //10 hours
        GTSSyncScheduler.initializeScheduler(start, interval);
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PathFinder.getInstance(conn);
        } finally {
            ConnectionUtil.close(conn);
        }
        CategoryCache.getInstance();
        DatalistCache.getInstance();
        IndexServiceOperations.refreshDatabase();
    }
}

/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.admin.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.cab2b.server.initializer.ApplicationInitializer;

/**
 * @author atul_jawale This listener cache the entity on the application
 *         initialization
 */
public class Cab2bAdminContextListener implements ServletContextListener {

    /*
     * (non-Javadoc)
     * 
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    /**
     * @param arg0
     */
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    /**
     * This method get called on the application initialization and caches the
     * entity from the server.
     * 
     * @param arg0
     */
    public void contextInitialized(ServletContextEvent arg0) {
        ApplicationInitializer.getInstance();
    }
}

/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.wustl.cab2b.server.initializer.ApplicationInitializer;

/**
 * @author gaurav_mehta
 * This is the ContextListener class which is invoked by container
 * It calls the method for synchronizing the Globus certificates 
 *
 */
public class cab2bWebAppContextListener implements ServletContextListener {

    /** 
     * @param ServletContextEvent
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    /** 
     * @param ServletContextEvent
     */    
    public void contextInitialized(ServletContextEvent arg0) {
        ApplicationInitializer.getInstance();
    }
}
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

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.lang.Runnable;
import edu.wustl.cab2b.admin.util.Utility;
import edu.wustl.cab2b.server.util.UtilityOperations;

/**
 * @author atul_jawale
 * 
 */
public class SessionListener implements HttpSessionListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http
     * .HttpSessionEvent)
     */
    /**
     * @param arg0
     */
    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet
     * .http.HttpSessionEvent)
     */
    /**
     * @param arg0
     */
    public void sessionDestroyed(HttpSessionEvent arg0) {

        //This will create a thread for cache update and allow user to logout.
        new Thread(new Runnable() {
            public void run() {
                UtilityOperations.refreshCache();

            }
        }).start();

    }

}

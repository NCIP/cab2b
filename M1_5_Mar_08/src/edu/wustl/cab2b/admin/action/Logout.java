/**
 * 
 */
package edu.wustl.cab2b.admin.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.util.Cab2bConstants;
import edu.wustl.catissuecore.applet.AppletConstants;

/**
 * @author atul_jawale
 *
 */

public class Logout implements SessionAware {
    private Map session;

    /**
     * @return the session
     */
    public Map getSession() {
        return session;
    }

    /**
     * sets session
     */
    public void setSession(final Map session) {
        this.session = session;
    }

    /**
     * 
     * @return result
     */
    public String execute() {

        getSession().clear();
        return Cab2bConstants.SUCCESS;
    }
}

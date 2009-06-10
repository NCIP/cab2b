/**
 * 
 */
package edu.wustl.cab2b.admin.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.admin.util.Utility;

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
	 * 
	 * @param session
	 */
	public void setSession(final Map session) {
		this.session = session;
	}

	/**
	 * 
	 * @return result
	 */
	public String execute() {
	    //This will create a thread and allow user to logout.
        new Thread(new Runnable() {
            public void run() {
                Utility.refreshCab2bCache();

            }
        }).start();

		return AdminConstants.SUCCESS;
	}
}

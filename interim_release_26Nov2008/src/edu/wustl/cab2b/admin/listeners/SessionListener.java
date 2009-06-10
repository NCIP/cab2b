/**
 * 
 */
package edu.wustl.cab2b.admin.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import edu.wustl.cab2b.admin.util.Utility;

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
		Utility.refreshCab2bCache();

	}

}

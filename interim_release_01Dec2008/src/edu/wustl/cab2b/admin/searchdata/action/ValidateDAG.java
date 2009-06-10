package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.SUCCESS;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

/**
 * 
 * @author lalit_chand
 * 
 */
public class ValidateDAG implements SessionAware, ServletResponseAware {

	private Map session;

	private HttpServletResponse response;

	/**
	 * @param response
	 * 
	 */
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
	 */
	/**
	 * Sets session
	 * 
	 * @param session
	 * 
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * Returns session
	 * 
	 * @return
	 */
	public Map getSession() {
		return session;
	}

	/**
	 * @return
	 */
	public String execute() {
		return SUCCESS;
	}
}

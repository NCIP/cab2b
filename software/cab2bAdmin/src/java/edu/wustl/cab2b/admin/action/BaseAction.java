/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.action;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * This class implements all the interfaces .
 * @author atul_jawale
 * @author lalit_chand  
 */
public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, SessionAware,
        ServletContextAware {
    private static final long serialVersionUID = -7729440970860405895L;

    /** The HTTP request */
    protected HttpServletRequest request;

    /** The HTTP response */
    protected HttpServletResponse response;

    /** The Session object*/
    protected Map session;

    /** The context */
    protected ServletContext servletContext = null;
    
    /**
     * It initializes HttpServletRequest
     * @param request 
     */
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return HttpServletRequests
     */

    public HttpServletRequest getServletRequest() {
        return request;
    }

    /**
     * It initializes HttpServletResponse
     * @param response 
     */
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Sets session object.
     * @param session
     */
    public void setSession(Map session) {
        this.session = session;
    }

    /**
     * @return the session
     */

    public Map getSession() {
        return session;
    }
    /**
     * Sets servlet context.
     * @param servletContext 
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
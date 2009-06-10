/**
 * 
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
 * 
 */
public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, SessionAware,
        ServletContextAware {
    private static final long serialVersionUID = -7729440970860405895L;

    private String submit = null;

    /**
     * @return the submit
     */
    public String getSubmit() {
        return submit;
    }

    /**
     * @param submit the submit to set
     */
    public void setSubmit(String submit) {
        this.submit = submit;
    }

    /**  */
    public HttpServletRequest request;

    /**  */
    public HttpServletResponse response;

    /**  */
    public Map session;

    /**  */
    public ServletContext servletContext = null;

    /**
     * It initializes HttpServletRequest
     * @param request 
     */
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 
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

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    /**
     * Sets session object.
     * 
     * @param session
     * 
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

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts2.util.ServletContextAware#setServletContext(javax.servlet
     *      .ServletContext)
     */
    /**
     * Sets servlet context.
     * @param servletContext 
     * 
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}

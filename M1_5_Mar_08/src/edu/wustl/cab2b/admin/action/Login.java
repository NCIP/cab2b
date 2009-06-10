/**
 * 
 */

package edu.wustl.cab2b.admin.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.wustl.cab2b.admin.util.Cab2bConstants;

/**
 * @author atul_jawale
 *
 */
public class Login extends ActionSupport implements SessionAware {

    /**
     * 
     */
    private static final long serialVersionUID = -6968058664730781257L;

    private Map session = new HashMap<String, Object>();

    /** @param session */
    public void setSession(final Map session) {
        this.session = session;

    }

    /** @return the session
     */
    public Map getSession() {

        return session;
    }

    @Override
    public String execute() {
       return Cab2bConstants.SUCCESS;
    }

    private String userName = null;

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    private String password = null;

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

}

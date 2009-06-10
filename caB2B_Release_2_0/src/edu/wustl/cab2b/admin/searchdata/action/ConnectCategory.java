/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;

/**
 * @author atul_jawale
 * @author lalit_chand  
 *
 */
public class ConnectCategory extends BaseAction implements SessionAware {
    private static final long serialVersionUID = -1054042346804011812L;

    private Map session;

    public void setSession(final Map session) {
        this.session = session;
    }

    public Map getSession() {
        return session;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String execute() {
        return SUCCESS;
    }

}

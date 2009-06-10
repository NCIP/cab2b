/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;

/**
 * @author atul_jawale
 * @author lalit_chand
 */
public class CreateCategory extends BaseAction implements  SessionAware {

    private String title;

    private String description;

    private Map session;

    public void setSession(final Map session) {
        this.session = session;
    }

    public Map getSession() {

        return session;
    }

    private static final long serialVersionUID = 7660830850643731270L;

    /**
     * 
     * @return
     */
    @Override
    public String execute() {
        return SUCCESS;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}

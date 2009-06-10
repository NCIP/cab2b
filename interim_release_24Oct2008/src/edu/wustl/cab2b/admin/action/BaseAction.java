/**
 * 
 */
package edu.wustl.cab2b.admin.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author atul_jawale
 *
 */
public class BaseAction extends ActionSupport {
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

}

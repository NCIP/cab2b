package edu.wustl.cab2b.admin.searchdata.action;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import static edu.wustl.cab2b.admin.util.AdminConstants.*;

public class ValidateDAG implements SessionAware, ServletResponseAware {

    private Map session;

    private HttpServletResponse response;

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;

    }

    public void setSession(Map session) {
        this.session = session;

    }

    public Map getSession() {

        return session;
    }

    public String execute() {

        return SUCCESS;
    }
}

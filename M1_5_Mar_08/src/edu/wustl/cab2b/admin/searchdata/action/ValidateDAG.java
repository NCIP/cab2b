package edu.wustl.cab2b.admin.searchdata.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import edu.wustl.cab2b.admin.bizlogic.ValidateDAGBizLogic;
import edu.wustl.catissuecore.flex.dag.DAGConstant;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQuery;

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

        HttpServletRequest request = ServletActionContext.getRequest();
        IQuery query = (IQuery) getSession().get(DAGConstant.QUERY_OBJECT);
        String validationMessage = new ValidateDAGBizLogic().getValidateMessage(query, request);
        try {
           
            response.setContentType("text/html");
            response.getWriter().write(validationMessage);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

package edu.wustl.cab2b.admin.searchdata.action;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.flex.CategoryDagPanel;
import edu.wustl.cab2b.admin.util.AttributePair;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;
import edu.wustl.cab2b.admin.util.InterModelConnectionsUtil;

/**
 * This class persists the InterModelConnection beetween the two classes 
 * @author lalit_chand
 *
 */
public class PersistInterModel extends BaseAction implements ServletRequestAware, ServletResponseAware,
        SessionAware {

    private static final long serialVersionUID = 1L;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private Map session;

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setSession(final Map session) {
        this.session = session;
    }

    public String execute() throws IOException {

        CategoryDagPanel categoryDagPanel = (CategoryDagPanel) session.get(CATEGORY_INSTANCE);
        AttributePair attributePair = categoryDagPanel.getAttributePair();
        response.setContentType("text/html");
        String returnAction;
        try {
            InterModelConnectionsUtil.saveInterModelConnection(attributePair);
            returnAction = SUCCESS;
        } catch (IllegalArgumentException iAE) {
            iAE.printStackTrace();
            returnAction = iAE.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            returnAction = e.getMessage();
        }
        response.getWriter().write(returnAction);
        return null;

    }
}

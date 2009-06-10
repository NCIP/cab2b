package edu.wustl.cab2b.admin.searchdata.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.CuratePathBizLogic;
import edu.wustl.cab2b.admin.bizlogic.PathBizLogic;
import edu.wustl.cab2b.admin.flex.CategoryDagPanel;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;

/**
 * This class is called by Ajax function to save curate path and checks multiple roots.
 * @author lalit_chand
 *
 */
public class CuratePath extends BaseAction implements ServletRequestAware, ServletResponseAware, SessionAware {
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
        CuratePathBizLogic bizLogic = new CuratePathBizLogic();
        response.setContentType("text/html");

        boolean flag = ((CategoryDagPanel) session.get(CATEGORY_INSTANCE)).checkMultipleRoot();
        if (flag) {
            response.getWriter().write("Mutliple Root Exists !");
            return null;
        } else {
            String isSelected = request.getParameter("isSelected");
            boolean temp = isSelected.equals("true");
            CuratedPath curatePath = new CuratedPath();
            bizLogic.initialize(session, curatePath, temp);
            PathBizLogic pathBizLogic = new PathBizLogic();
            try {
                if (!pathBizLogic.isDuplicate(curatePath)) {
                    pathBizLogic.saveCuratedPath(curatePath);
                    PathFinder.getInstance().addCuratedPath(curatePath);
                    response.getWriter().write("Saved successfully");
                } else {
                    response.getWriter().write("This Path is already curated");
                }

                return null;
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("Error occured--->" + e.getMessage());
                return null;
            }
        }
    }

}

package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.CATEGORY_INSTANCE;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.CuratePathBizLogic;
import edu.wustl.cab2b.admin.flex.CategoryDagPanel;
import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * This class is called by Ajax function to save curate path and checks multiple roots.
 * @author lalit_chand
 *
 */
public class CuratePath extends BaseAction implements ServletRequestAware, ServletResponseAware, SessionAware {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(CuratePath.class);

    private HttpServletRequest request;

    private HttpServletResponse response;

    private Map session;

    /**
     *  It initializes HttpServletRequest 
     */
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     *  It initializes HttpServletResponse 
     */
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     *  It initializes HttpSession 
     */
    public void setSession(final Map session) {
        this.session = session;
    }

    /**
     * Main method gets called from Ajax call 
     */
    public String execute() throws IOException {

        response.setContentType("text/html");

        boolean flag = ((CategoryDagPanel) session.get(CATEGORY_INSTANCE)).areMultipleRoots();
        if (flag) {
            response.getWriter().write("Mutliple Root Exists !");
            return null;
        } else {
            boolean isSelected = "true".equals(request.getParameter("isSelected"));

            CategoryDagPanel categoryDAGPanel = (CategoryDagPanel) session.get(AdminConstants.CATEGORY_INSTANCE);
            Map<String, IPath> idVsPathMap = (Map<String, IPath>) session.get(AdminConstants.ID_VS_PATH_MAP);

            CuratePathBizLogic bizLogic = new CuratePathBizLogic();
            try {
                boolean isSaved = bizLogic.saveCuratedPath(categoryDAGPanel, idVsPathMap, isSelected);
                if (isSaved) {
                    response.getWriter().write("Saved successfully");
                } else {
                    response.getWriter().write("This Path is already curated");
                }

                return null;
            } catch (Exception e) {
                logger.debug(e.getStackTrace());
                response.getWriter().write("Error occured--->" + e.getMessage());
                return null;
            }
        }
    }

}

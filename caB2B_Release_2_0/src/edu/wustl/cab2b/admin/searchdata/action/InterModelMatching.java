/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.util.AttributePair;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;
import edu.wustl.cab2b.admin.util.InterModelConnectionsUtil;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.util.Utility;

/**
 * This class retrieves the matching attribute pairs of the selected entities. 
 * @author chetan_patil
 *
 */
public class InterModelMatching extends BaseAction implements ServletRequestAware, ServletResponseAware,
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
        if (request.getParameter("newWindow") != null) {
            return SUCCESS;
        }

        AbstractEntityCache entityCache = AbstractEntityCache.getCache();

        String sourceEntityId = ((Integer) session.get(SOURCE_CLASS_ID)).toString();
        EntityInterface sourceEntity = entityCache.getEntityById(Long.valueOf(sourceEntityId));

        String targetEntityId = ((Integer) session.get(TARGET_CLASS_ID)).toString();
        EntityInterface targetEntity = entityCache.getEntityById(Long.valueOf(targetEntityId));

        String returnAction = SUCCESS;
        try {
            Set<AttributePair> attributePairSet = InterModelConnectionsUtil.determineConnections(sourceEntity,
                                                                                                 targetEntity);
            session.put(ATTRIBUTE_PAIR_SET, attributePairSet);
            session.put(ATTRIBUTE_LIST1, sourceEntity.getAttributeCollection());
            session.put(ATTRIBUTE_LIST2, targetEntity.getAttributeCollection());

            String sourceEntityName = Utility.getDisplayName(sourceEntity);
            String targetEntityName = Utility.getDisplayName(targetEntity);

            session.put(ENTITY1, sourceEntityName);
            session.put(ENTITY2, targetEntityName);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            returnAction = e.getMessage();
        }

        response.setContentType("text/html");
        response.getWriter().write(returnAction);

        return null;
    }
}

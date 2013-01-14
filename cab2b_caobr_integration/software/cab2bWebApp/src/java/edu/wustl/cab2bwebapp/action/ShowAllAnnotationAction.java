/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.AnnotationDVO;

public class ShowAllAnnotationAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ShowAllAnnotationAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for generating 
     * dynamic HTML for add limit page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        ActionForward actionForward = null;
        try {
            String resourceId = (String) request.getParameter(Constants.RESOURCE_ID);

            Map<String, AnnotationDVO> resourceAnnotationDVOMap = (Map<String, AnnotationDVO>) session.getAttribute(Constants.RESOURCE_ANNOTATIONMAPS);

            AnnotationDVO annotationDVO = resourceAnnotationDVOMap.get(resourceId);

            session.setAttribute(Constants.DISPLAY_All_ANNOTATION, annotationDVO);
            ActionForward forward = mapping.findForward(Constants.DISPLAY_All_ANNOTATION);
            actionForward = new ActionForward(forward.getName(), forward.getPath(), false);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.addlimit.failure", e.getMessage());
            errors.add(Constants.FATAL_ADD_LIMIT_FAILURE, error);
            saveErrors(request, errors);
            actionForward = mapping.findForward(Constants.FORWARD_FAILURE);
        }
        return actionForward;
    }
}

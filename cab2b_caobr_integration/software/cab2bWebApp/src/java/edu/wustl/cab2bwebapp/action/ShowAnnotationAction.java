package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.List;
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

public class ShowAnnotationAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ShowAnnotationAction.class);

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
            String key = (String) request.getParameter("key");

            Map<String, List<String>> parsedResult = (Map<String, List<String>>) session.getAttribute(Constants.PARSED_RESULT);

            List<String> elementIds = parsedResult.get(key);
            String keyValues[] = key.split("custom_separator");
            String resourceDescription = keyValues[1];
            String resourceLogo = keyValues[2];

            StringBuilder temp = new StringBuilder();

            for (String val : elementIds) {
                String values[] = val.split("custom_separator");
                String elementId = values[0];
                String elementDesc = values[1];
                String resourceUrl = values[2];

                int length = elementDesc.length();
                String desc = "";
                if (length < 75) {
                    desc = elementDesc +"....<br>";
                } else
                    desc = elementDesc.substring(0, 75) + "....<br>";

                temp = temp.append(
                                   "<a class='link' href='" + resourceUrl + "' target='_blank'" + "title='"
                                           + elementDesc + "'>" + elementId + "</a>").append(
                                                                                             ": &nbsp;&nbsp;&nbsp;&nbsp;").append(
                                                                                                                                  desc);
            }
            session.setAttribute(Constants.DISPLAY_ANNOTATION, temp.toString());
            session.setAttribute(Constants.SELECTED_RESOURCE_DES, resourceDescription);
            session.setAttribute(Constants.SELECTED_RESOURCE_IMAGE, resourceLogo);
            ActionForward forward = mapping.findForward(Constants.DISPLAY_ANNOTATION);
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

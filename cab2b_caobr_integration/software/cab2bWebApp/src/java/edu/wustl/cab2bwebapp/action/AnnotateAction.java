package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.cagrid.caObr.client.CaObrClient;

import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;

public class AnnotateAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AddLimitAction.class);

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
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        String actionForward = null;
        CaObrClient client = new CaObrClient("http://ps4266:8080/wsrf/services/cagrid/CaObr");
        try {
            String index = request.getParameter("index");
            String[] indexs = index.split("sep");

            List<List<SearchResultDVO>> searchResultview = (List<List<SearchResultDVO>>) request.getSession().getAttribute(
                                                                                                                           Constants.SEARCH_RESULTS_VIEW);

            for (String intex : indexs) {
                List<SearchResultDVO> searchResultDVOList = searchResultview.get(Integer.parseInt(intex));
                for (SearchResultDVO dvo : searchResultDVOList) {
                    Object values = dvo.getValue();
                    StringBuilder stb = new StringBuilder();
                    if (values instanceof String) {

                        String tokens[] = ((String) values).split(" ");
                        boolean[] flags = client.isConceptsInAllOntologies(tokens);

                        int i = 0;
                        for (String token : tokens) {
                            if (flags[i]) {
                                token = (new StringBuilder("<a class='link' href='AnnotationResult.do?token=")).append(
                                                                                                                       token).append(
                                                                                                                                     "'>").append(
                                                                                                                                                  token).append(
                                                                                                                                                                "</a>").toString();
                            }
                            if (i == tokens.length - 1) {
                                stb.append(token);
                            }
                            stb.append(token).append(" ");
                            i++;

                        }
                    }
                    dvo.setValue(stb.toString());
                }
                actionForward = Constants.FORWARD_SEARCH_RESULTS_PANEL;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.addlimit.failure");
            errors.add(Constants.FATAL_ADD_LIMIT_FAILURE, error);
            saveErrors(request, errors);
            actionForward = Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}

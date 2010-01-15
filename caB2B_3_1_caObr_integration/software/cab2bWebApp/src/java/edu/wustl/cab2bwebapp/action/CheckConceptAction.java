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
import org.cagrid.caobr.client.CaObrClient;

import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;
import edu.wustl.cab2bwebapp.util.caObr.PropertyLoader;

public class CheckConceptAction extends Action {

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
        CaObrClient client = new CaObrClient(PropertyLoader.getCaObrServiceURL());
        try {
            String index = request.getParameter(Constants.INDEX);
            String[] indexs = index.split(Constants.INDEX_SEPEARATOR);

            List<List<SearchResultDVO>> searchResultview = (List<List<SearchResultDVO>>) request.getSession().getAttribute(
                                                                                                                           Constants.SEARCH_RESULTS_VIEW);

            for (String intex : indexs) {
                List<SearchResultDVO> searchResultDVOList = searchResultview.get(Integer.parseInt(intex));
                for (SearchResultDVO dvo : searchResultDVOList) {
                    Object values = dvo.getValue();
                    if (values instanceof String) {
                        StringBuilder stb = new StringBuilder();
                        String tokens[] = ((String) values).split(" ");
                        boolean[] flags = client.isConceptsInAnyOntology(tokens);

                        int i = 0;
                        for (String token : tokens) {
                            if (flags[i]) {
                                token = (new StringBuilder(
                                        "<a class='link' href='ShowLimitedAnnotationResult.do?token=")).append(
                                                                                                               token).append(
                                                                                                                             "' target='_blank' >").append(
                                                                                                                                                           token).append(
                                                                                                                                                                         "</a>").toString();
                            }
                            // removed check for white space 
                            stb.append(token).append(" ");
                            i++;
                        }
                        dvo.setValue(stb.toString());
                    }
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

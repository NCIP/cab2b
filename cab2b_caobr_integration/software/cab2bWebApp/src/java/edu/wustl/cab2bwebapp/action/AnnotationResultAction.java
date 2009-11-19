package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.List;

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
import org.cagrid.caObr.client.CaObrClient;

import demo.AnnotationService;
import demo.AnnotationServiceThreadPool;

import per.edu.wustl.Annotation;
import per.edu.wustl.Ontology;
import per.edu.wustl.Resource;
import edu.wustl.cab2bwebapp.bizlogic.caNanoLab.DisplayResourceBizlogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.AnnotationDVO;

public class AnnotationResultAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AnnotationResultAction.class);

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
        HttpSession session = request.getSession();
        CaObrClient client = new CaObrClient("http://ps4266:8080/wsrf/services/cagrid/CaObr");
        String actionForward = null;
        try {
            String token = request.getParameter("token");
            List<AnnotationDVO> resourceListDVO = null;
            Annotation annotations[] = null;
            logger.info("Fetching All Annotation");
            executeThreads(client.getAllOntologies(), client.getAllResources(), token);
            annotations = AnnotationService.annotations.toArray(new Annotation[AnnotationService.annotations.size()]);

            if (annotations != null) {
                resourceListDVO = DisplayResourceBizlogic.getResult(annotations, session);
            }
            session.setAttribute(Constants.DISPLAY_ANNOTATION, resourceListDVO);
            session.setAttribute("token", token);
            actionForward = Constants.DISPLAY_RESOURCE;

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

    private void executeThreads(Ontology[] fromOntologies, Resource[] fromResources, String term) {
        AnnotationServiceThreadPool mt = new AnnotationServiceThreadPool();

        for (Resource resource : fromResources) {
            Resource[] res = new Resource[1];
            res[0] = resource;
            AnnotationService t = new AnnotationService(fromOntologies, res, term);
            mt.runTask(t);
        }
        mt.shutDown();
        long i = 0;
        while (!mt.getThreadPool().isTerminated()) {
            i++;
            if (i == 500000000) {
                System.out.println("Active executing Thread for fetching annotation :"
                        + mt.getThreadPool().getActiveCount());
                i = 0;
            }
        }
    }
}
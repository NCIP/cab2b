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

import edu.wustl.cab2bwebapp.bizlogic.caNanoLab.DisplayResourceBizlogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.AnnotationDVO;
import edu.wustl.cab2bwebapp.util.caObr.AnnotationServiceThreadPool;
import edu.wustl.cab2bwebapp.util.caObr.ServiceInvoker;
import edu.wustl.caobr.Annotation;
import edu.wustl.caobr.Ontology;
import edu.wustl.caobr.Resource;

public class ShowLimitedAnnotationResultAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ShowLimitedAnnotationResultAction.class);

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
        String actionForward = null;
        try {
            String token = request.getParameter(Constants.TOKEN);
            List<AnnotationDVO> resourceListDVO = null;
            Annotation annotations[] = null;

            logger.info("Fetching All Annotation");

            Resource[] resources = null;
            resources = (Resource[]) session.getAttribute(Constants.RESOURCES);

            if (resources == null) {
                ServiceInvoker serviceInvoker = new ServiceInvoker();
                serviceInvoker = new ServiceInvoker();
                resources = serviceInvoker.getResources();
                session.setAttribute(Constants.RESOURCES, resources);
            }

            Ontology[] ontologies = null;
            ontologies = (Ontology[]) session.getAttribute(Constants.ONTOLOGIES);

            if (ontologies == null) {
                ServiceInvoker serviceInvoker = new ServiceInvoker();
                serviceInvoker = new ServiceInvoker();
                ontologies = serviceInvoker.getOntologies();
                session.setAttribute(Constants.ONTOLOGIES, ontologies);
            }

            annotations = getAnnotation(ontologies, resources, token);
            if (annotations != null) {
                resourceListDVO = DisplayResourceBizlogic.getResult(annotations, session);
            }
            session.setAttribute(Constants.SAVE_ANNOTATION, resourceListDVO);
            session.setAttribute(Constants.TOKEN, token);
            actionForward = Constants.DISPLAY_LIMITED_ANNOTATION;

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

    private Annotation[] getAnnotation(Ontology[] fromOntologies, Resource[] fromResources, String term) {
        AnnotationServiceThreadPool mt = AnnotationServiceThreadPool.getInstance();
        return mt.getAnnotations(fromOntologies, fromResources, term);

    }
}
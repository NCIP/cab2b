package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.QueryConditionDVO;
import edu.wustl.cab2bwebapp.dvo.QueryStatusDVO;
import edu.wustl.cab2bwebapp.dvo.ServiceInstanceDVO;

/**
 * @author chetan_pundhir
 * This action class is called to prepare for and forward to the application dash board.
 *
 */

public class DisplayDashboardAction extends Action {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(DisplayDashboardAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for setting 
     * parameters required at the dash board page.
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
        try {
            Collection<QueryStatusDVO> queryStatusDVOList = new ArrayList<QueryStatusDVO>();

            QueryURLStatusOperations opr = new QueryURLStatusOperations();
            UserInterface user = (UserInterface) request.getSession().getAttribute(Constants.USER);

            //Update all background query status in database for the logged user.            
            UserBackgroundQueries.getInstance().updateBackgroundQueryStatusForUser(user);

            //Get query status updates from database.
            Collection<QueryStatus> qsCollection = opr.getAllQueryStatusByUser(user);

            Iterator<QueryStatus> i = qsCollection.iterator();
            int inProgressQueryCount = 0;
            int completedQueryCount = 0;
            while (i.hasNext()) {
                QueryStatus qs = i.next();
                QueryStatusDVO queryStatusDVO = new QueryStatusDVO();
                ICab2bQuery query = qs.getQuery();

                queryStatusDVO.setType("ANDed".equalsIgnoreCase(query.getType()) ? "Saved Search" : "Keyword");
                queryStatusDVO.setStatus(qs.getStatus());
                if (qs.getResultCount() != null) {
                    queryStatusDVO.setResultCount(qs.getResultCount());
                }
                queryStatusDVO.setExecutedOn(qs.getQueryStartTime());

                List<QueryConditionDVO> queryConditions = new ArrayList<QueryConditionDVO>();
                String pattern = "(.*)\\((.*)\\)(.*)";
                String values[] = qs.getQueryConditions().split(";");
                Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                String conditionValue = null;
                for (int j = 0; j < values.length; j++) {
                    QueryConditionDVO queryCondition = new QueryConditionDVO();
                    Matcher m = p.matcher(values[j]);
                    m.find();
                    queryCondition.setParameter(m.group(1));
                    queryCondition
                        .setCondition(edu.wustl.cab2b.common.util.Utility.getFormattedString(m.group(2)));
                    conditionValue = m.group(3);
                    queryCondition.setValue(conditionValue);
                    queryConditions.add(queryCondition);
                }
                if (query instanceof KeywordQuery) {
                    KeywordQuery keywordQuery = (KeywordQuery) query;
                    StringBuffer title =
                            new StringBuffer("Keyword search for ").append('"').append(conditionValue).append('"')
                                .append(" on ").append('"').append(
                                                                   keywordQuery.getApplicationGroup()
                                                                       .getModelGroupName()).append('"');
                    queryStatusDVO.setTitle(title.toString());
                } else {
                    queryStatusDVO.setTitle(query.getName());
                }
                queryStatusDVO.setConditions(queryConditions);

                List<ServiceInstanceDVO> serviceInstances = new ArrayList<ServiceInstanceDVO>();
                Iterator<URLStatus> itr = qs.getUrlStatus().iterator();
                while (itr.hasNext()) {
                    URLStatus urlStatus = itr.next();
                    String hostingIntitutionname =
                            Utility.getHostingInstitutionName(new ServiceURLOperations()
                                .getServiceURLbyURLLocation(urlStatus.getUrl()));
                    ServiceInstanceDVO serviceInstance = new ServiceInstanceDVO();
                    serviceInstance.setName(hostingIntitutionname);
                    serviceInstance.setStatus(urlStatus.getStatus());
                    serviceInstance.setResultCount(urlStatus.getResultCount() == null ? 0 : urlStatus
                        .getResultCount());
                    serviceInstances.add(serviceInstance);
                }
                serviceInstances = merge(serviceInstances);
                queryStatusDVO.setServiceInstances(serviceInstances);
                queryStatusDVO.setFileName(qs.getFileName());
                queryStatusDVOList.add(queryStatusDVO);
                if (qs.getStatus().equals(AbstractStatus.Processing)) {
                    inProgressQueryCount++;
                } else {
                    completedQueryCount++;
                }
            }

            request.getSession().setAttribute("completedQueryCount", completedQueryCount);
            request.getSession().setAttribute("inProgressQueryCount", inProgressQueryCount);
            request.setAttribute("queryStatusDVOList", queryStatusDVOList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.displaydashboard.failure", e.getMessage());
            errors.add(Constants.FATAL_DISPLAY_DASHBOARD_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
        ActionForward actionForward =
                mapping.findForward(request.getHeader(Constants.AJAX_CALL) == null ? Constants.FORWARD_DASHBOARD
                        : Constants.FORWARD_DASHBOARD_PANEL);
        ActionForward actionRedirect = new ActionForward(actionForward.getName(), actionForward.getPath(), false);
        return actionRedirect;
    }

    private List<ServiceInstanceDVO> merge(List<ServiceInstanceDVO> serviceInstances) {
        Map<String, ServiceInstanceDVO> map = new HashMap<String, ServiceInstanceDVO>();
        for (ServiceInstanceDVO dvo : serviceInstances) {
            if (map.containsKey(dvo.getName())) {
                ServiceInstanceDVO oldDVO = map.get(dvo.getName());
                oldDVO.setResultCount(oldDVO.getResultCount() + dvo.getResultCount());
                oldDVO.setStatus(decide(oldDVO.getStatus(), dvo.getStatus()));
            } else {
                map.put(dvo.getName(), dvo);
            }
        }
        return new ArrayList<ServiceInstanceDVO>(map.values());
    }

    private String decide(String s1, String s2) {
        String status = "";
        if (s1.equals(AbstractStatus.Processing) || s2.equals(AbstractStatus.Processing)) {
            status = AbstractStatus.Processing;
        } else if (s1.equals(AbstractStatus.Complete) && s2.equals(AbstractStatus.Complete)) {
            status = AbstractStatus.Complete;
        } else if (s1.equals(AbstractStatus.SUSPENDED) || s2.equals(AbstractStatus.SUSPENDED)) {
            status = AbstractStatus.SUSPENDED;
        } else {
            status = AbstractStatus.Complete_With_Error;
        }
        return status;
    }
}
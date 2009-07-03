package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SavedQueryDVO;

/**
* Action for populating things needed as prerequisites for searchresults.jsp .
*/
public class PreExecuteQueryAction extends Action {

   /**
    * The execute method is called by the struts action flow and it calls the code to retrieve the 
    * query results.
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return ActionForward
    * @throws IOException
    * @throws ServletException
    */
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException 
   {
       String actionForward   = Constants.FORWARD_SEARCH_RESULTS;
       HttpSession session    = request.getSession();
       Boolean isFirstRequest = (Boolean) session.getAttribute(Constants.IS_FIRST_REQUEST);
       if(isFirstRequest == null)   // its a first request, set isFirstRequest = true and it will be set as false at onload of searchresults.jsp
       {
           isFirstRequest = true;
           session.setAttribute(Constants.IS_FIRST_REQUEST, isFirstRequest);
       }
           
       if(isFirstRequest)   //  in 1st request , set all unimp values to null and empty lists and take first params from request
       {
           Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;
           List<SavedQueryDVO> queryList                = new ArrayList<SavedQueryDVO>();
           Collection<ServiceURLInterface> failedURLS   = null;
           SavedQueryBizLogic savedQueryBizLogic        =(SavedQueryBizLogic) session.getServletContext().getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
           Long queryId                                 = Long.parseLong(request.getParameter(Constants.QUERY_ID));
           ICab2bQuery query                            = savedQueryBizLogic.getQueryById(queryId);
           String conditionstr                          = request.getParameter(Constants.CONDITION_LIST);
           String[] modelGroupNames                     =(String[]) request.getParameterValues(Constants.MODEL_GROUPS);
           List<String> urlsForSelectedQueries          = query.getOutputUrls();
           urlsForSelectedQueries.add(0, Constants.ALL_HOSTING_INSTITUTIONS);
           
           //set query name in dropdown
           SavedQueryDVO savedQuery = new SavedQueryDVO();
           savedQuery.setName(query.getName());
           savedQuery.setResultCount(0);
           queryList.add(savedQuery);

           // to be saved in session
           session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
           session.setAttribute(Constants.SAVED_QUERIES, queryList);
           session.setAttribute(Constants.FAILED_SERVICES, failedURLS);
           session.setAttribute(Constants.QUERY_ID, queryId);
           session.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
           session.setAttribute(Constants.CONDITION_LIST, conditionstr);
           session.setAttribute(Constants.MODEL_GROUPS, modelGroupNames);
           session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedURLS != null ? failedURLS.size(): 0);
           session.setAttribute(Constants.SEARCH_RESULTS_VIEW, Constants.PROCESSING);   //1st request will say processing
       }
       return mapping.findForward(actionForward);   //else if its second request bcz of page refresh by display tag, just forward the page to the same jsp.
   }
}
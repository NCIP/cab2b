/**
 *
 */

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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.FailedTargetURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.IQueryUpdateBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.SearchQueryExecutor;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.util.Utility;

/**
 * Action for executing query related operations.
 * @author deepak_shingan, chetan_patil
 */
public class ExecuteQueryAction extends Action {

	private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExecuteQueryAction.class);

	/**
	 * The execute method is called by the struts action flow and it calls the code for retrieve the 
	 * query results.
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
		String conditionstr = request.getParameter("conditionList");
		if (conditionstr == null) {
			conditionstr = "";
		}

		HttpSession session = request.getSession();

		SavedQueryBizLogic savedQueryBizLogic = (SavedQueryBizLogic) session
				.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
		ICab2bQuery query = savedQueryBizLogic.getQueryById(Long.parseLong(request.getParameter(Constants.QUERY_ID)));

		//Update the IQuery according to parameterized conditions.
		String errorMessage = new IQueryUpdateBizLogic().setInputDataToQuery(conditionstr, query.getConstraints(),
				null, query);
		if (!errorMessage.equals("")) {
			logger.error(errorMessage);
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("error.query.execute");
			errors.add(Constants.ERROR_QUERY_EXECUTE, error);
			saveErrors(request, errors);
			return mapping.findForward(Constants.FORWARD_FAILURE);
		}

		Collection<ICab2bQuery> queries = new ArrayList<ICab2bQuery>();
		queries.add(query);
		GlobusCredential proxy = (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);
		Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = new SearchQueryExecutor().execute(
				queries, proxy);
		queries = searchResults.keySet();
		List<String> queryNameList = Utility.getQueryNameList(queries);

		List<String> urlsForSelectedQueries = null;
		List<Map<AttributeInterface, Object>> finalResult = null;
		Collection<FailedTargetURL> failedServiceURLs = new ArrayList<FailedTargetURL>();
		Collection<ServiceURLInterface> failedSercives = new ArrayList<ServiceURLInterface>();
		if (queryNameList != null || !queryNameList.isEmpty()) {
			String selectedQueryName = queryNameList.get(0);
			for (ICab2bQuery queryObj : queries) {
				if (queryObj.getName().equals(selectedQueryName)) {
					urlsForSelectedQueries = queryObj.getOutputUrls();
					finalResult = searchResults.get(queryObj).getResultForAllUrls();
					failedServiceURLs.addAll(searchResults.get(queryObj).getFailedServiceUrl());
					break;
				}
			}
			if (!failedServiceURLs.isEmpty()) {
				for (FailedTargetURL failedurl : failedServiceURLs) {
					ServiceURLInterface serviceurl = new ServiceURLOperations().getServiceURLbyURLLocation(failedurl
							.getTargetUrl());
					failedSercives.add(serviceurl);
				}
			}
		}
		if (!finalResult.isEmpty()) {
			request.setAttribute(Constants.SEARCH_RESULTS_VIEW, finalResult);
		}
		urlsForSelectedQueries.add(0, "All Hosting Institutions");
		request.setAttribute(Constants.FAILED_SERVICES, failedSercives);
		request.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
		request.setAttribute(Constants.SAVED_SEARCHES, queryNameList);
		session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
		return mapping.findForward(Constants.FORWARD_SEARCH_RESULTS);
	}
}

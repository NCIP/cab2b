/**
 *
 */

package edu.wustl.cab2bwebapp.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.FailedTargetURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2bwebapp.actionform.KeywordSearchForm;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.SearchQueryExecutor;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_patil
 *
 */
public class KeywordSearchAction extends Action {

	private static final org.apache.log4j.Logger logger = Logger.getLogger(KeywordSearchAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		KeywordSearchForm keywordSearchForm = (KeywordSearchForm) form;
		String[] modelGroupNames = keywordSearchForm.getModelGroups();
		String keyword = keywordSearchForm.getKeyword();
		HttpSession session = request.getSession();

		String findForward = null;
		try {
			SavedQueryBizLogic savedQueryBizLogic = (SavedQueryBizLogic) session
					.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);

			final Set<EntityGroupInterface> entityGroups = Utility.getEntityGroupsForModelGroups(modelGroupNames);
			final Collection<ICab2bQuery> keywordSearches = savedQueryBizLogic.getKeywordQueries(entityGroups);

			GlobusCredential globusCredential = (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);

			Map<ICab2bQuery, TransformedResultObjectWithContactInfo> queryResults = new SearchQueryExecutor()
					.execute(keywordSearches, keyword, globusCredential);

			//Result from all queries of all selected Models for all urls.
			Set<ICab2bQuery> queries = queryResults.keySet();
			List<Map<AttributeInterface, Object>> finalResult = null;
			List<String> queryNameList = Utility.getQueryNameList(queries);
			List<String> urlsForSelectedQueries = null;
			Collection<FailedTargetURL> failedServiceURLs = new ArrayList<FailedTargetURL>();
			Collection<ServiceURLInterface> failedSercives = new ArrayList<ServiceURLInterface>();
			if (queryNameList != null || !queryNameList.isEmpty()) {
				String selectedQueryName = queryNameList.get(0);
				for (ICab2bQuery queryObj : queries) {
					if (queryObj.getName().equals(selectedQueryName)) {
						urlsForSelectedQueries = queryObj.getOutputUrls();
						finalResult = queryResults.get(queryObj).getResultForAllUrls();
						failedServiceURLs.addAll(queryResults.get(queryObj).getFailedServiceUrl());
						break;
					}
				}

				if (!failedServiceURLs.isEmpty()) {
					for (FailedTargetURL failedurl : failedServiceURLs) {
						ServiceURLInterface serviceurl = new ServiceURLOperations()
								.getServiceURLbyURLLocation(failedurl.getTargetUrl());
						failedSercives.add(serviceurl);
					}
				}
			}
			if (!finalResult.isEmpty()) {
				request.setAttribute(Constants.SEARCH_RESULTS_VIEW, finalResult);
			}
			urlsForSelectedQueries.add(0, "All Hosting Institutions");
			request.setAttribute(Constants.FAILED_SERVICES, failedSercives);
			request.setAttribute(Constants.SAVED_SEARCHES, queryNameList);
			request.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
			session.setAttribute(Constants.SEARCH_RESULTS, queryResults);
			findForward = Constants.FORWARD_SEARCH_RESULTS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("fatal.keywordsearch.failure");
			errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, error);
			saveErrors(request, errors);
			findForward = Constants.FORWARD_FAILURE;
		}
		return mapping.findForward(findForward);
	}
}

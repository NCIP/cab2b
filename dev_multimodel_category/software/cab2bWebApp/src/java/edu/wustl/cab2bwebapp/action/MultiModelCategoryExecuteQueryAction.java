package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.MultimodelCategoryQueryPreprocessor;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryUpdateBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SavedQueryDVO;

public class MultiModelCategoryExecuteQueryAction extends Action {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(MultiModelCategoryExecuteQueryAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        String actionForward = Constants.FORWARD_SEARCH_RESULTS;
        try {
            Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults = null;
            ICab2bQuery selectedQueryObj = null;

            HttpSession session = request.getSession();
            String[] modelGroupNames = (String[]) session.getAttribute(Constants.MODEL_GROUPS);
            SavedQueryBizLogic savedQueryBizLogic =
                    (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
            ICab2bQuery query = savedQueryBizLogic.getQueryById((Long) request.getAttribute(Constants.QUERY_ID));
            UserInterface user = (UserInterface) session.getAttribute(Constants.USER);
            GlobusCredential proxy = (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);
            String conditionstr = (String) session.getAttribute(Constants.CONDITION_LIST);
            if (conditionstr == null) {
                conditionstr = "";
            }

            //Update the IQuery according to parameterized conditions.
            String errorMessage =
                    new QueryUpdateBizLogic().setInputDataToQuery(conditionstr, query.getConstraints(), null,
                                                                  query);
            if (!errorMessage.equals("")) {
                throw new Exception(errorMessage);
            } else {
                Collection<ICab2bQuery> queries = new ArrayList<ICab2bQuery>();

                MultiModelCategoryQuery mmcQuery = (MultiModelCategoryQuery) query;
                new MultimodelCategoryQueryPreprocessor().preprocessQuery(mmcQuery);
                queries.addAll(mmcQuery.getSubQueries());

                ExecuteQueryBizLogic executeQueryBizLogic =
                        new ExecuteQueryBizLogic(queries, proxy, user, modelGroupNames);
                Thread.sleep(5000);
                //As each executor is invoked in a thread SearchQueryExecutor.executeAll()   
                //the next call executeQueryBizLogic.isProcessingFinished() returns immediately as TRUE 
                //This is because query execution hasn't even started. 
                //Adding sleep to allow at least one query executing thread to run
                int transformationMaxLimit = Integer.MAX_VALUE;
                while (!executeQueryBizLogic.isProcessingFinished()) {
                    Thread.sleep(1000); // to ensure next lines are not executed before server finishes keyword query.
                }
                searchResults = executeQueryBizLogic.getSearchResults(transformationMaxLimit);

                Collection<ICab2bQuery> allQueries = searchResults.keySet();

                //Collect query details to be shown on result page combo box 
                List<SavedQueryDVO> queryList = new ArrayList<SavedQueryDVO>();
                for (ICab2bQuery queryObj : allQueries) {
                    SavedQueryDVO savedQuery = new SavedQueryDVO();
                    savedQuery.setName(queryObj.getName());
                    TransformedResultObjectWithContactInfo res = searchResults.get(queryObj);
                    if (res != null) {
                        //It occurs in case of failed URLs
                        savedQuery.setResultCount(res.getResultForAllUrls().size());
                    }
                    queryList.add(savedQuery);
                }
                Collections.sort(queryList, new Comparator() {
                    public int compare(Object a, Object b) {
                        return (((SavedQueryDVO) b).getResultCount() - ((SavedQueryDVO) a).getResultCount());
                    }
                });
                session.setAttribute(Constants.SAVED_QUERIES, queryList);
                Iterator<ICab2bQuery> iter = allQueries.iterator();
                String highestResultCountQueryName = queryList.get(0).getName();

                //select the query object with highest number of results 
                while (iter.hasNext()) {
                    selectedQueryObj = iter.next();
                    if (selectedQueryObj.getName().equals(highestResultCountQueryName)) {
                        break;
                    }
                }
                List<String> urlsForSelectedQueries =
                        new ArrayList<String>(1 + selectedQueryObj.getOutputUrls().size());
                urlsForSelectedQueries.add(Constants.ALL_HOSTING_INSTITUTIONS);
                urlsForSelectedQueries.addAll(selectedQueryObj.getOutputUrls());
               
                if (searchResults.get(selectedQueryObj) != null) {
                    TransformedResultObjectWithContactInfo selectedQueryResult =
                            searchResults.get(selectedQueryObj);
                    Collection<ServiceURLInterface> failedURLS =
                            ExecuteQueryBizLogic.getFailedServiceUrls(selectedQueryResult.getFailedServiceUrl());

                    if (failedURLS != null && failedURLS.size() == 0) {
                        failedURLS = null;
                    }
                    session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedURLS != null ? failedURLS.size()
                            : 0);
                    session.setAttribute(Constants.FAILED_SERVICES, failedURLS);
                    session.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
                    session.setAttribute(Constants.SEARCH_RESULTS_VIEW, ExecuteQueryBizLogic
                        .getSearchResultsView(selectedQueryResult.getResultForAllUrls(), selectedQueryResult
                            .getAllowedAttributes()));
                }
                session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.home.failure", e.getMessage());
            errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, error);
            Writer writer = response.getWriter();
            response.setContentType("text/xml");
            writer.write("Exception");
        }
        return mapping.findForward(actionForward);
    }

}

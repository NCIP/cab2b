/**
 *
 */

package edu.wustl.cab2bwebapp.action;

import java.util.Collection;
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

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
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
            SavedQueryBizLogic savedQueryBizLogic = (SavedQueryBizLogic) session.getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);

            final Set<EntityGroupInterface> entityGroups = Utility.getEntityGroupsForModelGroups(modelGroupNames);
            final Collection<ICab2bQuery> keywordSearches = savedQueryBizLogic.getKeywordQueries(entityGroups);

            GlobusCredential globusCredential = (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);

            Map<ICab2bQuery, TransformedResultObjectWithContactInfo> queryResults = new SearchQueryExecutor().execute(
                                                                                                                      keywordSearches,
                                                                                                                      keyword,
                                                                                                                      globusCredential);
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

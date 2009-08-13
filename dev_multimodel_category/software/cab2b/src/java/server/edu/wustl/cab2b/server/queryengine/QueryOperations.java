/**
 *
 */
package edu.wustl.cab2b.server.queryengine;

import static edu.wustl.cab2b.common.util.Constants.MMC_ENTITY_GROUP_NAME;
import static edu.wustl.cab2b.common.util.Constants.MULTIMODELCATEGORY;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.globus.gsi.GlobusCredential;
import org.hibernate.HibernateException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.authentication.util.AuthenticationUtility;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.QueryType;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.category.PopularCategoryOperations;
import edu.wustl.cab2b.server.util.UtilityOperations;
import edu.wustl.common.hibernate.HibernateCleanser;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * @author chetan_patil
 *
 */
public class QueryOperations extends QueryBizLogic<ICab2bQuery> {

    /**
     * This method checks whether the given query name has already been used by the given user or not.
     *
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#isQueryNameDuplicate(java.lang.String)
     *
     * @param queryName name of the query that is to be verified
     * @return true if the queryName is duplicate; false if not
     */
    public boolean isQueryNameDuplicate(String queryName, String userName) {
        boolean isDuplicate = false;

        Collection<ICab2bQuery> queries = getUsersQueriesDetail(userName);
        for (IParameterizedQuery query : queries) {
            if (query.getName().equalsIgnoreCase(queryName)) {
                isDuplicate = true;
                break;
            }
        }

        return isDuplicate;
    }

    /**
     * This method returns all the queries created by given user with
     * only their name, description and created date populated.
     *
     * @see edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface#getAllQueryNameAndDescription()
     *
     * @param userName
     *
     * @return list of IParameterizedQuery having only name, description and created date populated.
     */
    public Collection<ICab2bQuery> getUsersQueriesDetail(String userName) {
        List<Object> idList = new ArrayList<Object>(1);
        idList.add(userName);

        List<ICab2bQuery> queries = null;
        try {
            queries = (List<ICab2bQuery>) Utility.executeHQL("getQueriesByUserName", idList);
            postProcessMMCQueries(queries);
            filterSystemGeneratedSubQueries(queries);
        } catch (HibernateException e) {
            throw new RuntimeException("Error occured while executing the HQL:" + e.getMessage(), e);
        }

        return queries;
    }

    /**
     * This method returns all the regular queries created by the given user.
     *
     * @param userName creator/owner of the queries
     * @return
     */
    public List<ICab2bQuery> getRegularQueriesByUserName(final String userName) {
        return getQueriesByTypeAndUserName(QueryType.ANDed, userName);
    }

    /**
     * This method returns all the keyword search queries created by the given user.
     *
     * @param userName creator/owner of the queries
     * @return
     */
    public List<ICab2bQuery> getKeywordQueriesByUserName(final String userName) {
        return getQueriesByTypeAndUserName(QueryType.ORed, userName);
    }

    /**
     * This method returns all the queries of the given type created by the given user.
     *
     * @param queryType type of the queries to be retrieved
     * @param userName creator/owner of the queries
     * @return
     */
    private List<ICab2bQuery> getQueriesByTypeAndUserName(QueryType queryType, final String userName) {
        List<Object> paramList = new ArrayList<Object>(2);
        paramList.add(queryType.toString());
        paramList.add(userName);

        List<ICab2bQuery> queries = null;
        try {
            queries = (List<ICab2bQuery>) Utility.executeHQL("getQueriesByTypeAndUserName", paramList);
            postProcessMMCQueries(queries);
            filterSystemGeneratedSubQueries(queries);
        } catch (HibernateException e) {
            throw new RuntimeException("Error occured while executing the HQL:" + e.getMessage(), e);
        }
        return queries;
    }

    public IQueryResult<? extends IRecord> executeQuery(ICab2bQuery query, String serializedDCR) {
        GlobusCredential globusCredential = null;
        boolean hasAnySecureService = Utility.hasAnySecureService(query);
        if (hasAnySecureService) {
            globusCredential = AuthenticationUtility.getGlobusCredential(serializedDCR);
        }

        new PopularCategoryOperations().setPopularity(query);
        QueryExecutor queryExecutor = new QueryExecutor(query, globusCredential);
        queryExecutor.executeQuery();
        return queryExecutor.getCompleteResults();
    }

    /**
     * This method saves the Cab2bQuery
     */
    public void saveCab2bQuery(ICab2bQuery query, String serializedDCR) throws RemoteException {
        Long userId = UtilityOperations.getLocalUserId(serializedDCR);
        query.setCreatedBy(userId);

        if (isMultiModelCategoryQuery(query)) {
            query = new MultimodelCategoryQueryProcessor().process(query);
        }

        // First Save as keyword search
        saveAsKeywordQuery(query);

        // Reset as regular query and save.
        query.setIsKeywordSearch(Boolean.FALSE);
        new QueryOperations().saveQuery(query);
    }

    /**
     * This method save the given regular query as keyword search query.
     *
     * @param query
     * @throws RemoteException
     */
    public void saveAsKeywordQuery(ICab2bQuery query) throws RemoteException {
        if (query.isKeywordSearch()) {
            ICab2bQuery oredQuery = (ICab2bQuery) DynamicExtensionsUtility.cloneObject(query);
            if (oredQuery.getId() != null) {
                new HibernateCleanser(oredQuery).clean();
            }
            oredQuery.setName(oredQuery.getName() + "#");

            QueryConverter converter = new QueryConverter();
            if (isMultiModelCategoryQuery(query)) {
                MultiModelCategoryQuery mmcQuery = (MultiModelCategoryQuery) query;
                Collection<ICab2bQuery> subQueries = mmcQuery.getSubQueries();
                List<ICab2bQuery> queries = new ArrayList<ICab2bQuery>(subQueries);
                for (int index = 0; index < queries.size(); index++) {
                    ICab2bQuery subQuery = queries.get(index);
                    subQuery.setName(subQuery.getName() + "#");
                    subQuery = converter.convertToKeywordQuery(subQuery);
                    queries.set(index, subQuery);
                }
                mmcQuery.setSubQueries(queries);
            } else {
                oredQuery = converter.convertToKeywordQuery(oredQuery);
            }

            new QueryOperations().saveQuery(oredQuery);
        }
    }

    /**
     * This method finds whether the given query is designated to be a MultiModelCategoryQuery.
     * @param query
     * @return
     */
    private Boolean isMultiModelCategoryQuery(ICab2bQuery query) {
        EntityInterface outputEntity = query.getOutputEntity();

        Boolean isTaggedMMC = Boolean.FALSE;
        for (TaggedValueInterface taggedValue : outputEntity.getTaggedValueCollection()) {
            if (MULTIMODELCATEGORY.equals(taggedValue.getKey())
                    && MULTIMODELCATEGORY.equals(taggedValue.getValue())) {
                isTaggedMMC = Boolean.TRUE;
                break;
            }
        }

        Boolean isMMC = Boolean.FALSE;
        EntityGroupInterface entityGroup = outputEntity.getEntityGroupCollection().iterator().next();
        if (MMC_ENTITY_GROUP_NAME.equals(entityGroup.getName())) {
            isMMC = Boolean.TRUE;
        }

        return isTaggedMMC && isMMC;
    }

    public ICab2bQuery getQueryById(Long queryId) {
        ICab2bQuery query = super.getQueryById(queryId);
        postProcessMMCQuery(query);

        return query;
    }

    private void postProcessMMCQueries(List<ICab2bQuery> queries) {
        if (queries != null || !queries.isEmpty()) {
            for (ICab2bQuery query : queries) {
                postProcessMMCQuery(query);
            }
        }
    }

    private void postProcessMMCQuery(ICab2bQuery query) {
        if (query != null && query instanceof MultiModelCategoryQuery) {
            MultiModelCategoryQuery mmcQuery = (MultiModelCategoryQuery) query;
            Collection<ICab2bQuery> subQueries = mmcQuery.getSubQueries();
            for (ICab2bQuery subQuery : subQueries) {
                subQuery.setIsSystemGenerated(Boolean.TRUE);
            }
        }
    }

    private void filterSystemGeneratedSubQueries(List<ICab2bQuery> queries) {
        if (queries != null || !queries.isEmpty()) {
            Iterator<ICab2bQuery> queryIterator = queries.iterator();
            while (queryIterator.hasNext()) {
                ICab2bQuery query = queryIterator.next();
                if (query.getIsSystemGenerated()) {
                    queryIterator.remove();
                }
            }
        }
    }
}

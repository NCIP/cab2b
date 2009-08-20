package edu.wustl.cab2b.server.queryengine;

import static edu.wustl.cab2b.common.util.Constants.MMC_ENTITY_GROUP_NAME;
import static edu.wustl.cab2b.common.util.Constants.MULTIMODELCATEGORY;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.globus.gsi.GlobusCredential;
import org.hibernate.HibernateException;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.cab2b.common.authentication.util.AuthenticationUtility;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQueryImpl;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.QueryType;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.category.PopularCategoryOperations;
import edu.wustl.cab2b.server.util.UtilityOperations;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.utils.ConstraintsObjectBuilder;

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
        List<Object> params = new ArrayList<Object>(1);
        params.add(userName);

        List<ICab2bQuery> queries = null;
        try {
            queries = (List<ICab2bQuery>) Utility.executeHQL("getUserQueriesDetails", params);
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
        List<Object> params = new ArrayList<Object>(2);
        params.add(QueryType.ANDed.toString());
        params.add(userName);

        List<ICab2bQuery> queries = null;
        try {
            queries = (List<ICab2bQuery>) Utility.executeHQL("getQueriesByTypeAndUserName", params);
            postProcessMMCQueries(queries);
            filterSystemGeneratedSubQueries(queries);
        } catch (HibernateException e) {
            throw new RuntimeException("Error occured while executing the HQL:" + e.getMessage(), e);
        }
        return queries;
    }

    /**
     * This method returns all the keyword search queries created by the given user.
     *
     * @param userName creator/owner of the queries
     * @return
     */
    public List<KeywordQuery> getKeywordQueriesByUserName(final String userName) {
        List<Object> params = new ArrayList<Object>(1);
        params.add(userName);

        List<KeywordQuery> keywordQueries = null;
        try {
            List<ICab2bQuery> queries =
                    (List<ICab2bQuery>) Utility.executeHQL("getKeywordQueriesByUserName", params);
            filterSystemGeneratedSubQueries(queries);

            keywordQueries = new ArrayList<KeywordQuery>(queries.size());
            for (ICab2bQuery query : queries) {
                keywordQueries.add((KeywordQuery) query);
            }
        } catch (HibernateException e) {
            throw new RuntimeException("Error occured while executing the HQL:" + e.getMessage(), e);
        }

        return keywordQueries;
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.common.querysuite.bizlogic.QueryBizLogic#getQueryById(java.lang.Long)
     */
    public ICab2bQuery getQueryById(Long queryId) {
        ICab2bQuery query = super.getQueryById(queryId);
        postProcessMMCQuery(query);

        return query;
    }

    /**
     * This method executes the given query and returns the result
     * @param query
     * @param serializedDCR
     * @return
     */
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
    public void saveFormQuery(ICab2bQuery query, String serializedDCR) throws RemoteException {
        Long userId = UtilityOperations.getLocalUserId(serializedDCR);
        query.setCreatedBy(userId);

        if (isMultiModelCategoryQuery(query)) {
            query = new MultimodelCategoryQueryProcessor().process(query);
        }

        saveQuery(query);
    }

    /**
     * This method save the given regular query as keyword search query.
     *
     * @param query
     * @throws RemoteException
     */
    public void saveKeywordQuery(ICab2bQuery query, String serializedDCR) throws RemoteException {
        Long userId = UtilityOperations.getLocalUserId(serializedDCR);
        query.setCreatedBy(userId);
        query.setName(query.getName() + "#");

        QueryConverter converter = new QueryConverter();
        if (isMultiModelCategoryQuery(query)) {
            query = new MultimodelCategoryQueryProcessor().process(query);
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
        }
        query = converter.convertToKeywordQuery(query);

        String userName = AuthenticationUtility.getUsersGridId(serializedDCR);
        saveInKeywordQuery(query, userName, userId);
    }

    private void saveInKeywordQuery(ICab2bQuery query, String userName, Long userId) {
        EntityInterface queryEntity = query.getOutputEntity();
        Collection<ModelGroupInterface> modelGroups = UtilityOperations.getModelGroups(queryEntity);
        List<KeywordQuery> queries = getKeywordQueriesByUserName(userName);

        List<KeywordQuery> keywordQueries = new ArrayList<KeywordQuery>(modelGroups.size());
        for (ModelGroupInterface modelGroup : modelGroups) {
            boolean isPresent = false;
            for (KeywordQuery keywordQuery : queries) {
                String appGroupName = keywordQuery.getApplicationGroup().getModelGroupName();
                if (modelGroup.getModelGroupName().equals(appGroupName)) {
                    keywordQueries.add(keywordQuery);
                    isPresent = true;
                }
            }

            if (!isPresent) {
                KeywordQuery keywordQuery = createAndSaveKeywordQuery(query.getOutputEntity(), modelGroup, userId);
                keywordQueries.add(keywordQuery);
            }
        }

        for (KeywordQuery keywordQuery : keywordQueries) {
            keywordQuery.addSubQuery(query);
            updateQuery(keywordQuery);
        }
    }

    private KeywordQuery createAndSaveKeywordQuery(EntityInterface en, ModelGroupInterface modelGroup, Long userId) {
        ConstraintsObjectBuilder queryBuilder =
                new ConstraintsObjectBuilder(Cab2bQueryObjectFactory.createCab2bQuery());
        queryBuilder.addRule(new ArrayList<AttributeInterface>(0), new ArrayList<String>(0),
                             new ArrayList<String>(0), new ArrayList<String>(0), en);
        ICab2bQuery dummy = (ICab2bQuery) queryBuilder.getQuery();
        dummy.setOutputEntity(en);

        KeywordQuery keywordQuery = new KeywordQueryImpl(dummy);
        keywordQuery.setName(modelGroup.getModelGroupName() + "_KeywordSearch");
        keywordQuery.setCreatedBy(userId);
        keywordQuery.setApplicationGroup(modelGroup);
        keywordQuery.setCreatedDate(new Date());
        keywordQuery.setIsSystemGenerated(Boolean.TRUE);
        keywordQuery.setIsKeywordSearch(Boolean.TRUE);

        saveQuery(keywordQuery);

        return keywordQuery;
    }

    /**
     * This method finds whether the given query is designated to be a MultiModelCategoryQuery.
     * @param query
     * @return
     */
    private Boolean isMultiModelCategoryQuery(ICab2bQuery query) {
        EntityInterface outputEntity = query.getOutputEntity();

        Boolean isTaggedAsMMC = Boolean.FALSE;
        for (TaggedValueInterface taggedValue : outputEntity.getTaggedValueCollection()) {
            if (MULTIMODELCATEGORY.equals(taggedValue.getKey())
                    && MULTIMODELCATEGORY.equals(taggedValue.getValue())) {
                isTaggedAsMMC = Boolean.TRUE;
                break;
            }
        }

        Boolean isMMC = Boolean.FALSE;
        EntityGroupInterface entityGroup = outputEntity.getEntityGroupCollection().iterator().next();
        if (MMC_ENTITY_GROUP_NAME.equals(entityGroup.getName())) {
            isMMC = Boolean.TRUE;
        }

        return isTaggedAsMMC && isMMC;
    }

    /**
     * This method process the given queries to set  the sub-queries as system-generated queires
     * @param queries
     */
    private void postProcessMMCQueries(List<ICab2bQuery> queries) {
        if (queries != null || !queries.isEmpty()) {
            for (ICab2bQuery query : queries) {
                postProcessMMCQuery(query);
            }
        }
    }

    /**
     * This method process the MMCQuery to set the sub-queries as system-generated queires
     * @param query
     */
    private void postProcessMMCQuery(ICab2bQuery query) {
        if (query != null && query instanceof MultiModelCategoryQuery) {
            MultiModelCategoryQuery mmcQuery = (MultiModelCategoryQuery) query;
            Collection<ICab2bQuery> subQueries = mmcQuery.getSubQueries();
            for (ICab2bQuery subQuery : subQueries) {
                subQuery.setIsSystemGenerated(Boolean.TRUE);
            }
        }
    }

    /**
     * This method filters the system generated queries from the collections.
     * @param queries
     */
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

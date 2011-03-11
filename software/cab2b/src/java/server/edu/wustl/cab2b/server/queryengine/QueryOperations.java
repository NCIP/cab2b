package edu.wustl.cab2b.server.queryengine;

import static edu.wustl.cab2b.common.util.Constants.MMC_ENTITY_GROUP_NAME;
import static edu.wustl.cab2b.common.util.Constants.MULTIMODELCATEGORY;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cache.EhCache;


import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.wustl.cab2b.common.authentication.util.AuthenticationUtility;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQuery;
import edu.wustl.cab2b.common.queryengine.KeywordQueryImpl;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.QueryType;
import edu.wustl.cab2b.common.queryengine.ServiceGroup;
import edu.wustl.cab2b.common.queryengine.ServiceGroupItem;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.DatalistCache;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.category.PopularCategoryOperations;
import edu.wustl.cab2b.server.util.UtilityOperations;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import edu.wustl.common.querysuite.bizlogic.QueryBizLogic;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.impl.QueryEntity;
import edu.wustl.common.querysuite.utils.ConstraintsObjectBuilder;
import edu.wustl.common.util.dbManager.DBUtil;

/**
 * @author chetan_patil
 *
 */
public class QueryOperations extends QueryBizLogic<ICab2bQuery> {
	
    private static final Logger logger = Logger.getLogger(QueryOperations.class);


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
     * This method returns all queries that touch multiple object models.
     *
     * @param userName creator/owner of the queries
     * @return
     */
    public List<ICab2bQuery> getAllMultiModelQueries() {
        List<ICab2bQuery> allQueries = this.getAllQueries();
        postProcessMMCQueries(allQueries);
        filterSystemGeneratedSubQueries(allQueries);
        List<ICab2bQuery> regularQueries = new ArrayList<ICab2bQuery>();
        for(ICab2bQuery query : allQueries) {
        	if(query.getType().equals(QueryType.ANDed.toString()) 
        			&& query.getConstraints().getQueryEntities() != null 
        			&& query.getConstraints().getQueryEntities().size() > 1) {
        		Set<String> models = new HashSet<String>();
        		for(IQueryEntity entity : query.getConstraints().getQueryEntities()) {
        			models.add(((QueryEntity)entity).getEntityInterface().getEntityGroupCollection().iterator().next().getName());
        		}
        		if(models.size() > 1) {
        			regularQueries.add(query);
        		}
        	}
        }
        return regularQueries;
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
            keywordQueries = (List<KeywordQuery>) Utility.executeHQL("getKeywordQueriesByUserName", params);
        } catch (HibernateException e) {
            throw new RuntimeException("Error occured while executing the HQL:" + e.getMessage(), e);
        }

        return keywordQueries;
    }

    /**
     * This method returns all the keyword search queries created by the given user.
     *
     * @param userName creator/owner of the queries
     * @return
     */
    public List<KeywordQuery> getKeywordQueriesByUserId(final Long userId) {
        List<Object> params = new ArrayList<Object>(1);
        params.add(userId);

        List<KeywordQuery> keywordQueries = null;
        try {
            keywordQueries = (List<KeywordQuery>) Utility.executeHQL("getKeywordQueriesByUserId", params);
            postProcessKeywordQueries(keywordQueries);
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
    	Session session = HibernateUtil.newSession();
        ICab2bQuery query = (ICab2bQuery) session.load(Cab2bQuery.class, queryId);
        if (query != null && query instanceof MultiModelCategoryQuery) {
            postProcessMMCQuery((MultiModelCategoryQuery) query);
        }
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

        //TODO may need to comment popularity for the time being
        //This is causing connection issues in case of Apply Datalist where client calls EJB in multiple threads
        //For each thread a new session is created due to ThreadLocal way of getting Session
        new PopularCategoryOperations().setPopularity(query);
        QueryExecutor queryExecutor = new QueryExecutor(query, globusCredential);
        queryExecutor.executeQuery();
        return queryExecutor.getCompleteResults();
    }
    
    /**
     * This method executes the given query and returns the result
     * @param query
     * @param serializedDCR
     * @return
     */
    public IQueryResult<? extends IRecord> executeQueryForApplyDatalist(ICab2bQuery query, String serializedDCR) {
        GlobusCredential globusCredential = null;
        boolean hasAnySecureService = Utility.hasAnySecureService(query);
        if (hasAnySecureService) {
            globusCredential = AuthenticationUtility.getGlobusCredential(serializedDCR);
        }
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
            for (ICab2bQuery subQuery : subQueries) {
                subQuery.setName(subQuery.getName() + "#");
                subQuery = converter.convertToKeywordQuery(subQuery);
            }
        }
        query = converter.convertToKeywordQuery(query);

        saveInKeywordQuery(query, userId);
    }

    public Cab2bQuery addServiceGroupsToQuery(Long queryId, Collection<ServiceGroup> groups) {
    	Session session = HibernateUtil.newSession();
    	HibernateDatabaseOperations<ServiceGroup> dbopr =
            new HibernateDatabaseOperations<ServiceGroup>(session);
    	Cab2bQuery query = (Cab2bQuery)session.get(Cab2bQuery.class, queryId);
    	for(ServiceGroup group : groups) {
    		group.setQuery(query);
    		session.persist(group);
    	}
    	query = (Cab2bQuery)session.get(Cab2bQuery.class, queryId);
    	query.getServiceGroups();
    	return query;
    }
    
    public void deleteAllServiceGroups(Long id) {
    	Session session = HibernateUtil.newSession();
    	Cab2bQuery query = (Cab2bQuery)session.get(Cab2bQuery.class, id);
    	if(query.getServiceGroups() != null && !query.getServiceGroups().isEmpty()) {
	    	List<Long> ids = new ArrayList<Long>();
			List<Long> itemIds = new ArrayList<Long>();
			for(ServiceGroup group : query.getServiceGroups()) {
	    		ids.add(group.getId());
	    		for(ServiceGroupItem item : group.getItems()) {
	    			itemIds.add(item.getId());
	    		}
	    	}
			if(itemIds.size() > 0) {
		    	Query deleteGroups = session.createQuery("delete ServiceGroupItem where id in ("  + StringUtils.join(itemIds.iterator(), ", ") + ")");
		    	deleteGroups.executeUpdate();
			}
			if(ids.size() > 0) {
		    	Query deleteQuery = session.createQuery("delete ServiceGroup where id in ("  + StringUtils.join(ids.iterator(), ", ") + ")");
		    	deleteQuery.executeUpdate();
			}
    	}
    }
    
    


public void deleteQuery(Long id) {
	
	logger.info("id to delete:"+id);
   	
    Session session = HibernateUtil.getSessionFactory().openSession();
	Transaction tx = session.beginTransaction();
	
	try{
	 session.connection().createStatement().execute("update query_abstract_query set created_by=-1 where identifier="+id); 
		
	} catch(Exception e) {	
		logger.error(e);

	}
	
   		tx.commit();
   		session.clear();
   		session.close();
   		
   		EntityCache.getCache().refreshCache();
   		
   		CategoryCache.getInstance().refreshCategoryCache();   	   		
        UtilityOperations.refreshCache();
   		
}
    
    /**
     * This method saves the given query as a subquery of the appropriate keyword query present in the system.
     * @param query
     * @param userId
     */
    private void saveInKeywordQuery(ICab2bQuery query, Long userId) {
        EntityInterface queryEntity = query.getOutputEntity();
        Collection<ModelGroupInterface> modelGroups = UtilityOperations.getModelGroups(queryEntity);
        List<KeywordQuery> queries = getKeywordQueriesByUserId(userId);

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

        try {
            HibernateDatabaseOperations<KeywordQuery> dbopr =
                    new HibernateDatabaseOperations<KeywordQuery>(DBUtil.currentSession());
            for (KeywordQuery keywordQuery : keywordQueries) {
                keywordQuery.addSubQuery(query);
                dbopr.update(keywordQuery);
            }
        } catch (Exception e) {
            DBUtil.closeSession();
        }
    }

    /**
     * This method create and save the keyword query
     * @param en
     * @param modelGroup
     * @param userId
     * @return
     */
    private KeywordQuery createAndSaveKeywordQuery(EntityInterface en, ModelGroupInterface modelGroup, Long userId) {
        ConstraintsObjectBuilder queryBuilder =
                new ConstraintsObjectBuilder(Cab2bQueryObjectFactory.createCab2bQuery());
        queryBuilder.addRule(new ArrayList<AttributeInterface>(0), new ArrayList<String>(0),
                             new ArrayList<String>(0), new ArrayList<String>(0), en);
        ICab2bQuery dummy = (ICab2bQuery) queryBuilder.getQuery();
        dummy.setOutputEntity(en);

        KeywordQuery keywordQuery = new KeywordQueryImpl(dummy);
        keywordQuery.setName(modelGroup.getModelGroupName() + "_KeywordSearch_" + userId);
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
     * This method sends the MultiModelCategoryQuery for post processing, which is a child of the keyword query.
     * @param keywordQueries
     */
    private void postProcessKeywordQueries(Collection<KeywordQuery> keywordQueries) {
        for (KeywordQuery keywordQuery : keywordQueries) {
            postProcessMMCQueries(keywordQuery.getSubQueries());
        }
    }

    /**
     * This method process the given queries to set  the sub-queries as system-generated queires
     * @param queries
     */
    private void postProcessMMCQueries(Collection<ICab2bQuery> queries) {
        if (queries != null || !queries.isEmpty()) {
            for (ICab2bQuery query : queries) {
                if (query instanceof MultiModelCategoryQuery) {
                    postProcessMMCQuery((MultiModelCategoryQuery) query);
                }
            }
        }
    }

    /**
     * This method process the MMCQuery to set the sub-queries as system-generated queires
     * @param query
     */
    private void postProcessMMCQuery(MultiModelCategoryQuery mmcQuery) {
        Collection<ICab2bQuery> subQueries = mmcQuery.getSubQueries();
        for (ICab2bQuery subQuery : subQueries) {
            subQuery.setIsSystemGenerated(Boolean.TRUE);
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

package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.cagrid.fqp.execution.QueryExecutionParameters;
import org.cagrid.fqp.execution.TargetDataServiceQueryBehavior;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.querystatus.AbstractStatus;
import edu.wustl.cab2b.common.queryengine.result.FQPUrlStatus;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.dcqlresult.DCQLResult;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

/**
 * Skeletal implementation of a query result transformer. Concrete
 * implementations need only implement the <code>createRecords</code> and
 * <code>createCategoryRecords</code> methods. Additional hooks are provided
 * and can be used to customize the creation and population of the records in
 * the result.
 * 
 * @author srinath_k
 * @param <R>
 * @param <C>
 * @see edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer
 */
public abstract class AbstractQueryResultTransformer<R extends IRecord, C extends ICategorialClassRecord>
        implements IQueryResultTransformer<R, C> {
    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(AbstractQueryResultTransformer.class);

    protected QueryLogger queryLogger;

    //This value is updated by FQPQueryListener
    private Map<String, FQPUrlStatus> urlVsStatus = new HashMap<String, FQPUrlStatus>();

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * Default Constructor
     */
    public AbstractQueryResultTransformer() {
        queryLogger = new QueryLogger();
    }

    /**
     * Subclasses can specify a custom dcql logger.
     * 
     * @param dcqlLogger
     *            custom logger.
     */
    protected AbstractQueryResultTransformer(QueryLogger queryLogger) {
        this.queryLogger = queryLogger;
    }

    /**
     * Executes the DCQL using the {@link FederatedQueryEngine} and transforms
     * the results to appropriate {@link IQueryResult}.
     * <p>
     * This method uses the <code>createResult</code> method for creating the
     * result object. <code>createResult</code> can be overridden to provide
     * custom result objects.
     * <p>
     * For each {@link CQLQueryResults} obtained by executing the dcql, the
     * method <code>createRecords</code> is invoked. Concrete transformers are
     * required to implement the method <code>createRecords</code>.
     * @param query
     * @param targetEntity
     * @param cred
     * @return {@link IQueryResult}
     * 
     */
    public IQueryResult<R> getResults(DCQLQuery query, EntityInterface targetEntity, GlobusCredential cred) {
        IQueryResult<R> result = null;
        try {
            log(query);
            Map<String, CQLQueryResults> queryResults = executeDcql(query, cred);
            int numRecs = 0;
            result = createResult(targetEntity);
            for (Map.Entry<String, CQLQueryResults> entry : queryResults.entrySet()) {
                String url = entry.getKey();
                CQLQueryResults cqlQueryResult = entry.getValue();
                List<R> recs = createRecords(url, cqlQueryResult, targetEntity);
                result.addRecords(url, recs);
                numRecs += recs.size();
            }

            //FQP bug
            postProcessResult(query);
            result.setFQPUrlStatus(urlVsStatus.values());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(Utility.getStackTrace(e), ErrorCodeConstants.QM_0004);
        }
        return result;
    }

    /**
     * 
     * @param query
     */
    private void postProcessResult(DCQLQuery query) {
        //FQP dosn't fire status update for urls which returns zero results
        //This is a bug in FQP. In this case we are adding our own url with 
        for (String url : query.getTargetServiceURL()) {
            if (!urlVsStatus.containsKey(url)) {
                updateStatus(url, "Completed with zero errors", "Completed with zero errors",
                             AbstractStatus.Complete);
            }
        }
    }

    /**
     * Executes DCQL and transforms the results.
     * @param query
     * @param cred
     * @return Map<String, CQLQueryResults>
     */
    protected Map<String, CQLQueryResults> executeDcql(DCQLQuery query, GlobusCredential cred) {
        DCQLQueryResultsCollection queryResults = null;
        try {
            QueryExecutionParameters queryParameter = new QueryExecutionParameters();

            TargetDataServiceQueryBehavior targetBehaviour = new TargetDataServiceQueryBehavior();
            targetBehaviour.setFailOnFirstError(false);

            queryParameter.setTargetDataServiceQueryBehavior(targetBehaviour);
            String queryName = query.getTargetObject().getName();
            String className = queryName.substring(queryName.lastIndexOf('.') + 1, queryName.length());
            logger.info("Executing DQCL to get " + className);
            FederatedQueryEngine federatedQueryEngine = new FederatedQueryEngine(cred, queryParameter, executor);
            FQPQueryListener listener = new FQPQueryListener(this);
            federatedQueryEngine.addStatusListener(listener);
            queryResults = federatedQueryEngine.execute(query);
            logger.info("Query for " + className + " Completed");
        } catch (FederatedQueryProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(Utility.getStackTrace(e), ErrorCodeConstants.QM_0004);
        }
        Map<String, CQLQueryResults> res = new HashMap<String, CQLQueryResults>();
        for (DCQLResult dcqlQueryResult : queryResults.getDCQLResult()) {
            res.put(dcqlQueryResult.getTargetServiceURL(), dcqlQueryResult.getCQLQueryResultCollection());
        }
        return res;
    }

    /**
     * Executes DCQL using the <code>getResults</code> method and transforms
     * the results to category results.
     * <p>
     * This method uses the <code>createCategoryResult</code> method for
     * creating the result object. <code>createCategoryResult</code> can be
     * overridden to provide custom result objects.
     * <p>
     * First, results for the DCQL are obtained by invoking
     * <code>getResults</code>. Then, for each {@link IRecord}<br>
     * <ol>
     * <li>a corresponding {@link ICategorialClassRecord} is created using the
     * <code>createCategoryRecord</code> method.</li>
     * <li> The values for the attributes are then copied from the
     * {@link IRecord} to the {@link ICategorialClassRecord}</li>
     * <li><code>copyFromRecord</code> is invoked. Subclasses can override
     * this method to copy any remaining fields from the actual {@link IRecord}
     * to the {@link ICategorialClassRecord}.</li>
     * </ol>
     * Finally <code>copyFromResult</code> is invoked. Subclasses can override
     * this method
     * @param query
     * @param categorialClass
     * @param cred
     * @return {@link IQueryResult}
     */
    public IQueryResult<C> getCategoryResults(DCQLQuery query, CategorialClass categorialClass,
                                              GlobusCredential cred) {
        Set<AttributeInterface> categoryAttributes = new HashSet<AttributeInterface>();
        for (CategorialAttribute categorialAttr : categorialClass.getCategorialAttributeCollection()) {
            categoryAttributes.add(categorialAttr.getCategoryAttribute());
        }

        IQueryResult<R> classResults = getResults(query, categorialClass.getCategorialClassEntity(), cred);
        IQueryResult<C> catResult = createCategoryResult(categorialClass.getCategory().getCategoryEntity());

        for (Map.Entry<String, List<R>> entry : classResults.getRecords().entrySet()) {
            String url = entry.getKey();
            catResult.addUrl(url);
            for (R rec : entry.getValue()) {
                C catRec = createCategoryRecord(categorialClass, categoryAttributes, rec.getRecordId());
                for (CategorialAttribute catAttr : categorialClass.getCategorialAttributeCollection()) {
                    catRec.putValueForAttribute(catAttr.getCategoryAttribute(), rec.getValueForAttribute(catAttr
                        .getSourceClassAttribute()));
                    copyFromRecord(catRec, rec);
                }
                catResult.addRecord(url, catRec);
            }
        }
        copyFromResult(catResult, classResults);
        postProcessResult(query);
        catResult.setFQPUrlStatus(urlVsStatus.values());
        return catResult;
    }

    /**
     * @param dcqlQuery
     */
    private void log(DCQLQuery dcqlQuery) {
        queryLogger.log(dcqlQuery);
    }

    // hooks
    /**
     * Returns the default query result object. A subclass that overrides this
     * method would generally also override <code>createResult</code> and
     * <code>copyFromResult</code>.
     * 
     * @return the query result object.
     * @see QueryResultFactory#createResult()
     */
    protected IQueryResult<C> createCategoryResult(EntityInterface outputEntity) {
        return QueryResultFactory.createResult(outputEntity);
    }

    /**
     * Returns the default query result object.A subclass that overrides this
     * method would generally also override <code>createCategoryResult</code>
     * and <code>copyFromResult</code>.
     * 
     * @return the query result object.
     * @see QueryResultFactory#createResult()
     */
    protected IQueryResult<R> createResult(EntityInterface outputEntity) {
        return QueryResultFactory.createResult(outputEntity);
    }

    /**
     * Does nothing and is a hook for subclasses.
     * 
     * @param catRec
     *            the categorialClassRecord.
     * @param rec
     *            the record for the class obtained by executing DCQL.
     */
    protected void copyFromRecord(C catRec, R rec) {

    }

    /**
     * Does nothing and is a hook for subclasses. A subclass would typically
     * override this method if it has overriden either of the
     * <code>createResult</code> or <code>createCategoryResult</code>
     * methods.
     * 
     * @param catResult
     *            the category results.
     * @param classResults
     *            the class results.
     */
    protected void copyFromResult(IQueryResult<C> catResult, IQueryResult<R> classResults) {

    }

    /**
     * Returns a {@link List} of {@link IRecord}s that represent the given
     * {@link CQLQueryResults}.
     * 
     * @param url
     *            the service url from which the cqlResults were obtained.
     * @param cqlQueryResults
     *            the results obtained by executing DCQL.
     * @param targetEntity
     *            the target entity.
     * @return list of transformed records.
     */
    protected abstract List<R> createRecords(String url, CQLQueryResults cqlQueryResults,
                                             EntityInterface targetEntity);

    /**
     * @param categorialClass
     *            the categorial class.
     * @param categoryAttributes
     *            the attributes of the categorial class that are to be present in the record.
     * @param id
     *            the id of the record.
     * @return a {@link ICategorialClassRecord}.
     */
    protected abstract C createCategoryRecord(CategorialClass categorialClass,
                                              Set<AttributeInterface> categoryAttributes, RecordId id);

    /**
     * @param serviceURL
     * @param status
     */
    protected void registerStatus(String serviceURL, FQPUrlStatus status) {
        urlVsStatus.put(serviceURL, status);
    }

    /**
     * Updates URL status.
     * @param serviceURL
     * @param message
     * @param description
     */
    protected void updateStatus(String serviceURL, String message, String description, String status) {        
        FQPUrlStatus urlStatus = urlVsStatus.get(serviceURL);
        if (urlStatus != null) {
            urlStatus.setDescription(description);
            urlStatus.setMessage(message);
        } else {
            urlStatus = new FQPUrlStatus(serviceURL, message, description);
        }
        urlStatus.setStatus(status);
        registerStatus(serviceURL, urlStatus);
    }

}

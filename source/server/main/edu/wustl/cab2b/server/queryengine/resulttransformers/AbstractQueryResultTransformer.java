package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.util.logger.Logger;
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
 * @see edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer
 */
public abstract class AbstractQueryResultTransformer<R extends IRecord, C extends ICategorialClassRecord>
        implements IQueryResultTransformer<R, C> {
    protected QueryLogger queryLogger;

    public AbstractQueryResultTransformer() {
        queryLogger = new QueryLogger();
    }

    /**
     * Subclasses can specify a custom dcql logger.
     * 
     * @param dcqlLogger custom logger.
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
     * 
     * @see edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer#getResults(gov.nih.nci.cagrid.dcql.DCQLQuery,
     *      edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public IQueryResult<R> getResults(DCQLQuery query, EntityInterface targetEntity) {
        log(query);
        Map<String, CQLQueryResults> queryResults = executeDcql(query);
        int numRecs = 0;
        IQueryResult<R> result = createResult(targetEntity);
        for (Map.Entry<String, CQLQueryResults> entry : queryResults.entrySet()) {
            String url = entry.getKey();
            CQLQueryResults cqlQueryResult = entry.getValue();
            List<R> recs = createRecords(url, cqlQueryResult, targetEntity);
            result.addRecords(url, recs);
            numRecs += recs.size();
        }
        Logger.out.info("No. of records found and transformed : " + numRecs);
        return result;
    }

    protected Map<String, CQLQueryResults> executeDcql(DCQLQuery query) {
        DCQLQueryResultsCollection queryResults = null;
        try {
            FederatedQueryEngine federatedQueryEngine = new FederatedQueryEngine();
            Logger.out.info("Executing DCQL... Target is : " + query.getTargetObject().getName());
            queryResults = federatedQueryEngine.execute(query);
            Logger.out.info("Executed DCQL successfully.");
        } catch (FederatedQueryProcessingException e) {
            throw new RuntimeException("Exception while executing DCQL", e,
                    ErrorCodeConstants.QUERY_EXECUTION_ERROR);
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
     * 
     * @see edu.wustl.cab2b.server.queryengine.resulttransformers.IQueryResultTransformer#getCategoryResults(gov.nih.nci.cagrid.dcql.DCQLQuery,
     *      edu.wustl.common.querysuite.metadata.category.CategorialClass)
     */
    public IQueryResult<C> getCategoryResults(DCQLQuery query, CategorialClass categorialClass) {
        Set<AttributeInterface> categoryAttributes = new HashSet<AttributeInterface>();

        for (CategorialAttribute categorialAttr : categorialClass.getCategorialAttributeCollection()) {
            AttributeInterface categoryAttribute = categorialAttr.getCategoryAttribute();
            categoryAttributes.add(categoryAttribute);
        }

        IQueryResult<R> classResults = getResults(query, categorialClass.getCategorialClassEntity());
        IQueryResult<C> catResult = createCategoryResult(categorialClass.getCategory().getCategoryEntity());

        for (Map.Entry<String, List<R>> entry : classResults.getRecords().entrySet()) {
            String url = entry.getKey();
            catResult.addUrl(url);
            for (R rec : entry.getValue()) {
                C catRec = createCategoryRecord(categorialClass, categoryAttributes, rec.getId());
                for (CategorialAttribute catAttr : categorialClass.getCategorialAttributeCollection()) {
                    catRec.putValueForAttribute(catAttr.getCategoryAttribute(),
                                                rec.getValueForAttribute(catAttr.getSourceClassAttribute()));
                    copyFromRecord(catRec, rec);
                }
                catResult.addRecord(url, catRec);
            }
        }
        copyFromResult(catResult, classResults);
        return catResult;
    }

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
     * @param catRec the categorialClassRecord.
     * @param rec the record for the class obtained by executing DCQL.
     */
    protected void copyFromRecord(C catRec, R rec) {

    }

    /**
     * Does nothing and is a hook for subclasses. A subclass would typically
     * override this method if it has overriden either of the
     * <code>createResult</code> or <code>createCategoryResult</code>
     * methods.
     * 
     * @param catResult the category results.
     * @param classResults the class results.
     */
    protected void copyFromResult(IQueryResult<C> catResult, IQueryResult<R> classResults) {

    }

    /**
     * Returns a {@link List} of {@link IRecord}s that represent the given
     * {@link CQLQueryResults}.
     * 
     * @param url the service url from which the cqlResults were obtained.
     * @param cqlQueryResults the results obtained by executing DCQL.
     * @param targetEntity the target entity.
     * @return list of transformed records.
     */
    protected abstract List<R> createRecords(String url, CQLQueryResults cqlQueryResults,
                                             EntityInterface targetEntity);

    /**
     * @param categorialClass the categorial class.
     * @param categoryAttributes the attributes of the categorial class that are
     *            to be present in the record.
     * @param id the id of the record.
     * @return a {@link ICategorialClassRecord}.
     */
    protected abstract C createCategoryRecord(CategorialClass categorialClass,
                                              Set<AttributeInterface> categoryAttributes, String id);
}

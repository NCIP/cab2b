package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.QueryBuilder;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

/**
 * This class is responsible for building a DCQL out of ICab2bQuery, and get
 * results by executing that DCQL.
 * @author Chandrakant Talele
 */
public class QueryExecutor {
    /**
     * This methods generates DCQL out of input ICab2bQuery object and fires it,
     * and returns the results.
     * @param query
     *            Query which needs to be executed.
     * @return Returns the IQueryResult
     */
    public static IQueryResult executeQuery(ICab2bQuery query) {
        DCQLQueryResultsCollection dcqlQueryResults = getDcqlResults(query);

        IOutputTreeNode rootOutputClass = query.getRootOutputClass();
        EntityInterface outputEntity = rootOutputClass.getOutputEntity().getDynamicExtensionsEntity();

        // TODO get selected attributes;
        // TODO categories??
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>(
                outputEntity.getAttributeCollection());

        DCQLResultsTransformer resultsTransformer = new DCQLResultsTransformer();
        IQueryResult queryResult = resultsTransformer.getQueryResults(
                                                                      dcqlQueryResults,
                                                                      attributeList);
        return queryResult;
    }

    /**
     * Gets DCQL and fires it and returns generated CQLQueryResults.
     * @param query
     *            Input query
     * @return Returns the DCQLQueryResults
     */
    private static DCQLQueryResultsCollection getDcqlResults(ICab2bQuery query) {
        QueryBuilder queryBuilder = new QueryBuilder();
        DCQLQuery dcqlQuery = queryBuilder.buildQuery(query);
        FederatedQueryEngine federatedQueryEngine = new FederatedQueryEngine();
        DCQLQueryResultsCollection queryResults = null;
        try {
            Logger.out.info("Executing DCQL...");
            queryResults = federatedQueryEngine.execute(dcqlQuery);
            Logger.out.info("Executed DCQL successfully.");
        } catch (FederatedQueryProcessingException e) {
            throw new RuntimeException("Exception while executing DCQL", e,
                    ErrorCodeConstants.QUERY_EXECUTION_ERROR);
        }

        return queryResults;
    }
}

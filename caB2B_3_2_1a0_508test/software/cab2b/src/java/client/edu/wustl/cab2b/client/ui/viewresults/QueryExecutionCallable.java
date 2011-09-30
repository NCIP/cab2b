package edu.wustl.cab2b.client.ui.viewresults;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;

public class QueryExecutionCallable implements Callable<QueryResultObject> {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryExecutionCallable.class);

    private IDataRow m_sourceEntity;

    private IDataRow m_destinationEntity;

    private List<IDataRow> m_childNodes;

    public QueryExecutionCallable(
            IDataRow sourceEntity,
            IDataRow destinationEntity,
            List<IDataRow> childNodes) {
        m_sourceEntity = sourceEntity;
        m_destinationEntity = destinationEntity;
        m_childNodes = childNodes;
    }

    /**
     * 
     */
    public QueryResultObject call() throws Exception {
        List<IDataRow> results = executeRelatedQuery();
        if (null != results) {
            QueryResultObject queryResult = new QueryResultObject(results, m_childNodes);
            return queryResult;
        }
        return null;
    }

    /**
     * Executes the query to get destination enitities associated with source entity
     * @param sourceEntity : The source entity in the query
     * @param destinationEntity : The destination entity in the query 
     * @return 
     */
    private List<IDataRow> executeRelatedQuery() {
        //	Fire query for every child element
        final ClientQueryBuilder queryObject = new ClientQueryBuilder();
        // construct expression object for source entity
        AttributeInterface idAttribute = Utility.getIdAttribute(m_sourceEntity.getEntity());
        List<AttributeInterface> attributes = Collections.singletonList(idAttribute);
        List<String> operators = Collections.singletonList("Equals");
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(Collections.singletonList(m_sourceEntity.getId()));

        int sourceExpressionID = queryObject.addRule(attributes, operators, values, idAttribute.getEntity());

        /* Get the source expression id. Needed to add the path.*/
        final EntityInterface targetEntity = m_destinationEntity.getEntity();
        int targetExpressionID = queryObject.createDummyExpression(targetEntity);

        final IAssociation association = m_destinationEntity.getAssociation();
        if (association instanceof IIntraModelAssociation) {
            /*Get the association from the fourth element.*/
            IIntraModelAssociation intraModelAssociation = (IIntraModelAssociation) association;
            try {
                queryObject.addAssociation(targetExpressionID, sourceExpressionID, intraModelAssociation);
            } catch (CyclicException exCyclic) {
                exCyclic.printStackTrace();
            }
            queryObject.setOutputForQueryForSpecifiedURL(targetEntity, m_sourceEntity.getURL());
        } else if (association instanceof IInterModelAssociation) {

            IInterModelAssociation interaModelAssociation = (IInterModelAssociation) association;
            try {
                queryObject.addAssociation(targetExpressionID, sourceExpressionID,
                                           interaModelAssociation.reverse());
                queryObject.setOutputForQuery(targetEntity);
            } catch (CyclicException exCyclic) {
                exCyclic.printStackTrace();
            } catch (RemoteException e) {
                //TODO Handle it properly
                logger.error("Error in setting output URL :" + e.getMessage());
                return null;
            }

        }
        return getRecords((ICab2bQuery) queryObject.getQuery());
    }

    @SuppressWarnings("unchecked")
    private List<IDataRow> getRecords(ICab2bQuery query) {
        IQueryResult<IRecord> relatedQueryResults = null;
        try {
            relatedQueryResults = (IQueryResult<IRecord>) CommonUtils.executeQuery(query, true);
        } catch (Exception e) {
            logger.error("Error in exeuting query :" + e.getMessage());
            return null;
        }

        Map<String, List<IRecord>> allRecords = relatedQueryResults.getRecords();
        List<IDataRow> dataRows = new ArrayList<IDataRow>();
        for (String url : allRecords.keySet()) {
            List<? extends IRecord> results = allRecords.get(url);
            for (IRecord record : results) {
                DataRow dataRow = new DataRow(record, relatedQueryResults.getOutputEntity());
                dataRow.setParent(m_sourceEntity);
                dataRow.setAssociation(m_destinationEntity.getAssociation());
                dataRows.add(dataRow);
            }
        }
        return dataRows;
    }

}

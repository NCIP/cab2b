package edu.wustl.cab2b.client.ui.viewresults;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.util.logger.Logger;

public class QueryExecutionCallable implements Callable<QueryResultObject> {

    private IDataRow m_sourceEntity;

    private IDataRow m_destinationEntity;

    private QueryEngineBusinessInterface m_queryEngineBus;

    private List<IDataRow> m_childNodes;

    public QueryExecutionCallable(
            IDataRow sourceEntity,
            IDataRow destinationEntity,
            QueryEngineBusinessInterface queryEngineBus,
            List<IDataRow> childNodes) {
        m_sourceEntity = sourceEntity;
        m_destinationEntity = destinationEntity;
        m_queryEngineBus = queryEngineBus;
        m_childNodes = childNodes;
    }

    /**
     * 
     */
    public QueryResultObject call() throws Exception {
        // TODO Auto-generated method stub
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
        System.out.println("Source Entity :" + m_sourceEntity.getClassName());
        System.out.println("Source Entity :" + m_destinationEntity.getClassName());
        //	Fire query for every child element
        final ClientQueryBuilder queryObject = new ClientQueryBuilder();
        // construct expression object for source entity
        int size = 1;
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>(size);
        List<String> operators = new ArrayList<String>(size);
        List<List<String>> values = new ArrayList<List<String>>(size);
        List<String> tempValues = new ArrayList<String>(size);
        int idIndex = CommonUtils.getIdAttributeIndexFromAttributes(m_sourceEntity.getAttributes());
        attributes.add(m_sourceEntity.getAttributes().get(idIndex));
        operators.add("Equals");
        tempValues.add((m_sourceEntity.getRow()[idIndex]).toString());
        values.add(tempValues);
        IExpressionId sourceExpressionID = queryObject.addRule(attributes, operators, values);

        /* Get the source expression id. Needed to add the path.*/
        final EntityInterface targetEntity = m_destinationEntity.getAttributes().get(0).getEntity();
        IExpressionId targetExpressionID = queryObject.createDummyExpression(targetEntity);

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
                Logger.out.error("Error in setting output URL :" + e.getMessage());
                return null;
            }
           
        }
        IQueryResult relatedQueryResults = null;
        try {
            relatedQueryResults = CommonUtils.executeQuery((ICab2bQuery) queryObject.getQuery(), m_queryEngineBus);
        } catch (RuntimeException e) {
            Logger.out.error("Error in exeuting query :" + e.getMessage());
            return null;
        } catch (RemoteException e) {
            Logger.out.error("Error in exeuting query :" + e.getMessage());
            return null;
        }

        Map<String, List<IRecord>> allRecords = relatedQueryResults.getRecords();
        attributes = Utility.getAttributeList(relatedQueryResults);
        List<IDataRow> dataRows = new ArrayList<IDataRow>();
        for (String url : allRecords.keySet()) {

            List<IRecord> results = allRecords.get(url);

            for (IRecord record : results) {

                DataRow dataRow = new DataRow();
                //attributes = relatedQueryResults.getAttributes();

                Object[] valueArray = new Object[attributes.size()];
                for (int i = 0; i < attributes.size(); i++) {
                    valueArray[i] = record.getValueForAttribute(attributes.get(i));
                }

                AttributeInterface attrib = attributes.get(0);
                EntityInterface presentEntityInterface = attrib.getEntity();
                //set proper class name
                String strclassName = edu.wustl.cab2b.common.util.Utility.getDisplayName(relatedQueryResults.getOutputEntity());
                AttributeInterface idAttribute = Utility.getIdAttribute(relatedQueryResults.getOutputEntity());
                Object id = record.getValueForAttribute(idAttribute);
                dataRow.setRow(valueArray);
                dataRow.setAttributes(attributes);
                dataRow.setClassName(strclassName);
                dataRow.setParent(m_sourceEntity);
                dataRow.setId(id);
                dataRow.setAssociation(m_destinationEntity.getAssociation());
                dataRow.setEntityInterface(relatedQueryResults.getOutputEntity());
                dataRow.setURL(url);
                dataRows.add(dataRow);
            }
        }
        return dataRows;
    }

}

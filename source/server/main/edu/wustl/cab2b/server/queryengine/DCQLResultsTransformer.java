package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.QueryResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.dcqlresult.DCQLResult;

public class DCQLResultsTransformer {

    IQueryResult getQueryResults(
                                 DCQLQueryResultsCollection queryResultsCollection,
                                 List<AttributeInterface> attributes) {
        QueryResult queryResult = new QueryResult();
        queryResult.setAttributes(attributes);
        for (DCQLResult dcqlQueryResult : queryResultsCollection.getDCQLResult()) {
            CQLQueryResults cqlQueryResult = dcqlQueryResult.getCQLQueryResultCollection();
            List<String[]> recordList = new ArrayList<String[]>();

            CQLQueryResultsIterator itr = new CQLQueryResultsIterator(
                    cqlQueryResult, true);
            while (itr.hasNext()) {
                String singleRecordXml = (String) itr.next();
                String[] row = getOneRow(singleRecordXml, attributes);
                recordList.add(row);
            }
            String[][] arr = recordList.toArray(new String[0][0]);
            queryResult.putRecords(dcqlQueryResult.getTargetServiceURL(), arr);
        }

        return queryResult;

    }

    private String[] getOneRow(String singleRecordXml,
                               List<AttributeInterface> attributes) {
        String[] row = new String[attributes.size()];
        for (int i = 0; i < attributes.size(); i++) {
            row[i] = "";

            AttributeInterface attribute = attributes.get(i);
            String searchStr = attribute.getName() + "=\"";
            int attrStartIndex = singleRecordXml.indexOf(searchStr);
            if (attrStartIndex == -1) {
                continue;
                // TODO log warning?
            }
            // find the ending quote of the value...
            int valStartIndex = attrStartIndex + searchStr.length();
            int endQuoteIndex = singleRecordXml.indexOf("\"", valStartIndex);
            String value = singleRecordXml.substring(valStartIndex,
                                                     endQuoteIndex);
            row[i] = value;
            // TODO anything for dates???
        }
        return row;
    }

}

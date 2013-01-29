/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryLogger;
import edu.wustl.cab2b.server.queryengine.resulttransformers.QueryResultTransformerUtil;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

/**
 * @author Chandrakant Talele
 */
public class QueryResultTransformerUtilTest extends TestCase {
    public void testGetCQLResultsById() {
        String className = "edu.wustl.geneconnect.domain.Gene";
        String idAttrName = "id";
        String url = "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect";
        String id = "3";

        QueryLogger queryLogger = new QueryLogger();
        QueryResultTransformerUtil util = new QueryResultTransformerUtil(queryLogger);
        CQLQueryResults cqlQueryResults = util.getCQLResultsById(className, idAttrName, id, url, null);
        CQLQueryResultsIterator itr = new CQLQueryResultsIterator(cqlQueryResults, true);
        int counter = 0;
        String expected = "<ns2:Gene id=\"3\" ensemblGeneId=\"ENSG00000125834\" entrezGeneId=\"140901\" unigeneClusterId=\"Hs.100057\" ensemblGeneAsOutput=\"true\" entrezGeneAsOutput=\"true\" unigeneAsOutput=\"true\" xmlns:ns2=\"gme://caCORE.cabig/3.0/edu.wustl.geneconnect.domain\"/>";
        while (itr.hasNext()) {
            String singleRecordXml = (String) itr.next();
            System.out.println(singleRecordXml);
            assertEquals(expected, singleRecordXml);
            counter++;
        }
        assertEquals(1, counter);
    }

    public void testGetAttributeResult() {
        String className = "edu.wustl.geneconnect.domain.Gene";
        String idAttrName = "id";
        String[] attributeNames = { "entrezGeneId", "ensemblGeneId" };
        String[] ids = { "1", "2" };
        String url = "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect";

        QueryLogger queryLogger = new QueryLogger();
        QueryResultTransformerUtil util = new QueryResultTransformerUtil(queryLogger);
        List<Map<String, String>> res = util.getAttributeResult(className, attributeNames, idAttrName, ids, url, null);

        assertEquals(2, res.size());

        for (Map<String, String> map : res) {
            for (String name : attributeNames) {
                assertTrue(map.containsKey(name));
            }
        }
    }
}

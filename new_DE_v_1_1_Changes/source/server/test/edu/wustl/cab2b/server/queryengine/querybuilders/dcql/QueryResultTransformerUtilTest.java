/*L
 * Copyright Georgetown University.
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
        String className = "edu.wustl.fe.Gene";
        String idAttrName = "id";
        String url = "http://128.252.227.94:9094/wsrf/services/cagrid/CaFE";
        String id = "3";

        QueryLogger queryLogger = new QueryLogger();
        QueryResultTransformerUtil util = new QueryResultTransformerUtil(queryLogger);
        CQLQueryResults cqlQueryResults = util.getCQLResultsById(className, idAttrName, id, url, null);
        CQLQueryResultsIterator itr = new CQLQueryResultsIterator(cqlQueryResults, true);
        int counter = 0;
        String expected = "<ns2:Gene id=\"3\" symbol=\"A2MP\" name=\"alpha-2-macroglobulin pseudogene\" pubmedCount=\"6\" chromosomeMap=\"12p13.3-p12.3\" xmlns:ns2=\"gme://caCORE.cabig/3.0/edu.wustl.fe\"/>";
        while (itr.hasNext()) {
            String singleRecordXml = (String) itr.next();
            assertEquals(expected, singleRecordXml);
            counter++;
        }
        assertEquals(1, counter);
    }

    public void testGetAttributeResult() {
        String className = "edu.wustl.fe.Gene";
        String idAttrName = "id";
        String[] attributeNames = { "name", "symbol" };
        String[] ids = { "1", "2" };
        String url = "http://128.252.227.94:9094/wsrf/services/cagrid/CaFE";

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

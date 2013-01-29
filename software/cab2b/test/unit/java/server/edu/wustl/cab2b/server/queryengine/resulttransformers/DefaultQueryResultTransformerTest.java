/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.util.TestUtil;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcql.Object;

/**
 * @author Chandrakant Talele
 */
public class DefaultQueryResultTransformerTest extends TestCase {
    public void testGetResults() {
        String name = "edu.wustl.geneconnect.domain.Gene";
        String aName = "id";
        String url = "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect";
        EntityInterface en = TestUtil.getEntity(name, aName);

        Attribute a = new Attribute();
        a.setName(aName);
        a.setPredicate(Predicate.LESS_THAN);
        a.setValue("3");

        Object targetObject = new Object();
        targetObject.setAttribute(a);
        targetObject.setName(name);

        DCQLQuery dcql = new DCQLQuery();
        dcql.setTargetServiceURL(new String[] { url });
        dcql.setTargetObject(targetObject);

        GlobusCredential gc = null;
        DefaultQueryResultTransformer transformer = new DefaultQueryResultTransformer();
        IQueryResult<IRecord> result = transformer.getResults(dcql, en, gc);
        
        Map<String, List<IRecord>> map = result.getRecords();
        assertEquals(1, map.size());
        assertTrue(map.containsKey(url));

        Set<String> ex = new HashSet<String>();
        ex.add("0");
        ex.add("1");
        ex.add("2");
        for(IRecord rec : map.get(url)) {
            String res= (String) rec.getValueForAttribute(en.getAttributeCollection().iterator().next());
            assertTrue(ex.remove(res)); 
        }
        assertTrue(ex.isEmpty());
    }
}

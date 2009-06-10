package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.util.TestUtil;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * @author Chandrakant Talele
 */
public class QueryExecutorTest extends TestCase {
    
    @SuppressWarnings("unchecked")
    public void testExecuteQuery() {

        EntityInterface gene = TestUtil.getEntityWithCaB2BGrp("caFE", "edu.wustl.fe.Gene", "id");
        AttributeInterface id = gene.getAttributeCollection().iterator().next();
        AttributeInterface name = TestUtil.getAttribute("name");
        gene.addAttribute(name);

        List<ICondition> conditions = new ArrayList<ICondition>();
        conditions.add(getCondition(id, RelationalOperator.LessThan,"3"));
        conditions.add(getCondition(name, RelationalOperator.Contains,"alpha"));
        
        IRule rule = QueryObjectFactory.createRule(conditions);

        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(gene);

        IConstraints constraints = Cab2bQueryObjectFactory.createConstraints();
        IExpression expr = constraints.addExpression(queryEntity);
        expr.addOperand(rule);

        String url = "http://128.252.227.94:9094/wsrf/services/cagrid/CaFE";
        
        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        query.setOutputEntity(gene);
        query.setOutputUrls(Arrays.asList(url));
        query.setConstraints(constraints);

        QueryExecutor executor = new QueryExecutor(query, null);
        IQueryResult<? extends IRecord> res = executor.executeQuery();
        
        Map<String, ?> urlVsRecords = res.getRecords();
        assertEquals(1, urlVsRecords.size());
        assertTrue(urlVsRecords.containsKey(url));
        
        List<IRecord> records = (List<IRecord>) urlVsRecords.get(url);
        Set<String> set = new HashSet<String>();
        set.add("alpha-2-macroglobulin");
        set.add("alpha-1-B glycoprotein");
        for (IRecord record : records) {
            String str = (String) record.getValueForAttribute(name);
            assertTrue(set.remove(str));
        }
        assertEquals(0, set.size());
    }
    private ICondition getCondition(AttributeInterface attr, RelationalOperator opr , String val) {
        return QueryObjectFactory.createCondition(attr, opr, Arrays.asList(val));
        
    }
}

package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
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

        EntityInterface gene = TestUtil.getEntityWithGrp("caFE", "edu.wustl.fe.Gene", "id");
        AttributeInterface name = TestUtil.getAttribute("name");
        gene.addAttribute(name);

        EntityGroupInterface eg = gene.getEntityGroupCollection().iterator().next();
        DynamicExtensionUtility.addTaggedValue(eg, Constants.CAB2B_ENTITY_GROUP, Constants.CAB2B_ENTITY_GROUP);

        AttributeInterface id = gene.getAttributeCollection().iterator().next();
        List<String> values = new ArrayList<String>();
        values.add("3");
        ICondition condition = QueryObjectFactory.createCondition(id, RelationalOperator.LessThan, values);

        IRule rule = QueryObjectFactory.createRule(null);
        rule.addCondition(condition);

        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(gene);

        IConstraints constraints = Cab2bQueryObjectFactory.createConstraints();
        IExpression expr = constraints.addExpression(queryEntity);
        expr.addOperand(rule);

        List<String> urls = new ArrayList<String>();
        urls.add("http://128.252.227.94:9094/wsrf/services/cagrid/CaFE");

        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        query.setOutputEntity(gene);
        query.setOutputUrls(urls);
        query.setConstraints(constraints);

        QueryExecutor executor = new QueryExecutor(query, null);
        IQueryResult<? extends IRecord> res = executor.executeQuery();
        Map<String, ?> urlVsRecords = res.getRecords();
        assertTrue(urlVsRecords.containsKey(urls.get(0)));
        List<IRecord> records = (List<IRecord>) urlVsRecords.get(urls.get(0));
        Set<String> set = new HashSet<String>();
        set.add("alpha-2-macroglobulin");
        set.add("alpha-1-B glycoprotein");
        for (IRecord record : records) {
            String str = (String) record.getValueForAttribute(name);
            assertTrue(set.contains(str));
            set.remove(str);
        }
        assertEquals(0, set.size());
    }
}

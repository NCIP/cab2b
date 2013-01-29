/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * JUnitTest cases for  <code>SpreadSheetResultTransformer</code> class.
 * @author deepak_shingan
 *
 */
public class SpreadSheetResultTransformerTest extends TestCase {

    /**
     * Test method for transforming query results in <code>IQueryResult</code> format
     * into spreadsheet data-model format when passed query parameter is null.
     */
    public void testTransResultToSpreadSheetView_QueryParameterIsNull() {
        ICab2bQuery query = createQuery();
        QueryExecutor exe = new QueryExecutor(query, null);
        exe.executeQuery();
        IQueryResult<? extends IRecord> result = exe.getCompleteResults();

        SpreadSheetResultTransformer trnsformer = new SpreadSheetResultTransformer(query, result);
        TransformedResultObjectWithContactInfo transformedResult = trnsformer.transResultToSpreadSheetView(100);
        assertNotNull(transformedResult);
    }

    /**
     * Test method for transforming query results in <code>IQueryResult</code> format
     * into spreadsheet data-model format when passed query result parameter is null.
     */
    public void testTransResultToSpreadSheetView_QueryResultParameterIsNull() {

        ICab2bQuery query = createQuery();
        SpreadSheetResultTransformer trnsformer = new SpreadSheetResultTransformer(query, null);
        TransformedResultObjectWithContactInfo transformedResult = trnsformer.transResultToSpreadSheetView(100);
        assertNull("Query result object is null, method should return null", transformedResult);
    }

    /**
     * Test method for transforming query results in <code>IQueryResult</code> format
     * into spreadsheet data-model format.
     */
    public void testTransResultToSpreadSheetView() {
        ICab2bQuery query = createQuery();
        QueryExecutor exe = new QueryExecutor(query, null);
        exe.executeQuery();
        IQueryResult<? extends IRecord> result = exe.getCompleteResults();

        SpreadSheetResultTransformer trnsformer = new SpreadSheetResultTransformer(query, result);
        TransformedResultObjectWithContactInfo transformedResult = trnsformer.transResultToSpreadSheetView(100);
        assertNotNull("Valid input, method should not return null", transformedResult);
    }
    public ICab2bQuery createQuery() {
        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
        query.setCreatedBy(2L);
        query.setName("Query created from testcase : " + System.nanoTime());
        query.setDescription("Setting some description for test query " + System.currentTimeMillis());

        IConstraints constraints = query.getConstraints();

        EntityInterface entity = null;
        AttributeInterface attribute1 = null;
        AttributeInterface attribute2 = null;
        AttributeInterface attribute3 = null;
        Collection<EntityGroupInterface> entityGroups = EntityCache.getInstance().getEntityGroups();
        for (EntityGroupInterface eg : entityGroups) {
            Collection<EntityInterface> entities = eg.getEntityCollection();
            for (EntityInterface en : entities) {
                Collection<AttributeInterface> attributes = en.getAttributeCollection();
                if (attributes.size() >= 3) {
                    Iterator<AttributeInterface> iterator = attributes.iterator();
                    attribute1 = iterator.next();
                    attribute2 = iterator.next();
                    attribute3 = iterator.next();
                    entity = en;
                    break;
                }
            }
            if (attribute1 != null && attribute2 != null && attribute3 != null) {
                break;
            }
        }

        if (attribute1 == null || attribute2 == null || attribute3 == null) {
            fail("NO entity with 3 attributes found !!!. This testcase needs it");
        }
        List<ICondition> conditions = new ArrayList<ICondition>();
        ICondition condition1 = getCondition(attribute2, RelationalOperator.Contains, "true");
        ICondition condition2 = getCondition(attribute1, RelationalOperator.Contains, "123");
        ICondition condition3 = getCondition(attribute3, RelationalOperator.IsNotNull, "");

        conditions.add(condition1);
        conditions.add(condition2);
        conditions.add(condition3);

        IRule rule = QueryObjectFactory.createRule(conditions);
        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(entity);

        IExpression expression = constraints.addExpression(queryEntity);
        expression.addOperand(rule);

        String url = "http://array.nci.nih.gov:80/wsrf/services/cagrid/CaArraySvc";
        query.setOutputEntity(expression.getQueryEntity().getDynamicExtensionsEntity());
        query.setOutputUrls(Arrays.asList(url));

        return query;
    }

    // Creates condition
    private ICondition getCondition(AttributeInterface attr, RelationalOperator opr, String val) {
        return QueryObjectFactory.createCondition(attr, opr, Arrays.asList(val));
    }
}

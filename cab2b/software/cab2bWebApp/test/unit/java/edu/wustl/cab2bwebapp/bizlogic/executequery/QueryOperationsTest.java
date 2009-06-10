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
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.queryengine.QueryOperations;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

public class QueryOperationsTest extends TestCase {
    public void testSave() {
        EntityCache.getInstance();

        long id = 10L;

        for (int i = 0; i < 10; i++) {
            QueryOperations opr = new QueryOperations();
            ICab2bQuery query = createQuery();
            opr.saveQuery(query);
            System.out.print(query.getId() + " ");
        }

        System.out.println("");
        List<ICab2bQuery> queries = new QueryOperations().getAllQueries();
        for (ICab2bQuery q : queries) {
            if (q.getId() > id) {
                System.out.println("---------------------------------");
                System.out.println("ID : " + q.getId() + " name : " + q.getName() + " desc : "
                        + q.getDescription());
                for (IExpression exp : q.getConstraints()) {
                    for (IExpressionOperand op : exp) {
                        if (op instanceof IRule) {
                            for (ICondition c : (IRule) op) {
                                System.out.println(c.getAttribute().getName() + " " + c.getRelationalOperator()
                                        + " " + c.getValues());
                            }
                        }
                    }
                }
            }
        }
    }

    long getQueryMaxId() {
        long id = 0;
        QueryOperations opr = new QueryOperations();
        List<ICab2bQuery> queries = opr.getAllQueries();
        for (ICab2bQuery q : queries) {
            if (id < q.getId()) {
                id = q.getId();
            }
        }
        return id;
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
        if (!entityGroups.isEmpty()) {
            Collection<EntityInterface> entities = entityGroups.iterator().next().getEntityCollection();
            if (!entities.isEmpty()) {
                entity = entities.iterator().next();

                Collection<AttributeInterface> attributes = entity.getAttributeCollection();
                if (attributes.size() >= 3) {
                    Iterator<AttributeInterface> iterator = attributes.iterator();
                    attribute1 = iterator.next();
                    attribute2 = iterator.next();
                    attribute3 = iterator.next();
                }
            }
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

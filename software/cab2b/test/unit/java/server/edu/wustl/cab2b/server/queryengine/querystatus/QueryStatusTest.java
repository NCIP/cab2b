/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querystatus;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatusImpl;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.queryengine.QueryOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2b.server.util.TestConnectionUtil;
import edu.wustl.cab2b.server.util.UtilityOperations;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * @author gaurav_mehta
 *
 */
public class QueryStatusTest extends TestCase {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryStatusTest.class);

    public void testSaveQueryStatus() {
        try {
            UserOperations userop = new UserOperations();
            UserInterface user = userop.getUserById(Long.toString(2L));

            ICab2bQuery query = createQuery();
            Set<URLStatus> urls = new HashSet<URLStatus>(1);

            String queryConditions =
                    new UtilityOperations().getStringRepresentationofConstraints(query.getConstraints());
            System.out.println(queryConditions);
            URLStatus urStatus = new URLStatusImpl();
            urStatus.setStatus("In Progresss");
            urStatus.setResultCount(5);
            urStatus.setMessage("This url is working fine");
            urStatus.setUrl("http://array.nci.nih.gov:80/wsrf/services/cagrid/CaArraySvc");
            urls.add(urStatus);

            QueryStatus queryStatus = new QueryStatusImpl();
            queryStatus.setStatus("Executed");
            queryStatus.setResultCount(0);
            queryStatus.setMessage("This is the Test insert Operation");

            queryStatus.setQuery(query);
            queryStatus.setUser(user);

            queryStatus.setQueryConditions(queryConditions);
            queryStatus.setQueryStartTime(new Date());
            queryStatus.setQueryEndTime(new Date());
            queryStatus.setFileName("Hello.xls");
            queryStatus.setVisible(Boolean.TRUE);
            queryStatus.setChildrenQueryStatus(null);
            queryStatus.setUrlStatus(urls);

            QueryURLStatusOperations queryURLStatusOperations = new QueryURLStatusOperations();
            queryURLStatusOperations.insertQueryStatus(queryStatus);
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            throw new RuntimeException("Eroor occured while saving Query Status", e);
        }
    }

    public void testRetreiveQueryStatus() {
        loadCache();
        UserInterface user = new UserOperations().getUserById(Long.toString(2L));
        Collection<QueryStatus> qss = new QueryURLStatusOperations().getAllQueryStatusByUser(user);

        for (QueryStatus qs : qss) {
            System.out.println(qs.getId());
            System.out.println(qs.getQueryConditions());
            for (URLStatus us : qs.getUrlStatus()) {
                System.out.println("URL Status: " + us.getId());
            }
        }
    }

    /*public void testDeleteQueryStatus() {
        loadCache();
        QueryURLStatusOperations qo = new QueryURLStatusOperations();
        QueryStatus qss = qo.getQueryStatusById(1L);
        qo.deleteQueryStatus(qss);
    }*/

    private void loadCache() {
        Connection con = TestConnectionUtil.getConnection();
        try {
            PathFinder.getInstance(con);
        } finally {
            TestConnectionUtil.close(con);
        }
        EntityCache.getInstance();
    }

    private ICab2bQuery createQuery() {
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
        new QueryOperations().saveQuery(query);
        return query;
    }

    private ICondition getCondition(AttributeInterface attr, RelationalOperator opr, String val) {
        return QueryObjectFactory.createCondition(attr, opr, Arrays.asList(val));
    }
}
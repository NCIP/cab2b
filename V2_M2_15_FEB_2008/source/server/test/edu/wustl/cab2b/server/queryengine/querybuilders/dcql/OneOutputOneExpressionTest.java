package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.dcql.Object;

public class OneOutputOneExpressionTest extends BaseQueryBuilderTestUtil {

    private IExpressionId exprId;

    private IOutputTreeNode output;

//    private IClass klass;

    private String[] urls;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
//        klass = createClass(classNamePrefix + 1);
//        output = QueryObjectFactory.createOutputTreeNode(klass);
//        exprId = createExpressionId(1);
//        urls = new String[] { "url1", "url2" };
    }

    public void testOneParantheses() {
//        final int numConditions = 4;
//
//        ICondition[] conditions = new ICondition[numConditions];
//        Attribute[] attributes = new Attribute[numConditions];
//        for (int i = 0; i < numConditions; i++) {
//            conditions[i] = createCondition(createAttribute(attrNamePrefix + i,
//                                                            klass),
//                                            RelationalOperator.Equals, "val"
//                                                    + i);
//            attributes[i] = createDcqlAttribute(attrNamePrefix + i,
//                                                Predicate.EQUAL_TO, "val" + i);
//        }
//        IConstraints constraints = QueryObjectFactory.createConstraints();
//        IExpression expression = constraints.addExpression(klass);
//        expression.addOperand(createRule(conditions[0], conditions[1]));
//        expression.addOperand(createLogicalConnector(LogicalOperator.Or, 1),
//                              createRule(conditions[2]));
//        expression.addOperand(createLogicalConnector(LogicalOperator.And, 0),
//                              createRule(conditions[3]));
//
//        Group ruleGroup = new Group();
//        ruleGroup.setLogicRelation(gov.nih.nci.cagrid.cqlquery.LogicalOperator.AND);
//        ruleGroup.setAttribute(new Attribute[] { attributes[0], attributes[1] });
//        
//        Group innerGroup = new Group();
//        innerGroup.setLogicRelation(gov.nih.nci.cagrid.cqlquery.LogicalOperator.OR);
//        innerGroup.setAttribute(new Attribute[] { attributes[2] });
//        innerGroup.setGroup(new Group[] { ruleGroup });
//
//        Group outerGroup = new Group();
//        outerGroup.setLogicRelation(gov.nih.nci.cagrid.cqlquery.LogicalOperator.AND);
//        outerGroup.setGroup(new Group[] { innerGroup });
//        outerGroup.setAttribute(new Attribute[] { attributes[3] });
//        
//        Object target = new Object();
//        target.setGroup(outerGroup);
//        check(constraints, target);

        // check(constraintsMock, "3R_1Par");
    }

    private void check(IConstraints constraints, Object expectedTarget) {
//        QueryBuilder queryBuilder = new QueryBuilder();
//        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
//        query.setRootOutputClass(output);
//        query.setOutputClassUrls(Arrays.asList(urls));
//        query.setConstraints(constraints);
//        DCQLQuery actualQuery = queryBuilder.buildQuery(query);
//
//        DCQLQuery expectedQuery = new DCQLQuery();
//        expectedQuery.setTargetServiceURL(urls);
//        expectedTarget.setName(klass.getFullyQualifiedName());
//        expectedQuery.setTargetObject(expectedTarget);
//        expectedQuery.equals(actualQuery);
//        assertEquals(expectedQuery, actualQuery);
//        
//        check(expectedQuery, "FOOBAR");
    }

    public void testOneRule() {
//        IRule rule = createRule(createCondition(
//                                                createAttribute(
//                                                                attrNamePrefix + 1,
//                                                                klass),
//                                                RelationalOperator.LessThan,
//                                                "val1"));
//        IConstraints constraints = QueryObjectFactory.createConstraints();
//        constraints.addExpression(klass).addOperand(rule);
//
//        Object expectedTarget = new Object();
//        expectedTarget.setAttribute(createDcqlAttribute(attrNamePrefix + 1,
//                                                        Predicate.LESS_THAN,
//                                                        "val1"));
//
//        check(constraints, expectedTarget);
    }

    private Attribute createDcqlAttribute(String attrName, Predicate predicate,
                                          String val) {
        Attribute attr = new Attribute();
//        attr.setName(attrName);
//        attr.setPredicate(predicate);
//        attr.setValue(val);
//
        return attr;
    }

    public void testEmpty() {
//        QueryBuilder queryBuilder = new QueryBuilder();
//        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
//        query.setRootOutputClass(output);
//        query.setOutputClassUrls(Collections.singletonList("foo"));
//        DCQLQuery actualQuery = queryBuilder.buildQuery(query);
//
//        DCQLQuery expectedQuery = new DCQLQuery();
//        expectedQuery.setTargetServiceURL(new String[] { "foo" });
//        Object target = new Object();
//        target.setName(klass.getFullyQualifiedName());
//        expectedQuery.setTargetObject(target);
    }

}

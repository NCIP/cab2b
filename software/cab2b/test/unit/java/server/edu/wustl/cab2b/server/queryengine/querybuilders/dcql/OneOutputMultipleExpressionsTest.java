/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;


public class OneOutputMultipleExpressionsTest extends BaseQueryBuilderTestUtil {
    public void testOneNestedExpression() {
        // Parent expression
//        IClass class1 = createClass(classNamePrefix + 1);
//        IAttribute attribute11 = createAttribute(attrNamePrefix + "c1a1",
//                                                 class1);
//        // suffix is <expr, rule>
//        IRule rule11 = createRule(createCondition(attribute11,
//                                                  RelationalOperator.Equals,
//                                                  "val11"));
//
//        // suffix is <expr, leftop, rightop>
//        ILogicalConnector[] conns = new ILogicalConnector[] { createLogicalConnector(
//                                                                                     LogicalOperator.Or,
//                                                                                     0) };
//        Mock constraintsMock = createConstraintsMock();
//        IExpressionId parentExprId = createExpressionId(1);
//
//        Mock parentExpressionMock = createExpressionMock(constraintsMock,
//                                                         parentExprId, class1);
//
//        // Child expression
//        IClass class2 = createClass(classNamePrefix + 2);
//        IAttribute attribute21 = createAttribute(attrNamePrefix + "c2a1",
//                                                 class2);
//        IRule rule21 = createRule(createCondition(attribute21,
//                                                  RelationalOperator.LessThan,
//                                                  "val21"));
//
//        IExpressionId childExprId = createExpressionId(2);
//        Mock childExpressionMock = createExpressionMock(constraintsMock,
//                                                        childExprId, class2);
//        setOperandsOfExpression(childExpressionMock, rule21);
//
//        setOperandsOfExpression(parentExpressionMock, rule11, childExprId);
//        setLogicalConnectorsOfExpression(parentExpressionMock, conns);
//
//        IJoinGraph joinGraph = createJoinGraph(createEdge(
//                                                          parentExprId,
//                                                          childExprId,
//                                                          createLocalAssociation(
//                                                                                 class1,
//                                                                                 class2,
//                                                                                 "villain")));
//        setJoinGraph(constraintsMock, joinGraph);
//        setRootExpressionId(constraintsMock, parentExprId);
//        check(createQuery((IConstraints) constraintsMock.proxy(), class1),
//              "1NE_1R");
//
//        // add another rule to parent expr
//        IRule rule12 = createRule(createCondition(
//                                                  attribute11,
//                                                  RelationalOperator.GreaterThan,
//                                                  "val12"));
//        conns = new ILogicalConnector[] { createLogicalConnector(
//                                                                 LogicalOperator.Or,
//                                                                 1), createLogicalConnector(
//                                                                                            LogicalOperator.And,
//                                                                                            0) };
//
//        setOperandsOfExpression(parentExpressionMock, rule11, childExprId,
//                                rule12);
//        setLogicalConnectorsOfExpression(parentExpressionMock, conns);
//
//        check(createQuery((IConstraints) constraintsMock.proxy(), class1),
//              "R_E_R");
    }

    /**
     * Graph tested here is Root has 2 children (C1, C2). These two children
     * each have a single child (leaf) which is common. The leaf has a single
     * condition.
     */
    public void testSharedChild() {
        // Parent expression
//        IClass rootClass = createClass(classNamePrefix + "root");
//        IClass interiorClass1 = createClass(classNamePrefix + "int1");
//        IClass interiorClass2 = createClass(classNamePrefix + "int2");
//        IClass leafClass = createClass(classNamePrefix + "leaf");
//
//        IAttribute leafAttribute = createAttribute(attrNamePrefix + "leaf",
//                                                   leafClass);
//
//        Mock constraintsMock = createConstraintsMock();
//        IExpressionId rootExprId = createExpressionId(1);
//        IExpressionId intExpr1Id = createExpressionId(2);
//        IExpressionId intExpr2Id = createExpressionId(3);
//        IExpressionId leafExprId = createExpressionId(4);
//
//        Mock rootExprMock = createExpressionMock(constraintsMock, rootExprId,
//                                                 rootClass);
//        Mock intExpr1Mock = createExpressionMock(constraintsMock, intExpr1Id,
//                                                 interiorClass1);
//        Mock intExpr2Mock = createExpressionMock(constraintsMock, intExpr2Id,
//                                                 interiorClass1);
//        Mock leafExprMock = createExpressionMock(constraintsMock, leafExprId,
//                                                 leafClass);
//
//        setOperandsOfExpression(rootExprMock, intExpr1Id, intExpr2Id);
//        setOperandsOfExpression(intExpr1Mock, leafExprId);
//        setOperandsOfExpression(intExpr2Mock, leafExprId);
//
//        IRule rule = createRule(createCondition(leafAttribute,
//                                                RelationalOperator.Equals,
//                                                "val"));
//        setOperandsOfExpression(leafExprMock, rule);
//
//        ILogicalConnector[] rootExprConns = new ILogicalConnector[] { createLogicalConnector(
//                                                                                             LogicalOperator.And,
//                                                                                             0) };
//        setLogicalConnectorsOfExpression(rootExprMock, rootExprConns);
//
//        Edge rootInt1 = createEdge(rootExprId, intExpr1Id,
//                                   createLocalAssociation(rootClass,
//                                                          interiorClass1,
//                                                          "root_int1"));
//        Edge rootInt2 = createEdge(rootExprId, intExpr2Id,
//                                   createLocalAssociation(rootClass,
//                                                          interiorClass2,
//                                                          "root_int2"));
//        Edge int1Leaf = createEdge(intExpr1Id, leafExprId,
//                                   createLocalAssociation(interiorClass1,
//                                                          leafClass,
//                                                          "int1_leaf"));
//        Edge int2Leaf = createEdge(intExpr2Id, leafExprId,
//                                   createLocalAssociation(interiorClass2,
//                                                          leafClass,
//                                                          "int2_leaf"));
//        IJoinGraph joinGraph = createJoinGraph(rootInt1, rootInt2, int1Leaf,
//                                               int2Leaf);
//        setJoinGraph(constraintsMock, joinGraph);
//        setRootExpressionId(constraintsMock, rootExprId);
//        check(createQuery((IConstraints) constraintsMock.proxy(), rootClass),
//              "SharedChild");

    }
}

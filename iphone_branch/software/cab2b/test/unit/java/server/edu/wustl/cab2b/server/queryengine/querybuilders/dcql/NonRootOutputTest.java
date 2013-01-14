/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;


public class NonRootOutputTest extends BaseQueryBuilderTestUtil {
    public void testFoo() {
//        ICab2bQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
//        IConstraints constraints = QueryObjectFactory.createConstraints();
//
//        IClass parentClass = QueryObjectFactory.createClass();
//        parentClass.setFullyQualifiedName("parent");
//
//        IClass childClass = QueryObjectFactory.createClass();
//        childClass.setFullyQualifiedName("child");
//
//        IExpression parentExpr = constraints.addExpression(parentClass);
//        IExpression childExpr = constraints.addExpression(childClass);
//        parentExpr.addOperand(childExpr.getExpressionId());
//
//        IIntraModelAssociation association = QueryObjectFactory.createIntraModelAssociation(
//                                                                                            parentClass,
//                                                                                            childClass,
//                                                                                            "parentRole",
//                                                                                            "childRole",
//                                                                                            true);
//        try {
//            constraints.getJoinGraph().putAssociation(
//                                                      parentExpr.getExpressionId(),
//                                                      childExpr.getExpressionId(),
//                                                      association);
//        } catch (CyclicException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        IOutputTreeNode outputTreeNode = new OutputTreeNode(parentClass);
//        query.setRootOutputClass(outputTreeNode);
//        query.setConstraints(constraints);
//        query.setOutputClassUrls(Collections.singletonList("outputUrl1"));
//
//        IRule parentRule = QueryObjectFactory.createRule();
//        ICondition parentCondition = parentRule.addCondition();
//        parentCondition.setAttribute(QueryObjectFactory.createAttribute(
//                                                                        DataType.String,
//                                                                        parentClass,
//                                                                        "parentAttr"));
//        parentCondition.setRelationalOperator(RelationalOperator.Equals);
//        parentCondition.setValue("parentVal");
//        parentExpr.addOperand(
//                              QueryObjectFactory.createLogicalConnector(LogicalOperator.And),
//                              parentRule);
//
//        IRule childRule = QueryObjectFactory.createRule();
//        ICondition childCondition = childRule.addCondition();
//        childCondition.setAttribute(QueryObjectFactory.createAttribute(
//                                                                       DataType.String,
//                                                                       childClass,
//                                                                       "childAttr"));
//        childCondition.setRelationalOperator(RelationalOperator.Equals);
//        childCondition.setValue("childVal");
//        childExpr.addOperand(childRule);
//
//        check(query, "NonRootOutput");
    }
}

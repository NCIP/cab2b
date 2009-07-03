package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import edu.wustl.cab2b.server.queryengine.base.BaseQueryEngineTestUtil;

public abstract class BaseQueryBuilderTestUtil extends BaseQueryEngineTestUtil {
    protected final String classNamePrefix = "srinath.mocks.query.class_";

    protected final String attrNamePrefix = "attr_";

    private static final String RES = ".res";

//    protected class Edge {
//        IExpressionId parentExprId;
//
//        IExpressionId childExprId;
//
//        IAssociation association;
//
//        public Edge(
//                IExpressionId parentExprId,
//                IExpressionId childExprId,
//                IAssociation association) {
//            this.parentExprId = parentExprId;
//            this.childExprId = childExprId;
//            this.association = association;
//        }
//
//        public IAssociation getAssociation() {
//            return association;
//        }
//
//        public IExpressionId getChildExpressionId() {
//            return childExprId;
//        }
//
//        public IExpressionId getParentExpressionId() {
//            return parentExprId;
//        }
//    }
//
//    protected Mock createConstraintsMock() {
//        return mock(IConstraints.class);
//    }
//
//    protected void setRootExpressionId(Mock constraintsMock,
//                                       IExpressionId rootExpressionId) {
//        constraintsMock.expects(atLeastOnce()).method("getRootExpressionId").will(
//                                                                                  returnValue(rootExpressionId));
//    }
//
//    protected void setJoinGraph(Mock constraintsMock, IJoinGraph joinGraph) {
//        constraintsMock.stubs().method("getJoinGraph").will(
//                                                            returnValue(joinGraph));
//    }
//
//    protected IOutputTreeNode createOutputTreeNode(
//                                                   IFunctionalClass functionalClass) {
//        Mock mock = mock(IOutputTreeNode.class);
//        mock.expects(atLeastOnce()).method("getFunctionalClass").will(
//                                                                      returnValue(functionalClass));
//        return (IOutputTreeNode) mock.proxy();
//    }
//
//    protected ICab2bQuery createQuery(IConstraints constraints,
//                                      IFunctionalClass output) {
//        Mock queryMock = mock(ICab2bQuery.class);
//        queryMock.expects(atLeastOnce()).method("getConstraints").will(
//                                                                       returnValue(constraints));
//
//        queryMock.expects(atLeastOnce()).method("getRootOutputClass").will(
//                                                                           returnValue(createOutputTreeNode(output)));
//
//        queryMock.expects(atLeastOnce()).method("getOutputClassUrl").will(
//                                                                          returnValue("http://targetUrl"));
//        return (ICab2bQuery) queryMock.proxy();
//    }
//
//    protected Edge createEdge(IExpressionId parentExprId,
//                              IExpressionId childExprId,
//                              IAssociation association) {
//        return new Edge(parentExprId, childExprId, association);
//    }
//
//    protected IJoinGraph createJoinGraph() {
//        Mock joinGraphMock = mock(IJoinGraph.class);
//
//        return (IJoinGraph) joinGraphMock.proxy();
//    }
//
//    protected IJoinGraph createJoinGraph(Edge... edges) {
//        Mock joinGraphMock = mock(IJoinGraph.class);
//
//        for (int i = 0; i < edges.length; i++) {
//            joinGraphMock.expects(atLeastOnce()).method("getAssociation").with(
//                                                                               same(edges[i].getParentExpressionId()),
//                                                                               same(edges[i].getChildExpressionId())).will(
//                                                                                                                           returnValue(edges[i].getAssociation()));
//        }
//        return (IJoinGraph) joinGraphMock.proxy();
//    }
//
//    /**
//     * @param expressionMock
//     * @param logicalConnectors
//     */
//    protected void setLogicalConnectorsOfExpression(
//                                                    Mock expressionMock,
//                                                    ILogicalConnector... logicalConnectors) {
//        for (int i = 0; i < logicalConnectors.length; i++) {
//            expressionMock.expects(atLeastOnce()).method("getLogicalConnector").with(
//                                                                                     eq(i),
//                                                                                     eq(i + 1)).will(
//                                                                                                     returnValue(logicalConnectors[i]));
//        }
//    }
//
//    protected void setOperandsOfExpression(Mock expressionMock,
//                                           IExpressionOperand... rules) {
//        for (int i = 0; i < rules.length; i++) {
//            expressionMock.expects(atLeastOnce()).method("getOperand").with(
//                                                                            eq(i)).will(
//                                                                                        returnValue(rules[i]));
//        }
//        expressionMock.expects(atLeastOnce()).method("numberOfOperands").will(
//                                                                              returnValue(rules.length));
//    }
//
//    protected Mock createExpressionMock(Mock constraintsMock,
//                                        IExpressionId expressionId,
//                                        IFunctionalClass functionalClass) {
//        Mock expressionMock = mock(IExpression.class);
//        constraintsMock.expects(atLeastOnce()).method("getExpression").with(
//                                                                            same(expressionId)).will(
//                                                                                                     returnValue(expressionMock.proxy()));
//        expressionMock.expects(atLeastOnce()).method("getFunctionalClass").will(
//                                                                                returnValue(functionalClass));
//        expressionMock.stubs().method("getExpressionId").will(
//                                                              returnValue(expressionId));
//        return expressionMock;
//    }
//
//    protected Mock createExpressionMock(Mock constraintsMock, int id,
//                                        IFunctionalClass functionalClass) {
//        return createExpressionMock(constraintsMock, createExpressionId(id),
//                                    functionalClass);
//    }
//
//    protected IExpressionId createExpressionId(final int id) {
//        return new IExpressionId() {
//
//            public int getInt() {
//                return id;
//            }
//
//            public boolean isSubExpressionOperand() {
//                return true;
//            }
//
//        };
//    }
//
//    protected IIntraModelAssociation createLocalAssociation(
//                                                            IClass source,
//                                                            IClass target,
//                                                            String targetRoleName) {
//        return createLocalAssociation(source, target, "", targetRoleName);
//    }
//
//    protected IInterModelAssociation createForeignAssociation(
//                                                              IAttribute sourceAttribute,
//                                                              IAttribute targetAttribute,
//                                                              String sourceUrl,
//                                                              String targetUrl) {
//        IInterModelAssociation interModelAssociation = QueryObjectFactory.createInterModelAssociation(sourceAttribute, targetAttribute);
//
//        interModelAssociation.addTargetServiceUrl(sourceUrl, targetUrl);
//        return interModelAssociation;
//    }
//
//    protected IIntraModelAssociation createLocalAssociation(
//                                                            IClass source,
//                                                            IClass target,
//                                                            String sourceRoleName,
//                                                            String targetRoleName) {
//        IIntraModelAssociation intraModelAssociation = QueryObjectFactory.createIntraModelAssociation(source, target, sourceRoleName, targetRoleName, false);
//        return intraModelAssociation;
//    }
//
//    protected ILogicalConnector createLogicalConnector(
//                                                       LogicalOperator logicalOperator,
//                                                       int nestingNum) {
//
//        return QueryObjectFactory.createLogicalConnector(logicalOperator,
//                                                         nestingNum);
//    }
//
//    protected IRule createRule(ICondition... conditions) {
//        IRule rule = QueryObjectFactory.createRule(null);
//        for (ICondition condition : conditions) {
//            rule.addCondition(condition);
//        }
//        return rule;
//    }
//
//    protected ICondition createCondition(IAttribute attribute,
//                                         RelationalOperator relationalOperator,
//                                         String... values) {
//        ICondition condition = QueryObjectFactory.createCondition();
//        condition.setAttribute(attribute);
//        condition.setRelationalOperator(relationalOperator);
//        condition.setValues(Arrays.asList(values));
//        return condition;
//    }
//
//    protected IAttribute createAttribute(String attributeName, IClass klass) {
//        IAttribute attribute = QueryObjectFactory.createAttribute(
//                                                                  DataType.String,
//                                                                  klass,
//                                                                  attributeName);
//        return attribute;
//    }
//
//    protected IClass createClass(String fullyQualifiedName) {
//        IClass klass = QueryObjectFactory.createClass();
//        klass.setFullyQualifiedName(fullyQualifiedName);
//        klass.setAttributes(new ArrayList<IAttribute>());
//
//        return klass;
//    }
//
//    protected void check(ICab2bQuery query, String fileName) {
//        QueryBuilder queryBuilder = new QueryBuilder();
//        check(queryBuilder.buildQuery(query), fileName);
//    }
//
//    protected void check(DCQLQuery query, String fileName) {
//        try {
//            Utils.serializeDocument(fileName, query, DCQLQuery.getTypeDesc().getXmlType());
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
////        assertTrue(check(xml, fileName));
//    }
//
//    public boolean check(String resultString, String compareStringFileName) {
//        String res = resultString;
//        String fileNameRoot = getClass().getSimpleName() + '_'
//                + compareStringFileName;
//        String cmp = testText(fileNameRoot + RES);
//        String res1 = rationalizeWhitespace(res);
//        String cmp1 = rationalizeWhitespace(cmp);
//
//        if (res1.equals(cmp1)) {
//            return true;
//        } else {
//            // String fileNameRoot = compareStringFileName.replaceAll("\\.res",
//            // "");
//
//            String tgtDir = "errors";
//            String tgtFile = "errors/" + fileNameRoot;
//            writeText(BaseQueryBuilderTestUtil.class, tgtDir, tgtFile
//                    + ".expected", cmp);
//            writeText(BaseQueryBuilderTestUtil.class, tgtDir, tgtFile + ".res",
//                      res);
//
//            return false;
//        }
//    }
}

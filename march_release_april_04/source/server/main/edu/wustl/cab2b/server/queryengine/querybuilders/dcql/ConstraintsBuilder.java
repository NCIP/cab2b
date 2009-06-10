package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AbstractAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AttributeConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.ForeignAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.LocalAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.utils.TransformerUtil;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.DataType;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.ILogicalConnector;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.dcql.Association;
import gov.nih.nci.cagrid.dcql.ForeignAssociation;
import gov.nih.nci.cagrid.dcql.ForeignPredicate;
import gov.nih.nci.cagrid.dcql.JoinCondition;
import gov.nih.nci.cagrid.dcql.Object;

/**
 * Builds the {@link ConstraintsBuilderResult} for the given constraints. It
 * parses all the constraints, starting from the root expression. The processing
 * is as follows : <br>
 * 1. Form a
 * {@link edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint}
 * for each {@link edu.wustl.common.querysuite.queryobject.IExpression}. In
 * this step, the expressions are parsed in a bottom-up fashion, i.e. the
 * constraint for one expression is built only after all its children
 * expressions have been processed. This step results in each expression also
 * being constrained by the children expressions. <br>
 * 2. The expression tree is then traversed in a breadth-first fashion, and each
 * expression is now constrained by its parents. (this is temporary, and this
 * step may change when we move to multiple outputs).
 * @author srinath_k
 */
class ConstraintsBuilder {

    private ConstraintsBuilderResult result;

    private IConstraints constraints;

    private IExpression rootExpr;

    private static final String DCQL_WILDCARD = "%";

    /**
     * This is probably temporary (might be removed when multiple outputs are to
     * be supported)
     */
    private final static boolean constrainByParentExpressions = true;

    /**
     * @return the rootExpr.
     */
    public IExpression getRootExpr() {
        return rootExpr;
    }

    /**
     * @param rootExpr
     *            the rootExpr to set.
     */
    public void setRootExpr(IExpression rootExpr) {
        this.rootExpr = rootExpr;
    }

    public ConstraintsBuilder(IConstraints constraints) {
        setConstraints(constraints);
        setResult(new ConstraintsBuilderResult());
        setRootExpr(findRootExpression());
    }

    public ConstraintsBuilderResult buildConstraints() {
        createDcqlConstraintForExpression(getRootExpr());
        ConstraintsBuilderResult res = getResult();
        if (!constrainByParentExpressions) {
            return res;
        }
        constrainByParentExpressions();
        return getResult();
    }

    // BEGIN CONSTRAINING EACH EXPR BY PARENTS
    private void constrainByParentExpressions() {
        Set<IExpressionId> processedExpressions = new HashSet<IExpressionId>();
        Set<IExpressionId> currExprIds = new HashSet<IExpressionId>();
        currExprIds.add(getRootExpr().getExpressionId());

        while (!currExprIds.isEmpty()) {
            Set<IExpressionId> nextExprIds = new HashSet<IExpressionId>();
            for (IExpressionId currExprId : currExprIds) {
                if (processedExpressions.contains(currExprId)) {
                    continue;
                }
                List<IExpressionId> parentExprIds = getJoinGraph().getParentList(
                                                                                 currExprId);

                assert processedExpressions.containsAll(parentExprIds);
                constrainChildByParents(currExprId, parentExprIds);

                nextExprIds.addAll(getJoinGraph().getChildrenList(currExprId));
                processedExpressions.add(currExprId);
            }
            currExprIds = nextExprIds;
        }
        assert processedExpressions.equals(getResult().getExpressionToConstraintMap().keySet());
    }

    private void constrainChildByParents(IExpressionId childExprId,
                                         List<IExpressionId> parentExprIds) {
        IExpression childExpr = getExpression(childExprId);
        DcqlConstraint childConstraint = getResult().getConstraintForExpression(
                                                                                childExpr);

        MyGroup group = new MyGroup(LogicalOperator.And);
        group.addConstraint(childConstraint);

        for (IExpressionId parentExprId : parentExprIds) {
            IExpression parentExpr = getExpression(parentExprId);
            IAssociation association = getJoinGraph().getAssociation(
                                                                     parentExprId,
                                                                     childExprId);
            if (!association.isBidirectional()) {
                Logger.out.warn("Unidirectional association found "
                        + association + ". Results could be incorrect.");
                continue;
            }
            DcqlConstraint parentConstraint = getResult().getConstraintForExpression(
                                                                                     parentExpr);
            AbstractAssociationConstraint associationConstraint = createAssociation(
                                                                                    association,
                                                                                    true);
            associationConstraint.addChildConstraint(parentConstraint);
            group.addConstraint(associationConstraint);
        }
        getResult().putConstraintForExpression(childExpr,
                                               group.getDcqlConstraint(), true);
    }

    // END CONSTRAINING EACH EXPR BY PARENTS

    private IExpression findRootExpression() {
        try {
            IExpressionId rootExpressionId = getConstraints().getRootExpressionId();
            return getConstraints().getExpression(rootExpressionId);
        } catch (MultipleRootsException e) {
            String msg = "Invalid query object submitted; it's got multiple roots...";
            Logger.out.error(msg);
            throw new RuntimeException(msg, e,
                    ErrorCodeConstants.QUERY_INVALID_INPUT);
        }

    }

    private DcqlConstraint createDcqlConstraintForExpression(IExpression expr) {

        List<DcqlConstraint> dcqlConstraintsList = new ArrayList<DcqlConstraint>(
                expr.numberOfOperands());

        for (int i = 0; i < expr.numberOfOperands(); i++) {
            IExpressionOperand operand = expr.getOperand(i);
            if (operand.isSubExpressionOperand()) {
                dcqlConstraintsList.add(createDcqlConstraintForChildExpression(
                                                                               expr.getExpressionId(),
                                                                               (IExpressionId) operand));
            } else {
                dcqlConstraintsList.add(createDcqlConstraintForRule((IRule) operand));
            }
        }

        DcqlConstraint dcqlConstraint = mergeConstraints(expr,
                                                         dcqlConstraintsList);
        getResult().putConstraintForExpression(expr, dcqlConstraint,
                                               !constrainByParentExpressions);
        return dcqlConstraint;
    }

    private DcqlConstraint mergeConstraints(
                                            IExpression expr,
                                            List<DcqlConstraint> dcqlConstraintsList) {
        if (dcqlConstraintsList.size() == 0) {
            return new DcqlConstraint();
        }
        if (dcqlConstraintsList.size() == 1) {
            return dcqlConstraintsList.get(0);
        }
        return new GroupBuilder(getConnectors(expr), dcqlConstraintsList).buildGroup();
    }

    private List<ILogicalConnector> getConnectors(IExpression expression) {
        List<ILogicalConnector> conns = new ArrayList<ILogicalConnector>();
        for (int i = 0; i < expression.numberOfOperands() - 1; i++) {
            conns.add(expression.getLogicalConnector(i, i + 1));
        }
        return conns;
    }

    private DcqlConstraint createDcqlConstraintForChildExpression(
                                                                  IExpressionId parentExpressionId,
                                                                  IExpressionId childExpressionId) {
        IExpression childExpr = getConstraints().getExpression(
                                                               childExpressionId);
        DcqlConstraint childExprDcqlConstraint;

        if (getResult().containsExpression(childExpr)) {
            childExprDcqlConstraint = getResult().getConstraintForExpression(
                                                                             childExpr);
        } else {
            childExprDcqlConstraint = createDcqlConstraintForExpression(childExpr);
        }

        IAssociation association = getJoinGraph().getAssociation(
                                                                 parentExpressionId,
                                                                 childExpressionId);

        AbstractAssociationConstraint dcqlConstraint = createAssociation(
                                                                         association,
                                                                         false);
        dcqlConstraint.addChildConstraint(childExprDcqlConstraint);
        return dcqlConstraint;
    }

    private AbstractAssociationConstraint createAssociation(
                                                            IAssociation association,
                                                            boolean reverseDir) {

        if (reverseDir && !isAssociationBidirectional(association)) {
            throw new IllegalArgumentException(
                    "Cannot do reverseDirection if association ain't bidirectional...");
        }
        AbstractAssociationConstraint dcqlConstraint;

        if (association instanceof IIntraModelAssociation) {
            dcqlConstraint = createLocalAssociation(
                                                    (IIntraModelAssociation) association,
                                                    reverseDir);
        } else {
            dcqlConstraint = createForeignAssociation(
                                                      (IInterModelAssociation) association,
                                                      reverseDir);
        }
        return dcqlConstraint;
    }

    private boolean isAssociationBidirectional(IAssociation association) {
        // if (association instanceof IIntraModelAssociation) {
        // return ((IIntraModelAssociation)
        // association).getDynamicExtensionsAssociation().getAssociationDirection()
        // == AssociationDirection.BI_DIRECTIONAL;
        // } else {
        // return true;
        // }
        return association.isBidirectional();
    }

    private LocalAssociationConstraint createLocalAssociation(
                                                              IIntraModelAssociation intraModelAssociation,
                                                              boolean reverseDir) {
        Association association = new Association();

        AssociationInterface deAssociation = intraModelAssociation.getDynamicExtensionsAssociation();
        if (reverseDir) {
            association.setRoleName(deAssociation.getSourceRole().getName());
            association.setName(deAssociation.getEntity().getName());
        } else {
            association.setRoleName(deAssociation.getTargetRole().getName());
            association.setName(deAssociation.getTargetEntity().getName());
        }

        return new LocalAssociationConstraint(association);
    }

    private ForeignAssociationConstraint createForeignAssociation(
                                                                  IInterModelAssociation interModelAssociation,
                                                                  boolean reverseDir) {
        // 1. create foreign object
        // TODO multiple urls...
        String leftServiceUrl = interModelAssociation.getSourceServiceUrl();
        String rightServiceUrl = interModelAssociation.getTargetServiceUrl();

        Object foreignObject = new Object();
        ForeignAssociation association = new ForeignAssociation();
        JoinCondition joinCondition = new JoinCondition();
        joinCondition.setPredicate(ForeignPredicate.EQUAL_TO);
        if (reverseDir) {
            foreignObject.setName(interModelAssociation.getSourceAttribute().getEntity().getName());
            association.setTargetServiceURL(leftServiceUrl);

            // 2. create join condition
            joinCondition.setLocalAttributeName(interModelAssociation.getTargetAttribute().getName());
            joinCondition.setForeignAttributeName(interModelAssociation.getSourceAttribute().getName());
            association.setForeignObject(foreignObject);
            association.setJoinCondition(joinCondition);

        } else {
            foreignObject.setName(interModelAssociation.getTargetEntity().getName());
            association.setTargetServiceURL(rightServiceUrl);

            // 2. create join condition
            // TODO check new dcql jar.
            joinCondition.setLocalAttributeName(interModelAssociation.getSourceAttribute().getName());
            joinCondition.setForeignAttributeName(interModelAssociation.getTargetAttribute().getName());
        }
        association.setForeignObject(foreignObject);
        association.setJoinCondition(joinCondition);
        return new ForeignAssociationConstraint(association);
    }

    private DcqlConstraint createDcqlConstraintForRule(IRule rule) {
        // DcqlConstraint dcqlConstraint;
        // if (rule.size() == 1) {
        // return new AttributeConstraint(
        // createAttribute(rule.getCondition(0)));
        // }
        MyGroup group = new MyGroup(LogicalOperator.And);

        // List<Attribute> attributesList = new ArrayList<Attribute>();
        for (int i = 0; i < rule.size(); i++) {
            group.addConstraint((createConstraintForCondition(rule.getCondition(i))));
        }
        return group.getDcqlConstraint();
    }

    private AttributeConstraint createAttribute(String attributeName,
                                                RelationalOperator operator,
                                                DataType dataType) {
        return createAttribute(attributeName, operator, null, dataType);
    }

    private AttributeConstraint createAttribute(String attributeName,
                                                RelationalOperator operator,
                                                String value, DataType dataType) {
        Attribute attribute = new Attribute();
        attribute.setName(attributeName);
        attribute.setPredicate(TransformerUtil.getCqlPredicate(operator));
        if (value != null) {
            attribute.setValue(modifyValue(value, operator, dataType));
        }
        return new AttributeConstraint(attribute);
    }

    private String modifyDateValue(String value) {
        // TODO this doesn't work... may need to follow up with Scott/ later
        // cagrid releases.
        // return DateUtils.getDateString(Date.valueOf(value));
        return value;
    }

    private String modifyValue(String value, RelationalOperator operator,
                               DataType dataType) {
        if (dataType == DataType.Date) {
            return modifyDateValue(value);
        }
        if (operator == RelationalOperator.StartsWith) {
            return value + DCQL_WILDCARD;
        }
        if (operator == RelationalOperator.EndsWith) {
            return DCQL_WILDCARD + value;
        }
        if (operator == RelationalOperator.Contains) {
            return DCQL_WILDCARD + value + DCQL_WILDCARD;
        }
        return value;
    }

    private DcqlConstraint createBetweenCondition(String attributeName,
                                                  String value1, String value2,
                                                  DataType dataType) {
        MyGroup group = new MyGroup(LogicalOperator.And);
        group.addConstraint((createAttribute(
                                             attributeName,
                                             RelationalOperator.GreaterThanOrEquals,
                                             value1, dataType)));
        group.addConstraint((createAttribute(
                                             attributeName,
                                             RelationalOperator.LessThanOrEquals,
                                             value2, dataType)));
        return group.getDcqlConstraint();
    }

    private DcqlConstraint createInCondition(String attributeName,
                                             List<String> values,
                                             DataType dataType) {
        MyGroup group = new MyGroup(LogicalOperator.Or);
        for (String value : values) {
            group.addConstraint((createAttribute(attributeName,
                                                 RelationalOperator.Equals,
                                                 value, dataType)));
        }
        return group.getDcqlConstraint();
    }

    private DcqlConstraint createNotInCondition(String attributeName,
                                                List<String> values,
                                                DataType dataType) {
        MyGroup group = new MyGroup(LogicalOperator.And);
        for (String value : values) {
            group.addConstraint((createAttribute(attributeName,
                                                 RelationalOperator.NotEquals,
                                                 value, dataType)));
        }
        return group.getDcqlConstraint();
    }

    private DcqlConstraint createConstraintForCondition(ICondition condition) {

        // TODO IN operator etc...

        // TODO func class etc...

        String attributeName = condition.getAttribute().getName();
        RelationalOperator operator = condition.getRelationalOperator();
        DataType dataType = Utility.getDataType(condition.getAttribute().getAttributeTypeInformation());

        if (operator.numberOfValuesRequired() == 0)
            return createAttribute(attributeName, operator, dataType);
        if (operator.numberOfValuesRequired() == 1) {
            return createAttribute(attributeName, operator,
                                   condition.getValue(), dataType);
        }

        if (condition.getRelationalOperator() == RelationalOperator.Between) {
            return createBetweenCondition(attributeName,
                                          condition.getValues().get(0),
                                          condition.getValues().get(1),
                                          dataType);
        }

        if (condition.getRelationalOperator() == RelationalOperator.In) {
            return createInCondition(attributeName, condition.getValues(),
                                     dataType);

        }
        if (condition.getRelationalOperator() == RelationalOperator.NotIn) {
            return createNotInCondition(attributeName, condition.getValues(),
                                        dataType);

        }
        // will never occur.
        return null;
    }

    /**
     * @return the result.
     */
    private ConstraintsBuilderResult getResult() {
        return result;
    }

    /**
     * @param result
     *            the result to set.
     */
    private void setResult(ConstraintsBuilderResult result) {
        this.result = result;
    }

    public IConstraints getConstraints() {
        return constraints;
    }

    public void setConstraints(IConstraints constraints) {
        this.constraints = constraints;
    }

    private IJoinGraph getJoinGraph() {
        return getConstraints().getJoinGraph();
    }

    private IExpression getExpression(IExpressionId expressionId) {
        return getConstraints().getExpression(expressionId);
    }
}

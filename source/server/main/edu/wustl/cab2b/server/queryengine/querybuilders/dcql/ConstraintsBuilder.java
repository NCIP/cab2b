package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.queryengine.querybuilders.CategoryPreprocessorResult;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AbstractAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AttributeConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.ForeignAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.LocalAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.utils.TransformerUtil;
import edu.wustl.cab2b.server.queryengine.utils.TreeNode;
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
import edu.wustl.common.querysuite.queryobject.IConnector;
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
public class ConstraintsBuilder {

    private ConstraintsBuilderResult result;

    private ICab2bQuery query;

    private CategoryPreprocessorResult categoryPreprocessorResult;

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

    public ConstraintsBuilder(ICab2bQuery query, CategoryPreprocessorResult categoryPreprocessorResult) {
        setQuery(query);
        setCategoryPreprocessorResult(categoryPreprocessorResult);
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
    private Set<IExpressionId> getCategoryOutputExpressionIds() {
        Set<TreeNode<IExpression>> outputExpressions = getCategoryPreprocessorResult().getExprsSourcedFromCategories().get(
                                                                                                                           getQuery().getOutputEntity());
        Set<IExpressionId> outputExpressionIds = new HashSet<IExpressionId>(outputExpressions.size());
        for (TreeNode<IExpression> exprNode : outputExpressions) {
            outputExpressionIds.add(exprNode.getValue().getExpressionId());
        }
        return outputExpressionIds;
    }

    private EntityInterface getOutputEntity() {
        return getQuery().getOutputEntity();
    }

    private void constrainByParentExpressions() {
        boolean isCategoryOutput = Utility.isCategory(getOutputEntity());
        Set<IExpressionId> outputExprIds = null;
        if (isCategoryOutput) {
            outputExprIds = getCategoryOutputExpressionIds();
        }
        // Set<IExpressionId> processedExpressions = new
        // HashSet<IExpressionId>();
        Set<IExpressionId> currExprIds = new HashSet<IExpressionId>();
        currExprIds.add(getRootExpr().getExpressionId());

        while (!currExprIds.isEmpty()) {
            Set<IExpressionId> nextExprIds = new HashSet<IExpressionId>();
            for (IExpressionId currExprId : currExprIds) {
                // TODO why is this needed??
                // if (processedExpressions.contains(currExprId)) {
                // continue;
                // }
                IExpression currExpr = getExpression(currExprId);
                List<IExpressionId> parentExprIds = getJoinGraph().getParentList(currExprId);

                // assert processedExpressions.containsAll(parentExprIds);
                constrainChildByParents(currExprId, parentExprIds);

                // do "constrain by parents" only till the output expressions.
                boolean isOutputExpr = (isCategoryOutput && outputExprIds.contains(currExprId))
                        || currExpr.getQueryEntity().equals(getOutputEntity());
                if (!isOutputExpr) {
                    nextExprIds.addAll(getJoinGraph().getChildrenList(currExprId));
                }
                // processedExpressions.add(currExprId);
            }
            currExprIds = nextExprIds;
        }
        // assert
        // processedExpressions.equals(getResult().getExpressionToConstraintMap().keySet());
    }

    private void constrainChildByParents(IExpressionId childExprId, List<IExpressionId> parentExprIds) {
        IExpression childExpr = getExpression(childExprId);
        DcqlConstraint childConstraint = getResult().getConstraintForExpression(childExpr);

        Cab2bGroup group = new Cab2bGroup(LogicalOperator.And);
        group.addConstraint(childConstraint);

        for (IExpressionId parentExprId : parentExprIds) {
            IExpression parentExpr = getExpression(parentExprId);
            IAssociation association = getJoinGraph().getAssociation(parentExprId, childExprId);
            if (!association.isBidirectional()) {
                Logger.out.warn("Unidirectional association found " + association
                        + ". Results could be incorrect.");
                continue;
            }
            DcqlConstraint parentConstraint = getResult().getConstraintForExpression(parentExpr);
            AbstractAssociationConstraint associationConstraint = createAssociation(association.reverse());
            associationConstraint.addChildConstraint(parentConstraint);
            group.addConstraint(associationConstraint);
        }
        getResult().putConstraintForExpression(childExpr, group.getDcqlConstraint(), true);
    }

    // END CONSTRAINING EACH EXPR BY PARENTS

    private IExpression findRootExpression() {
        try {
            IExpressionId rootExpressionId = getConstraints().getRootExpressionId();
            return getConstraints().getExpression(rootExpressionId);
        } catch (MultipleRootsException e) {
            String msg = "Invalid query object submitted; it's got multiple roots...";
            Logger.out.error(msg);
            throw new RuntimeException(msg, e, ErrorCodeConstants.QUERY_INVALID_INPUT);
        }

    }

    private boolean isExprRedundant(IExpressionId exprId) {
        return getCategoryPreprocessorResult().getRedundantExprs().contains(getExpression(exprId));
    }

    private DcqlConstraint createDcqlConstraintForExpression(IExpression expr) {
        List<DcqlConstraint> dcqlConstraintsList = new ArrayList<DcqlConstraint>(expr.numberOfOperands());

        for (int i = 0; i < expr.numberOfOperands(); i++) {
            IExpressionOperand operand = expr.getOperand(i);
            if (operand instanceof IExpressionId) {
                IExpressionId exprId = (IExpressionId) operand;
                // if (!isExprRedundant(exprId)) {
                dcqlConstraintsList.add(createDcqlConstraintForChildExpression(expr.getExpressionId(), exprId));
                // }
            } else if(operand instanceof IRule){
                dcqlConstraintsList.add(createDcqlConstraintForRule((IRule) operand));
            } else {
                throw new RuntimeException("uknown operand type.");
            }
        }

        DcqlConstraint dcqlConstraint = mergeConstraints(expr, dcqlConstraintsList);

        getResult().putConstraintForExpression(expr, dcqlConstraint, !constrainByParentExpressions);
        return dcqlConstraint;
    }

    private DcqlConstraint mergeConstraints(IExpression expr, List<DcqlConstraint> dcqlConstraintsList) {
        if (dcqlConstraintsList.size() == 0) {
            return new DcqlConstraint();
        }
        if (dcqlConstraintsList.size() == 1) {
            return dcqlConstraintsList.get(0);
        }
        return new GroupBuilder(getConnectors(expr), dcqlConstraintsList).buildGroup();
    }

    private List<IConnector<LogicalOperator>> getConnectors(IExpression expression) {
        List<IConnector<LogicalOperator>> conns = new ArrayList<IConnector<LogicalOperator>>();
        for (int i = 0; i < expression.numberOfOperands() - 1; i++) {
            conns.add(expression.getConnector(i, i + 1));
        }
        return conns;
    }

    private DcqlConstraint createDcqlConstraintForChildExpression(IExpressionId parentExpressionId,
                                                                  IExpressionId childExpressionId) {
        IExpression childExpr = getConstraints().getExpression(childExpressionId);
        DcqlConstraint childExprDcqlConstraint;

        if (getResult().containsExpression(childExpr)) {
            childExprDcqlConstraint = getResult().getConstraintForExpression(childExpr);
        } else {
            childExprDcqlConstraint = createDcqlConstraintForExpression(childExpr);
        }

        DcqlConstraint dcqlConstraint;
        if (isExprRedundant(childExpressionId)) {
            dcqlConstraint = createAnyConstraint();
        } else {
            IAssociation association = getJoinGraph().getAssociation(parentExpressionId, childExpressionId);

            AbstractAssociationConstraint associationConstraint = createAssociation(association);
            associationConstraint.addChildConstraint(childExprDcqlConstraint);

            dcqlConstraint = associationConstraint;
        }
        return dcqlConstraint;
    }

    private DcqlConstraint createAnyConstraint() {
        return new DcqlConstraint();
    }

    public static AbstractAssociationConstraint createAssociation(IAssociation association) {

        AbstractAssociationConstraint dcqlConstraint = null;

        if (association instanceof IIntraModelAssociation) {
            dcqlConstraint = createLocalAssociation((IIntraModelAssociation) association);
        } else if (association instanceof IInterModelAssociation) {
            dcqlConstraint = createForeignAssociation((IInterModelAssociation) association);
        }
        return dcqlConstraint;
    }

    private static LocalAssociationConstraint createLocalAssociation(IIntraModelAssociation intraModelAssociation) {
        Association association = new Association();

        AssociationInterface deAssociation = intraModelAssociation.getDynamicExtensionsAssociation();
        association.setRoleName(deAssociation.getTargetRole().getName());
        association.setName(deAssociation.getTargetEntity().getName());

        return new LocalAssociationConstraint(association);
    }

    private static ForeignAssociationConstraint createForeignAssociation(
                                                                         IInterModelAssociation interModelAssociation) {
        // 1. create foreign object
        // TODO multiple urls...
        // String leftServiceUrl = interModelAssociation.getSourceServiceUrl();
        String rightServiceUrl = interModelAssociation.getTargetServiceUrl();

        Object foreignObject = new Object();
        ForeignAssociation association = new ForeignAssociation();
        JoinCondition joinCondition = new JoinCondition();
        joinCondition.setPredicate(ForeignPredicate.EQUAL_TO);
        foreignObject.setName(interModelAssociation.getTargetEntity().getName());
        association.setTargetServiceURL(rightServiceUrl);

        // 2. create join condition

        joinCondition.setLocalAttributeName(interModelAssociation.getSourceAttribute().getName());
        joinCondition.setForeignAttributeName(interModelAssociation.getTargetAttribute().getName());
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
        Cab2bGroup group = new Cab2bGroup(LogicalOperator.And);

        // List<Attribute> attributesList = new ArrayList<Attribute>();
        for (int i = 0; i < rule.size(); i++) {
            group.addConstraint((createConstraintForCondition(rule.getCondition(i))));
        }
        return group.getDcqlConstraint();
    }

    private AttributeConstraint createAttribute(String attributeName, RelationalOperator operator,
                                                DataType dataType) {
        return createAttributeConstraint(attributeName, operator, null, dataType);
    }

    public static AttributeConstraint createAttributeConstraint(String attributeName, RelationalOperator operator,
                                                                String value, DataType dataType) {
        Attribute attribute = new Attribute();
        attribute.setName(attributeName);
        attribute.setPredicate(TransformerUtil.getCqlPredicate(operator));
        if (value != null) {
            attribute.setValue(modifyValue(value, operator, dataType));
        }
        return new AttributeConstraint(attribute);
    }

    private static String modifyDateValue(String value) {
        // TODO this doesn't work... may need to follow up with Scott/ later
        // cagrid releases.
        // return DateUtils.getDateString(Date.valueOf(value));
        return value;
    }

    private static String modifyValue(String value, RelationalOperator operator, DataType dataType) {
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

    private DcqlConstraint createBetweenCondition(String attributeName, String value1, String value2, DataType dataType) {
        Cab2bGroup group = new Cab2bGroup(LogicalOperator.And);
        boolean swapVals;
        switch (dataType) {
            case Integer:
            case Long: {
                long long1 = Long.parseLong(value1);
                long long2 = Long.parseLong(value2);
                swapVals = long1 > long2;
                break;
            }
            case Float:
            case Double: {
                double d1 = Double.parseDouble(value1);
                double d2 = Double.parseDouble(value2);
                swapVals = d1 > d2;
                break;
            }
            case Date: {
                Date d1 = Date.valueOf(value1);
                Date d2 = Date.valueOf(value2);
                swapVals = d1.compareTo(d2) > 0;
                break;
            }
            default:
                throw new RuntimeException("Invalid datatype for Between operator.");
        }
        if (swapVals) {
            String temp = value1;
            value1 = value2;
            value2 = temp;
        }
        group.addConstraint((createAttributeConstraint(attributeName, RelationalOperator.GreaterThanOrEquals, value1, dataType)));
        group.addConstraint((createAttributeConstraint(attributeName, RelationalOperator.LessThanOrEquals, value2, dataType)));
        return group.getDcqlConstraint();
    }

    private DcqlConstraint createInCondition(String attributeName, List<String> values, DataType dataType) {
        Cab2bGroup group = new Cab2bGroup(LogicalOperator.Or);
        for (String value : values) {
            group.addConstraint((createAttributeConstraint(attributeName, RelationalOperator.Equals, value,
                                                           dataType)));
        }
        return group.getDcqlConstraint();
    }

    private DcqlConstraint createNotInCondition(String attributeName, List<String> values, DataType dataType) {
        Cab2bGroup group = new Cab2bGroup(LogicalOperator.And);
        for (String value : values) {
            group.addConstraint((createAttributeConstraint(attributeName, RelationalOperator.NotEquals, value,
                                                           dataType)));
        }
        return group.getDcqlConstraint();
    }

    private DcqlConstraint createConstraintForCondition(ICondition condition) {
        String attributeName = condition.getAttribute().getName();
        RelationalOperator operator = condition.getRelationalOperator();
        DataType dataType = Utility.getDataType(condition.getAttribute().getAttributeTypeInformation());

        if (operator.numberOfValuesRequired() == 0)
            return createAttribute(attributeName, operator, dataType);
        if (operator.numberOfValuesRequired() == 1) {
            return createAttributeConstraint(attributeName, operator, condition.getValue(), dataType);
        }

        if (condition.getRelationalOperator() == RelationalOperator.Between) {
            return createBetweenCondition(attributeName, condition.getValues().get(0),
                                          condition.getValues().get(1), dataType);
        }

        if (condition.getRelationalOperator() == RelationalOperator.In) {
            return createInCondition(attributeName, condition.getValues(), dataType);

        }
        if (condition.getRelationalOperator() == RelationalOperator.NotIn) {
            return createNotInCondition(attributeName, condition.getValues(), dataType);

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
        return getQuery().getConstraints();
    }

    private IJoinGraph getJoinGraph() {
        return getConstraints().getJoinGraph();
    }

    private IExpression getExpression(IExpressionId expressionId) {
        return getConstraints().getExpression(expressionId);
    }

    public CategoryPreprocessorResult getCategoryPreprocessorResult() {
        return categoryPreprocessorResult;
    }

    public void setCategoryPreprocessorResult(CategoryPreprocessorResult categoryPreprocessorResult) {
        this.categoryPreprocessorResult = categoryPreprocessorResult;
    }

    public ICab2bQuery getQuery() {
        return query;
    }

    public void setQuery(ICab2bQuery query) {
        this.query = query;
    }
}

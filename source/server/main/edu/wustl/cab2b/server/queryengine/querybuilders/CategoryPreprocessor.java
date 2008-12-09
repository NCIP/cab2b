package edu.wustl.cab2b.server.queryengine.querybuilders;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.TreeNode;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

/**
 * Category Preprocessor
 * @author Srinath K
 */
public class CategoryPreprocessor {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(CategoryPreprocessor.class);

    private IQuery query;

    private CategoryPreprocessorResult categoryPreprocessorResult;

    private List<IExpression> catExprs = new ArrayList<IExpression>();

    /**
     * The query object is modified appropriately.
     * @param query
     * @return Results from category preprocessor
     * 
     */
    public CategoryPreprocessorResult processCategories(IQuery query) {
        clear();
        this.query = query;
        this.categoryPreprocessorResult = new CategoryPreprocessorResult();
        processCategories();
        return this.categoryPreprocessorResult;
    }

    private void clear() {
        this.catExprs.clear();
    }

    private void processCategories() {

        for (IExpression expr : getConstraints()) {
            if (!Utility.isCategory(expr.getQueryEntity().getDynamicExtensionsEntity())) {
                continue;
            }
            processCategoryExpression(expr);
            this.catExprs.add(expr);
        }
        removeCategoryExpressions();
        // check there is a single root.
        try {
            getConstraints().getRootExpression();
        } catch (MultipleRootsException e) {
            throw new RuntimeException("Problem in code...", e);
        }
    }

    private void removeCategoryExpressions() {
        for (IExpression expression : this.catExprs) {
            // by this time, none of the cat expressions should have any
            // parents...
            // assert getJoinGraph().getParentList(expressionId).isEmpty();
            getConstraints().removeExpression(expression);
        }
    }

    private void processCategoryExpression(IExpression expr) {
        List<IExpression> parentExprs = getJoinGraph().getParentList(expr);
        if (parentExprs.isEmpty()) {
            // implies that this is the root expr.

            // transformCategoryExpression(expr, this.masterEntryPoint);

            EntityInterface catEntity = expr.getQueryEntity().getDynamicExtensionsEntity();
            Category category = getCategoryFromEntity(catEntity);
            transformCategoryExpression(expr, category.getRootClass().getCategorialClassEntity());
        } else {
            for (IExpression parentExpr : parentExprs) {
                if (this.catExprs.contains(parentExpr)) {
                    // this parent will be removed in the future; so don't heed
                    // it.
                    continue;
                }
                IAssociation association = getJoinGraph().getAssociation(parentExpr, expr);
                IExpression transformedExpr = transformCategoryExpression(expr, association.getTargetEntity());

                int index = parentExpr.indexOfOperand(expr);
                parentExpr.setOperand(index, transformedExpr);
                try {
                    getJoinGraph().putAssociation(parentExpr, transformedExpr, association);
                } catch (CyclicException e) {
                    // this should not occur...
                    throw new RuntimeException("Problem in code... might be due to invalid input.", e);
                }
                getJoinGraph().removeAssociation(parentExpr, expr);
            }
        }
    }

    // protected for testing
    protected Category getCategoryFromEntity(EntityInterface catEntity) {
        Long entityId = catEntity.getId();
        return CategoryCache.getInstance().getCategoryByEntityId(entityId);
    }

    private Category pivot(Category category, EntityInterface requiredRoot) {
        Category clonedCat = cloneCategorialClasses(category);
        clonedCat.setCategoryEntity(category.getCategoryEntity());

        getResult().getOriginallyRootCatClasses().add(clonedCat.getRootClass());

        List<CategorialClass> catClassesPath = findCategorialClassesPathFromRoot(clonedCat.getRootClass(),
                                                                                 requiredRoot);
        if (catClassesPath.isEmpty()) {
            throw new RuntimeException("Problem in code; could not find entry point in category");
            // TODO this is a hack for single outputs.... ????
            // return clonedCat;
        }
        Collections.reverse(catClassesPath);
        catClassesPath.get(0).setParent(null);

        List<IPath> correctedPaths = new ArrayList<IPath>(catClassesPath.size() - 1);

        for (int i = 0; i < catClassesPath.size() - 1; i++) {
            CategorialClass currCatClass = catClassesPath.get(i);
            CategorialClass nextCatClass = catClassesPath.get(i + 1);
            IPath origPath = currCatClass.getPathFromParent();
            if (!origPath.isBidirectional()) {
                logger.warn("Unidirectional path found " + origPath + " in category " + category
                        + ". Results could be incorrect.");
                break;
            }
            nextCatClass.removeChildCategorialClass(currCatClass);
            currCatClass.addChildCategorialClass(nextCatClass);
            correctedPaths.add(origPath.reverse());
        }
        clonedCat.setRootClass(catClassesPath.get(0));

        catClassesPath.get(0).setPathFromParent(null);
        for (int i = 1; i < catClassesPath.size(); i++) {
            catClassesPath.get(i).setPathFromParent(correctedPaths.get(i - 1));
        }
        return clonedCat;
    }

    /**
     * Creates a new category with a deep clone of the categorial classes' tree
     * (and categorial attributes). DE entities and paths are not cloned. Info
     * regarding subcategories is not copied.
     * 
     * @return the cloned category.
     */
    private Category cloneCategorialClasses(Category category) {
        Category clone = new Category();
        CategorialClass clonedRootCatClass = cloneCategorialClass(category.getRootClass());
        clone.setRootClass(clonedRootCatClass);
        clonedRootCatClass.setCategory(clone);
        clonedRootCatClass.setParent(null);

        List<CategorialClass> currOrigCatClasses = new ArrayList<CategorialClass>();
        currOrigCatClasses.add(category.getRootClass());
        List<CategorialClass> currClonedCatClasses = new ArrayList<CategorialClass>();
        currClonedCatClasses.add(clone.getRootClass());

        while (!currOrigCatClasses.isEmpty()) {
            List<CategorialClass> nextOrigCatClasses = new ArrayList<CategorialClass>();
            List<CategorialClass> nextClonedCatClasses = new ArrayList<CategorialClass>();

            for (int i = 0; i < currOrigCatClasses.size(); i++) {
                CategorialClass currOrigCatClass = currOrigCatClasses.get(i);
                CategorialClass currClonedCatClass = currClonedCatClasses.get(i);

                for (CategorialClass origChildCatClass : currOrigCatClass.getChildren()) {
                    CategorialClass clonedChildCatClass = cloneCategorialClass(origChildCatClass);
                    clonedChildCatClass.setCategory(clone);
                    currClonedCatClass.addChildCategorialClass(clonedChildCatClass,
                                                               origChildCatClass.getPathFromParent());

                    nextOrigCatClasses.add(origChildCatClass);
                    nextClonedCatClasses.add(clonedChildCatClass);
                }
            }
            currOrigCatClasses = nextOrigCatClasses;
            currClonedCatClasses = nextClonedCatClasses;
        }

        return clone;
    }

    // does not set parentCatClass and category.
    // does not set path.
    private CategorialClass cloneCategorialClass(CategorialClass categorialClassToClone) {
        CategorialClass clone = new CategorialClass();
        clone.setDeEntityId(categorialClassToClone.getDeEntityId());
        clone.setId(categorialClassToClone.getId());
        // clone.setPathFromParent(categorialClassToClone.getPathFromParent());
        clone.setCategorialClassEntity(categorialClassToClone.getCategorialClassEntity());
        for (CategorialAttribute origAttr : categorialClassToClone.getCategorialAttributeCollection()) {
            clone.addCategorialAttribute(cloneCategorialAttribute(origAttr));
        }
        return clone;
    }

    // does not set containingCatClass
    private CategorialAttribute cloneCategorialAttribute(CategorialAttribute categorialAttributeToClone) {
        CategorialAttribute clone = new CategorialAttribute();
        clone.setDeCategoryAttributeId(categorialAttributeToClone.getDeCategoryAttributeId());
        clone.setDeSourceClassAttributeId(categorialAttributeToClone.getDeSourceClassAttributeId());
        clone.setCategoryAttribute(categorialAttributeToClone.getCategoryAttribute());
        clone.setSourceClassAttribute(categorialAttributeToClone.getSourceClassAttribute());
        clone.setId(categorialAttributeToClone.getId());
        return clone;
    }

    protected boolean areEntitiesEqual(EntityInterface entity1, EntityInterface entity2) {
        return entity1.getId().equals(entity2.getId());
    }

    private List<CategorialClass> findCategorialClassesPathFromRoot(CategorialClass rootCatClass,
                                                                    EntityInterface requiredRoot) {
        List<CategorialClass> res = new ArrayList<CategorialClass>();

        if (areEntitiesEqual(rootCatClass.getCategorialClassEntity(), requiredRoot)) {
            res.add(rootCatClass);
            return res;
        }
        for (CategorialClass childCatClass : rootCatClass.getChildren()) {
            List<CategorialClass> pathFromChild = findCategorialClassesPathFromRoot(childCatClass, requiredRoot);
            if (!pathFromChild.isEmpty()) {
                res.add(rootCatClass);
                res.addAll(pathFromChild);
                // TODO taking first plausible exit point as THE exit point...
                // calling code should ensure (for jan release) that there is
                // only one plausible exit point.
                break;
            }
        }
        return res;
    }

    private IExpression transformCategoryExpression(IExpression catExpr, EntityInterface entryPoint) {
        EntityInterface catEntity = catExpr.getQueryEntity().getDynamicExtensionsEntity();
        Category originalCategory = getCategoryFromEntity(catEntity);
        getResult().getCategoryForEntity().put(catEntity, originalCategory);

        Category category = pivot(originalCategory, entryPoint);

        CategorialClass rootCatClass = category.getRootClass();
        IExpression rootExpr = createExpression(rootCatClass.getCategorialClassEntity(), catExpr.isInView());

        TreeNode<IExpression> rootExprNode = new TreeNode<IExpression>(rootExpr);
        getResult().addExprSourcedFromCategory(catEntity, rootExprNode);
        getResult().getCatClassForExpr().put(rootExpr, rootCatClass);
        if (catExpr.numberOfOperands() == 0) {
            markRedundant(rootExpr);
            return rootExpr;
        }

        // this map contains the appropriate position, in the new expr, of the
        // original connectors.
        Map<Integer, Integer> followingConnIndexOrigToNew = new HashMap<Integer, Integer>();

        for (int i = 0; i < catExpr.numberOfOperands(); i++) {
            int initialRootExprSize = rootExpr.numberOfOperands();
            IExpressionOperand operand = catExpr.getOperand(i);

            if (operand instanceof IExpression) {

                // find path to exit point
                IExpression externalExpr = (IExpression) operand;

                IAssociation exitAssociation = getJoinGraph().getAssociation(catExpr, externalExpr);
                EntityInterface exitPoint = exitAssociation.getSourceEntity();

                List<CategorialClass> catClassesPath = findCategorialClassesPathFromRoot(rootCatClass, exitPoint);

                IExpression lastExpr = rootExpr;
                TreeNode<IExpression> lastExprNode = rootExprNode;
                for (int j = 1; j < catClassesPath.size(); j++) {
                    CategorialClass catClass = catClassesPath.get(j);
                    List<IAssociation> associations = catClass.getPathFromParent().getIntermediateAssociations();
                    TreeNode<IExpression> newExprNode = createExpressionsForAssociations(
                                                                                         lastExpr,
                                                                                         associations,
                                                                                         new HashSet<IExpression>(),
                                                                                         catExpr.isInView(),
                                                                                         lastExprNode);
                    IExpression newExpr = newExprNode.getValue();
                    getResult().getCatClassForExpr().put(newExpr, catClass);

                    lastExpr = newExpr;
                    lastExprNode = newExprNode;
                }
                // set external expr as subexpr for last expression...
                addSubExpr(lastExpr, externalExpr, exitAssociation);

            } else {
                // it is a rule

                // traverse category tree in BFS creating IExpression's along
                // the way.
                IRule rule = (IRule) operand;
                addRuleToExpr(rootExpr, gleanConditions(rootCatClass, rule));

                Map<CategorialClass, IExpression> exprForCatClass = new HashMap<CategorialClass, IExpression>();
                exprForCatClass.put(rootCatClass, rootExpr);

                Map<CategorialClass, TreeNode<IExpression>> treeNodeForCatClass = new HashMap<CategorialClass, TreeNode<IExpression>>();
                treeNodeForCatClass.put(rootCatClass, rootExprNode);

                Set<CategorialClass> currCatClasses = new HashSet<CategorialClass>();
                currCatClasses.addAll(rootCatClass.getChildren());

                Set<IExpression> possiblyRedundantExprs = new HashSet<IExpression>();
                // BFS starts from one level below the root...
                while (!currCatClasses.isEmpty()) {
                    Set<CategorialClass> nextCatClasses = new HashSet<CategorialClass>();
                    for (CategorialClass categorialClass : currCatClasses) {
                        nextCatClasses.addAll(categorialClass.getChildren());

                        CategorialClass parentCatClass = categorialClass.getParent();
                        TreeNode<IExpression> newExprNode = createExpressionsForAssociations(
                                                                                             exprForCatClass.get(parentCatClass),
                                                                                             categorialClass.getPathFromParent().getIntermediateAssociations(),
                                                                                             possiblyRedundantExprs,
                                                                                             catExpr.isInView(),
                                                                                             treeNodeForCatClass.get(parentCatClass));

                        IExpression newExpr = newExprNode.getValue();
                        addRuleToExpr(newExpr, gleanConditions(categorialClass, rule));
                        exprForCatClass.put(categorialClass, newExpr);
                        treeNodeForCatClass.put(categorialClass, newExprNode);
                        getResult().getCatClassForExpr().put(newExpr, categorialClass);
                    }
                    currCatClasses = nextCatClasses;
                }
                for (IExpression possiblyRedundantExpr : possiblyRedundantExprs) {
                    processRedundantExprs(possiblyRedundantExpr, possiblyRedundantExprs);
                }
            }
            if (i < catExpr.numberOfOperands() - 1) {
                followingConnIndexOrigToNew.put(i, rootExpr.numberOfOperands() - 1);
            }
            int numOperandsAdded = rootExpr.numberOfOperands() - initialRootExprSize;
            if (numOperandsAdded > 1) {
                // add parantheses around the operands created. The connectors
                // were created with default nesting (0). The no. of parantheses
                // to be added thus equals the no. of parantheses around the
                // original operand + 1.
                int precedingConnNesting = catExpr.getConnector(i - 1, i).getNestingNumber();
                int followingConnNesting = catExpr.getConnector(i, i + 1).getNestingNumber();
                // the nesting of operand equals the nesting of the adjacent
                // connector with greater nesting.
                int operandNesting = (precedingConnNesting > followingConnNesting) ? precedingConnNesting
                        : followingConnNesting;
                // add operandNesting + 1 parantheses.
                for (int j = 0; j <= operandNesting; j++) {
                    rootExpr.addParantheses(initialRootExprSize, initialRootExprSize + numOperandsAdded - 1);
                }
            }
        }

        // place connectors in original expression in appropriate positions in
        // new expression.
        for (Map.Entry<Integer, Integer> entry : followingConnIndexOrigToNew.entrySet()) {
            IConnector<LogicalOperator> connector = catExpr.getConnector(entry.getKey(), entry.getKey() + 1);
            rootExpr.setConnector(entry.getValue(), entry.getValue() + 1, connector);
        }
        return rootExpr;
    }

    private void processRedundantExprs(IExpression possiblyRedundantExpr, Set<IExpression> possiblyRedundantExprs) {
        if (isRedundant(possiblyRedundantExpr)) {
            // the expr was already found to be redundant.
            return;
        }

        for (IExpression possiblyRedundantChildExprId : getJoinGraph().getChildrenList(possiblyRedundantExpr)) {
            processRedundantExprs(possiblyRedundantChildExprId, possiblyRedundantExprs);
        }
        if (possiblyRedundantExprs.contains(possiblyRedundantExpr)) {
            List<IExpression> childExprs = getJoinGraph().getChildrenList(possiblyRedundantExpr);
            if (possiblyRedundantExpr.numberOfOperands() == childExprs.size()) {
                // this means that possiblyRedundantExpr has no rules
                boolean redundant = true;
                for (IExpression childExpr : childExprs) {
                    if (!isRedundant(childExpr)) {
                        redundant = false;
                        break;
                    }
                }
                if (redundant) {
                    // all child exprs are redundant, so this expr is redundant.
                    markRedundant(possiblyRedundantExpr);
                }
            }
            // }
        }
    }

    private void markRedundant(IExpression expr) {
        getResult().getRedundantExprs().add(expr);
    }

    private boolean isRedundant(IExpression expr) {
        return getResult().getRedundantExprs().contains(expr);
    }

    /**
     * Creates expressions corresponding to the associations; also sets the
     * subexpressions in parentExpr.
     * 
     * @param parentExpr the parentExpr that which is start-point for the
     *            expressions created.
     * @param associationsToLastChild the associations that lead from
     *            parentExpr's entity to the entity for which the expr is to be
     *            created.
     * @return the last expression.
     */
    private TreeNode<IExpression> createExpressionsForAssociations(IExpression parentExpr,
                                                                   List<IAssociation> associationsToLastChild,
                                                                   Set<IExpression> expressionsAdded,
                                                                   boolean inView,
                                                                   TreeNode<IExpression> parentExprNode) {
        IExpression lastChildExpr = parentExpr;
        TreeNode<IExpression> lastChildNode = parentExprNode;
        for (IAssociation association : associationsToLastChild) {
            parentExpr = lastChildExpr;
            parentExprNode = lastChildNode;
            lastChildExpr = createExpression(association.getTargetEntity(), inView);
            lastChildNode = new TreeNode<IExpression>(lastChildExpr);
            expressionsAdded.add(lastChildExpr);

            addSubExpr(parentExpr, lastChildExpr, association);
            parentExprNode.addChild(lastChildNode);
        }
        return lastChildNode;
    }

    private IRule gleanConditions(CategorialClass categorialClass, IRule rule) {
        IRule newRule = QueryObjectFactory.createRule();
        for (int i = 0; i < rule.size(); i++) {
            ICondition condition = rule.getCondition(i);
            AttributeInterface catAttr = condition.getAttribute();
            AttributeInterface srcAttr = findSourceAttribute(catAttr, categorialClass);
            if (srcAttr != null) {
                newRule.addCondition(cloneWithSpecifiedAttribute(condition, srcAttr));
            }
        }
        return newRule;
    }

    protected AttributeInterface findSourceAttribute(AttributeInterface catAttr, CategorialClass categorialClass) {
        AttributeInterface origAttr = categorialClass.findSourceAttribute(catAttr);
        return origAttr;
    }

    private ICondition cloneWithSpecifiedAttribute(ICondition origCondition, AttributeInterface attr) {
        ICondition newCondition = QueryObjectFactory.createCondition();
        newCondition.setValues(origCondition.getValues());
        newCondition.setRelationalOperator(origCondition.getRelationalOperator());
        newCondition.setAttribute(attr);
        return newCondition;
    }

    private void addRuleToExpr(IExpression expr, IRule rule) {
        if (rule.size() != 0) {
            addOperandToExpr(expr, rule);
        }
    }

    private void addSubExpr(IExpression parentExpr, IExpression childExpr, IAssociation association) {
        addOperandToExpr(parentExpr, childExpr);
        try {
            getJoinGraph().putAssociation(parentExpr, childExpr, association);
        } catch (CyclicException e) {
            // this should not occur...
            throw new RuntimeException("Problem in code...", e);
        }
    }

    private void addOperandToExpr(IExpression parentExpr, IExpressionOperand operand) {
        if (parentExpr.numberOfOperands() > 0) {
            parentExpr.addOperand(QueryObjectFactory.createLogicalConnector(LogicalOperator.And), operand);
        } else {
            parentExpr.addOperand(operand);
        }
    }

    private IExpression createExpression(EntityInterface entity, boolean inView) {
        IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(entity);
        IExpression expr = getConstraints().addExpression(queryEntity);
        expr.setInView(inView);
        return expr;
    }

    private IJoinGraph getJoinGraph() {
        return getConstraints().getJoinGraph();
    }

    private IConstraints getConstraints() {
        return query.getConstraints();
    }

    private CategoryPreprocessorResult getResult() {
        return this.categoryPreprocessorResult;
    }
}
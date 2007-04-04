package edu.wustl.cab2b.server.queryengine.querybuilders;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraintEntity;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.ILogicalConnector;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.util.logger.Logger;

public class CategoryPreprocessor {
    private IConstraints constraints;

    private EntityInterface masterEntryPoint;

    private Map<Long, Category> entityIdToCategory = new HashMap<Long, Category>();

    private static EntityCache cache = EntityCache.getInstance();
    
    private Connection connection;

    /**
     * The constraints object is modified appropriately.
     */
    // TODO this is plain pathetic... taking outputEntity in here. the whole
    // thing needs to be thought over for multiple outputs or when category is
    // in outputs. Lots of scenarios in here to think about.
    /**
     * @param constraints
     * @param masterEntryPoint
     *            this is considered as the entry point for the category which
     *            constrains the root expression.
     */
    public void processCategories(IConstraints constraints,
                                  EntityInterface masterEntryPoint, Connection connection) {
        this.constraints = constraints;
        this.masterEntryPoint = masterEntryPoint;
        this.connection = connection;
        processCategories();
    }

    private void processCategories() {
        Enumeration<IExpressionId> exprIds = constraints.getExpressionIds();
        List<IExpressionId> catExprIds = new ArrayList<IExpressionId>();
        while (exprIds.hasMoreElements()) {
            IExpressionId exprId = exprIds.nextElement();
            IExpression expr = constraints.getExpression(exprId);

            if (!Utility.isCategory(expr.getConstraintEntity().getDynamicExtensionsEntity())) {
                continue;
            }
            processCategoryExpression(expr);
            catExprIds.add(exprId);
        }
        removeCategoryExpressions(catExprIds);
        // check there is a single root.
        try {
            constraints.getRootExpressionId();
        } catch (MultipleRootsException e) {
            throw new RuntimeException("Problem in code...", e);
        }
    }

    private void removeCategoryExpressions(List<IExpressionId> catExprIds) {
        for (IExpressionId expressionId : catExprIds) {
            // by this time, none of the cat expressions should have any
            // parents...
            assert getJoinGraph().getParentList(expressionId).isEmpty();
            constraints.removeExpressionWithId(expressionId);
        }
    }

    private void processCategoryExpression(IExpression expr) {
        IExpressionId exprId = expr.getExpressionId();
        List<IExpressionId> parentExprIds = getJoinGraph().getParentList(exprId);
        if (parentExprIds.isEmpty()) {
            // implies that this is the root expr.
            // TODO since this is the root, treat output class as
            // entryPoint

            transformCategoryExpression(expr, this.masterEntryPoint);

            // below is okish code; doesn't work though.
            // EntityInterface catEntity =
            // expr.getConstraintEntity().getDynamicExtensionsEntity();
            // Category category = getCategoryFromEntity(catEntity);
            // transformCategoryExpression(
            // expr,
            // getEntityById(category.getRootClass().getDeEntityId()));

            // return;
        } else {
            for (IExpressionId parentExprId : parentExprIds) {
                IExpression parentExpr = constraints.getExpression(parentExprId);
                IAssociation association = getJoinGraph().getAssociation(
                                                                         parentExprId,
                                                                         exprId);
                IExpression transformedExpr = transformCategoryExpression(
                                                                          expr,
                                                                          association.getTargetEntity());

                int index = parentExpr.indexOfOperand(exprId);
                parentExpr.setOperand(index, transformedExpr.getExpressionId());
                try {
                    getJoinGraph().putAssociation(
                                                  parentExprId,
                                                  transformedExpr.getExpressionId(),
                                                  association);
                } catch (CyclicException e) {
                    // this should not occur...
                    throw new RuntimeException(
                            "Problem in code... might be due to invalid input.",
                            e);
                }
            }
        }
    }

    private Category getCategoryFromEntity(EntityInterface catEntity) {
        Long entityId = catEntity.getId();
        if (this.entityIdToCategory.containsKey(entityId)) {
            return this.entityIdToCategory.get(entityId);
        }
        return new CategoryOperations().getCategoryByEntityId(entityId, connection);
//        CategoryBusinessInterface catBus;
//        try {
//            catBus = (CategoryBusinessInterface) Locator.getInstance().locate(
//                                                                              EjbNamesConstants.CATEGORY_BEAN,
//                                                                              CategoryHomeInterface.class);
//            Category category = catBus.getCategoryByEntityId(catEntity.getId());
//            this.entityIdToCategory.put(entityId, category);
//            return category;
//        } catch (RemoteException e) {
//            throw new RuntimeException("Exception while fetching category.", e);
//        } catch (LocatorException e) {
//            throw new RuntimeException(
//                    "Unable to locate category bean in queryBuilder", e,
//                    ErrorCodeConstants.JN_0001);
//        }
    }

    private Category pivot(Category category, EntityInterface requiredRoot) {
        Category clonedCat = cloneCategorialClasses(category);
        List<CategorialClass> catClassesPath = findCategorialClassesPathFromRoot(
                                                                                 clonedCat.getRootClass(),
                                                                                 requiredRoot);
        if (catClassesPath.isEmpty()) {
            // throw new RuntimeException(
            // "Problem in code; could not find entry point in category");
            // TODO this is a hack for single outputs....
            return clonedCat;
        }
        Collections.reverse(catClassesPath);
        catClassesPath.get(0).setParent(null);

        List<IPath> correctedPaths = new ArrayList<IPath>(
                catClassesPath.size() - 1);

        for (int i = 0; i < catClassesPath.size() - 1; i++) {
            CategorialClass currCatClass = catClassesPath.get(i);
            CategorialClass nextCatClass = catClassesPath.get(i + 1);
            IPath origPath = currCatClass.getPathFromParent();
            if (!origPath.isBidirectional()) {
                Logger.out.warn("Unidirectional path found " + origPath
                        + " in category " + category
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
                    currClonedCatClass.addChildCategorialClass(
                                                               clonedChildCatClass,
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
    private CategorialClass cloneCategorialClass(
                                                 CategorialClass categorialClassToClone) {
        CategorialClass clone = new CategorialClass();
        clone.setDeEntityId(categorialClassToClone.getDeEntityId());
        // clone.setPathFromParent(categorialClassToClone.getPathFromParent());

        for (CategorialAttribute origAttr : categorialClassToClone.getCategorialAttributeCollection()) {
            clone.addCategorialAttribute(cloneCategorialAttribute(origAttr));
        }
        return clone;
    }

    // does not set containingCatClass
    private CategorialAttribute cloneCategorialAttribute(
                                                         CategorialAttribute categorialAttributeToClone) {
        CategorialAttribute clone = new CategorialAttribute();
        clone.setDeCategoryAttributeId(categorialAttributeToClone.getDeCategoryAttributeId());
        clone.setDeSourceClassAttributeId(categorialAttributeToClone.getDeSourceClassAttributeId());
        return clone;
    }

    private List<CategorialClass> findCategorialClassesPathFromRoot(
                                                                    CategorialClass rootCatClass,
                                                                    EntityInterface requiredRoot) {
        List<CategorialClass> res = new ArrayList<CategorialClass>();

        if (rootCatClass.getDeEntityId().equals(requiredRoot.getId())) {
            res.add(rootCatClass);
            return res;
        }
        for (CategorialClass childCatClass : rootCatClass.getChildren()) {
            List<CategorialClass> pathFromChild = findCategorialClassesPathFromRoot(
                                                                                    childCatClass,
                                                                                    requiredRoot);
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

    private EntityInterface getEntityById(Long entityId) {
        return cache.getEntityById(entityId);
    }

    private AttributeInterface getAttributeById(Long entityId, Long attributeId) {
        return getEntityById(entityId).getAttributeByIdentifier(attributeId);
    }

    private IExpression transformCategoryExpression(IExpression catExpr,
                                                    EntityInterface entryPoint) {
        EntityInterface catEntity = catExpr.getConstraintEntity().getDynamicExtensionsEntity();
        Category category = pivot(getCategoryFromEntity(catEntity), entryPoint);

        CategorialClass rootCatClass = category.getRootClass();
        IExpression rootExpr = createExpression(getEntityById(rootCatClass.getDeEntityId()));

        Map<Integer, Integer> followingConnIndexOrigToNew = new HashMap<Integer, Integer>();

        for (int i = 0; i < catExpr.numberOfOperands(); i++) {
            int initialRootExprSize = rootExpr.numberOfOperands();
            IExpressionOperand operand = catExpr.getOperand(i);

            if (operand.isSubExpressionOperand()) {

                // find path to exit point
                IExpressionId externalExprId = (IExpressionId) operand;

                IAssociation exitAssociation = getJoinGraph().getAssociation(
                                                                             catExpr.getExpressionId(),
                                                                             externalExprId);
                EntityInterface exitPoint = exitAssociation.getSourceEntity();
                List<IAssociation> associationsFromRoot = findAssociationsFromRoot(
                                                                                   rootCatClass,
                                                                                   exitPoint);
                IExpression newExpr = createExpressionsForAssociations(
                                                                       rootExpr,
                                                                       associationsFromRoot);
                // set external expr as subexpr for last expression...
                IExpression externalExpr = constraints.getExpression(externalExprId);
                addSubExpr(newExpr, externalExpr, exitAssociation);

            } else {
                // it is a rule

                // traverse category tree in BFS creating IExpression's along
                // the way.
                IRule rule = (IRule) operand;
                addRuleToExpr(rootExpr, gleanConditions(rootCatClass, rule));

                Map<CategorialClass, IExpression> exprForCatClass = new HashMap<CategorialClass, IExpression>();
                exprForCatClass.put(rootCatClass, rootExpr);
                Set<CategorialClass> currCatClasses = new HashSet<CategorialClass>();
                currCatClasses.addAll(rootCatClass.getChildren());

                Set<IExpressionId> possiblyRedundantExprIds = new HashSet<IExpressionId>();
                // BFS starts from one level below the root...
                while (!currCatClasses.isEmpty()) {
                    Set<CategorialClass> nextCatClasses = new HashSet<CategorialClass>();
                    for (CategorialClass categorialClass : currCatClasses) {
                        nextCatClasses.addAll(categorialClass.getChildren());

                        CategorialClass parentCatClass = categorialClass.getParent();
                        IExpression newExpr = createExpressionsForAssociations(
                                                                               exprForCatClass.get(parentCatClass),
                                                                               categorialClass.getPathFromParent().getIntermediateAssociations(),
                                                                               possiblyRedundantExprIds);

                        addRuleToExpr(newExpr, gleanConditions(categorialClass,
                                                               rule));
                        exprForCatClass.put(categorialClass, newExpr);
                    }
                    currCatClasses = nextCatClasses;
                }
                for (IExpressionId possiblyRedundantExprId : possiblyRedundantExprIds) {
                    removeExprIfRedundant(possiblyRedundantExprId,
                                          possiblyRedundantExprIds);
                }
            }
            if (i < catExpr.numberOfOperands() - 1) {
                followingConnIndexOrigToNew.put(i,
                                                rootExpr.numberOfOperands() - 1);
            }
            int numOperandsAdded = rootExpr.numberOfOperands()
                    - initialRootExprSize;
            if (numOperandsAdded > 1) {
                int precedingConnNesting = catExpr.getLogicalConnector(i - 1, i).getNestingNumber();
                int followingConnNesting = catExpr.getLogicalConnector(i, i + 1).getNestingNumber();
                int operandNesting = (precedingConnNesting > followingConnNesting) ? precedingConnNesting
                        : followingConnNesting;
                for (int j = 0; j <= operandNesting; j++) {
                    rootExpr.addParantheses(initialRootExprSize,
                                            initialRootExprSize
                                                    + numOperandsAdded - 1);
                }
            }
        }

        for (Map.Entry<Integer, Integer> entry : followingConnIndexOrigToNew.entrySet()) {
            ILogicalConnector connector = catExpr.getLogicalConnector(
                                                                      entry.getKey(),
                                                                      entry.getKey() + 1);
            rootExpr.setLogicalConnector(entry.getValue(),
                                         entry.getValue() + 1, connector);
        }
        return rootExpr;
    }

    private void removeExprIfRedundant(
                                       IExpressionId possiblyRedundantExprId,
                                       Set<IExpressionId> possiblyRedundantExprIds) {
        IExpression expr = constraints.getExpression(possiblyRedundantExprId);
        if (expr == null) {
            // the child was already killed along with its parents... one
            // problem with dead expresssions is that you can't kill 'em.
            return;
        }

        for (IExpressionId possiblyRedundantChildExprId : getJoinGraph().getChildrenList(
                                                                                         possiblyRedundantExprId)) {
            removeExprIfRedundant(possiblyRedundantChildExprId,
                                  possiblyRedundantExprIds);
        }
        if (expr.numberOfOperands() == 0
                && possiblyRedundantExprIds.contains(possiblyRedundantExprId)) {
            constraints.removeExpressionWithId(possiblyRedundantExprId);
        }
    }

    /**
     * Creates expressions corresponding to the associations; also sets the
     * subexpressions in parentExpr.
     * @param parentExpr
     *            the parentExpr that which is start-point for the expressions
     *            created.
     * @param associationsToLastChild
     *            the associations that lead from parentExpr's entity to the
     *            entity for which the expr is to be created.
     * @return the last expression.
     */
    private IExpression createExpressionsForAssociations(
                                                         IExpression parentExpr,
                                                         List<IAssociation> associationsToLastChild) {
        IExpression lastChild = parentExpr;
        for (IAssociation association : associationsToLastChild) {
            parentExpr = lastChild;
            lastChild = createExpression(association.getTargetEntity());
            addSubExpr(parentExpr, lastChild, association);
        }
        return lastChild;
    }

    /**
     * Creates expressions corresponding to the associations; also sets the
     * subexpressions in parentExpr.
     * @param parentExpr
     *            the parentExpr that which is start-point for the expressions
     *            created.
     * @param associationsToLastChild
     *            the associations that lead from parentExpr's entity to the
     *            entity for which the expr is to be created.
     * @return the last expression.
     */
    private IExpression createExpressionsForAssociations(
                                                         IExpression parentExpr,
                                                         List<IAssociation> associationsToLastChild,
                                                         Set<IExpressionId> expressionsAdded) {
        IExpression lastChild = parentExpr;
        for (IAssociation association : associationsToLastChild) {
            parentExpr = lastChild;
            lastChild = createExpression(association.getTargetEntity());
            expressionsAdded.add(lastChild.getExpressionId());
            addSubExpr(parentExpr, lastChild, association);
        }
        return lastChild;
    }

    private List<IAssociation> findAssociationsFromRoot(
                                                        CategorialClass rootCatClass,
                                                        EntityInterface exitPoint) {

        List<CategorialClass> catClassesPath = findCategorialClassesPathFromRoot(
                                                                                 rootCatClass,
                                                                                 exitPoint);
        List<IAssociation> associations = new ArrayList<IAssociation>();
        for (int i = 1; i < catClassesPath.size(); i++) {
            associations.addAll(catClassesPath.get(i).getPathFromParent().getIntermediateAssociations());
        }
        return associations;
    }

    private IRule gleanConditions(CategorialClass categorialClass, IRule rule) {
        IRule newRule = QueryObjectFactory.createRule();
        for (int i = 0; i < rule.size(); i++) {
            ICondition condition = rule.getCondition(i);
            AttributeInterface catAttr = condition.getAttribute();

            Long origAttrid = categorialClass.findSourceAttributeId(catAttr.getId());
            AttributeInterface origAttr = getAttributeById(
                                                           categorialClass.getDeEntityId(),
                                                           origAttrid);
            if (origAttr != null) {
                newRule.addCondition(cloneWithSpecifiedAttribute(condition,
                                                                 origAttr));
            }
        }
        return newRule;
    }

    private ICondition cloneWithSpecifiedAttribute(ICondition origCondition,
                                                   AttributeInterface attr) {
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

    private void addSubExpr(IExpression parentExpr, IExpression childExpr,
                            IAssociation association) {
        addOperandToExpr(parentExpr, childExpr.getExpressionId());
        try {
            getJoinGraph().putAssociation(parentExpr.getExpressionId(),
                                          childExpr.getExpressionId(),
                                          association);
        } catch (CyclicException e) {
            // this should not occur...
            throw new RuntimeException("Problem in code...", e);
        }
    }

    private void addOperandToExpr(IExpression parentExpr,
                                  IExpressionOperand operand) {
        if (parentExpr.numberOfOperands() > 0) {
            parentExpr.addOperand(
                                  QueryObjectFactory.createLogicalConnector(LogicalOperator.And),
                                  operand);
        } else {
            parentExpr.addOperand(operand);
        }
    }

    private IExpression createExpression(EntityInterface entity) {
        IConstraintEntity constraintEntity = QueryObjectFactory.createConstrainedEntity(entity);
        IExpression expr = constraints.addExpression(constraintEntity);

        return expr;
    }

    private IJoinGraph getJoinGraph() {
        return constraints.getJoinGraph();
    }
}

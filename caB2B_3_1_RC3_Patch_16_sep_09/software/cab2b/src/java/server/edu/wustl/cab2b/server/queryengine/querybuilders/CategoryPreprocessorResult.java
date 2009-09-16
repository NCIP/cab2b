package edu.wustl.cab2b.server.queryengine.querybuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.util.TreeNode;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.queryobject.IExpression;

/**
 * Provides details regarding the result of expanding a category expression into
 * its constituent expressions. Auxiliary info related to the categories (e.g.
 * category for entity) in the query is also provided to enhance performance.
 * @author srinath_k
 */
public class CategoryPreprocessorResult {
    /**
     * key = entity of a category <br>
     * value = set of rootnodes. Each rootNode points to the rootExpression
     * obtained on breaking an expression on a category entity (the key of this
     * map). The treeNode provides a "view" of the joinGraph. Traversing this
     * view will cover all the expressions created by breaking a category
     * expression.
     */
    private Map<EntityInterface, Set<TreeNode<IExpression>>> exprsSourcedFromCategories;

    /**
     * A redundant expr is one which satisfies the following conditions :<br>
     * <ul>
     * <li>It is created during the process of expanding a category expression
     * (i.e. an expression originally defined on a class as part of IQuery will
     * never be marked redundant.)</li>
     * <li>It contains no rules</li>
     * <li>Each subExpression of this expression is a redundant expresssion
     * (recursive).</li>
     * </ul>
     */
    private Set<IExpression> redundantExprs;

    /**
     * key = expression<br>
     * value = the categorial class for which this expression was created.
     */
    private Map<IExpression, CategorialClass> catClassForExpr;

    // this would be needed when pivoting catResults...
    private List<CategorialClass> originallyRootCatClasses;

    /**
     * key = entity<br>
     * value = category for this entity<br>
     * Finding this info is a database call. So it is present primarily so that
     * clients of the categorypreprocessor need not perform more database calls.
     */
    private Map<EntityInterface, Category> categoryForEntity;

    /**
     * Default constructor for CategoryPreprocessorResult
     */
    public CategoryPreprocessorResult() {
        setRedundantExprs(new HashSet<IExpression>());
        setExprsSourcedFromCategories(new HashMap<EntityInterface, Set<TreeNode<IExpression>>>());
        setCatClassForExpr(new HashMap<IExpression, CategorialClass>());
        setOriginallyRootCatClasses(new ArrayList<CategorialClass>());
        setCategoryForEntity(new HashMap<EntityInterface, Category>());
    }

    /**
     * This method returns the Map of Expressions sourced form Categories.
     * 
     * @return Map of Entity -> Set of {TreeNode of Expression}
     */
    public Map<EntityInterface, Set<TreeNode<IExpression>>> getExprsSourcedFromCategories() {
        return exprsSourcedFromCategories;
    }

    /**
     * Adds expression resource from category
     * @param catEntity
     * @param exprNode
     */
    public void addExprSourcedFromCategory(EntityInterface catEntity,
                                           TreeNode<IExpression> exprNode) {
        Set<TreeNode<IExpression>> value = getExprsSourcedFromCategories().get(
                                                                               catEntity);
        if (value == null) {
            value = new HashSet<TreeNode<IExpression>>();
            getExprsSourcedFromCategories().put(catEntity, value);
        }
        value.add(exprNode);
    }

    /**
     * Setter for expression sources from category
     * @param exprsSourcedFromCategories
     */
    public void setExprsSourcedFromCategories(
                                              Map<EntityInterface, Set<TreeNode<IExpression>>> exprsSourcedFromCategories) {
        this.exprsSourcedFromCategories = exprsSourcedFromCategories;
    }

    /**
     * This method returns the set of redundant expressions
     * 
     * @return Set of {Expressions}
     */
    public Set<IExpression> getRedundantExprs() {
        return redundantExprs;
    }

    /**
     * Setter for redundantExprs
     * @param redundantExprs
     */
    public void setRedundantExprs(Set<IExpression> redundantExprs) {
        this.redundantExprs = redundantExprs;
    }

    /**
     * This method clears map and set
     */
    public void clear() {
        getExprsSourcedFromCategories().clear();
        getRedundantExprs().clear();
    }

    /**
     * This method returns the Output Expressions
     * 
     * @return Set of {OutputExpressions}
     */
    public Set<TreeNode<IExpression>> getOutputExpressions() {
        Set<TreeNode<IExpression>> outputExpressions = new HashSet<TreeNode<IExpression>>();
        for (Set<TreeNode<IExpression>> treeNodeSet : getExprsSourcedFromCategories().values()) {
            for (TreeNode<IExpression> root : treeNodeSet) {
                if (root.getValue().isInView()) {
                    outputExpressions.add(root);
                }
            }
        }
        return outputExpressions;
    }

    /**
     * This method returns category class of expressions.
     * 
     * @return Map of Expression -> CategorialClass
     */
    public Map<IExpression, CategorialClass> getCatClassForExpr() {
        return catClassForExpr;
    }

    /**
     * Setter for Category class of expressions
     * @param catClassForExpr
     */
    public void setCatClassForExpr(
                                   Map<IExpression, CategorialClass> catClassForExpr) {
        this.catClassForExpr = catClassForExpr;
    }

    /**
     * This method returns the List of CategorialClass
     * @return List of CateogrialClass
     */
    public List<CategorialClass> getOriginallyRootCatClasses() {
        return originallyRootCatClasses;
    }

    /**
     * Setter for List of CategorialClass
     * @param originallyRootCatClasses
     */
    public void setOriginallyRootCatClasses(
                                            List<CategorialClass> originallyRootCatClasses) {
        this.originallyRootCatClasses = originallyRootCatClasses;
    }

    /**
     * This method returns the map between category and entity 
     * 
     * @return Map of Entity -> Category
     */
    public Map<EntityInterface, Category> getCategoryForEntity() {
        return categoryForEntity;
    }

    /**
     * Setter method for map between entity and category
     * @param categoryForEntity
     */
    public void setCategoryForEntity(
                                     Map<EntityInterface, Category> categoryForEntity) {
        this.categoryForEntity = categoryForEntity;
    }
}

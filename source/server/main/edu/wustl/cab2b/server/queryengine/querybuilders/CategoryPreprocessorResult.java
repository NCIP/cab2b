package edu.wustl.cab2b.server.queryengine.querybuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.queryengine.utils.TreeNode;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.queryobject.IExpression;

public class CategoryPreprocessorResult {
    private Map<EntityInterface, Set<TreeNode<IExpression>>> exprsSourcedFromCategories;

    private Set<IExpression> redundantExprs;

    private Map<IExpression, CategorialClass> catClassForExpr;

    // this would be needed when pivoting catResults...
    private List<CategorialClass> originallyRootCatClasses;

    private Map<EntityInterface, Category> categoryForEntity;

    public CategoryPreprocessorResult() {
        setRedundantExprs(new HashSet<IExpression>());
        setExprsSourcedFromCategories(new HashMap<EntityInterface, Set<TreeNode<IExpression>>>());
        setCatClassForExpr(new HashMap<IExpression, CategorialClass>());
        setOriginallyRootCatClasses(new ArrayList<CategorialClass>());
        setCategoryForEntity(new HashMap<EntityInterface, Category>());
    }

    public Map<EntityInterface, Set<TreeNode<IExpression>>> getExprsSourcedFromCategories() {
        return exprsSourcedFromCategories;
    }

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

    public void setExprsSourcedFromCategories(
                                              Map<EntityInterface, Set<TreeNode<IExpression>>> exprsSourcedFromCategories) {
        this.exprsSourcedFromCategories = exprsSourcedFromCategories;
    }

    public Set<IExpression> getRedundantExprs() {
        return redundantExprs;
    }

    public void setRedundantExprs(Set<IExpression> redundantExprs) {
        this.redundantExprs = redundantExprs;
    }

    public void clear() {
        getExprsSourcedFromCategories().clear();
        getRedundantExprs().clear();
    }

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

    public Map<IExpression, CategorialClass> getCatClassForExpr() {
        return catClassForExpr;
    }

    public void setCatClassForExpr(
                                   Map<IExpression, CategorialClass> catClassForExpr) {
        this.catClassForExpr = catClassForExpr;
    }

    public List<CategorialClass> getOriginallyRootCatClasses() {
        return originallyRootCatClasses;
    }

    public void setOriginallyRootCatClasses(
                                            List<CategorialClass> originallyRootCatClasses) {
        this.originallyRootCatClasses = originallyRootCatClasses;
    }

    public Map<EntityInterface, Category> getCategoryForEntity() {
        return categoryForEntity;
    }

    public void setCategoryForEntity(
                                     Map<EntityInterface, Category> categoryForEntity) {
        this.categoryForEntity = categoryForEntity;
    }
}

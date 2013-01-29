/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * <p>
 * Title: IClientQueryBuilderInterface interface>
 * <p>
 * Description: This interface provides APIs for creating the query object from
 * the DAG view.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * 
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.querysuite.utils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

/**
 * This interface provides APIs for creating the query object from the DAG view.
 * 
 * @author gautam_shetty
 */
public interface IConstraintsObjectBuilderInterface {

    /**
     * Returns the query object.
     * 
     * @return Returns the query object.
     */
    public IQuery getQuery();

    /**
     * Sets the query object.
     * 
     * @param query The query object to set.
     */
    public void setQuery(IQuery query);

    /**
     * Creates a new expression in the query and adds this rule to that
     * expression.
     * 
     * @param attributes The attributes for the conditions in the rule.
     * @param operators The operators for the conditions in the rule.
     * @param firstValues The first values for the conditions in the rule.
     * @param secondValues The second values for the conditions in the rule.
     * @param entity the entity for which the new expression is to be created
     *            (can be different from conditions' attribute's entity).
     * @return The int of the expression added.
     */
    public int addExpression(IRule rule, EntityInterface entity);
    
    /**
     * Creates a new empty expression on the specified entity.
     * @param entity the entity for which the new expression is to be created
     * @return the if of the new expression.
     */
    public int addExpression(EntityInterface entity);

    /**
     * Edits the expression with the given expression id with the conditions
     * passed.
     * 
     * @param iExpressionId The expression to be updated.
     * @param conditionList The list of conditions to be added to the
     *            expression.
     * @return The expression added.
     */
    public IExpression editExpression(int iExpressionId, IRule rule);

    /**
     * Removes the expression with the specified expression id from the query.
     * Returns true if the expression is removed else returns false.
     * 
     * @param iExpressionId The id of the expression to be removed.
     * @return true if the expression is removed else returns false.
     */
    public IExpression removeExpression(int iExpressionId);

    /**
     * Adds the path between the source and destination expressions.
     * 
     * @param sourceExpressionId The source expression.
     * @param destExpressionId The destination expression.
     * @param association The association to be set.
     */
    public List<Integer> addPath(int sourceExpressionId, int destExpressionId, IPath path)
            throws CyclicException;

    /**
     * Adds the association between the source and destination expressions.
     * 
     * @param sourceExpressionId The source expression.
     * @param destExpressionId The destination expression.
     * @param association The association to be set.
     */
    public void addAssociation(int sourceExpressionId, int destExpressionId,
            IAssociation association) throws CyclicException;

    /**
     * Removes the associations between the source and destination expressions.
     * Returns true if the association are removed successfully else returns
     * false.
     * 
     * @param sourceExpressionId The source expression id.
     * @param destExpressionId The destination expressio id.
     * @return true if the association are removed successfully else returns
     *         false.
     */
    public boolean removeAssociation(int sourceExpressionId, int destExpressionId);

    /**
     * Sets the logical operator between the parent and child expressions.
     * 
     * @param parentExpressionId The parent expression id.
     * @param childExpressionId The child expression id.
     * @param logicalOperator The logical operator to be set.
     */
    public void setLogicalConnector(int parentExpressionId, int childExpressionId,
            LogicalOperator logicalOperator, boolean isUpdate);

    /**
     * Creates a copy of the source expression passed and returns the expression
     * id of the new expression.
     * 
     * @param sourceExpression The source expression.
     * @return The expression id of the new expression created.
     */
    public int createExpressionCopy(IExpression sourceExpression);

    /**
     * Returns all the entities in the constraints of the query.
     * 
     * @return Collection of all entities in the constraints of the query.
     */
    public Collection<EntityInterface> getEntities();

    /**
     * Creates a dummy expression for the specified entity. This expression
     * contains no rules in it. Its just an empty expression.
     * 
     * @param entity The entity for which the expression is to be created.
     */
    public int createDummyExpression(EntityInterface entity);

    /**
     * Adds the rule for the given conditions.
     * 
     * @param attributes The attributes in the condition.
     * @param operators The operators in the condition.
     * @param firstValues The first values for the condition.
     * @param secondValues The second values for the condition. *
     * @param entity the entity for which the new expression is to be created
     *            (can be different from conditions' attribute's entity).
     */
    public int addRule(List<AttributeInterface> attributes, List<String> operators,
            List<String> firstValues, List<String> secondValues, EntityInterface entity);

    /**
     * Adds the rule for the given conditions.
     * 
     * @param attributes The attributes in the condition.
     * @param operators The operators in the condition.
     * @param firstValues The first values for the condition.
     * @param secondValues The second values for the condition. *
     * @param entity the entity for which the new expression is to be created
     *            (can be different from conditions' attribute's entity).
     */
    public int addRule(List<AttributeInterface> attributes, List<String> operators,
            List<List<String>> Values, EntityInterface entity);

    /**
     * Adds parantheses around operands with ids child1Id and child2Id which are
     * present in parent operand with Id parentId.
     * 
     * @param parentId The parent operand id.
     * @param child1Id The first child operand id.
     * @param child2Id The second child operand id.
     */
    public void addParantheses(int parentId, int child1Id, int child2Id);

    /**
     * Removes parantheses around operands with ids child1Id and child2Id which
     * are present in parent operand with Id parentId.
     * 
     * @param parentId The parent operand id.
     * @param child1Id The first child operand id.
     * @param child2Id The second child operand id.
     */
    public void removeParantheses(int parentId, int child1Id, int child2Id);

    /**
     * Method to get all the user added expression Ids in query object
     * 
     * @return List of visible expressionIds
     */
    public Set<Integer> getVisibleExressionIds();

    /**
     * Method to add the user added expression Id to visible list
     * 
     * @param expressionId ExpressionId to be added to visible list
     */
    public void addExressionIdToVisibleList(int expressionId);

    /**
     * Method to remove the user added expression Id from visible list
     * 
     * @param expressionId ExpressionId to be removed from visible list
     */
    public void removeExressionIdFromVisibleList(int expressionId);

    /**
     * Method to check if adding the path generates cyclic graph in the query
     * 
     * @param sourceExpressionId The expression ID of source node
     * @param destExpressionId The expression ID of destination node
     * @param path The path to added between source and destination node
     * @return true if addition of this path generates cyclic query
     */
    public boolean isPathCreatesCyclicGraph(int sourceExpressionId, int destExpressionId,
            IPath path);

}
/**
 * <p>Title: IClientQueryBuilderInterface interface>
 * <p>Description:  This interface provides APIs for creating the query
 * object from the DAG view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

/**
 * This interface provides APIs for creating the query
 * object from the DAG view.
 * @author gautam_shetty
 */
public interface IClientQueryBuilderInterface
{
    
    /**
     * Returns the query object.
     * @return Returns the query object.
     */
    public IQuery getQuery();
    
    /**
     * Sets the query object.
     * @param query The query object to set.
     */
    public void setQuery(IQuery query);
    
    /**
     * Creates a new expression in the query and adds this rule to that expression.
     * @param attributes The attributes for the conditions in the rule.
     * @param operators The operators for the conditions in the rule.
     * @param firstValues The first values for the conditions in the rule.
     * @param secondValues The second values for the conditions in the rule.
     * @return The IExpressionId of the expression added.
     */
    public IExpressionId addExpression(IRule rule);
    
    /**
     * Edits the expression with the given expression id with the conditions passed.
     * @param iExpressionId The expression to be updated.
     * @param conditionList The list of conditions to be added to the expression.
     * @return The expression added.
     */
    public IExpression editExpression(IExpressionId iExpressionId, IRule rule);
    
    /**
     * Removes the expression with the specified expression id from the query.
     * Returns true if the expression is removed else returns false.  
     * @param iExpressionId The id of the expression to be removed. 
     * @return true if the expression is removed else returns false. 
     */
    public IExpression removeExpression(IExpressionId iExpressionId);
    
    /**
     * Adds the path between the source and destination expressions.
     * @param sourceExpressionId The source expression.
     * @param destExpressionId The destination expression.
     * @param association The association to be set.
     */
    public List<IExpressionId> addPath(IExpressionId sourceExpressionId,
            IExpressionId destExpressionId, IPath path) throws CyclicException;
    /**
     * Adds the association between the source and destination expressions.
     * @param sourceExpressionId The source expression.
     * @param destExpressionId The destination expression.
     * @param association The association to be set.
     */
    public void addAssociation(IExpressionId sourceExpressionId,
            IExpressionId destExpressionId, IAssociation association) throws CyclicException;
    
    /**
     * Removes the associations between the source and destination expressions.
     * Returns true if the association are removed successfully else returns false. 
     * @param sourceExpressionId The source expression id.
     * @param destExpressionId The destination expressio id.
     * @return true if the association are removed successfully else returns false.
     */
    public boolean removeAssociation(IExpressionId sourceExpressionId, IExpressionId destExpressionId);
    
    /**
     * Sets the logical operator between the parent and child expressions.
     * @param parentExpressionId The parent expression id.
     * @param childExpressionId The child expression id.
     * @param logicalOperator The logical operator to be set.
     */
    public void setLogicalConnector(IExpressionId parentExpressionId,
                IExpressionId childExpressionId, LogicalOperator logicalOperator, boolean isUpdate);
    
    /**
     * Creates a copy of the source expression passed and 
     * returns the expression id of the new expression. 
     * @param sourceExpression The source expression.
     * @return The expression id of the new expression created.
     */
    public IExpressionId createExpressionCopy(IExpression sourceExpression);
    
    /**
     * Returns all the entities in the constraints of the query.
     * @return Collection of all entities in the constraints of the query.
     */
    public Collection<EntityInterface> getEntities();
    
    /**
     * Creates a dummy expression for the specified entity.
     * This expression contains no rules in it. Its just an empty expression.
     * @param entity The entity for which the expression is to be created.
     */
    public IExpressionId createDummyExpression(EntityInterface entity);
    
    /**
     * Adds the rule for the given conditions. 
     * @param attributes The attributes in the condition.
     * @param operators The operators in the condition.
     * @param firstValues The first values for the condition. 
     * @param secondValues The second values for the condition.
     */
    public IExpressionId addRule(List<AttributeInterface> attributes,
            List<String> operators, List<String> firstValues,
            List<String> secondValues);
    
    /**
     * Adds the rule for the given conditions. 
     * @param attributes The attributes in the condition.
     * @param operators The operators in the condition.
     * @param firstValues The first values for the condition. 
     * @param secondValues The second values for the condition.
     */
    public IExpressionId addRule(List<AttributeInterface> attributes,
            List<String> operators, ArrayList<ArrayList<String>> Values);
    
    /**
     * Adds parantheses around operands with ids child1Id and child2Id which are 
     * present in parent operand with Id parentId.
     * @param parentId The parent operand id.
     * @param child1Id The first child operand id.
     * @param child2Id The second child operand id.
     */
    public void addParantheses(IExpressionId parentId, IExpressionId child1Id, IExpressionId child2Id);
    
    /**
     * Removes parantheses around operands with ids child1Id and child2Id which are 
     * present in parent operand with Id parentId.
     * @param parentId The parent operand id.
     * @param child1Id The first child operand id.
     * @param child2Id The second child operand id.
     */
    public void removeParantheses(IExpressionId parentId, IExpressionId child1Id, IExpressionId child2Id);
    
    /**
     * Sets the output of the query to the specified entity.
     * @param entity The entity to be set as output.
     */
    public void setOutputForQuery(EntityInterface entity);
    
    public void setOutputForQueryForSpecifiedURL(EntityInterface entity, String strURL);
    
    
    /**
     * Method to get all the user added expression Ids in query object
     * @return List of visible expressionIds
     */
    public List<IExpressionId> getVisibleExressionIds();
    
    /**
     * Method to add the user added expression Id to visible list
     * @param expressionId ExpressionId to be added to visible list
     */
    public void addExressionIdToVisibleList(IExpressionId expressionId);
    
    /**
     * Method to remove the user added expression Id from visible list
     * @param expressionId ExpressionId to be removed from visible list
     */
    public void removeExressionIdFromVisibleList(IExpressionId expressionId);
    
    /**
     * Method to check if adding the path generates cyclic graph in the query
     * @param sourceExpressionId The expression ID of source node
	 * @param destExpressionId The expression ID of destination node
	 * @param path The path to added between source and destination node
	 * @return true if addition of this path generates cyclic query 
	 */
	public boolean isPathCreatesCyclicGraph(IExpressionId sourceExpressionId, IExpressionId destExpressionId, IPath path);
	
    
    
}
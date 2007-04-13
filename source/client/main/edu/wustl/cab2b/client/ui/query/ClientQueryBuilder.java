/**
 * <p>Title: ClientQueryBuilder Class>
 * <p>Description:  This class provides implementations for the APIs for creating query
 * object from the DAG view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraintEntity;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.ILogicalConnector;
import edu.wustl.common.querysuite.queryobject.IOutputEntity;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.impl.ConstraintEntity;
import edu.wustl.common.util.logger.Logger;

/**
 * This class provides the APIs for creating the query object from the 
 * DAG view.
 * @author gautam_shetty
 */
public class ClientQueryBuilder implements IClientQueryBuilderInterface
{
    
    /**
     * The query object.
     */
    private IQuery query = Cab2bQueryObjectFactory.createCab2bQuery();
    
    /**
     * 
     */
    private List<IExpressionId> m_visibleExpressions = new ArrayList<IExpressionId>();
    /**
     * Returns the query object.
     * @return Returns the query object.
     */
    public IQuery getQuery()
    {
        return query;
    }
    
    /**
     * Sets the query object.
     * @param query The query object to set.
     */
    public void setQuery(IQuery query)
    {
        this.query = query;
    }
    
    /**
     * Adds the expression with the conditions passed and returns the expression id.
     * @param conditionList The condition list.
     * @return The expression id of the expression added.  
     */
    public IExpressionId addExpression(IRule rule)
    {
        IConstraintEntity constraintEntity = Cab2bQueryObjectFactory
                                        .createConstrainedEntity(rule.getCondition(0)
                                                .getAttribute().getEntity());
        IExpression expression = query.getConstraints().addExpression(constraintEntity);
        expression.setInView(true);
        expression.addOperand(rule);
        
        return expression.getExpressionId();
    }
    
    /**
     * Edits the expression with the given expression id and adds the 
     * condition list.
     * @param iExpressionId The expression to be updated.
     * @param conditionList The conditions to be added.
     * @return The IExpression of the updated expression. 
     */
    public IExpression editExpression(IExpressionId iExpressionId,
            IRule rule)
    {
        IExpression expression = query.getConstraints().getExpression(iExpressionId);
        expression.addOperand(rule);
        
        return expression;
    }
    
    /**
     * Removes the expression with the specified expression id
     * from the query and returns the removed expression.
     * @param iExpressionId The id of the expression to be removed. 
     * @return The IExpression of the expression removed.
     */
    public IExpression removeExpression(IExpressionId iExpressionId)
    {
        return query.getConstraints().removeExpressionWithId(iExpressionId);
    }
    
    /**
     * Adds the association between the source and destination expressions.
     * @param sourceExpressionId The source expression.
     * @param destExpressionId The destination expression.
     * @param association The association to be set.
     */
    public void addAssociation(IExpressionId sourceExpressionId,
            IExpressionId destExpressionId, IAssociation association) throws CyclicException
    {
        IExpression sourceExpression = query.getConstraints().getExpression(sourceExpressionId);
        IExpression destExpression = query.getConstraints().getExpression(destExpressionId);
        addAssociationToQuery(sourceExpressionId, destExpressionId, association);
        sourceExpression.addOperand(destExpression.getExpressionId());
        if(association instanceof IInterModelAssociation)
        {
        	((IInterModelAssociation)association).setSourceServiceUrl(edu.wustl.cab2b.common.util.Utility.getServiceURLS(
        			association.getSourceEntity())[0]);
        	((IInterModelAssociation)association).setTargetServiceUrl(edu.wustl.cab2b.common.util.Utility.getServiceURLS(
        			association.getTargetEntity())[0]);
        }
    }
    
    /**
     * Creates a copy of the source expression passed and 
     * returns the expression id of the new expression. 
     * @param sourceExpression The source expression.
     * @return The expression id of the new expression created.
     */
    public IExpressionId createExpressionCopy(IExpression sourceExpression)
    {
        return addExpression((IRule)sourceExpression.getOperand(0));
    }
    
    /**
     * Adds the association between the source and destination expressions.   
     * @param sourceExpressionId The source expression id.
     * @param destExpressionId The destiantion expression id.
     * @param association The association to be added.
     */
    private void addAssociationToQuery(IExpressionId sourceExpressionId,
            IExpressionId destExpressionId, IAssociation association) throws CyclicException
    {
        try
        {
            query.getConstraints().getJoinGraph().putAssociation(sourceExpressionId,
                    destExpressionId, association);
        }
        catch (CyclicException cycExp)
        {
            Logger.out.error("Cyclic Exception while putting association between " + 
                    sourceExpressionId.getInt()+" and "+destExpressionId.getInt());
            throw cycExp;
        }
    }
    
    /**
     * Removes the association between the specified source and destination expressions.
     * Returns true if the association is successfully removed else returns false.
     * @param sourceExpressionId The source expression id.
     * @param destExpressionId The destination expression id.
     * @return Returns true if the association is successfully removed else returns false. 
     */
    public boolean removeAssociation(IExpressionId sourceExpressionId,
            IExpressionId destExpressionId)
    {
        return query.getConstraints().getExpression(sourceExpressionId).removeOperand(destExpressionId)
        && query.getConstraints().getJoinGraph().removeAssociation(sourceExpressionId, destExpressionId);
    }
    
    /**
     * Sets the logical operator between the parent and child expressions.
     * @param parentExpressionId The parent expression id.
     * @param childExpressionId The child expression id.
     * @param logicalOperator The logical operator to be set.
     */
    public void setLogicalConnector(IExpressionId parentExpressionId,
                IExpressionId childExpressionId, LogicalOperator logicalOperator, boolean isUpdate)
    {
    	IExpression parentExpression = query.getConstraints().getExpression(parentExpressionId);
        int childIndex = parentExpression.indexOfOperand(childExpressionId);
        if (childIndex != 0)
        {
            int parentIndex = childIndex - 1;
        	if(false == isUpdate)
            {
            	parentExpression.setLogicalConnector(parentIndex, childIndex, QueryObjectFactory.createLogicalConnector(logicalOperator));
            }
        	else
        	{
        		parentExpression.getLogicalConnector(parentIndex, childIndex).setLogicalOperator(logicalOperator);
        	}
        }
    }
    
    /**
     * Returns all the entities in the constraints of the query.
     * @return Collection of all entities in the constraints of the query.
     */
    public Collection<EntityInterface> getEntities()
    {
        Collection<EntityInterface> entities = new HashSet<EntityInterface>();
        
        Enumeration<IExpressionId> expressionIds = query.getConstraints().getExpressionIds();
        while (expressionIds.hasMoreElements())
        {
            IExpressionId expressionId = expressionIds.nextElement();
            IExpression expression = query.getConstraints().getExpression(expressionId);
            entities.add((EntityInterface)expression.getConstraintEntity().getDynamicExtensionsEntity());
        }
        
        return entities;
    }
    
    /**
     * Creates a dummy expression for the specified entity.
     * This expression contains no rules in it. Its just an empty expression.
     * @param entity The entity for which the expression is to be created.
     */
    public IExpressionId createDummyExpression(EntityInterface entity)
    {
        Logger.out.info("Inside createDummyExpression()");
        IConstraintEntity constraintEntity = Cab2bQueryObjectFactory.createConstrainedEntity(entity);
        IExpression expression = query.getConstraints().addExpression(constraintEntity);
        Logger.out.info("Exiting createDummyExpression()");
        
        return expression.getExpressionId();
    }
    
    /**
     * Creates a rule for the given conditions.
     * An expression is also created and the rule is added to that expression.
     * @param attributes The attributes in the condition.
     * @param operators The operators in the condition.
     * @param firstValues The first values for the condition. 
     * @param secondValues The second values for the condition.
     */
    public IExpressionId addRule(List<AttributeInterface> attributes,
            List<String> operators, List<String> firstValues,
            List<String> secondValues)
    {
        // Create the list of conditions submitted by user.
        List<ICondition> conditionList = getConditions(attributes,
                operators, firstValues, secondValues);
        
        // Create the rule for the conditions submitted.
        IRule rule = Cab2bQueryObjectFactory.createRule(conditionList);
        
        return addExpression(rule);
    }
    /**
     * Creates a rule for the given conditions.
     * An expression is also created and the rule is added to that expression.
     * @param attributes The attributes in the condition.
     * @param operators The operators in the condition.
     * @param Values The list of values for all the conditions. 
     */
    public IExpressionId addRule(List<AttributeInterface> attributes, List<String> operators, ArrayList<ArrayList<String>> Values) 
    {
    	// Create the list of conditions submitted by user.
        List<ICondition> conditionList = getConditions(attributes,
                operators, Values);
        
        // Create the rule for the conditions submitted.
        IRule rule = Cab2bQueryObjectFactory.createRule(conditionList);
        return addExpression(rule);
	}
    
    
    /**
     * Returns the list of conditions given the attributes, operators and the values for the attributes.
     * @param attributes The attributes on which the conditions are defined.
     * @param operators The operators for the conditions.
     * @param firstValues The first values in the conditions.
     * @param secondValues The second values in the conditions.
     * @return the list of conditions given the attributes, operators and the values for the attributes.
     */
    public List<ICondition> getConditions(List<AttributeInterface> attributes,
            List<String> operators,  ArrayList<ArrayList<String>> Values)
    {
        List<ICondition> conditionList = new ArrayList<ICondition>();
        for (int i = 0; i < attributes.size(); i++)
        {
            ICondition condition = Cab2bQueryObjectFactory.createCondition();
            condition.setAttribute(attributes.get(i));
            condition.setRelationalOperator(edu.wustl.cab2b.client.ui.query.Utility
                            .getRelationalOperator(operators.get(i)));
            List<String> condtionValues = Values.get(i);
            if(0 != condtionValues.size())
            {
            	for(int j=0; j<condtionValues.size(); j++)
            	condition.addValue(condtionValues.get(j));
            }
            conditionList.add(condition);
        }
        return conditionList;
    }
    
    /**
     * Returns the list of conditions given the attributes, operators and the values for the attributes.
     * @param attributes The attributes on which the conditions are defined.
     * @param operators The operators for the conditions.
     * @param firstValues The first values in the conditions.
     * @param secondValues The second values in the conditions.
     * @return the list of conditions given the attributes, operators and the values for the attributes.
     */
    public List<ICondition> getConditions(List<AttributeInterface> attributes,
            List<String> operators, List<String> firstValues,
            List<String> secondValues)
    {
        List<ICondition> conditionList = new ArrayList<ICondition>();
        for (int i = 0; i < attributes.size(); i++)
        {
            ICondition condition = Cab2bQueryObjectFactory.createCondition();
            condition.setAttribute(attributes.get(i));
            condition.setRelationalOperator(edu.wustl.cab2b.client.ui.query.Utility
                            .getRelationalOperator(operators.get(i)));
            if(null != firstValues.get(i))
            {
            	condition.addValue((String) firstValues.get(i));
            	if (null != secondValues.get(i))
            	{
            		condition.addValue((String) secondValues.get(i));
            	}
            }
            
            conditionList.add(condition);
        }
        return conditionList;
    }
    
    /**
     * Adds parantheses around operands with ids child1Id and child2Id which are 
     * present in parent operand with Id parentId.
     * @param parentId The parent operand id.
     * @param child1Id The first child operand id.
     * @param child2Id The second child operand id.
     */
    public void addParantheses(IExpressionId parentId, IExpressionId child1Id, IExpressionId child2Id)
    {
        
        IExpression parentExpression = query.getConstraints().getExpression(parentId);
        int child1Index = parentExpression.indexOfOperand(child1Id);
        int child2Index = parentExpression.indexOfOperand(child2Id);
        parentExpression.addParantheses(child1Index, child2Index);
    }
    
    /**
     * Removes parantheses around operands with ids child1Id and child2Id which are 
     * present in parent operand with Id parentId.
     * @param parentId The parent operand id.
     * @param child1Id The first child operand id.
     * @param child2Id The second child operand id.
     */
    public void removeParantheses(IExpressionId parentId, IExpressionId child1Id, IExpressionId child2Id)
    {
        IExpression parentExpression = query.getConstraints().getExpression(parentId);
        int child1Index = parentExpression.indexOfOperand(child1Id);
        int child2Index = parentExpression.indexOfOperand(child2Id);
        parentExpression.removeParantheses(child1Index, child2Index);
    }
    
    /**
     * Adds the path between the source and destination expressions.
     * @param sourceExpressionId The source expression.
     * @param destExpressionId The destination expression.
     * @param path The association path for the source and target entity.
     * @throws CyclicException 
     */
	public List<IExpressionId> addPath(IExpressionId sourceExpressionId, IExpressionId destExpressionId, IPath path) throws CyclicException 
	{
		// TODO Auto-generated method stub
		List<IExpressionId> intermediateExpressionIds = new ArrayList<IExpressionId>();
		List<IAssociation> associations = path.getIntermediateAssociations();
		// No intermediate path between source and target entity
		IExpressionId srcExpressionId = sourceExpressionId;
		for(int i=0; i<associations.size()-1; i++)
		{
				EntityInterface targetEntity = associations.get(i).getTargetEntity();
				IExpressionId targetExpressionId = createDummyExpression(targetEntity);
				IExpression targetExpression = this.query.getConstraints().getExpression(targetExpressionId);
				targetExpression.setVisible(false);
				intermediateExpressionIds.add(targetExpressionId);
				try
				{
					addAssociation(srcExpressionId, targetExpressionId, associations.get(i));
				}
				catch (CyclicException cycExp)
				{
					// remove the associations from here
					for(int j=0; j<intermediateExpressionIds.size(); j++)
					{
						this.removeExpression(intermediateExpressionIds.get(j));
					}
					throw cycExp;
				}
				srcExpressionId = targetExpressionId;
		}
		addAssociation(srcExpressionId, destExpressionId, associations.get(associations.size()-1));
		return intermediateExpressionIds;
	}
    
    /**
     * Sets the output of the query to the specified entity.
     * @param entity The entity to be set as output.
     */
    public void setOutputForQuery(EntityInterface entity)
    {
        IOutputEntity outputEntity = Cab2bQueryObjectFactory.createOutputEntity(entity);
        String[] outputServiceUrls = edu.wustl.cab2b.common.util.Utility.getServiceURLS(entity);
        
        IOutputTreeNode output = Cab2bQueryObjectFactory.createOutputTreeNode(outputEntity);
        query.setRootOutputClass(output);
        
        ((ICab2bQuery)query).setOutputClassUrls(Arrays.asList(outputServiceUrls));
    }
    
    
    public void setOutputForQueryForSpecifiedURL(EntityInterface entity, String strURL)
    {
        IOutputEntity outputEntity = Cab2bQueryObjectFactory.createOutputEntity(entity);        
        IOutputTreeNode output = Cab2bQueryObjectFactory.createOutputTreeNode(outputEntity);
        query.setRootOutputClass(output);
        
        List list = new ArrayList();
        list.add(strURL);
        
        ((ICab2bQuery)query).setOutputClassUrls(list);
    }
    
    
    
    /**
     * Method to add the user added expression Id to visible list
     * @param expressionId ExpressionId to be added to visible list
     */
	public void addExressionIdToVisibleList(IExpressionId expressionId) 
	{
		m_visibleExpressions.add(expressionId);
	}
	
	/**
     * Method to get all the user added expression Ids in query object
     * @return List of visible expressionIds
     */
	public List<IExpressionId> getVisibleExressionIds()
	{
		return m_visibleExpressions;
	}
	
	/**
     * Method to remove the user added expression Id from visible list
     * @param expressionId ExpressionId to be removed from visible list
     */
	public void removeExressionIdFromVisibleList(IExpressionId expressionId)
	{
		m_visibleExpressions.remove(expressionId);
	}

	/**
     * Method to check if adding the path generates cyclic graph in the query
     * @param sourceExpressionId The expression ID of source node
	 * @param destExpressionId The expression ID of destination node
	 * @param path The path to added between source and destination node
	 * @return true if addition of this path generates cyclic query 
	 */
	public boolean isPathCreatesCyclicGraph(IExpressionId sourceExpressionId, IExpressionId destExpressionId, IPath path)
	{
		try 
		{
			List<IExpressionId> intermediateExpressionIds = addPath(sourceExpressionId, destExpressionId, path);
			if(0 == intermediateExpressionIds.size())
			{
				removeAssociation(sourceExpressionId, destExpressionId);
                IExpression sourceExpression = query.getConstraints().getExpression(sourceExpressionId);
                sourceExpression.removeOperand(destExpressionId);
			}
			else
			{
				for(int i=0; i<intermediateExpressionIds.size(); i++)
				{
					this.removeExpression(intermediateExpressionIds.get(i));
                }
			}
			return false;
		}
		catch (CyclicException e) 
		{
			// TODO Auto-generated catch block
			return true;
		}
	}
	
	public IExpressionId addExpression(EntityInterface entity)
	{
		IConstraintEntity constraintEntity = new ConstraintEntity(entity);
		IExpression expression = query.getConstraints().addExpression(constraintEntity);
		expression.setInView(true);
		return expression.getExpressionId();
	}

	
}
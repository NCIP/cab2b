package edu.wustl.common.querysuite.queryobject.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.util.Collections;

/**
 * @author Mandar Shidhore
 * @author chetan_patil
 * @version 1.0
 * @created 12-Oct-2006 13.35.04 AM
 * @updated Aug 10, 2007, 12:22:17 PM
 * 
 * @hibernate.class table="QUERY_CONSTRAINTS"
 * @hibernate.cache usage="read-write"
 */
public class Constraints extends BaseQueryObject implements IConstraints {
    private static final long serialVersionUID = 6169601255945564445L;

    private transient Map<Integer, IExpression> exprIdToExpr = new HashMap<Integer, IExpression>();

    private Set<IExpression> expressions = new HashSet<IExpression>();

    private JoinGraph joinGraph = new JoinGraph();

    private transient int currentExpressionId = 0;

    /**
     * Default Constructor
     */
    public Constraints() {

    }

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * 
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CONSTRAINT_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * Name: Abhishek Mehta Reviewer Name : Deepti Bug ID: 5661 Patch ID: 5661_4
     * See also: 1-7 Description : Making cascade all-delete-orphan from cascade
     * all
     */

    /**
     * This method returns the JoinGraph of this Constraint
     * 
     * @return the reference to joingraph.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getJoinGraph()
     * 
     * @hibernate.many-to-one column="QUERY_JOIN_GRAPH_ID" unique="true"
     *                        class="edu.wustl.common.querysuite.queryobject.impl.JoinGraph"
     *                        cascade="all-delete-orphan" lazy="false"
     */
    public IJoinGraph getJoinGraph() {
        return joinGraph;
    }

    /**
     * @return the root expression of the join graph.
     * @throws MultipleRootsException When there exists multiple roots in
     *             joingraph.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getRootExpression()
     */
    public IExpression getRootExpression() throws MultipleRootsException {
        return joinGraph.getRoot();
    }

    /**
     * @param constraintEntity the constraint Entity for which the new expr is
     *            created.
     * @return the newly created expression.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#addExpression(edu.wustl.common.querysuite.queryobject.IQueryEntity)
     */
    public IExpression addExpression(IQueryEntity constraintEntity) {
        IExpression expression = new Expression(constraintEntity, ++currentExpressionId);

        exprIdToExpr.put(expression.getExpressionId(), expression);
        expressions.add(expression);
        System.out.println("JJJ JoinGraph added? "+((JoinGraph) joinGraph).addIExpression(expression));
        
        System.out.println("JJJ %%AddedExpression "+expression.getExpressionId()+" queryEntity="+expression.getQueryEntity()+" #expressions="+expressions.size());
        
        System.out.println("JJJ %%Added--- exprIdToExpr size="+exprIdToExpr.size());		
        
        return expression;
    }

    /**
     * @param expression the id of the expression to be removed.
     * @return the removed expression.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#removeExpressionWithId(edu.wustl.common.querysuite.queryobject.IExpression)
     */
    public IExpression removeExpressionWithId(int expressionId) {
    	System.out.println("REMOVE expression id="+expressionId);
        JoinGraph theJoinGraph = (JoinGraph) joinGraph;
        IExpression expression = getExpression(expressionId);

        List<IExpression> parents = theJoinGraph.getParentList(expression);
        for (IExpression parentExpression : parents) {
            parentExpression.removeOperand(expression);
        }
        theJoinGraph.removeIExpression(expression);

        expressions.remove(expression);
        exprIdToExpr.remove(expressionId);
        return expression;
    }

    public void removeExpression(IExpression expr) {
    	System.out.println("REMOVE expression Entity="+expr.getQueryEntity());

        removeExpressionWithId(expr.getExpressionId());
    }

    /**
     * @param id the id (usually obtained from getExpressions)
     * @return the reference to the IExpression associatied with the given
     *         IExpression.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getExpression(edu.wustl.common.querysuite.queryobject.IExpression)
     */
    public IExpression getExpression(int id) {
        IExpression expr = exprIdToExpr.get(id);
        if (expr == null) {
            throw new IllegalArgumentException("expr with id " + id + " not present in constraints.");
        }
        return expr;
    }

    /**
     * TO get the Set of all ConstraintEntites present in the Constraints
     * object.
     * 
     * @return Set of all Constraint Entities.
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#getQueryEntities()
     */
    public ArrayList<IQueryEntity> getQueryEntities() {
//        Set<IQueryEntity> constraintEntitySet = new HashSet<IQueryEntity>();

        ArrayList<IQueryEntity> constraintEntitySet = new ArrayList<IQueryEntity>();
        for (IExpression expression : expressions) {
        	System.out.println("JJJ getQueryEntities.adding"+expression.getQueryEntity());
            constraintEntitySet.add(expression.getQueryEntity());
        }
    	System.out.println("JJJ returning"+constraintEntitySet.size());

        return constraintEntitySet;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IConstraints#iterator()
     */
    public Iterator<IExpression> iterator() {
        return Collections.removalForbiddenIterator(expressions);
    }

    public int size() {
        return expressions.size();
    }

    // for hibernate

    /**
     * @param joinGraph the joinGraph to set
     */
    @SuppressWarnings("unused")
    private void setJoinGraph(JoinGraph joinGraph) {
        this.joinGraph = joinGraph;
    }

    /**
     * This method returns the List of IExpression associated with this
     * Constraint. This method will only be used by Hibernate to save into the
     * database.
     * 
     * @return the expressionCollection
     * 
     * @hibernate.set name="expressionCollection" cascade="all-delete-orphan"
     *                inverse="false" lazy="false"
     * @hibernate.collection-key column="QUERY_CONSTRAINT_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.common.querysuite.queryobject.impl.Expression"
     * @hibernate.cache usage="read-write"
     */
    @SuppressWarnings("unused")
    private Set<IExpression> getExpressions() {
        return expressions;
    }

    /**
     * This method sets the List of IExpression associated with this Constraint.
     * This method will only be used by Hibernate to restore form the database.
     * 
     * @param expressionCollection the expressionCollection to set
     */
    @SuppressWarnings("unused")
    private void setExpressions(Set<IExpression> expressions) {
        if (expressions == null) {
            throw new NullPointerException();
        }
        this.expressions = expressions;
        populateExprIdMap();
    }

    private void populateExprIdMap() {
        exprIdToExpr = new HashMap<Integer, IExpression>();
        currentExpressionId = 0;
        for (IExpression expression : expressions) {
            exprIdToExpr.put(expression.getExpressionId(), expression);
            if(expression.getExpressionId() > currentExpressionId) {
                currentExpressionId = expression.getExpressionId(); 
            }
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        populateExprIdMap();
    }
}
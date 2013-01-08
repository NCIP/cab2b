/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * The IQuery implementation class.
 * 
 * @author Mandar Shidhore
 * @author chetan_patil
 * @version 1.0
 * @created 12-Oct-2006 15.07.04 AM
 * 
 * @hibernate.class table="QUERY"
 * @hibernate.cache usage="read-write"
 */
public class Query extends AbstractQuery implements IQuery {
    private static final long serialVersionUID = -9105109010866749580L;

    private IConstraints constraints;

    private List<IOutputTerm> outputTerms;

    /**
     * Default Constructor
     */
    public Query() {

    }
    /**
     * Parameterized Constructor
     * 
     * @param query
     */
    public Query(IQuery query) {
    	this.setDescription(query.getDescription());
    	this.setName(query.getName());
    	this.setOutputTerms(query.getOutputTerms());
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
     * Name: Abhishek Mehta Reviewer Name : Deepti Bug ID: 5661 Patch ID: 5661_5
     * See also: 1-7 Description : Making cascade all-delete-orphan from cascade
     * all
     */

    /**
     * @return the reference to constraints.
     * @see edu.wustl.common.querysuite.queryobject.IQuery#getConstraints()
     * 
     * @hibernate.many-to-one column="QUERY_CONSTRAINTS_ID"
     *                        class="edu.wustl.common.querysuite.queryobject.impl.Constraints"
     *                        unique="true" cascade="all-delete-orphan"
     *                        lazy="false"
     */
    public IConstraints getConstraints() {
        if (constraints == null) {
            constraints = new Constraints();
        }
    	System.out.println("JJJ %%%GETConstraints with "+constraints.getQueryEntities().size()+" queryEntities and "+constraints.size()+" constraints");

        return constraints;
    }

    /**
     * @param constraints the constraints to set.
     * @see edu.wustl.common.querysuite.queryobject.IQuery#setConstraints(edu.wustl.common.querysuite.queryobject.IConstraints)
     */
    public void setConstraints(IConstraints constraints) {
    	System.out.println("JJJ %%%setConstraints with "+constraints.getQueryEntities().size()+" queryEntities ");
        this.constraints = constraints;
    }

    public List<IOutputTerm> getOutputTerms() {
        if (outputTerms == null) {
            outputTerms = new ArrayList<IOutputTerm>();
        }
        return outputTerms;
    }

    // for hibernate
    @SuppressWarnings("unused")
    private void setOutputTerms(List<IOutputTerm> outputTerms) {
        this.outputTerms = outputTerms;
    }
}

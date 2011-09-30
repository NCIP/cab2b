package edu.wustl.common.querysuite.queryobject;

import java.util.List;


/**
 * The query object... this is the unit built from UI, persisted, and from which
 * queries will be built.
 * 
 * @version 1.0
 * @updated 11-Oct-2006 02:57:13 PM
 */
public interface IQuery extends IAbstractQuery {
    /**
     * @return the reference to constraints
     * @see IConstraints
     */
    IConstraints getConstraints();

    /**
     * To set the constraints object.
     * 
     * @param constraints the constraints to set.
     */
    void setConstraints(IConstraints constraints);

    List<IOutputTerm> getOutputTerms();
}

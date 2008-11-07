package edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints;

/**
 * @author 
 *
 */
public abstract class AbstractAssociationConstraint extends DcqlConstraint {

    /**
     * @param constraintType
     */
    protected AbstractAssociationConstraint(ConstraintType constraintType) {
        super(constraintType);
    }

    /**
     * Should be overridden by subclasses.
     * @param childConstraint
     *            the child constraint to add.
     */
    public abstract void addChildConstraint(DcqlConstraint childConstraint);
    
    /**
     * Abstract method
     * @see java.lang.Object#clone()
     */
    public abstract AbstractAssociationConstraint clone();
}

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints;

public abstract class AbstractAssociationConstraint extends DcqlConstraint {

    protected AbstractAssociationConstraint(ConstraintType constraintType) {
        super(constraintType);
    }

    /**
     * Should be overridden by subclasses.
     * @param childConstraint
     *            the child constraint to add.
     */
    public abstract void addChildConstraint(DcqlConstraint childConstraint);
    
    public abstract AbstractAssociationConstraint clone();
}

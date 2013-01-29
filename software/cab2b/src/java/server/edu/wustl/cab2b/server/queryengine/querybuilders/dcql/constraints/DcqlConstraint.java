/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints;


public class DcqlConstraint {
    private ConstraintType constraintType;

    private Object constraint;

    public enum ConstraintType {
        Any, Attribute, Group, LocalAssociation, ForeignAssociation
    }

    public DcqlConstraint() {
        setConstraintType(ConstraintType.Any);
    }

    protected DcqlConstraint(ConstraintType constraintType) {
        setConstraintType(constraintType);
    }

    /**
     * @return the constraintType.
     */
    public ConstraintType getConstraintType() {
        return constraintType;
    }

    /**
     * @param constraintType
     *            the constraintType to set.
     */
    private void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }

    /**
     * @return the constraint.
     */
    protected Object getConstraint() {
        if (constraintType == ConstraintType.Any) {
            throw new UnsupportedOperationException("constraint incompatible with constraintType.");
        }
        return constraint;
    }

    /**
     * @param constraint
     *            the constraint to set.
     */
    protected void setConstraint(Object constraint) {
        if (constraintType == ConstraintType.Any) {
            throw new UnsupportedOperationException("constraint incompatible with constraintType.");
        }
        this.constraint = constraint;
    }
}

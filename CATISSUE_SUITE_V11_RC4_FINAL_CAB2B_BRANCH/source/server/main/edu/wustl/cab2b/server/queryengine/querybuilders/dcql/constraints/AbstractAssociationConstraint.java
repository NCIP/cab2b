/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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

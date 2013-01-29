/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints;

import gov.nih.nci.cagrid.dcql.Association;

public class LocalAssociationConstraint extends AbstractAssociationConstraint {

    public LocalAssociationConstraint() {
        super(ConstraintType.LocalAssociation);
    }

    public LocalAssociationConstraint(Association association) {
        super(ConstraintType.LocalAssociation);
        setLocalAssociation(association);
    }

    public Association getLocalAssociation() {
        return (Association) getConstraint();
    }

    public void setLocalAssociation(Association association) {
        setConstraint(association);
    }

    @Override
    public void addChildConstraint(DcqlConstraint childConstraint) {
        switch (childConstraint.getConstraintType()) {
            case Attribute:
                getLocalAssociation().setAttribute(((AttributeConstraint) childConstraint).getAttribute());
                break;

            case Group:
                getLocalAssociation().setGroup(((GroupConstraint) childConstraint).getGroup());
                break;
            case LocalAssociation:
                getLocalAssociation().setAssociation(((LocalAssociationConstraint) childConstraint).getLocalAssociation());
                break;
            case ForeignAssociation:
                getLocalAssociation().setForeignAssociation(
                                                       ((ForeignAssociationConstraint) childConstraint).getForeignAssociation());
                break;
        }
    }

    @Override
    public AbstractAssociationConstraint clone() {
        return new LocalAssociationConstraint(getLocalAssociation());
    }
}

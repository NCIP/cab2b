/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints;

import gov.nih.nci.cagrid.dcql.ForeignAssociation;

public class ForeignAssociationConstraint extends AbstractAssociationConstraint {

    public ForeignAssociationConstraint() {
        super(ConstraintType.ForeignAssociation);
    }

    public ForeignAssociationConstraint(ForeignAssociation foreignAssociation) {
        super(ConstraintType.ForeignAssociation);
        setForeignAssociation(foreignAssociation);
    }

    public ForeignAssociation getForeignAssociation() {
        return (ForeignAssociation) getConstraint();
    }

    public void setForeignAssociation(ForeignAssociation foreignAssociation) {
        setConstraint(foreignAssociation);
    }

    @Override
    public void addChildConstraint(DcqlConstraint childConstraint) {
        switch (childConstraint.getConstraintType()) {
            case Attribute:
                getForeignAssociation().getForeignObject().setAttribute(
                                                                        ((AttributeConstraint) childConstraint).getAttribute());
                break;

            case Group:
                getForeignAssociation().getForeignObject().setGroup(((GroupConstraint) childConstraint).getGroup());
                break;
            case LocalAssociation:
                getForeignAssociation().getForeignObject().setAssociation(
                                                                          ((LocalAssociationConstraint) childConstraint).getLocalAssociation());
                break;
            case ForeignAssociation:
                getForeignAssociation().getForeignObject().setForeignAssociation(
                                                                                 ((ForeignAssociationConstraint) childConstraint).getForeignAssociation());
                break;
        }
    }

    @Override
    public AbstractAssociationConstraint clone() {
        return new ForeignAssociationConstraint(getForeignAssociation());
    }

}

/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints;

import gov.nih.nci.cagrid.cqlquery.Attribute;

public class AttributeConstraint extends DcqlConstraint {

    public AttributeConstraint() {
        super(ConstraintType.Attribute);
    }
    
    public AttributeConstraint(Attribute attribute) {
        super(ConstraintType.Attribute);
        setAttribute(attribute);
    }

    public Attribute getAttribute() {
        return (Attribute) getConstraint();
    }

    public void setAttribute(Attribute attribute) {
        setConstraint(attribute);
    }

}

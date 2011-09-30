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

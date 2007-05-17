package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.AttributeConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.ForeignAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.GroupConstraint;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.LocalAssociationConstraint;
import edu.wustl.cab2b.server.queryengine.utils.TransformerUtil;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.dcql.Association;
import gov.nih.nci.cagrid.dcql.ForeignAssociation;
import gov.nih.nci.cagrid.dcql.Group;

class ImmutableGroup {
    private Group group;

    ImmutableGroup(Group group) {
        this.group = group;
    }

    Group getGroup() {
        return group;
    }

    void setLogicRelation(LogicalOperator logicalOperator) {
        group.setLogicRelation(TransformerUtil.getCqlLogicalOperator(logicalOperator));
    }
}

public class MyGroup extends ImmutableGroup {
    private List<Association> associations;

    private List<Attribute> attributes;

    private List<ImmutableGroup> myGroups;

    private List<ForeignAssociation> foreignAssociations;

    private int numElements;

    public MyGroup() {
        super(new Group());
        // this.group = Group.Factory.newInstance();
        // this.mutable = true;
        associations = new ArrayList<Association>();
        attributes = new ArrayList<Attribute>();
        myGroups = new ArrayList<ImmutableGroup>();
        foreignAssociations = new ArrayList<ForeignAssociation>();
        numElements = 0;
    }

    public MyGroup(LogicalOperator logicalOperator) {
        this();
        setLogicRelation(logicalOperator);
    }

    // private void aboutToMutate() {
    // if (!mutable) {
    // throw new UnsupportedOperationException();
    // }
    // }

    void incNumElems() {
        ++numElements;
    }

    void addAssociation(Association association) {
        // aboutToMutate();
        associations.add(association);
        incNumElems();
    }

    void addAttribute(Attribute attribute) {
        // aboutToMutate();
        attributes.add(attribute);
        incNumElems();
    }

    void addGroup(Group group) {
        // aboutToMutate();
        myGroups.add(new ImmutableGroup(group));
        incNumElems();
    }

    MyGroup addGroup() {
        // aboutToMutate();
        MyGroup newGroup = new MyGroup();
        myGroups.add(newGroup);
        incNumElems();
        return newGroup;
    }

    void addForeignAssociation(ForeignAssociation foreignAssociation) {
        // aboutToMutate();
        foreignAssociations.add(foreignAssociation);
        incNumElems();
    }

    public void addConstraint(DcqlConstraint constraint) {
        switch (constraint.getConstraintType()) {
            case Attribute:
                AttributeConstraint attributeConstraint = (AttributeConstraint) constraint;
                addAttribute(attributeConstraint.getAttribute());
                break;

            case LocalAssociation:
                LocalAssociationConstraint localAssociationConstraint = (LocalAssociationConstraint) constraint;
                addAssociation(localAssociationConstraint.getLocalAssociation());
                break;

            case ForeignAssociation:
                ForeignAssociationConstraint foreignAssociationConstraint = (ForeignAssociationConstraint) constraint;
                addForeignAssociation(foreignAssociationConstraint.getForeignAssociation());
                break;

            case Group:
                GroupConstraint groupConstraint = (GroupConstraint) constraint;
                addGroup(groupConstraint.getGroup());
                break;
        }
    }

    /**
     * Call if you're sure that the group will contain more than one element.
     * @return the Group.
     */
    @Override
    Group getGroup() {
        Group group = super.getGroup();
        group.setAssociation(associations.toArray(new Association[0]));
        group.setAttribute((attributes.toArray(new Attribute[0])));
        group.setForeignAssociation((foreignAssociations.toArray(new ForeignAssociation[0])));
        List<Group> groupList = new ArrayList<Group>(myGroups.size());
        for (ImmutableGroup myGroup : myGroups) {
            groupList.add(myGroup.getGroup());
        }
        group.setGroup((groupList.toArray(new Group[0])));
        return group;
    }

    /**
     * Call if you're not sure about the contents/size of the group.
     * @return appropriate constraint.
     */
    public DcqlConstraint getDcqlConstraint() {
        if (this.numElements > 1) {
            return new GroupConstraint(getGroup());
        }
        if (associations.size() == 1) {
            return new LocalAssociationConstraint(associations.get(0));
        }
        if (attributes.size() == 1) {
            return new AttributeConstraint(attributes.get(0));
        }
        if (foreignAssociations.size() == 1) {
            return new ForeignAssociationConstraint(foreignAssociations.get(0));
        }
        if (myGroups.size() == 1) {
            return new GroupConstraint(myGroups.get(0).getGroup());
        }
        return new DcqlConstraint();
    }
}
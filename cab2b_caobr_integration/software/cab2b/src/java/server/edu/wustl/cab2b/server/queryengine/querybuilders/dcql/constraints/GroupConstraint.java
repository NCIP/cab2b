/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints;

import gov.nih.nci.cagrid.dcql.Group;

public class GroupConstraint extends DcqlConstraint {

    public GroupConstraint() {
        super(ConstraintType.Group);
    }
    
    public GroupConstraint(Group group) {
        super(ConstraintType.Group);
        setGroup(group);
    }
    
    public Group getGroup() {
        return (Group) getConstraint();
    }

    public void setGroup(Group group) {
        setConstraint(group);
    }
    
}

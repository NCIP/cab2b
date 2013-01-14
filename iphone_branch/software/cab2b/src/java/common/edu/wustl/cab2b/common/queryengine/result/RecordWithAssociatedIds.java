/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public class RecordWithAssociatedIds extends Record implements IRecordWithAssociatedIds {

    private static final long serialVersionUID = 1941857218196149238L;

    private Map<AssociationInterface, List<String>> associatedClassesIdentifiers;

    protected RecordWithAssociatedIds(Set<AttributeInterface> attributes, RecordId id) {
        super(attributes, id);
        associatedClassesIdentifiers = new HashMap<AssociationInterface, List<String>>();
    }

    public Map<AssociationInterface, List<String>> getAssociatedClassesIdentifiers() {
        return associatedClassesIdentifiers;
    }

}

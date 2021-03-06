/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;

/**
 * Represents a record that can have associated entities' identifiers.
 * @author srinath_k
 */
public interface IRecordWithAssociatedIds extends IRecord {
    /**
     * @return a mapping of the association to the corresponding {@link List} of
     *         identifiers for the associated entity.
     */
    Map<AssociationInterface, List<String>> getAssociatedClassesIdentifiers();
}

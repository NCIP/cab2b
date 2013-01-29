/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package cab2b.common.caarray;

import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.queryengine.result.RecordWithAssociatedIds;

public class CaArrayRecord extends RecordWithAssociatedIds implements ICaArrayRecord {

    private static final long serialVersionUID = -7864369803132187094L;

    public CaArrayRecord(Set<AttributeInterface> attributes, RecordId id) {
        super(attributes, id);
    }
}

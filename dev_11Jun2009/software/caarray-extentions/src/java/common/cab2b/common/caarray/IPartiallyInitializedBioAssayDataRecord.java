/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package cab2b.common.caarray;

import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitialized3DRecord;

public interface IPartiallyInitializedBioAssayDataRecord
        extends
        ICaArrayRecord,
        IPartiallyInitialized3DRecord<IPartiallyInitializedBioAssayDataRecord, IFullyInitializedBioAssayDataRecord> {

}

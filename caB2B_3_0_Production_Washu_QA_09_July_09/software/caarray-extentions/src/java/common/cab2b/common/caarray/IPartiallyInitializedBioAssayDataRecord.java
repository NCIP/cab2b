package cab2b.common.caarray;

import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitialized3DRecord;

public interface IPartiallyInitializedBioAssayDataRecord
        extends
        ICaArrayRecord,
        IPartiallyInitialized3DRecord<IPartiallyInitializedBioAssayDataRecord, IFullyInitializedBioAssayDataRecord> {

}

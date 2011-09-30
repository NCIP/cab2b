package cab2b.common.caarray;

import edu.wustl.cab2b.common.queryengine.result.IFullyInitialized3DRecord;

public interface IFullyInitializedBioAssayDataRecord extends ICaArrayRecord,
        IFullyInitialized3DRecord<IPartiallyInitializedBioAssayDataRecord, IFullyInitializedBioAssayDataRecord> {

}

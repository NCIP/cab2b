package edu.wustl.cab2b.common.queryengine.result;

public interface IPartiallyInitialized3DRecord<P extends IPartiallyInitialized3DRecord<P, F>, F extends IFullyInitialized3DRecord<P, F>>
        extends I3DDataRecord, IPartiallyInitializedRecord<P, F> {

}

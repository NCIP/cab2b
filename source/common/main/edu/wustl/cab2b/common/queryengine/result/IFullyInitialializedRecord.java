package edu.wustl.cab2b.common.queryengine.result;

public interface IFullyInitialializedRecord<P extends IPartiallyInitializedRecord<P, F>, F extends IFullyInitialializedRecord<P, F>>
        extends ILazilyInitializableRecord {
    P view(ILazyParams params);
}

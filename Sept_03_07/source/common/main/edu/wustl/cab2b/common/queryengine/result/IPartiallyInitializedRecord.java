package edu.wustl.cab2b.common.queryengine.result;

public interface IPartiallyInitializedRecord<P extends IPartiallyInitializedRecord<P, F>, F extends IFullyInitialializedRecord<P, F>>
        extends ILazilyInitializableRecord {
    int handle();

    ILazyParams initializationParams();
}

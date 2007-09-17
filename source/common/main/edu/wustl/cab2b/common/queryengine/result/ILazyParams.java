package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;

/**
 * Marker interface that represents the parameters that are needed to lazily
 * intialize a record.
 * 
 * @author srinath_k
 * @see IFullyInitialializedRecord#view(ILazyParams, int)
 */
public interface ILazyParams extends Serializable {

}

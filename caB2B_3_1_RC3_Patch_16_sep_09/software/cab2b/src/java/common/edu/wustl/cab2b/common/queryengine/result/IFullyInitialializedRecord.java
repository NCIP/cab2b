package edu.wustl.cab2b.common.queryengine.result;

/**
 * Specifies operations to be supported by a fully initialized record.
 * 
 * @author srinath_k
 * 
 * @param
 * <P>
 * the partially initialized record corresponding to this fully initialized
 * record.
 * @param <F> the actual type of this fully initialized record. Note: These two
 *            params imply that there is always a pair of <full rec, partial
 *            rec>.
 */
public interface IFullyInitialializedRecord<P extends IPartiallyInitializedRecord<P, F>, F extends IFullyInitialializedRecord<P, F>>
        extends ILazilyInitializableRecord {
    /**
     * Creates the partially initialized record corresponding to the lazy params
     * provided.
     * 
     * @param params the parameters that specify the desired view.
     * @param handle the handle with which this full record has been registered
     *            in {@link edu.wustl.cab2b.server.queryengine.LazyInitializer}.
     * @return the partially initialized record corresponding to the lazy params
     *         provided.
     * @throws IllegalArgumentException if the params is not "understood" by
     *             this record.
     */
    P view(ILazyParams params, int handle);
}

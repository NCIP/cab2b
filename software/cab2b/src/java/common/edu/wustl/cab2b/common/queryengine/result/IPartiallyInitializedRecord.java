/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

/**
 * @author srinath_k
 * 
 * @param
 * <P>
 * the actual type of this partially initialized record
 * @param <F> The fully initialized record corresponding to this partially
 *            initialized record. Note: These two params imply that there is
 *            always a pair of <full rec, partial rec>.
 */
public interface IPartiallyInitializedRecord<P extends IPartiallyInitializedRecord<P, F>, F extends IFullyInitialializedRecord<P, F>>
        extends ILazilyInitializableRecord {
    /**
     * @return the handle to the fully initialized record.
     */
    int handle();

    /**
     * @return the params with which this record was created.
     */
    ILazyParams initializationParams();
}

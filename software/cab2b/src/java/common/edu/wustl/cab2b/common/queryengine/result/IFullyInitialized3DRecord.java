/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

public interface IFullyInitialized3DRecord<P extends IPartiallyInitialized3DRecord<P, F>, F extends IFullyInitialized3DRecord<P, F>> extends I3DDataRecord,
        IFullyInitialializedRecord<P, F> {

}

/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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

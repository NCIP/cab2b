/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.analyticalservice;

import java.util.List;

import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * @author Chandrakant Talele
 */
public interface ServiceInvokerInterface {
    /**
     * @param data
     * @param serviceParamSet
     * @return
     */
    List<IRecord> invokeService(List<IRecord> data, List<IRecord> serviceParamSet);

}

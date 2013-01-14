/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.ejb.analyticalservice;

import java.rmi.RemoteException;
import java.util.List;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * @author Chandrakant Talele
 */
public interface AnalyticalServiceOperationsBusinessInterface extends BusinessInterface {
		
    /**
     * Returns all services for an Entity
     * @param entityId
     * @return List of services for an Entity
     * @throws RemoteException
     */
    List<ServiceDetailsInterface> getApplicableAnalyticalServices(Long entityId) throws RemoteException;;

    /**
     * Invokes a analytical service and return results in a {@link edu.wustl.cab2b.common.queryengine.result.IRecord}
     * @param serviceDetails 
     * @param data input data for service 
     * @param serviceParamSet parameters used for service
     * @return Result after execution of service
     * @throws RemoteException
     */
    List<IRecord> invokeService(ServiceDetailsInterface serviceDetails, List<IRecord> data,
                                List<IRecord> serviceParamList) throws RemoteException;

}

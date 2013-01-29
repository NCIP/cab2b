/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.analyticalservice;

import java.rmi.RemoteException;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;
import edu.wustl.cab2b.common.ejb.analyticalservice.AnalyticalServiceOperationsBusinessInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.analyticalservice.AnalyticalServiceOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;

/**
 * @author Chandrakant Talele
 */
public class AnalyticalServiceOperationsBean extends AbstractStatelessSessionBean implements
        AnalyticalServiceOperationsBusinessInterface {

    private static final long serialVersionUID = 6783481698510254878L;

    /**
     * Returns all services for an Entity
     * @param entityId
     * @return List of services for an Entity
     * @throws RemoteException
     * @see edu.wustl.cab2b.common.ejb.analyticalservice.ServiceInvokerFactoryBusinessInterface#getApplicableAnalyticalServices(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public List<ServiceDetailsInterface> getApplicableAnalyticalServices(Long entityId)
            throws RemoteException {
        return new AnalyticalServiceOperations().getApplicableAnalyticalServices(entityId);
    }

    /**
     * Invokes a analytical service and return results in a {@link edu.wustl.cab2b.common.queryengine.result.IRecord}
     * @param serviceDetails 
     * @param data input data for service 
     * @param serviceParamList parameters used for service
     * @return Result after execution of service
     * @throws RemoteException
     * 
     * @see edu.wustl.cab2b.common.ejb.analyticalservice.ServiceInvokerFactoryBusinessInterface#invokeService(ServiceDetailsInterface, List, List)
     */
    public List<IRecord> invokeService(ServiceDetailsInterface serviceDetails, List<IRecord> data,
                                       List<IRecord> serviceParamList) throws RemoteException {
        return new AnalyticalServiceOperations().invokeService(serviceDetails, data, serviceParamList);
    }
}
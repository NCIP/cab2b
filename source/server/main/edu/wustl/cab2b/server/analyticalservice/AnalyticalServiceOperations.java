package edu.wustl.cab2b.server.analyticalservice;

import java.rmi.RemoteException;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.cache.EntityCache;

/**
 * @author Chandrakant Talele
 */
public class AnalyticalServiceOperations {
	
    /**
     * Returns all services for an Entity
     * @param entityId
     * @return List of services for an Entity
     * @throws RemoteException
     */
    public List<ServiceDetailsInterface> getApplicableAnalyticalServices(Long entityId) {
        EntityCache entityCache = EntityCache.getInstance();
        EntityInterface entity = entityCache.getEntityById(entityId);
        
        EntityToAnalyticalServiceMapper entityServiceMapper = EntityToAnalyticalServiceMapper.getInstance();
        List<ServiceDetailsInterface> services = entityServiceMapper.getServices(entity);
        return services;
    }

    /**
     * Invokes a analytical service and return results in a {@link edu.wustl.cab2b.common.queryengine.result.IRecord}
     * @param serviceDetails 
     * @param data input data for service 
     * @param serviceParamSet parameters used for service
     * @return Result after execution of service
     * @throws RemoteException
     */
    public List<IRecord> invokeService(ServiceDetailsInterface serviceDetails, List<IRecord> data,
                                       List<IRecord> serviceParamList) {
        EntityToAnalyticalServiceMapper entityServiceMapper = EntityToAnalyticalServiceMapper.getInstance();
        ServiceInvokerInterface serviceInvoker = entityServiceMapper.getServiceInvoker(serviceDetails);
        return serviceInvoker.invokeService(data, serviceParamList);
    }
}

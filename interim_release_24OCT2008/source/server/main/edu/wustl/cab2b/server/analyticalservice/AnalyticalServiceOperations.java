package edu.wustl.cab2b.server.analyticalservice;

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
     * @param entityId
     * @return
     */
    public List<ServiceDetailsInterface> getApplicableAnalyticalServices(Long entityId) {
        EntityCache entityCache = EntityCache.getInstance();
        EntityInterface entity = entityCache.getEntityById(entityId);
        
        EntityToAnalyticalServiceMapper entityServiceMapper = EntityToAnalyticalServiceMapper.getInstance();
        List<ServiceDetailsInterface> services = entityServiceMapper.getServices(entity);
        return services;
    }

    /**
     * @param serviceDetails
     * @param data
     * @param serviceParamSet
     * @return
     */
    public List<IRecord> invokeService(ServiceDetailsInterface serviceDetails, List<IRecord> data,
                                       List<IRecord> serviceParamList) {
        EntityToAnalyticalServiceMapper entityServiceMapper = EntityToAnalyticalServiceMapper.getInstance();
        ServiceInvokerInterface serviceInvoker = entityServiceMapper.getServiceInvoker(serviceDetails);
        return serviceInvoker.invokeService(data, serviceParamList);
    }
}

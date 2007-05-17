package edu.wustl.cab2b.server.analyticalservice;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;

/**
 * @author Chandrakant Talele
 */
public class AnalyticalServiceOperations {
    /**
     * @param entity
     * @return
     */
    public List<ServiceDetailsInterface> getApplicableAnalyticalServices(EntityInterface entity) {
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
    public EntityRecordResultInterface invokeService(ServiceDetailsInterface serviceDetails,
                                                     List<EntityRecordResultInterface> data,
                                                     List<EntityRecordResultInterface> serviceParamSet) {
        EntityToAnalyticalServiceMapper entityServiceMapper = EntityToAnalyticalServiceMapper.getInstance();
        ServiceInvokerInterface serviceInvoker = entityServiceMapper.getServiceInvoker(serviceDetails);
        EntityRecordResultInterface res = serviceInvoker.invokeService(data, serviceParamSet);
        return res;
    }
}

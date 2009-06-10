package edu.wustl.cab2b.server.ejb.analyticalservice;

import java.rmi.RemoteException;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;
import edu.wustl.cab2b.common.ejb.analyticalservice.AnalyticalServiceOperationsBusinessInterface;
import edu.wustl.cab2b.server.analyticalservice.AnalyticalServiceOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;

/**
 * @author Chandrakant Talele
 */
public class AnalyticalServiceOperationsBean extends AbstractStatelessSessionBean implements
        AnalyticalServiceOperationsBusinessInterface {

    private static final long serialVersionUID = 6783481698510254878L;

    /**
     * @see edu.wustl.cab2b.common.ejb.analyticalservice.ServiceInvokerFactoryBusinessInterface#getApplicableAnalyticalServices(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public List<ServiceDetailsInterface> getApplicableAnalyticalServices(EntityInterface entity)
            throws RemoteException {
        return new AnalyticalServiceOperations().getApplicableAnalyticalServices(entity);
    }

    /**
     * @see edu.wustl.cab2b.common.ejb.analyticalservice.ServiceInvokerFactoryBusinessInterface#invokeService(ServiceDetailsInterface, List, List)
     */
    public EntityRecordResultInterface invokeService(ServiceDetailsInterface serviceDetails,
                                                     List<EntityRecordResultInterface> data,
                                                     List<EntityRecordResultInterface> serviceParamSet)
            throws RemoteException {
        return new AnalyticalServiceOperations().invokeService(serviceDetails, data, serviceParamSet);
    }
}
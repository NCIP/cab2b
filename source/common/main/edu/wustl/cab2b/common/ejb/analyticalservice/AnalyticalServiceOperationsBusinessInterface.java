package edu.wustl.cab2b.common.ejb.analyticalservice;

import java.rmi.RemoteException;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;

/**
 * @author Chandrakant Talele
 */
public interface AnalyticalServiceOperationsBusinessInterface {
    /**
     * @param entity
     * @return
     * @throws RemoteException
     */
    List<ServiceDetailsInterface> getApplicableAnalyticalServices(EntityInterface entity) throws RemoteException;;

    /**
     * @param data
     * @param serviceParamSet
     * @return
     * @throws RemoteException
     */
    EntityRecordResultInterface invokeService(ServiceDetailsInterface serviceDetails,
                                                     List<EntityRecordResultInterface> data,
                                                     List<EntityRecordResultInterface> serviceParamSet)
            throws RemoteException;

}

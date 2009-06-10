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
     * @param entity
     * @return
     * @throws RemoteException
     */
    List<ServiceDetailsInterface> getApplicableAnalyticalServices(Long entityId) throws RemoteException;;

    /**
     * @param data
     * @param serviceParamSet
     * @return
     * @throws RemoteException
     */
    List<IRecord> invokeService(ServiceDetailsInterface serviceDetails, List<IRecord> data,
                                List<IRecord> serviceParamList) throws RemoteException;

}

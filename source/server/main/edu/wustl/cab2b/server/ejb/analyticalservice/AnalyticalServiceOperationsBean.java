package edu.wustl.cab2b.server.ejb.analyticalservice;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;
import edu.wustl.cab2b.common.ejb.analyticalservice.AnalyticalServiceOperationsBusinessInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.analyticalservice.AnalyticalServiceOperations;

/**
 * @author Chandrakant Talele
 * 
 * 
 */

@Remote(AnalyticalServiceOperationsBusinessInterface.class)
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AnalyticalServiceOperationsBean implements AnalyticalServiceOperationsBusinessInterface {

    private static final long serialVersionUID = 6783481698510254878L;

    /**
     * @see edu.wustl.cab2b.common.ejb.analyticalservice.ServiceInvokerFactoryBusinessInterface#getApplicableAnalyticalServices(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public List<ServiceDetailsInterface> getApplicableAnalyticalServices(Long entityId) throws RemoteException {
        return new AnalyticalServiceOperations().getApplicableAnalyticalServices(entityId);
    }

    /**
     * @see edu.wustl.cab2b.common.ejb.analyticalservice.ServiceInvokerFactoryBusinessInterface#invokeService(ServiceDetailsInterface,
     *      List, List)
     */
    public List<IRecord> invokeService(ServiceDetailsInterface serviceDetails, List<IRecord> data,
                                       List<IRecord> serviceParamList) throws RemoteException {
        return new AnalyticalServiceOperations().invokeService(serviceDetails, data, serviceParamList);
    }
}
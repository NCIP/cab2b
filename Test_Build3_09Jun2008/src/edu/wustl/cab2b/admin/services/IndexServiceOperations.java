package edu.wustl.cab2b.admin.services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import edu.wustl.cab2b.admin.util.ServerProperties;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

/**
 * Handles all index service operations
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class IndexServiceOperations {

    /**
     * @param names of entity group for which service instances are needed
     * @return array of EPRs for given entity group
     * @throws MalformedURIException in case of index service url not correct
     * @throws RemoteException 
     */
    public EndpointReferenceType[] getServicesByNames(final String name) throws MalformedURIException,
            RemoteException {

        final List<EndpointReferenceType> list = new ArrayList<EndpointReferenceType>();
        String[] urls = ServerProperties.getIndexServiceUrls();
        for (String url : urls) {

            DiscoveryClient client = new DiscoveryClient(url);
            EndpointReferenceType[] endpointReferenceType = null;
            try {
                // endpointReferenceType = client.getAllDataServices();
                endpointReferenceType = client.discoverDataServicesByDomainModel(name);
            } catch (RemoteResourcePropertyRetrievalException e) {
                Logger.out.error(e.getMessage(), e);
                throw new RemoteException(e.getMessage());
            } catch (QueryInvalidException e) {
                Logger.out.error(e.getMessage(), e);
                throw new RemoteException(e.getMessage());
            } catch (ResourcePropertyRetrievalException e) {
                Logger.out.error(e.getMessage(), e);
                throw new RemoteException(e.getMessage());
            }
            if (endpointReferenceType == null) {
                return new EndpointReferenceType[0];
            }

            //There have been times when we got the EPRs null. So this check
            for (EndpointReferenceType referenceType : endpointReferenceType) {
                if (referenceType != null) {
                    list.add(referenceType);
                }
            }
        }
        return list.toArray(new EndpointReferenceType[0]);
    }

}

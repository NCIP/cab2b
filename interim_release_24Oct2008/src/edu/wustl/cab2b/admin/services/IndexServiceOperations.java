package edu.wustl.cab2b.admin.services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

import edu.wustl.cab2b.admin.util.ServerProperties;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

/**
 * Handles all index service operations
 * @author hrishikesh_rajpathak
 */
public class IndexServiceOperations {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(IndexServiceOperations.class);
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
                endpointReferenceType = client.discoverDataServicesByDomainModel(name);
            } catch (ResourcePropertyRetrievalException e) {
                logger.error(e.getMessage(), e);
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

/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.serviceurl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

import edu.wustl.cab2b.common.util.CommonPropertyLoader;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

/**
 * Handles all index service operations
 * @author hrishikesh_rajpathak
 */
public class IndexServiceOperations {
    private final static Logger logger = Logger.getLogger(IndexServiceOperations.class);

    /**
     * @param names of entity group for which service instances are needed
     * @return array of EPRs for given entity group
     * @throws MalformedURIException in case of index service url not correct
     * @throws RemoteException 
     */
    public Map<String, ServiceMetadata> getServicesByNames(final String name, String version)
            throws MalformedURIException, RemoteException {
        final Map<String, ServiceMetadata> serviceURLTometadata = new HashMap<String, ServiceMetadata>();
        String[] urls = CommonPropertyLoader.getIndexServiceUrls();
        for (String url : urls) {
            DiscoveryClientForMetaData client = new DiscoveryClientForMetaData(url);
            try {
                serviceURLTometadata.putAll(client.getServiceMetadataByDomainNameVersion(name, version));
            } catch (ResourcePropertyRetrievalException e) {
                logger.error(e.getMessage(), e);
                throw new RemoteException(e.getMessage());
            }
        }

        return serviceURLTometadata;
    }

}

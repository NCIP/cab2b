package edu.wustl.cab2b.admin.services;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import edu.wustl.cab2b.admin.util.Cab2bConstants;
import edu.wustl.cab2b.admin.util.PropertyLoader;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

/**
 * @author hrishikesh_rajpathak
 * 
 */
public class IndexServiceOperations {

    public IndexServiceOperations() {

    }

    /**
     * @param names
     * @return
     * @throws MalformedURIException
     */
    public EndpointReferenceType[] getServicesByNames(final String name) throws MalformedURIException {

        final List<EndpointReferenceType> list = new ArrayList<EndpointReferenceType>();

        String str = PropertyLoader.getPropertyValue(Cab2bConstants.PROPERTY_FILENAME, Cab2bConstants.INDEX_URL);
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        int count = tokenizer.countTokens();
        String[] urls = new String[count];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            urls[i] = tokenizer.nextToken();
            i++;
        }
        for (String url : urls) {

            DiscoveryClient client = new DiscoveryClient(url);
            EndpointReferenceType[] endpointReferenceType = null;
            try {
                // endpointReferenceType = client.getAllDataServices();
                endpointReferenceType = client.discoverDataServicesByDomainModel(name);
            } catch (RemoteResourcePropertyRetrievalException e) {
                e.printStackTrace();
            } catch (QueryInvalidException e) {
                e.printStackTrace();
            } catch (ResourcePropertyRetrievalException e) {
                e.printStackTrace();
            }
            if (endpointReferenceType == null) {
                return new EndpointReferenceType[0];
            }
            for (EndpointReferenceType referenceType : endpointReferenceType) {
                if (referenceType != null) {
                    list.add(referenceType);
                }
            }
        }

        return list.toArray(new EndpointReferenceType[0]);
    }

}

package edu.wustl.cab2b.server.serviceurl;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.XPathUtils;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;

/**
 * This class is the cab2b wrapper on the DiscoveryClient for to improve the query performance 
 * This class allows user to query on the index service dump for the service metadata.    
 * @author atul_jawale
 *
 */
public class DiscoveryClientForMetaData extends DiscoveryClient {

    //Variable required to map the namespace for the Address tag
    public static String add = "add";

    /*
     * Overriding the constructor to add new namespace for the
     * Address tag
     */
    public DiscoveryClientForMetaData(EndpointReferenceType indexEPR) {
        super(indexEPR);
        nsMap.put(add, "http://schemas.xmlsoap.org/ws/2004/03/addressing");
    }

    /*
     * Overriding the constructor to add new namespace for the
     * Address tag
     */
    public DiscoveryClientForMetaData(String indexurl) throws MalformedURIException {
        super(indexurl);
        nsMap.put(add, "http://schemas.xmlsoap.org/ws/2004/03/addressing");
    }

    /**
     * This method take service name and version and query to the index service for the 
     * service metadata. 
     * @param name
     * @param version
     * @return serviceURL to ServiceMetadata Map 
     * @throws RemoteResourcePropertyRetrievalException
     * @throws QueryInvalidException
     * @throws ResourcePropertyRetrievalException
     */
    public Map<String, ServiceMetadata> getServiceMetadataByDomainNameVersion(String name, String version)
            throws RemoteResourcePropertyRetrievalException, QueryInvalidException,
            ResourcePropertyRetrievalException {

        Map<String, ServiceMetadata> urlTometadata = new HashMap<String, ServiceMetadata>();
        //Get the service url first
        EndpointReferenceType[] endPointRefTypeArray = discoverDataServicesByDomainModel(name, version);
        if (endPointRefTypeArray != null) {
            for (EndpointReferenceType endPoint : endPointRefTypeArray) {
                String serviceURL = endPoint.getAddress().toString();
                // Now search for metadata for this url
                ServiceMetadata metadata = discoverDataServiceByURL(serviceURL);
                urlTometadata.put(serviceURL, metadata);
            }

        }
        return urlTometadata;
    }

    /**
     * 
     * @param url
     * @return
     * @throws RemoteResourcePropertyRetrievalException
     * @throws QueryInvalidException
     * @throws ResourcePropertyRetrievalException
     */
    public ServiceMetadata discoverDataServiceByURL(String url) throws RemoteResourcePropertyRetrievalException,
            QueryInvalidException, ResourcePropertyRetrievalException {

        String queryString = "wssg:MemberServiceEPR/add:Address/text() ='" + url + "' ";
        return discoverByFilterForMetadata(queryString);
    }

    /**
     * Applies the specified predicate to the common path in the Index Service's
     * Resource Properties to return registered services' EPRs that match the
     * predicate.
     * @param xpathPredicate
     * @return
     * @throws RemoteResourcePropertyRetrievalException
     * @throws QueryInvalidException
     * @throws ResourcePropertyRetrievalException
     */

    protected ServiceMetadata discoverByFilterForMetadata(String xpathPredicate)
            throws RemoteResourcePropertyRetrievalException, QueryInvalidException,
            ResourcePropertyRetrievalException {
        ServiceMetadata results = null;

        // query the service and deser the results
        MessageElement[] elements = ResourcePropertyHelper.queryResourceProperties(
                                                                                   this.indexEPR,
                                                                                   translateXPathForURL(xpathPredicate));
        Object objects[] = null;
        try {
            objects = ObjectDeserializer.toObject(elements, ServiceMetadata.class);
        } catch (DeserializationException e) {
            throw new ResourcePropertyRetrievalException("Unable to deserialize results to EPRs!", e);
        }

        // if we got results, cast them into what we are expected to return
        if (objects != null) {
            results = (ServiceMetadata) objects[0];

        }
        return results;

    }

    /**
     * Adds the common Index RP Entry filter, and translates the xpath to
     * IndexService friendly XPath 
     * @param xpathPredicate
     * @return
     */
    protected String translateXPathForURL(String xpathPredicate) {
        String xpath = "/*/" + wssg + ":Entry[" + xpathPredicate
                + "]/wssg:Content/agg:AggregatorData/cagrid:ServiceMetadata";
        LOG.debug("Querying for: " + xpath);

        String translatedxpath = XPathUtils.translateXPath(xpath, nsMap);
        LOG.debug("Issuing actual query: " + translatedxpath);

        return translatedxpath;
    }

    /**
      * Searches to find data services that are exposing a subset of given domain
      * (short name or long name).
      * 
      * @param modelName
      *            The model to look for
      * @return EndpointReferenceType[] matching the criteria
      * @throws ResourcePropertyRetrievalException
      * @throws QueryInvalidException
      * @throws RemoteResourcePropertyRetrievalException
      */
    public EndpointReferenceType[] discoverDataServicesByDomainModel(String modelName, String version)
            throws RemoteResourcePropertyRetrievalException, QueryInvalidException,
            ResourcePropertyRetrievalException {
        String queryString = DATA_MD_PATH + "[@projectShortName='" + modelName + "' or @projectLongName='"
                + modelName + "']";
        if (version != null && version.length() > 0) {
            queryString = DATA_MD_PATH + "[@projectShortName='" + modelName + "' or @projectLongName='"
                    + modelName + "'] and " + DATA_MD_PATH + "/@projectVersion='" + version + "'";
        }
        return discoverByFilter(queryString);
    }

    public static void main(String[] args) {
        DiscoveryClientForMetaData client;
        try {
            client = new DiscoveryClientForMetaData(
                    "http://cagrid-index.nci.nih.gov:8080/wsrf/services/DefaultIndexService");

            client.getServiceMetadataByDomainNameVersion("caArray", "2");

        } catch (RemoteResourcePropertyRetrievalException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (QueryInvalidException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ResourcePropertyRetrievalException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (MalformedURIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

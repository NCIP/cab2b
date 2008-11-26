package edu.wustl.cab2b.server.serviceurl;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;

import java.rmi.RemoteException;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

public class IndexServiceOperationsTest extends TestCase {

    public void testGetServicesByNames() {
        IndexServiceOperations indexOperations = new IndexServiceOperations();
        Map<String, ServiceMetadata> eprArray = null;
        try {
            eprArray = indexOperations.getServicesByNames("caNanoLab",null);
        } catch (MalformedURIException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (RemoteException e) {
            e.printStackTrace();
            assertFalse(true);
        }
        assertTrue(eprArray != null || eprArray.size() > 0);
    }
}
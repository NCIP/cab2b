package edu.wustl.cab2b.server.serviceurl;
import java.rmi.RemoteException;

import junit.framework.TestCase;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

public class IndexServiceOperationsTest extends TestCase {

    public void testGetServicesByNames() {
        IndexServiceOperations indexOperations = new IndexServiceOperations();
        EndpointReferenceType[] eprArray = null;
        try {
            eprArray = indexOperations.getServicesByNames("caNanoLab");
        } catch (MalformedURIException e) {
            e.printStackTrace();
            assertFalse(true);
        } catch (RemoteException e) {
            e.printStackTrace();
            assertFalse(true);
        }
        assertTrue(eprArray != null || eprArray.length > 0);
    }
}
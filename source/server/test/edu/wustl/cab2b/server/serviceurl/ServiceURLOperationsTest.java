package edu.wustl.cab2b.server.serviceurl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.cab2b.common.user.ServiceURL;

import junit.framework.TestCase;

public class ServiceURLOperationsTest extends TestCase {

	public void testGetAllApplicationNames() throws RemoteException {
	    /*
		Set<String> set = new HashSet<String>();
		set.add("caArray");
		set.add("CategoryEntityGroup");
		set.add("GeneConnect");
		set.add("caFE Server 1.1");
		set.add("caTissue_Core_1_2");
		set.add("caNanoLab");

		ServiceURLOperations opr = new ServiceURLOperations();
		Collection<String> names = opr.getAllApplicationNames();
		assertNotNull(names);
		assertFalse(names.isEmpty());
		Set<String> resultSet = new HashSet<String>();
		for (String url : names) {
			resultSet.add(url);
		}
		assertEquals(set, resultSet);
		*/
	}

	public void testGetAllServiceURLs() throws RemoteException {
	    /*
		Set<String> set = new HashSet<String>();
		set.add("http://128.252.227.94:9094/wsrf/services/cagrid/CaFE");
		set.add("http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect");
		set.add("http://caarray.wustl.edu:18080/wsrf/services/cagrid/CaArraySvc");
		set.add("https://caarray.wustl.edu:58443/wsrf/services/cagrid/CaTissueCore");
		set.add("http://cananolab.nci.nih.gov:80/wsrf-canano/services/cagrid/CaNanoLabService");
		ServiceURLOperations opr = new ServiceURLOperations();
		Collection<ServiceURL> names = opr.getAllServiceURLs();

		assertNotNull(names);
		assertFalse(names.isEmpty());
		Set<String> resultSet = new HashSet<String>();
		for (ServiceURL url : names) {
			resultSet.add(url.getUrlLocation());
		}
		assertEquals(set, resultSet);
		*/
	}
	
	public void testMerge() {
	    
	    ServiceURL urlMetadatFromIndexService = new ServiceURL();
	    ServiceURL urlMetadataFromDatabase = new ServiceURL();
	    
	    Map<String, ServiceURL> urlsFromDatabase = new HashMap<String, ServiceURL>();
        Map<String, ServiceURL> urlsFromIndexService = new HashMap<String, ServiceURL>();
        
        List<ServiceURL> expectedOutput = new ArrayList<ServiceURL>();
	    
	    urlMetadatFromIndexService.setHostingCenterName("");
	    urlMetadatFromIndexService.setAdminDefined(false);
	    urlMetadatFromIndexService.setConfigured(false);
	    urlMetadatFromIndexService.setDescription("Yes Description is Available");
	    urlMetadatFromIndexService.setEntityGroupName("CaTissue_Core_1_2");
	    urlMetadatFromIndexService.setUrlLocation("http://128.252.127.224");
	    urlsFromIndexService.put("http://128.252.127.224", urlMetadatFromIndexService);
	    
	    urlMetadatFromIndexService.setHostingCenterName("WashU");
        urlMetadatFromIndexService.setAdminDefined(false);
        urlMetadatFromIndexService.setConfigured(false);
        urlMetadatFromIndexService.setDescription("Yes Description is Available");
        urlMetadatFromIndexService.setEntityGroupName("CaTissue_Core_1_2");
        urlMetadatFromIndexService.setUrlLocation("http://128.132.127.224");
        urlsFromIndexService.put("http://128.132.127.224", urlMetadatFromIndexService);
        
	    urlMetadataFromDatabase.setHostingCenterName("Washu");
	    urlMetadataFromDatabase.setAdminDefined(true);
	    urlMetadataFromDatabase.setConfigured(true);
	    urlMetadataFromDatabase.setDescription("No Description Available");
	    urlMetadataFromDatabase.setEntityGroupName("CaTissue_Core_1_2");
	    urlMetadataFromDatabase.setUrlLocation("http://128.252.127.224");
	    urlsFromDatabase.put("http://128.252.127.224", urlMetadataFromDatabase);
	    
	    ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
	    expectedOutput = serviceURLOperations.merge(urlsFromDatabase, urlsFromIndexService);
	    
	    assertEquals(expectedOutput.get(0).getDescription(), "Yes Description is Available");
	    assertTrue(!expectedOutput.get(1).getDescription().isEmpty());
	}
}
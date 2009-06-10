package edu.wustl.cab2b.server.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.cab2b.common.user.ServiceURL;

import junit.framework.TestCase;

public class ServiceURLOperationsTest extends TestCase {

	public void testGetAllApplicationNames() throws RemoteException {
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
	}

	public void testGetAllServiceURLs() throws RemoteException {
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
	}
}
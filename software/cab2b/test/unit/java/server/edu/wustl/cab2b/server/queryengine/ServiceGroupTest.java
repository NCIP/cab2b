/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine;

import java.util.List;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;

public class ServiceGroupTest extends TestCase {

//	public void testAddServiceGroup() {
//		EntityCache.getInstance();
//		Session currentSession = HibernateUtil.currentSession();
//		ServiceURL url = (ServiceURL) currentSession.get(ServiceURL.class, 1L);
//		QueryOperations queryOperations = new QueryOperations();
//		
//		ServiceGroup group = new ServiceGroup();
//		group.setName("UCLA1");
//		
//		ServiceGroup group2 = new ServiceGroup();
//		group2.setName("UCLA2");
//		
//		ServiceGroupItem item = new ServiceGroupItem();
//		item.setTargetObject("test.object");
//		item.setServiceUrl(url);
//		item.setServiceGroup(group);
//		
//		ServiceGroupItem item2 = new ServiceGroupItem();
//		item2.setTargetObject("test.object2");
//		item2.setServiceUrl(url);
//		item2.setServiceGroup(group);
//		
//		ServiceGroupItem item3 = new ServiceGroupItem();
//		item3.setTargetObject("ncia.image");
//		item3.setServiceUrl(url);
//		item3.setServiceGroup(group2);
//		
//		ServiceGroupItem item4 = new ServiceGroupItem();
//		item4.setTargetObject("ncia.image2");
//		item4.setServiceUrl(url);
//		item4.setServiceGroup(group2);
//		
//		Set items = new HashSet<ServiceGroupItem>();
//		items.add(item);
//		items.add(item2);
//		group.setItems(items);
//		
//		Set items2 = new HashSet<ServiceGroupItem>();
//		items2.add(item3);
//		items2.add(item4);
//		group2.setItems(items2);
//		
//		
//		Set groups = new HashSet<ServiceGroup>();
//		groups.add(group);
//		groups.add(group2);
//		
//		Cab2bQuery query = queryOperations.addServiceGroupsToQuery(1L, groups);
//		System.out.println(query.getServiceGroups());
//		
//	}
	
//	public void testGetServiceGroups() {
//		EntityCache.getInstance();
//		QueryOperations queryOperations = new QueryOperations();
//		ICab2bQuery queryById = queryOperations.getQueryById(1L);
//		System.out.println(queryById.getServiceGroups());
//	}
	
	public void testGetServiceInstances() {
		
		ServiceURL findById = new ServiceURLOperations().findById(1L);
		System.out.println(findById);
	}
}

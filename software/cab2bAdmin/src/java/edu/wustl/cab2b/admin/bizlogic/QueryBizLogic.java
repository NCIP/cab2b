/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.bizlogic;

import java.util.Collection;
import java.util.List;

import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.ServiceGroup;
import edu.wustl.cab2b.server.queryengine.QueryOperations;

public class QueryBizLogic {

	public Collection<ICab2bQuery> findMultiModelQueries() {
		 QueryOperations queryOperations = new QueryOperations();
		 List<ICab2bQuery> queries = queryOperations.getAllMultiModelQueries();
		 return queries;
	}
	
	public ICab2bQuery loadQuery(Long id) {
		 QueryOperations queryOperations = new QueryOperations();
		 ICab2bQuery query = queryOperations.getQueryById(id);
		 query.getServiceGroups();
		 return query;
	}
	
	public Cab2bQuery addServiceGroups(Long id, Collection<ServiceGroup> groups) {
		QueryOperations queryOperations = new QueryOperations();
		queryOperations.deleteAllServiceGroups(id);
		return queryOperations.addServiceGroupsToQuery(id, groups);
	}
}

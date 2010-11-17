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
		 return queryOperations.getQueryById(id);
	}
	
	public Cab2bQuery addServiceGroups(Long id, Collection<ServiceGroup> groups) {
		QueryOperations queryOperations = new QueryOperations();
		return queryOperations.addServiceGroupsToQuery(id, groups);
	}
}

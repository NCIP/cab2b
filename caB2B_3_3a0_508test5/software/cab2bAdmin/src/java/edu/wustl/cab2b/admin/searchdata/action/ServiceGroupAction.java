package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.USER_OBJECT;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;


import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.QueryBizLogic;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.ServiceGroup;
import edu.wustl.cab2b.common.queryengine.ServiceGroupItem;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.impl.QueryEntity;

public class ServiceGroupAction extends BaseAction {

	private static final long serialVersionUID = -8241503431200945653L;
	
	private Collection allQueries;
	private String selectedQuery;
	private ICab2bQuery query;
	private Map serviceUrls;
	private List groupNames;
	private Map<String, String[]> selectedUrls;

	public String execute() {
		QueryBizLogic queryLogic = new QueryBizLogic();
		UserInterface user = (User) getSession().get(USER_OBJECT);
		Collection<ICab2bQuery> allQueries = queryLogic.findMultiModelQueries();
		this.allQueries = allQueries;
		return SUCCESS;
	}
	
	public String define() {
		QueryBizLogic queryLogic = new QueryBizLogic();
		this.query = queryLogic.loadQuery(Long.parseLong(selectedQuery));
		System.out.println(selectedQuery);
		System.out.println(query);
		//Set<IQueryEntity> queryEntities = query.getConstraints().getQueryEntities();
		ArrayList<IQueryEntity> queryEntities = query.getConstraints().getQueryEntities();
		ServiceURLOperations serviceUrlOps = new ServiceURLOperations();
		serviceUrls = new HashMap<String, List<ServiceURLInterface>>();
		for(IQueryEntity queryEntity : queryEntities) {
			EntityGroupInterface entity = ((QueryEntity)queryEntity).getEntityInterface().getEntityGroupCollection().iterator().next();
			List<ServiceURLInterface> urlsForGroup = serviceUrlOps.getAllURLsForEntityGroup(entity.getName());
			serviceUrls.put(((QueryEntity)queryEntity).getEntityInterface().getName(), urlsForGroup);
		}

		return "define";
	}
	
	public String save() {
		System.out.println("NAMES: " + groupNames);
		System.out.println("URLS: " + selectedUrls);
		QueryBizLogic bizLogic = new QueryBizLogic();
		Collection<ServiceGroup> groups = buildServiceGroups(groupNames, selectedUrls);
		bizLogic.addServiceGroups(Long.parseLong(selectedQuery), groups);
		return "save";
	}
	
	private Collection<ServiceGroup> buildServiceGroups(List<String> names, Map<String, String[]> urls) {
		
		ServiceURLOperations serviceUrlOps = new ServiceURLOperations();
		Set<ServiceGroup> groups = new HashSet<ServiceGroup>();
		for(int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			ServiceGroup group = new ServiceGroup();
			group.setName(name);
			Set<ServiceGroupItem> serviceItems = new HashSet<ServiceGroupItem>();
			for(String targetObject : urls.keySet()) {
				ServiceGroupItem item = new ServiceGroupItem();
				item.setServiceGroup(group);
				item.setTargetObject(targetObject);
				ServiceURL url = serviceUrlOps.findById(Long.parseLong(urls.get(targetObject)[i]));
				item.setServiceUrl(url);
				serviceItems.add(item);
			}
			group.setItems(serviceItems);
			groups.add(group);
		}
		return groups;
	}
	
	public Collection getAllQueries() {
		return allQueries;
	}
	
	public String getSelectedQuery() {
		return selectedQuery;
	}

	public void setSelectedQuery(String selectedQuery) {
		this.selectedQuery = selectedQuery;
	}

	public ICab2bQuery getQuery() {
		return query;
	}

	public void setQuery(ICab2bQuery query) {
		this.query = query;
	}

	public Map getServiceUrls() {
		return serviceUrls;
	}

	public void setServiceUrls(Map serviceUrls) {
		this.serviceUrls = serviceUrls;
	}

	public List getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(List groupNames) {
		this.groupNames = groupNames;
	}

	public Map getSelectedUrls() {
		return selectedUrls;
	}

	public void setSelectedUrls(Map selectedUrls) {
		this.selectedUrls = selectedUrls;
	}
	
}

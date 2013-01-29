/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.searchdata.action;

import java.util.Collection;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;

public class ServiceURLAction extends BaseAction {
	
	private static final long serialVersionUID = 8511022749467300864L;
	
	private Collection modelGroups;
	private String model;
	private String url;
	private String description;
	private String center;
	private String centerShort;
	private String contactName;
	private String contactEmail;
	
	public String execute() {
		ServiceInstanceBizLogic bizLogic = new ServiceInstanceBizLogic();
		modelGroups = bizLogic.getMetadataEntityGroups();
		return SUCCESS;
	}
	
	public String save() {
		ServiceURLInterface serviceURL = new ServiceURL();
		serviceURL.setEntityGroupName(model);
		serviceURL.setUrlLocation(url);
		serviceURL.setDescription(description);
		serviceURL.setHostingCenter(center);
		serviceURL.setHostingCenterShortName(centerShort);
		serviceURL.setContactName(contactName);
		serviceURL.setContactMailId(contactEmail);
		ServiceInstanceBizLogic bizLogic = new ServiceInstanceBizLogic();
		bizLogic.saveNewServiceInstance(serviceURL);
		return "save";
	}

	public Collection getModelGroups() {
		return modelGroups;
	}

	public void setModelGroups(Collection modelGroups) {
		this.modelGroups = modelGroups;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getCenterShort() {
		return centerShort;
	}

	public void setCenterShort(String centerShort) {
		this.centerShort = centerShort;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}

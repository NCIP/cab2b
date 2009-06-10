package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_LOADED_MODELS;
import static edu.wustl.cab2b.admin.util.AdminConstants.FILTERED_LOADED_MODELS;
import static edu.wustl.cab2b.admin.util.AdminConstants.OFFSET_PARAMETER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;

/**
 * @author lalit_chand, atul_jawale
 * 
 */
public class LoadServiceModels extends BaseAction {
	private static final long serialVersionUID = 1854118345769487660L;

	private String includeDescription;

	private String textbox;

	private String action;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Sets includedescription
	 * 
	 * @param includeDescription
	 */
	public void setIncludeDescription(String includeDescription) {
		this.includeDescription = includeDescription;
	}

	/**
	 * Returns includeDescription
	 * 
	 * @return
	 */
	public String getIncludeDescriptionChecked() {
		return this.includeDescription;
	}

	/**
	 * Sets textbox
	 * 
	 * @param textbox
	 */
	public void setTextbox(String textbox) {
		this.textbox = textbox;
	}

	/**
	 * @return
	 */
	public String getTextbox() {
		return this.textbox;
	}

	/**
	 * @return the action result type This action gets all the service models
	 *         and store them in session.
	 */
	@Override
	public String execute() {
		List<EntityGroupInterface> searchedEntitylList = new ArrayList<EntityGroupInterface>();
		List<EntityGroupInterface> allEntityList = new ArrayList<EntityGroupInterface>();

		String offset = (String) this.request.getParameter(OFFSET_PARAMETER);
		String callFrom = (String) this.request.getParameter("callFrom");
		if (callFrom != null) {
			session.remove(ALL_LOADED_MODELS);
			session.remove(FILTERED_LOADED_MODELS);
		}
		if (action != null) {
			if (session.get(ALL_LOADED_MODELS) != null)
				session.put(FILTERED_LOADED_MODELS,
						(List<EntityGroupInterface>) session
								.get(ALL_LOADED_MODELS));
		}
		if (offset != null) {
			return SUCCESS;
		}
		if (textbox != null && !textbox.equals("")) {
			allEntityList = (List<EntityGroupInterface>) session
					.get(ALL_LOADED_MODELS);
			getSearchedModels(allEntityList, searchedEntitylList);
			session.put(FILTERED_LOADED_MODELS, searchedEntitylList);
		} else {
			if (session.get(ALL_LOADED_MODELS) != null)
				session.put(FILTERED_LOADED_MODELS,
						(List<EntityGroupInterface>) session
								.get(ALL_LOADED_MODELS));
			else {
				allEntityList
						.addAll((Collection<EntityGroupInterface>) new ServiceInstanceBizLogic()
								.getMetadataEntityGroups());
				session.put(ALL_LOADED_MODELS, allEntityList);
				session.put(FILTERED_LOADED_MODELS, allEntityList);
			}
		}
		return SUCCESS;
	}

	/**
	 * Searches name
	 * 
	 * @param modelName
	 * @param searchName
	 * @return
	 */
	public boolean searchName(String modelName, String searchName) {
		StringTokenizer tokenizer = new StringTokenizer(modelName);
		int counter = 0;
		while (tokenizer.hasMoreTokens()) {
			if ((tokenizer.nextToken().toLowerCase()).contains(searchName
					.toLowerCase()))
				counter++;
		}
		if (counter == 0)
			return false;
		else
			return true;
	}

	/**
	 * returns searched models
	 * 
	 * @param allEntityGroup
	 * @param searchedEntitylList
	 */
	public void getSearchedModels(List<EntityGroupInterface> allEntityGroup,
			List<EntityGroupInterface> searchedEntitylList) {
		for (EntityGroupInterface entityGroup : allEntityGroup) {
			if (searchName(entityGroup.getLongName(), textbox))
				searchedEntitylList.add(entityGroup);
		}
		if (includeDescription != null) {
			for (EntityGroupInterface entityGroup : allEntityGroup) {
				if (searchName(entityGroup.getDescription(), textbox)) {
					if (searchedEntitylList.isEmpty()) {
						searchedEntitylList.add(entityGroup);
					} else if (!searchedEntitylList.contains(entityGroup)) {
						searchedEntitylList.add(entityGroup);
					}
				}
			}
		}
	}

}

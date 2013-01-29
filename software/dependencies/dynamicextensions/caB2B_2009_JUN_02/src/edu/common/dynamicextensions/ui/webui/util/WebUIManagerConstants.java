/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.webui.util;

public interface WebUIManagerConstants
{

	/**
	 *
	 */
	String CREATE_CONTAINER_URL=  "/dynamicExtensions/DisplayContainer.do";

	/**
	 *
	 */
	String MODE_PARAM_NAME =  "mode";
	/**
	 *
	 */
	String  CALLBACK_URL_PARAM_NAME = "callbackURL";
	/**
	 *
	 */
	String  DYNAMIC_EXTENSIONS_INTERFACE_ACTION_URL = "/dynamicExtensions/DynamicExtensionsInterfaceAction";
	/**
	 *
	 */
	String DYNAMIC_EXTENSIONS_INTERFACE_ACTION_PARAM_NAME =  "operation";
	/**
	 *
	 */
	String LOAD_DATA_ENTRY_FORM_ACTION_URL  =  "/dynamicExtensions/LoadDataEntryFormAction.do?dataEntryOperation=insertParentData";
	/**
	 *
	 */
	String CONATINER_IDENTIFIER_PARAMETER_NAME = "containerIdentifier";
	/**
	 *
	 */
	String RECORD_IDENTIFIER_PARAMETER_NAME =  "recordIdentifier";
	/**
	 *
	 */
	String OPERATION_STATUS = "operationStatus";
	/**
	 *
	 */
	String SUCCESS = "success";
	/**
	 *
	 */
	String CANCELLED = "cancelled";
	/**
	 *
	 */
	String DELETED = "deleted";

	/**
	 *
	 */
	String OPERATION_STATUS_PARAMETER_NAME =  "operationStatus";

	/**
	 *
	 */
	String CONTAINER_NAME = "containerName";
	/**
	 *
	 */
	String CONTAINER_IDENTIFIER = "containerIdentifier";
	/**
	 *
	 */
	String GET_ALL_CONTAINERS = "getAllContainers";
	/**
	 *
	 */
	String EDIT_MODE = "edit";
	/**
	 *
	 */
	String VIEW_MODE = "view";
	/**
	 *
	 */
	String DELETED_ASSOCIATION_IDS = "deletedAssociationIds";
}

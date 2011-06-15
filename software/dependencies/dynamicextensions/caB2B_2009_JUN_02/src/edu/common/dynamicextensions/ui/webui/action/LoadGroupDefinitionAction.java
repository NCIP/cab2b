/*
 * Created on Nov 15, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadGroupDefinitionProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.GroupForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 */
public class LoadGroupDefinitionAction extends BaseDynamicExtensionsAction
{

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		GroupForm groupForm = (GroupForm) form;
		loadGroup(request, groupForm);
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * 
	 * @param request
	 * @param groupForm
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private void loadGroup(HttpServletRequest request, GroupForm groupForm)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String operationMode = groupForm.getOperationMode();
		String containerIdentifier = request.getParameter("containerIdentifier");
		ContainerInterface container = null;
		if (operationMode != null && operationMode.equalsIgnoreCase(Constants.EDIT_FORM))
		{
			container = DynamicExtensionsUtility.getContainerByIdentifier(containerIdentifier);
			CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, container);
			if (container != null)
			{
				CacheManager.addObjectToCache(request, Constants.CURRENT_CONTAINER_NAME, container
						.getCaption());
				CacheManager.addObjectToCache(request, container.getCaption(), container);
			}
		}
		else
		{
			container = (ContainerInterface) CacheManager.getObjectFromCache(request,
					Constants.CONTAINER_INTERFACE);
		}
		
		EntityGroupInterface entityGroup = null;
		if (container != null)
		{
			EntityInterface entity = container.getEntity();
			entityGroup = DynamicExtensionsUtility.getEntityGroup(entity);
		}
		else
		{
			entityGroup = (EntityGroupInterface) CacheManager.getObjectFromCache(request,
					Constants.ENTITYGROUP_INTERFACE);
		}
		
		LoadGroupDefinitionProcessor loadGroupDefinitionProcessor = LoadGroupDefinitionProcessor
				.getInstance();
		loadGroupDefinitionProcessor.loadGroupDetails(entityGroup, groupForm);
	}

}
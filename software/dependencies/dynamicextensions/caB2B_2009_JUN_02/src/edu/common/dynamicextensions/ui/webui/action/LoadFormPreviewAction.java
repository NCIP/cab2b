/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.webui.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * This Action Class is responsible for displaying the Preview Forms of the Dynamic UI.
 * @author sujay_narkar, chetan_patil
 */
public class LoadFormPreviewAction extends BaseDynamicExtensionsAction
{

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		ContainerInterface containerInterface = (ContainerInterface) CacheManager
				.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);

		Stack<ContainerInterface> containerStack = (Stack<ContainerInterface>) CacheManager
				.getObjectFromCache(request, Constants.CONTAINER_STACK);
		Stack<Map<AbstractAttributeInterface, Object>> valueMapStack = (Stack<Map<AbstractAttributeInterface, Object>>) CacheManager
				.getObjectFromCache(request, Constants.VALUE_MAP_STACK);

		DataEntryForm dataEntryForm = (DataEntryForm) form;

		String dataEntryOperation = dataEntryForm.getDataEntryOperation();

		if (containerStack == null)
		{
			containerStack = new Stack<ContainerInterface>();
			CacheManager.addObjectToCache(request, Constants.CONTAINER_STACK, containerStack);

			valueMapStack = new Stack<Map<AbstractAttributeInterface, Object>>();
			CacheManager.addObjectToCache(request, Constants.VALUE_MAP_STACK, valueMapStack);

			Map<AbstractAttributeInterface, Object> recordMap = new HashMap<AbstractAttributeInterface, Object>();
			UserInterfaceiUtility.addContainerInfo(containerStack, containerInterface,
					valueMapStack, recordMap);
			dataEntryForm.setContainerInterface(containerInterface);
		}
		else if (dataEntryOperation != null
				&& dataEntryOperation.equalsIgnoreCase("insertChildData"))
		{
			String childContainerId = dataEntryForm.getChildContainerId();
			ContainmentAssociationControl associationControl = UserInterfaceiUtility
					.getAssociationControlForpreviewMode((ContainerInterface) containerStack.peek(),
							childContainerId);
			ContainerInterface childContainer = associationControl.getContainer();

			Map<AbstractAttributeInterface, Object> childContainerValueMap = new HashMap<AbstractAttributeInterface, Object>();
			UserInterfaceiUtility.addContainerInfo(containerStack, childContainer, valueMapStack,
					childContainerValueMap);
			dataEntryForm.setContainerInterface(childContainer);

		}
		else if (dataEntryOperation != null
				&& dataEntryOperation.equalsIgnoreCase("insertParentData"))
		{
			UserInterfaceiUtility.removeContainerInfo(containerStack, valueMapStack);
			if (!containerStack.isEmpty())
			{
				dataEntryForm.setContainerInterface(containerStack.peek());
			}
		}

		ActionForward forwardTo = null;
		if (containerStack.isEmpty())
		{
			CacheManager.addObjectToCache(request, Constants.CONTAINER_STACK, null);
			CacheManager.addObjectToCache(request, Constants.VALUE_MAP_STACK, null);
			forwardTo = mapping.findForward("LoadFormControls");
		}
		else
		{
			forwardTo = mapping.findForward(Constants.SUCCESS);
		}

		return forwardTo;
	}

}

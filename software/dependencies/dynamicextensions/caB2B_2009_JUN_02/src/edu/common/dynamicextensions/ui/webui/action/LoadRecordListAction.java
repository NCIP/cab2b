/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.LoadRecordListProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.RecordListForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * LoadRecordListAction class loads the RecordList page displaying list of existing records that can be edited or viewed 
 * depending on the "mode" selected.
 * @author chetan_patil
 *
 */
public class LoadRecordListAction extends BaseDynamicExtensionsAction
{

	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String containerIdentifier = request.getParameter("containerIdentifier");
		String mode = request.getParameter("mode");
		
		ContainerInterface container = DynamicExtensionsUtility.getContainerByIdentifier(containerIdentifier);
		CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, container);
		
		RecordListForm recordListForm = (RecordListForm) form;
		LoadRecordListProcessor loadRecordListProcessor = LoadRecordListProcessor.getInstance();
		loadRecordListProcessor.populateRecordIndex(recordListForm, container, mode);
		
		return mapping.findForward(Constants.SHOW_EDIT_RECORDS_PAGE);
	}
	
}

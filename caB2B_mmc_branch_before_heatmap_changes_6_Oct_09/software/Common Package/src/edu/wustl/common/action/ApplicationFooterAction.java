/*
 * Created on May 26, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonFileReader;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ApplicationFooterAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
		ActionForward forward= mapping.findForward(Constants.FAILURE);
		String pageTitle = (String)request.getParameter("PAGE_TITLE_KEY");
		String fileNameKey = (String)request.getParameter("FILE_NAME_KEY");
		
		if(isUserInputValid(pageTitle,fileNameKey)){		
			String fileName = XMLPropertyHandler.getValue(fileNameKey);
			
			CommonFileReader reader = new CommonFileReader();
			
			StringBuffer filePath = new StringBuffer();
			filePath.append(Variables.propertiesDirPath).append(File.separator).append(fileName);
			String contents = reader.readData(filePath.toString());
			
			request.setAttribute("CONTENTS",contents);
			request.setAttribute("PAGE_TITLE",pageTitle);
			forward= mapping.findForward(Constants.SUCCESS);
		}
		
	 	return forward;
    }
	
	private boolean isUserInputValid(String pageTitle,String fileNameKey ){
		
		boolean isValid=true;
		
		//Added check for xss vulnerable character for bug:8583
		Validator validator = new Validator();
		if(validator.isEmpty(pageTitle) || Validator.isXssVulnerable(pageTitle))
		{
			isValid=false;
		}
		
		if( validator.isEmpty(fileNameKey) || Validator.isXssVulnerable(fileNameKey))
		{
			isValid=false;
		}
		return isValid;
	}
}
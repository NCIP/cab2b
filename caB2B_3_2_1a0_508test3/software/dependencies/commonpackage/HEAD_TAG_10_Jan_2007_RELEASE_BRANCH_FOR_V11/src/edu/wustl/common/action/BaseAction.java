
package edu.wustl.common.action;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.UserNotAuthenticatedException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the base class for all other Actions. The class provides generic
 * methods that are resuable by all subclasses. In addition, this class ensures
 * that the user is authenticated before calling the executeWorkflow of the
 * subclass. If the User is not authenticated then an
 * UserNotAuthenticatedException is thrown.
 * 
 * @author Aarti Sharma
 *  
 */
public abstract class BaseAction extends Action
{

	/*
	 * Method ensures that the user is authenticated before calling the
	 * executeAction of the subclass. If the User is not authenticated then an
	 * UserNotAuthenticatedException is thrown.
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		/**
		 *  This flag based checking is specific to Password Security feature.
		 */
		preExecute(mapping, form, request, response);

		boolean flag = true;
		if (request.getSession().getAttribute(Constants.TEMP_SESSION_DATA) != null && request.getParameter(Constants.ACCESS) != null)
		{
			flag = false;
		}

		if (flag)
		{
			/* The case of session time out */
			if (getSessionData(request) == null)
			{
				//Forward to the Login
				throw new UserNotAuthenticatedException();
			}

		}
		setRequestData(request);
		setSelectedMenu(request);
		
		ActionForward actionForward = executeAction(mapping, form, request, response);
		
		long endTime = System.currentTimeMillis();		
		Logger.out.info("EXECUTE TIME FOR ACTION - " + this.getClass().getSimpleName() + " : " + (endTime - startTime));
		
		return actionForward;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	protected void preExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		/** Added by amit_doshi
		 *  code reviewer abhijit_naik 
		 */
		if(request.getRequestURL()!=null)
		{
			Utility.setApplicationURL(request.getRequestURL().toString());
		}
	}

	protected void setRequestData(HttpServletRequest request)
	{
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		if (operation != null)
		{
			//Sets the operation attribute to be used in the Add/Edit User Page. 
			request.setAttribute(Constants.OPERATION, operation);
		}
	}

	/**
	 * Returns the current User authenticated by CSM Authentication.
	 */
	protected String getUserLoginName(HttpServletRequest request)
	{
		SessionDataBean sessionData = getSessionData(request);
		if (sessionData != null)
		{
			return sessionData.getUserName();
		}
		return null;
	}

	protected SessionDataBean getSessionData(HttpServletRequest request)
	{
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if (obj != null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return sessionData;
		}
		return null;
	}

	/**
	 * Subclasses should implement the action's business logic in this method
	 * and can be sure that an authenticated user is present.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected abstract ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	protected void setSelectedMenu(HttpServletRequest request)
	{
		Logger.out.debug("Inside setSelectedMenu.....");
		String strMenu = request.getParameter(Constants.MENU_SELECTED);
		if (strMenu != null)
		{
			request.setAttribute(Constants.MENU_SELECTED, strMenu);
			Logger.out.debug(Constants.MENU_SELECTED + " " + strMenu + " set successfully");
		}
	}

	/**
	 * This function checks call to the action and sets/removes required attributes if AddNew or ForwardTo activity is executing. 
	 * @param request - HTTPServletRequest calling the action
	 */
	protected void checkAddNewOperation(HttpServletRequest request)
	{
		String submittedFor = (String) request.getAttribute(Constants.SUBMITTED_FOR);
		Logger.out.debug("SubmittedFor in checkAddNewOperation()------------->" + submittedFor);

		String submittedForParameter = (String) request.getParameter(Constants.SUBMITTED_FOR);

		//if AddNew loop is going on
		if (((submittedFor != null) && (submittedFor.equals("AddNew"))))
		{
			Logger.out.debug("<<<<<<<<<<<<<<  SubmittedFor is AddNew in checkAddNewOperation()   >>>>>>>>>>>>>");

			//Storing SUBMITTED_FOR attribute into Request
			request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_ADD_NEW);
		}
		//if Page is submitted on same page
		else if ((submittedForParameter != null) && (submittedForParameter.equals("AddNew")))
		{
			Logger.out.debug("<<<<<<<<<<<<<<  SubmittedFor parameter is AddNew in checkAddNewOperation()   >>>>>>>>>>>>>");
			if ((submittedFor != null) && (submittedFor.equals("Default")))
			{
				//Storing SUBMITTED_FOR attribute into Request
				request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_DEFAULT);
			}
			else
			{
				//Storing SUBMITTED_FOR attribute into Request
				request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_ADD_NEW);
			}
		}
		//if ForwardTo request is submitted
		else if ((submittedFor != null) && (submittedFor.equals("ForwardTo")))
		{
			Logger.out.debug("<<<<<<<<<<<<<<  SubmittedFor is ForwardTo in checkAddNewOperation()   >>>>>>>>>>>>>");

			request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_FORWARD_TO);

			Logger.out.debug("<<<<<<<<<<<<  Checking for FormBeanStack to remove in checkAddNewOperation()   >>>>>>>>>>>>>");
			HttpSession session = request.getSession();
			if ((session.getAttribute(Constants.FORM_BEAN_STACK)) != null)
			{
				Logger.out.debug("Removing FormBeanStack from Session in checkAddNewOperation()............");
				session.removeAttribute(Constants.FORM_BEAN_STACK);
			}
		}
		//if AddNew loop is over
		else if ((submittedFor != null) && (submittedFor.equals("Default")))
		{
			Logger.out.debug("<<<<<<<<<<<<<<  SubmittedFor is Default in checkAddNewOperation()   >>>>>>>>>>>>>");

			request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_DEFAULT);

			Logger.out.debug("<<<<<<<<<<<<  Checking for FormBeanStack to remove in checkAddNewOperation()   >>>>>>>>>>>>>");
			HttpSession session = request.getSession();
			if ((session.getAttribute(Constants.FORM_BEAN_STACK)) != null)
			{
				Logger.out.debug("Removing FormBeanStack from Session in checkAddNewOperation()............");
				session.removeAttribute(Constants.FORM_BEAN_STACK);
			}
		}
		//if AddNew or ForwardTo loop is broken...
		else
		{
			Logger.out.debug("<<<<<<<<<<<<<<  SubmittedFor is NULL in checkAddNewOperation()   >>>>>>>>>>>>>");

			Logger.out.debug("<<<<<<<<<<<<  Checking for FormBeanStack to remove in checkAddNewOperation()   >>>>>>>>>>>>>");
			HttpSession session = request.getSession();
			if ((session.getAttribute(Constants.FORM_BEAN_STACK)) != null)
			{
				Logger.out.debug("Removing FormBeanStack from Session in checkAddNewOperation()............");
				session.removeAttribute(Constants.FORM_BEAN_STACK);
			}
		}
	}

	/**
	 * This method calls the specified method passed as parameter. This allows us to have different entry points 
	 * to an action class. To use this method, 
	 * 1. pass the methodname as parameter in some request variable
	 * 2. in your executeAction/executeSecureAction method, get the parameter passed and pass it to this method.
	 * 3. the control will directly go to the methodname of the class that you specified. 
	 * This way you can reuse the same action multiple times.
	 *  
	 * @param methodName - name of the method to be called
	 * @param mapping  
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (methodName.trim().length() > 0)
		{
			Method method = getMethod(methodName, this.getClass());
			if (method != null)
			{
				Object args[] = {mapping, form, request, response};
				return (ActionForward) method.invoke(this, args);
			}
			else
			{
				return null;
			}
		}
		return null;
	}

	/**
	 * This method returns the method with the specified name if the method exists. Return null other wise.
	 * @param name - String name of method
	 * @param className - Class 
	 * @return method object
	 */
	protected Method getMethod(String name, Class className)
	{
		//argument types
		Class[] types = {ActionMapping.class, ActionForm.class, HttpServletRequest.class, HttpServletResponse.class};
		try
		{
			Method method = className.getDeclaredMethod(name, types);
			return method;
		}
		catch (NoSuchMethodException excp1)
		{
			Logger.out.error(excp1.getMessage(), excp1);
		}
		catch (NullPointerException excp2)
		{
			Logger.out.error(excp2.getMessage(), excp2);
		}
		catch (SecurityException excp3)
		{
			Logger.out.error(excp3.getMessage(), excp3);
		}
		return null;
	}
}
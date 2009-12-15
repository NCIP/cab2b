/**
 * <p>Title: ResponseServlet Class>
 * <p>Description:	This servlet generates & sends the response to the HTTP API 
 * Client in the form of HTTPMessage object.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @author Kapil Kaveeshwar
 * @version 1.00
 * Created on Dec 19, 2005
 */

package edu.wustl.common.struts;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This servlet generates & sends the response to the HTTP API Client 
 * in the form of HTTPMessage object.
 * @author aniruddha_phadnis
 * @author kapil_kaveeshwar
 */
public class ResponseServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		doPost(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		HTTPMessage httpMessage = new HTTPMessage();
		
		ActionMessages messages = (ActionMessages)req.getAttribute(Globals.MESSAGE_KEY);
		ActionErrors errors = (ActionErrors)req.getAttribute(Globals.ERROR_KEY);
		String operation = (String)req.getAttribute(Constants.OPERATION);
		Iterator it = null;

		if(messages != null)
		{
			it = messages.properties();
			httpMessage.setResponseStatus(Constants.SUCCESS);
		}
		else if(errors != null)
		{
			it = errors.properties();
			httpMessage.setResponseStatus(Constants.FAILURE);
		}
		
		if(it != null)
		{
			Locale local = (Locale)req.getSession().getAttribute(Globals.LOCALE_KEY);
			MessageResources resources = (MessageResources)req.getAttribute(Globals.MESSAGES_KEY);
			
			while(it.hasNext())
			{
				String property = (String)it.next();
				
				Iterator iterator = null;
				
				if(messages != null)
				{
					iterator = messages.get(property);
				}
				else
				{
					iterator = errors.get(property);
				}
				
				while(iterator.hasNext())
				{
					ActionMessage actionMessage = (ActionMessage)iterator.next();
					String key = actionMessage.getKey();
					httpMessage.addMessage(resources.getMessage(local,key,actionMessage.getValues()));
				}
			}
		}
		else if(operation.equals(Constants.LOGIN))
		{
			httpMessage.setResponseStatus(Constants.SUCCESS);
			httpMessage.addMessage(new String("Successful Login"));
			httpMessage.setSessionId(req.getSession(true).getId());
		}
		else if(operation.equals(Constants.LOGOUT))
		{
		    httpMessage.setResponseStatus(Constants.SUCCESS);
			httpMessage.addMessage(new String("Successful Logout"));
			httpMessage.setSessionId(null);
		}
		
		if(!operation.equals(Constants.LOGIN) && !operation.equals(Constants.LOGOUT))
		{
		    Logger.out.debug("id in ResponseServlet-->"+req.getAttribute(Constants.SYSTEM_IDENTIFIER));
		
		    if(req.getAttribute(Constants.SYSTEM_IDENTIFIER) != null)
		    {
		        httpMessage.setDomainObjectId((Long)req.getAttribute(Constants.SYSTEM_IDENTIFIER));
		    }
		    else
		    {
		        httpMessage.setDomainObjectId(null);
		    }
		}
		
		res.setContentType(Constants.HTTP_API);
		
		ObjectOutputStream oos = new ObjectOutputStream(res.getOutputStream());
		oos.writeObject(httpMessage);
		oos.flush();
		oos.close();
	}
}

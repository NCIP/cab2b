
package edu.common.dynamicextensions.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.common.util.dbManager.DBUtil;

import java.util.Properties;

/**
 * This filter class is used to check the URL, So that the dynamic Extensions should not be accessed directly.
 * @author megha_likhar
 *
 */
public class AuthorizationFilter implements Filter
{

	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException
	{
		HttpSession session = ((HttpServletRequest) request).getSession();
		String isAuthenticatedUser = "";
		if (session.getAttribute("isAuthenticatedUser") == null)
		{
			isAuthenticatedUser = request.getParameter("isAuthenticatedUser");
			session.setAttribute("isAuthenticatedUser", isAuthenticatedUser);
		}
		else
		{
			isAuthenticatedUser = session.getAttribute("isAuthenticatedUser").toString();
		}
		//System.out.println("isAuthenticatedUser...." + isAuthenticatedUser);
		InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(
				"FilterConfiguration.properties");
		Properties properties = new Properties();
		try
		{
			properties.load(inputStream);

			boolean isAuthorized = true;

			if (isAuthenticatedUser == null)
			{
				if (properties.getProperty("filter.value").equals("ON"))
					isAuthorized = false;
			}
			if (isAuthorized)
			{
				chain.doFilter(request, response);
			}
			else
			{
				showError(response, "Access Denied:Please log on using catissuecore");
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			inputStream.close();
		}
	}

	/**
	 *
	 * @param response
	 * @param string
	 * @throws IOException
	 */
	private void showError(ServletResponse response, String errorMessage) throws IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML>");
		out.println("<BODY>");
		out.println("<font color=\"black\">" + errorMessage + "</font>");
		out.println("</BODY>");
		out.println("</HTML>");
	}

}

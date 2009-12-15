package edu.wustl.cab2b.admin.preactionlogic;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * This is the AuthenticationProcessor
 * @author atul_jawale
 */
public class AuthenticationProcessor implements Filter {
	private static final long serialVersionUID = -5992264946335132057L;

	/**
	 * This method is called only once at startup and we are setting the property file name
	 * @param filterConfig
	 * @throws ServletException
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * This method is called for every request to the server and used to check for a 
	 * valid session also saves user URL in request which is used by the pagination logic
	 * @param req
	 * @param res
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		request.setAttribute("URL", request.getRequestURL().toString());
		final HttpSession session = request.getSession(true);
		
		if (session.getAttribute(AdminConstants.USER_OBJECT) == null) {
		    String userName = request.getParameter("userName");
		    String password = request.getParameter("password");
			if (userName == null) {
				request.setAttribute("error","Session expired please login again.");
				request.getRequestDispatcher("/jsp/default.jsp").forward(req, res);
				return;
			}

			String result = AdminConstants.FAILURE;
			
			UserInterface user = new UserOperations().getUserByName(userName);
			if (user != null && ((User) user).getPassword().compareTo(password) == 0) {
				session.setAttribute(AdminConstants.USER_OBJECT, user);
				result = AdminConstants.SUCCESS;
			} 
			if (AdminConstants.FAILURE.compareTo(result) == 0) {
				request.setAttribute("error", "Invalid username or password.");
				request.getRequestDispatcher("/jsp/default.jsp").forward(req,res);
				return;
			}
		}
		filterChain.doFilter(req, res);
	}
	/**
	 * Destroys session
	 */
	public void destroy() {
	}
}
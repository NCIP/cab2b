/**
 * 
 */
package edu.wustl.cab2b.admin.preactionlogic;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.cab2b.admin.util.Cab2bConstants;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * @author atul_jawale
 *
 */
public class AuthenticationProcessor implements Filter {

    /**
     * 
     */
    private static final long serialVersionUID = -5992264946335132057L;

    /**
     * This method is called only once at startup and we are setting the property file name
     */
    public void init(FilterConfig filterConfig) throws ServletException {

        String fileName = filterConfig.getInitParameter(Cab2bConstants.PROPERTY_FILE);
        Cab2bConstants.PROPERTY_FILENAME = fileName;
    }

    /**
     * This method is called for every request to the server and used to check for a valid session
     * also saves user URL in request which is used by the pagination logic
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
            ServletException {
        String result = Cab2bConstants.FAILURE;

        HttpServletRequest request = (HttpServletRequest) req;
        request.setAttribute("URL", request.getRequestURL().toString());
        final HttpSession session = request.getSession(true);
        String userName = null, password = null;
        if (session.getAttribute(Cab2bConstants.USER_OBJECT) == null) {
            userName = request.getParameter("userName");
            password = request.getParameter("password");
            if(userName==null)
            {
                request.setAttribute("error", "Session expired please login again.");
                request.getRequestDispatcher("/jsp/default.jsp").forward(req, res);
                return;
            }
            UserOperations userOperation = new UserOperations();
            try {
                User user = userOperation.getUserByName(userName);
                if (user.getPassword().compareTo(password) == 0) {
                    session.setAttribute(Cab2bConstants.USER_OBJECT, user);
                    result = Cab2bConstants.SUCCESS;
                } else
                    result = Cab2bConstants.FAILURE;

            } catch (RemoteException e) {
                result = Cab2bConstants.FAILURE;
            }
            if (Cab2bConstants.FAILURE.compareTo(result) == 0) {
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/jsp/default.jsp").forward(req, res);
                return;
            }

        }

        filterChain.doFilter(req, res);
    }

    public void destroy() {

    }

}

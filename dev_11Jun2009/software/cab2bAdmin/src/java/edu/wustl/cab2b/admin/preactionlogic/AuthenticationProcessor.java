package edu.wustl.cab2b.admin.preactionlogic;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    FilterConfig fc = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        fc = filterConfig;
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
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.addHeader("Cache-Control", fc.getInitParameter("Cache-Control"));
        request.setAttribute("URL", request.getRequestURL().toString());
        HttpSession session = request.getSession(true);

        if (session.getAttribute(AdminConstants.USER_OBJECT) == null
                || (request.getMethod().indexOf("POST") != -1 && request.getParameter("userName") != null)) {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            if (userName == null) {
                request.setAttribute("error", "Session expired please login again.");
                request.getRequestDispatcher("/jsp/default.jsp").forward(request, response);
                return;
            }
            String result = AdminConstants.FAILURE;
            UserInterface user = new UserOperations().getUserByName(userName);
            if (user != null && ((User) user).getPassword().compareTo(password) == 0) {
                session.setAttribute(AdminConstants.USER_OBJECT, user);
                result = AdminConstants.SUCCESS;

            }
            if (AdminConstants.FAILURE.compareTo(result) == 0) {
                session.invalidate();
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/jsp/default.jsp").forward(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Destroys session
     */
    public void destroy() {
    }
}
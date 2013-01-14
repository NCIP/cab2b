/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.preactionlogic;

import java.io.IOException;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2b.server.util.XSSVulnerableDetector;

/**
 * This filter class is used to refine all requests before actual processing.
 * @author atul_jawale
 */
public class AuthenticationProcessor implements Filter {
    private static final long serialVersionUID = -5992264946335132057L;

    FilterConfig fc = null;

    /**
     * This method is called only once at startup and we are setting the property file name
     * @param filterConfig
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        fc = filterConfig;
    }

    /**
     * This method is called for every request to the server and is used to refine the requests before actual processing.
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

        if (!(request.getMethod().trim().equalsIgnoreCase("GET") || request.getMethod().trim()
            .equalsIgnoreCase("POST"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            //To make check on cross site scripting (XSS).
            //Check each parameter in the request for XssVulnerability using RegEx=[<>] (useful for POST requests).
            //If any parameter is found to be XssVulnerable, then redirect to home page.
            for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                String parameter = (String) e.nextElement();
                if (("password").equals(parameter)) {
                    continue;
                }
                if (XSSVulnerableDetector.isXssVulnerable(request.getParameter(parameter))) {
                    request.setAttribute(AdminConstants.INVALID_REQUEST, AdminConstants.INVALID_REQUEST);
                    request.getRequestDispatcher("/jsp/default.jsp").forward(req, res);
                    return;
                }
            }
        }

        request.setAttribute("URL", request.getRequestURL().toString());
        ServletContext application = fc.getServletContext();
        if (request.getSession().getAttribute(AdminConstants.USER_OBJECT) == null
                || request.getParameter("userName") != null) {
            String userName = request.getParameter("userName");
            UserInterface user = null;
            if (application.getAttribute("invalidLoginCount" + userName) != null
                    && Integer.parseInt(application.getAttribute("invalidLoginCount" + userName).toString()) < 6
                    && (new GregorianCalendar().getTimeInMillis()
                            - (Long) application.getAttribute("lastInvalidLoginAttemptTime" + userName) > 15 * 60 * 1000)) {
                application.removeAttribute("invalidLoginCount" + userName);
            }
            if (application.getAttribute("invalidLoginCount" + userName) != null
                    && Integer.parseInt(application.getAttribute("invalidLoginCount" + userName).toString()) == 6) {
                if (new GregorianCalendar().getTimeInMillis()
                        - (Long) application.getAttribute("lastInvalidLoginAttemptTime" + userName) > 60 * 60 * 1000) {
                    application.removeAttribute("invalidLoginCount" + userName);
                    application.removeAttribute("lastInvalidLoginAttemptTime" + userName);
                } else {
                    request
                        .setAttribute("error",
                                      "Your account has been locked for one hour because of 6 continuous invalid login attempts!");
                    request.getRequestDispatcher("/jsp/default.jsp").forward(request, response);
                    return;
                }
            }
            request.getSession().removeAttribute(AdminConstants.USER_OBJECT);
            if (request.getParameter("userName") == null) {
                request.setAttribute("error",
                                     "Your session has been timed out because of long period of inactivity!");
                request.getRequestDispatcher("/jsp/default.jsp").forward(request, response);
            } else {
                String password = request.getParameter("password");
                if (!(XSSVulnerableDetector.isXssSQLVulnerable(userName) || XSSVulnerableDetector
                    .isXssSQLVulnerable(password))) {
                    user = new UserOperations().getUserByName(userName);
                    if (user != null && ((User) user).getPassword().compareTo(password) == 0) {
                        request.getSession().setAttribute(AdminConstants.USER_OBJECT, user);
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                if (user != null) {
                    int invalidLoginCount =
                            application.getAttribute("invalidLoginCount" + userName) == null ? 0 : Integer
                                .parseInt(application.getAttribute("invalidLoginCount" + userName).toString());
                    application.setAttribute("invalidLoginCount" + userName, ++invalidLoginCount);
                    application.setAttribute("lastInvalidLoginAttemptTime" + userName, new GregorianCalendar()
                        .getTimeInMillis());
                    if (invalidLoginCount == 6) {
                        request
                            .setAttribute("error",
                                          "Your account has been locked for one hour because of 6 continuous invalid login attempts!");
                        request.getRequestDispatcher("/jsp/default.jsp").forward(request, response);
                        return;
                    }
                }
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/jsp/default.jsp").forward(request, response);
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * This method is called only once when the filter class is unloaded.
     */
    public void destroy() {
    }
}
package edu.wustl.cab2bwebapp.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2b.server.util.XSSVulnerableDetector;

/**
 * This filter class is used for disabling page caching and checking proper authentications.
 * @author chetan_pundhir
 */

public class ApplicationFilter implements Filter {

    /**
     * This method is called only once at startup.
     * @param filterConfig
     * @throws ServletException
     */

    FilterConfig fc = null;

    /**
     * @param FilterConfig
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.fc = filterConfig;
    }

    /**
     * This method is called for every request to the server and is used to disable caching.
     * @param req
     * @param res
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */

    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
            ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.addHeader("Cache-Control", fc.getInitParameter("Cache-Control"));

        //To prevent cross site scripting (XSS)
        boolean isXssVulnerable = false;
        
        //1)Check each parameter in the request for XssVulnerability using RegEx= [<>] (useful for POST requests).
        //As soon as a parameter is found to be XssVulnerable, break the loop and redirect to homepage 
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String parameterVal = request.getParameter((String) e.nextElement());
            if (XSSVulnerableDetector.isXssVulnerable(parameterVal)) {
                isXssVulnerable = true;
                break;
            }
        }
        //2)Even if the parameters are non-XssVulnerable, there should not be any parameter appended in the URL (useful for GET requests)
        //If no parameter is found XssVulnerable & its not a ajax call & it has some parameters in the URL, then redirect to homepage 
        if ((isXssVulnerable == false) && (request.getHeader(Constants.AJAX_CALL) == null)
                && (request.getQueryString() != null)) {
            isXssVulnerable = true;
        }
        
        //If request is XssVulnerable, redirect to homepage (with an alert message in the home.jsp)
        if (isXssVulnerable) {
            request.setAttribute(Constants.INVALID_REQUEST, Constants.INVALID_REQUEST);
            request.getRequestDispatcher("/pages/home.jsp").forward(req, res);
        }

        else if (request.getSession().isNew()) {
            if (request.getHeader(Constants.AJAX_CALL) != null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter pw = response.getWriter();
                ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
                pw.write(bundle.getString("alert.sessiontimeout"));
                pw.close();
            } else if (request.getRequestURL().indexOf("Home.do") == -1
                    && request.getRequestURL().indexOf("Login.do") == -1) {
                String tokens[] = request.getRequestURL().toString().split("/");
                if (tokens[tokens.length - 1].indexOf(".") != -1) {
                    request.setAttribute(Constants.ERROR_SESSION_TIMEOUT, Constants.ERROR_SESSION_TIMEOUT);
                    request.getRequestDispatcher("/pages/home.jsp").forward(req, res);
                } else {
                    filterChain.doFilter(request, response);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } else if (request.getSession().getAttribute(Constants.USER_NAME) != null
                && request.getRequestURL().indexOf("Login.do") != -1) {
            request.getRequestDispatcher("/pages/home.jsp").forward(req, res);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Destroys session
     */
    public void destroy() {
    }
}
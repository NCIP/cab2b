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

import edu.wustl.cab2b.server.util.XSSVulnerableDetector;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * This filter class is used to refine all requests before actual processing.
 * @author chetan_pundhir
 */

public class ApplicationFilter implements Filter {

    FilterConfig fc = null;

    /**
     * This method is called only once at startup.
     * @param FilterConfig
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.fc = filterConfig;
    }

    /**
     * This method is called for every request to the server and is used to refine the requests before actual processing.
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

        if (!(request.getMethod().trim().equalsIgnoreCase("GET") || request.getMethod().trim()
            .equalsIgnoreCase("POST"))) {
            //Disable all HTTP methods except GET and POST.           
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            //To make check on cross site scripting (XSS).
            boolean isXssVulnerable = false;

            //There should not be any parameter appended in the URL (for Non AJAX GET requests).
            //If some parameters are appended in the URL and the call is not AJAX based, then redirect to home page.
            // isXssVulnerable = request.getQueryString() != null && request.getHeader(Constants.AJAX_CALL) == null;

            //Check each parameter in the request for XssVulnerability using RegEx=[<>] (useful for POST requests).
            //If any parameter is found to be XssVulnerable, then redirect to home page.
            for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                String parameter = (String) e.nextElement();
                if (("password").equals(parameter)) {
                    continue;
                }
                String parameterVal = request.getParameter(parameter);
                if (XSSVulnerableDetector.isXssVulnerable(parameterVal)) {
                    isXssVulnerable = true;
                    break;
                }
            }
            if (isXssVulnerable) { //If request is XssVulnerable, redirect to home page.
                request.setAttribute(Constants.INVALID_REQUEST, Constants.INVALID_REQUEST);
                request.getRequestDispatcher("/pages/home.jsp").forward(req, res);
                return;
            }
        }

        if (request.getSession().isNew()) {
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
     * This method is called only once when the filter class is unloaded.
     */
    public void destroy() {
    }
}
package edu.wustl.cab2bwebapp.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * This filter class is used to filter out unauthorized users from accessing the application and for disabling page caching.
 * @author chetan_pundhir
 */

public class AuthenticationFilter implements Filter {

    /**
     * This method is called only once at startup.
     * @param filterConfig
     * @throws ServletException
     */

    FilterConfig fc = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.fc = filterConfig;
    }

    /**
     * This method is called for every request to the server and used to validate users and disable caching.
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

        String authenticationExclusion[] = fc.getInitParameter("Authentication-Exclusion").split(",");
        for (int i = 0; i < authenticationExclusion.length; i++) {
            if (request.getRequestURL().indexOf(authenticationExclusion[i]) != -1) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        if (request.getSession().getAttribute(Constants.USER) == null) {
            String userName = request.getParameter("userName");
            if (userName == null) {
                request.setAttribute(Constants.ERROR_AUTHENTICATION, Constants.ERROR_AUTHENTICATION);
                request.getRequestDispatcher("/pages/home.jsp").forward(req, res);
                return;
            }
        } else {
            response.addHeader("Cache-Control", fc.getInitParameter("Cache-Control"));
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Destroys session
     */
    public void destroy() {
    }
}
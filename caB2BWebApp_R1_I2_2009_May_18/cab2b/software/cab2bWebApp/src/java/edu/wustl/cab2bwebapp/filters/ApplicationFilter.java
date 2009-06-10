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

/**
 * This filter class is used for disabling page caching.
 * @author chetan_pundhir
 */

public class ApplicationFilter implements Filter {

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
     * This method is called for every request to the server and is used to disable caching.
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
        filterChain.doFilter(request, response);
    }

    /**
     * Destroys session
     */
    public void destroy() {
    }
}
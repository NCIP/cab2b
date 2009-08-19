package edu.wustl.cab2bwebapp.filters;

import java.io.IOException;
import java.io.PrintWriter;
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

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
            ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.addHeader("Cache-Control", fc.getInitParameter("Cache-Control"));
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
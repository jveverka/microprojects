package one.microproject.auth.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);


    public SecurityFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("FILTER: doFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String authorization = httpServletRequest.getHeader("Authorization");
        if ("Bearer valid".equals(authorization)) {
            LOGGER.info("FILTER: VALID");
            chain.doFilter(request, response);
            return;
        } else {
            LOGGER.error("not authorized: request header \"Authorization\" is missing !");
        }
        LOGGER.info("FILTER: doFilter FORBIDDEN");
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}

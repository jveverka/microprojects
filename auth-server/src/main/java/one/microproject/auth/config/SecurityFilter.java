package one.microproject.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import jakarta.servlet.Filter;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);


    public SecurityFilter() {
        LOGGER.info("FILTER: created");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOGGER.info("FILTER: initialized");
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

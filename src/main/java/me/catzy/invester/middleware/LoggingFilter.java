package me.catzy.invester.middleware;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class LoggingFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest) request;
    	if(req.getMethod().equals("OPTIONS")) {
    		chain.doFilter(request, response);
    		return;
    	}
    	logger.info(req.getRemoteAddr() + " => " + req.getMethod() + " " + req.getRequestURI());
        chain.doFilter(request, response);
    }
}
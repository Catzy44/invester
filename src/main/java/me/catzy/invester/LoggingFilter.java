package me.catzy.invester;

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
import me.catzy.invester.objects.marketEvent.MarketEventService;

@Component
public class LoggingFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(MarketEventService.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest) request;
    	logger.info(req.getRemoteAddr() + " => " + req.getMethod() + " " + req.getRequestURI());
        chain.doFilter(request, response);
    }
}
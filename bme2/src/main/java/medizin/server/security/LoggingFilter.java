package medizin.server.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import medizin.server.domain.Person;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class LoggingFilter implements Filter{

	private static final String USER_NAME = "userName";
	private static final String PAGEURL = "pageurl";
	private static Logger log = Logger.getLogger(LoggingFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
            if (request instanceof HttpServletRequest) {
            	HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            	Person person = Person.myGetLoggedPerson(httpServletRequest.getSession());
            	
            	if(person != null) {
            		MDC.put(USER_NAME, person.getName());
                	String pageURL = httpServletRequest.getHeader(PAGEURL);
                	if(StringUtils.isNotBlank(pageURL)) {
                		log.debug(pageURL);	
                	}
            	}
        	}
            chain.doFilter(request, response);
        } finally {
            MDC.remove(USER_NAME);
        }
	}

	@Override
	public void destroy() {
	}

}

package medizin.server.security;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import medizin.server.domain.Person;
import medizin.server.utils.ServerConstants;

import org.apache.log4j.Logger;

public class AuthenticationFilter implements Filter {
	
	private static final String EMAIL = "mail";
	private static final String UNIQUE_ID = "uniqueID";
	private static Logger log = Logger.getLogger(AuthenticationFilter.class);
	
	@Override
	public void destroy() {		
		log.info("Inside destroy");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		log.info("Inside doFilter");
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		
		Enumeration<String> names = request.getHeaderNames();
		log.info("Headers are:");
		while(names.hasMoreElements())
		{
			String headerName = names.nextElement();
			log.info(headerName + " : " + request.getHeader(headerName));
		}
		
		log.info("Attributes are:");
		names = request.getAttributeNames();
		while(names.hasMoreElements())
		{
			String attributeName = names.nextElement();
			log.info(attributeName + " : " + request.getAttribute(attributeName));
		}
		
		Cookie[] cookies = request.getCookies();
		log.info("Cookies are:");
		int index = 0;
		while(cookies != null && index > cookies.length)
		{			
			log.info(cookies[index].getName() + " : " + cookies[index].getValue());
		}
		
		/* for production uniqueID and for testing uid */
		String shibdId = request.getHeader(UNIQUE_ID);
		String email = request.getHeader(EMAIL);
		//String userId = request.getHeader("uid");
		log.info("shibdId : " + shibdId);
		log.info("mail : " + email);
		
		boolean flag = false;
		// Session Management
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			Object sessionUserId = session.getAttribute(ServerConstants.SESSION_SHIBD_ID_KEY);
			
			if(sessionUserId != null && shibdId.equals((String)sessionUserId)) {
				log.info("----> Authenticated using session");
				flag = true;
			} else {
				flag = authenticationUsingDB(request, email, shibdId);
				if(flag) {
					//session.setAttribute(ServerConstants.SESSION_SHIBD_ID_KEY, shibdId);
					log.info("----> Authenticated using DB");
				}
			}
		} else {
			log.info("---->Doing Authentication using New Session");
			flag = authenticationUsingDB(request, email, shibdId);
			if(flag){
//				session = request.getSession();
//				session.setAttribute(ServerConstants.SESSION_SHIBD_ID_KEY, shibdId);
				log.info("----> Authenticated using New session");
			}		
		}
		
		
		if(flag) {
			filterChain.doFilter(servletRequest, servletResponse);
		}
		else {
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, shibdId + " is not authorized to bme.");
		}
			
	}

	private boolean authenticationUsingDB(HttpServletRequest request, String email, String shibdId) {
		boolean flag = false;
		
		try{
			Person person = Person.findPersonByShibId(shibdId);
			if(person == null) {
				person = Person.findPersonByEmail(email);	
			}
			
			if(person != null) {
				person.loginPerson(request.getSession(), shibdId);
				log.info("Login successfully" );
				flag=true;
			}else {
				log.info("Person is null, is not authorized");
				flag = false;
			}
			
		}catch(Exception e)
		{
			log.error("Exception in authentication ",e);
			flag=false;
		}
		return flag;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.info("Inside destroy");		
	}
	

}
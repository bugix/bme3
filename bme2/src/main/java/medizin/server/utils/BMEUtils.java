package medizin.server.utils;

import javax.servlet.http.HttpServletRequest;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public final class BMEUtils {

	private BMEUtils() {
	}

	/*
	 * only useful in domain class. Cannot be used in servlet directly.
	 */
	public static String getRealPath(String path) {

		return getRealPath(RequestFactoryServlet.getThreadLocalRequest(), path);
	}
	
	/*
	 * only useful in domain class. Cannot be used in servlet directly.
	 */
	public static String getContextPath(String path) {
		return getContextPath(RequestFactoryServlet.getThreadLocalRequest(), path);
	}
	

	/*
	 * Only useful in sevlet class.
	 */
	public static String getRealPath(HttpServletRequest request, String path) {

		String fileSeparator = System.getProperty("file.separator");
		return request.getSession().getServletContext()
				.getRealPath(fileSeparator)
				+ path;
	}

	/*
	 * Only useful in sevlet class.
	 */
	public static String getContextPath(HttpServletRequest request, String path) {
		String contextFileSeparator = "/";
		return request.getSession().getServletContext().getContextPath()
				+ contextFileSeparator + path;
	}

}

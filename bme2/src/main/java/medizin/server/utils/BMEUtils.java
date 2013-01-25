package medizin.server.utils;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public final class BMEUtils {

	private BMEUtils() {
	}

	/*
	 * only useful in domain class. Cannot be used in servlet directly.
	 */
	public static String getRealPath(String url) {
		return RequestFactoryServlet.getThreadLocalServletContext()
				.getRealPath(url);
	}
	
}

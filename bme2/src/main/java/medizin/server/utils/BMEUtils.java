package medizin.server.utils;

import ij.ImagePlus;
import ij.io.Opener;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public final class BMEUtils {
	private static Logger log = Logger.getLogger(BMEUtils.class);
	
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

	public static int[] findImageWidthAndHeight(File appUploadedFile) {
		int[] size = null;
		try{
			ImagePlus image;
			String inputURL = "file:///" + appUploadedFile.getAbsolutePath();
			Opener opener = new Opener();
			image = opener.openURL(inputURL);
			size = new int[2];
			size[0] = image.getWidth();
			size[1] = image.getHeight();
		}catch(Exception e){
			log.error(e);
		}
		return size;
	}

}

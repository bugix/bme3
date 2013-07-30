package medizin.server.download;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import medizin.server.utils.BMEUtils;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class FileDownloadServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(FileDownloadServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		fileCopy(config.getServletContext());
		
	}

	public void fileCopy(ServletContext servletContext) {
		try {
			log.info("In init method");
			
			File copyTo = new File(BMEUtils.getRealPath(servletContext, "/") + SharedConstant.DOWNLOAD_DIR);
			File copyFrom = new File(SharedConstant.getUploadBaseDIRPath() + SharedConstant.DOWNLOAD_DIR);
			log.info("copy to : " + copyTo.getAbsolutePath());
			log.info("Copy from :" + copyFrom.getAbsolutePath());
			
			FileUtils.copyDirectory(copyFrom, copyTo, true);
		}catch (Exception e) {
			log.error("Error in copying file ",e);
		}
	}
	
	@Override
	public void destroy() {

	}
}

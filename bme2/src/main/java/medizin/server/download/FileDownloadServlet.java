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
		final String copyToPath = BMEUtils.getRealPath(servletContext, "/") + SharedConstant.DOWNLOAD_DIR;
		final String copyFromPath = SharedConstant.getUploadBaseDIRPath() + SharedConstant.DOWNLOAD_DIR;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
		
				try {
					log.info("In init method");
					final File copyTo = new File(copyToPath);
					final File copyFrom = new File(copyFromPath);
					log.info("copy to : " + copyTo.getAbsolutePath());
					log.info("Copy from :" + copyFrom.getAbsolutePath());
					FileUtils.forceMkdir(copyTo);
					FileUtils.forceMkdir(copyFrom);
					FileUtils.copyDirectory(copyFrom, copyTo, true);
				}catch (Exception e) {
					log.error("Error in copying file ",e);
				}
			}
		}).start();
	}
	
	@Override
	public void destroy() {

	}
}

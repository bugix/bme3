package medizin.server.download;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import medizin.server.utils.BMEUtils;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class FileDownloadFilter implements Filter {

	private static Logger log = Logger.getLogger(FileDownloadFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		boolean flag = false;

		log.info("Inside doFilter");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		File copyTo = new File(BMEUtils.getRealPath(request, request.getServletPath()));
		File copyFrom = new File(SharedConstant.getUploadBaseDIRPath() + request.getServletPath());
		log.info("copy to : " + copyTo.getAbsolutePath());
		log.info("Copy from :" + copyFrom.getAbsolutePath());

		// check at download location
		if (copyTo.exists() == false) {
			if(copyFrom.exists() == true) {
			
				// get from external path
				FileUtils.copyFile(copyFrom,copyTo);
				flag = true;
				log.info("Copy done");
				// sendFile(request,response,FileUtils.readFileToByteArray(new
				// File(FilePathConstant.getUploadBaseDIRPath() +
				// request.getServletPath())) , f.getName());

			}else {
				log.info("copyFrom.exists() " + copyFrom.exists());
				flag = false;
			}
			
		}else {
			flag = true;
			log.info("Resource exists");
		}

		if (flag == true) {
			chain.doFilter(servletRequest, servletResponse);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND,
					"Requested resource not found");
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	private static void sendFile(HttpServletRequest request,
			HttpServletResponse response, byte[] resource, String fileName)
			throws IOException {
		ServletOutputStream stream = null;
		stream = response.getOutputStream();

		if (FilenameUtils.isExtension(fileName, "pdf")) {
			response.setContentType("application/x-download");
		} else {
			response.setContentType(request.getContentType());
		}

		response.addHeader("Content-Disposition", "inline; filename=\""
				+ fileName + "\"");
		response.setContentLength((int) resource.length);
		if (resource.length > 0) {
			stream.write(resource);
		}
		stream.close();
	}

	
}

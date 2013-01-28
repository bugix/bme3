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

import medizin.shared.utils.FilePathConstant;

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

		boolean flag = true;

		log.info("Inside doFilter");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String copyTo = fetchRealPath(request, request.getServletPath());
		String copyFrom = FilePathConstant.getUploadBaseDIRPath() + request.getServletPath();
		log.info("copy to : " + copyTo);
		log.info("Copy from :" + copyFrom);
		
		File f = new File(copyTo);

		// check at download location
		if (f.exists() == false) {
			// get from external path
			copyFile(copyFrom, copyTo);
			flag = true;

			// sendFile(request,response,FileUtils.readFileToByteArray(new
			// File(FilePathConstant.getUploadBaseDIRPath() +
			// request.getServletPath())) , f.getName());
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

	public static String fetchRealPath(HttpServletRequest request, String path) {

		String fileSeparator = System.getProperty("file.separator");
		return request.getSession().getServletContext()
				.getRealPath(fileSeparator)
				+ path;
	}

	private String fetchContextPath(HttpServletRequest request, String path) {
		String contextFileSeparator = "/";
		return request.getSession().getServletContext().getContextPath()
				+ contextFileSeparator + path;
	}

	private void copyFile(String realPath, String contextPath)
			throws IOException {

		FileUtils.copyFile(new File(realPath), new File(contextPath));

	}

}

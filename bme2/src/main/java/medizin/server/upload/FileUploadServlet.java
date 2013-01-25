package medizin.server.upload;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import medizin.server.utils.FilePathConstant;
import medizin.shared.BMEFileUploadConstant;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

public class FileUploadServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(FileUploadServlet.class);

	private static String appUploadDirectory = FilePathConstant.UPLOAD_DIR_PATH;// DEFAULT_IMAGE_PATH;

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unsupported content");
		}

		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		ProgressListener progressListener = new ProgressListener() {
			private long megaBytes = -1;

			public void update(long pBytesRead, long pContentLength, int pItems) {
				long mBytes = pBytesRead / 1000000;
				if (megaBytes == mBytes) {
					return;
				}
				megaBytes = mBytes;
				log.info("We are currently reading item " + pItems);
				if (pContentLength == -1) {
					log.info("So far, " + pBytesRead + " bytes have been read.");
				} else {
					log.info("So far, " + pBytesRead + " of " + pContentLength + " bytes have been read.");
				}
			}
		};
		upload.setProgressListener(progressListener);
		String fileName = "";
		String temp = "";

		// fileName=request.getAttribute("id").toString()+request.getAttribute("name").toString()+"_";

		try {
			log.info("Request path: " + request.getContextPath());
			log.info("Request content type: " + request.getContentType());
			log.info("Request content type: " + request.getRequestURL());
			log.info("Request content type: " + request.getServletPath());
			
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);
			System.out.println("Item Size: " + items.size());

			for (FileItem item : items) {
				if (item.isFormField()) {
					// fileName=item.getString() +"_"+fileName;
					fileName = fileName;
				} else {

					temp = FilenameUtils.getName(item.getName());

				}
			}

			fileName = temp;

			for (FileItem item : items) {
				// process only file upload - discard other form item types

				/*
				 * if (item.isFormField()) {
				 * Log.info("Form Field: "+item.getFieldName
				 * ()+" = "+item.getString()); continue; }
				 */
				if (item.isFormField()) {
					continue;
				} else {

					// Upload File to Application Directory
					// appUploadDirectory=session.getServletContext().getRealPath(".")
					// + appUploadDirectory;
					File appUploadedFile = new File(appUploadDirectory, fileName);

					FileUtils.touch(appUploadedFile);
					appUploadedFile.createNewFile();
					// save
					item.write(appUploadedFile);
					
					log.info("file name " + fileName);

					// upload file to local directory

					/*
					 * File localUploadedFile = new File( localUploadDirectory,
					 * fileName); localUploadedFile.createNewFile(); //save
					 * item.write(localUploadedFile);
					 * //resp.setStatus(HttpServletResponse.SC_CREATED);
					 * Log.info("file name " + fileName);
					 */

					System.out.println("File Name :" + fileName);

					
					//filterResource(request,response,appUploadedFile);
					
				
					
				}
			}
		} catch (Exception e) {

			log.error("An error occurred while creating the file : " + e.getMessage());
		}
		// service info
		log.info("Attribtue names: ");
		Enumeration<?> enu = request.getAttributeNames();
		while (enu.hasMoreElements()) {
			log.info("Attribute name: " + enu.nextElement().toString());
		}
		log.info("Content type: " + request.getContentType());

		response.setContentType("text/html");

	}

	private void filterResource(HttpServletRequest request,
			HttpServletResponse response, File appUploadedFile) {
		
		
		String extension = FilenameUtils.getExtension(appUploadDirectory);
		
		//for image 
		if(ArrayUtils.contains(BMEFileUploadConstant.imageExtension, extension)) {
			// positive response 
			response.setStatus(HttpServletResponse.SC_CREATED);
		}else {
			// negative response
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
	}
}
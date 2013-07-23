package medizin.server.download;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import medizin.server.utils.docx.DocxPaperMHTML;
import medizin.server.utils.docx.XmlPaper;
import medizin.shared.utils.FileDownloaderProps;
import medizin.shared.utils.FileDownloaderProps.Method;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.primitives.Ints;

@SuppressWarnings("serial")
public class FileDownloader extends HttpServlet{
	
	private static final Logger log = Logger.getLogger(FileDownloader.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String entity = request.getParameter(FileDownloaderProps.METHOD_KEY);
		
		if(StringUtils.isNotBlank(entity)) {
			try {
					callMethod(request,response);
			} catch (IOException e) {
				log.error("IO error in FileDownloader ",e);
			}
		}else {
			log.error("Entity is not valid : " + entity);
		}
		
	}

	private void callMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ByteArrayOutputStream os = null;
		try {
		
			int ordinal = Integer.parseInt(request.getParameter(FileDownloaderProps.METHOD_KEY));
			Method method =  FileDownloaderProps.Method.values()[ordinal];
			os = new ByteArrayOutputStream();
			String fileName = "error.txt";
			
			
			switch (method) {
			case DOCX_PAPER: 
			{
				fileName = createDocxPaperForExam(request,response,os);
				break;
			}
			case XML_PAPER:
			{
				fileName = createXmlPaperForExam(request,response,os);
				break;
			}
			default:
				log.error("Error in method ordinal");
				break;
			}

			sendFile(response, os.toByteArray(), fileName);
		}finally {

			if(os != null) {
				os.close();
				os = null;
			}
		}	
	}

	private void sendFile(HttpServletResponse response, byte[] resource, String fileName) throws IOException {
		ServletOutputStream stream = null;
		stream = response.getOutputStream();
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "inline; filename=\""+ fileName + "\"");
		response.setContentLength((int) resource.length);
		if(resource.length > 0) {
			stream.write(resource);
		}
		stream.close();
	}

	private String createDocxPaperForExam(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream os) {
		
		Integer assignment = null; 
		boolean isVersionA = true;
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.ASSIGNMENT))) {
			assignment = Ints.tryParse(request.getParameter(FileDownloaderProps.ASSIGNMENT));	
		}else {
			log.error("Error in assignment id");
			return "error.docx";
		}
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.VERSION))) {
			String version = request.getParameter(FileDownloaderProps.VERSION);
			
			if(version.equals(FileDownloaderProps.A_VERSION)) {
				isVersionA = true;
			}else if(version.equals(FileDownloaderProps.B_VERSION)) {
				isVersionA = false;
			}else {
				log.error("Error in Version");
				return "error.docx";
			}
		}else {
			log.error("Error in Version");
			return "error.docx";
		}
		
		
		if(assignment == null) {
			log.error("Error in assignment null");
			return "error.docx";
		}
			
		/*DocxPaper paper = new DocxPaper(os,assignment,isVersionA);*/
		/*DocxPaperHTML paper = new DocxPaperHTML(os,assignment,isVersionA);*/
		DocxPaperMHTML paper = new DocxPaperMHTML(os,assignment,isVersionA);
		paper.createWordFile();
		
		return paper.getFileName();
	}
	
	private String createXmlPaperForExam(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream os) {
		Integer assignment = null; 
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.ASSIGNMENT))) {
			assignment = Ints.tryParse(request.getParameter(FileDownloaderProps.ASSIGNMENT));	
		}else {
			log.error("Error in assignment id");
			return "error.docx";
		}
		
		
		if(assignment == null) {
			log.error("Error in assignment null");
			return "error.docx";
		}
			
		XmlPaper paper = new XmlPaper(os, assignment);
		paper.createXMLFile();
		
		return paper.getFileName();
	}
}

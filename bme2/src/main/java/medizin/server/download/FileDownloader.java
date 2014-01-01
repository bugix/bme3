package medizin.server.download;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.Person;
import medizin.server.utils.docx.DocxPaperMHTML;
import medizin.server.utils.docx.XmlPaper;
import medizin.server.utils.solutionkey.SolutionKeyZip;
import medizin.shared.utils.FileDownloaderProps;
import medizin.shared.utils.FileDownloaderProps.Method;
import medizin.shared.utils.PersonAccessRight;

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
			
			Person loggedPerson = Person.myGetLoggedPerson(request.getSession());
			PersonAccessRight accessRights = Person.fetchPersonAccessFromSession(request.getSession());
			
			if(loggedPerson == null || accessRights == null) {
				throw new IllegalArgumentException("User Need to login before downloading this Document.");
			}
			
			if(loggedPerson.getIsAdmin() == true || accessRights.getIsInstitutionalAdmin() == true) {
				switch (method) {
				case DOCX_PAPER: 
				{
					fileName = createDocxPaperForExam(request,response,os,loggedPerson);
					break;
				}
				case XML_PAPER:
				{
					fileName = createXmlPaperForExam(request,response,os,loggedPerson);
					break;
				}
				case DOCX_PAPER_ALL:
				{
					fileName = createDocxPaperForExamWithAllQuestions(request,response,os,loggedPerson);
					break;
				}
				case SOLUTION_KEY:
				{
					fileName = createSolutionKey(request,response,os,loggedPerson);
					break;
				}
				default:
					log.error("Error in method ordinal");
					break;
				}

				sendFile(response, os.toByteArray(), fileName);
			}else {
				throw new IllegalArgumentException("User may not have rights to download this Document.");
			}
			
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

	private String createDocxPaperForExam(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream os, Person loggedPerson) {
		
		Integer assignment = null; 
		boolean isVersionA = true;
		final boolean printAllQuestions = false;
		Boolean disallowSorting = null;
		
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
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.DISALLOW_SORTING))) {
			disallowSorting = Boolean.parseBoolean(request.getParameter(FileDownloaderProps.DISALLOW_SORTING));	
		}else {
			log.error("Error in disallow sorting");
			return "error.docx";
		}
			
		//AssesmentQuestion.checkIfShuffleQuestionsAnswersNeeded(assignment,disallowSorting);
		DocxPaperMHTML paper = new DocxPaperMHTML(os,assignment,isVersionA,printAllQuestions,loggedPerson);
		paper.createWordFile();
		
		return paper.getFileName();
	}

	private String createDocxPaperForExamWithAllQuestions(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream os, Person loggedPerson) {
		
		final Integer assignment; 
		final boolean isVersionA = true;
		final boolean printAllQuestions = true;
		Boolean disallowSorting = null;
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.ASSIGNMENT))) {
			assignment = Ints.tryParse(request.getParameter(FileDownloaderProps.ASSIGNMENT));	
		}else {
			log.error("Error in assignment id");
			return "error.docx";
		}
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.DISALLOW_SORTING))) {
			disallowSorting = Boolean.parseBoolean(request.getParameter(FileDownloaderProps.DISALLOW_SORTING));	
		}else {
			log.error("Error in disallow sorting");
			return "error.docx";
		}
		
		//AssesmentQuestion.checkIfShuffleQuestionsAnswersNeeded(assignment,disallowSorting);
		DocxPaperMHTML paper = new DocxPaperMHTML(os,assignment,isVersionA,printAllQuestions,loggedPerson);
		paper.createWordFile();
		
		return paper.getFileName();

	}
	
	private String createXmlPaperForExam(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream os, Person loggedPerson) {
		Integer assignment = null; 
		Boolean disallowSorting = null;
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.ASSIGNMENT))) {
			assignment = Ints.tryParse(request.getParameter(FileDownloaderProps.ASSIGNMENT));	
		}else {
			log.error("Error in assignment id");
			return "error.zip";
		}
		
		
		if(assignment == null) {
			log.error("Error in assignment null");
			return "error.zip";
		}
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.DISALLOW_SORTING))) {
			disallowSorting = Boolean.parseBoolean(request.getParameter(FileDownloaderProps.DISALLOW_SORTING));	
		}else {
			log.error("Error in disallow sorting");
			return "error.zip";
		}
		
		//AssesmentQuestion.checkIfShuffleQuestionsAnswersNeeded(assignment,disallowSorting);
		XmlPaper paper = new XmlPaper(os, assignment,loggedPerson);
		paper.createXMLFile();
		
		return paper.getFileName();
	}
	
	private String createSolutionKey(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream os, Person loggedPerson) {
		Integer assignment = null; 
		Boolean disallowSorting = null;
		
		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.ASSIGNMENT))) {
			assignment = Ints.tryParse(request.getParameter(FileDownloaderProps.ASSIGNMENT));	
		}else {
			log.error("Error in assignment id");
			return "error.zip";
		}
		
		if(assignment == null) {
			log.error("Error in assignment null");
			return "error.zip";
		}

		if(StringUtils.isNotBlank(request.getParameter(FileDownloaderProps.DISALLOW_SORTING))) {
			disallowSorting = Boolean.parseBoolean(request.getParameter(FileDownloaderProps.DISALLOW_SORTING));	
		}else {
			log.error("Error in disallow sorting");
			return "error.zip";
		}
		
		//AssesmentQuestion.checkIfShuffleQuestionsAnswersNeeded(assignment,disallowSorting);
		SolutionKeyZip solutionKeyZip = new SolutionKeyZip(os, assignment,loggedPerson);
		solutionKeyZip.generate();
		
		return solutionKeyZip.getFileName();
	}
}

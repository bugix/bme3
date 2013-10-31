package medizin.server.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import medizin.server.domain.Assesment;
import medizin.server.domain.Student;
import medizin.server.domain.StudentToAssesment;
import medizin.server.utils.BMEUtils;
import medizin.shared.Gender;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;

/**
 * Servlet implementation class CsvFileUploadServlet
 */

@SuppressWarnings("serial")
public class CsvFileUploadServlet extends HttpServlet {
	
	private static Logger Log = Logger.getLogger(CsvFileUploadServlet.class);
		
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
	
		String path="";
		String assesmentId = "";
		File appUploadedFile = null;
		
		if(!ServletFileUpload.isMultipartContent(request)){
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
	            		"Unsupported content");
		}
	
		String uploadDir = BMEUtils.getRealPath(request, SharedConstant.DOWNLOAD_DIR);
		
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
	       
		ProgressListener progressListener = new ProgressListener(){
			private long megaBytes = -1;
			public void update(long pBytesRead, long pContentLength, int pItems) {
				long mBytes = pBytesRead / 1000000;
				if (megaBytes == mBytes) {
					return;
				}
				megaBytes = mBytes;
				Log.info("We are currently reading item " + pItems);
				if (pContentLength == -1) {
					Log.info("So far, " + pBytesRead + " bytes have been read.");
				} else {
					Log.info("So far, " + pBytesRead + " of " + pContentLength
							+ " bytes have been read.");
				}
			}
		};
		
		upload.setProgressListener(progressListener); 
		String fileName = "";
		String temp="";
		
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);
	        	
			for (FileItem item : items)
			{
				if (item.isFormField())
				{
					assesmentId = item.getString();
				}
				else
				{
					temp=FilenameUtils. getName(item.getName());
				}
			}
	           
			fileName = temp;
	            
			for (FileItem item : items) {
				// process only file upload - discard other form item types
				if (item.isFormField())
				{
					continue;
				}
				else
				{
					path = uploadDir + fileName;
					appUploadedFile = new File(uploadDir, fileName);
					
					if(!appUploadedFile.exists())
						FileUtils.touch(appUploadedFile);
						
					appUploadedFile.createNewFile();
					item.write(appUploadedFile);
				}
			}
		} catch (Exception e) {
			Log.info("An error occurred while creating the file : " + e.getMessage());
		}
	     
		Assesment assesment = Assesment.findAssesment(Long.parseLong(assesmentId));
		
		CsvReader student = new CsvReader(path);
		
		student.readHeaders();
		
		String studentIdCol, nameCol, prenameCol, emailCol, streetCol, cityCol, genderCol, studyYearCol;
		
		studentIdCol = nameCol = prenameCol = emailCol = streetCol = cityCol = genderCol = studyYearCol = "";
		
		
		for (int i=0; i<student.getHeaderCount(); i++)
		{
			if (student.getHeader(i).equalsIgnoreCase("student_id"))
				studentIdCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("name"))
				nameCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("prename"))
				prenameCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("email_id"))
				emailCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("street"))
				streetCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("city"))
				cityCol = student.getHeader(i);
			else if (student.getHeader(i).equalsIgnoreCase("gender"))
				genderCol = student.getHeader(i);
		}
		
		if (nameCol.isEmpty() || prenameCol.isEmpty())
		{
			String status = "false";
			appUploadedFile.delete();
			resp.getOutputStream().write(status.getBytes());
		}
		else if (studentIdCol.isEmpty() && emailCol.isEmpty())
		{
			String status = "false";
			appUploadedFile.delete();
			resp.getOutputStream().write(status.getBytes());
		}
		else
		{
			List<Student> studentList = new ArrayList<Student>();
			
			while (student.readRecord())
			{
				
				if (emailCol.isEmpty())
					studentList = Student.findStudentByStudentIdAndByEmail(student.get(studentIdCol), "");
				else if (studentIdCol.isEmpty())
					studentList = Student.findStudentByStudentIdAndByEmail("", student.get(emailCol));
				else
					studentList = Student.findStudentByStudentIdAndByEmail(student.get(studentIdCol), student.get(emailCol));
					
				
				if (studentList.size() == 0)
				{
					Student s=new Student();
					
					if (!studentIdCol.isEmpty())
						s.setStudentId(student.get(studentIdCol));
					
					if (!nameCol.isEmpty())
						s.setName(student.get(nameCol));
					
					if (!prenameCol.isEmpty())
						s.setPreName(student.get(prenameCol));
					
					if (!emailCol.isEmpty())
						s.setEmail(student.get(emailCol));
					
					if (!streetCol.isEmpty())
						s.setStreet(student.get(streetCol));
					
					if (!cityCol.isEmpty())
						s.setCity(student.get(cityCol));
					
					if (!genderCol.isEmpty())
						s.setGender(Gender.valueOf(student.get(genderCol).toUpperCase()));
				
					s.persist();
					
					Long assessmentCount = 0l;
					assessmentCount = StudentToAssesment.findStudentByStudIdAndAssesmentId(s.getId(), Long.parseLong(assesmentId));
					
					if (assessmentCount == 0)
					{
						StudentToAssesment studentToAssesment = new StudentToAssesment();
						studentToAssesment.setIsEnrolled(true);
						studentToAssesment.setStudent(s);
						studentToAssesment.setAssesment(assesment);
						studentToAssesment.persist();
					}
				}
				else
				{
					Long oscecount = StudentToAssesment.findStudentByStudIdAndAssesmentId(studentList.get(0).getId(), Long.parseLong(assesmentId));
		         		
					if (oscecount == 0)
					{
						StudentToAssesment studentToAssesment = new StudentToAssesment();
						studentToAssesment.setIsEnrolled(true);
						studentToAssesment.setStudent(studentList.get(0));
						studentToAssesment.setAssesment(assesment);
						studentToAssesment.persist();
					}	
				}
			}
			
			String status = "true";
			resp.getOutputStream().write(status.getBytes());
		}
		try
		{
			Log.info("~~Deleted : " + appUploadedFile.delete());
		}
		catch (Exception e) {
			e.printStackTrace();
	}
	
	}
}

package medizin.server.cronjob;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import medizin.server.domain.Assesment;
import medizin.server.domain.Person;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

public class SendMailJob implements Job {

	private static Logger log = Logger.getLogger(SendMailJob.class);
	private static final String PROPERTIES_FILE_PATH = new File(SendMailJob.class.getResource("CronJob.properties").getPath()).getAbsolutePath();
	private static String oldExpressionValue = "";
	private String mailSubject = "Mail Subject";
	private String mailFromName = "";
	private String mailFromAddress = "";
	private ServletContext servletContext;
	
	
	@Override
	public void execute(JobExecutionContext jobExeContext) throws JobExecutionException {
		try
		{
			servletContext = (ServletContext) jobExeContext.getJobDetail().getJobDataMap().get("servletContext");
			File file = new File(PROPERTIES_FILE_PATH);
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			String expressionValue = properties.getProperty("emailJobExpression");
			mailSubject = properties.getProperty("mailSubject");
			mailFromName = properties.getProperty("mailFromName");
			mailFromAddress = properties.getProperty("mailFromAddress");
			System.out.println("EXPRESSION VALUE : " + expressionValue);
			
			if (oldExpressionValue.isEmpty())
			{
				oldExpressionValue = expressionValue;
				sendMail();
			}
			else if (!oldExpressionValue.equals(expressionValue))
			{
				oldExpressionValue = expressionValue;
				jobExeContext.getScheduler().clear();
				new SendMailTrigger(jobExeContext.getScheduler(), servletContext);
			}
			else
			{
				sendMail();
			}			
		}
		catch(Exception e)
		{
			log.error(e);
		}
				
	}
	
	@Transactional
	public void sendMail()
	{
		List<Person> personList = Person.findAllPeople();
		String templateContent = loadSystemOverviewTemplate();
		
		for (Person person : personList) {
			List<Assesment> assessmentList = Assesment.findAssessmentForJob(person.getId());
			
			if (assessmentList != null && assessmentList.size() > 0)
			{
				if (StringUtils.isNotBlank(mailFromAddress))
				{
					Assesment.sendMail(templateContent, assessmentList, templateContent, mailSubject, person, mailFromName, mailFromAddress, servletContext);
				}
			}
		}
		
	}

	public String loadSystemOverviewTemplate()
	{
		String filePath = servletContext.getRealPath(SharedConstant.SYSTEM_OVERVIEW_MAIL_TEMPLATE);
		File file=new File(filePath);
		try{
			return FileUtils.readFileToString(file);
		}catch(IOException e)
		{
			log.error(e);
			return "";
		}
	}
}

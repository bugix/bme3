package medizin.server.cronjob;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import medizin.server.domain.Assesment;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class SendMailTrigger {
	
	private static Logger log = Logger.getLogger(SendMailTrigger.class);
	
	private static final String PROPERTIES_FILE_PATH = new File(SendMailTrigger.class.getResource("CronJob.properties").getPath()).getAbsolutePath();
	
	public SendMailTrigger(Scheduler scheduler, ServletContext servletContext) {
		
		try
		{
			File file = new File(PROPERTIES_FILE_PATH);
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			
			String expressionValue = properties.getProperty("emailJobExpression");
			
			if (expressionValue != null)
			{
				JobKey p1JobKey = new JobKey("JobP1", "groupP1");
				JobDetail job = JobBuilder.newJob(SendMailJob.class).withIdentity(p1JobKey).build();
				job.getJobDataMap().put("servletContext", servletContext);
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("triggerP1", "groupP1").
						withSchedule(CronScheduleBuilder.cronSchedule(expressionValue)).build();
				scheduler.start();
				scheduler.scheduleJob(job, trigger);
			}			
		}
		catch (Exception e) {
			log.error(e);
		}
	}
}

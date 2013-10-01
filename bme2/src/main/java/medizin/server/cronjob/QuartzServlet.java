package medizin.server.cronjob;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Servlet implementation class QuartzServlet
 */
public class QuartzServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Scheduler scheduler;
	private static Logger log = Logger.getLogger(QuartzServlet.class);	
	
	public QuartzServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			new SendMailTrigger(scheduler,config.getServletContext());
		} catch (SchedulerException e) {
			log.error(e);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if(scheduler != null) {
			try {
				scheduler.shutdown();
			} catch (SchedulerException e) {
				log.error(e);
			}
		}
	}
}

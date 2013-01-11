package medizin.client;

import medizin.client.ioc.McInjector;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class AppEntryPoint implements EntryPoint {

	final private McInjector injectorWrapper = GWT.create(McInjector.class);
	
	public void onModuleLoad() {
		

		/*
		 * Install an UncaughtExceptionHandler which will produce
		 * <code>FATAL</code> log messages
		 */
		Log.setUncaughtExceptionHandler();

		/*
		 * Use a deferred command so that the UncaughtExceptionHandler catches
		 * any exceptions in onModuleLoad2()
		 */
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				onModuleLoad2();				
			}
		});
		
		/*DeferredCommand.addCommand(new Command() {
			public void execute() {
				onModuleLoad2();
			}

		});
*/
	}

	private void onModuleLoad2() {
		Log.debug("Application starts...");
		injectorWrapper.getApplication().run();
		// new McAppFactory().getMcApp().run(RootLayoutPanel.get());

	}
}
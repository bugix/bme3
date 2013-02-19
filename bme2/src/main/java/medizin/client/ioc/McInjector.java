package medizin.client.ioc;

import medizin.client.McApplication;
import medizin.client.ui.widget.process.AppLoader;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = {McGinModule.class})
public interface McInjector extends Ginjector {

	McApplication getApplication();
	AppLoader getAppLoader();
}
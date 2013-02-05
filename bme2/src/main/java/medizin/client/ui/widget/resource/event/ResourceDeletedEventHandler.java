package medizin.client.ui.widget.resource.event;

import com.google.gwt.event.shared.EventHandler;

public interface ResourceDeletedEventHandler extends EventHandler {

	void onResourceDeleted(ResourceDeletedEvent event);
}

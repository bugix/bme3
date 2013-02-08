package medizin.client.ui.widget.resource.event;

import com.google.gwt.event.shared.EventHandler;

public interface ResourceSequenceChangedHandler extends EventHandler {

	void onSequenceChanged(ResourceSequenceChangedEvent event);
}

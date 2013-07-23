package medizin.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface RecordChangeHandler extends EventHandler {
	void onRecordChange(RecordChangeEvent event);
}

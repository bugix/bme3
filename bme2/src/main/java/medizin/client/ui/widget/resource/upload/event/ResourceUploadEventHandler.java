package medizin.client.ui.widget.resource.upload.event;

import com.google.gwt.event.shared.EventHandler;

public interface ResourceUploadEventHandler extends EventHandler {

	void onResourceUploaded(ResourceUploadEvent event);
}

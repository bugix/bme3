package medizin.client.ui.widget.resource.upload.event;

import com.google.gwt.event.shared.GwtEvent;

public class ResourceUploadEvent extends GwtEvent<ResourceUploadEventHandler> {

	private final boolean resourceUploaded;
	private final String fileName;
	
	public ResourceUploadEvent(String fileName, Boolean resourceUploaded) {
		this.resourceUploaded = resourceUploaded;
		this.fileName = fileName;
	}
	public static Type<ResourceUploadEventHandler> TYPE = new Type<ResourceUploadEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ResourceUploadEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResourceUploadEventHandler handler) {
		handler.onResourceUploaded(this);		
	}

	public boolean isResourceUploaded() {
		return resourceUploaded;
	}
	
	public String getFileName() {
		return fileName;
	}
}

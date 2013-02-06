package medizin.client.ui.widget.resource.event;

import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;

import com.google.gwt.event.shared.GwtEvent;

public class ResourceAddedEvent extends GwtEvent<ResourceAddedEventHandler> {

	private final QuestionResourceClient questionResource;
	private final boolean added;

	public ResourceAddedEvent(boolean added,QuestionResourceClient questionResource) {
		this.added = added;
		this.questionResource = questionResource;
	}
	public static Type<ResourceAddedEventHandler> TYPE = new Type<ResourceAddedEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ResourceAddedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResourceAddedEventHandler handler) {
		handler.onResourceAdded(this);		
	}

	public QuestionResourceClient getQuestionResourceClient() {
		return questionResource;
	}

	public boolean isAdded() {
		return added;
	}
	
}

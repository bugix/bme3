package medizin.client.ui.widget.resource.event;

import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;

import com.google.gwt.event.shared.GwtEvent;

public class ResourceDeletedEvent extends GwtEvent<ResourceDeletedEventHandler> {

	private final QuestionResourceClient questionResource;

	public ResourceDeletedEvent(QuestionResourceClient questionResource) {
		this.questionResource = questionResource;
	}
	public static Type<ResourceDeletedEventHandler> TYPE = new Type<ResourceDeletedEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ResourceDeletedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResourceDeletedEventHandler handler) {
		handler.onResourceDeleted(this);		
	}

	public QuestionResourceClient getQuestionResourceClient() {
		return questionResource;
	}

}

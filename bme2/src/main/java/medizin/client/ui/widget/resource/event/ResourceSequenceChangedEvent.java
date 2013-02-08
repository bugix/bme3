package medizin.client.ui.widget.resource.event;

import java.util.Set;

import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;

import com.google.gwt.event.shared.GwtEvent;

public class ResourceSequenceChangedEvent extends GwtEvent<ResourceSequenceChangedHandler> {

	private final Set<QuestionResourceClient> clients;

	public ResourceSequenceChangedEvent(Set<QuestionResourceClient> clients) {
		this.clients = clients;
	}
	public static Type<ResourceSequenceChangedHandler> TYPE = new Type<ResourceSequenceChangedHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ResourceSequenceChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResourceSequenceChangedHandler handler) {
		handler.onSequenceChanged(this);		
	}

	public Set<QuestionResourceClient> getQuestionResourceClients() {
		return clients;
	}
	
}

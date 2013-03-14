package medizin.client.events;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class QuestionSaveEvent extends GwtEvent<QuestionSaveEvent.Handler> {

	public static Type<Handler> TYPE = new Type<Handler>();
	
	public interface Handler extends EventHandler {
		void onSaveClicked(QuestionSaveEvent event);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onSaveClicked(this);
	}

	public static HandlerRegistration register(EventBus eventBus, QuestionSaveEvent.Handler handler) {
		return eventBus.addHandler(TYPE, handler);
	}
}

package medizin.client.ui.widget.process;

import medizin.client.factory.request.RequestEvent;
import medizin.client.factory.request.RequestEvent.Handler;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class AppLoader {

	private static int eventCounter = 0;

	// private boolean show;

	public static void initialCounter() {
		eventCounter = 0;
	}

	public void display(boolean show) {

		if (show) {
			eventCounter++;
			ApplicationLoadingPopupView.showApplicationLoadingPopup(show);
		} else {
			eventCounter--;
			if (eventCounter <= 0) {
				eventCounter = 0;
				ApplicationLoadingPopupView.showApplicationLoadingPopup(false);
			}
		}
	}

	@Inject
	public AppLoader(EventBus eventBus) {

		RequestEvent.register(eventBus, new Handler() {

			@Override
			public void onRequestEvent(RequestEvent event) {

				RequestEvent.State state = event.getState();

				switch (state) {
				case SENT: {
					display(true);
//					Log.info("state : SENT ");
					break;
				}
				case RECEIVED: {
					display(false);
//					Log.info("state : RECEIVED ");
					break;
				}
				default: {
					display(false);
					Log.info("state default or Invalid : Error ");
					break;
				}
				}

			}
		});

	}
}
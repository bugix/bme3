package medizin.client.ui.widget.process;

import medizin.client.factory.request.RequestEvent;
import medizin.client.factory.request.RequestEvent.Handler;
import medizin.client.util.MathJaxs;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public class AppLoader {

	private static int eventCounter = 0;
	private static boolean showLoader = true;
	private static final int LOADING_TIMEOUT = 50;
	// private boolean show;
	
	private final Timer showTimer = new Timer() {
	    @Override
	    public void run() {
	      if (eventCounter > 0) {
	    	  ApplicationLoadingPopupView.showApplicationLoadingPopup(true);
	      }
	    }
	};

	public static boolean isShowLoader() {
		return showLoader;
	}

	public static void setShowLoader(boolean showLoader) {
		AppLoader.showLoader = showLoader;
	}

	public static void initialCounter() {
		eventCounter = 0;
	}

	public void display(boolean show) {

		if (show) {
			eventCounter++;
			showTimer.schedule(LOADING_TIMEOUT);
			//ApplicationLoadingPopupView.showApplicationLoadingPopup(show);
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
				MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());

				RequestEvent.State state = event.getState();
				if (showLoader) {
	

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
			}
		});

	}
	
}
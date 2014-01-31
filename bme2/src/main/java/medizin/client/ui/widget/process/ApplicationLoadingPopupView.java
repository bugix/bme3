package medizin.client.ui.widget.process;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationLoadingPopupView extends DialogBox {

	private static ApplicationLoadingPopupViewImplUiBinder uiBinder = GWT.create(ApplicationLoadingPopupViewImplUiBinder.class);

	interface ApplicationLoadingPopupViewImplUiBinder extends UiBinder<Widget, ApplicationLoadingPopupView> {
	}

	static private ApplicationLoadingPopupView applicationLoadingPopupViewImpl;

	private ApplicationLoadingPopupView() {

		setWidget(uiBinder.createAndBindUi(this));
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setAutoHideEnabled(false);

		this.getElement().removeClassName("gwt-DialogBox");
		this.getElement().getStyle().setZIndex(1);
		center();

	}

	public static void showApplicationLoadingPopup(boolean show) {

		if (show) {
			if (applicationLoadingPopupViewImpl == null) {
				applicationLoadingPopupViewImpl = new ApplicationLoadingPopupView();
			}
			if (!applicationLoadingPopupViewImpl.isShowing()) {
				showCurrentLoading();

			}
		} else if (applicationLoadingPopupViewImpl != null) {
			hideAll();

		}
	}

	private static void hideAll() {
		applicationLoadingPopupViewImpl.hide();
		if (AppLoader.currentLoaders != null && AppLoader.currentLoaders.size() > 0) {
			Log.info("IN custom loader hide");
			for (ApplicationLoadingView loadingView : AppLoader.currentLoaders) {
				loadingView.setVisible(false);
			}
			AppLoader.currentLoaders.clear();
		}
	}

	private static void showCurrentLoading() {
		if (AppLoader.currentLoaders != null && AppLoader.currentLoaders.size() > 0) {
			Log.info("IN custom loader");
			Log.info("size : " + AppLoader.currentLoaders.size());
			for (ApplicationLoadingView loadingView : AppLoader.currentLoaders) {
				loadingView.setVisible(true);
			}
		} else {
			Log.info("Full loader show");
			applicationLoadingPopupViewImpl.show();
		}
	}

}
package medizin.client.ui.widget.process;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationLoadingPopupView extends DialogBox {

	private static ApplicationLoadingPopupViewImplUiBinder uiBinder = GWT
			.create(ApplicationLoadingPopupViewImplUiBinder.class);

	interface ApplicationLoadingPopupViewImplUiBinder extends
			UiBinder<Widget, ApplicationLoadingPopupView> {
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
				applicationLoadingPopupViewImpl.show();
			}
		} else if (applicationLoadingPopupViewImpl != null) {
			applicationLoadingPopupViewImpl.hide();
		}
	}

}
package medizin.client.ui.widget.process;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationLoadingView extends Widget {

	private ApplicationLoadingViewImplUiBinder uiBinder = GWT.create(ApplicationLoadingViewImplUiBinder.class);

	interface ApplicationLoadingViewImplUiBinder extends UiBinder<Widget, ApplicationLoadingView> {
	}

	public static ApplicationLoadingView EMPTY_LOADER = new ApplicationLoadingView();
	
	public ApplicationLoadingView() {
		this.setElement(uiBinder.createAndBindUi(this).getElement());
		this.getElement().getStyle().setZIndex(1);
		this.getElement().getStyle().setPosition(Position.ABSOLUTE);
		this.getElement().getStyle().setWidth(100, Unit.PCT);
		this.getElement().getStyle().setHeight(100, Unit.PCT);
		this.getElement().getStyle().setOpacity(0.5);
		this.getElement().getStyle().setBackgroundColor("grey");
		this.getElement().getStyle().setProperty("borderRadius", "5px");
	}

}
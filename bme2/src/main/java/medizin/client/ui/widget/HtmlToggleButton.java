package medizin.client.ui.widget;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.ToggleButton;

public class HtmlToggleButton extends ToggleButton {
	
	public HtmlToggleButton(SafeHtml up, SafeHtml down) {
		getDownFace().setHTML(down);
		getUpFace().setHTML(up);
	}
}

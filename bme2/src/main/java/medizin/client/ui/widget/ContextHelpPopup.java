package medizin.client.ui.widget;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ContextHelpPopup extends Composite {
	private PopupPanel popup;
	
	public ContextHelpPopup() {
		this(null);
	}

	public ContextHelpPopup(String helpText) {
		popup = new PopupPanel(false);
		popup.setStyleName("unibas-ContextHelp");
		if (helpText != null) {
			popup.add(new Label(helpText));
		}
	}
	
	/**
	 * Set the text to be shown in the popup.
	 * @param helpText
	 */
	public void setHelpText(String helpText) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder().appendHtmlConstant(helpText);
		popup.clear();
		popup.add(new HTML(sb.toSafeHtml()));
	}
	
	public void setPopupPosition(int left, int top) {
		popup.setPopupPosition(left, top);
	}
	
	/**
	 * Shows the popup. If the popup overflows the viewport, it will immediately be 
	 * moved up by the difference.
	 */
	public void show() {
		RootPanel rp = RootPanel.get();
		popup.show();
		int diff = rp.getAbsoluteTop() + rp.getOffsetHeight() - (popup.getPopupTop() + popup.getOffsetHeight());
		if (diff < 0) {
			popup.setPopupPosition(popup.getPopupLeft(), popup.getPopupTop() + diff - 20);
		}
	}
	
	public void hide() {
		popup.hide();
	}
	
}

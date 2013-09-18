package medizin.client.ui.widget.handler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Focusable;

/**
 * A Focushandler that delegates Focus to the wrapped object.
 * @author mwagner
 *
 */

public class FocusDelegatingHandler implements FocusHandler, ClickHandler, MouseOverHandler {
	private Focusable box;
	
	public FocusDelegatingHandler(Focusable box) {
		this.box = box;
	}
	
	private void focusTextBox() {
		this.box.setFocus(true);
	}
	
	@Override
	public void onFocus(FocusEvent event) {
		focusTextBox();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		focusTextBox();
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		focusTextBox();
	}
}

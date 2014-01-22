package medizin.client.ui.widget.handler;
import java.util.ArrayList;
import java.util.List;
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
	private List<Focusable> exceptions = new ArrayList<Focusable>();
	
	public FocusDelegatingHandler(Focusable box) {
		this.box = box;
	}
	
	public boolean addException(Focusable exception) {
		return exceptions.add(exception);
	}
	
	public boolean removeException(Focusable exception) {
		return exceptions.remove(exception);
	}
	
	private void focusTextBox(Object eventSrc) {
		if (eventSrc instanceof Focusable && exceptions.contains((Focusable)eventSrc)) {
			return;
		}
		this.box.setFocus(true);
	}
	
	@Override
	public void onFocus(FocusEvent event) {
		focusTextBox(event.getSource());
	}
	
	@Override
	public void onClick(ClickEvent event) {
		focusTextBox(event.getSource());
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		focusTextBox(event.getSource());
	}
}

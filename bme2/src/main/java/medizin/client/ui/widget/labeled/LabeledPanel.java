package medizin.client.ui.widget.labeled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import medizin.client.ui.widget.ContextHelpPopup;
import medizin.client.ui.widget.HasContextHelp;
import medizin.client.ui.widget.handler.FocusDelegatingHandler;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LabeledPanel extends Composite implements HasWidgets, Focusable, HasFocusHandlers, HasBlurHandlers, HasContextHelp {
	private Map<Widget, List<HandlerRegistration>> widgetsWithHandlers = new HashMap<Widget, List<HandlerRegistration>>();
	
	protected final String styleName = "unibas-LabelledTextBox";
	protected final String focusStyleName = "unibas-LabelledTextBox-focused";
	
	private FocusPanel wrapperPanel;
	private VerticalPanel parentPanel;
	private HorizontalPanel mainPanel;
	private Label label;

	protected boolean hasContextHelpHandlers = false;
	protected ContextHelpPopup popup;
		
	public LabeledPanel() {
		mainPanel = new HorizontalPanel();
		parentPanel = new VerticalPanel();
		wrapperPanel = new FocusPanel();
		label = new Label();
		
		parentPanel.add(label);
		wrapperPanel.add(parentPanel);
		parentPanel.add(mainPanel);
		
		initWidget(wrapperPanel);
		wrapperPanel.setStyleName(styleName);
		parentPanel.setWidth("100%");
//		mainPanel.setWidth("100%");
		label.setStyleName("unibas-LabelledTextBox-Label");
	}
	
	public void setLabelText(String label) {
		this.label.setText(label);
	}
	
	public Label getLabel() {
		return label;
	}
	
	// TODO: WrapperPanel will probably be blurred as soon as it is focused because of the focus delegation. the blur handler should be added to the widgets!
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		for (Entry<Widget,List<HandlerRegistration>> entry: widgetsWithHandlers.entrySet()) {
			if (entry.getValue().size() > 0) {
				((HasBlurHandlers)entry.getKey()).addBlurHandler(handler);
			}
		}
		return wrapperPanel.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return wrapperPanel.addFocusHandler(handler);
	}

	@Override
	public int getTabIndex() {
		return wrapperPanel.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		wrapperPanel.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		wrapperPanel.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		wrapperPanel.setTabIndex(index);
	}

	@Override
	public void add(Widget w) {
		List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();
		mainPanel.add(w);
		
		if (w instanceof Focusable) {
			if (widgetsWithHandlers.size() < 1) {
				FocusDelegatingHandler handler = new FocusDelegatingHandler((Focusable) w);		
				handlers.add(wrapperPanel.addClickHandler(handler));
				handlers.add(wrapperPanel.addFocusHandler(handler));
				handlers.add(wrapperPanel.addMouseOverHandler(handler));
			}
			
			if (w instanceof HasFocusHandlers) {
				handlers.add(((HasFocusHandlers) w).addFocusHandler(new FocusHandler() {
					
					@Override
					public void onFocus(FocusEvent event) {
						wrapperPanel.addStyleName(focusStyleName);
					}
				}));
			}
			
			if (w instanceof HasBlurHandlers) {
				handlers.add(((HasBlurHandlers) w).addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						wrapperPanel.removeStyleName(focusStyleName);
					}
				}));
			}
		}
		
		widgetsWithHandlers.put(w, handlers);
	}

	@Override
	public void clear() {
		Iterator<Entry<Widget, List<HandlerRegistration>>> it = widgetsWithHandlers.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Widget, List<HandlerRegistration>> entry = it.next();
			for (HandlerRegistration reg : entry.getValue()) {
				reg.removeHandler();
			}
			mainPanel.remove(entry.getKey());
			it.remove();
		}
	}

	@Override
	public Iterator<Widget> iterator() {
		return widgetsWithHandlers.keySet().iterator();
	}

	@Override
	public boolean remove(Widget w) {
		if (widgetsWithHandlers.containsKey(w)) {
			for (HandlerRegistration reg : widgetsWithHandlers.get(w)) {
				reg.removeHandler();
			}
			widgetsWithHandlers.remove(w);
			return mainPanel.remove(w);
		}
		return false;
	}
	
	private void addContextHelpHandlers() {
		if (!hasContextHelpHandlers) {
			popup = new ContextHelpPopup();
			wrapperPanel.addFocusHandler(new FocusHandler() {
				
				@Override
				public void onFocus(FocusEvent event) {
					popup.setPopupPosition(wrapperPanel.getAbsoluteLeft() + wrapperPanel.getOffsetWidth() + 30 , wrapperPanel.getAbsoluteTop());
					popup.show();
				}
			});
			wrapperPanel.addBlurHandler(new BlurHandler() {
				
				@Override
				public void onBlur(BlurEvent event) {
					popup.hide();
				}
			});
			hasContextHelpHandlers = true;
		}
	}

	@Override
	public void setHelpText(String helpText) {
		addContextHelpHandlers();
		popup.setHelpText(helpText);
	}
}

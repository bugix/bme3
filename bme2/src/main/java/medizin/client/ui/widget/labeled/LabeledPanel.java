package medizin.client.ui.widget.labeled;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LabeledPanel extends Composite implements HasWidgets, Focusable, HasFocusHandlers, HasBlurHandlers, HasContextHelp {
	private List<Widget> children = new ArrayList<Widget>();
	
	private final String styleName = "unibas-LabelledTextBox";
	private final String focusStyleName = "unibas-LabelledTextBox-focused";
	private final String helpStyleName = "unibas-LabelledTextBox-with-help";
	
	private FocusPanel wrapperPanel;
	private VerticalPanel parentPanel;
	private HorizontalPanel mainPanel;
	private Label label;

	private boolean hasContextHelpHandlers = false;
	private ContextHelpPopup popup;
	
	public static class DelegatingHandlerRegistration implements HandlerRegistration {
		private List<HandlerRegistration> regs = new ArrayList<HandlerRegistration>();
		
		public DelegatingHandlerRegistration(List<HandlerRegistration> regs) {
			this.regs = regs;
		}
		
		@Override
		public void removeHandler() {
			for (HandlerRegistration reg : regs) {
				reg.removeHandler();
			}
		}
	}
		
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
		List<HandlerRegistration> regs = new ArrayList<HandlerRegistration>();
		for (Widget w : children) {
			if (w instanceof HasBlurHandlers) {
				regs.add(((HasBlurHandlers)w).addBlurHandler(handler));
			}
		}
		regs.add(wrapperPanel.addBlurHandler(handler));
		return new DelegatingHandlerRegistration(regs);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		List<HandlerRegistration> regs = new ArrayList<HandlerRegistration>();
		for (Widget w : children) {
			if (w instanceof HasFocusHandlers) {
				regs.add(((HasFocusHandlers)w).addFocusHandler(handler));
			}
		}
		regs.add(wrapperPanel.addFocusHandler(handler));
		return new DelegatingHandlerRegistration(regs);
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
	
	// TODO / FIXME : add() should have no side FX; styles should not be added 
	
	@Override
	public void add(Widget w) {
		mainPanel.add(w);
		
		if (w instanceof Focusable) {
			if (children.size() < 1) {
				FocusDelegatingHandler handler = new FocusDelegatingHandler((Focusable) w);		
				wrapperPanel.addClickHandler(handler);
				wrapperPanel.addFocusHandler(handler);
			}
			
			if (w instanceof HasFocusHandlers) {
				((HasFocusHandlers) w).addFocusHandler(new FocusHandler() {
					
					@Override
					public void onFocus(FocusEvent event) {
						wrapperPanel.addStyleName(focusStyleName);
					}
				});
			}
			
			if (w instanceof HasBlurHandlers) {
				((HasBlurHandlers) w).addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						wrapperPanel.removeStyleName(focusStyleName);
					}
				});
			}
		}
		
		children.add(w);
	}

	@Override
	public void clear() {
		Iterator<Widget> it = children.iterator();
		while (it.hasNext()) {
			Widget w = it.next();
			mainPanel.remove(w);
			it.remove();
		}
	}

	@Override
	public Iterator<Widget> iterator() {
		return children.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return children.remove(w);
	}
	
	private void addContextHelpHandlers() {
		if (!hasContextHelpHandlers) {
			popup = new ContextHelpPopup();
			addFocusHandler(new FocusHandler() {
				
				@Override
				public void onFocus(FocusEvent event) {
					popup.setPopupPosition(wrapperPanel.getAbsoluteLeft() + wrapperPanel.getOffsetWidth() + 30 , wrapperPanel.getAbsoluteTop());
					popup.show();
				}
			});
			addBlurHandler(new BlurHandler() {
				
				@Override
				public void onBlur(BlurEvent event) {
					popup.hide();
				}
			});
			hasContextHelpHandlers = true;
		}
		removeStyleName(styleName);
		addStyleName(helpStyleName);
	}

	@Override
	public void setHelpText(String helpText) {
		addContextHelpHandlers();
		popup.setHelpText(helpText);
	}
}

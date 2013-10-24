package medizin.client.ui.widget.labeled;

import java.util.Collection;

import medizin.client.ui.widget.ContextHelpPopup;
import medizin.client.ui.widget.FocusableValueListBox;
import medizin.client.ui.widget.HasContextHelp;
import medizin.client.ui.widget.handler.FocusDelegatingHandler;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;

public class LabeledValueListBox<T> extends Composite implements Focusable, HasFocusHandlers, HasBlurHandlers, HasValue<T>, IsEditor<TakesValueEditor<T>>, HasContextHelp  {
	protected final String styleName = "unibas-LabelledTextBox";
	protected final String focusStyleName = "unibas-LabelledTextBox-focused";
	protected final String helpStyleName = "unibas-LabelledTextBox-with-help";
	private FocusableValueListBox<T> valueListBox;
	protected Label label;
	protected FocusPanel wrapper;
	protected VerticalPanel panel;
	protected boolean hasContextHelpHandlers = false;
	protected ContextHelpPopup popup;
	
	public LabeledValueListBox(AbstractRenderer<T> renderer) {
		valueListBox = new FocusableValueListBox<T>(renderer);
		init();
	}
	
	public LabeledValueListBox(Renderer<T> renderer, ProvidesKey<T> keyProvider) {
		valueListBox = new FocusableValueListBox<T>(renderer, keyProvider);
		init();
	}
	
	private void init() {
		panel = new VerticalPanel();
		wrapper = new FocusPanel();
		label = new Label();
		
		panel.add(label);
		panel.add(valueListBox);
		wrapper.add(panel);
		
		FocusDelegatingHandler handler = new FocusDelegatingHandler(valueListBox);		
		wrapper.addClickHandler(handler);
		wrapper.addFocusHandler(handler);
//		wrapper.addMouseOverHandler(handler);
		
		valueListBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				wrapper.addStyleName(focusStyleName);
			}
		});
		
		valueListBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				wrapper.removeStyleName(focusStyleName);
			}
		});
		
		initWidget(wrapper);
		wrapper.setStyleName(styleName);
		panel.setWidth("100%");
		label.setStyleName("unibas-LabelledTextBox-Label");
	}
	
	public void setLabelText(String label) {
		this.label.setText(label);
	}
	
	public Label getLabel() {
		return label;
	}
	
	public FocusableValueListBox<T> getValueListBox() {
		return valueListBox;
	}
	
	public void setAcceptableValues(Collection<T> acceptableValues) {
		valueListBox.setAcceptableValues(acceptableValues);
	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		return valueListBox.addValueChangeHandler(handler);
	}

	@Override
	public TakesValueEditor<T> asEditor() {
		return valueListBox.asEditor();
	}

	@Override
	public T getValue() {
		return valueListBox.getValue();
	}

	@Override
	public void setValue(T value) {
		valueListBox.setValue(value);
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		valueListBox.setValue(value, fireEvents);
	}

	@Override
	public int getTabIndex() {
		return valueListBox.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		valueListBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		valueListBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		valueListBox.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return valueListBox.addFocusHandler(handler);
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return valueListBox.addBlurHandler(handler);
	}
	
	public void setEnabled(boolean enabled) {
		DOM.setElementPropertyBoolean(valueListBox.getElement(), "disabled", !enabled);
	}
	
	@Override
	public void setWidth(String width) {
		wrapper.setWidth(width);
		panel.setWidth(width);
		super.setWidth(width);
	}

	@Override
	public void setHelpText(String helpText) {
		addContextHelpHandlers();
		popup.setHelpText(helpText);
	}
	
	private void addContextHelpHandlers() {
		if (!hasContextHelpHandlers) {
			popup = new ContextHelpPopup();
			valueListBox.addFocusHandler(new FocusHandler() {
				
				@Override
				public void onFocus(FocusEvent event) {
					popup.setPopupPosition(wrapper.getAbsoluteLeft() + wrapper.getOffsetWidth() + 30 , wrapper.getAbsoluteTop());
					popup.show();
				}
			});
			valueListBox.addBlurHandler(new BlurHandler() {
				
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
}

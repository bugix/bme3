package medizin.client.ui.widget.labeled;

import medizin.client.ui.widget.handler.FocusDelegatingHandler;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class LabeledValueBox<T> extends Composite implements Focusable, HasFocusHandlers, HasBlurHandlers, HasValue<T>, IsEditor<ValueBoxEditor<T>> {
	
	protected final String styleName = "unibas-LabelledTextBox";
	protected final String focusStyleName = "unibas-LabelledTextBox-focused";
	protected ValueBoxBase<T> valueBox;
	protected Label label;
	protected FocusPanel wrapper;
	protected VerticalPanel panel;
	
	protected LabeledValueBox(ValueBoxBase<T> valueBox) {
		this.valueBox = valueBox;
		init();
	}
	
	private void init() {
		panel = new VerticalPanel();
		wrapper = new FocusPanel();
		label = new Label();
		
		panel.add(label);
		panel.add(valueBox);
		wrapper.add(panel);
		
		FocusDelegatingHandler handler = new FocusDelegatingHandler(valueBox);
		wrapper.addClickHandler(handler);
		wrapper.addFocusHandler(handler);
		wrapper.addMouseOverHandler(handler);
		
		valueBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				wrapper.addStyleName(focusStyleName);
			}
		});
		
		valueBox.addBlurHandler(new BlurHandler() {
			
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
	
	public ValueBoxBase<T> getValueBox() {
		return valueBox;
	}
		
	public void setText(String text) {
		valueBox.setText(text);
	}
	
	public String getText() {
		return valueBox.getText();
	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		return valueBox.addValueChangeHandler(handler);
	}

	@Override
	public ValueBoxEditor<T> asEditor() {
		return valueBox.asEditor();
	}

	@Override
	public T getValue() {
		return valueBox.getValue();
	}

	@Override
	public void setValue(T value) {
		valueBox.setValue(value);
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		valueBox.setValue(value, fireEvents);
	}

	@Override
	public int getTabIndex() {
		return valueBox.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		valueBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		valueBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		valueBox.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return wrapper.addFocusHandler(handler);
	}
	
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return valueBox.addBlurHandler(handler);
	}
		
	@Override
	public void setWidth(String width) {
		wrapper.setWidth(width);
		panel.setWidth(width);
		super.setWidth(width);
	}
}

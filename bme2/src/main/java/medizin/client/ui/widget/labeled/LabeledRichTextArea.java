package medizin.client.ui.widget.labeled;

import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.widget.ContextHelpPopup;
import medizin.client.ui.widget.HasContextHelp;
import medizin.client.ui.widget.handler.FocusDelegatingHandler;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LabeledRichTextArea extends Composite implements Focusable, HasFocusHandlers, HasBlurHandlers, HasContextHelp, HasHTML, HasSafeHtml {
	
	protected final String styleName = "unibas-LabeledTextBox";
	protected final String focusStyleName = "unibas-LabeledTextBox-focused";
	protected final String helpStyleName = "unibas-LabeledTextBox-with-help";
	
	protected RichTextArea rtArea;
	protected RichTextToolbar rtToolbar;
	protected HTML charCounter;
	
	protected Label label;
	protected FocusPanel wrapper;
	protected VerticalPanel panel;
	protected boolean hasContextHelpHandlers = false;
	protected ContextHelpPopup popup;
	
	protected int maxCharCount = 0;
	protected String countPrefix = "";
		
	protected LabeledRichTextArea() {
		rtArea = new RichTextArea();
		rtToolbar = new RichTextToolbar(rtArea);
		charCounter = new HTML();
		
		panel = new VerticalPanel();
		wrapper = new FocusPanel();
		label = new Label();
		
		panel.add(label);
		panel.add(rtToolbar);
		panel.add(rtArea);
		panel.add(charCounter);
		
		wrapper.add(panel);
		
		FocusDelegatingHandler handler = new FocusDelegatingHandler(rtArea);
		handler.addException(rtToolbar.getBackColors());
		handler.addException(rtToolbar.getForeColors());
		handler.addException(rtToolbar.getFonts());
		handler.addException(rtToolbar.getFontSizes());
		wrapper.addClickHandler(handler);
		wrapper.addFocusHandler(handler);
		
		rtArea.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				wrapper.addStyleName(focusStyleName);
			}
		});
		
		rtArea.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				wrapper.removeStyleName(focusStyleName);
			}
		});
		
		rtArea.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateCharCount();
			}
		});
		
		initWidget(wrapper);
		wrapper.setStyleName(styleName);
		panel.setWidth("100%");
		label.setStyleName("unibas-LabeledTextBox-Label");
	}
	
	private void updateCharCount() {
		int length = rtArea.getText().length();
		String lengthText;
		
		if (maxCharCount == 0) {
			charCounter.setVisible(false);
			return;
		}
		
		charCounter.setVisible(true);
		
		if(maxCharCount > 0 && length > maxCharCount) {
			lengthText =  "<span style='color:red'>" + length + "</span>";
		} else {
			lengthText =  String.valueOf(length);
		}
		
		if (maxCharCount > 0) {
			lengthText += "/" + String.valueOf(maxCharCount);
		}
		
		charCounter.setHTML(countPrefix + " " + lengthText);	
	}
	
	public void setMaxLength(int maxLength) {
		this.maxCharCount = maxLength;
		updateCharCount();
	}
	
	public void setLabelText(String label) {
		this.label.setText(label);
	}
	
	public Label getLabel() {
		return label;
	}

	@Override
	public int getTabIndex() {
		return rtArea.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		rtArea.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		rtArea.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		rtArea.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return rtArea.addFocusHandler(handler);
	}
	
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return rtArea.addBlurHandler(handler);
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
	
	@Override
	public String getHTML() {
		return rtArea.getHTML();
	}
	
	@Override
	public void setHTML(String html) {
		rtArea.setHTML(html);
	}
	
	@Override
	public void setHTML(SafeHtml html) {
		rtArea.setHTML(html);
	}
	
	@Override
	public String getText() {
		return rtArea.getText();
	}
	
	@Override
	public void setText(String text) {
		rtArea.setText(text);
	}
	
	private void addContextHelpHandlers() {
		if (!hasContextHelpHandlers) {
			popup = new ContextHelpPopup();
			rtArea.addFocusHandler(new FocusHandler() {
				
				@Override
				public void onFocus(FocusEvent event) {
					popup.setPopupPosition(wrapper.getAbsoluteLeft() + wrapper.getOffsetWidth() + 30 , wrapper.getAbsoluteTop());
					popup.show();
				}
			});
			rtArea.addBlurHandler(new BlurHandler() {
				
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

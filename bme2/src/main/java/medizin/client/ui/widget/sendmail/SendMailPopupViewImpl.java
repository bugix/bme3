package medizin.client.ui.widget.sendmail;

import java.util.Map;
import java.util.Map.Entry;

import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

public class SendMailPopupViewImpl extends PopupPanel implements SendMailPopupView {
	
	BmeConstants constants = GWT.create(BmeConstants.class);
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, SendMailPopupViewImpl> {
	}

	private Delegate delegate;
	
	@UiField
	HeadingElement please;
	
	@UiField
	SpanElement mailTemplate;
	
	@UiField(provided=true)
	FlexTable paramsInfo = new FlexTable();
		
	@UiField(provided = true)
	RichTextArea message;
	
	@UiField(provided = true)
	RichTextToolbar messageToolbar;
	
	@UiField
	public Button sendMailButton;
	
	@UiField
	public Button restoreTemplateButton;
	
	@UiField
	public IconButton closeButton;
	
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event) {
		this.hide();
	}
	
	public SendMailPopupViewImpl(Map<String, String> paramsMap) {		
		super(false,true);
		this.setGlassEnabled(true);
		message = new RichTextArea();
		message.setSize("100%", "14em");
		messageToolbar = new RichTextToolbar(message);
		messageToolbar.setWidth("100%");
		this.addStyleName("printPopupPanel");
		add(BINDER.createAndBindUi(this));
				
		mailTemplate.setInnerText(constants.mailTemplate());
		sendMailButton.setText(constants.sendMail());
		
		restoreTemplateButton.setText(constants.restoreTemplate());
		please.setInnerText(constants.sendMailPlease());
		
		setParamsMap(paramsMap);
	}


	private void setParamsMap(Map<String, String> paramsMap) {
		int currentRow = -1;
		int currentColumn = 0;
		for (Entry<String, String> param : paramsMap.entrySet())
		{
			if(currentColumn % 6 == 0) {
				currentRow += 1;
				currentColumn = 0;
			}
			HTML htmlKey = new HTML(param.getKey());
			htmlKey.setTitle(param.getKey());
			htmlKey.setStyleName("sendMailParams");
			HTML htmlValue = new HTML(param.getValue());
			htmlValue.setTitle(param.getValue());
			htmlValue.setStyleName("sendMailParams");
			paramsInfo.setWidget(currentRow, currentColumn, htmlKey);
			paramsInfo.setWidget(currentRow, currentColumn+1, htmlValue);
			currentColumn += 2;			
		}
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public HandlerRegistration addSendClickHandler(ClickHandler handler)
	{
		return sendMailButton.addClickHandler(handler);
	}
	
	public HandlerRegistration addRestoreClickHandler(ClickHandler handler)
	{
		return restoreTemplateButton.addClickHandler(handler);
	}

	@Override
	public String getMessageContent(){
		return message.getHTML();
	}
	
	@Override
	public void setMessageContent(String html){
		message.setHTML(html);
	}	
}

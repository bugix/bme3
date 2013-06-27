package medizin.client.ui.view.assignquestion;

import java.util.ArrayList;
import java.util.List;

import medizin.client.proxy.PersonProxy;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AssesmentQuestionPanelImpl extends Composite implements AssesmentQuestionPanel {

	private static AssesmentQuestionPanelImplUiBinder uiBinder = GWT
			.create(AssesmentQuestionPanelImplUiBinder.class);

	interface AssesmentQuestionPanelImplUiBinder extends
			UiBinder<Widget, AssesmentQuestionPanelImpl> {
	}
	
	private BmeConstants constants=GWT.create(BmeConstants.class);
	
	private Delegate delegate;
	
	private SendMailPopupViewImpl sendMailPopupViewImpl=null;
	
	
	
	public SendMailPopupViewImpl getSendMailPopupViewImpl() {
		return sendMailPopupViewImpl;
	}


	@UiField
	VerticalPanel assesmentQuestionDisplayPanel;
	
	@UiField
	Button sendMail;
	
	@UiField
	ScrollPanel assesmentQuestionScrollPanel;
	
	public Button getSendMail() {
		return sendMail;
	}

	@UiField(provided = true)
	ValueListBox<PersonProxy> authorListBox=new ValueListBox<PersonProxy>(new AbstractRenderer<PersonProxy>() {

		@Override
		public String render(PersonProxy object) {
			// TODO Auto-generated method stub
			if(object!=null)
				return object.getPrename() +" "+ object.getName();
			else
			{
				return "";
			}
		}
	});
	
	public ValueListBox<PersonProxy> getAuthorListBox() {
		return authorListBox;
	}
	
	public AssesmentQuestionPanelImpl() {
		initWidget(uiBinder.createAndBindUi(this));
//		assesmentQuestionDisplayPanel.setBorderWidth(1);
//		assesmentQuestionDisplayPanel.setHeight("100px");
//		assesmentQuestionDisplayPanel.setWidth("100px");
		assesmentQuestionDisplayPanel.setSpacing(5);
		sendMail.setText(constants.sendMail());
		authorListBox.addValueChangeHandler(new ValueChangeHandler<PersonProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PersonProxy> event) {
				delegate.authorValueChanged(event.getValue());
				
			}
		});
		 
		assesmentQuestionScrollPanel.setHeight(((int)(Window.getClientHeight()*0.73))+"px");
		
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	@Override
	public void addAssesmentQuestion(AssesmentQuestionView question) {
		assesmentQuestionDisplayPanel.add(question);		
	}
	


	@Override
	public void removeAll() {
		assesmentQuestionDisplayPanel.clear();		
	}

	@Override
	public VerticalPanel getAssesmentQuestionDisplayPanel() {
		
		return assesmentQuestionDisplayPanel;
	}
	
	@UiHandler("sendMail")
	public void showSendMailPopup(ClickEvent event)
	{
		if(sendMailPopupViewImpl==null)
		{
			sendMailPopupViewImpl=new SendMailPopupViewImpl();
			
			sendMailPopupViewImpl.setAnimationEnabled(true);
			sendMailPopupViewImpl.center();
			delegate.loadTemplate();
			
			sendMailPopupViewImpl.getSendMailButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					delegate.sendMail(sendMailPopupViewImpl.getMessageContent());
					
				}
			});
		}
		sendMailPopupViewImpl.show();
		
	}

}

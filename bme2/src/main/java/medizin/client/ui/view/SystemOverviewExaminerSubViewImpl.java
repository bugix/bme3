package medizin.client.ui.view;

import java.util.List;
import java.util.Map;

import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.sendmail.SendMailPopupViewImpl;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeMessages;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SystemOverviewExaminerSubViewImpl extends Composite implements SystemOverviewExaminerSubView{

	private static SystemOverviewExaminerSubViewImplUiBinder uiBinder = GWT
			.create(SystemOverviewExaminerSubViewImplUiBinder.class);

	interface SystemOverviewExaminerSubViewImplUiBinder extends
			UiBinder<Widget, SystemOverviewExaminerSubViewImpl> {
	}
	
	@UiField
	Label acceptQueAnswerLbl;
	
	@UiField
	Label mcMsgLabel;
	
	@UiField
	VerticalPanel examinerVerticalPanel;
	
	@UiField
	Label examinerNameLbl;
	
	@UiField
	IconButton sendMailBtn;
	
	@UiField
	DisclosurePanel examinerDisclosurePanel;
	
	PersonProxy personProxy = null;
	
	private Delegate delegate;
	
	List<AssesmentProxy> assesmentProxy = null;
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private BmeMessages messages = GWT.create(BmeMessages.class);
	
	private SendMailPopupViewImpl sendMailPopupViewImpl=null;

	public SystemOverviewExaminerSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		sendMailBtn.setText(constants.sendMail());
		examinerDisclosurePanel.addStyleName("systemOverviewExaminerDisclosurePanel");
	}
	
	@UiHandler("sendMailBtn")
	public void sendMailBtnClicked(ClickEvent event)
	{
		examinerDisclosurePanel.setOpen(!examinerDisclosurePanel.isOpen());
		
		if(sendMailPopupViewImpl==null)
		{
			sendMailPopupViewImpl=new SendMailPopupViewImpl(getSendMailParamsMap());
			
			sendMailPopupViewImpl.setAnimationEnabled(true);
						
			sendMailPopupViewImpl.addSendClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					sendMailPopupViewImpl.hide();
					if (personProxy != null && assesmentProxy != null)
						delegate.sendMailBtnClicked(personProxy, assesmentProxy, sendMailPopupViewImpl.getMessageContent());					
				}
			});
			
			sendMailPopupViewImpl.addRestoreClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					delegate.loadTemplate(SystemOverviewExaminerSubViewImpl.this);
				}
			});		
		}
		delegate.loadTemplate(this);
	}
	
	private Map<String, String> getSendMailParamsMap() {
		Map<String, String> paramMap = Maps.newHashMap();
		
		paramMap.put(constants.mailVarAssesment(), constants.mailAssesment());
		paramMap.put(constants.mailVarTo(), constants.mailToName());
		paramMap.put(constants.mailVarFrom(), constants.mailFromName());
		paramMap.put(constants.mailVarStartDate(), constants.mailStartDate());
		paramMap.put(constants.mailVarClosedDate(), constants.mailClosedDate());
		paramMap.put(constants.mailVarMC(), constants.mailMC());
		paramMap.put(constants.mailVarProposedCount(), constants.mailProposedCount());
		paramMap.put(constants.mailVarTotalCount(), constants.mailTotalCount());
		paramMap.put(constants.mailVarLoopStart(), constants.mailLoopStart());
		paramMap.put(constants.mailVarLoopEnd(), constants.mailLoopEnd());
		paramMap.put(constants.mailVarAllocatedCount(), constants.mailAllocatedCount());
		paramMap.put(constants.mailVarQuestionType(), constants.mailQuestionType());
		paramMap.put(constants.mailVarSpecialization(), constants.mailSpecialization());
		paramMap.put(constants.mailVarTotalRemaining(), constants.mailTotalRemaining());
		paramMap.put(constants.mailVarTotalRemainingCount(), constants.mailTotalRemainingCount());
		paramMap.put(constants.mailVarAssessmentLoop(), constants.mailAssessmentLoop());
		paramMap.put(constants.mailVarAssessmentEndLoop(), constants.mailAssessmentEndLoop());		
		return paramMap;
	}

	public void displayMail(String response)
	{
		if (sendMailPopupViewImpl != null)
		{
			sendMailPopupViewImpl.setMessageContent(response);
			if (!sendMailPopupViewImpl.isShowing())
				sendMailPopupViewImpl.center();
		}
		else
		{
			Log.error("PopupImpl is null");
		}		
	}

	public Label getAcceptQueAnswerLbl() {
		return acceptQueAnswerLbl;
	}

	public void setAcceptQueAnswerLbl(Label acceptQueAnswerLbl) {
		this.acceptQueAnswerLbl = acceptQueAnswerLbl;
	}

	public Label getMcMsgLabel() {
		return mcMsgLabel;
	}

	public void setMcMsgLabel(Label mcMsgLabel) {
		this.mcMsgLabel = mcMsgLabel;
	}

	public VerticalPanel getExaminerVerticalPanel() {
		return examinerVerticalPanel;
	}

	public void setExaminerVerticalPanel(VerticalPanel examinerVerticalPanel) {
		this.examinerVerticalPanel = examinerVerticalPanel;
	}
	
	public void setAcceptAnswerAndQuestion(String examinerName, Long acceptQuestionCount, Long acceptAnswerCount)
	{
		examinerNameLbl.setText(examinerName);
		acceptQueAnswerLbl.setText(messages.acceptQuestionAndAnswerExaminer(acceptQuestionCount, acceptAnswerCount));		
	}

	public PersonProxy getPersonProxy() {
		return personProxy;
	}

	public void setPersonProxy(PersonProxy personProxy) {
		this.personProxy = personProxy;
	}

	public List<AssesmentProxy> getAssesmentProxy() {
		return assesmentProxy;
	}

	public void setAssesmentProxy(List<AssesmentProxy> assesmentProxy) {
		this.assesmentProxy = assesmentProxy;
	}

	public Delegate getDelegate() {
		return delegate;
	}

	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public DisclosurePanel getExaminerDisclosurePanel() {
		return examinerDisclosurePanel;
	}

	public void setExaminerDisclosurePanel(DisclosurePanel examinerDisclosurePanel) {
		this.examinerDisclosurePanel = examinerDisclosurePanel;
	}
	
	public IconButton getSendMailBtn() {
		return sendMailBtn;
	}
}

/**
 * 
 */
package medizin.client.ui.view.assignquestion;

import java.util.HashSet;
import java.util.Set;

import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class SendMailPopupViewImpl extends PopupPanel implements SendMailPopupView {

//	private static SummoningsPopupViewUiBinder uiBinder = GWT
//			.create(SummoningsPopupViewUiBinder.class);
	
	BmeConstants constants = GWT.create(BmeConstants.class);
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, SendMailPopupViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	
	@UiField
	HeadingElement please;
	
	@UiField
	SpanElement mailTemplate;
	
	@UiField
	SpanElement summoningsToName;
	
	@UiField
	SpanElement summoningsFromName;
	
	@UiField
	SpanElement summoningsAssignment;
	
	@UiField
	SpanElement varFrom;
	
	@UiField
	SpanElement varTo;
	
	@UiField
	SpanElement varAssignment;
	
	@UiField
	HeadingElement onlyIfExaminer;
	
	@UiField
	SpanElement assignmentFormatHead;
	
	@UiField
	SpanElement mailVarStartDate;
	
	@UiField
	SpanElement mailStartDate;
	
	@UiField
	SpanElement mailVarClosedDate;
	
	@UiField
	SpanElement mailClosedDate;
	
	@UiField
	SpanElement mailVarMC;
	
	@UiField
	SpanElement mailMC;
	
	@UiField
	SpanElement mailVarProposedCount;
	
	@UiField
	SpanElement mailProposedCount;
	
	@UiField
	SpanElement mailVarTotalCount;
	
	@UiField
	SpanElement mailTotalCount;
	@UiField
	SpanElement mailVarLoopStart;
	@UiField
	SpanElement mailLoopStart;
	@UiField
	SpanElement mailVarLoopEnd;
	
	@UiField
	SpanElement mailLoopEnd;
	@UiField
	SpanElement mailVarAllocatedCount;
	
	@UiField
	SpanElement mailAllocatedCount;
	
	@UiField
	SpanElement mailQuestionType;
	
	@UiField
	SpanElement mailVarQuestionType;
	
	@UiField
	SpanElement mailVarSpecialization;
	
	@UiField
	SpanElement mailSpecialization;
	
	
	@UiField
	SpanElement mailVarTotalRemaining;
	
	@UiField
	SpanElement mailTotalRemaining;
	
	
	@UiField
	SpanElement mailVarTotalRemainingCount;
	
	@UiField
	SpanElement mailTotalRemainingCount;
	
	
	
	@UiField(provided = true)
	RichTextArea message;
	
	@UiField(provided = true)
	RichTextToolbar messageToolbar;
	
	@UiField
	public Button sendMailButton;
	
	/*@UiField
	public Button saveTemplateButton;*/

	@UiField
	public Button restoreTemplateButton;
	
	@UiField
	public IconButton closeButton;
	
	/*@UiField
	public ListBox semesterList;
	
	@UiField
	public Button loadTemplateButton;*/
	
	@UiHandler("sendMailButton")
	public void sendMailButtonClicked(ClickEvent event) {
		// TODO export action
		
	}
	/*
	@UiHandler("saveTemplateButton")
	public void saveTemplateButtonClicked(ClickEvent event) {
		// TODO export action
		
	}*/
	
	@UiHandler("restoreTemplateButton")
	public void restoreTemplateButtonClicked(ClickEvent event) {
		// TODO export action
		
	}
	
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event) {
		// TODO export action
		this.hide();
	}
	
	public SendMailPopupViewImpl() {
		
		super(false,true);
		this.setGlassEnabled(true);
		message = new RichTextArea();
		message.setSize("100%", "14em");
		messageToolbar = new RichTextToolbar(message);
		messageToolbar.setWidth("100%");
		this.addStyleName("printPopupPanel");
		init();
		add(BINDER.createAndBindUi(this));
		
		
		mailTemplate.setInnerText(constants.mailTemplate());
		sendMailButton.setText(constants.sendMail());
		
		restoreTemplateButton.setText(constants.restoreTemplate());
		please.setInnerText(constants.sendMailPlease());
		
		varAssignment.setInnerText(constants.mailVarAssesment());
		varTo.setInnerText(constants.mailVarTo());
		varFrom.setInnerText(constants.mailVarFrom());
		
		summoningsAssignment.setInnerText(constants.mailAssesment());
		summoningsToName.setInnerText(constants.mailToName());
		summoningsFromName.setInnerText(constants.mailFromName());
		//onlyIfExaminer.setInnerText(constants.summoningsOnlyIfExaminer());
		assignmentFormatHead.setInnerText(constants.mailAssesmentFormatHead());
		//assignmentFormat.setInnerText(constants.summoningsAssignmentFormat());
		
		
		mailVarStartDate.setInnerText(constants.mailVarStartDate());
		
		mailStartDate.setInnerText(constants.mailStartDate());
		
		mailVarClosedDate.setInnerText(constants.mailClosedDate());
		
		 mailClosedDate.setInnerText(constants.mailClosedDate());
		
		mailVarMC.setInnerText(constants.mailVarMC());
		
		 mailMC.setInnerText(constants.mailMC());
		 mailVarProposedCount.setInnerText(constants.mailVarProposedCount());
		
		 mailProposedCount.setInnerText(constants.mailProposedCount());
		
		mailVarTotalCount.setInnerText(constants.mailVarTotalCount());
		
		 mailTotalCount.setInnerText(constants.mailTotalCount());
		 mailVarLoopStart.setInnerText(constants.mailVarLoopStart());
		 mailLoopStart.setInnerText(constants.mailLoopStart());
		 mailVarLoopEnd.setInnerText(constants.mailVarLoopEnd());
		
		 mailLoopEnd.setInnerText(constants.mailLoopEnd());
		 mailVarAllocatedCount.setInnerText(constants.mailVarAllocatedCount());
		
		mailAllocatedCount.setInnerText(constants.mailAllocatedCount());
		
	mailQuestionType.setInnerText(constants.mailQuestionType());
		
		mailVarQuestionType.setInnerText(constants.mailVarQuestionType());
		
		 mailVarSpecialization.setInnerText(constants.mailVarSpecialization());
		
		 mailSpecialization.setInnerText(constants.mailSpecialization());
		
		
		 mailVarTotalRemaining.setInnerText(constants.mailVarTotalRemaining());
		
	 mailTotalRemaining.setInnerText(constants.mailTotalRemaining());
		
		 mailVarTotalRemainingCount.setInnerText(constants.mailTotalRemainingCount());
		
	mailTotalRemainingCount.setInnerText(constants.mailTotalRemainingCount());
		
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// TODO implement this!
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Button getSendMailButton() {
		return sendMailButton;
	}
	
/*	@Override
	public Button getSaveTemplateButton() {
		return saveTemplateButton;
	}
*/
	@Override
	public Button getRestoreTemplateButton() {
		return restoreTemplateButton;
	}
	
	/*@Override
	public ListBox getSemesterList() {
		return semesterList;
	}

	@Override
	public Button getLoadTemplateButton() {
		return loadTemplateButton;
	}*/

	@Override
	public String getMessageContent(){
		return message.getHTML();
	}
	
	@Override
	public void setMessageContent(String html){
		message.setHTML(html);
	}
	
}

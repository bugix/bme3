package medizin.client.ui.view.question;

import static com.google.common.collect.Lists.newArrayList;
import static medizin.client.util.ClientUtility.defaultString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.view.question.keyword.QuestionKeywordView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectiveSubViewImpl;
import medizin.client.ui.view.question.usedinmc.QuestionUsedInMC;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.TabPanelHelper;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.client.ui.widget.process.AppLoader;
import medizin.client.ui.widget.process.ApplicationLoadingView;
import medizin.client.ui.widget.resource.dndview.ResourceView;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.util.ClientUtility;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeMessages;
import medizin.shared.utils.FileDownloaderProps;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionDetailsViewImpl extends Composite implements QuestionDetailsView {

	private static QuestionDetailsViewImplUiBinder uiBinder = GWT.create(QuestionDetailsViewImplUiBinder.class);

	interface QuestionDetailsViewImplUiBinder extends UiBinder<Widget, QuestionDetailsViewImpl> {}

	public BmeMessages bmeMessages = GWT.create(BmeMessages.class);
	public BmeConstants constants = GWT.create(BmeConstants.class);
	private Delegate delegate;
	private QuestionProxy proxy;
	private EventBus eventBus;
	
	@UiField
	TabPanel questionTypeDetailPanel;
	
	@UiField
	IconButton edit;

	@UiField
	IconButton delete;

	@UiField
	IconButton accept;
	
	@UiField
	IconButton resendToReview;
	
	@UiField
	SpanElement displayRenderer;
	
	@UiField
	SpanElement displayVersionRenderer;
	
	@UiField
	Label lblQuestionTypeValue;
	
	@UiField
	Label lblQuestionShortNameValue;

	@UiField
	HTML lblQuestionTextValue;
	
	@UiField
	Label lblAutherValue;

	@UiField
	Label lblReviewerValue;

	@UiField
	Label lblQuestionEventValue;

	@UiField
	Label lblCommentValue;

	@UiField
	Label lblMcsValue;

	@UiField
	IconButton previous;
	
	@UiField
	IconButton latest;

	@UiField
	IconButton forcedActive;
	
	@UiField
	HorizontalPanel resourceUploadPanel;
	
	@UiField
	HorizontalPanel resourceViewPanel;
	
	@UiField(provided=true)
	AnswerListViewImpl answerListViewImpl;

	@UiField
	VerticalPanel answerVerticalPanel;
	
	@UiField(provided=true)
	QuestionLearningObjectiveSubViewImpl questionLearningObjectiveSubViewImpl;

	@UiField
	CheckBox acceptQueAnswer;
	
	@UiField
	QuestionUsedInMC questionUsedInMC;
	
	@UiField
	QuestionKeywordView questionKeyword;
	
	@UiField
	IconButton pushToReviewProcess;
	
	@UiField(provided=true)
	MatrixAnswerListViewImpl matrixAnswerListViewImpl;
	
	@UiField
	IconButton printPdf;
	
	@UiField
	ApplicationLoadingView loadingPopup;
	
	private static final Comparator<McProxy> MC_COMPARATOR = new Comparator<McProxy>() {

		@Override
		public int compare(McProxy o1, McProxy o2) {	
			return o1.getId().compareTo(o2.getId());
		}
	};
	
	@Override
	public AnswerListViewImpl getAnswerListViewImpl() {
		return answerListViewImpl;
	}

	@UiHandler("printPdf")
	public void printPdfButtonClicked(ClickEvent e)
	{
		Widget eventSource = (Widget) e.getSource();
		final QuestionPrintFilterViewImpl filterView = new QuestionPrintFilterViewImpl();
		filterView.getPrintButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if (proxy != null)
				{
					String ordinal = URL.encodeQueryString(String.valueOf(FileDownloaderProps.Method.PRINT_QUESTION_PDF.ordinal()));          
					String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(FileDownloaderProps.METHOD_KEY).concat("=").concat(ordinal)
							.concat("&").concat(FileDownloaderProps.ID).concat("=").concat(URL.encodeQueryString(proxy.getId().toString()))
							.concat("&").concat(FileDownloaderProps.QUESTION_DETAIL).concat("=").concat(URL.encodeQueryString(filterView.getDetailsCheckBoxValue()))
							.concat("&").concat(FileDownloaderProps.KEYWORD).concat("=").concat(URL.encodeQueryString(filterView.getKeywordCheckBoxValue()))
							.concat("&").concat(FileDownloaderProps.LEARNING_OBJECTIVE).concat("=").concat(URL.encodeQueryString(filterView.getLearningObjectiveCheckBoxValue()))
							.concat("&").concat(FileDownloaderProps.USED_IN_MC).concat("=").concat(URL.encodeQueryString(filterView.getUsedInMcCheckBoxValue()))
							.concat("&").concat(FileDownloaderProps.ANSWER).concat("=").concat(URL.encodeQueryString(filterView.getAnswerCheckBoxValue()))
							.concat("&").concat(FileDownloaderProps.LOCALE).concat("=").concat(URL.encodeQueryString(LocaleInfo.getCurrentLocale().getLocaleName()));
					Log.info("--> url is : " +url);
					
					Window.open(url, "", "");
					
					filterView.hide();
				}
			}
		});		
		filterView.setPopupPosition(eventSource.getAbsoluteLeft()-200, eventSource.getAbsoluteTop()-5);
		filterView.show();	
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.deleteQuestionConfirmation(), new ConfirmDialogBoxYesNoButtonEventHandler() {
			
			@Override
			public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
				delegate.deleteClicked();		
			}
			
			@Override
			public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {}
		});
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
	}


	@UiHandler("previous")
	public void onPreviousClicked(ClickEvent e) {
		//remove edit and delete btn
		setVisibleEditAndDeleteBtn(false);
		resendToReview.setVisible(false);
		accept.setVisible(false);
		if(proxy != null && proxy.getPreviousVersion() != null) {
			delegate.getQuestionDetails(proxy.getPreviousVersion() ,new Function<QuestionProxy, Void>() {

				@Override
				public Void apply(QuestionProxy input) {
					setValue(input);
					latest.setEnabled(true);
					resendToReview.setVisible(false);
					accept.setVisible(false);
					forcedActive.setVisible(false);
					if(input.getPreviousVersion() == null) {
						previous.setEnabled(false);	
					}
					return null;
				}
			});	
		}else {
			previous.setEnabled(false);
			latest.setEnabled(true);
		}
	}

	@UiHandler("latest")
	public void onlatestClicked(ClickEvent e) {
		QuestionProxy proxy = delegate.getLatestQuestionDetails();	
		setValue(proxy);
		//remove edit and delete btn
		delegate.enableBtnOnLatestClicked();
		
		latest.setEnabled(false);
		if(proxy.getPreviousVersion()== null) {
			previous.setEnabled(false);	
		}else {
			previous.setEnabled(true);
		}
	}

	@UiHandler("forcedActive")
	public void onForcedActiveClicked(ClickEvent e) {
		delegate.forcedActiveClicked();
	}

	@UiHandler("pushToReviewProcess")
	public void onPushToReviewProcessClicked(ClickEvent e) {
		if(validationOfFields(false)) {
			delegate.pushToReviewProcessClicked();	
		}
	}
	
	@Override
	public void setValue(QuestionProxy proxy) {
		this.proxy = proxy;
		final String qVersion = defaultString(proxy.getQuestionVersion(), "0");
		final String qSubVersion = defaultString(proxy.getQuestionSubVersion(),"0");
		final String version = "(" + qVersion + "." + qSubVersion + ")";
		
		if (Status.ACTIVE.equals(proxy.getStatus())){
			removePrintPdfBtn(true);
		}
		else{
			removePrintPdfBtn(false);
		}
		
		final String title = proxy.getQuestionShortName()==null?proxy.getId().toString():proxy.getQuestionShortName();
		displayRenderer.setInnerText(title);
		displayRenderer.setTitle(title);
		
		displayVersionRenderer.setInnerText(version);
		
		lblQuestionTypeValue.setText(proxy.getQuestionType()==null?"":proxy.getQuestionType().getShortName());
		lblQuestionShortNameValue.setText(defaultString(proxy.getQuestionShortName()));
		lblQuestionTextValue.setHTML(defaultString(proxy.getQuestionText()));
		lblAutherValue.setText(getPersonName(proxy.getAutor()));
		lblReviewerValue.setText(getPersonName(proxy.getRewiewer()));
		lblQuestionEventValue.setText(proxy.getQuestEvent()==null?"":proxy.getQuestEvent().getEventName());
		lblCommentValue.setText(proxy.getComment()==null?"":proxy.getComment());
		
		ArrayList<McProxy> mcList = newArrayList(proxy.getMcs());
		Collections.sort(mcList, MC_COMPARATOR);
		lblMcsValue.setText(proxy.getMcs() == null ? "": medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.McProxyRenderer.instance()).render(mcList));
		if(proxy.getSubmitToReviewComitee()==true)
		{
			lblReviewerValue.setText(constants.submitToReviewComitee());
		}
		addSecondTabForQuestionResource(proxy);
		
		if(proxy.getPreviousVersion() == null) {
			previous.setEnabled(false);
		}
		
		if(proxy.getQuestionType()!= null && proxy.getQuestionType().getQuestionType().equals(QuestionTypes.Drawing)) {
			answerVerticalPanel.removeFromParent();
			acceptQueAnswer.removeFromParent();
		}
	}

	private final static String getPersonName(PersonProxy person) {
		return person == null ? "" : person.getName();
	}

	private void addSecondTabForQuestionResource(final QuestionProxy proxy) {
		
		resourceUploadPanel.clear();
		resourceViewPanel.clear();
		boolean isAdded = false;
		List<QuestionResourceClient> questionResourceClients = Lists.newArrayList();
		boolean haveImage = false;
		boolean haveSound = false;
		boolean haveVideo = false;
		
		if(proxy != null && proxy.getQuestionType() != null  && proxy.getQuestionType().getQuestionType() != null) {
			
			final QuestionTypes questionType = proxy.getQuestionType().getQuestionType();
			switch (questionType) {
			
			case Textual:
			case Sort:
			case LongText:
			case Drawing:
			{
				if(proxy != null && proxy.getQuestionType()!= null && proxy.getQuestionType().getQueHaveImage() != null &&  proxy.getQuestionType().getQueHaveSound() != null && proxy.getQuestionType().getQueHaveVideo() != null) {
					//setResourceUploadAndResourceViewer(proxy.getQuestionType(),proxy);
					questionResourceClients = ClientUtility.getQuestionResourceClient(proxy.getQuestionResources());
					haveImage = proxy.getQuestionType().getQueHaveImage();
					haveSound = proxy.getQuestionType().getQueHaveSound();
					haveVideo = proxy.getQuestionType().getQueHaveVideo();
					isAdded = true;
				}
				
				break;
			}	
				
			case Imgkey:
			case ShowInImage:
			{
				if(proxy != null && proxy.getQuestionType()!= null && proxy.getQuestionType().getQuestionType() != null /*&& proxy.getPicturePath() != null*/) {
					if(proxy.getQuestionResources() != null && proxy.getQuestionResources().isEmpty() == false) {
						questionResourceClients = ClientUtility.getQuestionResourceClient(proxy.getQuestionResources());
						haveImage = true;
						isAdded = true;	
					}
				}
				
				break;
			}
			default:
			{
				Log.info("in default case");
				Log.info("~~~TAB TEXT : " + questionTypeDetailPanel.getTabBar().getTabHTML(0));
				
				for (int i=0; i<questionTypeDetailPanel.getTabBar().getTabCount(); i++)
				{
					if (questionTypeDetailPanel.getTabBar().getTabHTML(i).equals(constants.media()))
					{
						questionTypeDetailPanel.remove(i);
						break;
					}
				}
				
				/*if(questionTypeDetailPanel.getTabBar().getTabCount() > 2) 
					questionTypeDetailPanel.remove(2);*/
				isAdded = false;
				break;	
			}
			}
			
			if(isAdded == true && (haveImage != false || haveSound != false || haveVideo != false) ) {
				final ResourceView resourceView = new ResourceView(eventBus,questionResourceClients, proxy.getQuestionType().getQuestionType(),haveImage,haveSound,haveVideo,false);
				resourceViewPanel.add(resourceView);
			}
		}
	}

	public QuestionDetailsViewImpl(EventBus eventBus, Boolean editDeleteflag, boolean isAnswerEditable, boolean addAnswerRights, boolean isforceView, boolean isAcceptView, boolean isDeleteLearningObjective, boolean removePushToReviewProcess, boolean printPdfBtnFlag, QuestionTypes questionTypes) {
		answerListViewImpl = new AnswerListViewImpl(addAnswerRights, isAnswerEditable,questionTypes);
		matrixAnswerListViewImpl = new MatrixAnswerListViewImpl(addAnswerRights, isAnswerEditable);
		questionLearningObjectiveSubViewImpl = new QuestionLearningObjectiveSubViewImpl(isDeleteLearningObjective);
		initWidget(uiBinder.createAndBindUi(this));
		
		removeEditAndDeleteBtn(editDeleteflag);
		removeForceView(isforceView);
		removeAcceptView(isAcceptView);
		removePushToReviewProcess(removePushToReviewProcess);
		removePrintPdfBtn(printPdfBtnFlag);
		this.eventBus = eventBus;
		
		questionTypeDetailPanel.selectTab(0);
		TabPanelHelper.moveTabBarToBottom(questionTypeDetailPanel);
			
		questionKeyword.initKeyword(isAnswerEditable);
		
		questionTypeDetailPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				int tabId = event.getSelectedItem();
				String tagName = questionTypeDetailPanel.getTabBar().getTabHTML(tabId);
				if (tagName.equals(constants.manageQuestion())) {
					// not to do
				} else if (tagName.equals(constants.media())) {
					// not to do
				} else if (tagName.equals(constants.keywords())) {
					AppLoader.setCurrentLoader(questionKeyword.getLoadingPopup());
					questionKeyword.initKeywordView();
				} else if (tagName.equals(constants.learning())) {
					AppLoader.setCurrentLoader(questionLearningObjectiveSubViewImpl.getLoadingPopup()); 
					questionLearningObjectiveSubViewImpl.initLearningObjectiveView();
				} else if (tagName.equals(constants.usedInMC())) {
					AppLoader.setCurrentLoader(questionUsedInMC.getLoadingPopup());
					questionUsedInMC.initUsedInMCView();
				}
			}
		});
	}
	
	private void removePrintPdfBtn(boolean printPdfBtnFlag) {
		if (printPdfBtnFlag == false)
		{
			printPdf.removeFromParent();
		}
	}

	private void removePushToReviewProcess(boolean removePushToReviewProcess) {
		if(removePushToReviewProcess) {
			pushToReviewProcess.removeFromParent();	
		}
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}
	
	@Override
	public void removeEditAndDeleteBtn(Boolean flag)
	{
		if(flag == false) {
			edit.removeFromParent();
			delete.removeFromParent();
		}
	}
	
	@Override
	public void setVisibleEditAndDeleteBtn(Boolean isEdit) {
		edit.setVisible(isEdit);
		delete.setVisible(isEdit);
	}
	
	public void setVisibleAcceptButton()
	{
		delete.removeFromParent();
		edit.setVisible(true);
		accept.setVisible(true);
	}
	
	@UiHandler("accept")
	public void onAcceptClicked(ClickEvent e) {
		if (proxy != null) {
			if(acceptQueAnswer.getValue() == true) {
				delegate.acceptQueAnswersClicked();
			} else {
				delegate.acceptQuestionClicked(proxy);	
			}
		}
	}
	
	@UiHandler("resendToReview")
	public void onResendToReviewClicked(ClickEvent e) {
		if (proxy != null)
			delegate.onResendToReviewClicked(proxy);
	}
	
	public MatrixAnswerListViewImpl getMatrixAnswerListViewImpl() {
		return matrixAnswerListViewImpl;
	}

	public void setMatrixAnswerListViewImpl(MatrixAnswerListViewImpl matrixAnswerListViewImpl) {
		this.matrixAnswerListViewImpl = matrixAnswerListViewImpl;
	}

	public IconButton getEdit() {
		return edit;
	}
	
	public VerticalPanel getAnswerVerticalPanel() {
		return answerVerticalPanel;
	}

	public void setAnswerVerticalPanel(VerticalPanel answerVerticalPanel) {
		this.answerVerticalPanel = answerVerticalPanel;
	}

	@Override
	public IconButton getResendToReviewBtn() {
		return resendToReview;
	}

	@Override
	public IconButton getAcceptBtn() {
		return accept;
	}
	
	@Override
	public CheckBox getAcceptQueAnswer() {
		return acceptQueAnswer;
	}
	

	public void setVisibleForcedActiveBtn(boolean visible) {
		
		if(visible == false) {
			forcedActive.setVisible(false);
		}else {
			forcedActive.setVisible(true);
		}	
		
	}
	
	private void removeAcceptView(boolean isAcceptView) {
		if(isAcceptView == false) {
			accept.removeFromParent();
			resendToReview.removeFromParent();
			acceptQueAnswer.removeFromParent();
		}else {
			accept.setVisible(true);
			resendToReview.setVisible(true);
			acceptQueAnswer.setVisible(true);
		}
	}

	private void removeForceView(boolean isforceView) {
		if(isforceView == false) {
			forcedActive.removeFromParent();
			forcedActive.removeFromParent();
		}else {
			forcedActive.setVisible(true);
		}
	}

	public QuestionLearningObjectiveSubViewImpl getQuestionLearningObjectiveSubViewImpl() {
		return questionLearningObjectiveSubViewImpl;
	}

	public void setQuestionLearningObjectiveSubViewImpl(
			QuestionLearningObjectiveSubViewImpl questionLearningObjectiveSubViewImpl) {
		this.questionLearningObjectiveSubViewImpl = questionLearningObjectiveSubViewImpl;
	}

	@Override
	public QuestionUsedInMC getQuestionUsedInMC() {
		return questionUsedInMC;
	}
	
	@Override
	public void removeQuestionUsedInMCTab() {
		Log.info("in default case");
		Log.info("~~~TAB TEXT : " + questionTypeDetailPanel.getTabBar().getTabHTML(0));
		
		for (int i=0; i<questionTypeDetailPanel.getTabBar().getTabCount(); i++)
		{
			if (questionTypeDetailPanel.getTabBar().getTabHTML(i).equals(constants.usedInMC()))
			{
				questionTypeDetailPanel.remove(i);
				break;
			}
		}
	}

	
	@Override
	public QuestionKeywordView getQuestionKeywordView() {
		return questionKeyword;
	}
	
	private boolean validationOfFields(Boolean isCreativeWork) {
		
		ArrayList<String> messages = Lists.newArrayList();
		boolean flag = true;
		flag = authorReviewerValidation(messages, flag);
		flag = questionTypeValidation(messages, flag);
		flag = questionTextValidation(messages, flag);
		flag = imageKeyAndShowInImageValidation(messages, flag);
		flag = mcValidation(messages, flag);
		if(flag == false) {
			ReceiverDialog.showMessageDialog(constants.pleaseEnterWarning(),messages);
		}
		
		return flag;
	}

	private boolean mcValidation(ArrayList<String> messages, boolean flag) {
		Set<McProxy> mcs = proxy.getMcs();
		if(mcs == null || mcs.isEmpty()) {
			flag = false;
			messages.add(constants.mcsMayNotBeNull());
		}
		return flag;
	}

	private boolean imageKeyAndShowInImageValidation(ArrayList<String> messages, boolean flag) {
		QuestionTypeProxy questionType = proxy.getQuestionType();
		Set<QuestionResourceProxy> questionResources = proxy.getQuestionResources();
		if(questionType != null) {
			switch (questionType.getQuestionType()) {
			
			case Imgkey:
			case ShowInImage:
			{
				if(questionResources == null || questionResources.isEmpty()) {
					flag = false;
					messages.add(constants.imageMayNotBeNull());
				} else {
					QuestionResourceProxy questionResourceProxy = Lists.newArrayList(questionResources).get(0);
					String path = questionResourceProxy.getPath();
					if(path == null || path.trim().isEmpty()) {
						flag = false;
						messages.add(constants.imageMayNotBeNull());
					}
				}
				break;
			}
			}
		}
		return flag;
	}

	private boolean questionTextValidation(ArrayList<String> messages, boolean flag) {
		String questionText = proxy.getQuestionText();
		if(questionText == null || questionText.isEmpty()) {
			flag = false;
			messages.add(constants.questionTextMayNotBeNull());
		}else {
			// question text is not null
			QuestionTypeProxy questionType = proxy.getQuestionType();
			if(questionType != null && questionType.getQuestionLength()  != null && questionType.getQuestionLength() < questionText.length()) {
				flag = false;
				messages.add(constants.questionTextMaxLength());
			}
		}
		return flag;
	}

	private boolean questionTypeValidation(ArrayList<String> messages, boolean flag) {
		QuestionTypeProxy questionType = proxy.getQuestionType();
		if(questionType == null) {
			flag = false;
			messages.add(constants.questionTypeMayNotBeNull());
		}
		return flag;
	}

	private boolean authorReviewerValidation(ArrayList<String> messages, boolean flag) {
		PersonProxy rewiewer = proxy.getRewiewer();
		PersonProxy author = proxy.getAutor();
		
		if(proxy.getSubmitToReviewComitee() == false && rewiewer == null) {
			flag = false;
			messages.add(constants.selectReviewerOrComitee());
		}
		
		if(author == null) {
			flag = false;
			messages.add(constants.authorMayNotBeNull());
		}
		
		if(author != null && rewiewer != null && author.getId().equals(rewiewer.getId()) == true) {
			flag = false;
			messages.add(constants.authorReviewerMayNotBeSame());
		}
		return flag;
	}
	
	@Override
	public ApplicationLoadingView getLoadingPopup() {
			return loadingPopup;
		}

}

package medizin.client.ui.view.question;

import static medizin.client.util.ClientUtility.defaultString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.view.question.ConfirmQuestionChangesPopup.ConfirmQuestionHandler;
import medizin.client.ui.view.roo.QuestionTypeProxyRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.client.ui.widget.mcs.McCheckboxEditor;
import medizin.client.ui.widget.process.ApplicationLoadingView;
import medizin.client.ui.widget.resource.dndview.ResourceView;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.event.ResourceAddedEvent;
import medizin.client.ui.widget.resource.event.ResourceAddedEventHandler;
import medizin.client.ui.widget.resource.image.ImageViewer;
import medizin.client.ui.widget.resource.upload.ResourceUpload;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEvent;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEventHandler;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.client.util.ClientUtility;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeMessages;
import medizin.shared.utils.SharedConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;


public class QuestionEditViewImpl extends Composite implements QuestionEditView /* , Editor<QuestionProxy>*/{

	private static QuestionEditViewImplUiBinder uiBinder = GWT.create(QuestionEditViewImplUiBinder.class);

	interface QuestionEditViewImplUiBinder extends UiBinder<Widget, QuestionEditViewImpl> {}

	private Delegate delegate;
	
	public static BmeConstants constants = GWT.create(BmeConstants.class);
	public static BmeMessages messages = GWT.create(BmeMessages.class);

	@UiField
	public IconButton cancel;

	@UiField
	public IconButton save;
	
	@UiField
	public IconButton resendToReview;
	
	@UiField
	TabPanel questionTypePanel;

	@UiField
	SpanElement title;

	@UiField
	public Label lblQuestionType;

	@UiField(provided = true)
	public ValueListBox<QuestionTypeProxy> questionType = new ValueListBox<QuestionTypeProxy>(QuestionTypeProxyRenderer.instance(),new EntityProxyKeyProvider<medizin.client.proxy.QuestionTypeProxy>());

	@UiField
	public Label lblQuestionText;

	@UiField(provided = true)
	public RichTextToolbar toolbar;

	@UiField
	public Label lblQuestionShortName;

	@UiField
	public TextBox questionShortName;

	@UiField
	public Label lblQuestionSubmitToReviewComitee;

	@UiField
	public CheckBox submitToReviewComitee;
	
	@UiField
	public DivElement descriptionValue;

	@UiField(provided = true)
	public RichTextArea questionTextArea;

	@UiField
	public Label lblAuther;

	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> author;

	@UiField
	public Label lblReviewer;

	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> rewiewer;
	
	@UiField
	public Label lblQuestionEvent;

	@UiField(provided = true)
	public ValueListBox<QuestionEventProxy> questEvent = new ValueListBox<QuestionEventProxy>(medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance(),new EntityProxyKeyProvider<medizin.client.proxy.QuestionEventProxy>());

	@UiField
	public Label lblQuestionComment;
	
	@UiField
	public TextArea questionComment;
	
	@UiField
	public Label lblMCS;

	@UiField 
	McCheckboxEditor mcs;
	
	@UiField
	public VerticalPanel containPanel;
	
	@UiField
	Label lblUploadText;
	
	@UiField
	HTMLPanel uploaderContainer;
	
	@UiField
	HTMLPanel viewerContainer;
	
	@UiField
	Label lblAutherValue;
	
	@UiField
	Label lblReviewerValue;
	
	@UiField
	Label lblSubmitToReviewComitee;
	
	@UiField
	HTML lblDigitCount;
	
	@UiField
	CheckBox pushToReviewProcess;
	
	@UiField
	CheckBox forcedActive;
	
	@UiField
	ApplicationLoadingView loadingPopup;
	
	private ResourceView viewer;
	private ImageViewer imageViewer;
	private QuestionProxy question = null;
	private final PersonProxy userLoggedIn;
	private final EventBus eventBus;
	private boolean isAuthorReviewerEditable = true;
	
	@UiHandler("cancel")
	void onCancel(ClickEvent event) {
		delegate.cancelClicked();
	}
	
	@UiHandler("resendToReview")
	void onResendToReview(ClickEvent event) {
		if(validationOfFields(false)) {
			delegate.resendToReview();
		}
	}

	@UiHandler("forcedActive")
	void onForcedActiveClicked(ClickEvent event) {
		if(forcedActive.getValue()== true) {
			pushToReviewProcess.setValue(false);
		}
	}
	
	@UiHandler("pushToReviewProcess")
	void onPushToReviewProcessClicked(ClickEvent event) {
		if(pushToReviewProcess.getValue()== true) {
			forcedActive.setValue(false);
		}
	}
	
	@UiHandler("save")
	void onSave(ClickEvent event) {
		boolean isCreativeWork = false;
		if(forcedActive.getValue() == true) {
			isCreativeWork = false;
		} else if(pushToReviewProcess.getValue()== true) {
			isCreativeWork = false;
		} else {
			isCreativeWork = true;
		}
		
		if(validationOfFields(isCreativeWork)) {
			// new question
			delegate.saveQuestionWithDetails(isCreativeWork,forcedActive.getValue());
		} else {
			Log.info("Validation fail");
		}
	}
	
	public QuestionEditViewImpl(Map<String, Widget> reciverMap, EventBus eventBus, PersonProxy userLoggedIn,boolean isResendToReview, boolean isAdminOrInstitutionalAdmin) {

		this.eventBus = eventBus;
		this.userLoggedIn = userLoggedIn;
		questionTextArea = new RichTextArea();
		questionTextArea.setSize("100%", "14em");
		toolbar = new RichTextToolbar(questionTextArea);
		toolbar.setWidth("100%");
		
		initWidget(uiBinder.createAndBindUi(this));
		
		reciverMap.put("questionShortName", questionShortName);
		reciverMap.put("questionText", questionTextArea);
		reciverMap.put("questionType", questionType);
		reciverMap.put("autor", author.getTextField().advancedTextBox);
		reciverMap.put("rewiewer", rewiewer);
		reciverMap.put("questEvent", questEvent);
		reciverMap.put("submitToReviewComitee", submitToReviewComitee);
		reciverMap.put("mcs", mcs);
		//reciverMap.put("comment", questionComment);
		
		questionTypePanel.selectTab(0);
		save.setText(constants.save());
		cancel.setText(constants.cancel());
		submitToReviewComitee.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(submitToReviewComitee.getValue() == true)
				{
					rewiewer.setEnabled(false);
				}
				else
				{
					rewiewer.setEnabled(true);
				}
			}
		});
		questionTypePanel.getTabBar().setTabText(0, constants.manageQuestion());
		
		questionType.addValueChangeHandler(new ValueChangeHandler<QuestionTypeProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<QuestionTypeProxy> event) {
				//Note: question is null in create mode 
				setMediaView(event.getValue(),question);
				setDigitCount();
			}

		});
		questionTextArea.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				setDigitCount();
			}
		});
		
		setDigitCount();
		
		setResendToReviewBtn(isResendToReview);
		
		if(isAdminOrInstitutionalAdmin == false) {
			forcedActive.removeFromParent();
		}
	}

	@Override
	public void setResendToReviewBtn(boolean isResendToReview) {
		if(isResendToReview == false) {
			resendToReview.removeFromParent();
		} else {
			resendToReview.setVisible(true);
		}
	}

	private void setDigitCount() {
		if(questionType.getValue() != null) {
			final int length = questionTextArea.getText().length();
			final String lengthText;
			if(length > questionType.getValue().getQuestionLength()) {
				lengthText =  "<span style='color:red'>" + length + "</span>";
			}else {
				lengthText =  String.valueOf(length);
			}
			lblDigitCount.setHTML(messages.questionDigitCount(lengthText, String.valueOf(questionType.getValue().getQuestionLength())));	
		} else {
			lblDigitCount.setHTML(messages.questionDigitCount("0", "0"));
		}
	}
	
	@Override
	public void setRichPanelHTML(String html) {
		Log.info(html);
		questionTextArea.setHTML(html);
	}
	
	@Override
	public void setValue(QuestionProxy question,boolean isAuthorReviewerEditable) {
		if(Status.CREATIVE_WORK.equals(question.getStatus()) == false) {
			pushToReviewProcess.removeFromParent();
			forcedActive.removeFromParent();
			DOM.setElementPropertyBoolean(questionType.getElement(), "disabled", true);
		} else {
			if(question.getRewiewer() != null && question.getRewiewer().getId().equals(userLoggedIn.getId())) {
				pushToReviewProcess.removeFromParent();
			}
		}
		
		this.isAuthorReviewerEditable = isAuthorReviewerEditable;
		disableEnableAuthorReviewerSuggestBox(isAuthorReviewerEditable);
		
		questionShortName.setValue(defaultString(question.getQuestionShortName()));
		questionType.setValue(question.getQuestionType());
		questionTextArea.setHTML(defaultString(question.getQuestionText()));
		
		if(isAuthorReviewerEditable == true) {
			author.setSelected(question.getAutor());
			rewiewer.setSelected(question.getRewiewer());	
			if(question.getSubmitToReviewComitee()==true)
			{
				rewiewer.setEnabled(false);
			}
			else
			{
				rewiewer.setEnabled(true);
			}
		} else {
	
			if (question.getAutor() != null)
				lblAutherValue.setText(question.getAutor().getName() + " " + question.getAutor().getPrename());
			
			if (question.getRewiewer() != null)
				lblReviewerValue.setText(question.getRewiewer().getName() + " " + question.getRewiewer().getPrename());
			
			lblSubmitToReviewComitee.setText(String.valueOf(question.getSubmitToReviewComitee()));
		}
		
		questEvent.setValue(question.getQuestEvent());
		questionComment.setValue(question.getComment() == null ? "" : question.getComment());
		submitToReviewComitee.setValue(question.getSubmitToReviewComitee());
		mcs.setValue(question.getMcs());
		
		this.question = question;

		// question is not null
		setMediaView(question.getQuestionType(), question);
		//resendToReview.setVisible(delegate.isAdminOrReviewer() && delegate.isAcceptQuestionView());
		
		setDigitCount();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}

	@Override
	public void setRewiewerPickerValues(Collection<PersonProxy> values, PersonProxy lastSelectedReviewer) {
		//rewiewer.setAcceptableValues(values);
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) rewiewer.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		 /* Collection<MyObjectType> myCollection = ...;
		 List<MyObjectType> list = new ArrayList<MyObjectType>(myCollection);*/
		rewiewer.setSuggestOracle(suggestOracle1);
		rewiewer.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
				if(object!=null)
				{
					return object.getName() + " "+ object.getPrename();
				}
				else
				{
					return "";
				}
			}
		});
		//change {
		rewiewer.setWidth(150);
		
		if (lastSelectedReviewer != null)
			rewiewer.setSelected(lastSelectedReviewer);
	}

	@Override
	public void setQuestEventPickerValues(Collection<QuestionEventProxy> values) {
		questEvent.setAcceptableValues(values);
	}

	@Override
	public void setAutorPickerValues(Collection<PersonProxy> values) {
		//autor.setAcceptableValues(values);
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) author.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		 /* Collection<MyObjectType> myCollection = ...;
		 List<MyObjectType> list = new ArrayList<MyObjectType>(myCollection);*/
		author.setSuggestOracle(suggestOracle1);
		author.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
				if(object!=null)
				{
					return object.getName() + " "+ object.getPrename();
				}
				else
				{
					return "";
				}
			}
		});
		//change {
		author.setWidth(150);

		if(delegate.isAdminOrInstitutionalAdmin() == false) {
			author.setSelected(userLoggedIn);
			author.setEnabled(false);
		}

	}

	@Override
	public void setQuestionTypePickerValues(List<QuestionTypeProxy> values) {
		if(values != null && values.isEmpty() == false) {
			questionType.setValue(values.get(0));
			setMediaView(questionType.getValue(),question);
		}
		questionType.setAcceptableValues(values);
		setDigitCount();
	}

	@Override
	public void setMcsPickerValues(List<McProxy> values) {
		 mcs.setAcceptableValues(values);
	}

	@Override
	public void setEditTitle(boolean edit) {
		if (edit) {
			title.setInnerText(constants.edit());
		} else {
			title.setInnerText(constants.create());
		}
	}

	@Override
	public ValueListBox<QuestionEventProxy> getQuestionEvent() {
		return questEvent;
	}
	
	// here for new question may be null
	private void setMediaView(final QuestionTypeProxy questionTypeProxy,final QuestionProxy question) {
	
		Log.info("In set Media View");
	
		final QuestionTypes questionType = questionTypeProxy.getQuestionType();
		switch (questionType) {
		
		case Textual:
		{
			setResourceUploadAndResourceViewer(questionTypeProxy, question);
			break;
		}	
			
		case Imgkey:
		{
			setImageViewer(questionTypeProxy, question,QuestionTypes.Imgkey);
			break;
		}
			
		case ShowInImage:
		{
			setImageViewer(questionTypeProxy, question,QuestionTypes.ShowInImage);
			break;
		}
		
		case Sort:
		{
			setResourceUploadAndResourceViewer(questionTypeProxy, question);
			break;
		}
		case LongText:
		{
			setResourceUploadAndResourceViewer(questionTypeProxy, question);
			break;			
		}
		case Drawing:
		{
			setResourceUploadAndResourceViewer(questionTypeProxy, question);
			break;
		}
		default:
		{
			clearMediaContainer();
			Log.info("question type :" + questionType + " is not implemented in setMediaView");
			break;	
		}
		
		}
		
	}
	
	private void setImageViewer(final QuestionTypeProxy questionTypeProxy, final QuestionProxy questionProxy,final QuestionTypes type) {
		
		//remove extra part
		clearMediaContainer();
		
		final ImageViewer imageViewer;
		if(this.imageViewer == null) {
			imageViewer = new ImageViewer();
			this.imageViewer = imageViewer;
		}else {
			imageViewer = this.imageViewer;	
		}
		
		this.imageViewer.clear();
		if(questionProxy != null ) {
			List<QuestionResourceProxy> questionResources = Lists.newArrayList(questionProxy.getQuestionResources());
			
			if(questionResources != null && questionResources.isEmpty() == false) {
			
				final QuestionResourceProxy questionResourceProxy = questionResources.get(0);
				final Integer width = questionResourceProxy.getImageWidth();
				final Integer height = questionResourceProxy.getImageHeight();
				final String imagePath = questionResourceProxy.getPath();
				final String name = questionResourceProxy.getName();
						
				if (width != null && height != null)
				{
					imageViewer.setUrl(name,imagePath, width, height, type);
				}
				else
				{
					imageViewer.setUrl(name,imagePath, null, null, type);
				}
			}
		}
			
		ArrayList<String> allowedExt = new ArrayList<String>();
		Map<MultimediaType, String> paths = Maps.newHashMap();
		
		allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
		paths.put(MultimediaType.Image, SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
		
		ResourceUpload resourceUpload = new ResourceUpload(allowedExt,paths/*,this.eventBus*/); 
		
		resourceUpload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
			
			@Override
			public void onResourceUploaded(final ResourceUploadEvent event) {

				final String filePath = event.getFilePath();
				
				if(event.isResourceUploaded() == true) {
					Log.info("filePath is " + filePath);
					
					// for image
					//final String url = new String(GWT.getHostPageBaseURL() + filePath);
					if(questionTypeProxy != null/* && questionTypeProxy.getImageWidth() != null && questionTypeProxy.getImageHeight() != null*/) {
						
						Function<Boolean, Void> function = new Function<Boolean, Void>() {
							
							@Override
							public Void apply(Boolean flag) {
						
								if(flag != null && flag == true) {
									Log.info("picturePath : " + filePath);
									if(imageViewer != null && imageViewer.getImageUrl() != null && imageViewer.getImageUrl().length() > 0) {
										// delete old files
										Log.info("Delete old uploaded file " + imageViewer.getImageUrl().toString());
										//delegate.deleteMediaFileFromDisk(imageViewer.getImageUrl().replace(GWT.getHostPageBaseURL(), ""));
									}
									
									imageViewer.setUrl(event.getName(),filePath, event.getWidth(), event.getHeight(), type);	
								} else {
									ConfirmationDialogBox.showOkDialogBox(constants.error(), messages.imageUploadSize(questionTypeProxy.getImageWidth(),questionTypeProxy.getImageHeight()));
									/*ErrorPanel errorPanel = new ErrorPanel();
									errorPanel.setErrorMessage("Only Upload image of size" + questionTypeProxy.getImageWidth() + "*" + questionTypeProxy.getImageHeight());*/
									//delegate.deleteMediaFileFromDisk(filePath);
								}

								return null;
							}
						};
						
						if(event.getWidth() != null && event.getHeight() != null) {
							/*if(event.getWidth().equals(questionTypeProxy.getImageWidth()) && event.getHeight().equals(questionTypeProxy.getImageHeight())) {
								function.apply(true);
							}else {
								function.apply(false);
							}*/
							function.apply(true);
						}else {
							//ClientUtility.checkImageSize(url,questionTypeProxy.getImageWidth(),questionTypeProxy.getImageHeight(),function);
							Log.error("Error in event width or height");
						}
							
					}else {
						Log.error("Error in questionType.");
					}
				}else {
					Log.error("Upload fail.");
				}
			}
		});
		
		setMediaContainer(resourceUpload,paths.keySet(), imageViewer);
	}
	
	private void setResourceUploadAndResourceViewer(QuestionTypeProxy questionType, QuestionProxy question) {	
	
		//remove extra part
		clearMediaContainer();
		
		if(questionType != null &&  questionType.getQuestionType() != null ) {
				
			// added viewer
			List<QuestionResourceProxy> questionResources = new ArrayList<QuestionResourceProxy>();
			if(question != null && question.getQuestionResources() != null) {
				
				questionResources.addAll(question.getQuestionResources());
				Collections.sort(questionResources, ClientUtility.QUESTION_RESOURCE_SEQUENCE_COMARATOR);
			}
			viewer = new ResourceView(eventBus,ClientUtility.getQuestionResourceClient(questionResources),questionType.getQuestionType(),questionType.getQueHaveImage(),questionType.getQueHaveSound(),questionType.getQueHaveVideo(),true);
			
			viewer.addResourceAddedHandler(new ResourceAddedEventHandler(){

				@Override
				public void onResourceAdded(ResourceAddedEvent event) {
					
					if(!event.isAdded()) {
						ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.mediaTypeNotAllowed());
						/*ErrorPanel errorPanel = new ErrorPanel();
						errorPanel.setErrorMessage("This type of media is not allowed");*/
						//delegate.deleteMediaFileFromDisk(event.getQuestionResourceClient().getPath()); 
					}
				}
			});	
			
			/*viewer.addResourceDeletedHandler(new ResourceDeletedEventHandler() {

				@Override
				public void onResourceDeleted(ResourceDeletedEvent event) {
					Log.info("QuestionResourceClient : " + event.getQuestionResourceClient().getPath());
					
					delegate.deleteSelectedQuestionResource(event.getQuestionResourceClient().getId());
				}
			
			});*/
			// allowed extension
			ArrayList<String> allowedExt = new ArrayList<String>();
			Map<MultimediaType, String> paths = Maps.newHashMap();
			if(questionType.getQueHaveImage() != null && questionType.getQueHaveImage() == true) {
				allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
				paths.put(MultimediaType.Image,SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
			}
			
			if(questionType.getQueHaveSound()  != null && questionType.getQueHaveSound() == true) {
				allowedExt.addAll(Arrays.asList(SharedConstant.SOUND_EXTENSIONS));
				paths.put(MultimediaType.Sound,SharedConstant.UPLOAD_MEDIA_SOUND_PATH);
			}
			
			if(questionType.getQueHaveVideo()  != null && questionType.getQueHaveVideo() == true) {
				allowedExt.addAll(Arrays.asList(SharedConstant.VIDEO_EXTENSIONS));
				paths.put(MultimediaType.Video,SharedConstant.UPLOAD_MEDIA_VIDEO_PATH);
			}			
			
			// added resourceUpload
			ResourceUpload resourceUpload = new ResourceUpload(allowedExt,paths/*,eventBus*/);
			 
			resourceUpload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(ResourceUploadEvent event) {
					String filePath = event.getFilePath();
					
					if(event.isResourceUploaded() == true) {
						Log.info("filePath is " + filePath);
						
						MultimediaType type = event.getType();
						if (viewer != null) {
							viewer.addUrl(event.getName(),filePath, type, event.getHeight(), event.getWidth());
						}else {
							Log.error("Viewer is null");
						}		
					}else {
						Log.error("Upload fail.");
					}
				}
			});
			
			
			if(questionType != null) {
				
				boolean flag = Boolean.TRUE.equals(questionType.getQueHaveImage())
								|| Boolean.TRUE.equals(questionType.getQueHaveSound())
								|| Boolean.TRUE.equals(questionType.getQueHaveVideo());
						
				if(flag == true) {
					// added to container
					setMediaContainer(resourceUpload,paths.keySet(), viewer);	
				}else {
					Log.info("Flag is false : " + questionType.getQueHaveImage() +"," + questionType.getQueHaveSound() + "," + questionType.getQueHaveVideo());
				}
					
			}
		}
	}
	
	private void clearMediaContainer() {
		//remove extra part
		lblUploadText.removeStyleName("label");
		lblUploadText.setText("");
		uploaderContainer.clear();
		viewerContainer.clear();		
	}
	
	private void setMediaContainer(Widget upload,Set<MultimediaType> set, Widget viewer) {
		lblUploadText.addStyleName("label");
		lblUploadText.setText(ClientUtility.getUploadLabel(set));
		uploaderContainer.add(upload);
		viewerContainer.add(viewer);
	}

	@Override
	public Set<QuestionResourceClient> getQuestionResources() {
		if(viewer == null) {
			return  Sets.newHashSet();
		}
		return viewer.getQuestionResources();
	}
	
	private boolean validationOfFields(Boolean isCreativeWork) {
		
		removeHighlight();
		ArrayList<String> messages = Lists.newArrayList();
		boolean flag = true;
		
		setSubmitToReviewCommittee();
		
		if(isCreativeWork == true) {
			flag = questionTypeValidation(messages, flag);
			flag = questionTextValidation(messages, flag);
		} else {
			flag = authorReviewerValidation(messages, flag);
			flag = questionTypeValidation(messages, flag);
			flag = questionTextValidation(messages, flag);
			flag = imageKeyAndShowInImageValidation(messages, flag);
			flag = mcValidation(messages, flag);
		}
		
		if(flag == false) {
			ReceiverDialog.showMessageDialog(constants.pleaseEnterWarning(),messages);
		}
		
		return flag;
	}

	private void setSubmitToReviewCommittee() {
		if(submitToReviewComitee.getValue() == true) 
		{
			if(rewiewer.isAttached()) rewiewer.setSelected(null);	
		} 
		else if(rewiewer.isAttached() && rewiewer.getSelected() != null)
		{
			submitToReviewComitee.setValue(false);
		}
	}

	private void removeHighlight() {
		author.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		//questionComment.removeStyleName("higlight_onViolation");
		questionType.removeStyleName("higlight_onViolation");
		questionTextArea.removeStyleName("higlight_onViolation");
		if(imageViewer != null)
			imageViewer.removeStyleName("higlight_onViolation");
		rewiewer.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		submitToReviewComitee.removeStyleName("higlight_onViolation");
		mcs.removeStyleName("higlight_onViolation");
	}

	private boolean mcValidation(ArrayList<String> messages, boolean flag) {
		if(mcs.getValue() == null || mcs.getValue().isEmpty()) {
			flag = false;
			messages.add(constants.mcsMayNotBeNull());
			mcs.addStyleName("higlight_onViolation");
		}
		return flag;
	}

	private boolean imageKeyAndShowInImageValidation(ArrayList<String> messages, boolean flag) {
		if(questionType.getValue() != null) {
			switch (questionType.getValue().getQuestionType()) {
			
			case Imgkey:
			case ShowInImage:
			{
				if(imageViewer.getImageRelativeUrl() == null || imageViewer.getImageRelativeUrl().isEmpty()) {
					flag = false;
					messages.add(constants.imageMayNotBeNull());
					imageViewer.addStyleName("higlight_onViolation");
				}
				break;
			}
			}
		}
		return flag;
	}

	private boolean questionTextValidation(ArrayList<String> messages, boolean flag) {
		//TODO need to change this condition
		if(questionTextArea.getText() == null || questionTextArea.getText().isEmpty()) {
			flag = false;
			messages.add(constants.questionTextMayNotBeNull());
			questionTextArea.addStyleName("higlight_onViolation");
		}else {
			// question text is not null
			
			questionTextRemoveCommentsFromHTML();
			if(questionType.getValue() != null && questionType.getValue().getQuestionLength()  != null && questionType.getValue().getQuestionLength() < questionTextArea.getText().length()) {
				flag = false;
				messages.add(constants.questionTextMaxLength());
				questionTextArea.addStyleName("higlight_onViolation");
			}
		}
		return flag;
	}

	private boolean questionTypeValidation(ArrayList<String> messages, boolean flag) {
		if(questionType.getValue() == null) {
			flag = false;
			messages.add(constants.questionTypeMayNotBeNull());
			questionType.addStyleName("higlight_onViolation");
		}
		return flag;
	}

	private boolean authorReviewerValidation(ArrayList<String> messages, boolean flag) {
		if(isAuthorReviewerEditable == true ) {
			if(submitToReviewComitee.getValue() == true) 
			{
				if(rewiewer.isAttached()) rewiewer.setSelected(null);	
			} 
			else if(rewiewer.isAttached() && rewiewer.getSelected() != null)
			{
				submitToReviewComitee.setValue(false);
			}
			else 
			{
				flag = false;
				messages.add(constants.selectReviewerOrComitee());
				rewiewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
				submitToReviewComitee.addStyleName("higlight_onViolation");
			}
			
			if(author.getSelected() == null) {
				flag = false;
				messages.add(constants.authorMayNotBeNull());
				author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			}
			
			if(author.getSelected() != null && rewiewer.getSelected() != null && author.getSelected().getId().equals(rewiewer.getSelected().getId()) == true) {
				flag = false;
				messages.add(constants.authorReviewerMayNotBeSame());
				author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
				rewiewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			}
		}
		return flag;
	}

	@Override
	public void setValuesForQuestion(QuestionProxy question) {
		if(rewiewer != null && rewiewer.getSelected() != null)
			Cookies.setCookie(McAppConstant.LAST_SELECTED_QUESTION_REVIEWER, String.valueOf(rewiewer.getSelected().getId()), ClientUtility.getDateFromOneYear());
		
		question.setQuestionType(questionType.getValue());
		question.setQuestionShortName(questionShortName.getText());
		question.setQuestionText(questionTextArea.getHTML());
		Log.info("Question Text length " + questionTextArea.getHTML().length());
		
		if(isAuthorReviewerEditable == true) {
			question.setAutor(author.getSelected());
			question.setRewiewer(rewiewer.getSelected());
			question.setSubmitToReviewComitee(submitToReviewComitee.getValue());
		} else {
			question.setAutor(this.question.getAutor());
			question.setRewiewer(this.question.getRewiewer());
			question.setSubmitToReviewComitee(this.question.getSubmitToReviewComitee());
		}
		
		
		question.setQuestEvent(questEvent.getValue());
		question.setMcs(mcs.getValue());
		question.setComment(questionComment.getText().isEmpty() == true ? " " : questionComment.getText());
	}

	
	@Override
	public void addPictureToQuestionResources(QuestionResourceProxy questionResourceProxy) {
		String picturePath = null;
		Integer height = null;
		Integer width = null;
		String name;
		switch (questionType.getValue().getQuestionType()) {
		
		case Imgkey:
		case ShowInImage:
		{
			picturePath = imageViewer.getImageRelativeUrl();
			height = imageViewer.getHeight();
			width = imageViewer.getWidth();
			name = imageViewer.getName();
			questionResourceProxy.setImageHeight(height);
			questionResourceProxy.setImageWidth(width);
			questionResourceProxy.setPath(picturePath);
			questionResourceProxy.setName(name);
			break;
		}
		
		default:
		{
			Log.info("in default case");
			picturePath = null;
			height = null;
			width = null;
			
			break;
		}
		} 
		
		

		/*question.setPicturePath(picturePath);
		question.setImageHeight(height);
		question.setImageWidth(width);*/
	}
	
	@Override
	public void comfirmQuestionChanges(ConfirmQuestionHandler handler,boolean isAdminOrInstitutionalAdmin) {
		new ConfirmQuestionChangesPopup(handler,isAdminOrInstitutionalAdmin);
	}

	@Override
	public Long getAuthorId() {
		if(author == null || author.getSelected() == null) {
			return -1l;
		}
		
		return author.getSelected().getId();
	}

	public void disableEnableAuthorReviewerValue(boolean flag)
	{
		lblAutherValue.setVisible(flag);
		lblReviewerValue.setVisible(flag);
		lblSubmitToReviewComitee.setVisible(flag);
	}
	
	private void disableEnableAuthorReviewerSuggestBox(boolean isAuthorReviewerEditable) {
		
		if (isAuthorReviewerEditable == true)
		{
			disableEnableAuthorReviewerValue(false);
			lblReviewerValue.removeFromParent();
			lblAutherValue.removeFromParent();
			lblSubmitToReviewComitee.removeFromParent();
		}
		else
		{
			disableEnableAuthorReviewerValue(true);
			author.removeFromParent();
			rewiewer.removeFromParent();
			submitToReviewComitee.removeFromParent();
		}	
		
	}

	@Override
	public void setQuestionAuthor(PersonProxy autor) {
		if(autor != null)
			author.setSelected(autor);
	}
	
	private void questionTextRemoveCommentsFromHTML() {
		if(questionTextArea.getHTML().length() >= 9000) {
			String html = questionTextArea.getHTML();
			for (int i = 0; i < 100; i++) {
				if(html.contains("<!--") && html.contains("-->")) {
					int startIndex = html.indexOf("<!--");
					int endIndex = html.indexOf("-->");
					int htmlLength = html.length();
					Log.info("Start Index" + startIndex);
					Log.info("End index " + endIndex);
					Log.info("Length : " + htmlLength);
					if(startIndex >= 0 && startIndex < htmlLength && endIndex > 0 && endIndex <= htmlLength) {
						html = html.substring(0, startIndex) + html.substring(endIndex+3, htmlLength);	
					}
				} else {
					break;
				}
			}
			questionTextArea.setHTML(html);
		}
	}

	@Override
	public boolean isPictureAddedForImgKeyOrShowInImage() {
		if(imageViewer.getImageRelativeUrl() == null) {
			return false;
		} else {
			return true;	
		}
	}

	@Override
	public ApplicationLoadingView getLoadingPopup() {
		return loadingPopup;
	}
	
	

}

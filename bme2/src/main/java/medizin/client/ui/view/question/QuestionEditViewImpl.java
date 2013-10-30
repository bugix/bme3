package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.roo.QuestionTypeProxyRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.client.ui.widget.labeled.LabeledPanel;
import medizin.client.ui.widget.labeled.LabeledRichTextArea;
import medizin.client.ui.widget.labeled.LabeledTextArea;
import medizin.client.ui.widget.labeled.LabeledTextBox;
import medizin.client.ui.widget.labeled.LabeledValueListBox;
import medizin.client.ui.widget.mcs.McCheckboxEditor;
import medizin.client.ui.widget.resource.dndview.ResourceView;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.event.ResourceAddedEvent;
import medizin.client.ui.widget.resource.event.ResourceAddedEventHandler;
import medizin.client.ui.widget.resource.event.ResourceDeletedEvent;
import medizin.client.ui.widget.resource.event.ResourceDeletedEventHandler;
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
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeContextHelpConstants;
import medizin.shared.i18n.BmeMessages;
import medizin.shared.utils.SharedConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;


public class QuestionEditViewImpl extends Composite implements QuestionEditView /* , Editor<QuestionProxy>*/{

	private static QuestionEditViewImplUiBinder uiBinder = GWT.create(QuestionEditViewImplUiBinder.class);

	interface QuestionEditViewImplUiBinder extends UiBinder<Widget, QuestionEditViewImpl> {}

	private Delegate delegate;
	
	public static BmeConstants constants = GWT.create(BmeConstants.class);
	public static BmeContextHelpConstants contextHelp = GWT.create(BmeContextHelpConstants.class);
	public static BmeMessages messages = GWT.create(BmeMessages.class);

	@UiField
	SpanElement title;
	
	@UiField
	public IconButton cancel;
	
	@UiField
	public IconButton cancel2;
	
	@UiField
	public IconButton save;
	
	@UiField
	public IconButton save2;
	
	@UiField
	public IconButton resendToReview;
	
	@UiField
	public IconButton resendToReview2;
	
	@UiField
	public Label baseGroupLbl;
	
	@UiField
	public Label mediaLbl;
	
	@UiField
	public Label reviewLbl;
	
	@UiField
	public Label organisationalOrderLbl;
	
	@UiField
	public Label commentGroupLbl;

	@UiField(provided = true)
	public LabeledValueListBox<QuestionTypeProxy> questionType = new LabeledValueListBox<QuestionTypeProxy>(QuestionTypeProxyRenderer.instance(),new EntityProxyKeyProvider<medizin.client.proxy.QuestionTypeProxy>());
	
	@UiField
	public LabeledPanel questionTypeDescription;
	
	@UiField
	public Label questionTypeDescriptionText;

	@UiField
	public LabeledTextBox questionShortName;
	
	@UiField
	public LabeledRichTextArea questionTextArea;
	
	@UiField
	public LabeledPanel uploadResourcePanel;
	
	@UiField
	public HTMLPanel uploaderContainer;
	
	@UiField
	public LabeledPanel viewerPanel;
	
	@UiField
	public HTMLPanel viewerContainer;

	@UiField 
	McCheckboxEditor mcs;
	
	@UiField
	public LabeledPanel authorPanel;
	
	@UiField
	Label lblAuthorValue;
	
	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> author;
	
	@UiField
	public LabeledPanel reviewerPanel;
	
	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> reviewer;
	
	@UiField
	Label lblReviewerValue;

	@UiField
	public LabeledPanel reviewCommitteePanel;
	
	@UiField
	public CheckBox submitToReviewComitee;
	
	@UiField(provided = true)
	public LabeledValueListBox<QuestionEventProxy> questionEvent = new LabeledValueListBox<QuestionEventProxy>(medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance(),new EntityProxyKeyProvider<medizin.client.proxy.QuestionEventProxy>());
	
	@UiField
	public LabeledPanel mcsPanel;
	
	@UiField
	public LabeledTextArea comment;
	
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
	
	@UiHandler("cancel2")
	void onCancel2(ClickEvent event) {
		delegate.cancelClicked();
	}
	
	private void resendToReview() {
		if(validationOfFields()) {
			delegate.resendToReview();
		}
	}
	
	@UiHandler("resendToReview")
	void onResendToReview(ClickEvent event) {
		resendToReview();
	}
	
	@UiHandler("resendToReview2")
	void onResendToReview2(ClickEvent event) {
		resendToReview();
	}
	
	private void save() {
		if(validationOfFields()) {
			delegate.saveQuestionWithDetails();
		}else {
			Log.info("Validation fail");
		}
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		save();
	}
	
	@UiHandler("save2")
	void onSave2(ClickEvent event) {
		save();
	}
	
	public QuestionEditViewImpl(Map<String, Widget> reciverMap, EventBus eventBus, PersonProxy userLoggedIn,boolean isResendToReview) {

		this.eventBus = eventBus;
		this.userLoggedIn = userLoggedIn;
				
		initWidget(uiBinder.createAndBindUi(this));
		
		reciverMap.put("questionShortName", questionShortName);
		reciverMap.put("questionText", questionTextArea);
		reciverMap.put("questionType", questionType);
		reciverMap.put("autor", author.getTextField().advancedTextBox);
		reciverMap.put("rewiewer", reviewer);
		reciverMap.put("questEvent", questionEvent);
		reciverMap.put("submitToReviewComitee", submitToReviewComitee);
		reciverMap.put("mcs", mcs);
		//reciverMap.put("comment", comment);
		
		save.setText(constants.save());
		save2.setText(constants.save());
		cancel.setText(constants.cancel());
		cancel2.setText(constants.cancel());
		resendToReview.setText(constants.resendToReview());
		resendToReview2.setText(constants.resendToReview());
		
		baseGroupLbl.setText(constants.questionGroupLbl());
		mediaLbl.setText(constants.mediaAttributes());
		reviewLbl.setText(constants.reviewAttributes());
		organisationalOrderLbl.setText(constants.organisationalOrder());
		commentGroupLbl.setText(constants.comment());
		
		questionType.setLabelText(constants.questionType());
		questionType.setHelpText(contextHelp.qQuestionType());
		
		questionShortName.setLabelText(constants.shortName());
		questionShortName.setHelpText(contextHelp.qShortName());
		
		questionTextArea.setLabelText(constants.questionText());
		questionTextArea.setHelpText(contextHelp.qQuestion());
		
		uploadResourcePanel.setLabelText(constants.uploadResource());
		uploadResourcePanel.setHelpText(contextHelp.qUploadResource());
		
		viewerPanel.setLabelText(constants.mediaViewer());
		viewerPanel.setHelpText(contextHelp.qMediaViewer());
		
		authorPanel.setLabelText(constants.auther());
		authorPanel.setHelpText(contextHelp.qAuthor());
		
		reviewerPanel.setLabelText(constants.reviewer());
		reviewerPanel.setHelpText(contextHelp.qReviewer());
		
		reviewCommitteePanel.setLabelText(constants.submitToReviewComitee());
		reviewCommitteePanel.setHelpText(contextHelp.qReviewCommittee());
		
		mcsPanel.setLabelText(constants.mcs());
		mcsPanel.setHelpText(contextHelp.qMcs());
		
		comment.setLabelText(constants.comment());
		comment.setHelpText(contextHelp.qComment());
		
		submitToReviewComitee.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if(submitToReviewComitee.getValue()) {
					reviewer.setEnabled(false);
				} else {
					reviewer.setEnabled(true);
				}
			}
		});
				
		questionType.addValueChangeHandler(new ValueChangeHandler<QuestionTypeProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<QuestionTypeProxy> event) {
				//Note: question is null in create mode 
				QuestionTypeProxy qt = questionType.getValue();
				if (qt != null) {
					questionTypeDescription.setVisible(true);
					questionTypeDescription.setLabelText(qt.getLongName());
					questionTypeDescriptionText.setText(qt.getDescription());
				} else {
					questionTypeDescription.setVisible(false);
				}
				setMediaView(event.getValue(),question);
				setDigitCount();
			}

		});
				
		setDigitCount();
		
		setResendToReviewBtn(isResendToReview);
	}

	@Override
	public void setResendToReviewBtn(boolean isResendToReview) {
		if(!isResendToReview) {
			resendToReview.removeFromParent();
		} else {
			resendToReview.setVisible(true);
		}
	}
	
	private void setDigitCount() {
		int length = 0;
		if (questionType.getValue() != null) {
			length = questionType.getValue().getQuestionLength();
		}
		questionTextArea.setMaxLength(length);
	}
	
	@Override
	public void setRichPanelHTML(String html) {
		Log.info(html);
		questionTextArea.setHTML(html);
	}
	
	@Override
	public void setValue(QuestionProxy question,boolean isAuthorReviewerEditable) {
		this.isAuthorReviewerEditable = isAuthorReviewerEditable;
		DOM.setElementPropertyBoolean(questionType.getElement(), "disabled", true);
		
		disableEnableAuthorReviewerSuggestBox(isAuthorReviewerEditable);
		
		questionShortName.setValue(ClientUtility.defaultString(question.getQuestionShortName()));
		questionType.setValue(question.getQuestionType());
		questionTextArea.setHTML(ClientUtility.defaultString(question.getQuestionText()));
		
		if(isAuthorReviewerEditable) {
			author.setSelected(question.getAutor());
			reviewer.setSelected(question.getRewiewer());	
			if(question.getSubmitToReviewComitee()) {
				reviewer.setEnabled(false);
			} else {
				reviewer.setEnabled(true);
			}
		} else {
			if (question.getAutor() != null) {
				lblAuthorValue.setText(question.getAutor().getName() + " " + question.getAutor().getPrename());
			}
			
			if (question.getRewiewer() != null) {
				lblReviewerValue.setText(question.getRewiewer().getName() + " " + question.getRewiewer().getPrename());
				reviewCommitteePanel.setVisible(false);
			} else {
				reviewerPanel.setVisible(false);
				submitToReviewComitee.setEnabled(false);
			}
		}
		
		questionEvent.setValue(question.getQuestEvent());
		comment.setValue(question.getComment() == null ? "" : question.getComment().getComment());
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
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) reviewer.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		 /* Collection<MyObjectType> myCollection = ...;
		 List<MyObjectType> list = new ArrayList<MyObjectType>(myCollection);*/
		reviewer.setSuggestOracle(suggestOracle1);
		reviewer.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
				if(object!=null) {
					return object.getName() + " "+ object.getPrename();
				} else {
					return "";
				}
			}
		});
		//change {
		//rewiewer.setWidth(150);
		
		if (lastSelectedReviewer != null)
			reviewer.setSelected(lastSelectedReviewer);
	}

	@Override
	public void setQuestEventPickerValues(Collection<QuestionEventProxy> values) {
		questionEvent.setAcceptableValues(values);
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
				if(object!=null) {
					return object.getName() + " "+ object.getPrename();
				} else {
					return "";
				}
			}
		});

		if(!delegate.isAdminOrInstitutionalAdmin()) {
			author.setSelected(userLoggedIn);
			author.setEnabled(false);
		}
	}

	@Override
	public void setQuestionTypePickerValues(List<QuestionTypeProxy> values) {
		if(values != null && !values.isEmpty()) {
			questionType.setValue(values.get(0));
			setMediaView(questionType.getValue(),question);
			questionTypeDescription.setVisible(true);
			questionTypeDescription.setLabelText(values.get(0).getLongName());
			questionTypeDescriptionText.setText(values.get(0).getDescription());
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
			title.setInnerText(constants.editQuestion());
		} else {
			title.setInnerText(constants.createQuestion());
		}
	}

	@Override
	public ValueListBox<QuestionEventProxy> getQuestionEvent() {
		return questionEvent.getValueListBox();
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
		default:
		{
			clearMediaContainer();
			Log.info("question type :" + questionType + " is not implemented in setMediaView");
			break;	
		}
		
		}
		
	}
	
	private void setImageViewer(final QuestionTypeProxy questionTypeProxy, final QuestionProxy questionProxy,final QuestionTypes type) {
		Log.info("setImageViewer()");
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
			
			if(questionResources != null && !questionResources.isEmpty()) {
			
				final QuestionResourceProxy questionResourceProxy = questionResources.get(0);
				final Integer width = questionResourceProxy.getImageWidth();
				final Integer height = questionResourceProxy.getImageHeight();
				final String imagePath = questionResourceProxy.getPath();

				imageViewer.setUrl(imagePath, width, height, type);
			}
		}
			
		ArrayList<String> allowedExt = new ArrayList<String>();
		Map<MultimediaType, String> paths = Maps.newHashMap();
		
		allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
		paths.put(MultimediaType.Image, SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
		
		ResourceUpload resourceUpload = new ResourceUpload(allowedExt,paths); 
		
		resourceUpload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
			
			@Override
			public void onResourceUploaded(final ResourceUploadEvent event) {

				final String filePath = event.getFilePath();
				
				if(event.isResourceUploaded()) {
					Log.info("filePath is " + filePath);
					
					if(questionTypeProxy != null) {
						
						Function<Boolean, Void> function = new Function<Boolean, Void>() {
							
							@Override
							public Void apply(Boolean flag) {
						
								if(flag != null && flag) {
									Log.info("picturePath : " + filePath);
									if(imageViewer != null && imageViewer.getImageUrl() != null && imageViewer.getImageUrl().length() > 0) {
										Log.info("Delete old uploaded file " + imageViewer.getImageUrl().toString());
									}
									
									imageViewer.setUrl(filePath, event.getWidth(), event.getHeight(), type);	
								} else {
									ConfirmationDialogBox.showOkDialogBox(constants.error(), messages.imageUploadSize(questionTypeProxy.getImageWidth(),questionTypeProxy.getImageHeight()));
								}

								return null;
							}
						};
						
						if(event.getWidth() != null && event.getHeight() != null) {
							function.apply(true);
						}else {
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
					}
				}
			});	
			
			viewer.addResourceDeletedHandler(new ResourceDeletedEventHandler() {

				@Override
				public void onResourceDeleted(ResourceDeletedEvent event) {
					Log.info("QuestionResourceClient : " + event.getQuestionResourceClient().getPath());
					
					delegate.deleteSelectedQuestionResource(event.getQuestionResourceClient().getId());
				}
			
			});
			// allowed extension
			ArrayList<String> allowedExt = new ArrayList<String>();
			Map<MultimediaType, String> paths = Maps.newHashMap();
			if(questionType.getQueHaveImage() != null && questionType.getQueHaveImage()) {
				allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
				paths.put(MultimediaType.Image,SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
			}
			
			if(questionType.getQueHaveSound()  != null && questionType.getQueHaveSound()) {
				allowedExt.addAll(Arrays.asList(SharedConstant.SOUND_EXTENSIONS));
				paths.put(MultimediaType.Sound,SharedConstant.UPLOAD_MEDIA_SOUND_PATH);
			}
			
			if(questionType.getQueHaveVideo()  != null && questionType.getQueHaveVideo()) {
				allowedExt.addAll(Arrays.asList(SharedConstant.VIDEO_EXTENSIONS));
				paths.put(MultimediaType.Video,SharedConstant.UPLOAD_MEDIA_VIDEO_PATH);
			}			
			
			// added resourceUpload
			ResourceUpload resourceUpload = new ResourceUpload(allowedExt,paths/*,eventBus*/);
			 
			resourceUpload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(ResourceUploadEvent event) {
					String filePath = event.getFilePath();
					
					if(event.isResourceUploaded()) {
						Log.info("filePath is " + filePath);
						
						MultimediaType type = event.getType();
						if (viewer != null) {
							viewer.addUrl(filePath, type);
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
						
				if(flag) {
					// added to container
					setMediaContainer(resourceUpload,paths.keySet(), viewer);	
				}else {
					Log.info("Flag is false : " + questionType.getQueHaveImage() +"," + questionType.getQueHaveSound() + "," + questionType.getQueHaveVideo());
				}
					
			}
		}
	}
	
	private void clearMediaContainer() {
		uploadResourcePanel.setLabelText("");
		uploaderContainer.clear();
		viewerContainer.clear();		
	}
	
	private void setMediaContainer(Widget upload,Set<MultimediaType> set, Widget viewer) {
		uploadResourcePanel.setLabelText(ClientUtility.getUploadLabel(set));
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
	
	private boolean validationOfFields() {
		author.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		questionType.removeStyleName("higlight_onViolation");
		questionTextArea.removeStyleName("higlight_onViolation");
		if(imageViewer != null)
			imageViewer.removeStyleName("higlight_onViolation");
		reviewer.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		submitToReviewComitee.removeStyleName("higlight_onViolation");
		mcs.removeStyleName("higlight_onViolation");
		
		ArrayList<String> messages = Lists.newArrayList();
		boolean flag = true;
		
		if (isAuthorReviewerEditable) {
			if(submitToReviewComitee.getValue()) {
				reviewer.setSelected(null);	
			} else if(reviewer.getSelected() != null) {
				submitToReviewComitee.setValue(false);
			} else {
				flag = false;
				messages.add(constants.selectReviewerOrComitee());
				reviewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
				submitToReviewComitee.addStyleName("higlight_onViolation");
			}
			
			if(author.getSelected() == null) {
				flag = false;
				messages.add(constants.authorMayNotBeNull());
				author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			}
		
			if(author.getSelected() != null && reviewer.getSelected() != null && author.getSelected().getId().equals(reviewer.getSelected().getId())) {
				flag = false;
				messages.add(constants.authorReviewerMayNotBeSame());
				author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
				reviewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			}	
		}
		
		if(questionType.getValue() == null) {
			flag = false;
			messages.add(constants.questionTypeMayNotBeNull());
			questionType.addStyleName("higlight_onViolation");
		}
		
		//TODO need to change this condition
		if(questionTextArea.getText() == null || questionTextArea.getText().isEmpty()) {
			flag = false;
			messages.add(constants.questionTextMayNotBeNull());
			questionTextArea.addStyleName("higlight_onViolation");
		}else {
			// question text is not null
			
			if(questionType.getValue() != null && questionType.getValue().getQuestionLength()  != null && questionType.getValue().getQuestionLength() < questionTextArea.getText().length()) {
				flag = false;
				messages.add(constants.questionTextMaxLength());
				questionTextArea.addStyleName("higlight_onViolation");
			}
		}
		
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
			default:
				break;
			}
		} 
		
		if(mcs.getValue() == null || mcs.getValue().isEmpty()) {
			flag = false;
			messages.add(constants.mcsMayNotBeNull());
			mcs.addStyleName("higlight_onViolation");
		}
		if(!flag) {
			ReceiverDialog.showMessageDialog(constants.pleaseEnterWarning(),messages);
		}
		
		return flag;
	}

	@Override
	public void setValuesForQuestion(QuestionProxy question, CommentProxy commentProxy) {
		if(reviewer != null && reviewer.getSelected() != null)
			Cookies.setCookie(McAppConstant.LAST_SELECTED_QUESTION_REVIEWER, String.valueOf(reviewer.getSelected().getId()), ClientUtility.getDateFromOneYear());
		
		question.setQuestionType(questionType.getValue());
		question.setQuestionShortName(questionShortName.getText());
		question.setQuestionText(questionTextArea.getHTML());
		
		if(isAuthorReviewerEditable) {
			question.setAutor(author.getSelected());
			question.setRewiewer(reviewer.getSelected());
			question.setSubmitToReviewComitee(submitToReviewComitee.getValue());
		} else {
			question.setAutor(this.question.getAutor());
			question.setRewiewer(this.question.getRewiewer());
			question.setSubmitToReviewComitee(this.question.getSubmitToReviewComitee());
		}
		
		
		question.setQuestEvent(questionEvent.getValue());
		question.setMcs(mcs.getValue());
		commentProxy.setComment(comment.getText().isEmpty() ? " " : comment.getText());
		question.setComment(commentProxy);
	}

	
	@Override
	public void addPictureToQuestionResources(QuestionResourceProxy questionResourceProxy) {
		String picturePath = null;
		Integer height = null;
		Integer width = null;
		
		switch (questionType.getValue().getQuestionType()) {
		
		case Imgkey:
		case ShowInImage:
		{
			picturePath = imageViewer.getImageRelativeUrl();
			height = imageViewer.getHeight();
			width = imageViewer.getWidth();
			questionResourceProxy.setImageHeight(height);
			questionResourceProxy.setImageWidth(width);
			questionResourceProxy.setPath(picturePath);
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
	}
	
	@Override
	public void comfirmQuestionChanges(Function<Boolean, Void> isMajorOrMinor) {
		new ConfirmQuestionChangesPopup(isMajorOrMinor);
	}

	@Override
	public Long getAuthorId() {
		if(author == null || author.getSelected() == null) {
			return -1l;
		}
		
		return author.getSelected().getId();
	}
	
	private void disableEnableAuthorReviewerSuggestBox(boolean isAuthorReviewerEditable) {
		lblReviewerValue.setVisible(!isAuthorReviewerEditable);
		lblAuthorValue.setVisible(!isAuthorReviewerEditable);
		author.setVisible(isAuthorReviewerEditable);
		reviewer.setVisible(isAuthorReviewerEditable);
		submitToReviewComitee.setVisible(isAuthorReviewerEditable);
	}

	@Override
	public void setQuestionAuthor(PersonProxy autor) {
		if(autor != null)
			author.setSelected(autor);
	}

}

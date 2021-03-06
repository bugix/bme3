package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.client.ui.widget.process.ApplicationLoadingView;
import medizin.client.ui.widget.resource.audio.AudioViewer;
import medizin.client.ui.widget.resource.image.polygon.ImagePolygonViewer;
import medizin.client.ui.widget.resource.image.rectangle.ImageRectangleViewer;
import medizin.client.ui.widget.resource.image.simple.SimpleImageViewer;
import medizin.client.ui.widget.resource.upload.ResourceUpload;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEvent;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEventHandler;
import medizin.client.ui.widget.resource.video.VideoViewer;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.client.util.ClientUtility;
import medizin.client.util.ImageWidthHeight;
import medizin.client.util.Point;
import medizin.client.util.PolygonPath;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.Validity;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeMessages;
import medizin.shared.utils.SharedConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnswerDialogboxImpl extends DialogBox implements AnswerDialogbox/*,Editor<AnswerProxy>*/{

	private static AnswerDialogboxImplUiBinder uiBinder = GWT.create(AnswerDialogboxImplUiBinder.class);

	interface AnswerDialogboxImplUiBinder extends UiBinder<Widget, AnswerDialogboxImpl> {}

	@UiField
	IconButton save;

	@UiField
	IconButton closeButton;

	@UiField
	public CheckBox submitToReviewComitee;

	@UiField
	public TextArea comment;
	
	/*@UiField
	TabPanel questionTypePanel;
*/
/*	@UiField
	public Label lblAuther;*/
	
	@UiField
	VerticalPanel viewContainer;

	@UiField
	VerticalPanel uploadContainer;
	
	@UiField
	Label lblUploadText;
	
	@UiField
	ApplicationLoadingView loadingPopup;
	
	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> author;
	
	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> rewiewer;
	
	@UiField
	Label lblAdditionalKeyword;
	
	@UiField
	Button btnAdditionalKeyword;
	
	@UiField
	TextArea txtAdditionalKeyword;
	
	@UiField
	Label lblSequenceNumber;
	
	@UiField
	TextBox txtSequenceNumber;
	
	@UiField
	DivElement descriptionValue;
	
	@UiField(provided = true)
	RichTextToolbar toolbar;
	
	@UiField(provided = true)
	RichTextArea answerTextArea;
	
	@UiField
	SpanElement digitMin;
	
	@UiField
	SpanElement digitMax;
	
	@UiField
	SpanElement digitCurrent;
	
	@UiField
	DivElement digitCount;
	
	@UiField
	CheckBox forcedActive;

	private Delegate delegate;
	private AnswerProxy answer;
    private final QuestionProxy question;
	//private final EventBus eventBus;
	
	public final static BmeMessages bmeMessages = GWT.create(BmeMessages.class);
	public final static BmeConstants constants = GWT.create(BmeConstants.class);

	private ImagePolygonViewer imagePolygonViewer;
	private ImageRectangleViewer imageRectangleViewer;
	private SimpleImageViewer simpleImageViewer;
	private AudioViewer audioViewer;
	private VideoViewer videoViewer;
	private Long answerTextMaxDiff = 0l;
	private Long answerTextMinDiff = 0l;

	@UiHandler("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
		hide();

	}

	/*@Override
	public CheckBox getSubmitToReviewerComitee() {
		return submitToReviewComitee;
	}

	@Override
	public TextArea getComment() {
		return comment;
	}*/

	/*
	 * interface EditorDriver extends RequestFactoryEditorDriver<AnswerProxy,
	 * AnswerDialogboxImpl> {}
	 */
	// private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

	/*
	 * @Override public
	 * RequestFactoryEditorDriver<AnswerProxy,AnswerDialogboxImpl>
	 * createEditorDriver() { RequestFactoryEditorDriver<AnswerProxy,
	 * AnswerDialogboxImpl> driver = GWT.create(EditorDriver.class);
	 * driver.initialize(this); return driver;
	 * 
	 * }
	 */

	/*
	 * @UiField SimplePanel toolbarPanel;
	 */
		
	public AnswerDialogboxImpl(QuestionProxy questionProxy, EventBus eventBus, Map<String, Widget> reciverMap, boolean isAdminOrInstitutionalAdmin) {
		
		this.question = questionProxy;
		//this.eventBus = eventBus;
		answerTextArea = new RichTextArea();
		answerTextArea.setSize("100%", "14em");
		toolbar = new RichTextToolbar(answerTextArea);
		toolbar.setWidth("100%");
		
		setWidget(uiBinder.createAndBindUi(this));
		
		reciverMap.put("answerText",answerTextArea);
		reciverMap.put("autor", author.getTextField().advancedTextBox);
		reciverMap.put("rewiewer", rewiewer.getTextField().advancedTextBox);
		reciverMap.put("validity", validity);
		reciverMap.put("submitToReviewComitee", submitToReviewComitee);
		//reciverMap.put("comment", comment);
		reciverMap.put("additionalKeywords", txtAdditionalKeyword);
		reciverMap.put("sequenceNumber", txtSequenceNumber);
		
		/*reciverMap.put("mediaPath", RunTimeGenerated);*/

		
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setTitle(constants.answerDialogBoxTitle());
		setText(constants.answerDialogBoxTitle());
		setHeight("100%");
		
		if(isAdminOrInstitutionalAdmin == false) {
			forcedActive.removeFromParent();
		}
		/*questionTypePanel.selectTab(0);
		questionTypePanel.getTabBar().setTabText(0, "Manage Answer");
		questionTypePanel.getTabBar().setTabText(1, "Media");*/

		/*lblAuther.setText(constants.auther());*/
		/*RichTextToolbar toolbar = new RichTextToolbar(answerTextArea);*/

		// toolbarPanel.add(toolbar);
		submitToReviewComitee.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(submitToReviewComitee.getValue())
				{
					rewiewer.setEnabled(false);
				}
				else
				{
					rewiewer.setEnabled(true);
					/*DOM.setElementPropertyBoolean(rewiewer.getElement(), "disabled", false);*/
				}
			}
		});
		
		answerTextArea.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				digitCount();
			}
		});
		digitCount();
		
		this.addStyleName("mainAnswerDialogPanel");
	}
	
	private void digitCount() {
		
		if(QuestionTypes.Textual.equals(question.getQuestionType().getQuestionType()) || QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType())) {
			int currentCount = answerTextArea.getText().length();
			final Style style = digitCurrent.getStyle();
			String color = style.getColor();
			if(currentCount < answerTextMinDiff || currentCount > answerTextMaxDiff ) {
				if("red".equalsIgnoreCase(color) == false) {
					style.clearColor();
					style.setColor("red");
				}
			}else{
				if("green".equalsIgnoreCase(color) == false) {
					style.clearColor();
					style.setColor("green");
				}
			}
			
			digitMin.setInnerHTML(String.valueOf(answerTextMinDiff));
			digitMax.setInnerHTML(String.valueOf(answerTextMaxDiff));
			digitCurrent.setInnerHTML(String.valueOf(currentCount));
	
		}else {
			Style style = digitCount.getStyle();
			String display = style.getDisplay();
			if("none".equalsIgnoreCase(display) == false) {
				style.setDisplay(Display.NONE);
			}
		}
	}

	private void addForShowInImage() {

		Log.info("Question id :" + question.getId());
		
		//validity.setValue(Validity.Wahr);
		
		Long currentAnswerId = answer != null ? answer.getId() : null;
		delegate.findAllAnswersPoints(question.getId(),currentAnswerId,new Function<List<String>, Void>(){

			@Override
			public Void apply(List<String> polygons) {
				
				List<PolygonPath> polygonPaths = PolygonPath.getPolygonPaths(polygons);
				
				/*if(question != null && question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) && question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null) {
					imagePolygonViewer = new ImagePolygonViewer(question.getPicturePath(), question.getQuestionType().getImageWidth(), question.getQuestionType().getImageHeight(),polygonPaths, true);
					viewContainer.add(imagePolygonViewer);
				}*/
				
				if(question != null && question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) && question.getQuestionResources() != null && question.getQuestionResources().isEmpty() == false /*&& question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null*/) {
					final QuestionResourceProxy questionResourceProxy = Lists.newArrayList(question.getQuestionResources()).get(0);
					final Integer imageWidth = questionResourceProxy.getImageWidth();
					final Integer imageHeight = questionResourceProxy.getImageHeight();
					final String path = questionResourceProxy.getPath();
					imagePolygonViewer = new ImagePolygonViewer(path, imageWidth, imageHeight,polygonPaths, true);
					
					if(answer != null && answer.getPoints() != null) {
						imagePolygonViewer.setCurrentPolygon(PolygonPath.getPolygonPath(answer.getPoints()));
					}
					viewContainer.add(imagePolygonViewer);
				}
				
				return null;
			}
			
		});
	}

	private void addForImageKey() {

		Log.info("Question id :" + question.getId());
		Long currentAnswerId = answer != null ? answer.getId() : null;
		delegate.findAllAnswersPoints(question.getId(),currentAnswerId,new Function<List<String>, Void>(){

			@Override
			public Void apply(List<String> points) {
				
				final List<Point> rectanglePoints = Point.getPoints(points);
				
				if(question != null && question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) && question.getQuestionResources() != null && question.getQuestionResources().isEmpty() == false  /*&& question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null*/) {
					
					final QuestionResourceProxy questionResourceProxy = Lists.newArrayList(question.getQuestionResources()).get(0);
					final Integer imageWidth = questionResourceProxy.getImageWidth();
					final Integer imageHeight = questionResourceProxy.getImageHeight();
					final String path = questionResourceProxy.getPath();
					
					if (imageWidth != null &&imageHeight != null)
					{
						imageRectangleViewer = new ImageRectangleViewer(path, imageWidth, imageHeight, rectanglePoints, true);
						if(answer != null && answer.getPoints() != null) {
							imageRectangleViewer.setCurrentPoint(Point.getPoint(answer.getPoints()));
						}
						viewContainer.add(imageRectangleViewer);
					}
					else
					{
						ClientUtility.getImageWidthHeight(path, new ImageWidthHeight() {
							
							@Override
							public void apply(Integer width, Integer height) {
								imageRectangleViewer = new ImageRectangleViewer(path, width, height, rectanglePoints, true);
								if(answer != null && answer.getPoints() != null) {
									imageRectangleViewer.setCurrentPoint(Point.getPoint(answer.getPoints()));
								}
								viewContainer.add(imageRectangleViewer);
							}
						});
					}
					
					/*imageRectangleViewer = new ImageRectangleViewer(question.getPicturePath(), question.getQuestionType().getImageWidth(), question.getQuestionType().getImageHeight(),rectanglePoints, true);
					viewContainer.add(imageRectangleViewer);*/
				}
				
				validity.addValueChangeHandler(new ValueChangeHandler<Validity>() {
					
					@Override
					public void onValueChange(ValueChangeEvent<Validity> event) {
						Validity value = event.getValue();
						
						if(Validity.Wahr.equals(value) == true) {
							viewContainer.setVisible(true);	
						}else {
							viewContainer.setVisible(false);
						}
						
					}
				});
				
				if(Validity.Wahr.equals(validity.getValue()) == true) {
					viewContainer.setVisible(true);
				}else {
					viewContainer.setVisible(false);
				}
				return null;
			}
		});
	}
	
	private void addForMCQ() {
		lblUploadText.setVisible(true);
		lblUploadText.setStyleName("label");
		ResourceUpload upload = null;
		Map<MultimediaType,String> paths = Maps.newHashMap();
		
		switch (question.getQuestionType().getMultimediaType()) {
		case Image:
		{
			ArrayList<String> allowedExt = Lists.newArrayList();
			allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
			paths.put(MultimediaType.Image, SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
			upload = new ResourceUpload(allowedExt,paths/*, eventBus*/);
			upload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(final ResourceUploadEvent event) {
					if(event.isResourceUploaded()) {
						final String url = new String(GWT.getHostPageBaseURL() + event.getFilePath());
						final QuestionTypeProxy questionTypeProxy = question.getQuestionType();
						if(questionTypeProxy != null /*&& questionTypeProxy.getImageWidth() != null && questionTypeProxy.getImageHeight() != null*/) {
						
							Function<Boolean, Void> function = new Function<Boolean, Void>() {
								
								@Override
								public Void apply(Boolean flag) {
							
									if(flag != null && flag == true) {
										Log.info("image Path : " + url);
										if(simpleImageViewer != null && simpleImageViewer.getURL() != null && simpleImageViewer.getURL().length() > 0) {
											// delete old files
											//Log.info("Delete old uploaded file");
											//delegate.deleteUploadedFiles(Sets.newHashSet(simpleImageViewer.getURL().replace(GWT.getHostPageBaseURL(), "")));
										}
										simpleImageViewer = new SimpleImageViewer(url);
										viewContainer.clear();
										viewContainer.add(simpleImageViewer);
									} else {
										ConfirmationDialogBox.showOkDialogBox(constants.error(), bmeMessages.imageSizeError(questionTypeProxy.getImageWidth(), questionTypeProxy.getImageHeight()));
										//delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
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
						}
					}					
				}
			});
			
			if(answer != null && answer.getMediaPath() != null) {
				String url = GWT.getHostPageBaseURL() + answer.getMediaPath();
				simpleImageViewer = new SimpleImageViewer(url);
				viewContainer.clear();
				viewContainer.add(simpleImageViewer);
			}
			break;
		}	
		case Sound:
		{	
			ArrayList<String> allowedExt = Lists.newArrayList();
			allowedExt.addAll(Arrays.asList(SharedConstant.SOUND_EXTENSIONS));
			paths.put(MultimediaType.Sound, SharedConstant.UPLOAD_MEDIA_SOUND_PATH);
			
			upload = new ResourceUpload(allowedExt,paths/*, this.eventBus*/);
			upload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(ResourceUploadEvent event) {
					if(event.isResourceUploaded()) {
						if(event.getSoundMediaSize() != null && question != null && question.getQuestionType() != null && question.getQuestionType().getMaxBytes() != null) {
							
							if(event.getSoundMediaSize() <= question.getQuestionType().getMaxBytes()) {
								if(audioViewer != null && audioViewer.getURL() != null && audioViewer.getURL().length() > 0) {
									// delete old files
									//Log.info("Delete old uploaded file");
									//delegate.deleteUploadedFiles(Sets.newHashSet(audioViewer.getURL().replace(GWT.getHostPageBaseURL(), "")));
								}
								audioViewer = new AudioViewer(event.getFilePath());
								viewContainer.clear();
								viewContainer.add(audioViewer);
							}else {
								ConfirmationDialogBox.showOkDialogBox(constants.error(), bmeMessages.mediaErrorMsg(question.getQuestionType().getMaxBytes() / 1024));
								//delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
							}
						}else {
							Log.error("Error in MCQ question.");
							//delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
						}
					}
				}
			});

			if(answer != null && answer.getMediaPath() != null) {
				audioViewer = new AudioViewer(answer.getMediaPath());
				viewContainer.clear();
				viewContainer.add(audioViewer);
			}
			break;
		}
		case Video:
		{	
			ArrayList<String> allowedExt = Lists.newArrayList();
			allowedExt.addAll(Arrays.asList(SharedConstant.VIDEO_EXTENSIONS));
			paths.put(MultimediaType.Video, SharedConstant.UPLOAD_MEDIA_VIDEO_PATH);
			
			upload = new ResourceUpload(allowedExt,paths/*, this.eventBus*/);
			
			upload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(ResourceUploadEvent event) {
					if(event.isResourceUploaded()) {
						if(event.getVideoMediaSize() != null && question != null && question.getQuestionType() != null && question.getQuestionType().getMaxBytes() != null) {
							
							if(event.getVideoMediaSize() <= question.getQuestionType().getMaxBytes()) {
								if(videoViewer != null && videoViewer.getURL() != null && videoViewer.getURL().length() > 0) {
									// delete old files
									//Log.info("Delete old uploaded file");
									//delegate.deleteUploadedFiles(Sets.newHashSet(videoViewer.getURL().replace(GWT.getHostPageBaseURL(), "")));
								}
								videoViewer = new VideoViewer(event.getFilePath());
								viewContainer.clear();
								viewContainer.add(videoViewer);
							}else {
								ConfirmationDialogBox.showOkDialogBox(constants.error(), bmeMessages.mediaErrorMsg(question.getQuestionType().getMaxBytes()/1024));
								//delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
							}
						}else {
							Log.error("Error in MCQ question.");
							//delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
						}
					}
				}
			});
			
			if(answer != null && answer.getMediaPath() != null) {
				videoViewer = new VideoViewer(answer.getMediaPath());
				viewContainer.clear();
				viewContainer.add(videoViewer);
			}
			
			break;
		}
		default:
			break;
		}
		
		if(upload != null) {
			
			lblUploadText.setText(ClientUtility.getUploadLabel(paths.keySet()));
			uploadContainer.clear();
			uploadContainer.add(upload);
		}
	}
	@Override
	public void setRichPanelHTML(String html) {
		// Log.error(html);
		answerTextArea.setHTML(html);
	}

	/*@Override
	public String getRichtTextHTML() {
		// Log.info(questionTextArea.getHTML());
		// Log.info(questionTextArea.getText());
		return answerTextArea.getHTML();
		// return new String("<b>hallo</b>");
	}*/
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	@Override
	public void display(QuestionTypes types) {
		
		// for addtional keyword
		if(question != null && question.getQuestionType() != null && question.getQuestionType().getQuestionType() != null) {
			
			if(QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) || QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType())) {
				lblAdditionalKeyword.setVisible(true);
				lblAdditionalKeyword.setStyleName("label");
				btnAdditionalKeyword.setVisible(true);
				txtAdditionalKeyword.setVisible(true);
			}
			
			if(QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType()) == true) {
				lblSequenceNumber.setVisible(true);
				lblSequenceNumber.setStyleName("label");
				txtSequenceNumber.setVisible(true);
			}
		}
		
		//image viewer
		if(question != null && question.getQuestionType() != null && question.getQuestionType().getQuestionType() != null) {
			
			uploadContainer.clear();
			viewContainer.clear();
			
			switch (question.getQuestionType().getQuestionType()) {
			case ShowInImage:
			{
				addForShowInImage();
				break;
			}
			case Imgkey:
			{
				addForImageKey();
				break;
			}
			case MCQ:
			{
				if(question.getQuestionType().getMultimediaType() != null) {
					addForMCQ();
				}
				break;
			}
			
			default:
				Log.info("check for media");
				break;
			}
	
		}
		center();
		show();
		
		if(QuestionTypes.MCQ.equals(types)) {
			Document.get().getElementById("answerDialogText").getStyle().setDisplay(Display.NONE);
			/*answerTextArea.setVisible(false);
			toolbar.setVisible(false);*/
		}
		
		if (QuestionTypes.ShowInImage.equals(types)){
			validity.setValue(Validity.Wahr);
			Document.get().getElementById("validity").getStyle().setDisplay(Display.NONE);
		}

	}

	
	/*@UiField(provided = true)
	ValueListBox<PersonProxy> rewiewer = new ValueListBox<PersonProxy>(
			PersonProxyRenderer.instance(),
			new EntityProxyKeyProvider<PersonProxy>());
*/
	@UiField(provided = true)
	ValueListBox<Validity> validity = new ValueListBox<Validity>(new AbstractRenderer<medizin.shared.Validity>() {

																public String render(medizin.shared.Validity obj) {
																	return obj == null ? "" : String.valueOf(obj);
																}
															});


	/*@Override
	public ValueListBox<Validity> getValidity() {
		return validity;
	}*/

	/*@Override
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getReviewerSuggestBox() {
		return rewiewer;
	}*/
	
	@Override
	public void setAutherPickerValues(Collection<PersonProxy> values, PersonProxy logedUser,  boolean isAdminOrInstitutionalAdmin) {	
		
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
		
		if(isAdminOrInstitutionalAdmin == false) {
			author.setSelected(logedUser);
			author.setEnabled(false);
		}else {
			author.setSelected(question.getAutor());
		}
		author.setWidth(150);
		rewiewer.setWidth(150);
	}
	
	@Override
	public void setRewiewerPickerValues(List<PersonProxy> values) {
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) rewiewer.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);
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
		
		PersonProxy lastSelectedReviwer = ClientUtility.getAnswerReviwerPersonProxyFromCookie(values);
		if (lastSelectedReviwer != null)
			rewiewer.setSelected(lastSelectedReviwer);
		
		if(question.getRewiewer() != null) {
			rewiewer.setSelected(question.getRewiewer());
		}
	}

	@Override
	public void setValidityPickerValues(Collection<Validity> values) {
		validity.setAcceptableValues(values);
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		if(validationOfFields()) {
		
			String points = null; 
			String mediaPath = null;
			String additionalKeywords = null; 
			Integer sequenceNumber = null;
			
			switch (question.getQuestionType().getQuestionType()) {
			case ShowInImage:
			{
				points = imagePolygonViewer.getPoints();
				break;
			}
			case Imgkey: 
			{
				additionalKeywords = txtAdditionalKeyword.getText();
				if(Validity.Wahr.equals(validity.getValue()) == true) {
					points = imageRectangleViewer.getPoint();	
				}
				break;
			}
			case MCQ:
			{
				switch (question.getQuestionType().getMultimediaType()) {
				case Image:
				{
					mediaPath = simpleImageViewer.getRelativeURL();
					break;
				}
				case Sound:
				{
					mediaPath = audioViewer.getRelativeURL();
					break;
				}
				case Video:
				{
					mediaPath = videoViewer.getRelativeURL();
					break;
				}
				}
				break;
			}
			case Sort:
			{
				additionalKeywords = txtAdditionalKeyword.getText();
				sequenceNumber = Integer.parseInt(txtSequenceNumber.getValue(), 10);
				break;
			}
			default:
				break;
			}
			
			if(QuestionTypes.Textual.equals(question.getQuestionType().getQuestionType()) || QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType())) {
				if(question.getQuestionType().getAnswerLength() != null  && answerTextMaxDiff != null && answerTextMinDiff != null) {
					if(answerTextArea.getText().length() < answerTextMinDiff || answerTextArea.getText().length() > answerTextMaxDiff) {
						final String fpoints = points; 
						final String fmediaPath = mediaPath;
						final String fadditionalKeywords = additionalKeywords; 
						final Integer fsequenceNumber = sequenceNumber;
						ConfirmationDialogBox.showYesNoDialogBox(constants.pleaseEnterWarning(), bmeMessages.answerTextMinMaxContinueAnyWay(answerTextMinDiff, answerTextMaxDiff), new ConfirmDialogBoxYesNoButtonEventHandler() {
							
							@Override
							public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
								saveAnswer(fpoints, fmediaPath, fadditionalKeywords, fsequenceNumber);
							}
							@Override
							public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {}
						});
						
						return; //returns from method
					} 
				}	
			}
			
			saveAnswer(points, mediaPath, additionalKeywords, sequenceNumber);
		}else {
			Log.info("Validation failed");
		}
		//delegate.addAnswerClicked();
		// hide();
	}
	
	private void saveAnswer(final String points, final String mediaPath, final String additionalKeywords, final Integer sequenceNumber) {
		delegate.saveAnswerProxy(answer, answerTextArea.getHTML(), author.getSelected(), rewiewer.getSelected(), submitToReviewComitee.getValue(), (comment.getText().isEmpty() ? " " : comment.getText()),validity.getValue(),points,mediaPath,additionalKeywords,sequenceNumber, forcedActive.getValue(), new Function<AnswerProxy,Void>() {

			@Override
			public Void apply(AnswerProxy input) {
				hide();
				return null;
			}
		});
	}


	private boolean validationOfFields() {
		
		ArrayList<String> messages = Lists.newArrayList();
		boolean flag = true;
		
		answerTextArea.removeStyleName("higlight_onViolation");
		rewiewer.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		author.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		submitToReviewComitee.removeStyleName("higlight_onViolation");
	//	comment.removeStyleName("higlight_onViolation");
		validity.removeStyleName("higlight_onViolation");
		if(imagePolygonViewer != null) imagePolygonViewer.removeStyleName("higlight_onViolation");
		if(imageRectangleViewer != null) imageRectangleViewer.removeStyleName("higlight_onViolation");
		if(simpleImageViewer != null) simpleImageViewer.removeStyleName("higlight_onViolation");
		if(audioViewer != null) audioViewer.removeStyleName("higlight_onViolation");
		if(videoViewer != null) videoViewer.removeStyleName("higlight_onViolation");
	
		if(submitToReviewComitee.getValue()){
			rewiewer.setSelected(null);
		}else if(rewiewer.getSelected() != null){
			submitToReviewComitee.setValue(false);
		}else {
			flag = false;
			messages.add(constants.selectReviewerOrComitee());
			rewiewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			submitToReviewComitee.addStyleName("higlight_onViolation");
		}
		
		if(author.getSelected() != null && rewiewer.getSelected() != null && author.getSelected().getId().equals(rewiewer.getSelected().getId()) == true) {
			flag = false;
			messages.add(constants.authorReviewerMayNotBeSame());
			author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			rewiewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
		}
		
		if(question.getQuestionType() != null && (QuestionTypes.MCQ.equals(question.getQuestionType().getQuestionType()) == false && QuestionTypes.LongText.equals(question.getQuestionType().getQuestionType()) == false)) {
			if(answerTextArea.getText() == null || answerTextArea.getText().length() <= 0) {
				flag = false;
				messages.add(constants.answerTextErrorMessage());
				answerTextArea.addStyleName("higlight_onViolation");
			}else if(question.getQuestionType().getAnswerLength() != null && answerTextArea.getText().length() >= question.getQuestionType().getAnswerLength()) {
				flag = false;
				messages.add(constants.answerTextMaxLength());
				answerTextArea.addStyleName("higlight_onViolation");
			}
			
			/*if(QuestionTypes.Textual.equals(question.getQuestionType().getQuestionType()) || QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType())) {
				if(question.getQuestionType().getAnswerLength() != null  && answerTextMaxDiff != null && answerTextMinDiff != null) {
					if(answerTextArea.getText().length() < answerTextMinDiff || answerTextArea.getText().length() > answerTextMaxDiff) {
						flag = false;
						messages.add(bmeMessages.answerTextMinMax(answerTextMinDiff, answerTextMaxDiff));
						answerTextArea.addStyleName("higlight_onViolation");
					}
				}	
			}*/
		}
		
		if(question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) == true ) {
			Log.info("IN ShowInImage Question type");
			if(imagePolygonViewer == null || imagePolygonViewer.isValidPolygon() == false) {
				flag = false;
				Log.error("Polygon is not property added. Try again");
				messages.add(constants.polygonErrorMessage());
				imagePolygonViewer.addStyleName("higlight_onViolation");
			}			
		}else if(question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) == true && validity.getValue() != null && Validity.Wahr.equals(validity.getValue())) {
			Log.info("IN Imgkey Question type");
			if(imageRectangleViewer == null || imageRectangleViewer.getPoint() == null) {
				flag = false;
				messages.add(constants.rectangleErrorMessage());
				imageRectangleViewer.removeStyleName("higlight_onViolation");
				Log.error("Rectangle is not property added. Try again");
			}
		}else if(question.getQuestionType() != null && QuestionTypes.MCQ.equals(question.getQuestionType().getQuestionType()) == true) {
			Log.info("IN MCQ Question type");
			if(question != null && question.getQuestionType() != null && question.getQuestionType().getMultimediaType() != null) {
			
				switch (question.getQuestionType().getMultimediaType()) {
				case Image:
				{
					if(simpleImageViewer == null || simpleImageViewer.getURL() == null || simpleImageViewer.getURL().length() <= 0) {
						flag = false;
						messages.add(constants.imageViewerError());
						Log.error("Error in imageview");
					}
					break;
				}
				case Sound:
				{
					if(audioViewer == null || audioViewer.getURL() == null || audioViewer.getURL().length() <= 0) {
						flag = false;
						messages.add(constants.audioViewerError());
						Log.error("Error in audioview.");
					}
					break;
				}
				case Video:
				{	
					if(videoViewer == null || videoViewer.getURL() == null || videoViewer.getURL().length() <= 0) {
						flag = false;
						messages.add(constants.videoViewerError());
						Log.error("Error in videoViewer. Try again");
					}
					break;
				}
				default:
				{
					ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.unknownMultimediaType());
					Log.error("Error in MultimediaType. Try again");
					//return false;
				}
				}
			}	
		}else if(question.getQuestionType() != null && QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType()) == true) {
			txtSequenceNumber.removeStyleName("higlight_onViolation");
			if(ClientUtility.isNumber(txtSequenceNumber.getValue()) == false) {
				messages.add(constants.sequenceNumberError());
				flag = false;
				txtSequenceNumber.addStyleName("higlight_onViolation");
				Log.info("squence number is not valid number");
			}
		}
		
		if(author.getSelected() == null) {
			flag = false;
			messages.add(constants.authorMayNotBeNull());
			author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
		}
		
		/*if(comment.getText() == null || comment.getText().isEmpty()) {
			flag = false;
			messages.add(constants.commentMayNotBeNull());
			comment.addStyleName("higlight_onViolation");
		}*/
		
		if(validity.getValue() == null) {
			flag = false;
			messages.add(constants.validityMayNotBeNull());
			validity.addStyleName("higlight_onViolation");
		}
		if(flag == false) {
			ReceiverDialog.showMessageDialog(constants.pleaseEnterWarning(),messages);
		}
		return flag;
	}

	@Override
	public void close() {
		hide();
	}

	@Override
	public void setValues(AnswerProxy answer) {
		this.answer = answer;
		answerTextArea.setHTML(answer.getAnswerText());
		txtAdditionalKeyword.setText(answer.getAdditionalKeywords());
		txtSequenceNumber.setText(answer.getSequenceNumber() != null ? answer.getSequenceNumber().toString() : null);
		author.setSelected(answer.getAutor());
		rewiewer.setSelected(answer.getRewiewer());
		validity.setValue(answer.getValidity());
		submitToReviewComitee.setValue(answer.getSubmitToReviewComitee());
		comment.setText(answer.getComment() != null?answer.getComment() : null);
		
	}

	@Override
	public void setMaxDifferenceBetween(long max, long min) {
		this.answerTextMaxDiff = max;
		this.answerTextMinDiff = min;
		digitCount();
	}

	/*@Override
	public ImagePolygonViewer getImagePolygonViewer() {
		return imagePolygonViewer;
	}

	@Override
	public ImageRectangleViewer getImageRectangleViewer() {
		return this.imageRectangleViewer;
	}
	@Override
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getAutherSuggestBox() {
		return auther;
	}

	@Override
	public SimpleImageViewer getSimpleImageViewer() {
		return this.simpleImageViewer;
	}
	
	@Override
	public AudioViewer getAudioViewer() {
		return this.audioViewer;
	}

	@Override
	public VideoViewer getVideoViewer() {
		return this.videoViewer;
	}

	@Override
	public RichTextArea getRichtTextArea() {
		return answerTextArea;
	}

	@Override
	public TextArea getAdditionalKeywords() {
		return txtAdditionalKeyword;
	}

	@Override
	public TextBox getSequenceNumber() {
		return txtSequenceNumber;
	}*/
	
	@Override
	public ApplicationLoadingView getLoadingPopup() {
			return loadingPopup;
		}

}

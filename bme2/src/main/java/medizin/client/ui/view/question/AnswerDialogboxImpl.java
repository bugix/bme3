package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.shared.Validity;
import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
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
import medizin.client.util.Point;
import medizin.client.util.PolygonPath;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.utils.SharedConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnswerDialogboxImpl extends DialogBox implements AnswerDialogbox/*,Editor<AnswerProxy>*/{

	private static EventAccessDialogboxImplUiBinder uiBinder = GWT
			.create(EventAccessDialogboxImplUiBinder.class);

	interface EventAccessDialogboxImplUiBinder extends
			UiBinder<Widget, AnswerDialogboxImpl> {
	}

	private Presenter presenter;

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
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> auther;
	
	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> rewiewer;
	
	@UiField
	Label lblAdditionalKeyword;
	
	@UiField
	Button btnAdditionalKeyword;
	
	@UiField
	TextArea txtAdditionalKeyword;
	
	public BmeConstants constants = GWT.create(BmeConstants.class);

	private ImagePolygonViewer imagePolygonViewer;
	private ImageRectangleViewer imageRectangleViewer;
	private SimpleImageViewer simpleImageViewer;
	private AudioViewer audioViewer;
	private VideoViewer videoViewer;

	@UiHandler("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
		hide();

	}

	@Override
	public CheckBox getSubmitToReviewerComitee() {
		return submitToReviewComitee;
	}

	@Override
	public TextArea getComment() {
		return comment;
	}

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
	@UiField
	public DivElement descriptionValue;
	
    private final QuestionProxy question;

	private final EventBus eventBus;
		
	public AnswerDialogboxImpl(QuestionProxy questionProxy, EventBus eventBus, Map<String, Widget> reciverMap) {
		
		this.question = questionProxy;
		this.eventBus = eventBus;
		answerTextArea = new RichTextArea();
		answerTextArea.setSize("100%", "14em");
		toolbar = new RichTextToolbar(answerTextArea);
		toolbar.setWidth("100%");
		
		setWidget(uiBinder.createAndBindUi(this));
		
		reciverMap.put("answerText",answerTextArea);
		reciverMap.put("autor", auther.getTextField().advancedTextBox);
		reciverMap.put("rewiewer", rewiewer.getTextField().advancedTextBox);
		//reciverMap.put("rewiewer", rewiewer);
		reciverMap.put("validity", validity);
		reciverMap.put("submitToReviewComitee", submitToReviewComitee);
		reciverMap.put("comment", comment);
		reciverMap.put("additionalKeywords", txtAdditionalKeyword);
		/*reciverMap.put("mediaPath", RunTimeGenerated);*/

		
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setTitle(constants.answerDialogBoxTitle());
		setText(constants.answerDialogBoxTitle());
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

		save.setText(constants.save());
		closeButton.setText(constants.cancel());
		
	}
	
	private void addForShowInImage() {

		Log.info("Question id :" + question.getId());
		delegate.findAllAnswersPoints(question.getId(),new Function<List<String>, Void>(){

			@Override
			public Void apply(List<String> polygons) {
				
				List<PolygonPath> polygonPaths = PolygonPath.getPolygonPaths(polygons);
				
				if(question != null && question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) && question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null) {
					imagePolygonViewer = new ImagePolygonViewer(question.getPicturePath(), question.getQuestionType().getImageWidth(), question.getQuestionType().getImageHeight(),polygonPaths);
					viewContainer.add(imagePolygonViewer);
				}
				
				return null;
			}
			
		});
	}

	private void addForImageKey() {

		Log.info("Question id :" + question.getId());
		delegate.findAllAnswersPoints(question.getId(),new Function<List<String>, Void>(){

			@Override
			public Void apply(List<String> points) {
				
				List<Point> rectanglePoints = Point.getPoints(points);
				
				if(question != null && question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) && question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null) {
					imageRectangleViewer = new ImageRectangleViewer(question.getPicturePath(), question.getQuestionType().getImageWidth(), question.getQuestionType().getImageHeight(),rectanglePoints);
					viewContainer.add(imageRectangleViewer);
				}
				
				return null;
			}
		});
	}
	
	private void addForMCQ() {
		lblUploadText.setVisible(true);
		lblUploadText.setStyleName("label");
		ResourceUpload upload = null;
		
		switch (question.getQuestionType().getMultimediaType()) {
		case Image:
		{
			ArrayList<String> allowedExt = Lists.newArrayList();
			Map<MultimediaType,String> paths = Maps.newHashMap();
			allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
			paths.put(MultimediaType.Image, SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
			upload = new ResourceUpload(allowedExt,paths, eventBus);
			upload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(ResourceUploadEvent event) {
					if(event.isResourceUploaded()) {
						simpleImageViewer = new SimpleImageViewer(event.getFilePath());
						viewContainer.clear();
						viewContainer.add(simpleImageViewer);
					}					
				}
			});
			
			break;
		}	
		case Sound:
		{	
			ArrayList<String> allowedExt = Lists.newArrayList();
			Map<MultimediaType,String> paths = Maps.newHashMap();
			allowedExt.addAll(Arrays.asList(SharedConstant.SOUND_EXTENSIONS));
			paths.put(MultimediaType.Sound, SharedConstant.UPLOAD_MEDIA_SOUND_PATH);
			
			upload = new ResourceUpload(allowedExt,paths, this.eventBus);
			upload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(ResourceUploadEvent event) {
					if(event.isResourceUploaded()) {
						if(event.getSoundMediaSize() != null && question != null && question.getQuestionType() != null && question.getQuestionType().getMaxBytes() != null) {
							
							if(event.getSoundMediaSize() <= question.getQuestionType().getMaxBytes()) {
								audioViewer = new AudioViewer(event.getFilePath());
								viewContainer.clear();
								viewContainer.add(audioViewer);
							}else {
								ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.mediaErrorMsg().replace("(0)", question.getQuestionType().getMaxBytes().toString()));
								delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
							}
						}else {
							Log.error("Error in MCQ question.");
							delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
						}
					}
				}
			});

			break;
		}
		case Video:
		{	
			ArrayList<String> allowedExt = Lists.newArrayList();
			Map<MultimediaType,String> paths = Maps.newHashMap();
			allowedExt.addAll(Arrays.asList(SharedConstant.VIDEO_EXTENSIONS));
			paths.put(MultimediaType.Video, SharedConstant.UPLOAD_MEDIA_VIDEO_PATH);
			
			upload = new ResourceUpload(allowedExt,paths, this.eventBus);
			
			upload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(ResourceUploadEvent event) {
					if(event.isResourceUploaded()) {
						if(event.getVideoMediaSize() != null && question != null && question.getQuestionType() != null && question.getQuestionType().getMaxBytes() != null) {
							
							if(event.getVideoMediaSize() <= question.getQuestionType().getMaxBytes()) {
								videoViewer = new VideoViewer(event.getFilePath());
								viewContainer.clear();
								viewContainer.add(videoViewer);
							}else {
								ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.mediaErrorMsg().replace("(0)", question.getQuestionType().getMaxBytes().toString()));
								delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
							}
						}else {
							Log.error("Error in MCQ question.");
							delegate.deleteUploadedFiles(Sets.newHashSet(event.getFilePath()));
						}
					}
				}
			});
			
			
			break;
		}
		default:
			break;
		}
		
		if(upload != null) {
			uploadContainer.clear();
			uploadContainer.add(upload);
		}
	}
	@Override
	public void setRichPanelHTML(String html) {
		// Log.error(html);
		answerTextArea.setHTML(html);
	}

	@Override
	public String getRichtTextHTML() {
		// Log.info(questionTextArea.getHTML());
		// Log.info(questionTextArea.getText());
		return answerTextArea.getHTML();
		// return new String("<b>hallo</b>");
	}
	
	@UiField(provided = true)
	public RichTextToolbar toolbar;
	
	@UiField(provided = true)
	RichTextArea answerTextArea;

	private Delegate delegate;

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	@Override
	public void display(QuestionTypes types) {
		
		if(QuestionTypes.MCQ.equals(types)) {
			answerTextArea.setVisible(false);
			toolbar.setVisible(false);
		}
		
		// for addtional keyword
		if(question != null && question.getQuestionType() != null && question.getQuestionType().getQuestionType() != null) {
			
			if(QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) || QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType())) {
				lblAdditionalKeyword.setVisible(true);
				lblAdditionalKeyword.setStyleName("label");
				btnAdditionalKeyword.setVisible(true);
				txtAdditionalKeyword.setVisible(true);
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

	}

	
	/*@UiField(provided = true)
	ValueListBox<PersonProxy> rewiewer = new ValueListBox<PersonProxy>(
			PersonProxyRenderer.instance(),
			new EntityProxyKeyProvider<PersonProxy>());
*/
	@UiField(provided = true)
	ValueListBox<Validity> validity = new ValueListBox<Validity>(new AbstractRenderer<medizin.client.shared.Validity>() {

																public String render(medizin.client.shared.Validity obj) {
																	return obj == null ? "" : String.valueOf(obj);
																}
															});

	@Override
	public ValueListBox<Validity> getValidity() {
		return validity;
	}

	@Override
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getReviewerSuggestBox() {
		return rewiewer;
	}
	
	@Override
	public void setAutherPickerValues(Collection<PersonProxy> values, PersonProxy logedUser) {	
		
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) auther.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		 /* Collection<MyObjectType> myCollection = ...;
		 List<MyObjectType> list = new ArrayList<MyObjectType>(myCollection);*/
		auther.setSuggestOracle(suggestOracle1);
		auther.setRenderer(new AbstractRenderer<PersonProxy>() {
	
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
		if(logedUser.getIsAdmin() == false) {
			
			auther.setSelected(logedUser);
			//answerDialogbox.getAutherSuggestBox().setSelected(userLoggedIn);
			auther.setEnabled(false);
		}
		auther.setWidth(150);
		rewiewer.setWidth(150);
	}
	
	@Override
	public void setRewiewerPickerValues(Collection<PersonProxy> values) {
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
	}

	@Override
	public void setValidityPickerValues(Collection<Validity> values) {
		validity.setAcceptableValues(values);
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		delegate.addAnswerClicked();
		// hide();
	}

	@Override
	public void close() {
		hide();
	}

	@Override
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
}

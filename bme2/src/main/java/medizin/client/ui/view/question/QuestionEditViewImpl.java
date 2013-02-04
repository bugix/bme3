package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.view.roo.McSetEditor;
import medizin.client.ui.view.roo.QuestionTypeProxyRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.resource.dndview.ResourceView;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.client.ui.widget.resource.upload.ResourceUpload;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEvent;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEventHandler;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.utils.SharedConstant;
import medizin.shared.utils.SharedUtility;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;

public class QuestionEditViewImpl extends Composite implements QuestionEditView/*
																				 * ,
																				 * Editor
																				 * <
																				 * QuestionProxy
																				 * >
																				 */{

	private static QuestionEditViewImplUiBinder uiBinder = GWT
			.create(QuestionEditViewImplUiBinder.class);

	interface QuestionEditViewImplUiBinder extends
			UiBinder<Widget, QuestionEditViewImpl> {
	}

	/*
	 * interface EditorDriver extends RequestFactoryEditorDriver<QuestionProxy,
	 * QuestionEditViewImpl> {} // private final EditorDriver editorDriver =
	 * GWT.create(EditorDriver.class);
	 * 
	 * @Override public
	 * RequestFactoryEditorDriver<QuestionProxy,QuestionEditViewImpl>
	 * createEditorDriver() { RequestFactoryEditorDriver<QuestionProxy,
	 * QuestionEditViewImpl> driver = GWT.create(EditorDriver.class);
	 * driver.initialize(this); return driver; }
	 */

	private Presenter presenter;
	private Delegate delegate;
	private QuestionProxy proxy;
	private McAppRequestFactory requests;
	public BmeConstants constants = GWT.create(BmeConstants.class);

	@UiField
	public IconButton cancel;

	@UiField
	public IconButton save;

	@UiField
	TabPanel questionTypePanel;

	@UiField
	SpanElement title;

	@UiField
	public Label lblQuestionType;

	@UiField(provided = true)
	public ValueListBox<QuestionTypeProxy> questionType = new ValueListBox<QuestionTypeProxy>(
			QuestionTypeProxyRenderer.instance(),
			new EntityProxyKeyProvider<medizin.client.proxy.QuestionTypeProxy>());

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
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> auther;

	
	/*@UiField(provided = true)
	public ValueListBox<PersonProxy> autor = new ValueListBox<PersonProxy>(
			medizin.client.ui.view.roo.PersonProxyRenderer.instance(),
			new EntityProxyKeyProvider<medizin.client.proxy.PersonProxy>());
*/
	@UiField
	public Label lblReviewer;

	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> rewiewer;

	
	/*@UiField(provided = true)
	public ValueListBox<PersonProxy> rewiewer = new ValueListBox<PersonProxy>(
			medizin.client.ui.view.roo.PersonProxyRenderer.instance(),
			new EntityProxyKeyProvider<medizin.client.proxy.PersonProxy>());
*/
	@UiField
	public Label lblQuestionEvent;

	@UiField(provided = true)
	public ValueListBox<QuestionEventProxy> questEvent = new ValueListBox<QuestionEventProxy>(
			medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance(),
			new EntityProxyKeyProvider<medizin.client.proxy.QuestionEventProxy>());

	@UiField
	public Label lblQuestionComment;

	/*@UiField(provided = true)
	public RichTextToolbar commentToolbar;

	@UiField
	public DivElement commentValue;
	@UiField(provided = true)
	public RichTextArea questionComment;

	*/
	
	@UiField
	public TextArea questionComment;
	

	
	@UiField
	public Label lblMCS;

	@UiField 
	McSetEditor mcs;
	
	@UiField
	public VerticalPanel containPanel;
	
	@UiField
	Label lblUploadText;
	
	@UiField
	HTMLPanel uploaderContainer;
	
	@UiField
	HTMLPanel viewerContainer;
	
	//private ImageViewer viewer;
	private ResourceView viewer;
	
	private QuestionProxy question = null;
	// @UiField
	// DateBox dateAdded;
	//
	// @UiField
	// DateBox dateChanged;

	//
	// @UiField
	// RichTextArea questionText;

	/*
	 * @UiField RichTextArea questionTextArea;
	 */

	/*
	 * @UiField McSetEditor mcs;
	 */

	@UiHandler("cancel")
	void onCancel(ClickEvent event) {
		delegate.cancelClicked();
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		if (!edit) {
			delegate.saveClicked(false);
		} else {
			ConfirmQuestionChangesPopup confirm = new ConfirmQuestionChangesPopup(
					delegate);
		}

		// delegate.saveClicked(false);
	}

	// @UiField
	// SpanElement displayRenderer;

	/*
	 * @UiField SimplePanel toolbarPanel;
	 */

	public QuestionEditViewImpl() {

		questionTextArea = new RichTextArea();
		questionTextArea.setSize("100%", "14em");
		toolbar = new RichTextToolbar(questionTextArea);
		toolbar.setWidth("100%");

		/*questionComment = new RichTextArea();
		questionComment.setSize("100%", "14em");
		commentToolbar = new RichTextToolbar(questionComment);
		commentToolbar.setWidth("100%");
*/
		initWidget(uiBinder.createAndBindUi(this));
		questionTypePanel.selectTab(0);
		save.setText(constants.save());
		cancel.setText(constants.cancel());

		questionTypePanel.getTabBar().setTabText(0, constants.manageQuestion());
		lblQuestionShortName.setText(constants.questionShortName());
		lblQuestionType.setText(constants.questionType());
		lblQuestionText.setText(constants.questionText());
		lblAuther.setText(constants.auther());
		lblReviewer.setText(constants.reviewer());
		lblQuestionComment.setText(constants.comment());
		lblQuestionEvent.setText(constants.questionEvent());
		lblMCS.setText(constants.mcs());
		lblQuestionSubmitToReviewComitee.setText(constants.submitToReviewComitee());
		// RichTextToolbar toolbar=new RichTextToolbar(questionTextArea);
		// toolbarPanel.add(toolbar);
		
		questionType.addValueChangeHandler(new ValueChangeHandler<QuestionTypeProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<QuestionTypeProxy> event) {
				//Note: question is null in create mode 
				setResourceUploadAndResourceViewer(event.getValue(),question);
			}
		});

	}

	@Override
	public void setRichPanelHTML(String html) {
		Log.error(html);
		questionTextArea.setHTML(html);
	}

	@Override
	public void setValue(QuestionProxy question) {
		// questionTextArea.setHTML(html);
		questionShortName.setValue(question.getQuestionShortName()==null ?" ": question.getQuestionShortName());
		questionType.setValue(question.getQuestionType());
		questionTextArea.setHTML(question.getQuestionText() == null ? ""
				: question.getQuestionText());
		//autor.setValue(question.getAutor());
		auther.setSelected(question.getAutor());
		rewiewer.setSelected(question.getRewiewer());
		//rewiewer.setValue(question.getRewiewer());
		questEvent.setValue(question.getQuestEvent());
		questionComment.setValue(question.getComment() == null ? "" : question.getComment().getComment());
		/*questionComment.setHTML(question.getComment() == null ? "" : question
				.getComment().getComment());*/
		submitToReviewComitee.setValue(question.getSubmitToReviewComitee());
		mcs.setValue(question.getMcs());
		
		this.question = question;
		
		if(question != null) {
			
			setResourceUploadAndResourceViewer(question.getQuestionType(), question);
		}

	}

	@Override
	public String getRichtTextHTML() {
		// Log.info(questionTextArea.getHTML());
		// Log.info(questionTextArea.getText());
		return questionTextArea.getHTML();
		// return new String("<b>hallo</b>");
	}

	@Override
	public void setName(String helloName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	@Override
	public void setRewiewerPickerValues(Collection<PersonProxy> values) {
		//rewiewer.setAcceptableValues(values);
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) rewiewer.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		 /* Collection<MyObjectType> myCollection = ...;
		 List<MyObjectType> list = new ArrayList<MyObjectType>(myCollection);*/
		rewiewer.setSuggestOracle(suggestOracle1);
		rewiewer.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
				// TODO Auto-generated method stub
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
	}

	@Override
	public void setQuestEventPickerValues(Collection<QuestionEventProxy> values) {
		questEvent.setAcceptableValues(values);
	}

	@Override
	public void setAutorPickerValues(Collection<PersonProxy> values) {
		//autor.setAcceptableValues(values);
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) auther.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		 /* Collection<MyObjectType> myCollection = ...;
		 List<MyObjectType> list = new ArrayList<MyObjectType>(myCollection);*/
		auther.setSuggestOracle(suggestOracle1);
		auther.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
				// TODO Auto-generated method stub
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
		auther.setWidth(150);

	}

	@Override
	public void setQuestionTypePickerValues(Collection<QuestionTypeProxy> values) {
		questionType.setAcceptableValues(values);
	}

	@Override
	public void setMcsPickerValues(Collection<McProxy> values) {
		 mcs.setAcceptableValues(values);
	}

	/*
	 * @UiField Element editTitle;
	 * 
	 * @UiField Element createTitle;
	 */

	private boolean edit;
	private EventBus eventBus;

	@Override
	public void setEditTitle(boolean edit) {
		this.edit = edit;
		if (edit) {
			title.setInnerText("Edit");
			// questionTypePanel.getTabBar().setTabText(0, "Edit Question");
			// editTitle.getStyle().clearDisplay();
			// createTitle.getStyle().setDisplay(Display.NONE);
		} else {
			title.setInnerText("Create");
			// questionTypePanel.getTabBar().setTabText(0, "New Question");
			// editTitle.getStyle().setDisplay(Display.NONE);
			// createTitle.getStyle().clearDisplay();
		}

	}

	@Override
	public ValueListBox<QuestionTypeProxy> getQuestionType() {
		return questionType;
	}

	@Override
	public RichTextArea getQuestionTextArea() {
		return questionTextArea;
	}

	
	@Override
	public CheckBox getSubmitToReviewComitee() {
		return submitToReviewComitee;
		
	}
	
//	@Override
//	public ValueListBox<PersonProxy> getAuther() {
//		//return autor;
//		return null;
//	}
	
	@Override
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getAutherListBox() {
		// TODO Auto-generated method stub
		return auther;
	}

	
	@Override
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getReviewerListBox() {
		// TODO Auto-generated method stub
		return rewiewer;
	}

	@Override
	public TextBox getShortName()
	{
		return questionShortName;
	}

//	@Override
//	public ValueListBox<PersonProxy> getReviewer() {
//		//return rewiewer;
//		return null;
//	}

	@Override
	public ValueListBox<QuestionEventProxy> getQuestionEvent() {
		return questEvent;
	}

	@Override
	public TextArea getQuestionComment() {
		return questionComment;
	}
	
	@Override
	public McSetEditor getMCS() {
		return mcs;
	}
	
	private void setResourceUploadAndResourceViewer(
			QuestionTypeProxy questionType, QuestionProxy question) {	
	
		//remove extra part
		lblUploadText.setText("");
		uploaderContainer.clear();
		viewerContainer.clear();
		
		if(questionType != null &&  questionType.getQuestionType() != null ) {
		
			
			if(allowedQuestionTypeForResouce(questionType)) {

				lblUploadText.setText(constants.uploadResource());
				
				// added viewer
				List<QuestionResourceProxy> questionResources = new ArrayList<QuestionResourceProxy>();
				if(question != null && question.getQuestionResources() != null) {
					
					questionResources.addAll(question.getQuestionResources());
					Collections.sort(questionResources, new Comparator<QuestionResourceProxy>() {

						@Override
						public int compare(QuestionResourceProxy o1, QuestionResourceProxy o2) {
					
							return o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
						}
					});
				}
				viewer = new ResourceView(getQuestionResourceClient(questionResources),questionType.getQuestionType());
				
				// allowed extension
				ArrayList<String> allowedExt = new ArrayList<String>();
				
				if(QuestionTypes.Textual.equals(questionType.getQuestionType()) || QuestionTypes.Imgkey.equals(questionType.getQuestionType()) || QuestionTypes.Area.equals(questionType.getQuestionType()) || QuestionTypes.MCQ.equals(questionType.getQuestionType())) {
					allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
				}
				
				if(QuestionTypes.Textual.equals(questionType.getQuestionType())) {
					allowedExt.addAll(Arrays.asList(SharedConstant.VIDEO_EXTENSIONS));
					allowedExt.addAll(Arrays.asList(SharedConstant.SOUND_EXTENSIONS));
				}
				
				// added resourceUpload
				
				for (String string : allowedExt) {
					
					Log.info(string);
				}
				ResourceUpload resourceUpload = new ResourceUpload(allowedExt,eventBus);
				
				final QuestionTypeProxy tempQuestionTypeProxy = questionType; 
				resourceUpload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
					
					@Override
					public void onResourceUploaded(ResourceUploadEvent event) {
						String fileName = event.getFileName();
						
						if(event.isResourceUploaded() == true) {
							Log.info("fileName is " + fileName);
							
							MultimediaType type = SharedUtility.getFileMultimediaType(SharedUtility.getFileExtension(fileName));
							
							switch (type) {
							case Image:
							{
								// for image
								String url = new String(GWT.getHostPageBaseURL() + SharedConstant.UPLOAD_QUESTION_IMAGES_PATH + fileName);
								
								if(checkImageSize(url,tempQuestionTypeProxy)) {
								
									if (viewer != null) {
										Log.info(SharedConstant.UPLOAD_QUESTION_IMAGES_PATH + fileName);
										
										viewer.addImageUrl(SharedConstant.UPLOAD_QUESTION_IMAGES_PATH + fileName);
									}
								}else {
								
									Window.alert("Only Upload image of size" + tempQuestionTypeProxy.getImageWidth() + "*" + tempQuestionTypeProxy.getImageHeight());
									
									//deleteImage(url.replaceAll(GWT.getHostPageBaseURL(), ""));
								}
	
								break;
							}	
							case Sound:
							{	
								if (viewer != null) {
									Log.info(SharedConstant.UPLOAD_QUESTION_SOUND_PATH + fileName);
									
									viewer.addSoundUrl(SharedConstant.UPLOAD_QUESTION_SOUND_PATH + fileName);
								}
								
								break;
							}
							case Video :
							{	
								if (viewer != null) {
									Log.info(SharedConstant.UPLOAD_QUESTION_VIDEO_PATH + fileName);
									
									viewer.addVideoUrl(SharedConstant.UPLOAD_QUESTION_VIDEO_PATH + fileName);
								}
								break;
							}
							default:
							{
								Window.alert("Error in ResourceUploadEventHandler");
								break;
							}
							}
							
						}else {
							Log.error("Upload fail.");
						}
					}
					
//					private boolean checkImgFileExtension() {
//						boolean flag = false;
//
//						String extension = ClientUtility.getFileExtension(fileName);
//						Log.info("Current extension : " + extension);
//						for (String ext : BMEFileUploadConstant.IMAGE_EXTENSIONS) {
//					
//							if(ext.equalsIgnoreCase(extension)) {
//								flag = true;
//								break;
//				}
//			}
//						return flag;
//		}
					
					private boolean checkImageSize(final String url,
							QuestionTypeProxy questionType) {
						boolean flag = false;
						
						Image image = new Image(new SafeUri() {
							
							@Override
							public String asString() {
								return url;
							}
						});
						
						
						Log.info("Image width * height : " + image.getWidth() + "*" + image.getHeight());
						if(QuestionTypes.Textual.equals(questionType.getQuestionType())) {
							flag = true;
						}
						else if(questionType.getImageWidth() != null && questionType.getImageWidth().equals(image.getWidth()) && questionType.getImageHeight() != null && questionType.getImageHeight().equals(image.getHeight())) {
							flag = true;
						}
						
						return flag;
					}
				});
				
				// added to container
				uploaderContainer.add(resourceUpload);
				viewerContainer.add(viewer);
				
//				if(question != null && questionType != null /*&& question.getPicturePath() != null && question.getPicturePath().length() > 0*/) {
//					
//					viewer.setup(question, questionType.getQuestionType());
//					//viewer.setUrl(GWT.getHostPageBaseURL() + question.getPicturePath(),questionType.getQuestionType());
//				}
	}

			
			
		}
	}

	private List<QuestionResourceClient> getQuestionResourceClient(
			List<QuestionResourceProxy> questionResources) {
		List<QuestionResourceClient> clients = new ArrayList<QuestionResourceClient>();
			
		for (QuestionResourceProxy proxy : questionResources) {
		
			QuestionResourceClient client = new QuestionResourceClient();
			client.setPath(proxy.getPath());
			client.setSequenceNumber(proxy.getSequenceNumber());
			client.setType(proxy.getType());
			client.setState(State.CREATED);
			clients.add(client);
		}
		return clients;
	}

	private boolean allowedQuestionTypeForResouce(QuestionTypeProxy questionType) {
		boolean flag = false;
		
		
		// for image key question
		if(QuestionTypes.Imgkey.equals(questionType.getQuestionType())){
			flag = true;
		}
		//for textual question having images, sound or video
		else if(QuestionTypes.Textual.equals(questionType.getQuestionType()) && ((questionType.getQueHaveImage() != null && questionType.getQueHaveImage().equals(true)) || (questionType.getQueHaveSound() != null && questionType.getQueHaveSound().equals(true)) || (questionType.getQueHaveVideo() != null && questionType.getQueHaveVideo().equals(true)))) {
			flag = true;
		}
		//for area question having height and width
		else if(QuestionTypes.Area.equals(questionType.getQuestionType()) && questionType.getImageWidth() != null && questionType.getImageHeight() != null) {
			flag = true;
		}
		//MCQ question having type as image and height and width or type as sound or type as video
		else if(QuestionTypes.MCQ.equals(questionType.getQuestionType()) && ( (MultimediaType.Image.equals(questionType.getMultimediaType()) && questionType.getImageWidth() != null && questionType.getImageHeight() != null) || (MultimediaType.Sound.equals(questionType.getMultimediaType()) ) || (MultimediaType.Video.equals(questionType.getMultimediaType()) ))) {
			flag = true;
		}
		
		return flag;
	}

//	@Override
//	public ImageViewer getImageViewer() {
//		return viewer;
//	}

	@Override
	public ResourceView getResourceView() {
		return viewer;
	}

	@Override
	public Label getAutherLbl() {
		return lblAuther;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public Set<QuestionResourceClient> getQuestionResources() {
		return viewer.getQuestionResources();
	}

}

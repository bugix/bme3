package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.TabPanelHelper;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.resource.dndview.ResourceView;
import medizin.client.ui.widget.resource.event.ResourceAddedEvent;
import medizin.client.ui.widget.resource.event.ResourceAddedEventHandler;
import medizin.client.ui.widget.resource.event.ResourceDeletedEvent;
import medizin.client.ui.widget.resource.event.ResourceDeletedEventHandler;
import medizin.client.ui.widget.resource.event.ResourceSequenceChangedEvent;
import medizin.client.ui.widget.resource.event.ResourceSequenceChangedHandler;
import medizin.client.ui.widget.resource.image.ImageViewer;
import medizin.client.ui.widget.resource.upload.ResourceUpload;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEvent;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEventHandler;
import medizin.client.util.ClientUtility;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.utils.SharedConstant;
import medizin.shared.utils.SharedUtility;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionDetailsViewImpl extends Composite implements
		QuestionDetailsView {

	private static AssesmentDetailsViewImplUiBinder uiBinder = GWT
			.create(AssesmentDetailsViewImplUiBinder.class);

	interface AssesmentDetailsViewImplUiBinder extends
			UiBinder<Widget, QuestionDetailsViewImpl> {
	}

	public BmeConstants constants = GWT.create(BmeConstants.class);
	private Presenter presenter;
	private Delegate delegate;
	private QuestionProxy proxy;
	
	@UiField
	TabPanel questionTypeDetailPanel;
	
	@UiField
	IconButton edit;

	@UiField
	IconButton delete;

	@UiField
	IconButton accept;
	
	@UiField
	SpanElement displayRenderer;

	@UiField
	Label lblQuestionType;
	
	@UiField
	Label lblQuestionTypeValue;
	
	@UiField
	Label lblQuestionShortName;
	
	@UiField
	Label lblQuestionShortNameValue;


	@UiField
	Label lblQuestionText;

	/*@UiField
	Label lblQuestionTextValue;*/
	
	@UiField
	HTML lblQuestionTextValue;
	
	@UiField
	Label lblAuther;

	@UiField
	Label lblAutherValue;

	@UiField
	Label lblReviewer;

	@UiField
	Label lblReviewerValue;

	@UiField
	Label lblQuestionEvent; 
	
	@UiField
	Label lblQuestionEventValue;

	@UiField
	Label lblComment;

	@UiField
	Label lblCommentValue;

	@UiField
	Label lblMcs;

	@UiField
	Label lblMcsValue;

	
	

	// @UiField
	// SpanElement id;
	//
	// @UiField
	// SpanElement version;

	/*@UiField
	SpanElement dateAdded;

	@UiField
	SpanElement dateChanged;

	@UiField
	SpanElement rewiewer;

	@UiField
	SpanElement autor;

	@UiField
	SpanElement questionText;

	@UiField
	SpanElement picturePath;

	@UiField
	SpanElement questionVersion;

	@UiField
	SpanElement isAcceptedRewiever;

	@UiField
	SpanElement isAcceptedAdmin;

	@UiField
	SpanElement isActive;

	@UiField
	SpanElement previousVersion;

	@UiField
	SpanElement keywords;

	@UiField
	SpanElement questEvent;

	@UiField
	SpanElement comment;

	@UiField
	SpanElement questionType;

	@UiField
	SpanElement mcs;*/

	// @UiField
	// SpanElement answers;

	

	@UiField
	AnswerListViewImpl answerListViewImpl;
	private EventBus eventBus;

	// @UiField
	// EventAccessViewImpl eventAccessView;
	//
	// @UiField
	// QuestionAccessViewImpl questionAccessView;

	// @Override
	// public EventAccessViewImpl getEventAccessView(){
	// return eventAccessView;
	// }

	@Override
	public AnswerListViewImpl getAnswerListViewImpl() {
		return answerListViewImpl;
		
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
	}

	// @UiField
	// SpanElement displayRenderer;

	@Override
	public void setValue(QuestionProxy proxy) {
		this.proxy = proxy;
		
		displayRenderer.setInnerText(proxy.getQuestionShortName()==null?proxy.getId().toString():proxy.getQuestionShortName());
		lblQuestionTypeValue.setText(proxy.getQuestionType()==null?"":proxy.getQuestionType().getShortName());
		lblQuestionShortNameValue.setText(proxy.getQuestionShortName()==null?"":proxy.getQuestionShortName());
		//lblQuestionTextValue.setText(proxy.getQuestionText()==null?"":proxy.getQuestionText());
		lblQuestionTextValue.setHTML(proxy.getQuestionText()==null?"":proxy.getQuestionText());
		lblAutherValue.setText(proxy.getAutor()==null?"":proxy.getAutor().getName());
		lblReviewerValue.setText(proxy.getRewiewer()==null?"":proxy.getRewiewer().getName());
		lblQuestionEventValue.setText(proxy.getQuestEvent()==null?"":proxy.getQuestEvent().getEventName());
		lblCommentValue.setText(proxy.getComment()==null?"":proxy.getComment().getComment());
		lblMcsValue.setText(proxy.getMcs() == null ? "": medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.McProxyRenderer.instance()).render(proxy.getMcs()));
		if(proxy.getSubmitToReviewComitee()==true)
		{
			lblReviewerValue.setText(constants.submitToReviewerCommitte());
		}
		addSecondTabForQuestionResource(proxy);
		
		
		
		
		
		/*mcs.setInnerText(proxy.getMcs() == null ? ""
				: medizin.client.ui.view.roo.CollectionRenderer.of(
						medizin.client.ui.view.roo.McProxyRenderer.instance())
						.render(proxy.getMcs()));
    	*/    
/*		// id.setInnerText(proxy.getId() == null ? "" :
		// String.valueOf(proxy.getId()));
		// version.setInnerText(proxy.getVersion() == null ? "" :
		// String.valueOf(proxy.getVersion()));
		dateAdded.setInnerText(proxy.getDateAdded() == null ? ""
				: DateTimeFormat.getFormat(
						DateTimeFormat.PredefinedFormat.DATE_SHORT).format(
						proxy.getDateAdded()));
		dateChanged.setInnerText(proxy.getDateChanged() == null ? ""
				: DateTimeFormat.getFormat(
						DateTimeFormat.PredefinedFormat.DATE_SHORT).format(
						proxy.getDateChanged()));
		rewiewer.setInnerText(proxy.getRewiewer() == null ? ""
				: medizin.client.ui.view.roo.PersonProxyRenderer.instance()
						.render(proxy.getRewiewer()));
		autor.setInnerText(proxy.getAutor() == null ? ""
				: medizin.client.ui.view.roo.PersonProxyRenderer.instance()
						.render(proxy.getAutor()));
		questionText.setInnerHTML(proxy.getQuestionText() == null ? "" : String
				.valueOf(proxy.getQuestionText()));
		questEvent.setInnerText(proxy.getQuestEvent() == null ? ""
				: medizin.client.ui.view.roo.QuestionEventProxyRenderer
						.instance().render(proxy.getQuestEvent()));
		if (proxy.getPicturePath() == null)
			Document.get().getElementById("picturePath").removeFromParent();
		else
			picturePath.setInnerText(String.valueOf(proxy.getPicturePath()));

		if (proxy.getQuestionVersion() < 2)
			Document.get().getElementById("previousVersion").removeFromParent();
		else {

			previousVersion
					.setInnerText(proxy.getPreviousVersion() == null ? ""
							: medizin.client.ui.view.roo.QuestionProxyRenderer
									.instance().render(
											proxy.getPreviousVersion()));
		}

		questionVersion
				.setInnerText(String.valueOf(proxy.getQuestionVersion()));

		isAcceptedRewiever
				.setClassName(proxy.getIsAcceptedRewiever() == true ? "ui-icon ui-icon-check"
						: "ui-icon ui-icon-closethick");
		isAcceptedAdmin
				.setClassName(proxy.getIsAcceptedAdmin() == true ? "ui-icon ui-icon-check"
						: "ui-icon ui-icon-closethick");
		isActive.setInnerText(proxy.getIsActive() == true ? "ja" : "nein");

		keywords.setInnerText(proxy.getKeywords() == null ? ""
				: medizin.client.ui.view.roo.CollectionRenderer.of(
						medizin.client.ui.view.roo.KeywordProxyRenderer
								.instance()).render(proxy.getKeywords()));

		comment.setInnerText(proxy.getComment() == null ? ""
				: medizin.client.ui.view.roo.CommentProxyRenderer.instance()
						.render(proxy.getComment()));
		questionType.setInnerText(proxy.getQuestionType() == null ? ""
				: medizin.client.ui.view.roo.QuestionTypeProxyRenderer
						.instance().render(proxy.getQuestionType()));
		mcs.setInnerText(proxy.getMcs() == null ? ""
				: medizin.client.ui.view.roo.CollectionRenderer.of(
						medizin.client.ui.view.roo.McProxyRenderer.instance())
						.render(proxy.getMcs()));*/
		// answers.setInnerText(proxy.getAnswers() == null ? "" :
		// medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.AnswerProxyRenderer.instance()).render(proxy.getAnswers()));

	}

	private void addSecondTabForQuestionResource(final QuestionProxy proxy) {
	
		if(proxy != null && proxy.getQuestionType() != null  && proxy.getQuestionType().getQuestionType() != null) {
			
			final QuestionTypes questionType = proxy.getQuestionType().getQuestionType();
			switch (questionType) {
			
			case Textual:
			{
				if(proxy != null && proxy.getQuestionType()!= null && proxy.getQuestionType().getQueHaveImage() != null &&  proxy.getQuestionType().getQueHaveSound() != null && proxy.getQuestionType().getQueHaveVideo() != null) {
					setResourceUploadAndResourceViewer(proxy.getQuestionType(),proxy);
				}
				
				break;
			}	
				
			case Imgkey:
			{
				if(proxy != null && proxy.getQuestionType()!= null && proxy.getQuestionType().getQuestionType() != null) {
					imageViewer(proxy.getQuestionType(),proxy,QuestionTypes.Imgkey);
				}
				
				break;
			}
				
			case ShowInImage:
			{
				if(proxy != null && proxy.getQuestionType()!= null && proxy.getQuestionType().getQuestionType() != null) {
					imageViewer(proxy.getQuestionType(),proxy,QuestionTypes.ShowInImage);
				}
				
				break;
			}
			
			case Sort:
			{
				if(proxy != null && proxy.getQuestionType()!= null && proxy.getQuestionType().getQueHaveImage() != null &&  proxy.getQuestionType().getQueHaveSound() != null && proxy.getQuestionType().getQueHaveVideo() != null) {
					setResourceUploadAndResourceViewer(proxy.getQuestionType(),proxy);
				}
				
				break;
			}
				
			default:
			{
				Log.info("Error");
				break;	
			}
			
			}
			
			
		}
		
		
		
		
		
	}

	public QuestionDetailsViewImpl(EventBus eventBus, Boolean flag) {
		initWidget(uiBinder.createAndBindUi(this));
		
		if (flag)
		{
			edit.setVisible(true);
			delete.setVisible(true);
		}
		else
		{
			edit.setVisible(false);
			delete.setVisible(false);
		}
		
		
		
		this.eventBus = eventBus;
		
		questionTypeDetailPanel.selectTab(0);
		questionTypeDetailPanel.getTabBar().setTabText(0, constants.manageQuestion());
		TabPanelHelper.moveTabBarToBottom(questionTypeDetailPanel);
		lblQuestionShortName.setText(constants.questionShortName());
		lblQuestionType.setText(constants.questionType());
		lblQuestionText.setText(constants.questionText());
		lblAuther.setText(constants.auther()); 
		lblReviewer.setText(constants.reviewer());
		lblComment.setText(constants.comment());
		lblQuestionEvent.setText(constants.questionEvent());
		lblMcs.setText(constants.mcs());
		
	}

	/*@Override
	public void setName(String helloName) {
		// TODO Auto-generated method stub

	}*/

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}
	
	private void setResourceUploadAndResourceViewer(QuestionTypeProxy questionTypeProxy, QuestionProxy question) { 
		
		final ResourceView resourceView = new ResourceView(eventBus,ClientUtility.getQuestionResourceClient(question.getQuestionResources()), proxy.getQuestionType().getQuestionType(), proxy.getQuestionType().getQueHaveImage(),proxy.getQuestionType().getQueHaveSound(),proxy.getQuestionType().getQueHaveVideo());
		
		resourceView.addResourceDeletedHandler(new ResourceDeletedEventHandler() {

				@Override
				public void onResourceDeleted(ResourceDeletedEvent event) {
					Log.info("QuestionResourceClient : " + event.getQuestionResourceClient().getPath());
					
					delegate.deleteSelectedQuestionResource(event.getQuestionResourceClient().getId());
				}
			
		});
		
		resourceView.addResourceAddedHandler(new ResourceAddedEventHandler(){

			@Override
			public void onResourceAdded(ResourceAddedEvent event) {
				
				if(event.isAdded()) {
					delegate.addNewQuestionResource(event.getQuestionResourceClient());
				}else {
					ConfirmationDialogBox.showOkDialogBox("Error", "This type of media is not allowed");
					Set<String> paths = Sets.newHashSet();
					paths.add(event.getQuestionResourceClient().getPath());
					delegate.deleteUploadedFiles(paths);
				}	
			}
		});	
		
		resourceView.addResourceSequenceChangedHandler(new ResourceSequenceChangedHandler() {

			@Override
			public void onSequenceChanged(ResourceSequenceChangedEvent event) {
				delegate.changedResourceSequence(event.getQuestionResourceClients());	
			}
			
		});
		
		Label lblUploadText = new Label(); 
		lblUploadText.setText(constants.uploadResource());
		lblUploadText.addStyleName("lblUploadPadding");
		ArrayList<String> allowedExt = new ArrayList<String>();
		Map<MultimediaType,String> paths = Maps.newHashMap();
	
	
		if(questionTypeProxy.getQueHaveImage() != null && questionTypeProxy.getQueHaveImage() == true) {
			allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
			paths.put(MultimediaType.Image, SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
		}
		
		if(questionTypeProxy.getQueHaveSound()  != null && questionTypeProxy.getQueHaveSound() == true) {
			allowedExt.addAll(Arrays.asList(SharedConstant.SOUND_EXTENSIONS));
			paths.put(MultimediaType.Sound, SharedConstant.UPLOAD_MEDIA_SOUND_PATH);
		}
		
		if(questionTypeProxy.getQueHaveVideo()  != null && questionTypeProxy.getQueHaveVideo() == true) {
			allowedExt.addAll(Arrays.asList(SharedConstant.VIDEO_EXTENSIONS));	
			paths.put(MultimediaType.Video, SharedConstant.UPLOAD_MEDIA_VIDEO_PATH);
		}
		
		ResourceUpload resourceUpload = new ResourceUpload(allowedExt,paths,this.eventBus);
		
		//final QuestionTypeProxy tempQuestionTypeProxy = question.getQuestionType(); 
		
		resourceUpload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
			
			@Override
			public void onResourceUploaded(ResourceUploadEvent event) {

				String filePath = event.getFilePath();
				
				if(event.isResourceUploaded() == true) {
					Log.info("filePath is " + filePath);
					
					MultimediaType mtype = SharedUtility.getFileMultimediaType(SharedUtility.getFileExtension(filePath));
					
					switch (mtype) {
					case Image:
					{
						// for image
						if (resourceView != null) {
//							resourceView.addImageUrl(SharedConstant.UPLOAD_QUESTION_IMAGES_PATH + fileName);
							resourceView.addImageUrl(filePath);
						}
						break;
					}	
					case Sound:
					{	
						if (resourceView != null) {
//							resourceView.addSoundUrl(SharedConstant.UPLOAD_QUESTION_SOUND_PATH + fileName);
							resourceView.addSoundUrl(filePath);
						}
						
						break;
					}
					case Video :
					{	
						if (resourceView != null) {
							resourceView.addVideoUrl(filePath);
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
		});
		
		
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		HorizontalPanel h1 =  new HorizontalPanel();
		HorizontalPanel h2 = new HorizontalPanel();
		h2.setWidth("100%");
		/*h1.add(lblUploadText);*/
		/*h1.add(resourceUpload);*/
		h2.add(resourceView);
		panel.add(h1);
		panel.add(h2);
		
		questionTypeDetailPanel.add(panel, constants.resources());

	}

	private void imageViewer(final QuestionTypeProxy questionTypeProxy,QuestionProxy questionProxy,final QuestionTypes type) {
		final ImageViewer imageViewer = new ImageViewer();
		if(questionProxy.getPicturePath() != null && questionProxy.getPicturePath().length() > 0) {
			imageViewer.setUrl(questionProxy.getPicturePath(),questionTypeProxy.getImageWidth(),questionTypeProxy.getImageHeight(), type);
		}
			
			
		Label lblUploadText = new Label(); 
		lblUploadText.setText(constants.uploadResource());
		lblUploadText.addStyleName("lblUploadPadding");
		ArrayList<String> allowedExt = new ArrayList<String>();
		allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
		Map<MultimediaType,String> paths = Maps.newHashMap();
		paths.put(MultimediaType.Image, SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
		
		ResourceUpload resourceUpload = new ResourceUpload(allowedExt,paths,this.eventBus);
		
//		final QuestionTypeProxy tempQuestionTypeProxy = proxy.getQuestionType(); 
		
		resourceUpload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
			
			@Override
			public void onResourceUploaded(ResourceUploadEvent event) {

				final String filePath = event.getFilePath();
				
				if(event.isResourceUploaded() == true) {
					Log.info("filepath is " + filePath);
					// for image
					final String url = new String(GWT.getHostPageBaseURL() + filePath);
					if(questionTypeProxy.getImageWidth() != null && questionTypeProxy.getImageHeight() !=  null) {
					
						Function<Boolean, Void> function = new Function<Boolean, Void>() {
							
							@Override
							public Void apply(Boolean flag) {
						
								if(flag != null && flag == true) {
									
									if(imageViewer != null && imageViewer.getImageUrl() != null && imageViewer.getImageUrl().length() > 0) {
										// delete old files
										Log.info("Delete old uploaded file " + imageViewer.getImageUrl().toString());
										delegate.deleteUploadedFiles(Sets.newHashSet(imageViewer.getImageUrl().replace(GWT.getHostPageBaseURL(), "")));
									}
									
									imageViewer.setUrl(filePath, questionTypeProxy.getImageWidth(), questionTypeProxy.getImageHeight(), type);	
									delegate.updatePicturePathInQuestion(filePath);
								}else {
									ConfirmationDialogBox.showOkDialogBox(constants.warning(),constants.imageSizeError().replace("(0)", questionTypeProxy.getImageWidth().toString()).replace("(1)", questionTypeProxy.getImageHeight().toString()));
									delegate.deleteUploadedPicture(filePath);
								}

								return null;
							}
						};
						
						if(event.getWidth() != null && event.getHeight() != null) {
							if(event.getWidth().equals(questionTypeProxy.getImageWidth()) && event.getHeight().equals(questionTypeProxy.getImageHeight())) {
								function.apply(true);
							}else {
								function.apply(false);
							}
						}else {
							ClientUtility.checkImageSize(url,questionTypeProxy.getImageWidth(),questionTypeProxy.getImageHeight(),function);
						}
					}
					
				}else {
					Log.error("Upload fail.");
				}
			}
		});
		
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		HorizontalPanel h1 =  new HorizontalPanel();
		HorizontalPanel h2 = new HorizontalPanel();
		h2.setWidth("100%");
		/*h1.add(lblUploadText);*/
		/*h1.add(resourceUpload);*/
		h2.add(imageViewer);
		panel.add(h1);
		panel.add(h2);
		
		questionTypeDetailPanel.add(panel, constants.resources());
	}
	
	public void setInvisibleIconButton(Boolean flag)
	{
		if (flag)
		{
			edit.setVisible(true);
			delete.setVisible(true);
		}
		else
		{
			edit.setVisible(false);
			delete.setVisible(false);
		}
	}
	
	public void setVisibleAcceptButton()
	{
		delete.setVisible(false);
		edit.setVisible(true);
		accept.setVisible(true);
	}
	
	@UiHandler("accept")
	public void onAcceptClicked(ClickEvent e) {
		if (proxy != null)
			delegate.acceptQuestionClicked(proxy);
	}
}

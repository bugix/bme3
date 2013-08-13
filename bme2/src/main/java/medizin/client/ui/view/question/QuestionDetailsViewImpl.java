package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.List;

import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.MyCellTableNoHilightResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.TabPanelHelper;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.resource.dndview.ResourceView;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.util.ClientUtility;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeMessages;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionDetailsViewImpl extends Composite implements QuestionDetailsView {

	private static QuestionDetailsViewImplUiBinder uiBinder = GWT.create(QuestionDetailsViewImplUiBinder.class);

	interface QuestionDetailsViewImplUiBinder extends UiBinder<Widget, QuestionDetailsViewImpl> {
	}

	public BmeMessages bmeMessages = GWT.create(BmeMessages.class);
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

	

	@UiField(provided=true)
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

	@UiField
	VerticalPanel answerVerticalPanel;
	
	@UiField(provided = true)
	CellTable<KeywordProxy> keywordTable;
	
	@UiField(provided = true)
	SimplePager keywordTablePager;
	
	@UiField
	public DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> keywordSuggestBox;
	
	@UiField
	IconButton keywordAddButton;

	private List<AbstractEditableCell<?, ?>> editableCells;
	
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
		/*//remove edit and delete btn
		
		if(delegate.isQuestionDetailsPlace() && proxy != null && proxy.getIsReadOnly() == false) {
			setVisibleIconButton(true);	
		}
		else if(delegate.isQuestionDetailsPlace() && proxy != null && proxy.getIsReadOnly() == true) {
			setVisibleIconButton(false);
		}
		else  if(delegate.isQuestionDetailsPlace() == false){
			setVisibleAcceptButton();
		}*/
		
		delegate.getLatestQuestionDetails(new Function<QuestionProxy, Void>() {

			@Override
			public Void apply(QuestionProxy input) {
				setValue(input);
				
				//remove edit and delete btn
				
				delegate.enableBtnOnLatestClicked();
				/*if(delegate.isQuestionDetailsPlace() && proxy != null && proxy.getIsReadOnly() == false) {
					setVisibleIconButton(true);	
				}
				else if(delegate.isQuestionDetailsPlace() && proxy != null && proxy.getIsReadOnly() == true) {
					setVisibleIconButton(false);
				}
				else  if(delegate.isQuestionDetailsPlace() == false){
					setVisibleAcceptButton();
				}*/
				
				latest.setEnabled(false);
				if(input.getPreviousVersion()== null) {
					previous.setEnabled(false);	
				}else {
					previous.setEnabled(true);
				}
				return null;
			}
		});	
	}

	@UiHandler("forcedActive")
	public void onForcedActiveClicked(ClickEvent e) {
		delegate.forcedActiveClicked();
	}
	// @UiField
	// SpanElement displayRenderer;

	@Override
	public void setValue(QuestionProxy proxy) {
		this.proxy = proxy;
		String qVersion = proxy.getQuestionVersion() == null ? "0" : proxy.getQuestionVersion().toString();
		String qSubVersion = proxy.getQuestionSubVersion() == null ? "0" : proxy.getQuestionSubVersion().toString();
		String version = "(" + qVersion + "." + qSubVersion + ")";
		
		displayRenderer.setInnerText(proxy.getQuestionShortName()==null?proxy.getId().toString():proxy.getQuestionShortName());
		displayRenderer.setTitle(proxy.getQuestionShortName()==null?proxy.getId().toString():proxy.getQuestionShortName());
		
		displayVersionRenderer.setInnerText(version);
		
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
			lblReviewerValue.setText(constants.submitToReviewComitee());
		}
		addSecondTabForQuestionResource(proxy);
		
		
		if(proxy.getPreviousVersion() == null) {
			previous.setEnabled(false);
		}
		
		delegate.checkForResendToReview();
		
		if(proxy.getIsReadOnly() == true) {
			setVisibleEditAndDeleteBtn(false);
		}
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
		
		resourceUploadPanel.clear();
		resourceViewPanel.clear();
		boolean isAdded = false;
		List<QuestionResourceClient> questionResourceClients = new ArrayList<QuestionResourceClient>();
		boolean haveImage = false;
		boolean haveSound = false;
		boolean haveVideo = false;
		
		if(proxy != null && proxy.getQuestionType() != null  && proxy.getQuestionType().getQuestionType() != null) {
			
			final QuestionTypes questionType = proxy.getQuestionType().getQuestionType();
			switch (questionType) {
			
			case Textual:
			case Sort:
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
					//imageViewer(proxy.getQuestionType(),proxy,QuestionTypes.Imgkey);
					questionResourceClients = ClientUtility.getQuestionResourceClient(proxy.getQuestionResources());
					/*QuestionResourceClient client = new QuestionResourceClient();
					client.setPath(proxy.getPicturePath());
					client.setSequenceNumber(0);
					client.setState(State.CREATED);
					client.setType(MultimediaType.Image);
					questionResourceClients.add(client);*/
					haveImage = true;
					isAdded = true;
				}
				
				break;
			}
			default:
			{
				Log.info("in default case");
				if(questionTypeDetailPanel.getTabBar().getTabCount() > 1) 
					questionTypeDetailPanel.remove(1);
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

	public QuestionDetailsViewImpl(EventBus eventBus, Boolean flag, boolean isAnswerEditable) {
		CellTable.Resources tableResources = GWT.create(MyCellTableNoHilightResources.class);
		keywordTable = new CellTable<KeywordProxy>(5, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		keywordTablePager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, 10, true);
		
		answerListViewImpl = new AnswerListViewImpl(isAnswerEditable);
		matrixAnswerListViewImpl = new MatrixAnswerListViewImpl(isAnswerEditable);
		initWidget(uiBinder.createAndBindUi(this));
		
		setVisibleEditAndDeleteBtn(flag);
		
		this.eventBus = eventBus;
		
		questionTypeDetailPanel.selectTab(0);
		questionTypeDetailPanel.getTabBar().setTabText(0, constants.manageQuestion());
		questionTypeDetailPanel.getTabBar().setTabText(1, constants.media());
		questionTypeDetailPanel.getTabBar().setTabText(2, constants.keywords());
		TabPanelHelper.moveTabBarToBottom(questionTypeDetailPanel);
		
		keywordAddButton.setText(constants.addKeyword());
		
		initKeyword(isAnswerEditable);
	}
	
	@UiHandler("keywordAddButton")
	public void keywordAddButtonClicked(ClickEvent event)
	{
		if (keywordSuggestBox.getText().isEmpty())
		{
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.keywordNullMessage());
		}
		else
		{
			delegate.keywordAddButtonClicked(keywordSuggestBox.getText(), proxy);
		}
	}

	private void initKeyword(boolean isAnswerEditable) {
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
		keywordTable.addColumn(new TextColumn<KeywordProxy>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

			@Override
			public String getValue(KeywordProxy object) {
				return object == null ? null : object.getName();
			}
		}, constants.keywords());
		
		if (isAnswerEditable)
		{
			addColumn(new ActionCell<KeywordProxy>(
	        		McAppConstant.DELETE_ICON, new ActionCell.Delegate<KeywordProxy>() {
						public void execute(final KeywordProxy keyword) {
							ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.deleteInstitutionConfirmation(), new ConfirmDialogBoxYesNoButtonEventHandler() {
								
								@Override
								public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
									delegate.deleteKeywordClicked(keyword, proxy);
								}
								
								@Override
								public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
																
								}
							});
							
							
						}	
					}), "", new GetValue<KeywordProxy>() {
				public KeywordProxy getValue(KeywordProxy keyword) {
					return keyword;
				}
			}, null);
	    
			keywordTable.addColumnStyleName(1, "iconColumn");			
		}
		
		keywordTable.addColumnStyleName(0, "questionTextColumn");
	}

	/*@Override
	public void setName(String helloName) {
		// todo Auto-generated method stub

	}*/

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}
	
	/*private void setResourceUploadAndResourceViewer(QuestionTypeProxy questionTypeProxy, QuestionProxy question) { 
		
		final ResourceView resourceView = new ResourceView(eventBus,ClientUtility.getQuestionResourceClient(question.getQuestionResources()), proxy.getQuestionType().getQuestionType(), proxy.getQuestionType().getQueHaveImage(),proxy.getQuestionType().getQueHaveSound(),proxy.getQuestionType().getQueHaveVideo(),false);
		
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
		h1.add(lblUploadText);
		h1.add(resourceUpload);
		h2.add(resourceView);
		panel.add(h1);
		panel.add(h2);
		questionTypeDetailPanel.add(panel, constants.media());
		
		
		resourceUploadPanel.add(lblUploadText);
		resourceUploadPanel.add(resourceUpload);
		resourceViewPanel.add(resourceView);

	}*/

	/*private void imageViewer(final QuestionTypeProxy questionTypeProxy,QuestionProxy questionProxy,final QuestionTypes type) {
		final ImageViewer imageViewer = new ImageViewer();
		if(questionProxy.getPicturePath() != null && questionProxy.getPicturePath().length() > 0) {
			imageViewer.setUrl(questionProxy.getPicturePath(),questionTypeProxy.getImageWidth(),questionTypeProxy.getImageHeight(), type);
		}else {
			return ;
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
									//ConfirmationDialogBox.showOkDialogBox(constants.warning(),constants.imageSizeError().replace("(0)", questionTypeProxy.getImageWidth().toString()).replace("(1)", questionTypeProxy.getImageHeight().toString()));
									ConfirmationDialogBox.showOkDialogBox(constants.warning(),bmeMessages.imageSizeError(questionTypeProxy.getImageWidth(), questionTypeProxy.getImageHeight()));
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
		h1.add(lblUploadText);
		h1.add(resourceUpload);
		h2.add(imageViewer);
		panel.add(h1);
		panel.add(h2);
		
		questionTypeDetailPanel.add(panel, constants.media());
		
		resourceUploadPanel.add(lblUploadText);
		resourceUploadPanel.add(resourceUpload);
		resourceViewPanel.add(imageViewer);
	}*/
	
	public void setVisibleEditAndDeleteBtn(Boolean flag)
	{
		edit.setVisible(flag);
		delete.setVisible(flag);
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
	
	@UiHandler("resendToReview")
	public void onResendToReviewClicked(ClickEvent e) {
		if (proxy != null)
			delegate.onResendToReviewClicked(proxy);
	}
	
	@UiField(provided=true)
	MatrixAnswerListViewImpl matrixAnswerListViewImpl;

	public MatrixAnswerListViewImpl getMatrixAnswerListViewImpl() {
		return matrixAnswerListViewImpl;
	}

	public void setMatrixAnswerListViewImpl(
			MatrixAnswerListViewImpl matrixAnswerListViewImpl) {
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
	public IconButton getForcedActiveBtn() {
		return forcedActive;
	}

	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<KeywordProxy, C> fieldUpdater) {
		Column<KeywordProxy, C> column = new Column<KeywordProxy, C>(cell) {
			@Override
			public C getValue(KeywordProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		keywordTable.addColumn(column);
	}
	
	  private static interface GetValue<C> {
	    C getValue(KeywordProxy keyword);
	  }

	public CellTable<KeywordProxy> getKeywordTable() {
		return keywordTable;
	}

	public void setKeywordTable(CellTable<KeywordProxy> keywordTable) {
		this.keywordTable = keywordTable;
	}

	public DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> getKeywordSuggestBox() {
		return keywordSuggestBox;
	}

	public void setKeywordSuggestBox(
			DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> keywordSuggestBox) {
		this.keywordSuggestBox = keywordSuggestBox;
	}

	public IconButton getKeywordAddButton() {
		return keywordAddButton;
	}

	public void setKeywordAddButton(IconButton keywordAddButton) {
		this.keywordAddButton = keywordAddButton;
	}

	
}

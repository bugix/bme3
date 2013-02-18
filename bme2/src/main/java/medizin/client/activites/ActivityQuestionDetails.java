package medizin.client.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.request.AnswerRequest;
import medizin.client.request.CommentRequest;
import medizin.client.request.QuestionRequest;
import medizin.client.request.QuestionResourceRequest;
import medizin.client.shared.Validity;
import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.AnswerDialogbox;
import medizin.client.ui.view.question.AnswerDialogboxImpl;
import medizin.client.ui.view.question.AnswerListView;
import medizin.client.ui.view.question.AnswerListViewImpl;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.ui.widget.resource.audio.AudioViewer;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.client.ui.widget.resource.image.polygon.ImagePolygonViewer;
import medizin.client.ui.widget.resource.image.rectangle.ImageRectangleViewer;
import medizin.client.ui.widget.resource.image.simple.SimpleImageViewer;
import medizin.client.ui.widget.resource.upload.ResourceUpload;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEvent;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEventHandler;
import medizin.client.ui.widget.resource.video.VideoViewer;
import medizin.client.util.Point;
import medizin.client.util.PolygonPath;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.utils.SharedConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;



public class ActivityQuestionDetails extends AbstractActivityWrapper implements 
	QuestionDetailsView.Delegate, QuestionDetailsView.Presenter, AnswerDialogbox.Delegate, 
	 AnswerListView.Delegate{

	private AcceptsOneWidget widget;
	//private QuestionDetailsView view;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private PlaceQuestionDetails questionPlace;
	
	private QuestionProxy question;
	private QuestionDetailsView questionDetailsView;
	private QuestionDetailsView view;
	private AnswerListViewImpl answerListView;
	private CellTable<AnswerProxy> answerTable;
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	public ActivityQuestionDetails(PlaceQuestionDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}
	
	private PersonProxy loggedUser;
	private HandlerRegistration answerRangeChangeHandler;
	private EventBus eventBus;
	
	
	@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}
	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {
		
		questionDetailsView = new QuestionDetailsViewImpl(eventBus);
		questionDetailsView.setName("hallo");
		questionDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = questionDetailsView;
		this.eventBus = eventBus;
        widget.setWidget(questionDetailsView.asWidget());
		//setTable(view.getTable());
        
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		//init();
		
		view.setDelegate(this);
		
		this.answerListView = view.getAnswerListViewImpl();
		answerListView.setDelegate(this);
		this.answerTable = answerListView.getTable();
		
		requests.personRequest().myGetLoggedPerson()
				.fire(new Receiver<PersonProxy>() {

					@Override
					public void onSuccess(PersonProxy response) {
						loggedUser = response;
						start2();

					}

					public void onFailure(ServerFailure error) {
						ErrorPanel erorPanel = new ErrorPanel();
						erorPanel.setErrorMessage(error.getMessage());
						Log.error(error.getMessage());
						onStop();
					}

					@Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while (iter.hasNext()) {
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
								+ " in Antwort löschen -" + message);

						ErrorPanel erorPanel = new ErrorPanel();
						erorPanel.setErrorMessage(message);
						onStop();

					}

				});

	}
	private void start2(){
		if(loggedUser==null) return;
		
		requests.find(questionPlace.getProxyId()).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof QuestionProxy){
					Log.info(((QuestionProxy) response).getQuestionText());
					init((QuestionProxy) response);
				}

				
			}
			
		    });
	}
	
	/**
	 * 
	 * @param AssesmentProxy assesment
	 */
	private void init(QuestionProxy question) {

		this.question = question;
		Log.debug("Details für: "+question.getQuestionText());
		view.setValue(question);	
		
		initAnswerView();
	}

	private void initAnswerView() {
		
		if (answerRangeChangeHandler!=null){
			answerRangeChangeHandler.removeHandler();
			answerRangeChangeHandler=null;
		}
		
		requests.answerRequest().contAnswersByQuestion(question.getId()).fire( new Receiver<Long>(){


				@Override
				public void onSuccess(Long response) {
					
					answerTable.setRowCount(response.intValue(), true);

					onAnswerTableRangeChanged();
				}
				
		          public void onFailure(ServerFailure error){
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(error.getMessage());
						Log.error(error.getMessage());
					}
		          @Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort hinzufügen -" + message);
						
			        	  ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setErrorMessage(message);
						

						
					}
		      
		});
		
		answerRangeChangeHandler =  answerTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityQuestionDetails.this.onAnswerTableRangeChanged();
			}
		});
		
		
		
		
	}

	protected void onAnswerTableRangeChanged() {
		final Range range = answerTable.getVisibleRange();
		
		
		requests.answerRequest().findAnswersEntriesByQuestion(question.getId(), range.getStart(), range.getLength()).fire( new Receiver<List<AnswerProxy>>(){


			@Override
			public void onSuccess(List<AnswerProxy> response) {
				
				answerTable.setRowData(range.getStart(), response);

			}
			
	          public void onFailure(ServerFailure error){
	        	  ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
	          @Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort hinzufügen -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);
					
					
				}
	      
	});
		
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void deleteClicked() {
		requests.questionRequest().remove().using(question).fire(new Receiver<Void>() {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	placeController.goTo(new PlaceQuestion("PlaceQuestion!DELETED"));
            	
            }
            @Override
			public void onFailure(ServerFailure error) {
					Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Assesment -" + error.getMessage());
					if(error.getMessage().contains("ConstraintViolationException")){
						Log.debug("Fehlen beim erstellen: Doppelter name");
						//TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
					}
				
			}
			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while(iter.hasNext()){
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
				
				//TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

				
			}
            
        });
		
	}

	@Override
	public void editClicked() {
		placeController.goTo(new PlaceQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT));

		
	}

	@Override
	public void newClicked() {
		placeController.goTo(new PlaceQuestionDetails(PlaceQuestionDetails.Operation.CREATE));
		
	}
	
	private AnswerDialogbox answerDialogbox;
	/*private RequestFactoryEditorDriver<AnswerProxy, AnswerDialogboxImpl> answerDriver;*/
	private AnswerProxy answerProxy;
	private CommentProxy commentProxy;

	@Override
	public void addNewAnswerClicked() {
		
		answerDialogbox = new AnswerDialogboxImpl(question);
		answerDialogbox.setDelegate(this);
		
		if(question != null && question.getQuestionType() != null && question.getQuestionType().getQuestionType() != null) {
			
			answerDialogbox.getLblUploadText().setText("");
			answerDialogbox.getUploaderContainer().clear();
			answerDialogbox.getViewContainer().clear();
			
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
					
		
		/*answerDriver = answerDialogbox.createEditorDriver();*/
		
		
		
			
			
			//this.answerProxy = request.create(AnswerProxy.class);
			/*AnswerRequest ansRequest = requests.answerRequest();
			CommentRequest commnetRequest=requests.commentRequest();
			AnswerProxy ansProxy=ansRequest.create(AnswerProxy.class);
			CommentProxy comProxy=ansRequest.create(CommentProxy.class);
			this.answerProxy = ansProxy;
			this.commentProxy=comProxy;
			
			
			

			ansRequest.persist().using(answerProxy);
	        answerProxy.setQuestion(question);
	        answerProxy.setDateAdded(new Date());
	        answerProxy.setAutor(loggedUser);
	        if(loggedUser.getIsAdmin()){
		        answerProxy.setIsAnswerAcceptedAdmin(true);
		        answerProxy.setIsAnswerAcceptedAutor(false);
		        answerProxy.setRewiewer(question.getAutor());
	        } else {
		        answerProxy.setIsAnswerAcceptedAdmin(false);
		        answerProxy.setIsAnswerAcceptedAutor(true);
	        }

	        answerProxy.setIsAnswerAcceptedReviewWahrer(false);
	        answerDialogbox.setRichPanelHTML(answerProxy.getAnswerText());
	        
	        answerProxy.setIsAnswerActive(false);
	        answerDriver.edit(answerProxy, ansRequest);

	        answerDriver.flush();
*/		
		answerDialogbox.setValidityPickerValues(Arrays.asList(Validity.values()));
		answerDialogbox.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new Receiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                answerDialogbox.setRewiewerPickerValues(values);
	            }
	        });
	        
	       // answerDialogbox.setAutherPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new Receiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                answerDialogbox.setAutherPickerValues(values,userLoggedIn);
	            }
	        });
	        
	        /*if(userLoggedIn!=null)
	        {
	        	if(userLoggedIn.getIsAdmin() == false) {
	        		Log.info("1 " + answerDialogbox);
	        		Log.info("1 " + answerDialogbox.getAutherSuggestBox());
	        		Log.info("1 " +loggedUser);
	        		
	        		answerDialogbox.getAutherSuggestBox().setSelected(loggedUser);
	        		//answerDialogbox.getAutherSuggestBox().setSelected(userLoggedIn);
	        		answerDialogbox.getAutherSuggestBox().setEnabled(false);
	        	}
	        }*/
	        	
	        answerDialogbox.display(question.getQuestionType().getQuestionType());

		
	}
	private void addForMCQ() {
		answerDialogbox.getLblUploadText().setText(constants.uploadResource());
		ResourceUpload upload = null;
		
		switch (question.getQuestionType().getMultimediaType()) {
		case Image:
		{
			ArrayList<String> allowedExt = Lists.newArrayList();
			Map<MultimediaType,String> paths = Maps.newHashMap();
			allowedExt.addAll(Arrays.asList(SharedConstant.IMAGE_EXTENSIONS));
			paths.put(MultimediaType.Image, SharedConstant.UPLOAD_MEDIA_IMAGES_PATH);
			upload = new ResourceUpload(allowedExt,paths, this.eventBus);
			upload.addResourceUploadedHandler(new ResourceUploadEventHandler() {
				
				@Override
				public void onResourceUploaded(ResourceUploadEvent event) {
					if(event.isResourceUploaded()) {
						final SimpleImageViewer viewer = new SimpleImageViewer(event.getFilePath());
						answerDialogbox.getViewContainer().clear();
						answerDialogbox.getViewContainer().add(viewer);
						answerDialogbox.setSimpleImageViewer(viewer);

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
								AudioViewer viewer = new AudioViewer(event.getFilePath());
								
								answerDialogbox.getViewContainer().clear();
								answerDialogbox.getViewContainer().add(viewer);
								answerDialogbox.setAudioViewer(viewer);	
							}else {
								ErrorPanel errorPanel = new ErrorPanel();
								errorPanel.setErrorMessage("Media size must be lessthan " + question.getQuestionType().getMaxBytes());
							}
						}else {
							Log.error("Error in MCQ question.");
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
								final VideoViewer viewer = new VideoViewer(event.getFilePath());
								answerDialogbox.getViewContainer().clear();
								answerDialogbox.setVideoViewer(viewer);
								answerDialogbox.getViewContainer().add(viewer);
							}else {
								ErrorPanel errorPanel = new ErrorPanel();
								errorPanel.setErrorMessage("Media size must be lessthan " + question.getQuestionType().getMaxBytes());
							}
						}else {
							Log.error("Error in MCQ question.");
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
			answerDialogbox.getUploaderContainer().clear();
			answerDialogbox.getUploaderContainer().add(upload);
		}
	}

	private void addForImageKey() {

		AnswerRequest answerRequest = requests.answerRequest();
		Log.info("Question id :" + question.getId());
		answerRequest.findAllAnswersPoints(question.getId()).fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> points) {
		
				List<Point> rectanglePoints = Point.getPoints(points);
				
				if(question != null && question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) && question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null) {
					ImageRectangleViewer viewer = new ImageRectangleViewer(question.getPicturePath(), question.getQuestionType().getImageWidth(), question.getQuestionType().getImageHeight(),rectanglePoints);
					answerDialogbox.getViewContainer().add(viewer);
					answerDialogbox.setImageRectangleViewer(viewer);
				}
			}
			

			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				Log.error("error in onConstraintViolation");
				super.onConstraintViolation(violations);
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Log.error("error in onFailure : " + error.getMessage());
				super.onFailure(error);
			}
			
		});		
	}

	private void addForShowInImage() {

		AnswerRequest answerRequest = requests.answerRequest();
		Log.info("Question id :" + question.getId());
		answerRequest.findAllAnswersPoints(question.getId()).fire(new Receiver<List<String>>() {

			@Override
			public void onSuccess(List<String> polygons) {
		
				List<PolygonPath> polygonPaths = PolygonPath.getPolygonPaths(polygons);
				
				if(question != null && question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) && question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null) {
					ImagePolygonViewer viewer = new ImagePolygonViewer(question.getPicturePath(), question.getQuestionType().getImageWidth(), question.getQuestionType().getImageHeight(),polygonPaths);
					answerDialogbox.getViewContainer().add(viewer);
					answerDialogbox.setImagePolygonViewer(viewer);
				}
			}
			

			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				Log.error("error in onConstraintViolation");
				super.onConstraintViolation(violations);
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Log.error("error in onFailure : " + error.getMessage());
				super.onFailure(error);
			}
			
		});
	}	

	@Override
	public void addAnswerClicked() {
		
		final AnswerRequest ansRequest = requests.answerRequest();
		CommentRequest commnetRequest=requests.commentRequest();
		
		final AnswerProxy answerProxy=ansRequest.create(AnswerProxy.class);
		CommentProxy commentProxy=commnetRequest.create(CommentProxy.class);
		
		/*this.answerProxy = ansProxy;
		this.commentProxy=comProxy;*/
		
		
        answerProxy.setQuestion(question);
        answerProxy.setDateAdded(new Date());
        answerProxy.setAutor(loggedUser);
        if(loggedUser.getIsAdmin()){
	        answerProxy.setIsAnswerAcceptedAdmin(true);
	        answerProxy.setIsAnswerAcceptedAutor(false);
	      //  answerProxy.setRewiewer(question.getAutor());
        } else {
	        answerProxy.setIsAnswerAcceptedAdmin(false);
	        answerProxy.setIsAnswerAcceptedAutor(true);
        }
        
        answerProxy.setIsAnswerAcceptedReviewWahrer(false);
        //answerDialogbox.setRichPanelHTML(answerProxy.getAnswerText());
        
        answerProxy.setIsAnswerActive(false);
        
		answerProxy.setAnswerText(answerDialogbox.getRichtTextHTML());
		answerProxy.setValidity(answerDialogbox.getValidity().getValue());
		commentProxy.setComment(answerDialogbox.getComment().getValue());
		if(answerDialogbox.getSubmitToReviewerComitee().isChecked())
		{
			answerProxy.setRewiewer(null);
		}
		else
		{
			answerProxy.setRewiewer(answerDialogbox.getRewiewer().getValue());
		}
		
		if(question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) == true ) {
			Log.info("IN ShowInImage Question type");
			if(answerDialogbox != null && answerDialogbox.getImagePolygonViewer() != null && answerDialogbox.getImagePolygonViewer().isValidPolygon()) {
				Log.info("Points : " + answerDialogbox.getImagePolygonViewer().getPoints());
				answerProxy.setPoints(answerDialogbox.getImagePolygonViewer().getPoints());
				Log.info("Points added");	
			}else {
				ErrorPanel errorPanel = new ErrorPanel();
				errorPanel.setErrorMessage("Polygon is not property added. Try again");
				Log.error("Polygon is not property added. Try again");
				return;
			}
			
		}else if(question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) == true && answerDialogbox.getValidity() != null && Validity.Wahr.equals(answerDialogbox.getValidity().getValue())) {
			Log.info("IN Imgkey Question type");
			if(answerDialogbox != null && answerDialogbox.getImageRectangleViewer() != null) {
				Log.info("Points : " + answerDialogbox.getImageRectangleViewer().getPoint());
				answerProxy.setPoints(answerDialogbox.getImageRectangleViewer().getPoint());
				Log.info("Points added");
			}else {
				ErrorPanel errorPanel = new ErrorPanel();
				errorPanel.setErrorMessage("Rectangle is not property added. Try again");
				Log.error("Rectangle is not property added. Try again");
				return;
			}
			
		}else if(question.getQuestionType() != null && QuestionTypes.MCQ.equals(question.getQuestionType().getQuestionType()) == true) {
			Log.info("IN MCQ Question type");
			if(answerDialogbox != null) {
				
				if(question != null && question.getQuestionType() != null && question.getQuestionType().getMultimediaType() != null) {
				
					switch (question.getQuestionType().getMultimediaType()) {
					case Image:
					{
						if(answerDialogbox.getSimpleImageViewer() != null && answerDialogbox.getSimpleImageViewer().getURL() != null && answerDialogbox.getSimpleImageViewer().getURL().length() > 0) {
							answerProxy.setMediaPath(answerDialogbox.getSimpleImageViewer().getURL());
						}else {
							ErrorPanel errorPanel = new ErrorPanel();
							errorPanel.setErrorMessage("Error in ImageViewer.Try again");
							Log.error("Error in ImageViewer. Try again");
							return;
						}
						
						break;
					}
					case Sound:
					{
						if(answerDialogbox.getAudioViewer() != null && answerDialogbox.getAudioViewer().getURL() != null && answerDialogbox.getAudioViewer().getURL().length() > 0) {
							answerProxy.setMediaPath(answerDialogbox.getAudioViewer().getURL());
						}else {
							ErrorPanel errorPanel = new ErrorPanel();
							errorPanel.setErrorMessage("Error in AudioViewer.Try again");
							Log.error("Error in ImageViewer. Try again");
							return;
						}
						break;
					}
					case Video:
					{	
						if(answerDialogbox.getVideoViewer() != null && answerDialogbox.getVideoViewer().getURL() != null && answerDialogbox.getVideoViewer().getURL().length() > 0) {
							answerProxy.setMediaPath(answerDialogbox.getVideoViewer().getURL());
						}else {
							ErrorPanel errorPanel = new ErrorPanel();
							errorPanel.setErrorMessage("Error in VideoViewer.Try again");
							Log.error("Error in ImageViewer. Try again");
							return;
						}
						break;
					}
					default:
					{
						ErrorPanel errorPanel = new ErrorPanel();
						errorPanel.setErrorMessage("Error in MultimediaType. Try again");
						Log.error("Error in MultimediaType. Try again");
						return;
					}
					}
				}
				
				
				
						
			}else {
				ErrorPanel errorPanel = new ErrorPanel();
				errorPanel.setErrorMessage(". Try again");
				return;
			}
			
		}
		
		answerProxy.setComment(commentProxy);
		answerProxy.setSubmitToReviewComitee(answerDialogbox.getSubmitToReviewerComitee().getValue());
		Log.info("before save");
		
		commnetRequest.persist().using(commentProxy).fire(new Receiver<Void>() {

			public void onFailure(ServerFailure error){
				Log.info("on failure");
				ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
			
	@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				
				Iterator<ConstraintViolation<?>> iter = violations.iterator();
				String message = "";
				while(iter.hasNext()){
					ConstraintViolation<?> v = iter.next();
					message += v.getPropertyPath() + " : " + v.getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
				
				ErrorPanel erorPanel = new ErrorPanel();
	        	erorPanel.setWarnMessage(message);
	        	super.onConstraintViolation(violations);
			}

//	          @Override
//				public void onViolation(Set<Violation> errors) {
//	        	  Log.info("on violate");
//					Iterator<Violation> iter = errors.iterator();
//					String message = "";
//					while(iter.hasNext()){
//						message += iter.next().getPath() + "<br>";
//					}
//					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antworten auflisten -" + message);
//					
//		        	  
//					ErrorPanel erorPanel = new ErrorPanel();
//		        	  erorPanel.setErrorMessage(message);
//
//				}
	          
			@Override
			public void onSuccess(Void response) {
				// TODO Auto-generated method stub
				ansRequest.persist().using(answerProxy).fire(new Receiver<Void>() {

					@Override
					public void onSuccess(Void response) {
						// TODO Auto-generated method stub
						Log.info("fullSaved");
			        	  
		        		initAnswerView();
		        		answerDialogbox.close();
					}
					public void onFailure(ServerFailure error){
						Log.info("on failure");
						ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setErrorMessage(error.getMessage());
							Log.error(error.getMessage());
						}
			          @Override
						public void onViolation(Set<Violation> errors) {
			        	  Log.info("on violate");
							Iterator<Violation> iter = errors.iterator();
							String message = "";
							while(iter.hasNext()){
								message += iter.next().getPath() + "<br>";
							}
							Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antworten auflisten -" + message);
							
				        	  
							ErrorPanel erorPanel = new ErrorPanel();
				        	  erorPanel.setErrorMessage(message);

						}
				});
		
			}
		});
				/*answerDriver.flush().fire(new Receiver<Void>() {
			
	          @Override
	          public void onSuccess(Void response) {
	        	  Log.info("fullSaved");
	        	  
	        		initAnswerView();
	        		answerDialogbox.close();
	          //	goTo(new PlaceAssesment(person.stableId()));
	          }
	          
	          public void onFailure(ServerFailure error){
	        	  ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
	          @Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antworten auflisten -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);

				}
	      }); */
	}
	

	@Override
	public void cancelAnswerClicked() {
		
		
	}

	@Override
	public void deleteAnswerClicked(AnswerProxy answer) {
		
		

		requests.answerRequest().eliminateAnswer().using(answer).fire(new Receiver<Void>(){

			@Override
			public void onSuccess(Void arg0) {
				initAnswerView();
				
			}
	          public void onFailure(ServerFailure error){
	        	  ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
	          @Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort löschen -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);
					

					
				}
		});
		
	}

	@Override
	public void deleteSelectedQuestionResource(final Long qestionResourceId) {
		requests.questionResourceRequest().removeSelectedQuestionResource(qestionResourceId).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
			
				
			}
		});
		
	}

	@Override
	public void addNewQuestionResource(final QuestionResourceClient questionResourceClient) {

		final QuestionResourceRequest request  = requests.questionResourceRequest();
		
		QuestionResourceProxy proxy = request.create(QuestionResourceProxy.class);
		
		proxy.setPath(questionResourceClient.getPath());
		proxy.setQuestion(question);
		proxy.setSequenceNumber(questionResourceClient.getSequenceNumber());
		proxy.setType(questionResourceClient.getType());
		
		final QuestionResourceProxy proxy2 = proxy; 
		request.persist().using(proxy).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("Added new Question Resource");
				
				requests.questionResourceRequest().find(proxy2.stableId()).fire(new Receiver<Object>(){

					@Override
					public void onSuccess(Object response) {
						QuestionResourceProxy resourceProxy = (QuestionResourceProxy) response;
						questionResourceClient.setId(resourceProxy.getId());
					}
				});
			}
		});
	}

	@Override
	public void updatePicturePathInQuestion(String picturePath) {
		
		if(question != null) {
			final QuestionRequest questionRequest = requests.questionRequest();
			
			QuestionProxy updatedQuestion = questionRequest.edit(question);
			updatedQuestion.setPicturePath(picturePath);
			questionRequest.persist().using(updatedQuestion).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("Picture path updated.");
				}
				
				@Override
				public void onConstraintViolation(
						Set<ConstraintViolation<?>> violations) {
					Log.error(violations.toString());
					super.onConstraintViolation(violations);
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
					super.onFailure(error);
				}
			});
	
		}else {
			Log.error("Question is null");
		}
	}

	@Override
	public void deleteUploadedPicture(String picturePath) {
		
		if(question != null) {
			final QuestionRequest questionRequest = requests.questionRequest();
			questionRequest.deleteMediaFileFromDisk(picturePath).fire(new Receiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					Log.info("Picture deleted " + response);
				}
				
				@Override
				public void onConstraintViolation(
						Set<ConstraintViolation<?>> violations) {
					Log.error(violations.toString());
					super.onConstraintViolation(violations);
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
					super.onFailure(error);
				}
			});
		}else {
			Log.error("Question is null");
		}
	}

	@Override
	public void deleteUploadedFiles(Set<String> paths) {
		
		requests.questionResourceRequest().deleteFiles(paths).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("Files area deleted");
				
			}
			
			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				Log.info("ConstraintViolation in files delete process");
				super.onConstraintViolation(violations);
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Log.info("error in files delete process");
				super.onFailure(error);
			}
		});
		
	}

	@Override
	public void changedResourceSequence(Set<QuestionResourceClient> questionResourceClients) {
		
		if(question != null && questionResourceClients.size() > 0) {
		
			QuestionResourceRequest questionResourceRequest = requests.questionResourceRequest();
			Set<QuestionResourceProxy> proxies = Sets.newHashSet();
			Log.info("proxies.size() " + proxies.size());
			for (QuestionResourceClient questionResource : questionResourceClients) {

				if(questionResource.getState().equals(State.NEW) || questionResource.getState().equals(State.EDITED)) {
					QuestionResourceProxy proxy = questionResourceRequest.create(QuestionResourceProxy.class);
					proxy.setPath(questionResource.getPath());
					proxy.setSequenceNumber(questionResource.getSequenceNumber());
					proxy.setType(questionResource.getType());
					proxy.setQuestion(question);
					proxies.add(proxy);
				}
			}
			
			questionResourceRequest.persistSet(proxies).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("sequence changed successfuly");
				}
				
				@Override
				public void onConstraintViolation(
						Set<ConstraintViolation<?>> violations) {
					
					Iterator<ConstraintViolation<?>> iter = violations.iterator();
					String message = "";
					while(iter.hasNext()){
						ConstraintViolation<?> v = iter.next();
						message += v.getPropertyPath() + " : " + v.getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
					
					ErrorPanel erorPanel = new ErrorPanel();
		        	erorPanel.setWarnMessage(message);
		        	super.onConstraintViolation(violations);
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					ErrorPanel erorPanel = new ErrorPanel();
		        	erorPanel.setErrorMessage(error.getMessage());
		        	super.onFailure(error);
				}
			});

		}
	}
}

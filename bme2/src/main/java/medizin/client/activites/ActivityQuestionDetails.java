package medizin.client.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.proxy.UserAccessRightsProxy;
import medizin.client.request.AnswerRequest;
import medizin.client.request.CommentRequest;
import medizin.client.request.MatrixValidityRequest;
import medizin.client.request.QuestionRequest;
import medizin.client.request.QuestionResourceRequest;
import medizin.client.shared.AccessRights;
import medizin.client.shared.Validity;
import medizin.client.ui.view.question.AnswerDialogbox;
import medizin.client.ui.view.question.AnswerDialogboxImpl;
import medizin.client.ui.view.question.AnswerListView;
import medizin.client.ui.view.question.AnswerListViewImpl;
import medizin.client.ui.view.question.MatrixAnswerListView;
import medizin.client.ui.view.question.MatrixAnswerView;
import medizin.client.ui.view.question.MatrixAnswerViewImpl;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.client.util.Matrix;
import medizin.client.util.MatrixValidityVO;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;



public class ActivityQuestionDetails extends AbstractActivityWrapper implements 
	QuestionDetailsView.Delegate, QuestionDetailsView.Presenter, AnswerDialogbox.Delegate, 
	 AnswerListView.Delegate, MatrixAnswerView.Presenter , MatrixAnswerView.Delegate, 
	 MatrixAnswerListView.Delegate, MatrixAnswerListView.Presenter{

	private AcceptsOneWidget widget;
	//private QuestionDetailsView view;
	protected McAppRequestFactory requests;
	private PlaceController placeController;
	private PlaceQuestionDetails questionPlace;
	
	protected QuestionProxy question;
	private QuestionDetailsView questionDetailsView;
	protected QuestionDetailsView view;
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
	
	/*private PersonProxy loggedUser;*/
	private HandlerRegistration answerRangeChangeHandler;
	private EventBus eventBus;
	
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	Boolean flag = false;
	Boolean answerFlag = false;
	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {
		startForAccessRights();
		/*if (!questionPlace.getFromPlace().equals("ACCEPT_QUESTION"))
		{
			if (personRightProxy.getIsAdmin())
			{
				flag = true;
				answerFlag = true;
			}
			else if (personRightProxy.getIsInstitutionalAdmin())
			{
				flag = true;
				answerFlag = true;
			}
			else
			{
				for (UserAccessRightsProxy proxy : personRightProxy.getQuestionEventAccList())
				{
					if (proxy.getAccRights().equals(AccessRights.AccWrite))
					{
						flag = true;
						answerFlag = false;
						break;
					}
				}
			}
		}*/		
		
		QuestionDetailsViewImpl questionDetailsView = new QuestionDetailsViewImpl(eventBus, flag,true);
		
		/*questionDetailsView.setName("hallo");*/
		questionDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = questionDetailsView;
		this.eventBus = eventBus;
        widget.setWidget(questionDetailsView.asWidget());
		//setTable(view.getTable());
        
		/*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});*/
		//init();
		
		view.setDelegate(this);
		
		startForAcceptQuestion();
		
		/*if(questionPlace.getFromPlace().equals("ACCEPT_QUESTION"))
		{
			view.getAnswerListViewImpl().setVisible(false);
			view.setVisibleAcceptButton();
		}*/
	
		this.answerListView = view.getAnswerListViewImpl();
		answerListView.setDelegate(this);
		this.answerTable = answerListView.getTable();
		
		
		start2();
		
		/*requests.personRequest().myGetLoggedPerson()
				.fire(new BMEReceiver<PersonProxy>() {

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
					
					@Override
					public void onReceiverFailure() {
						onStop();
					}

				});*/

	}
	private void start2(){
		/*if(loggedUser==null) return;*/
		if(userLoggedIn==null) return;
		
		requests.find(questionPlace.getProxyId()).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<Object>() {

			/*public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}*/
			@Override
			public void onSuccess(final Object response) {
				if(response instanceof QuestionProxy){
					Log.info(((QuestionProxy) response).getQuestionText());
					
					if (((QuestionProxy) response).getIsReadOnly() == true)
						view.setVisibleIconButton(false);
						
					initForActivity((QuestionProxy) response);
					/*if (questionPlace.getFromPlace().equals("ACCEPT_QUESTION"))
					{
						init((QuestionProxy) response);
					}
					else
					{
						if (!flag && !answerFlag)
						{
							if (((QuestionProxy) response).getAutor().getId().equals(userLoggedIn.getId()))
							{
								view.setInvisibleIconButton(true);
								init((QuestionProxy) response);
							}
							else
							{
								requests.userAccessRightsRequest().checkAddAnswerRightsByQuestionAndPerson(userLoggedIn.getId(), ((QuestionProxy) response).getId()).fire(new BMEReceiver<List<UserAccessRightsProxy>>() {

									@Override
									public void onSuccess(List<UserAccessRightsProxy> rightsResponse) {
										
										if (rightsResponse.size() > 0)
										{
											for (UserAccessRightsProxy proxy : rightsResponse)
											{
												if (proxy.getAccRights().equals(AccessRights.AccWrite))
												{
													view.setInvisibleIconButton(true);
													questionDetailsView.getAnswerListViewImpl().getNewAnswer().setVisible(false);
												}
												
												if (proxy.getAccRights().equals(AccessRights.AccAddAnswers))
												{
													view.setInvisibleIconButton(false);
												}	
											}
										}
										else
										{
											view.setInvisibleIconButton(false);
											questionDetailsView.getAnswerListViewImpl().getNewAnswer().setVisible(false);
										}
								
										init((QuestionProxy) response);
									}
								});
							}
						}
						else
						{
							init((QuestionProxy) response);
						}
					}*/
					
					
				}				
			}
			
		    });
	}
	
	protected void startForAcceptQuestion() {
		//do nothing

	}
	//method is overridden by sub class accept question
	protected void startForAccessRights() {

		if (personRightProxy.getIsAdmin())
		{
			flag = true;
			answerFlag = true;
		}
		else if (personRightProxy.getIsInstitutionalAdmin())
		{
			flag = true;
			answerFlag = true;
		}
		else
		{
			for (UserAccessRightsProxy proxy : personRightProxy.getQuestionEventAccList())
			{
				if (proxy.getAccRights().equals(AccessRights.AccWrite))
				{
					flag = true;
					answerFlag = false;
					break;
				}
			}
		}
	
	}
	
	//method is overridden by sub class accept question
	protected void initForActivity(final QuestionProxy response) {
		
		if (response.getStatus().equals(Status.ACTIVE) || response.getStatus().equals(Status.NEW))
		{
			if (!flag && !answerFlag)
			{
				if (((QuestionProxy) response).getAutor().getId().equals(userLoggedIn.getId()))
				{
					view.setVisibleIconButton(true);
					init((QuestionProxy) response);
				}
				else
				{
					requests.userAccessRightsRequest().checkAddAnswerRightsByQuestionAndPerson(userLoggedIn.getId(), ((QuestionProxy) response).getId()).fire(new BMEReceiver<List<UserAccessRightsProxy>>() {

						@Override
						public void onSuccess(List<UserAccessRightsProxy> rightsResponse) {
							
							if (rightsResponse.size() > 0)
							{
								for (UserAccessRightsProxy proxy : rightsResponse)
								{
									if (proxy.getAccRights().equals(AccessRights.AccWrite))
									{
										view.setVisibleIconButton(true);
										questionDetailsView.getAnswerListViewImpl().getNewAnswer().setVisible(false);
									}
									
									if (proxy.getAccRights().equals(AccessRights.AccAddAnswers))
									{
										view.setVisibleIconButton(false);
									}	
								}
							}
							else
							{
								view.setVisibleIconButton(false);
								questionDetailsView.getAnswerListViewImpl().getNewAnswer().setVisible(false);
							}
					
							init((QuestionProxy) response);
						}
					});
				}
			}
			else
			{
				init((QuestionProxy) response);
			}
		}
		else if (response.getStatus().equals(Status.NEW))
		{
			view.getEdit().setVisible(false);
			init((QuestionProxy) response);
		}
	}
	/**
	 * 
	 * @param AssesmentProxy assesment
	 */
	protected void init(QuestionProxy question) {

		this.question = question;
		Log.debug("Details für: "+question.getQuestionText());
		view.setValue(question);	
		
		if (question.getQuestionType().getQuestionType().equals(QuestionTypes.Matrix))
		{
			view.getMatrixAnswerListViewImpl().setDelegate(this);
			view.getAnswerListViewImpl().setVisible(false);
			initMatrixAnswerView();
		}
		else
		{
			view.getMatrixAnswerListViewImpl().setVisible(false);
			initAnswerView();
		}
	}

	private void initAnswerView() {
		
		if (answerRangeChangeHandler!=null){
			answerRangeChangeHandler.removeHandler();
			answerRangeChangeHandler=null;
		}
		
		requests.answerRequest().contAnswersByQuestion(question.getId()).fire( new BMEReceiver<Long>(){


				@Override
				public void onSuccess(Long response) {
					
					answerTable.setRowCount(response.intValue(), true);

					onAnswerTableRangeChanged();
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
		
		
		requests.answerRequest().findAnswersEntriesByQuestion(question.getId(), range.getStart(), range.getLength()).with("question","rewiewer","autor","comment").fire( new BMEReceiver<List<AnswerProxy>>(){


			@Override
			public void onSuccess(List<AnswerProxy> response) {
				
				answerTable.setRowData(range.getStart(), response);

			}
	});
		
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void deleteClicked() {
		requests.questionRequest().deactivatedQuestion().using(question).fire(new BMEReceiver<Void>(reciverMap) {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	placeController.goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION));
            	
            }
           /* @Override
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

				
			}*/
            
        });
		
	}

	//method is overridden by sub class accept question
	@Override
	public void editClicked() {
		
		/*if (questionPlace.getFromPlace().equals("ACCEPT_QUESTION"))
			placeController.goTo(new PlaceQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT, "ACCEPT_QUESTION"));
		else*/
			placeController.goTo(new PlaceQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT));
		
	}

	@Override
	public void newClicked() {
		placeController.goTo(new PlaceQuestionDetails(PlaceQuestionDetails.Operation.CREATE));
		
	}
	
	//private AnswerDialogbox answerDialogbox;
	/*private RequestFactoryEditorDriver<AnswerProxy, AnswerDialogboxImpl> answerDriver;*/
	//private AnswerProxy answerProxy;
	//private CommentProxy commentProxy;

	@Override
	public void addNewAnswerClicked() {
		
		if(question.getQuestionType() != null && QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType()) == true) {
			openMatrixAnswerView(null);
			return;
		}else {
			openAnswerView(null);	
		}
		
		/*answerDialogbox = new AnswerDialogboxImpl(question,eventBus,reciverMap);
		answerDialogbox.setDelegate(this);*/
		
		/*if(userLoggedIn.getIsAdmin() == false) {
			answerDialogbox.getAutherSuggestBox().setSelected(userLoggedIn);
			answerDialogbox.getAutherSuggestBox().setEnabled(false);
		}*/

		
					
		
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
		/*answerDialogbox.setValidityPickerValues(Arrays.asList(Validity.values()));
		answerDialogbox.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                answerDialogbox.setRewiewerPickerValues(values);
	            }
	        });
	        
	       // answerDialogbox.setAutherPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                answerDialogbox.setAutherPickerValues(values,userLoggedIn);
	            }
	        });
	        
	        if(userLoggedIn!=null)
	        {
	        	if(userLoggedIn.getIsAdmin() == false) {
	        		Log.info("1 " + answerDialogbox);
	        		Log.info("1 " + answerDialogbox.getAutherSuggestBox());
	        		Log.info("1 " +loggedUser);
	        		
	        		answerDialogbox.getAutherSuggestBox().setSelected(loggedUser);
	        		//answerDialogbox.getAutherSuggestBox().setSelected(userLoggedIn);
	        		answerDialogbox.getAutherSuggestBox().setEnabled(false);
	        	}
	        }
	        	
	        answerDialogbox.display(question.getQuestionType().getQuestionType());*/

		
	}

	/*@Override
	public void addAnswerClicked() {
		
		final AnswerRequest ansRequest = requests.answerRequest();
		CommentRequest commnetRequest=requests.commentRequest();
		
		final AnswerProxy answerProxy=ansRequest.create(AnswerProxy.class);
		CommentProxy commentProxy=commnetRequest.create(CommentProxy.class);
		answerDialogbox.getRichtTextArea().removeStyleName("higlight_onViolation");
		this.answerProxy = ansProxy;
		this.commentProxy=comProxy;
		
		
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
        answerProxy.setAutor(answerDialogbox.getAutherSuggestBox().getSelected());
        answerProxy.setIsAnswerAcceptedReviewWahrer(false);
        //answerDialogbox.setRichPanelHTML(answerProxy.getAnswerText());
        
        answerProxy.setIsAnswerActive(false);
        
        answerProxy.setIsAnswerAcceptedAdmin(false);
        answerProxy.setStatus(Status.NEW);
        
		answerProxy.setAnswerText(answerDialogbox.getRichtTextHTML());
		answerProxy.setValidity(answerDialogbox.getValidity().getValue());
		commentProxy.setComment(answerDialogbox.getComment().getValue());
		if(answerDialogbox.getSubmitToReviewerComitee().getValue())
		{
			answerProxy.setRewiewer(null);
		}
		else if(answerDialogbox.getReviewerSuggestBox().getSelected() != null)
		{
			answerProxy.setRewiewer(answerDialogbox.getReviewerSuggestBox().getSelected());
		}else {
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.selectReviewerOrComitee());
			return;
		}
		
		if(question.getQuestionType() != null && QuestionTypes.MCQ.equals(question.getQuestionType().getQuestionType()) == false) {
			if(answerDialogbox.getRichtTextArea().getText() == null || answerDialogbox.getRichtTextArea().getText().length() <= 0) {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.answerTextErrorMessage());
				answerDialogbox.getRichtTextArea().addStyleName("higlight_onViolation");
				return;
			}
		}
		
		if(question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) == true ) {
			Log.info("IN ShowInImage Question type");
			if(answerDialogbox != null && answerDialogbox.getImagePolygonViewer() != null && answerDialogbox.getImagePolygonViewer().isValidPolygon()) {
				Log.info("Points : " + answerDialogbox.getImagePolygonViewer().getPoints());
				answerProxy.setPoints(answerDialogbox.getImagePolygonViewer().getPoints());
				Log.info("Points added");	
			}else {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.polygonErrorMessage());
				Log.error("Polygon is not property added. Try again");
				return;
			}
			
		}else if(question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) == true && answerDialogbox.getValidity() != null && Validity.Wahr.equals(answerDialogbox.getValidity().getValue())) {
			Log.info("IN Imgkey Question type");
			answerProxy.setAdditionalKeywords(answerDialogbox.getAdditionalKeywords().getValue());
			
			if(answerDialogbox != null && answerDialogbox.getImageRectangleViewer() != null) {
				Log.info("Points : " + answerDialogbox.getImageRectangleViewer().getPoint());
				answerProxy.setPoints(answerDialogbox.getImageRectangleViewer().getPoint());
				Log.info("Points added");
			}else {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.rectangleErrorMessage());
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
							ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.imageViewerError());
							Log.error("Error in imageview");
							return;
						}
						
						break;
					}
					case Sound:
					{
						if(answerDialogbox.getAudioViewer() != null && answerDialogbox.getAudioViewer().getURL() != null && answerDialogbox.getAudioViewer().getURL().length() > 0) {
							answerProxy.setMediaPath(answerDialogbox.getAudioViewer().getURL());
						}else {
							ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.audioViewerError());
							Log.error("Error in audioview.");
							return;
						}
						break;
					}
					case Video:
					{	
						if(answerDialogbox.getVideoViewer() != null && answerDialogbox.getVideoViewer().getURL() != null && answerDialogbox.getVideoViewer().getURL().length() > 0) {
							answerProxy.setMediaPath(answerDialogbox.getVideoViewer().getURL());
						}else {
							ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.audioViewerError());
							Log.error("Error in videoViewer. Try again");
							return;
						}
						break;
					}
					default:
					{
						ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.unknownMultimediaType());
						Log.error("Error in MultimediaType. Try again");
						return;
					}
					}
				}
						
			}else {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.unknownError());
				Log.error("answerDialogbox is null");
				return;
			}
			
		}else if(question.getQuestionType() != null && QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType()) == true) {
			answerProxy.setAdditionalKeywords(answerDialogbox.getAdditionalKeywords().getValue());
			answerDialogbox.getSequenceNumber().removeStyleName("higlight_onViolation");
			if(ClientUtility.isNumber(answerDialogbox.getSequenceNumber().getValue())) {
				answerProxy.setSequenceNumber(Integer.parseInt(answerDialogbox.getSequenceNumber().getValue(), 10));
			}else {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.sequenceNumberError());
				answerDialogbox.getSequenceNumber().addStyleName("higlight_onViolation");
				Log.info("squence number is not valid number");
				return;
			}
		}
		
		answerProxy.setComment(commentProxy);
		answerProxy.setSubmitToReviewComitee(answerDialogbox.getSubmitToReviewerComitee().getValue());
		Log.info("before save");
		
		commnetRequest.persist().using(commentProxy).fire(new BMEReceiver<Void>(reciverMap) {

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
				
				ansRequest.persist().using(answerProxy).fire(new BMEReceiver<Void>(reciverMap) {

					@Override
					public void onSuccess(Void response) {
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
				answerDriver.flush().fire(new Receiver<Void>() {
			
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
	      }); 
	}*/
	

	@Override
	public void cancelAnswerClicked() {
		
		
	}

	@Override
	public void deleteAnswerClicked(AnswerProxy answer) {

		requests.answerRequest().eliminateAnswer().using(answer).fire(new BMEReceiver<Void>(reciverMap){

			@Override
			public void onSuccess(Void arg0) {
				initAnswerView();
				
			}
		});
		
	}

	@Override
	public void deleteSelectedQuestionResource(final Long qestionResourceId) {
		requests.questionResourceRequest().removeSelectedQuestionResource(qestionResourceId).fire(new BMEReceiver<Void>(reciverMap) {

			@Override
			public void onSuccess(Void response) {
				Log.info("Remove Resource.");				
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
		request.persist().using(proxy).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("Added new Question Resource");
				
				requests.questionResourceRequest().find(proxy2.stableId()).fire(new BMEReceiver<Object>(){

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
			questionRequest.persist().using(updatedQuestion).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					Log.info("Picture path updated.");
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
			questionRequest.deleteMediaFileFromDisk(picturePath).fire(new BMEReceiver<Boolean>(reciverMap) {

				@Override
				public void onSuccess(Boolean response) {
					Log.info("Picture deleted " + response);
				}

			});
		}else {
			Log.error("Question is null");
		}
	}

	@Override
	public void deleteUploadedFiles(Set<String> paths) {
		
		requests.questionResourceRequest().deleteFiles(paths).fire(new BMEReceiver<Void>(reciverMap) {

			@Override
			public void onSuccess(Void response) {
				Log.info("Files area deleted");
				
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
			
			questionResourceRequest.persistSet(proxies).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					Log.info("sequence changed successfuly");
				}
				
				/*@Override
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
				}*/
			});

		}
	}

	@Override
	public void findAllAnswersPoints(Long questionId,final Function<List<String>, Void> function) {
		
		requests.answerRequest().findAllAnswersPoints(questionId).fire(new BMEReceiver<List<String>>() {

			@Override
			public void onSuccess(List<String> polygons) {
		
				function.apply(polygons);
			}
		});

		
	}

	@Override
	public void saveMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy,Matrix<MatrixValidityVO> matrixList, PersonProxy author,PersonProxy rewiewer, Boolean submitToReviewComitee, String comment) {
		
		MatrixValidityRequest validityRequest = requests.MatrixValidityRequest();
		for (MatrixValidityVO vo : matrixList) {
			
			Log.info(vo.toString());

			if(vo.getMatrixValidityProxy() != null) {
				
				MatrixValidityProxy proxy = validityRequest.edit(vo.getMatrixValidityProxy());
				proxy.setValidity(vo.getValidity());
				
				proxy.getAnswerX().setDateChanged(new Date());
				proxy.getAnswerX().setAutor(author);
				proxy.getAnswerX().setRewiewer(rewiewer);
				proxy.getAnswerX().setSubmitToReviewComitee(submitToReviewComitee);
				proxy.getAnswerX().setIsAnswerAcceptedAdmin(false);
				proxy.getAnswerX().setIsAnswerAcceptedReviewWahrer(false);
				//proxy.getAnswerX().setIsAnswerActive(false);
				proxy.getAnswerX().getComment().setComment(comment);
				proxy.getAnswerX().setQuestion(question);
				proxy.getAnswerX().setValidity(vo.getValidity());
				proxy.getAnswerX().setStatus(Status.NEW);
				
				proxy.getAnswerY().setDateChanged(new Date());
				proxy.getAnswerY().setAutor(author);
				proxy.getAnswerY().setRewiewer(rewiewer);
				proxy.getAnswerY().setSubmitToReviewComitee(submitToReviewComitee);
				proxy.getAnswerY().setIsAnswerAcceptedAdmin(false);
				proxy.getAnswerY().setIsAnswerAcceptedReviewWahrer(false);
				//proxy.getAnswerY().setIsAnswerActive(false);
				proxy.getAnswerY().getComment().setComment(comment);
				proxy.getAnswerY().setQuestion(question);
				proxy.getAnswerY().setValidity(vo.getValidity());
				proxy.getAnswerY().setStatus(Status.NEW);
				
				validityRequest.persist().using(proxy);
			}
		}
		
		validityRequest.fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				
				Log.info("save done for matrix validity");
				initMatrixAnswerView();
			}
		});
		
		
	}

	@Override
	public void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords,Integer sequenceNumber, final Function<AnswerProxy, Void> function) {
		
		Log.info("in saveAnswerProxy method");
		final AnswerRequest answerRequest = requests.answerRequest();
		CommentRequest commentRequest = requests.commentRequest();
		
		if(answerProxy != null) {
			AnswerProxy editerAnswerProxy  = answerRequest.edit(answerProxy);
			editerAnswerProxy.setDateChanged(new Date());
			editerAnswerProxy.setAnswerText(answerText);
			editerAnswerProxy.setAutor(author);
			editerAnswerProxy.setRewiewer(rewiewer);
			editerAnswerProxy.setSubmitToReviewComitee(submitToReviewComitee);
			editerAnswerProxy.setIsAnswerAcceptedAdmin(false);
			editerAnswerProxy.setIsAnswerAcceptedReviewWahrer(false);
			//editerAnswerProxy.setIsAnswerActive(false);
			editerAnswerProxy.getComment().setComment(comment);
			editerAnswerProxy.setQuestion(question);
			editerAnswerProxy.setStatus(Status.NEW);
			editerAnswerProxy.setValidity(validity);
			editerAnswerProxy.setPoints(points);
			editerAnswerProxy.setMediaPath(mediaPath);
			editerAnswerProxy.setIsMedia(mediaPath != null && mediaPath.length() > 0);
			editerAnswerProxy.setAdditionalKeywords(additionalKeywords);
			editerAnswerProxy.setSequenceNumber(sequenceNumber);
			/*if(answerProxy.getValidity() != null) {
				editerAnswerProxy.setValidity(answerProxy.getValidity());
			}else {
				editerAnswerProxy.setValidity(Validity.Falsch);
			}*/
			
			final AnswerProxy finalAnswerProxy = editerAnswerProxy;
			answerRequest.persist().using(editerAnswerProxy).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					function.apply(finalAnswerProxy);
					initAnswerView();
				}
			});
		}else {
			AnswerProxy newAnswerProxy = answerRequest.create(AnswerProxy.class);
			CommentProxy commentProxy = commentRequest.create(CommentProxy.class);
			
			newAnswerProxy.setDateAdded(new Date());
			newAnswerProxy.setAnswerText(answerText);
			newAnswerProxy.setAutor(author);
			newAnswerProxy.setRewiewer(rewiewer);
			newAnswerProxy.setSubmitToReviewComitee(submitToReviewComitee);
			newAnswerProxy.setIsAnswerAcceptedAdmin(false);
			newAnswerProxy.setIsAnswerAcceptedReviewWahrer(false);
			//newAnswerProxy.setIsAnswerActive(false);
			newAnswerProxy.setComment(commentProxy);
			commentProxy.setComment(comment);
			newAnswerProxy.setValidity(validity);
			newAnswerProxy.setQuestion(question);
			newAnswerProxy.setPoints(points);
			newAnswerProxy.setMediaPath(mediaPath);
			newAnswerProxy.setIsMedia(mediaPath != null && mediaPath.length() > 0);
			newAnswerProxy.setAdditionalKeywords(additionalKeywords);
			newAnswerProxy.setSequenceNumber(sequenceNumber);
			
			newAnswerProxy.setStatus(Status.NEW);
			
			final AnswerProxy finalAnswerProxy = newAnswerProxy;
			
			commentRequest.persist().using(commentProxy).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					
					answerRequest.persist().using(finalAnswerProxy).fire(new BMEReceiver<Void>(reciverMap) {

						@Override
						public void onSuccess(Void response) {
							
							requests.answerRequest().find(finalAnswerProxy.stableId()).with("autor","rewiewer","comment").fire(new BMEReceiver<Object>() {

								@Override
								public void onSuccess(Object response) {
									if(response instanceof AnswerProxy) {
										function.apply((AnswerProxy) response);	
										initAnswerView();
									}		
								}
							});
								
						}
					});
					
				}
				
			});
		}

	}

	@Override
	public void saveMatrixValidityValue(final MatrixValidityVO matrixValidityVO, Validity validity, final Function<MatrixValidityProxy, Void> function) {
		
		if(matrixValidityVO.getAnswerX().getAnswerProxy() != null && matrixValidityVO.getAnswerY().getAnswerProxy() != null) {
			final MatrixValidityRequest matrixValidityRequest = requests.MatrixValidityRequest();
			MatrixValidityProxy proxy = null;
			if(matrixValidityVO.getMatrixValidityProxy() != null) {
				// update
				proxy = matrixValidityRequest.edit(matrixValidityVO.getMatrixValidityProxy());
				
			}else {
				// update
				proxy = matrixValidityRequest.create(MatrixValidityProxy.class);
			}
			
			proxy.setAnswerX(matrixValidityVO.getAnswerX().getAnswerProxy());
			proxy.setAnswerY(matrixValidityVO.getAnswerY().getAnswerProxy());
			proxy.setValidity(validity);
			final MatrixValidityProxy proxy2 = proxy;
			matrixValidityRequest.persist().using(proxy).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					if(matrixValidityVO.getMatrixValidityProxy() == null) {
						requests.MatrixValidityRequest().find(proxy2.stableId()).with("answerX","answerY","answerX.autor","answerX.rewiewer","answerX.comment","answerY.autor","answerY.rewiewer","answerY.comment").fire(new BMEReceiver<Object>() {

							@Override
							public void onSuccess(Object response) {

								if(response instanceof MatrixValidityProxy) {
									function.apply((MatrixValidityProxy) response);			
								}
							}
						});
						
					}else {
						function.apply(proxy2);		
					}
					initMatrixAnswerView();
				}
			});
			
		}else {
			Log.error("Error in saveMatrixValidityValue method");
		}
		
	}

	@Override
	public void deletedSelectedAnswer(AnswerProxy answerProxy, Boolean isAnswerX,final Function<Boolean, Void> function) {
		requests.MatrixValidityRequest().deleteAnswerAndItsMatrixValidity(answerProxy.getId(),isAnswerX).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				function.apply(response);
			}		
		});
	}

	/*@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {
		QuestionRequest questionRequest = requests.questionRequest();
		proxy = questionRequest.edit(proxy);
		
		if (userLoggedIn.getIsAdmin())
		{
			proxy.setIsAcceptedAdmin(true);
			
			if (proxy.getIsAcceptedRewiever())
			{
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			}
			else
				proxy.setStatus(Status.ACCEPTED_ADMIN);
		}
		else
		{
			proxy.setIsAcceptedRewiever(true);
			
			if (proxy.getIsAcceptedAdmin())
			{
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			}
			else
				proxy.setStatus(Status.ACCEPTED_REVIEWER);
		}
		
		questionRequest.persist().using(proxy).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				placeController.goTo(new PlaceAcceptQuestion(""));
			}
		});
	}*/
	
	

	//method is overridden by sub class accept question
	@Override
	public boolean isQuestionDetailsPlace() {
		return true;
	}

	@Override
	public void getQuestionDetails(QuestionProxy previousVersion, final Function<QuestionProxy, Void> function) {
		
		
		requests.questionRequest().findQuestion(previousVersion.getId()).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources").fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				function.apply((QuestionProxy) response);
			}
		});
		
	}

	@Override
	public void getLatestQuestionDetails(Function<QuestionProxy, Void> function) {
		function.apply(question);
	}
	
	private void initMatrixAnswerView()
	{
		if (answerRangeChangeHandler!=null){
			answerRangeChangeHandler.removeHandler();
			answerRangeChangeHandler=null;
		}
		
		requests.MatrixValidityRequest().countAllMatrixValidityForQuestion(question.getId()).with("answerX","answerY").fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				view.getMatrixAnswerListViewImpl().getTable().setRowCount(response.intValue(), true);
				
				onMatrixAnswerTableRangeChanged();
			}
		});
	
		answerRangeChangeHandler =  view.getMatrixAnswerListViewImpl().getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityQuestionDetails.this.onMatrixAnswerTableRangeChanged();
			}
		});
		
	}
	
	private void onMatrixAnswerTableRangeChanged()
	{
		requests.MatrixValidityRequest().findAllMatrixValidityForQuestion(question.getId()).with("answerX","answerY").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				view.getMatrixAnswerListViewImpl().getTable().setRowData(view.getMatrixAnswerListViewImpl().getTable().getVisibleRange().getStart(), response);
			}
		});
	}

	@Override
	public void addMatrixNewAnswerClicked() {
		
		if(question.getQuestionType() != null && QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType()) == true) {
			
			
			final MatrixAnswerView matrixAnswerView = new MatrixAnswerViewImpl(question);
			matrixAnswerView.setDelegate(this);
			
//			matrixAnswerView.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                matrixAnswerView.setRewiewerPickerValues(values);
	            }
	        });
	        
	       // answerDialogbox.setAutherPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                matrixAnswerView.setAutherPickerValues(values,userLoggedIn);
	            }
	        });
			
			requests.MatrixValidityRequest().findAllMatrixValidityForQuestion(question.getId()).with("answerX","answerY","answerX.autor","answerX.rewiewer","answerX.comment","answerY.autor","answerY.rewiewer","answerY.comment").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

				@Override
				public void onSuccess(List<MatrixValidityProxy> response) {
										
			        matrixAnswerView.setValues(response);
			        matrixAnswerView.display();
				}
			});			
		}
	}

	private AnswerDialogbox openAnswerView(final AnswerProxy answer) {
		
		final AnswerDialogbox answerDialogbox = new AnswerDialogboxImpl(question,eventBus,reciverMap);
		answerDialogbox.setDelegate(this);
		
		// because display need to be called after author and reviewer list.  
		final Function<Void, Void> sync = new Function<Void, Void>() {
			
			private int count = 0;  
			@Override
			public Void apply(Void input) {
				count++;
				if(count == 2) {
					if(answer != null) {
			        	answerDialogbox.setValues(answer);
			        }
			        answerDialogbox.display(question.getQuestionType().getQuestionType());
				}
				return null;
			}
		}; 
		
		
		answerDialogbox.setValidityPickerValues(Arrays.asList(Validity.values()));
		answerDialogbox.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                answerDialogbox.setRewiewerPickerValues(values);
                sync.apply(null);
            }
        });
        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                answerDialogbox.setAutherPickerValues(values,userLoggedIn);
                sync.apply(null);
            }
        });
	      
        Long answerId = answer != null ? answer.getId() : null;
        Long questionId = question!= null ? question.getId() : null;
        if(question != null && question.getQuestionType()!= null && question.getQuestionType().getQuestionType() != null) {
        	if(QuestionTypes.Textual.equals(question.getQuestionType().getQuestionType()) || QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType())) {
        		
        		requests.answerRequest().maxDifferenceBetweenAnswerForQuestion(answerId,questionId).fire(new BMEReceiver<List<Long>>() {

        			@Override
        			public void onSuccess(List<Long> response) {
        				
        				if(response.size() == 2) {
        					long diff1 = response.get(0);
        					long diff2 = response.get(1);
        					if(diff1 > diff2) {
        						answerDialogbox.setMaxDifferenceBetween(diff1,diff2);	
        					}else {
        						answerDialogbox.setMaxDifferenceBetween(diff2,diff1);
        					}
        						
        				}
        				
        			}
        		});
        	}
        }
        return answerDialogbox;
	}
	
	@Override
	public void editAnswerClicked(AnswerProxy answer) {
		openAnswerView(answer);
	}

	private void openMatrixAnswerView(final MatrixValidityProxy matrixValidity) {
		final MatrixAnswerView matrixAnswerView = new MatrixAnswerViewImpl(question);
		matrixAnswerView.setDelegate(this);
				
		matrixAnswerView.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                matrixAnswerView.setRewiewerPickerValues(values);
            }
        });
        
       // answerDialogbox.setAutherPickerValues(Collections.<PersonProxy>emptyList());
        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                matrixAnswerView.setAutherPickerValues(values,userLoggedIn);
            }
        });
		
		requests.MatrixValidityRequest().findAllMatrixValidityForQuestion(question.getId()).with("answerX","answerY","answerX.autor","answerX.rewiewer","answerX.comment","answerY.autor","answerY.rewiewer","answerY.comment").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
									
		        matrixAnswerView.setValues(response);
		        matrixAnswerView.display();
			}
		});
	}
	
	@Override
	public void editMatrixValidityClicked(MatrixValidityProxy matrixValidity) {
		openMatrixAnswerView(matrixValidity);
	}

	@Override
	public void deleteMatrixValidityClicked(MatrixValidityProxy matrixValidity) {
		// TODO delete need to be implemented
		ConfirmationDialogBox.showOkDialogBox("TODO", "Delete functionality is not implemented.");
	}

	@Override
	public void closedMatrixValidityView() {
		initMatrixAnswerView(); // refresh matrix list view.
	}

	// updated by subclass ActivityAcceptQuestionDetails
	@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {
	}

	@Override
	public void placeChanged(Place place) {
		//updateSelection(event.getNewPlace());
		// TODO implement
	}

	// updated by subclass ActivityAcceptQuestionDetails
	@Override
	public void onResendToReviewClicked(QuestionProxy proxy) {
	}
	
	//updated by subclass
	@Override
	public void checkForResendToReview() {		
	}

	@Override
	public void forcedActiveClicked() {
		// not to implement here this is for ActivityNotActivatedQuestionDetails.class
	}
}

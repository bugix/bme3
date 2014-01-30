package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.request.QuestionRequest;
import medizin.client.ui.view.AcceptAnswerSubView;
import medizin.client.ui.view.AcceptAnswerSubViewImpl;
import medizin.client.ui.view.AcceptMatrixAnswerSubView;
import medizin.client.ui.view.AcceptMatrixAnswerSubViewImpl;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.process.AppLoader;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;

public class ActivityAcceptQuestionDetails extends AbstractActivityWrapper implements
						QuestionDetailsView.Delegate, 
						AcceptAnswerSubView.Delegate, 
						AcceptMatrixAnswerSubView.Delegate{

	private PlaceAcceptQuestionDetails place;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private EventBus eventBus;
	private QuestionDetailsViewImpl view;
	private QuestionProxy question;
	private PartActivityQuestionLearningObjective partActivityQuestionLearningObjective;
	private PartActivityQuestionKeyword activityQuestionKeyword;
	
	public ActivityAcceptQuestionDetails(PlaceAcceptQuestionDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.place = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {
		this.widget = panel;
		this.eventBus = eventBus;
		
		getQuestionDetails();
	}
	
	private void getQuestionDetails(){
		if(userLoggedIn==null) return;
		
		requests.find(place.getProxyId()).with("previousVersion","keywords","questEvent","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<Object>() {
			
			@Override
			public void onSuccess(final Object response) {
				
				if(response instanceof QuestionProxy){
					
					initDetailsView((QuestionProxy) response);
							
					init((QuestionProxy) response);
				}				
			}
		});
	}

	public void initDetailsView(QuestionProxy questionProxy) {
		QuestionDetailsViewImpl questionDetailsView = new QuestionDetailsViewImpl(eventBus, true,hasAnswerWriteRights(questionProxy, null),hasAnswerAddRights(questionProxy),false,true,isQuestionTypeMCQ(questionProxy), true,true, false);
		this.view = questionDetailsView;
        widget.setWidget(questionDetailsView.asWidget());
		view.setDelegate(this);
		view.setVisibleAcceptButton();
		view.getAnswerListViewImpl().removeFromParent();
		view.getMatrixAnswerListViewImpl().removeFromParent();
		view.removeQuestionUsedInMCTab();
		
		activityQuestionKeyword = new PartActivityQuestionKeyword(requests, view.getQuestionKeywordView(),false);
		activityQuestionKeyword.setQuestionProxy(questionProxy);
		view.getQuestionKeywordView().setDelegate(activityQuestionKeyword);
		partActivityQuestionLearningObjective = new PartActivityQuestionLearningObjective(requests, view.getQuestionLearningObjectiveSubViewImpl(),false);
		view.getQuestionLearningObjectiveSubViewImpl().setDelegate(partActivityQuestionLearningObjective);
		partActivityQuestionLearningObjective.setQuestionProxy(questionProxy);
	}
	
	private boolean isQuestionTypeMCQ(QuestionProxy questionProxy) {
		return questionProxy != null && questionProxy.getQuestionType() != null && QuestionTypes.MCQ.equals(questionProxy.getQuestionType().getQuestionType());
	}

	private void init(QuestionProxy question) {

		this.question = question;
		Log.debug("Details für: "+question.getQuestionText());
		view.setValue(question);	
		
		if (question.getQuestionType() != null && QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType()))
			initMatrixAnswerView();
		else
			initAnswerView();
		
		checkForResendToReview();
	}
	
	@Override
	public void editClicked() {
		goTo(new PlaceAcceptQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT, place.getHeight()));
	}
	
	@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {
		/*QuestionRequest questionRequest = requests.questionRequest();
		proxy = questionRequest.edit(proxy);

		if (userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin()) {
			proxy.setIsAcceptedAdmin(true);

			if (proxy.getIsAcceptedRewiever()) {
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			} else
				proxy.setStatus(Status.ACCEPTED_ADMIN);
		} else if (proxy.getRewiewer().getId().equals(userLoggedIn.getId())) {
			proxy.setIsAcceptedRewiever(true);

			if (proxy.getIsAcceptedAdmin()) {
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			} else
				proxy.setStatus(Status.ACCEPTED_REVIEWER);
		}else if (proxy.getAutor().getId().equals(userLoggedIn.getId())) {
			
			if(proxy.getStatus().equals(Status.CORRECTION_FROM_ADMIN)) {
				proxy.setIsAcceptedAdmin(true);
				proxy.setStatus(Status.ACCEPTED_ADMIN);	
			}else if (proxy.getStatus().equals(Status.CORRECTION_FROM_REVIEWER)) {
				proxy.setIsAcceptedRewiever(true);
				proxy.setStatus(Status.ACCEPTED_REVIEWER);	
			}else if (proxy.getStatus().equals(Status.ACCEPTED_ADMIN)) {
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			}else if (proxy.getStatus().equals(Status.ACCEPTED_REVIEWER)) {
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			}
		}
		else if (proxy.getAutor().getId().equals(userLoggedIn.getId())
				&& (proxy.getStatus().equals(Status.CORRECTION_FROM_ADMIN) || proxy
						.getStatus().equals(Status.CORRECTION_FROM_REVIEWER))) {
			proxy.setIsAcceptedAdmin(true);
			proxy.setIsAcceptedRewiever(true);
			proxy.setIsActive(true);
			proxy.setStatus(Status.ACTIVE);
		}

		questionRequest.persist().using(proxy).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				goTo(new PlaceAcceptQuestion(""));
			}
		});*/
		AppLoader.setNoLoader();
		requests.questionRequest().questionAccepted(question, (userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION,place.getHeight()));				
			}
		});
	}
	
	private void initAnswerView()
	{
		AcceptAnswerSubView acceptAnswerSubView = new AcceptAnswerSubViewImpl(false, false);				
	    acceptAnswerSubView.setDelegate(ActivityAcceptQuestionDetails.this);
	    acceptAnswerSubView.setProxy(question);
	    acceptAnswerSubView.getQuestionDisclosurePanel().setOpen(true);
	    
	    view.getAnswerVerticalPanel().add(acceptAnswerSubView);
	}
	
	private void initMatrixAnswerView()
	{
		AcceptMatrixAnswerSubView matrixAnswerSubView = new AcceptMatrixAnswerSubViewImpl(false, false);
		matrixAnswerSubView.setDelegate(ActivityAcceptQuestionDetails.this);
		matrixAnswerSubView.setProxy(question);
		matrixAnswerSubView.getQuestionDisclosurePanel().setOpen(true);
		
		view.getAnswerVerticalPanel().add(matrixAnswerSubView);
	}

	@Override
	public void onRangeChanged(final QuestionProxy questionProxy, final AbstractHasData<AnswerProxy> table) {
	
		final Range range = table.getVisibleRange();
		AppLoader.setNoLoader();
		requests.answerRequest().countAnswerForAcceptQuestion(questionProxy.getId()).with("question", "autor", "rewiewer").fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
			
				if (view == null) {
					return;
				}
				
				table.setRowCount(response.intValue(), true);
				
				findAnswersEntriesNonAcceptedAdminByQuestion(questionProxy.getId(), range.getStart(), range.getLength(), table);
			}
		});
	}
	
	void findAnswersEntriesNonAcceptedAdminByQuestion(Long questionId, final Integer start, Integer length, final AbstractHasData<AnswerProxy> table){
		
		AppLoader.setNoLoader();
		requests.answerRequest().findAnswerForAcceptQuestion(questionId, start, length).with("rewiewer","autor", "question", "question.questionType").fire(new BMEReceiver<List<AnswerProxy>>() {
			@Override
			public void onSuccess(List<AnswerProxy> response) {
				if (view == null) {
					return;
				}
				table.setRowData(start, response);
			}
		});
	}

	@Override
	public void onMatrixRangeChanged(final QuestionProxy questionProxy, final AbstractHasData<MatrixValidityProxy> table, final AcceptMatrixAnswerSubView matrixAnswerListView) {
		final Range range = table.getVisibleRange();
		AppLoader.setNoLoader();
		requests.MatrixValidityRequest().countAllMatrixValidityForAcceptQuestion(questionProxy.getId()).fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				if (view == null)
				{
					return;
				}
				
				table.setRowCount(response.intValue(), true);
				
				findMatrixAnswersEntriesNonAcceptedAdminByQuestion(questionProxy.getId(), range.getStart(), range.getLength(), table, matrixAnswerListView);
			}
		});
	}

	void findMatrixAnswersEntriesNonAcceptedAdminByQuestion(final Long questionId, final Integer start, final Integer length, final AbstractHasData<MatrixValidityProxy> table, final AcceptMatrixAnswerSubView matrixAnswerListView){
		AppLoader.setNoLoader();
		requests.MatrixValidityRequest().findAllMatrixValidityForQuestion(questionId).with("answerX", "answerY", "answerX.rewiewer","answerX.autor", "answerX.question", "answerX.question.questionType", "answerY.rewiewer","answerY.autor", "answerY.question", "answerY.question.questionType").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				matrixAnswerListView.setMatrixAnswerList(response);
				
				requests.MatrixValidityRequest().findAllMatrixValidityForAcceptQuestion(questionId, start, length).with("answerX", "answerY", "answerX.rewiewer","answerX.autor", "answerX.question", "answerX.question.questionType", "answerY.rewiewer","answerY.autor", "answerY.question", "answerY.question.questionType").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

					@Override
					public void onSuccess(List<MatrixValidityProxy> response) {
						table.setRowData(start, response);
					}
				});
			}
		});
		
		
	}
	
	@Override
	public void onResendToReviewClicked(QuestionProxy proxy) {
		/*requests.questionRequest().questionResendToReviewWithMajorVersion((userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())).using(question).fire(new BMEReceiver<QuestionProxy>() {

			@Override
			public void onSuccess(QuestionProxy response) {
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
			}
		});*/

		// now the question with resend with minor version change as major version is updated by editing the question.
		Status status = question.getStatus();
		boolean isAcceptedAdmin =  false;
		boolean isAcceptedReviewer = false;
		boolean isAcceptedAuthor = false;
		
		if(isAdminOrInstitutionalAdmin() == true) {
			status = Status.CORRECTION_FROM_ADMIN;
			isAcceptedAdmin = true;
			updateQuestionStatusAndFlags(status, isAcceptedAdmin, isAcceptedReviewer, isAcceptedAuthor);
		}else if(userLoggedIn.getId().equals(question.getRewiewer().getId()) == true){
			status = Status.CORRECTION_FROM_REVIEWER;
			isAcceptedReviewer = true;
			updateQuestionStatusAndFlags(status, isAcceptedAdmin, isAcceptedReviewer, isAcceptedAuthor);
		}else if(userLoggedIn.getId().equals(question.getAutor().getId()) == true) {
			Log.error("Author cannnot see this button.");
		}else {
			Log.error("Error in logic");
		}
		
	}

	private void updateQuestionStatusAndFlags(final Status status, final boolean isAcceptedAdmin, final boolean isAcceptedReviewer, final boolean isAcceptedAuthor) {
		QuestionRequest questionRequest = requests.questionRequest();
		QuestionProxy editedQuestion = questionRequest.edit(question);
		editedQuestion.setStatus(status);
		editedQuestion.setIsAcceptedAdmin(isAcceptedAdmin);
		editedQuestion.setIsAcceptedAuthor(isAcceptedAuthor);
		editedQuestion.setIsAcceptedRewiever(isAcceptedReviewer);
		AppLoader.setNoLoader();
		questionRequest.persist().using(editedQuestion).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION,place.getHeight()));
			}
		});
	}
	
	// check for resendToReview
	@Override
	public void checkForResendToReview() {
		
		if(question != null && userLoggedIn != null) {
			
			if(Status.EDITED_BY_ADMIN.equals(question.getStatus()) == true) {
				if((userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())) {
					view.getResendToReviewBtn().setVisible(true);
					view.getEdit().setVisible(true);
					view.getAcceptBtn().removeFromParent();
					view.getAcceptQueAnswer().removeFromParent();
				}else {
					view.getResendToReviewBtn().removeFromParent();
					view.getAcceptQueAnswer().removeFromParent();
					view.getAcceptBtn().removeFromParent();
					view.getEdit().removeFromParent();
				}
			} else if(Status.EDITED_BY_REVIEWER.equals(question.getStatus()) == true) {
				if(question.getRewiewer() != null && userLoggedIn.getId().equals(question.getRewiewer().getId()) == true) {
					view.getResendToReviewBtn().setVisible(true);
					view.getEdit().setVisible(true);
					view.getAcceptBtn().removeFromParent();
					view.getAcceptQueAnswer().removeFromParent();
				}else {
					view.getResendToReviewBtn().removeFromParent();
					view.getAcceptBtn().removeFromParent();
					view.getEdit().removeFromParent();
					view.getAcceptQueAnswer().removeFromParent();
				}
			} else if(question.getSubmitToReviewComitee() == true) {
				//TODO for review committee
				view.getResendToReviewBtn().removeFromParent(); // need to remove.
			} else {
				view.getResendToReviewBtn().removeFromParent();
			}
			
		}
	}
	
	@Override
	public void enableBtnOnLatestClicked() {
			
		if(question.getStatus().equals(Status.EDITED_BY_REVIEWER) || question.getStatus().equals(Status.EDITED_BY_ADMIN)) {
			checkForResendToReview();
			//view.getEdit().setVisible(true);
		}else {
			view.setVisibleAcceptButton();
		}	
	}
	
	
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void getQuestionDetails(QuestionProxy previousVersion, final Function<QuestionProxy, Void> function) {
		
		AppLoader.setCurrentLoader(view.getLoadingPopup());
		requests.questionRequest().findQuestion(previousVersion.getId()).with("previousVersion","keywords","questEvent","questionType","mcs", "rewiewer", "autor","questionResources").fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				function.apply((QuestionProxy) response);
			}
		});
	}

	@Override
	public QuestionProxy getLatestQuestionDetails() {
		AppLoader.setCurrentLoader(view.getLoadingPopup());
		return question;
	}
	
	@Override
	public void placeChanged(Place place) {
		// TODO Auto-generated method stub
		
	}
	
	//unused method
	@Override
	public void forceMatrixAcceptClicked(QuestionProxy questionProxy) {}

	//unused method
	@Override
	public void forcedAcceptClicked(AnswerProxy answerProxy,AcceptAnswerSubView acceptAnswerSubView) {}
	
	@Override
	public void forcedActiveClicked() {}
	
	@Override
	public void deleteClicked() {}

	// unused method
	@Override
	public void matrixAcceptClicked(QuestionProxy questionProxy) {}

	// unused method
	@Override
	public void matrixRejectClicked(QuestionProxy questionProxy) {}

	// unused method
	@Override
	public void acceptClicked(AnswerProxy answerProxy, AcceptAnswerSubView acceptAnswerSubView) {}

	// unused method
	@Override
	public void rejectClicked(AnswerProxy answerProxy) {}

	@Override
	public void acceptQueAnswersClicked() {
		AppLoader.setNoLoader();
		requests.questionRequest().acceptQuestionAndAllAnswers(question.getId(),isAdminOrInstitutionalAdmin()).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if(response == null ||  response == false) {
					ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.notResponsiblePerson());
				} else {
					goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION,place.getHeight()));
				}
			}
		});
		
	}

	@Override
	public void pushToReviewProcessClicked() {}

}

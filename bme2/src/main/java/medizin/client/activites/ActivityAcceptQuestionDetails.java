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
import medizin.client.ui.view.AcceptAnswerSubView;
import medizin.client.ui.view.AcceptAnswerSubViewImpl;
import medizin.client.ui.view.AcceptMatrixAnswerSubView;
import medizin.client.ui.view.AcceptMatrixAnswerSubViewImpl;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.view.client.Range;

public class ActivityAcceptQuestionDetails extends ActivityQuestionDetails implements AcceptAnswerSubView.Delegate, AcceptMatrixAnswerSubView.Delegate{

	public ActivityAcceptQuestionDetails(PlaceAcceptQuestionDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(createNewPlace(place), requests, placeController);
	}

	private static PlaceQuestionDetails createNewPlace(PlaceAcceptQuestionDetails place) {
		PlaceQuestionDetails details;
		
		if(place.getOperation() != PlaceAcceptQuestionDetails.Operation.CREATE) {
			details = new PlaceQuestionDetails(place.getProxyId(),place.getOperation());	
		}else {
			details = new PlaceQuestionDetails(place.getOperation());
		}
		
		return details;
	}



	@Override
	protected void startForAccessRights() {
		// do nothing
	}
	
	@Override
	protected void startForAcceptQuestion() {
		view.getAnswerListViewImpl().setVisible(false);
		view.setVisibleAcceptButton();
		view.getMatrixAnswerListViewImpl().setVisible(false);
	}
	
	@Override
	protected void initForActivity(QuestionProxy response) {
		//init((QuestionProxy) response);
		this.question = response;
		Log.debug("Details für: "+question.getQuestionText());
		view.setValue(question);	
		
		if (question.getQuestionType() != null && QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType()))
			initMatrixAnswerView();
		else
			initAnswerView();
		
	}
	
	@Override
	public void editClicked() {
		goTo(new PlaceAcceptQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT));
	}
	
	@Override
	public boolean isQuestionDetailsPlace() {
		return false;
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
		
		requests.questionRequest().questionAccepted(question, (userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				goTo(new PlaceAcceptQuestion(""));				
			}
		});
	}
	
	private void initAnswerView()
	{
		AcceptAnswerSubView acceptAnswerSubView = new AcceptAnswerSubViewImpl(false);				
	    acceptAnswerSubView.setDelegate(ActivityAcceptQuestionDetails.this);
	    acceptAnswerSubView.setProxy(question);
	    acceptAnswerSubView.getQuestionDisclosurePanel().setOpen(true);
	    
	    view.getAnswerVerticalPanel().add(acceptAnswerSubView);
	}
	
	private void initMatrixAnswerView()
	{
		AcceptMatrixAnswerSubView matrixAnswerSubView = new AcceptMatrixAnswerSubViewImpl(false);
		matrixAnswerSubView.setDelegate(ActivityAcceptQuestionDetails.this);
		matrixAnswerSubView.setProxy(question);
		matrixAnswerSubView.getQuestionDisclosurePanel().setOpen(true);
		
		view.getAnswerVerticalPanel().add(matrixAnswerSubView);
	}

	@Override
	public void onRangeChanged(final QuestionProxy questionProxy, final AbstractHasData<AnswerProxy> table) {
	
		final Range range = table.getVisibleRange();

		requests.answerRequest().countAnswerForAcceptQuestion(questionProxy.getId()).with("question", "autor", "rewiewer").fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
			
				if (view == null) {
					return;
				}
				
				table.setRowCount(response.intValue());
				
				findAnswersEntriesNonAcceptedAdminByQuestion(questionProxy.getId(), range.getStart(), range.getLength(), table);
			}
		});
	}
	
	void findAnswersEntriesNonAcceptedAdminByQuestion(Long questionId, Integer start, Integer length, final AbstractHasData<AnswerProxy> table){
		
		requests.answerRequest().findAnswerForAcceptQuestion(questionId, start, length).with("rewiewer","autor", "question", "question.questionType").fire(new BMEReceiver<List<AnswerProxy>>() {
			@Override
			public void onSuccess(List<AnswerProxy> response) {
				if (view == null) {
					return;
				}
				table.setRowData(response);
			}
		});
	}

	@Override
	public void onMatrixRangeChanged(final QuestionProxy questionProxy, final AbstractHasData<MatrixValidityProxy> table, final AcceptMatrixAnswerSubView matrixAnswerListView) {
		final Range range = table.getVisibleRange();
		
		requests.MatrixValidityRequest().countAllMatrixValidityForAcceptQuestion(questionProxy.getId()).fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				if (view == null)
				{
					return;
				}
				
				table.setRowCount(response.intValue());
				
				findMatrixAnswersEntriesNonAcceptedAdminByQuestion(questionProxy.getId(), range.getStart(), range.getLength(), table, matrixAnswerListView);
			}
		});
	}

	void findMatrixAnswersEntriesNonAcceptedAdminByQuestion(Long questionId, Integer start, Integer length, final AbstractHasData<MatrixValidityProxy> table, final AcceptMatrixAnswerSubView matrixAnswerListView){
		
		requests.MatrixValidityRequest().findAllMatrixValidityForAcceptQuestion(questionId, start, length).with("answerX", "answerY", "answerX.rewiewer","answerX.autor", "answerX.question", "answerX.question.questionType", "answerY.rewiewer","answerY.autor", "answerY.question", "answerY.question.questionType").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				matrixAnswerListView.setMatrixAnswerList(response);
				table.setRowData(response);
			}
		});
	}

	// unused method
	@Override
	public void matrixAcceptClicked(QuestionProxy questionProxy) {
	}

	// unused method
	@Override
	public void matrixRejectClicked(QuestionProxy questionProxy) {
	}

	// unused method
	@Override
	public void acceptClicked(AnswerProxy answerProxy) {
	}

	// unused method
	@Override
	public void rejectClicked(AnswerProxy answerProxy) {
	}
	
	@Override
	public void onResendToReviewClicked(QuestionProxy proxy) {
		requests.questionRequest().questionResendToReviewWithMajorVersion((userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())).using(question).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
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
					view.getAcceptBtn().setVisible(false);
				}else {
					view.getResendToReviewBtn().setVisible(false);
					view.getAcceptBtn().setVisible(false);
					view.getEdit().setVisible(false);
				}
			}else if(Status.EDITED_BY_REVIEWER.equals(question.getStatus()) == true) {
				if(question.getRewiewer() != null && userLoggedIn.getId().equals(question.getRewiewer().getId()) == true) {
					view.getResendToReviewBtn().setVisible(true);
					view.getAcceptBtn().setVisible(false);
				}else {
					view.getResendToReviewBtn().setVisible(false);
					view.getAcceptBtn().setVisible(false);
					view.getEdit().setVisible(false);
				}
			}else {
				//TODO for review committee 
			}
			
		}
	}
}

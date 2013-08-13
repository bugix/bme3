package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.request.QuestionRequest;
import medizin.client.ui.view.AcceptAnswerSubView;
import medizin.client.ui.view.AcceptAnswerSubViewImpl;
import medizin.client.ui.view.AcceptMatrixAnswerSubView;
import medizin.client.ui.view.AcceptMatrixAnswerSubViewImpl;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
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
	protected void initForActivity(QuestionProxy response,Function<Boolean, Void> function) {
		//init((QuestionProxy) response);
		this.question = response;
		Log.debug("Details für: "+question.getQuestionText());
		view.setValue(question);	
		
		if (question.getQuestionType() != null && QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType()))
			initMatrixAnswerView();
		else
			initAnswerView();	
		
		initKeywordView();
	}
	
	/*private void initKeywordView() {
		requests.keywordRequest().findAllKeywords().fire(new BMEReceiver<List<KeywordProxy>>() {

			@Override
			public void onSuccess(List<KeywordProxy> response) {
				DefaultSuggestOracle<KeywordProxy> suggestOracle1 = (DefaultSuggestOracle<KeywordProxy>) view.getKeywordSuggestBox().getSuggestOracle();
				suggestOracle1.setPossiblilities(response);
				view.getKeywordSuggestBox().setSuggestOracle(suggestOracle1);
				
				view.getKeywordSuggestBox().setRenderer(new AbstractRenderer<KeywordProxy>() {

					@Override
					public String render(KeywordProxy object) {
						return object == null ? "" : object.getName();					
					}
				});
			}
		});
		
		if (question != null && question.getKeywords() != null)
		{
			
			
			requests.keywordRequest().countKeywordByQuestion(question.getId()).fire(new BMEReceiver<Integer>() {

				@Override
				public void onSuccess(Integer response) {
					view.getKeywordTable().setRowCount(response);
					ActivityAcceptQuestionDetails.this.onKeywordTableRangeChanged();
				}
			});
			
			view.getKeywordTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					ActivityAcceptQuestionDetails.this.onKeywordTableRangeChanged();
				}
			});
		}
		
	}
*/	
	public void onKeywordTableRangeChanged()
	{
		final Range range = view.getKeywordTable().getVisibleRange();
		
		requests.keywordRequest().findKeywordByQuestion(question.getId(), range.getStart(), range.getLength()).fire(new BMEReceiver<List<KeywordProxy>>() {

			@Override
			public void onSuccess(List<KeywordProxy> response) {
				view.getKeywordTable().setRowData(range.getStart(), response);
			}
		});
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
				goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));				
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
	public void acceptClicked(AnswerProxy answerProxy, AcceptAnswerSubView acceptAnswerSubView) {
	}

	// unused method
	@Override
	public void rejectClicked(AnswerProxy answerProxy) {
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
		
		questionRequest.persist().using(editedQuestion).fire(new BMEReceiver<Void>() {

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
	
	@Override
	public void enableBtnOnLatestClicked() {
			
		if(question.getStatus().equals(Status.EDITED_BY_REVIEWER) || question.getStatus().equals(Status.EDITED_BY_ADMIN)) {
			checkForResendToReview();
			//view.getEdit().setVisible(true);
		}else {
			view.setVisibleAcceptButton();
		}	
	}
	
	//unused method
	@Override
	public void forceMatrixAcceptClicked(QuestionProxy questionProxy) {
	}

	//unused method
	@Override
	public void forcedAcceptClicked(AnswerProxy answerProxy,AcceptAnswerSubView acceptAnswerSubView) {
	}
	
/*	@Override
	public void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy) {
		
		requests.keywordRequest().deleteKeywordFromQuestion(keyword, proxy).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<QuestionProxy>() {

			@Override
			public void onSuccess(QuestionProxy response) {
				view.setValue(response);
				question = response;
				view.getKeywordSuggestBox().getTextField().setText("");
				initForActivity(question, new Function<Boolean, Void>() {
					@Override
					public Void apply(Boolean input) {
						//do nothing 
						return null;
					}
				});
			}
		});
	}
	
	@Override
	public void keywordAddButtonClicked(String text, final QuestionProxy proxy) {
		Set<KeywordProxy> keywordProxySet = proxy.getKeywords();
		boolean flag = false;
		for (KeywordProxy keywordProxy : keywordProxySet)
		{
			if (keywordProxy.getName().equals(text))
			{
				flag = true;
				break;
			}
		}
		
		if (flag == false)
		{
			requests.keywordRequest().findKeywordByStringOrAddKeyword(text, proxy).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<QuestionProxy>() {

				@Override
				public void onSuccess(QuestionProxy response) {
					view.setValue(response);
					question = response;
					view.getKeywordSuggestBox().getTextField().setText("");
					initForActivity(question, new Function<Boolean, Void>() {
						@Override
						public Void apply(Boolean input) {
							//do nothing 
							return null;
						}
					});
				}
			});
		}
		else
		{
			view.getKeywordSuggestBox().getTextField().setText("");
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.keywordExist());
		}		
	}*/

}

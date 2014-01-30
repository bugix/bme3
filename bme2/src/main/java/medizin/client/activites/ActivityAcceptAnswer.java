package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.request.AnswerRequest;
import medizin.client.ui.view.AcceptAnswerSubView;
import medizin.client.ui.view.AcceptAnswerSubViewImpl;
import medizin.client.ui.view.AcceptAnswerView;
import medizin.client.ui.view.AcceptAnswerViewImpl;
import medizin.client.ui.view.AcceptMatrixAnswerSubView;
import medizin.client.ui.view.AcceptMatrixAnswerSubViewImpl;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
/**
 * Activity for accepting answers.
 * @author masterthesis
 *
 */
public class ActivityAcceptAnswer extends AbstractActivityWrapper implements AcceptAnswerView.Delegate, AcceptAnswerSubView.Delegate, AcceptMatrixAnswerSubView.Delegate{

	private PlaceAcceptAnswer answerPlace;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AcceptAnswerView view;
	private VerticalPanel questionPanel;
	//protected PersonProxy loggedUser;


	@Inject
	public ActivityAcceptAnswer(PlaceAcceptAnswer place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.answerPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	} 
	
	@Override
	public String mayStop() {
		
		
		return null;
	}

	@Override
	public void onCancel() {
		onStop();

	}

	@Override
	public void onStop() {


	}

	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	
	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {

		AcceptAnswerView acceptAnswerView = new AcceptAnswerViewImpl();

		acceptAnswerView.setDelegate(this);
		this.widget = panel;
		this.view = acceptAnswerView;
        widget.setWidget(acceptAnswerView.asWidget());
        
        questionPanel=view.getQuestionPanel();
       
        acceptAnswerView.setDelegate(this);
       
        
        init();
//    	requests.personRequest().getLoggedPerson()
//		.fire(new Receiver<PersonProxy>() {
//
//			@Override
//			public void onSuccess(PersonProxy response) {
//				loggedUser = response;
//				init();
//
//			}
//
//			public void onFailure(ServerFailure error) {
//				ErrorPanel erorPanel = new ErrorPanel();
//				erorPanel.setErrorMessage(error.getMessage());
//				Log.error(error.getMessage());
//				onStop();
//			}
//
//			@Override
//			public void onViolation(Set<Violation> errors) {
//				Iterator<Violation> iter = errors.iterator();
//				String message = "";
//				while (iter.hasNext()) {
//					message += iter.next().getMessage() + "<br>";
//				}
//				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
//						+ " in Antwort löschen -" + message);
//
//				ErrorPanel erorPanel = new ErrorPanel();
//				erorPanel.setErrorMessage(message);
//				onStop();
//
//			}
//
//		});
	}
	

	
	
	private void init() {

		questionPanel.clear();
		requests.questionRequest().findQuestionsAnswersNonAcceptedAdmin().with("answers", "questionType","questionResources").fire(new BMEReceiver<List<QuestionProxy>>() {
			@Override
			public void onSuccess(List<QuestionProxy> response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				
				for(QuestionProxy proxy : response)
				{
					QuestionProxy questionProxy = proxy;
					
					if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.Matrix))
					{
						AcceptMatrixAnswerSubView matrixAnswerSubView = new AcceptMatrixAnswerSubViewImpl(true, false);
						matrixAnswerSubView.setDelegate(ActivityAcceptAnswer.this);
						matrixAnswerSubView.setProxy(questionProxy);
						questionPanel.add(matrixAnswerSubView);
					}
					else
					{
						AcceptAnswerSubView acceptAnswerSubView = new AcceptAnswerSubViewImpl(true, false, true);				
					    acceptAnswerSubView.setDelegate(ActivityAcceptAnswer.this);
					    acceptAnswerSubView.setProxy(questionProxy);
					    acceptAnswerSubView.setAcceptAnswerSubView(acceptAnswerSubView);
					    questionPanel.add(acceptAnswerSubView);
					}
				}
				
			}
		});

	}
	
	@Override
	public void onRangeChanged(final QuestionProxy questionProxy, final AbstractHasData<AnswerProxy> table) {
		final Range range = table.getVisibleRange();
		
		requests.answerRequest().countAnswersNonAcceptedAdminByQuestion(questionProxy.getId()).with("question", "autor", "rewiewer").fire(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				
				findAnswersEntriesNonAcceptedAdminByQuestion(questionProxy.getId(), range.getStart(), range.getLength(), table);
				
			}
			
		});
		
	}
	void findAnswersEntriesNonAcceptedAdminByQuestion(Long questionId, Integer start, Integer length, final AbstractHasData<AnswerProxy> table){
		requests.answerRequest().findAnswersEntriesNonAcceptedAdminByQuestion(questionId, start, length).with("rewiewer","autor", "question", "question.questionType").fire(new BMEReceiver<List<AnswerProxy>>() {
			@Override
			public void onSuccess(List<AnswerProxy> response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				table.setRowData(response);
				
				
			}
		});
	}
	@Override
	public void acceptClicked(final AnswerProxy answerProxy2, final AcceptAnswerSubView acceptAnswerSubView) {

			AnswerRequest req = requests.answerRequest();
			AnswerProxy answerProxy =  req.edit((AnswerProxy)answerProxy2);
			if(userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin()){
				answerProxy.setIsAnswerAcceptedAdmin(true);
				answerProxy.setStatus(Status.ACCEPTED_ADMIN);
			} 
			if(answerProxy2.getRewiewer() != null && answerProxy2.getRewiewer().getId().equals(userLoggedIn.getId())) {
				answerProxy.setIsAnswerAcceptedReviewWahrer(true);
				answerProxy.setStatus(Status.ACCEPTED_REVIEWER);
			}
			if(answerProxy2.getAutor().getId().equals(userLoggedIn.getId()))	{
				answerProxy.setIsAnswerAcceptedAutor(true);
			}
			
			if(answerProxy.getIsAnswerAcceptedAdmin() == true && answerProxy.getIsAnswerAcceptedAutor() == true && answerProxy.getIsAnswerAcceptedReviewWahrer() == true) {
				answerProxy.setStatus(Status.ACTIVE);
			}
			
			req.persist().using(answerProxy).fire(new BMEReceiver<Void>(){

				@Override
				public void onSuccess(Void arg0) {
					
					if (acceptAnswerSubView != null && acceptAnswerSubView.getTable().getRowCount() > 1)
					{
						acceptAnswerSubView.setProxy(answerProxy2.getQuestion());
					}
					else
					{
						init();
					}
				}
			});
		
	}
	/*@Override
	public void acceptClicked(EntityProxy entityProxy) {
		if(entityProxy instanceof AnswerProxy){
			Log.debug("is AnswerProxy");
			AnswerRequest req = requests.answerRequest();
			AnswerProxy answerProxy =  req.edit((AnswerProxy)entityProxy);
			if(userLoggedIn.getIsAdmin()){
				answerProxy.setIsAnswerAcceptedAdmin(true);
				answerProxy.setIsAnswerActive(true);
				answerProxy.setStatus(Status.ACTIVE);
			} 
			if(answerProxy.getRewiewer().getId() == userLoggedIn.getId()) {
				answerProxy.setIsAnswerAcceptedReviewWahrer(true);
			}
			if(answerProxy.getAutor().getId() == userLoggedIn.getId())
			{
				answerProxy.setIsAnswerAcceptedAutor(true);
			}
			
			
			
			req.persist().using(answerProxy).fire(new BMEReceiver<Void>(){

				@Override
				public void onSuccess(Void arg0) {
					init();
					
				}
			});
		}
		
	}*/
	//not user
	/*@Override
	public void rejectClicked(EntityProxy entityProxy, String message) {
		if(entityProxy instanceof AnswerProxy){
			Log.debug("is Answer");
			AnswerRequest req = requests.answerRequest();
			AnswerProxy answerProxy =  (AnswerProxy)entityProxy;`
			
			req.remove().using(answerProxy).fire(new BMEReceiver<Void>(){
	
				@Override
				public void onSuccess(Void arg0) {
					init();
					
				}
			});
		}
	}*/

	@Override
	public void rejectClicked(AnswerProxy answerProxy) {
		AnswerRequest req = requests.answerRequest();
		answerProxy =  req.edit(answerProxy);
		
		answerProxy.setStatus(Status.DEACTIVATED);
		
		req.persist().using(answerProxy).fire(new BMEReceiver<Void>(){

			@Override
			public void onSuccess(Void arg0) {
				init();
			}
          
		});
	}

	@Override
	public void onMatrixRangeChanged(final QuestionProxy questionProxy, final AbstractHasData<MatrixValidityProxy> table, final AcceptMatrixAnswerSubView matrixAnswerListView) {
		final Range range = table.getVisibleRange();
		
		requests.MatrixValidityRequest().countAllMatrixValidityForQuestionForAcceptAnswerView(questionProxy.getId(), personRightProxy.getIsInstitutionalAdmin()).fire(new BMEReceiver<Long>() {

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
		
		requests.MatrixValidityRequest().findAllMatrixValidityForQuestionForAcceptAnswerView(questionId, personRightProxy.getIsInstitutionalAdmin(), start, length).with("answerX", "answerY", "answerX.rewiewer","answerX.autor", "answerX.question", "answerX.question.questionType", "answerY.rewiewer","answerY.autor", "answerY.question", "answerY.question.questionType").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				matrixAnswerListView.setMatrixAnswerList(response);
				table.setRowData(response);
			}
		});
	}
	
	@Override
	public void matrixAcceptClicked(QuestionProxy questionProxy) {

		requests.answerRequest().acceptMatrixAnswer(questionProxy, userLoggedIn.getIsAdmin(), personRightProxy.getIsInstitutionalAdmin()).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if (response)
					init();
			}
		});
	}

	@Override
	public void matrixRejectClicked(QuestionProxy questionProxy) {
		
		//List<AnswerProxy> ansProxyList = (List<AnswerProxy>) questionProxy.getAnswers();
		AnswerRequest req = requests.answerRequest();
		
		for (AnswerProxy proxy : questionProxy.getAnswers())
		{
			AnswerProxy answerProxy =  req.edit(proxy);
			
			answerProxy.setStatus(Status.DEACTIVATED);
			
			req.persist().using(answerProxy);
		}
		
		req.fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				init();
			}
		});
	}

	@Override
	public void placeChanged(Place place) {
		// TODO add place changed code here
		
	}

	@Override
	public void forceMatrixAcceptClicked(QuestionProxy questionProxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forcedAcceptClicked(AnswerProxy answerProxy,
			AcceptAnswerSubView acceptAnswerSubView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptAllAnswerClicked(QuestionProxy questionProxy) {
		requests.answerRequest().acceptAllAnswerClicked(questionProxy.getId()).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				init();
			}
		});
	}
}

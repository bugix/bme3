package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptAnswer;
import medizin.client.place.PlaceNotActivatedAnswer;
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

public class ActivityNotActivatedAnswer extends AbstractActivityWrapper implements AcceptAnswerView.Delegate, AcceptAnswerSubView.Delegate, AcceptMatrixAnswerSubView.Delegate {

	private PlaceNotActivatedAnswer answerPlace;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AcceptAnswerView view;
	private VerticalPanel questionPanel;

	
	public ActivityNotActivatedAnswer(PlaceNotActivatedAnswer place,McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.answerPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	}

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
	}

	private void init() {
		questionPanel.clear();
		requests.questionRequest().findAllQuestionsAnswersNotActivatedByPerson().with("answers", "questionType").fire(new BMEReceiver<List<QuestionProxy>>() {
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
						AcceptMatrixAnswerSubView matrixAnswerSubView = new AcceptMatrixAnswerSubViewImpl(false, true);
						matrixAnswerSubView.setDelegate(ActivityNotActivatedAnswer.this);
						matrixAnswerSubView.setProxy(questionProxy);
						questionPanel.add(matrixAnswerSubView);
					}
					else
					{
						AcceptAnswerSubView acceptAnswerSubView = new AcceptAnswerSubViewImpl(false, true);				
					    acceptAnswerSubView.setDelegate(ActivityNotActivatedAnswer.this);
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
		
		requests.answerRequest().countAnswersForForceActiveByQuestion(questionProxy.getId()).with("question", "autor", "rewiewer").fire(new BMEReceiver<Long>() {
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
		
		requests.answerRequest().findAnswersForForceActiveByQuestion(questionId, start, length).with("rewiewer","autor", "question", "question.questionType").fire(new BMEReceiver<List<AnswerProxy>>() {
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
	}
	
	@Override
	public void rejectClicked(AnswerProxy answerProxy) {
	}

	@Override
	public void onMatrixRangeChanged(final QuestionProxy questionProxy, final AbstractHasData<MatrixValidityProxy> table, final AcceptMatrixAnswerSubView matrixAnswerListView) {
		final Range range = table.getVisibleRange();
		
		requests.MatrixValidityRequest().countAllMatrixValidityForForceActiveByQuestion(questionProxy.getId(), personRightProxy.getIsInstitutionalAdmin()).fire(new BMEReceiver<Long>() {

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
		
		requests.MatrixValidityRequest().findAllMatrixValidityForForceActiveByQuestion(questionId, personRightProxy.getIsInstitutionalAdmin(), start, length).with("answerX", "answerY", "answerX.rewiewer","answerX.autor", "answerX.question", "answerX.question.questionType", "answerY.rewiewer","answerY.autor", "answerY.question", "answerY.question.questionType").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				matrixAnswerListView.setMatrixAnswerList(response);
				table.setRowData(response);
			}
		});
	}
	
	@Override
	public void matrixAcceptClicked(QuestionProxy questionProxy) {		
	}

	@Override
	public void matrixRejectClicked(QuestionProxy questionProxy) {		
	}

	@Override
	public void placeChanged(Place place) {		
	}

	@Override
	public void forceMatrixAcceptClicked(QuestionProxy questionProxy) {
		requests.answerRequest().forceAcceptMatrixAnswer(questionProxy, userLoggedIn.getIsAdmin(), personRightProxy.getIsInstitutionalAdmin()).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if (response)
					init();
			}
		});
	}

	@Override
	public void forcedAcceptClicked(final AnswerProxy answerProxy2,final AcceptAnswerSubView acceptAnswerSubView) {
		AnswerRequest req = requests.answerRequest();
		AnswerProxy answerProxy =  req.edit((AnswerProxy)answerProxy2);
		if(userLoggedIn.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin()){
			answerProxy.setIsForcedActive(true);
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
}

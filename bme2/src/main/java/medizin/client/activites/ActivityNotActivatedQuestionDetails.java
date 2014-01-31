package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceNotActivatedQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.question.AnswerDialogbox;
import medizin.client.ui.view.question.AnswerListView;
import medizin.client.ui.view.question.MatrixAnswerListView;
import medizin.client.ui.view.question.MatrixAnswerView;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.util.Matrix;
import medizin.client.util.MatrixValidityVO;
import medizin.shared.QuestionTypes;
import medizin.shared.Validity;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class ActivityNotActivatedQuestionDetails extends AbstractActivityWrapper implements 
							QuestionDetailsView.Delegate, 
							AnswerDialogbox.Delegate, 
							AnswerListView.Delegate, 
							MatrixAnswerView.Delegate, 
							MatrixAnswerListView.Delegate{

	private final PlaceNotActivatedQuestionDetails placeNotActivatedQuestionDetails;
	private final McAppRequestFactory requests;
	private final PlaceController placeController;
	private AcceptsOneWidget widget;
	//private EventBus eventBus;
	private QuestionDetailsViewImpl view;
	private QuestionProxy question;
	private HandlerRegistration answerRangeChangeHandler;
	private final ActivityNotActivatedQuestionDetails thiz;
	
	private PartActivityQuestionLearningObjective partActivityQuestionLearningObjective;
	private PartActivityQuestionKeyword activityQuestionKeyword;

	public ActivityNotActivatedQuestionDetails(PlaceNotActivatedQuestionDetails place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.placeNotActivatedQuestionDetails = place;
		this.requests = requests;
		this.placeController = placeController;
		thiz = this;
	}

	public void goTo(Place place) {
		placeController.goTo(place);
	}
	
	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void onCancel() {
	}

	@Override
	public void onStop() {
	}
	
	@Override
	public void start2(final AcceptsOneWidget panel, final EventBus eventBus) {
		
		if(userLoggedIn==null) return;
		
		requests.find(placeNotActivatedQuestionDetails.getProxyId()).with("previousVersion","keywords","questEvent","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(final Object response) {
				if(response instanceof QuestionProxy){
					Log.info(((QuestionProxy) response).getQuestionText());
					
					QuestionDetailsViewImpl questionDetailsView = new QuestionDetailsViewImpl(eventBus, false,false,false,true,false, false,true, false,getQuestionType((QuestionProxy) response));
					thiz.view = questionDetailsView;
					
					questionDetailsView.setDelegate(thiz);
					
					thiz.widget = panel;
					//this.eventBus = eventBus;
			        widget.setWidget(questionDetailsView.asWidget());
			        
					if (((QuestionProxy) response).getIsReadOnly() == true)
						view.setVisibleEditAndDeleteBtn(false);
					
					init((QuestionProxy) response);
				}				
			}
			
		});
	}
	
	private QuestionTypes getQuestionType(QuestionProxy questionProxy) {
		if(questionProxy != null && questionProxy.getQuestionType() != null) {
			return questionProxy.getQuestionType().getQuestionType();	
		} 
		return null;
	}
	
	private void init(QuestionProxy question) {
		this.question = question;
		Log.debug("Details für: "+question.getQuestionText());
		
		view.removeQuestionUsedInMCTab();
		view.setValue(question);	
		
//		view.getQuestionLearningObjectiveSubViewImpl().setDelegate(this);
		
		view.getMatrixAnswerListViewImpl().setDelegate(this);
		view.getAnswerListViewImpl().setDelegate(this);
		
		view.getAnswerListViewImpl().setVisible(false);
		view.getMatrixAnswerListViewImpl().setVisible(false);
		
		if (QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType()))
		{	
			view.getMatrixAnswerListViewImpl().setVisible(true);
			initMatrixAnswerView();
		}
		else
		{
			view.getAnswerListViewImpl().setVisible(true);
			initAnswerView();
		}
			
		activityQuestionKeyword = new PartActivityQuestionKeyword(requests, view.getQuestionKeywordView(),true);
		activityQuestionKeyword.setQuestionProxy(question);
		view.getQuestionKeywordView().setDelegate(activityQuestionKeyword);
		partActivityQuestionLearningObjective = new PartActivityQuestionLearningObjective(requests, view.getQuestionLearningObjectiveSubViewImpl(),true);
		view.getQuestionLearningObjectiveSubViewImpl().setDelegate(partActivityQuestionLearningObjective);
		partActivityQuestionLearningObjective.setQuestionProxy(question);
	}
	
	private void initAnswerView() {
		
		if (answerRangeChangeHandler!=null){
			answerRangeChangeHandler.removeHandler();
			answerRangeChangeHandler=null;
		}
		
		requests.answerRequest().contAnswersByQuestion(question.getId()).fire( new BMEReceiver<Long>(){

			@Override
			public void onSuccess(Long response) {
				view.getAnswerListViewImpl().getTable().setRowCount(response.intValue(), true);
				onAnswerTableRangeChanged();
			}
		});
		
		answerRangeChangeHandler =  view.getAnswerListViewImpl().getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityNotActivatedQuestionDetails.this.onAnswerTableRangeChanged();
			}
		});
	}
	
	private void onAnswerTableRangeChanged() {
		final Range range = view.getAnswerListViewImpl().getTable().getVisibleRange();
		requests.answerRequest().findAnswersEntriesByQuestion(question.getId(), range.getStart(), range.getLength()).with("question","rewiewer","autor","question.questionType").fire( new BMEReceiver<List<AnswerProxy>>(){
			@Override
			public void onSuccess(List<AnswerProxy> response) {
				view.getAnswerListViewImpl().getTable().setRowData(range.getStart(), response);
			}
		});
	}

	private void initMatrixAnswerView() {
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
				ActivityNotActivatedQuestionDetails.this.onMatrixAnswerTableRangeChanged();
			}
		});
		
	}
	
	private void onMatrixAnswerTableRangeChanged() {
		final Range range = view.getMatrixAnswerListViewImpl().getTable().getVisibleRange();
		requests.MatrixValidityRequest().findAllMatrixValidityForAcceptQuestion(question.getId(), range.getStart(), range.getLength()).with("answerX","answerY").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				view.getMatrixAnswerListViewImpl().getTable().setRowData(range.getStart(), response);
			}
		});
	}
	
	@Override
	public void placeChanged(Place place) {		
	}
	
	@Override
	public void forcedActiveClicked() {
		requests.questionRequest().forcedActiveQuestion(question.getId()).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				goTo(new PlaceNotActivatedQuestion(PlaceNotActivatedQuestion.PLACE_NOT_ACTIVATED_QUESTION));
			}
		});
	}

	@Override
	public void getQuestionDetails(QuestionProxy previousVersion, final Function<QuestionProxy, Void> function) {
		requests.questionRequest().findQuestion(previousVersion.getId()).with("previousVersion","keywords","questEvent","questionType","mcs", "rewiewer", "autor","questionResources").fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				function.apply((QuestionProxy) response);
			}
		});
	}
	
	@Override
	public QuestionProxy getLatestQuestionDetails() {
		return question;
	}
	
	@Override
	public void enableBtnOnLatestClicked() {
		view.setVisibleEditAndDeleteBtn(false);
		view.getAcceptBtn().setVisible(false);
		view.setVisibleForcedActiveBtn(true);
	}
	
	@Override
	public void addMatrixNewAnswerClicked() {}

	@Override
	public void editMatrixValidityClicked(MatrixValidityProxy matrixValidity) {}

	@Override
	public void deleteMatrixValidityClicked(MatrixValidityProxy matrixValidity) {}

	@Override
	public void deletedSelectedAnswer(AnswerProxy answerProxy, Boolean isAnswerX, Function<Boolean, Void> function) {}

	@Override
	public void closedMatrixValidityView() {}

	@Override
	public void deleteAnswerClicked(AnswerProxy Answer) {}

	@Override
	public void addNewAnswerClicked() {}

	@Override
	public void editAnswerClicked(AnswerProxy answer) {}

	@Override
	public void cancelAnswerClicked() {}

	@Override
	public void findAllAnswersPoints(Long id,Long currentAnswerId, Function<List<String>, Void> function) {}

	@Override
	public void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords, Integer sequenceNumber, Boolean forcedActive,Function<AnswerProxy, Void> function) {}

	@Override
	public void deleteClicked() {}

	@Override
	public void editClicked() {}

	@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {}

	@Override
	public void onResendToReviewClicked(QuestionProxy proxy) {}

	@Override
	public void checkForResendToReview() {}

	@Override
	public void saveAllTheValuesToAnswerAndMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Boolean forcedActive) {}

	@Override
	public void acceptQueAnswersClicked() {}

	@Override
	public void showAllClicked() {}

	@Override
	public void pushToReviewProcessClicked() {}
}

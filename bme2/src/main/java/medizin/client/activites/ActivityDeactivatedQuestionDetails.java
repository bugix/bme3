package medizin.client.activites;

import java.util.List;
import java.util.Set;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceDeactivatedQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.question.AnswerDialogbox;
import medizin.client.ui.view.question.AnswerListView;
import medizin.client.ui.view.question.MatrixAnswerListView;
import medizin.client.ui.view.question.MatrixAnswerView;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
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
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class ActivityDeactivatedQuestionDetails extends AbstractActivityWrapper implements QuestionDetailsView.Delegate, AnswerDialogbox.Delegate, AnswerListView.Delegate, MatrixAnswerView.Delegate, MatrixAnswerListView.Delegate {

	private PlaceDeactivatedQuestionDetails placeDeactivatedQuestionDetails;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private QuestionDetailsView view;
	private AcceptsOneWidget widget;
	private QuestionProxy question;
	private HandlerRegistration answerRangeChangeHandler;
	private final ActivityDeactivatedQuestionDetails thiz;

	public ActivityDeactivatedQuestionDetails(PlaceDeactivatedQuestionDetails place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.placeDeactivatedQuestionDetails = place;
		this.requests = requests;
		this.placeController = placeController;
		thiz = this;
	}

	@Override
	public void start2(final AcceptsOneWidget panel, final EventBus eventBus) {
        if(userLoggedIn==null) return;
		
		requests.find(placeDeactivatedQuestionDetails.getProxyId()).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(final Object response) {
				if(response instanceof QuestionProxy){
					Log.info(((QuestionProxy) response).getQuestionText());
					
					QuestionDetailsViewImpl questionDetailsView = new QuestionDetailsViewImpl(eventBus, false,false,false,false,false,isQuestionTypeMCQ((QuestionProxy) response));
					thiz.view = questionDetailsView;		
					questionDetailsView.setDelegate(thiz);		
					thiz.widget = panel;
			        widget.setWidget(questionDetailsView.asWidget());
					
					if (((QuestionProxy) response).getIsReadOnly() == true)
						view.setVisibleEditAndDeleteBtn(false);
					
					init((QuestionProxy) response);
				}				
			}
			
		});
	}

	private boolean isQuestionTypeMCQ(QuestionProxy questionProxy) {
		return questionProxy != null && questionProxy.getQuestionType() != null && QuestionTypes.MCQ.equals(questionProxy.getQuestionType().getQuestionType());
	}
	
	private void init(QuestionProxy response) {
		this.question = response;
		Log.debug("Details für: "+question.getQuestionText());
		
		view.setValue(question);	
		
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
				
		initKeywordView();
	}

	private void initKeywordView() {
		view.getKeywordSuggestBox().setVisible(false);
		view.getKeywordAddButton().setVisible(false);
		
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
					onKeywordTableRangeChanged();
				}
			});
			
			view.getKeywordTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onKeywordTableRangeChanged();
				}
			});
		}
	}

	private void onKeywordTableRangeChanged() {
		final Range range = view.getKeywordTable().getVisibleRange();
		
		requests.keywordRequest().findKeywordByQuestion(question.getId(), range.getStart(), range.getLength()).fire(new BMEReceiver<List<KeywordProxy>>() {

			@Override
			public void onSuccess(List<KeywordProxy> response) {
				view.getKeywordTable().setRowData(range.getStart(), response);
			}
		});
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
				ActivityDeactivatedQuestionDetails.this.onAnswerTableRangeChanged();
			}
		});
	}

	private void onAnswerTableRangeChanged() {
		final Range range = view.getAnswerListViewImpl().getTable().getVisibleRange();
		requests.answerRequest().findAnswersEntriesByQuestion(question.getId(), range.getStart(), range.getLength()).with("question","rewiewer","autor","comment","question.questionType").fire( new BMEReceiver<List<AnswerProxy>>(){
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
				ActivityDeactivatedQuestionDetails.this.onMatrixAnswerTableRangeChanged();
			}
		});
	}

	private void onMatrixAnswerTableRangeChanged() {
		final Range range = view.getMatrixAnswerListViewImpl().getTable().getVisibleRange();
		requests.MatrixValidityRequest().findAllMatrixValidityForAcceptQuestion(question.getId(), range.getStart(), range.getLength()).with("answerX","answerY").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				view.getMatrixAnswerListViewImpl().getTable().setRowData(view.getMatrixAnswerListViewImpl().getTable().getVisibleRange().getStart(), response);
			}
		});
	}

	@Override
	public void placeChanged(Place place) {
		//placeController.goTo(place);
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
	public QuestionProxy getLatestQuestionDetails() {
		return question;
	}
	
	@Override
	public String mayStop() {
		return null;
	}

	@Override
	public void onResendToReviewClicked(QuestionProxy proxy) {}

	@Override
	public void checkForResendToReview() {}

	@Override
	public void forcedActiveClicked() {}

	@Override
	public void enableBtnOnLatestClicked() {}

	@Override
	public void keywordAddButtonClicked(String text, QuestionProxy proxy) {}

	@Override
	public void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy) {}
	
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
	public void saveAllTheValuesToAnswerAndMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment) {}

	@Override
	public void deleteAnswerClicked(AnswerProxy Answer) {}

	@Override
	public void addNewAnswerClicked() {}

	@Override
	public void editAnswerClicked(AnswerProxy answer) {}

	@Override
	public void cancelAnswerClicked() {}

	@Override
	public void findAllAnswersPoints(Long id, Long currentAnswerId, Function<List<String>, Void> function) {}

	@Override
	public void deleteUploadedFiles(Set<String> paths) {}

	@Override
	public void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords,  Integer sequenceNumber, Function<AnswerProxy, Void> function) {}

	@Override
	public void deleteClicked() {}

	@Override
	public void editClicked() {}

	@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {}
}

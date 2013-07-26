package medizin.client.activites;

import java.util.List;
import java.util.Set;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceNotActivatedQuestionDetails;
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
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
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

public class ActivityNotActivatedQuestionDetails extends AbstractActivityWrapper implements QuestionDetailsView.Delegate, QuestionDetailsView.Presenter, AnswerDialogbox.Delegate, AnswerListView.Delegate, MatrixAnswerView.Presenter, MatrixAnswerView.Delegate, MatrixAnswerListView.Delegate, MatrixAnswerListView.Presenter {

	private final PlaceNotActivatedQuestionDetails placeNotActivatedQuestionDetails;
	private final McAppRequestFactory requests;
	private final PlaceController placeController;
	private AcceptsOneWidget widget;
	private EventBus eventBus;
	private QuestionDetailsViewImpl view;
	private QuestionProxy question;
	private HandlerRegistration answerRangeChangeHandler;

	public ActivityNotActivatedQuestionDetails(PlaceNotActivatedQuestionDetails place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.placeNotActivatedQuestionDetails = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
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
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {
		QuestionDetailsViewImpl questionDetailsView = new QuestionDetailsViewImpl(eventBus, false,false);
		this.view = questionDetailsView;
		
		questionDetailsView.setPresenter(this);
		questionDetailsView.setDelegate(this);
		
		this.widget = panel;
		this.eventBus = eventBus;
        widget.setWidget(questionDetailsView.asWidget());
                	
		start2();
	}

	private void start2(){
		if(userLoggedIn==null) return;
		
		requests.find(placeNotActivatedQuestionDetails.getProxyId()).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(final Object response) {
				if(response instanceof QuestionProxy){
					Log.info(((QuestionProxy) response).getQuestionText());
					
					if (((QuestionProxy) response).getIsReadOnly() == true)
						view.setVisibleEditAndDeleteBtn(false);
					
					init((QuestionProxy) response);
				}				
			}
			
		    });
	}
	
	private void init(QuestionProxy question) {
		this.question = question;
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
		
		view.getAcceptBtn().setVisible(false);
		view.getForcedActiveBtn().setVisible(true);
		
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
		requests.answerRequest().findAnswersEntriesByQuestion(question.getId(), range.getStart(), range.getLength()).with("question","rewiewer","autor","comment").fire( new BMEReceiver<List<AnswerProxy>>(){
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
		requests.MatrixValidityRequest().findAllMatrixValidityForQuestion(question.getId()).with("answerX","answerY").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				view.getMatrixAnswerListViewImpl().getTable().setRowData(view.getMatrixAnswerListViewImpl().getTable().getVisibleRange().getStart(), response);
			}
		});
	}
	

	@Override
	public boolean isQuestionDetailsPlace() {
		return false;
	}
	
	@Override
	public void placeChanged(Place place) {
		placeController.goTo(place);
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
	
	@Override
	public void enableBtnOnLatestClicked() {
		view.setVisibleEditAndDeleteBtn(false);
		view.getAcceptBtn().setVisible(false);
		view.getForcedActiveBtn().setVisible(true);
	}
	
	@Override
	public void addMatrixNewAnswerClicked() {}

	@Override
	public void editMatrixValidityClicked(MatrixValidityProxy matrixValidity) {}

	@Override
	public void deleteMatrixValidityClicked(MatrixValidityProxy matrixValidity) {}

	@Override
	public void saveMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment) {}

	@Override
	public void saveMatrixValidityValue(MatrixValidityVO matrixValidityVO, Validity validity, Function<MatrixValidityProxy, Void> function) {}

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
	public void findAllAnswersPoints(Long id, Function<List<String>, Void> function) {}

	@Override
	public void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords, Integer sequenceNumber, Function<AnswerProxy, Void> function) {}

	@Override
	public void deleteClicked() {}

	@Override
	public void editClicked() {}

	@Override
	public void newClicked() {}

	@Override
	public void deleteSelectedQuestionResource(Long qestionResourceId) {}

	@Override
	public void addNewQuestionResource(QuestionResourceClient questionResourceClient) {}

	@Override
	public void updatePicturePathInQuestion(String picturePath) {}

	@Override
	public void deleteUploadedPicture(String picturePath) {}

	@Override
	public void deleteUploadedFiles(Set<String> paths) {}

	@Override
	public void changedResourceSequence(Set<QuestionResourceClient> questionResourceClients) {}

	@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {}

	@Override
	public void onResendToReviewClicked(QuestionProxy proxy) {}

	@Override
	public void checkForResendToReview() {}

	@Override
	public void keywordAddButtonClicked(String text, QuestionProxy proxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveAllTheValuesToAnswerAndMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment) {}
}

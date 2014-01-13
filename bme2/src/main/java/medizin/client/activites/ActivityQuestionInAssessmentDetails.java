package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestionInAssessmentDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.AcceptAnswerSubView;
import medizin.client.ui.view.AcceptAnswerSubViewImpl;
import medizin.client.ui.view.AcceptMatrixAnswerSubView;
import medizin.client.ui.view.AcceptMatrixAnswerSubViewImpl;
import medizin.client.ui.view.QuestionInAssessmentDetailsView;
import medizin.client.ui.view.QuestionInAssessmentDetailsViewImpl;
import medizin.shared.QuestionTypes;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;

public class ActivityQuestionInAssessmentDetails extends AbstractActivityWrapper implements
															QuestionInAssessmentDetailsView.Delegate, 
															AcceptAnswerSubView.Delegate, 
															AcceptMatrixAnswerSubView.Delegate
{ 
	private PlaceQuestionInAssessmentDetails place;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private EventBus eventBus;
	private QuestionInAssessmentDetailsView view;
	private QuestionProxy question;

	public ActivityQuestionInAssessmentDetails(PlaceQuestionInAssessmentDetails place,McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.place = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {
		QuestionInAssessmentDetailsView view = new QuestionInAssessmentDetailsViewImpl();
		this.view = view;
		this.widget = panel;
		this.eventBus = eventBus;
		widget.setWidget(view.asWidget());
		view.setDelegate(this);
		createAnswerPanel();
	}
	
	public void createAnswerPanel()
	{
		requests.find(place.getProxyId()).with("question", "question.questEvent","question.questionType","question.mcs", "question.rewiewer", "question.autor","question.questionResources","question.answers").fire(new BMEReceiver<Object>() {
			
			@Override
			public void onSuccess(final Object response) {
				
				if(response instanceof AssesmentQuestionProxy){
					
					initDetailsView((AssesmentQuestionProxy) response);
					
				}				
			}
		});
	}

	protected void initDetailsView(AssesmentQuestionProxy response) {
		if (response.getQuestion().getQuestionType() != null && QuestionTypes.Matrix.equals(response.getQuestion().getQuestionType().getQuestionType()))
			initMatrixAnswerView(response);
		else
			initAnswerView(response);
	}
	
	private void initAnswerView(AssesmentQuestionProxy response)
	{
		AcceptAnswerSubView acceptAnswerSubView = new AcceptAnswerSubViewImpl(false, false);				
	    acceptAnswerSubView.setDelegate(ActivityQuestionInAssessmentDetails.this);
	    acceptAnswerSubView.setProxy(response.getQuestion());
	    Label header = new Label(response.getQuestion().getQuestionShortName());
		header.getElement().getStyle().setPadding(10, Unit.PX);
	    acceptAnswerSubView.getQuestionDisclosurePanel().setHeader(header);
	    acceptAnswerSubView.getQuestionDisclosurePanel().setOpen(true);
	    
	    view.getAnswerVerticalPanel().add(acceptAnswerSubView);
	}
	
	private void initMatrixAnswerView(AssesmentQuestionProxy response)
	{
		AcceptMatrixAnswerSubView matrixAnswerSubView = new AcceptMatrixAnswerSubViewImpl(false, false);
		matrixAnswerSubView.setDelegate(ActivityQuestionInAssessmentDetails.this);
		matrixAnswerSubView.setProxy(response.getQuestion());
		Label header = new Label(response.getQuestion().getQuestionShortName());
		header.getElement().getStyle().setPadding(10, Unit.PX);
		matrixAnswerSubView.getQuestionDisclosurePanel().setHeader(header);
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
				
				table.setRowCount(response.intValue(), true);
				
				findAnswersEntriesNonAcceptedAdminByQuestion(questionProxy.getId(), range.getStart(), range.getLength(), table);
			}
		});
	}
	
	void findAnswersEntriesNonAcceptedAdminByQuestion(Long questionId, final Integer start, Integer length, final AbstractHasData<AnswerProxy> table){
		
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
	public void matrixAcceptClicked(QuestionProxy questionProxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forceMatrixAcceptClicked(QuestionProxy questionProxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void matrixRejectClicked(QuestionProxy questionProxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptClicked(AnswerProxy answerProxy,
			AcceptAnswerSubView acceptAnswerSubView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forcedAcceptClicked(AnswerProxy answerProxy,
			AcceptAnswerSubView acceptAnswerSubView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rejectClicked(AnswerProxy answerProxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeChanged(Place place) {
		// TODO Auto-generated method stub
		
	}
}

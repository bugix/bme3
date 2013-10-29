package medizin.client.activites;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.ApplianceProxy;
import medizin.client.proxy.ClassificationTopicProxy;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.MainClassificationProxy;
import medizin.client.proxy.MainQuestionSkillProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.MinorQuestionSkillProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.SkillHasApplianceProxy;
import medizin.client.proxy.SkillLevelProxy;
import medizin.client.proxy.SkillProxy;
import medizin.client.proxy.TopicProxy;
import medizin.client.request.MainQuestionSkillRequest;
import medizin.client.request.MinorQuestionSkillRequest;
import medizin.client.request.QuestionRequest;
import medizin.client.request.SkillRequest;
import medizin.client.ui.view.AcceptAnswerSubView;
import medizin.client.ui.view.AcceptAnswerSubViewImpl;
import medizin.client.ui.view.AcceptMatrixAnswerSubView;
import medizin.client.ui.view.AcceptMatrixAnswerSubViewImpl;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.ui.view.question.learningobjective.LearningObjectiveView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectivePopupView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectiveSubView;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.LearningObjectiveData;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class ActivityAcceptQuestionDetails extends AbstractActivityWrapper implements
						QuestionDetailsView.Delegate, 
						AcceptAnswerSubView.Delegate, 
						AcceptMatrixAnswerSubView.Delegate,
						QuestionLearningObjectiveSubView.Delegate,
						QuestionLearningObjectivePopupView.Delegate,
						LearningObjectiveView.Delegate{

	private PlaceAcceptQuestionDetails place;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private EventBus eventBus;
	private QuestionDetailsViewImpl view;
	private QuestionProxy question;
	private Long mainClassificationId = null;
	private Long classificationTopicId = null;
	private Long topicId = null;
	private Long skillLevelId = null;
	private Long applianceId = null;
	private LearningObjectiveView learningObjectiveView;
	private LearningObjectiveData learningObjective;
	private String temp;
	private List<LearningObjectiveData> learningObjectiveData = new ArrayList<LearningObjectiveData>();

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
		
		requests.find(place.getProxyId()).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<Object>() {
			
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
		QuestionDetailsViewImpl questionDetailsView = new QuestionDetailsViewImpl(eventBus, true,hasAnswerWriteRights(questionProxy, null),hasAnswerAddRights(questionProxy),false,true,isQuestionTypeMCQ(questionProxy), true);
		this.view = questionDetailsView;
        widget.setWidget(questionDetailsView.asWidget());
		view.setDelegate(this);
		view.getQuestionLearningObjectiveSubViewImpl().setDelegate(this);
		view.setVisibleAcceptButton();
		view.getAnswerListViewImpl().removeFromParent();
		view.getMatrixAnswerListViewImpl().removeFromParent();
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
		
		initKeywordView();
		
		checkForResendToReview();
		
		initLearningObjectiveView();
	}
	
	private void initLearningObjectiveView() {
		RecordChangeEvent.register(requests.getEventBus(), view.getQuestionLearningObjectiveSubViewImpl());
		if (question != null)
		{
			MainQuestionSkillRequest mainQuestionSkillRequest = requests.mainQuestionSkillRequest();
			mainQuestionSkillRequest.countMainQuestionSkillByQuestionId(question.getId()).to(new BMEReceiver<Integer>() {

				@Override
				public void onSuccess(Integer response) {
					view.getQuestionLearningObjectiveSubViewImpl().majorTable.setRowCount(response);
					onMainQuestionSkillTableRangeChanged();
				}
			});
			
			MinorQuestionSkillRequest minorQuestionSkillRequest = mainQuestionSkillRequest.append(requests.minorQuestionSkillRequest());
			minorQuestionSkillRequest.countMinorQuestionSkillByQuestionId(question.getId()).to(new BMEReceiver<Integer>() {

				@Override
				public void onSuccess(Integer response) {
					view.getQuestionLearningObjectiveSubViewImpl().minorTable.setRowCount(response);
					onMinorQuestionSkillTableRangeChanged();
				}
			});
			
			minorQuestionSkillRequest.fire();
			
			view.getQuestionLearningObjectiveSubViewImpl().majorTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onMainQuestionSkillTableRangeChanged();
				}
			});
			
			view.getQuestionLearningObjectiveSubViewImpl().minorTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onMinorQuestionSkillTableRangeChanged();
				}
			});
		}
	}
	
	public void onMinorQuestionSkillTableRangeChanged()
	{
		if (question != null)
		{
			final Range range = view.getQuestionLearningObjectiveSubViewImpl().minorTable.getVisibleRange();
			
			requests.minorQuestionSkillRequest().findMinorQuestionSkillByQuestionId(question.getId(), range.getStart(), range.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new BMEReceiver<List<MinorQuestionSkillProxy>>() {

				@Override
				public void onSuccess(List<MinorQuestionSkillProxy> response) {
					view.getQuestionLearningObjectiveSubViewImpl().minorTable.setRowData(range.getStart(), response);
				}
			});
		}
	}
	
	public void onMainQuestionSkillTableRangeChanged()
	{
		if (question != null)
		{
			final Range range = view.getQuestionLearningObjectiveSubViewImpl().majorTable.getVisibleRange();
			requests.mainQuestionSkillRequest().findMainQuestionSkillByQuestionId(question.getId(), range.getStart(), range.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new BMEReceiver<List<MainQuestionSkillProxy>>() {

				@Override
				public void onSuccess(List<MainQuestionSkillProxy> response) {
					view.getQuestionLearningObjectiveSubViewImpl().majorTable.setRowData(range.getStart(), response);
				}
			});
		}
	}
	
	private void initKeywordView() {
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
					view.getEdit().setVisible(true);
					view.getAcceptBtn().removeFromParent();
				}else {
					view.getResendToReviewBtn().removeFromParent();
					view.getAcceptBtn().removeFromParent();
					view.getEdit().removeFromParent();
				}
			} else if(Status.EDITED_BY_REVIEWER.equals(question.getStatus()) == true) {
				if(question.getRewiewer() != null && userLoggedIn.getId().equals(question.getRewiewer().getId()) == true) {
					view.getResendToReviewBtn().setVisible(true);
					view.getEdit().setVisible(true);
					view.getAcceptBtn().removeFromParent();
				}else {
					view.getResendToReviewBtn().removeFromParent();
					view.getAcceptBtn().removeFromParent();
					view.getEdit().removeFromParent();
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
	public void keywordAddButtonClicked(String text, QuestionProxy proxy) {
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
					initKeywordView();
				}
			});
		}
		else
		{
			view.getKeywordSuggestBox().getTextField().setText("");
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.keywordExist());
		}
	}

	@Override
	public void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy) {
		
		requests.keywordRequest().deleteKeywordFromQuestion(keyword, proxy).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<QuestionProxy>() {

			@Override
			public void onSuccess(QuestionProxy response) {
				view.setValue(response);
				question = response;
				view.getKeywordSuggestBox().getTextField().setText("");
				initKeywordView();
			}
		});
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
	public void mainClassificationSuggestboxChanged(Long value) {
		this.mainClassificationId = value;
		learningObjectiveView.getClassificationTopicSuggestBox().setSelected(null);
		classificationTopicId = null;
		
		fillClassificationTopicSuggestBox(mainClassificationId);
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void classificationTopicSuggestboxChanged(Long value) {
		this.classificationTopicId = value;
		
		learningObjectiveView.getTopicSuggestBox().setSelected(null);
		topicId = null;
		
		fillTopicSuggestBox(classificationTopicId);
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void topicSuggestboxChanged(Long value) {
		this.topicId = value;		
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void skillLevelSuggestboxChanged(Long value) {
		this.skillLevelId = value;
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void applianceSuggestboxChanged(Long value) {
		this.applianceId = value;
		onLearningObjectiveViewTableRangeChanged();
	}

	@Override
	public void mainClassiListBoxClicked(MainClassificationProxy proxy, final QuestionLearningObjectivePopupView popupView) {
		requests.classificationTopicRequest().findClassificationTopicByMainClassification(proxy.getId()).fire(new BMEReceiver<List<ClassificationTopicProxy>>() {

			@Override
			public void onSuccess(List<ClassificationTopicProxy> response) {
				popupView.getClassiTopicListBox().setAcceptableValues(response);
				
				if (response.size() > 0)
					classiTopicListBoxClicked(response.get(0), popupView);
			}
		});
	}

	@Override
	public void classiTopicListBoxClicked(ClassificationTopicProxy proxy, final QuestionLearningObjectivePopupView popupView) {
		requests.topicRequest().findTopicByClassificationTopicId(proxy.getId()).fire(new BMEReceiver<List<TopicProxy>>() {

			@Override
			public void onSuccess(List<TopicProxy> response) {
				popupView.getTopicListBox().setAcceptableValues(response);
			}
		});
	}

	@Override
	public void minorDeleteClicked(MinorQuestionSkillProxy minorSkill) {
		requests.minorQuestionSkillRequest().remove().using(minorSkill).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				initLearningObjectiveView();
			}
		});
	}

	@Override
	public void majorDeleteClicked(MainQuestionSkillProxy mainSkill) {
		requests.mainQuestionSkillRequest().remove().using(mainSkill).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				initLearningObjectiveView();
			}
		});
	}

	@Override
	public void addMainClicked() {
		Iterator<LearningObjectiveData> itr = learningObjectiveView.getMultiselectionModel().getSelectedSet().iterator();
		
		while (itr.hasNext())
		{
			LearningObjectiveData learningObjectiveData = itr.next();
			MainQuestionSkillRequest mainQuestionSkillRequest = requests.mainQuestionSkillRequest();
			MainQuestionSkillProxy mainQuestionSkillProxy = mainQuestionSkillRequest.create(MainQuestionSkillProxy.class);
			mainQuestionSkillProxy.setQuestion(question);
			mainQuestionSkillProxy.setSkill(learningObjectiveData.getSkill());
			mainQuestionSkillRequest.persist().using(mainQuestionSkillProxy).fire(new BMEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
				
				}
			});
		}
	
	}

	@Override
	public void addMinorClicked() {
		Iterator<LearningObjectiveData> itr = learningObjectiveView.getMultiselectionModel().getSelectedSet().iterator();
		
		while (itr.hasNext())
		{
			LearningObjectiveData learningObjectiveData = itr.next();
			MinorQuestionSkillRequest minorQuestionSkillRequest = requests.minorQuestionSkillRequest();
			MinorQuestionSkillProxy minorQuestionSkillProxy = minorQuestionSkillRequest.create(MinorQuestionSkillProxy.class);
			minorQuestionSkillProxy.setQuestion(question);
			minorQuestionSkillProxy.setSkill(learningObjectiveData.getSkill());
			minorQuestionSkillRequest.persist().using(minorQuestionSkillProxy).fire(new BMEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
				}
			});
		}
	}

	@Override
	public void setMainClassiPopUpListBox(final QuestionLearningObjectivePopupView popupView) {
		popupView.setDelegate(this);
		requests.mainClassificationRequest().findAllMainClassifications().fire(new BMEReceiver<List<MainClassificationProxy>>() {

			@Override
			public void onSuccess(List<MainClassificationProxy> response) {
				popupView.getMainClassiListBox().setAcceptableValues(response);
			}
		});
		
	}
	@Override
	public void setSkillLevelPopupListBox(final QuestionLearningObjectivePopupView popupView) {
		requests.skillLevelRequest().findAllSkillLevels().fire(new BMEReceiver<List<SkillLevelProxy>>() {

			@Override
			public void onSuccess(List<SkillLevelProxy> response) {
				popupView.getLevelListBox().setAcceptableValues(response);
			}
		});
	}

	@Override
	public void loadLearningObjectiveData() {
		learningObjectiveView = view.getQuestionLearningObjectiveSubViewImpl().getLearningObjectiveViewImpl();
		learningObjectiveView.setDelegate(this);
		
		
		
		requests.skillRequest().countSkillBySearchCriteria(mainClassificationId, classificationTopicId, topicId, skillLevelId, applianceId).fire(new BMEReceiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				learningObjectiveView.getTable().setRowCount(response);
				onLearningObjectiveViewTableRangeChanged();
				fillMainClassificationSuggestBox();
				fillClassificationTopicSuggestBox(mainClassificationId);
				fillTopicSuggestBox(classificationTopicId);
				fillSkillLevelSuggestBox();
				fillApplianceSuggestBox();			
			}
		});
		
		learningObjectiveView.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				onLearningObjectiveViewTableRangeChanged();
			}
		});
	}
	
	public void fillMainClassificationSuggestBox()
	{
		requests.mainClassificationRequest().findAllMainClassifications().fire(new BMEReceiver<List<MainClassificationProxy>>() {

			@Override
			public void onSuccess(List<MainClassificationProxy> response) {
				DefaultSuggestOracle<MainClassificationProxy> suggestOracle1 = (DefaultSuggestOracle<MainClassificationProxy>) learningObjectiveView.getMainClassificationSuggestBox().getSuggestOracle();
				suggestOracle1.setPossiblilities(response);
				learningObjectiveView.getMainClassificationSuggestBox().setSuggestOracle(suggestOracle1);
				
				learningObjectiveView.getMainClassificationSuggestBox().setRenderer(new AbstractRenderer<MainClassificationProxy>() {

					@Override
					public String render(MainClassificationProxy object) {
						if (object != null)
							return (object.getDescription() +  "[" + object.getShortcut() + "]");
						else
							return "";
					}
				});
			}
		});
	}
	
	public void fillClassificationTopicSuggestBox(Long mainClassificationId)
	{
		requests.classificationTopicRequest().findClassificationTopicByMainClassification(mainClassificationId).fire(new BMEReceiver<List<ClassificationTopicProxy>>() {

			@Override
			public void onSuccess(List<ClassificationTopicProxy> response) {
				DefaultSuggestOracle<ClassificationTopicProxy> suggestOracle = (DefaultSuggestOracle<ClassificationTopicProxy>) learningObjectiveView.getClassificationTopicSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				learningObjectiveView.getClassificationTopicSuggestBox().setSuggestOracle(suggestOracle);
				
				learningObjectiveView.getClassificationTopicSuggestBox().setRenderer(new AbstractRenderer<ClassificationTopicProxy>() {

					@Override
					public String render(ClassificationTopicProxy object) {
						if (object != null)
							return (object.getDescription() + "[" + object.getShortcut() + "]");
						else
							return "";
					}
				});
			}
		});
	}
	
	public void fillTopicSuggestBox(Long classificationTopicId)
	{
		requests.topicRequest().findTopicByClassificationTopicId(classificationTopicId).fire(new BMEReceiver<List<TopicProxy>>() {

			@Override
			public void onSuccess(List<TopicProxy> response) {
				DefaultSuggestOracle<TopicProxy> suggestOracle = (DefaultSuggestOracle<TopicProxy>) learningObjectiveView.getTopicSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				learningObjectiveView.getTopicSuggestBox().setSuggestOracle(suggestOracle);
				
				learningObjectiveView.getTopicSuggestBox().setRenderer(new AbstractRenderer<TopicProxy>() {

					@Override
					public String render(TopicProxy object) {
						if (object != null)
							return object.getTopicDesc();
						else
							return "";
					}
				});
			}
		});
	}
	
	public void fillSkillLevelSuggestBox()
	{
		requests.skillLevelRequest().findAllSkillLevels().fire(new BMEReceiver<List<SkillLevelProxy>>() {

			@Override
			public void onSuccess(List<SkillLevelProxy> response) {
				DefaultSuggestOracle<SkillLevelProxy> suggestOracle = (DefaultSuggestOracle<SkillLevelProxy>) learningObjectiveView.getSkillLevelSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				learningObjectiveView.getSkillLevelSuggestBox().setSuggestOracle(suggestOracle);
				
				learningObjectiveView.getSkillLevelSuggestBox().setRenderer(new AbstractRenderer<SkillLevelProxy>() {

					@Override
					public String render(SkillLevelProxy object) {
						if (object != null)
							return String.valueOf(object.getLevelNumber());
						else
							return "";
					}
				});
			}
		});
	}
	
	public void fillApplianceSuggestBox()
	{
		requests.applianceRequest().findAllAppliances().fire(new BMEReceiver<List<ApplianceProxy>>() {

			@Override
			public void onSuccess(List<ApplianceProxy> response) {
				DefaultSuggestOracle<ApplianceProxy> suggestOracle = (DefaultSuggestOracle<ApplianceProxy>) learningObjectiveView.getApplianceSuggestBox().getSuggestOracle();
				suggestOracle.setPossiblilities(response);
				learningObjectiveView.getApplianceSuggestBox().setSuggestOracle(suggestOracle);
				
				learningObjectiveView.getApplianceSuggestBox().setRenderer(new AbstractRenderer<ApplianceProxy>() {

					@Override
					public String render(ApplianceProxy object) {
						if (object != null)
							return object.getShortcut();
						else
							return "";
					}
				});		
			}
		});
	}
	
	public void onLearningObjectiveViewTableRangeChanged()
	{
		learningObjectiveData.clear();
		final Range range = learningObjectiveView.getTable().getVisibleRange();
		
		SkillRequest skillRequest = requests.skillRequest();
		skillRequest.countSkillBySearchCriteria(mainClassificationId, classificationTopicId, topicId, skillLevelId, applianceId).to(new BMEReceiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				learningObjectiveView.getTable().setRowCount(response);
			}
		});
		
		SkillRequest skillRequest2 = skillRequest.append(requests.skillRequest());
		skillRequest2.findSkillBySearchCriteria(range.getStart(), range.getLength(), mainClassificationId, classificationTopicId, topicId, skillLevelId, applianceId).with("topic", "skillLevel", "skillHasAppliances", "skillHasAppliances.appliance", "topic.classificationTopic", "topic.classificationTopic.mainClassification").to(new BMEReceiver<List<SkillProxy>>() {

			@Override
			public void onSuccess(List<SkillProxy> response) {
				for (int i=0; i<response.size(); i++)
				{
					learningObjective = new LearningObjectiveData();
					SkillProxy skill = response.get(i);
					
					temp = skill.getTopic().getClassificationTopic().getMainClassification().getShortcut() + " " + skill.getTopic().getClassificationTopic().getShortcut() + " " + skill.getShortcut();
					learningObjective.setCode(temp);
					learningObjective.setSkill(skill);
					learningObjective.setText(skill.getDescription());
					learningObjective.setTopic(skill.getTopic().getTopicDesc());	
					
					if (skill.getSkillLevel() != null)
						learningObjective.setSkillLevel(String.valueOf(skill.getSkillLevel().getLevelNumber()));
					else
						learningObjective.setSkillLevel("");
					
					Iterator<SkillHasApplianceProxy> iter = skill.getSkillHasAppliances().iterator();
					
					while (iter.hasNext())
					{
						SkillHasApplianceProxy skillHasApplianceProxy = iter.next();
						
						if (skillHasApplianceProxy.getAppliance().getShortcut().equals("D"))
							learningObjective.setD("D");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("T"))
							learningObjective.setT("T");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("E"))
							learningObjective.setE("E");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("P"))
							learningObjective.setP("P");
						else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("G"))
							learningObjective.setG("G");
					}
					
					learningObjectiveData.add(learningObjective);
					learningObjective = null;
				}
				
				learningObjectiveView.getTable().setRowData(range.getStart(), learningObjectiveData);
			}
		});
		
		skillRequest2.fire();
	}

	@Override
	public void closeButtonClicked() {
		mainClassificationId = null;
		classificationTopicId = null;
		topicId = null;
		skillLevelId = null;
		applianceId = null;
		learningObjectiveView = null;
		initLearningObjectiveView();
	}

	@Override
	public void clearAllButtonClicked() {
		loadLearningObjectiveData();
	}

	@Override
	public void acceptQueAnswersClicked() {
		requests.questionRequest().acceptQuestionAndAllAnswers(question.getId(),isAdminOrInstitutionalAdmin()).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if(response == null ||  response == false) {
					ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.notResponsiblePerson());
				} else {
					goTo(new PlaceAcceptQuestion(PlaceAcceptQuestion.PLACE_ACCEPT_QUESTION));
				}
			}
		});
		
	}

}

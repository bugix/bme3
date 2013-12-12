package medizin.client.activites;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceNotActivatedQuestion;
import medizin.client.place.PlaceNotActivatedQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.ApplianceProxy;
import medizin.client.proxy.ClassificationTopicProxy;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.MainClassificationProxy;
import medizin.client.proxy.MainQuestionSkillProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.MinorQuestionSkillProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.SkillHasApplianceProxy;
import medizin.client.proxy.SkillLevelProxy;
import medizin.client.proxy.SkillProxy;
import medizin.client.proxy.TopicProxy;
import medizin.client.request.MainQuestionSkillRequest;
import medizin.client.request.MinorQuestionSkillRequest;
import medizin.client.request.SkillRequest;
import medizin.client.ui.view.question.AnswerDialogbox;
import medizin.client.ui.view.question.AnswerListView;
import medizin.client.ui.view.question.MatrixAnswerListView;
import medizin.client.ui.view.question.MatrixAnswerView;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.ui.view.question.learningobjective.LearningObjectiveView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectivePopupView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectiveSubView;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.client.util.Matrix;
import medizin.client.util.MatrixValidityVO;
import medizin.shared.LearningObjectiveData;
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

public class ActivityNotActivatedQuestionDetails extends AbstractActivityWrapper implements 
							QuestionDetailsView.Delegate, 
							AnswerDialogbox.Delegate, 
							AnswerListView.Delegate, 
							MatrixAnswerView.Delegate, 
							MatrixAnswerListView.Delegate,
							QuestionLearningObjectiveSubView.Delegate,
							QuestionLearningObjectivePopupView.Delegate,
							LearningObjectiveView.Delegate {

	private final PlaceNotActivatedQuestionDetails placeNotActivatedQuestionDetails;
	private final McAppRequestFactory requests;
	private final PlaceController placeController;
	private AcceptsOneWidget widget;
	//private EventBus eventBus;
	private QuestionDetailsViewImpl view;
	private QuestionProxy question;
	private HandlerRegistration answerRangeChangeHandler;
	private final ActivityNotActivatedQuestionDetails thiz;
	
	private Long mainClassificationId = null;
	private Long classificationTopicId = null;
	private Long topicId = null;
	private Long skillLevelId = null;
	private Long applianceId = null;
	private LearningObjectiveView learningObjectiveView;
	private LearningObjectiveData learningObjective;
	private String temp;
	private List<LearningObjectiveData> learningObjectiveData = new ArrayList<LearningObjectiveData>();

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
					
					QuestionDetailsViewImpl questionDetailsView = new QuestionDetailsViewImpl(eventBus, false,false,false,true,false,isQuestionTypeMCQ((QuestionProxy) response), false);
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
	
	private boolean isQuestionTypeMCQ(QuestionProxy questionProxy) {
		return questionProxy != null && questionProxy.getQuestionType() != null && QuestionTypes.MCQ.equals(questionProxy.getQuestionType().getQuestionType());
	}
	
	private void init(QuestionProxy question) {
		this.question = question;
		Log.debug("Details für: "+question.getQuestionText());
		
		view.removeQuestionUsedInMCTab();
		view.setValue(question);	
		
		view.getQuestionLearningObjectiveSubViewImpl().setDelegate(this);
		
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
		initLearningObjectiveView();
	}
	
	private void initLearningObjectiveView() {
		view.getQuestionLearningObjectiveSubViewImpl().btnAdd.removeFromParent();
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
		
		view.getKeywordSuggestBox().setVisible(false);
		view.getKeywordAddButton().setVisible(false);
		//Added this to get keywords by name as ASC - Manish.
		requests.keywordRequest().findAllKeywordsByNameASC().fire(new BMEReceiver<List<KeywordProxy>>() {
		//requests.keywordRequest().findAllKeywords().fire(new BMEReceiver<List<KeywordProxy>>() {

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
	public void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords, Integer sequenceNumber, Function<AnswerProxy, Void> function) {}

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
	public void keywordAddButtonClicked(String text, QuestionProxy proxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveAllTheValuesToAnswerAndMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment) {}

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
	}

	@Override
	public void majorDeleteClicked(MainQuestionSkillProxy mainSkill) {
	}

	@Override
	public void addMainClicked() {
	}

	@Override
	public void addMinorClicked() {
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
		//Added this to show main classification in ASC order by its description - Manish .
		requests.mainClassificationRequest().findAllMainClassificationByDescASC().fire(new BMEReceiver<List<MainClassificationProxy>>() {
		//requests.mainClassificationRequest().findAllMainClassifications().fire(new BMEReceiver<List<MainClassificationProxy>>() {

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
		// Added this to show skill level by its level as ASC - Manish.
		requests.skillLevelRequest().findAllSkillLevelsByLevelASC().fire(new BMEReceiver<List<SkillLevelProxy>>() {
		//requests.skillLevelRequest().findAllSkillLevels().fire(new BMEReceiver<List<SkillLevelProxy>>() {

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
		// Added this to show all appliances by its shortcut as ASC - Manish.
		requests.applianceRequest().findAllAppliancesByShortcutASC().fire(new BMEReceiver<List<ApplianceProxy>>() {
		//requests.applianceRequest().findAllAppliances().fire(new BMEReceiver<List<ApplianceProxy>>() {

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
	public void acceptQueAnswersClicked() {}

	@Override
	public void showAllClicked() {}
}

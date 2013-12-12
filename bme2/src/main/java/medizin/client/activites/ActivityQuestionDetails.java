package medizin.client.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.ApplianceProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
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
import medizin.client.request.AnswerRequest;
import medizin.client.request.MainQuestionSkillRequest;
import medizin.client.request.MatrixValidityRequest;
import medizin.client.request.MinorQuestionSkillRequest;
import medizin.client.request.PersonRequest;
import medizin.client.request.SkillRequest;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.AnswerDialogbox;
import medizin.client.ui.view.question.AnswerDialogboxImpl;
import medizin.client.ui.view.question.AnswerDialogboxTabView;
import medizin.client.ui.view.question.AnswerDialogboxTabViewImpl;
import medizin.client.ui.view.question.AnswerListView;
import medizin.client.ui.view.question.AnswerListViewImpl;
import medizin.client.ui.view.question.MatrixAnswerListView;
import medizin.client.ui.view.question.MatrixAnswerView;
import medizin.client.ui.view.question.MatrixAnswerViewImpl;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.ui.view.question.learningobjective.LearningObjectiveView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectivePopupView;
import medizin.client.ui.view.question.learningobjective.QuestionLearningObjectiveSubView;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.client.util.AnswerVO;
import medizin.client.util.ClientUtility;
import medizin.client.util.MathJaxs;
import medizin.client.util.Matrix;
import medizin.client.util.MatrixValidityVO;
import medizin.shared.LearningObjectiveData;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.Validity;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ActivityQuestionDetails extends AbstractActivityWrapper implements 
							QuestionDetailsView.Delegate, 
							AnswerDialogbox.Delegate, AnswerListView.Delegate, 
							MatrixAnswerView.Delegate, MatrixAnswerListView.Delegate, 
							QuestionLearningObjectiveSubView.Delegate,
							QuestionLearningObjectivePopupView.Delegate,
							LearningObjectiveView.Delegate,
							AnswerDialogboxTabView.Delegate
							{

	private AcceptsOneWidget widget;
	protected McAppRequestFactory requests;
	private PlaceController placeController;
	private PlaceQuestionDetails questionPlace;
	
	protected QuestionProxy question;
	protected QuestionDetailsView view;
	private AnswerListViewImpl answerListView;
	private CellTable<AnswerProxy> answerTable;
	
	private EventBus eventBus;

	//private BmeConstants constants = GWT.create(BmeConstants.class);
	
	public ActivityQuestionDetails(PlaceQuestionDetails place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;
        this.requests = requests;
        this.placeController = placeController;
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
	
	private HandlerRegistration answerRangeChangeHandler;
	
	Boolean editDeleteBtnFlag = false;
	Boolean answerFlag = false;
	
	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {
		this.widget = panel;
		this.eventBus = eventBus;
		
		getQuestionDetails();
	}

	public void initDetailsView(QuestionProxy questionProxy) {
		editDeleteBtnFlag = hasQuestionWriteRights(questionProxy);
				
		QuestionDetailsViewImpl questionDetailsView = new QuestionDetailsViewImpl(eventBus, editDeleteBtnFlag,hasAnswerWriteRights(questionProxy, null),hasAnswerAddRights(questionProxy),false,false,isQuestionTypeMCQ(questionProxy), true);
		this.view = questionDetailsView;
        widget.setWidget(questionDetailsView.asWidget());
		view.setDelegate(this);
		view.getQuestionLearningObjectiveSubViewImpl().setDelegate(this);
		this.answerListView = view.getAnswerListViewImpl();
		answerListView.setDelegate(this);
		this.answerTable = answerListView.getTable();
	}
	
	private boolean isQuestionTypeMCQ(QuestionProxy questionProxy) {
		return questionProxy != null && questionProxy.getQuestionType() != null && QuestionTypes.MCQ.equals(questionProxy.getQuestionType().getQuestionType());
	}
	
	private void getQuestionDetails(){
		if(userLoggedIn==null) return;
		
		requests.find(questionPlace.getProxyId()).with("previousVersion","keywords","questEvent","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<Object>() {
			
			@Override
			public void onSuccess(final Object response) {
				
				if(response instanceof QuestionProxy){
					
					initDetailsView((QuestionProxy) response);
							
					init((QuestionProxy) response);
				}				
			}
		});
	}
	
	/**
	 * 
	 * @param AssesmentProxy assesment
	 */
	private void init(QuestionProxy question) {

		this.question = question;
		Log.debug("Details für: "+question.getQuestionText());
		view.setValue(question);	
		
		if (question.getQuestionType().getQuestionType().equals(QuestionTypes.Matrix))
		{
			view.getMatrixAnswerListViewImpl().setDelegate(this);
			view.getAnswerListViewImpl().setVisible(false);
			initMatrixAnswerView();
		}
		else
		{
			view.getMatrixAnswerListViewImpl().setVisible(false);
			initAnswerView();
		}
		MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());
		
		initKeywordView();
		
		initLearningObjectiveView();	
		
		initUsedInMCView();
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

	protected void initKeywordView() {
		//Added this to get all keywords an ASC by name - Manish
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
					
					answerTable.setRowCount(response.intValue(), true);

					onAnswerTableRangeChanged();
				}
		});
		
		answerRangeChangeHandler =  answerTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityQuestionDetails.this.onAnswerTableRangeChanged();
			}
		});
		
		
		
		
	}

	protected void onAnswerTableRangeChanged() {
		final Range range = answerTable.getVisibleRange();
		
		
		requests.answerRequest().findAnswersEntriesByQuestion(question.getId(), range.getStart(), range.getLength()).with("question","rewiewer","autor","question.questionType").fire( new BMEReceiver<List<AnswerProxy>>(){


			@Override
			public void onSuccess(List<AnswerProxy> response) {
				
				answerTable.setRowData(range.getStart(), response);

			}
	});
		
	}

	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void deleteClicked() {
		requests.questionRequest().deactivatedQuestion().using(question).fire(new BMEReceiver<Void>(reciverMap) {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	placeController.goTo(new PlaceQuestion(PlaceQuestion.PLACE_QUESTION));
            	
            }
        });
		
	}

	//method is overridden by sub class accept question
	@Override
	public void editClicked() {
		
		/*if (questionPlace.getFromPlace().equals("ACCEPT_QUESTION"))
			placeController.goTo(new PlaceQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT, "ACCEPT_QUESTION"));
		else*/
			placeController.goTo(new PlaceQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT));
		
	}

	
	//private AnswerDialogbox answerDialogbox;
	/*private RequestFactoryEditorDriver<AnswerProxy, AnswerDialogboxImpl> answerDriver;*/
	//private AnswerProxy answerProxy;
	//private CommentProxy commentProxy;

	@Override
	public void addNewAnswerClicked() {
		
		if(question.getQuestionType() != null && QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType()) == true) 
		{
			openMatrixAnswerView(null,true, true,true);
			return;
		} 
		else if (question.getQuestionType() != null && (QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) == true || QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) == true))
		{
			openImageKeyOrShowInInImageAnswerView(null);
		}
		else 
		{
			openAnswerView(null);	
		}
		
		/*answerDialogbox = new AnswerDialogboxImpl(question,eventBus,reciverMap);
		answerDialogbox.setDelegate(this);*/
		
		/*if(userLoggedIn.getIsAdmin() == false) {
			answerDialogbox.getAutherSuggestBox().setSelected(userLoggedIn);
			answerDialogbox.getAutherSuggestBox().setEnabled(false);
		}*/

		
					
		
		/*answerDriver = answerDialogbox.createEditorDriver();*/
		
		
		
			
			
			//this.answerProxy = request.create(AnswerProxy.class);
			/*AnswerRequest ansRequest = requests.answerRequest();
			CommentRequest commnetRequest=requests.commentRequest();
			AnswerProxy ansProxy=ansRequest.create(AnswerProxy.class);
			CommentProxy comProxy=ansRequest.create(CommentProxy.class);
			this.answerProxy = ansProxy;
			this.commentProxy=comProxy;
			
			
			

			ansRequest.persist().using(answerProxy);
	        answerProxy.setQuestion(question);
	        answerProxy.setDateAdded(new Date());
	        answerProxy.setAutor(loggedUser);
	        if(loggedUser.getIsAdmin()){
		        answerProxy.setIsAnswerAcceptedAdmin(true);
		        answerProxy.setIsAnswerAcceptedAutor(false);
		        answerProxy.setRewiewer(question.getAutor());
	        } else {
		        answerProxy.setIsAnswerAcceptedAdmin(false);
		        answerProxy.setIsAnswerAcceptedAutor(true);
	        }

	        answerProxy.setIsAnswerAcceptedReviewWahrer(false);
	        answerDialogbox.setRichPanelHTML(answerProxy.getAnswerText());
	        
	        answerProxy.setIsAnswerActive(false);
	        answerDriver.edit(answerProxy, ansRequest);

	        answerDriver.flush();
*/		
		/*answerDialogbox.setValidityPickerValues(Arrays.asList(Validity.values()));
		answerDialogbox.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                answerDialogbox.setRewiewerPickerValues(values);
	            }
	        });
	        
	       // answerDialogbox.setAutherPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                answerDialogbox.setAutherPickerValues(values,userLoggedIn);
	            }
	        });
	        
	        if(userLoggedIn!=null)
	        {
	        	if(userLoggedIn.getIsAdmin() == false) {
	        		Log.info("1 " + answerDialogbox);
	        		Log.info("1 " + answerDialogbox.getAutherSuggestBox());
	        		Log.info("1 " +loggedUser);
	        		
	        		answerDialogbox.getAutherSuggestBox().setSelected(loggedUser);
	        		//answerDialogbox.getAutherSuggestBox().setSelected(userLoggedIn);
	        		answerDialogbox.getAutherSuggestBox().setEnabled(false);
	        	}
	        }
	        	
	        answerDialogbox.display(question.getQuestionType().getQuestionType());*/

		
	}

	private AnswerDialogboxTabView openImageKeyOrShowInInImageAnswerView(final AnswerProxy answer) {
		
		final AnswerDialogboxTabView answerDialogboxTabView = new AnswerDialogboxTabViewImpl(question, eventBus, reciverMap);
		answerDialogboxTabView.setDelegate(this);
		answerDialogboxTabView.setValidityPickerValues(Arrays.asList(Validity.values()));
		
		PersonRequest personRequest = requests.personRequest();
        personRequest.findAllPeople().with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                answerDialogboxTabView.setRewiewerPickerValues(values);                
                answerDialogboxTabView.setAutherPickerValues(values,userLoggedIn,isAdminOrInstitutionalAdmin());
                
                if(answer != null) {
                	answerDialogboxTabView.setValues(answer);
		        }
                answerDialogboxTabView.display(question.getQuestionType().getQuestionType());
            }
        });
        
        personRequest.fire();
        return answerDialogboxTabView;
	}

	/*@Override
	public void addAnswerClicked() {
		
		final AnswerRequest ansRequest = requests.answerRequest();
		CommentRequest commnetRequest=requests.commentRequest();
		
		final AnswerProxy answerProxy=ansRequest.create(AnswerProxy.class);
		CommentProxy commentProxy=commnetRequest.create(CommentProxy.class);
		answerDialogbox.getRichtTextArea().removeStyleName("higlight_onViolation");
		this.answerProxy = ansProxy;
		this.commentProxy=comProxy;
		
		
        answerProxy.setQuestion(question);
        answerProxy.setDateAdded(new Date());
        answerProxy.setAutor(loggedUser);
        if(loggedUser.getIsAdmin()){
	        answerProxy.setIsAnswerAcceptedAdmin(true);
	        answerProxy.setIsAnswerAcceptedAutor(false);
	      //  answerProxy.setRewiewer(question.getAutor());
        } else {
	        answerProxy.setIsAnswerAcceptedAdmin(false);
	        answerProxy.setIsAnswerAcceptedAutor(true);
        }
        answerProxy.setAutor(answerDialogbox.getAutherSuggestBox().getSelected());
        answerProxy.setIsAnswerAcceptedReviewWahrer(false);
        //answerDialogbox.setRichPanelHTML(answerProxy.getAnswerText());
        
        answerProxy.setIsAnswerActive(false);
        
        answerProxy.setIsAnswerAcceptedAdmin(false);
        answerProxy.setStatus(Status.NEW);
        
		answerProxy.setAnswerText(answerDialogbox.getRichtTextHTML());
		answerProxy.setValidity(answerDialogbox.getValidity().getValue());
		commentProxy.setComment(answerDialogbox.getComment().getValue());
		if(answerDialogbox.getSubmitToReviewerComitee().getValue())
		{
			answerProxy.setRewiewer(null);
		}
		else if(answerDialogbox.getReviewerSuggestBox().getSelected() != null)
		{
			answerProxy.setRewiewer(answerDialogbox.getReviewerSuggestBox().getSelected());
		}else {
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.selectReviewerOrComitee());
			return;
		}
		
		if(question.getQuestionType() != null && QuestionTypes.MCQ.equals(question.getQuestionType().getQuestionType()) == false) {
			if(answerDialogbox.getRichtTextArea().getText() == null || answerDialogbox.getRichtTextArea().getText().length() <= 0) {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.answerTextErrorMessage());
				answerDialogbox.getRichtTextArea().addStyleName("higlight_onViolation");
				return;
			}
		}
		
		if(question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) == true ) {
			Log.info("IN ShowInImage Question type");
			if(answerDialogbox != null && answerDialogbox.getImagePolygonViewer() != null && answerDialogbox.getImagePolygonViewer().isValidPolygon()) {
				Log.info("Points : " + answerDialogbox.getImagePolygonViewer().getPoints());
				answerProxy.setPoints(answerDialogbox.getImagePolygonViewer().getPoints());
				Log.info("Points added");	
			}else {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.polygonErrorMessage());
				Log.error("Polygon is not property added. Try again");
				return;
			}
			
		}else if(question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) == true && answerDialogbox.getValidity() != null && Validity.Wahr.equals(answerDialogbox.getValidity().getValue())) {
			Log.info("IN Imgkey Question type");
			answerProxy.setAdditionalKeywords(answerDialogbox.getAdditionalKeywords().getValue());
			
			if(answerDialogbox != null && answerDialogbox.getImageRectangleViewer() != null) {
				Log.info("Points : " + answerDialogbox.getImageRectangleViewer().getPoint());
				answerProxy.setPoints(answerDialogbox.getImageRectangleViewer().getPoint());
				Log.info("Points added");
			}else {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.rectangleErrorMessage());
				Log.error("Rectangle is not property added. Try again");
				return;
			}
			
		}else if(question.getQuestionType() != null && QuestionTypes.MCQ.equals(question.getQuestionType().getQuestionType()) == true) {
			Log.info("IN MCQ Question type");
			if(answerDialogbox != null) {
				
				if(question != null && question.getQuestionType() != null && question.getQuestionType().getMultimediaType() != null) {
				
					switch (question.getQuestionType().getMultimediaType()) {
					case Image:
					{
						if(answerDialogbox.getSimpleImageViewer() != null && answerDialogbox.getSimpleImageViewer().getURL() != null && answerDialogbox.getSimpleImageViewer().getURL().length() > 0) {
							answerProxy.setMediaPath(answerDialogbox.getSimpleImageViewer().getURL());
						}else {
							ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.imageViewerError());
							Log.error("Error in imageview");
							return;
						}
						
						break;
					}
					case Sound:
					{
						if(answerDialogbox.getAudioViewer() != null && answerDialogbox.getAudioViewer().getURL() != null && answerDialogbox.getAudioViewer().getURL().length() > 0) {
							answerProxy.setMediaPath(answerDialogbox.getAudioViewer().getURL());
						}else {
							ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.audioViewerError());
							Log.error("Error in audioview.");
							return;
						}
						break;
					}
					case Video:
					{	
						if(answerDialogbox.getVideoViewer() != null && answerDialogbox.getVideoViewer().getURL() != null && answerDialogbox.getVideoViewer().getURL().length() > 0) {
							answerProxy.setMediaPath(answerDialogbox.getVideoViewer().getURL());
						}else {
							ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.audioViewerError());
							Log.error("Error in videoViewer. Try again");
							return;
						}
						break;
					}
					default:
					{
						ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.unknownMultimediaType());
						Log.error("Error in MultimediaType. Try again");
						return;
					}
					}
				}
						
			}else {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.unknownError());
				Log.error("answerDialogbox is null");
				return;
			}
			
		}else if(question.getQuestionType() != null && QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType()) == true) {
			answerProxy.setAdditionalKeywords(answerDialogbox.getAdditionalKeywords().getValue());
			answerDialogbox.getSequenceNumber().removeStyleName("higlight_onViolation");
			if(ClientUtility.isNumber(answerDialogbox.getSequenceNumber().getValue())) {
				answerProxy.setSequenceNumber(Integer.parseInt(answerDialogbox.getSequenceNumber().getValue(), 10));
			}else {
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.sequenceNumberError());
				answerDialogbox.getSequenceNumber().addStyleName("higlight_onViolation");
				Log.info("squence number is not valid number");
				return;
			}
		}
		
		answerProxy.setComment(commentProxy);
		answerProxy.setSubmitToReviewComitee(answerDialogbox.getSubmitToReviewerComitee().getValue());
		Log.info("before save");
		
		commnetRequest.persist().using(commentProxy).fire(new BMEReceiver<Void>(reciverMap) {

			public void onFailure(ServerFailure error){
				Log.info("on failure");
				ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
			
	@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				
				Iterator<ConstraintViolation<?>> iter = violations.iterator();
				String message = "";
				while(iter.hasNext()){
					ConstraintViolation<?> v = iter.next();
					message += v.getPropertyPath() + " : " + v.getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
				
				ErrorPanel erorPanel = new ErrorPanel();
	        	erorPanel.setWarnMessage(message);
	        	super.onConstraintViolation(violations);
			}

//	          @Override
//				public void onViolation(Set<Violation> errors) {
//	        	  Log.info("on violate");
//					Iterator<Violation> iter = errors.iterator();
//					String message = "";
//					while(iter.hasNext()){
//						message += iter.next().getPath() + "<br>";
//					}
//					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antworten auflisten -" + message);
//					
//		        	  
//					ErrorPanel erorPanel = new ErrorPanel();
//		        	  erorPanel.setErrorMessage(message);
//
//				}
	          
			@Override
			public void onSuccess(Void response) {
				
				ansRequest.persist().using(answerProxy).fire(new BMEReceiver<Void>(reciverMap) {

					@Override
					public void onSuccess(Void response) {
						Log.info("fullSaved");
			        	  
		        		initAnswerView();
		        		answerDialogbox.close();
					}
					public void onFailure(ServerFailure error){
						Log.info("on failure");
						ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setErrorMessage(error.getMessage());
							Log.error(error.getMessage());
						}
			          @Override
						public void onViolation(Set<Violation> errors) {
			        	  Log.info("on violate");
							Iterator<Violation> iter = errors.iterator();
							String message = "";
							while(iter.hasNext()){
								message += iter.next().getPath() + "<br>";
							}
							Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antworten auflisten -" + message);
							
				        	  
							ErrorPanel erorPanel = new ErrorPanel();
				        	  erorPanel.setErrorMessage(message);

						}
				});
		
			}
		});
				answerDriver.flush().fire(new Receiver<Void>() {
			
	          @Override
	          public void onSuccess(Void response) {
	        	  Log.info("fullSaved");
	        	  
	        		initAnswerView();
	        		answerDialogbox.close();
	          //	goTo(new PlaceAssesment(person.stableId()));
	          }
	          
	          public void onFailure(ServerFailure error){
	        	  ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
	          @Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antworten auflisten -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);

				}
	      }); 
	}*/
	

	@Override
	public void cancelAnswerClicked() {
		
		
	}

	@Override
	public void deleteAnswerClicked(AnswerProxy answer) {

		requests.answerRequest().eliminateAnswer().using(answer).fire(new BMEReceiver<Void>(reciverMap){

			@Override
			public void onSuccess(Void arg0) {
				initAnswerView();
				
			}
		});
		
	}

	/*@Override
	public void deleteSelectedQuestionResource(final Long qestionResourceId) {
		requests.questionResourceRequest().removeSelectedQuestionResource(qestionResourceId).fire(new BMEReceiver<Void>(reciverMap) {

			@Override
			public void onSuccess(Void response) {
				Log.info("Remove Resource.");				
			}
		});
		
	}

	@Override
	public void addNewQuestionResource(final QuestionResourceClient questionResourceClient) {

		final QuestionResourceRequest request  = requests.questionResourceRequest();
		
		QuestionResourceProxy proxy = request.create(QuestionResourceProxy.class);
		
		proxy.setPath(questionResourceClient.getPath());
		proxy.setQuestion(question);
		proxy.setSequenceNumber(questionResourceClient.getSequenceNumber());
		proxy.setType(questionResourceClient.getType());
		
		final QuestionResourceProxy proxy2 = proxy; 
		request.persist().using(proxy).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("Added new Question Resource");
				
				requests.questionResourceRequest().find(proxy2.stableId()).fire(new BMEReceiver<Object>(){

					@Override
					public void onSuccess(Object response) {
						QuestionResourceProxy resourceProxy = (QuestionResourceProxy) response;
						questionResourceClient.setId(resourceProxy.getId());
					}
				});
			}
		});
	}*/

	/*@Override
	public void updatePicturePathInQuestion(String picturePath) {
		
		if(question != null) {
			final QuestionRequest questionRequest = requests.questionRequest();
			
			QuestionProxy updatedQuestion = questionRequest.edit(question);
			updatedQuestion.setPicturePath(picturePath);
			questionRequest.persist().using(updatedQuestion).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					Log.info("Picture path updated.");
				}
			});
	
		}else {
			Log.error("Question is null");
		}
	}

	@Override
	public void deleteUploadedPicture(String picturePath) {
		
		if(question != null) {
			final QuestionRequest questionRequest = requests.questionRequest();
			questionRequest.deleteMediaFileFromDisk(picturePath).fire(new BMEReceiver<Boolean>(reciverMap) {

				@Override
				public void onSuccess(Boolean response) {
					Log.info("Picture deleted " + response);
				}

			});
		}else {
			Log.error("Question is null");
		}
	}*/

	/*@Override
	public void changedResourceSequence(Set<QuestionResourceClient> questionResourceClients) {
		
		if(question != null && questionResourceClients.size() > 0) {
		
			QuestionResourceRequest questionResourceRequest = requests.questionResourceRequest();
			Set<QuestionResourceProxy> proxies = Sets.newHashSet();
			Log.info("proxies.size() " + proxies.size());
			for (QuestionResourceClient questionResource : questionResourceClients) {

				if(questionResource.getState().equals(State.NEW) || questionResource.getState().equals(State.EDITED)) {
					QuestionResourceProxy proxy = questionResourceRequest.create(QuestionResourceProxy.class);
					proxy.setPath(questionResource.getPath());
					proxy.setSequenceNumber(questionResource.getSequenceNumber());
					proxy.setType(questionResource.getType());
					proxy.setQuestion(question);
					proxies.add(proxy);
				}
			}
			
			questionResourceRequest.persistSet(proxies).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					Log.info("sequence changed successfuly");
				}
				
				@Override
				public void onConstraintViolation(
						Set<ConstraintViolation<?>> violations) {
					
					Iterator<ConstraintViolation<?>> iter = violations.iterator();
					String message = "";
					while(iter.hasNext()){
						ConstraintViolation<?> v = iter.next();
						message += v.getPropertyPath() + " : " + v.getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
					
					ErrorPanel erorPanel = new ErrorPanel();
		        	erorPanel.setWarnMessage(message);
		        	super.onConstraintViolation(violations);
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					ErrorPanel erorPanel = new ErrorPanel();
		        	erorPanel.setErrorMessage(error.getMessage());
		        	super.onFailure(error);
				}
			});

		}
	}*/

	@Override
	public void findAllAnswersPoints(Long questionId,Long currentAnswerId,final Function<List<String>, Void> function) {
		
		requests.answerRequest().findAllAnswersPoints(questionId,currentAnswerId).fire(new BMEReceiver<List<String>>() {

			@Override
			public void onSuccess(List<String> polygons) {
		
				function.apply(polygons);
			}
		});

		
	}

	/*@Override
	public void saveMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy,Matrix<MatrixValidityVO> matrixList, PersonProxy author,PersonProxy rewiewer, Boolean submitToReviewComitee, String comment) {
		
		MatrixValidityRequest validityRequest = requests.MatrixValidityRequest();
		for (MatrixValidityVO vo : matrixList) {
			
			Log.info(vo.toString());

			if(vo.getMatrixValidityProxy() != null) {
				
				MatrixValidityProxy proxy = validityRequest.edit(vo.getMatrixValidityProxy());
				proxy.setValidity(vo.getValidity());
				
				proxy.getAnswerX().setDateChanged(new Date());
				proxy.getAnswerX().setAutor(author);
				proxy.getAnswerX().setRewiewer(rewiewer);
				proxy.getAnswerX().setSubmitToReviewComitee(submitToReviewComitee);
				proxy.getAnswerX().setIsAnswerAcceptedAdmin(false);
				proxy.getAnswerX().setIsAnswerAcceptedReviewWahrer(false);
				//proxy.getAnswerX().setIsAnswerActive(false);
				proxy.getAnswerX().getComment().setComment(comment);
				proxy.getAnswerX().setQuestion(question);
				proxy.getAnswerX().setValidity(vo.getValidity());
				proxy.getAnswerX().setStatus(Status.NEW);
				
				proxy.getAnswerY().setDateChanged(new Date());
				proxy.getAnswerY().setAutor(author);
				proxy.getAnswerY().setRewiewer(rewiewer);
				proxy.getAnswerY().setSubmitToReviewComitee(submitToReviewComitee);
				proxy.getAnswerY().setIsAnswerAcceptedAdmin(false);
				proxy.getAnswerY().setIsAnswerAcceptedReviewWahrer(false);
				//proxy.getAnswerY().setIsAnswerActive(false);
				proxy.getAnswerY().getComment().setComment(comment);
				proxy.getAnswerY().setQuestion(question);
				proxy.getAnswerY().setValidity(vo.getValidity());
				proxy.getAnswerY().setStatus(Status.NEW);
				
				validityRequest.persist().using(proxy);
			}
		}
		
		validityRequest.fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				
				Log.info("save done for matrix validity");
				initMatrixAnswerView();
			}
		});
	}*/

	@Override
	public void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, final PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords,Integer sequenceNumber, final Function<AnswerProxy, Void> function) {
		
		Log.info("in saveAnswerProxy method");
		
		final AnswerRequest answerRequest = requests.answerRequest();
		
		if(answerProxy != null) {
			AnswerProxy newAnswerProxy  = answerRequest.edit(answerProxy);
			newAnswerProxy.setDateChanged(new Date());
			
			fillAnswerData(answerText, author, rewiewer, submitToReviewComitee, comment, validity, points, mediaPath, additionalKeywords, sequenceNumber, newAnswerProxy);
					
			final AnswerProxy finalAnswerProxy = newAnswerProxy;
			answerRequest.persist().using(newAnswerProxy).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					if(rewiewer != null)
						Cookies.setCookie(McAppConstant.LAST_SELECTED_ANSWER_REVIEWER, String.valueOf(rewiewer.getId()), ClientUtility.getDateFromOneYear());
					
					function.apply(finalAnswerProxy);
					initAnswerView();
				}
			});
		}else {
			AnswerProxy newAnswerProxy = answerRequest.create(AnswerProxy.class);			
			newAnswerProxy.setDateAdded(new Date());
			
			fillAnswerData(answerText, author, rewiewer, submitToReviewComitee, comment, validity, points, mediaPath, additionalKeywords, sequenceNumber, newAnswerProxy);
			
			final AnswerProxy finalAnswerProxy = newAnswerProxy;
			
			answerRequest.persist().using(finalAnswerProxy).fire(new BMEReceiver<Void>(reciverMap) {
	
				@Override
				public void onSuccess(Void response) {
					
					requests.answerRequest().find(finalAnswerProxy.stableId()).with("autor","rewiewer").fire(new BMEReceiver<Object>() {
	
						@Override
						public void onSuccess(Object response) {
							if(response instanceof AnswerProxy) {
								if(rewiewer != null)
									Cookies.setCookie(McAppConstant.LAST_SELECTED_ANSWER_REVIEWER, String.valueOf(rewiewer.getId()), ClientUtility.getDateFromOneYear());
								
								function.apply((AnswerProxy) response);	
								initAnswerView();
							}		
						}
					});
						
				}
			});
				
		}

	}

	public void fillAnswerData(String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords, Integer sequenceNumber, AnswerProxy newAnswerProxy) {
		newAnswerProxy.setAnswerText(answerText);
		newAnswerProxy.setAutor(author);
		newAnswerProxy.setRewiewer(rewiewer);
		newAnswerProxy.setSubmitToReviewComitee(submitToReviewComitee);
		newAnswerProxy.setIsAnswerAcceptedAutor(userLoggedIn.getId().equals(author.getId()));
		newAnswerProxy.setIsAnswerAcceptedAdmin(isAdminOrInstitutionalAdmin());
		newAnswerProxy.setIsAnswerAcceptedReviewWahrer(false);
		newAnswerProxy.setComment(comment);
		newAnswerProxy.setQuestion(question);
		newAnswerProxy.setValidity(validity);
		newAnswerProxy.setPoints(points);
		newAnswerProxy.setMediaPath(mediaPath);
		newAnswerProxy.setIsMedia(mediaPath != null && mediaPath.length() > 0);
		newAnswerProxy.setAdditionalKeywords(additionalKeywords);
		newAnswerProxy.setSequenceNumber(sequenceNumber);
		
		if ((isAdminOrInstitutionalAdmin())) {
			newAnswerProxy.setStatus(Status.ACCEPTED_ADMIN);
		} else {
			newAnswerProxy.setStatus(Status.NEW);
		}
	}

	/*@Override
	public void saveMatrixValidityValue(final MatrixValidityVO matrixValidityVO, Validity validity, final Function<MatrixValidityProxy, Void> function) {
		
		if(matrixValidityVO.getAnswerX().getAnswerProxy() != null && matrixValidityVO.getAnswerY().getAnswerProxy() != null) {
			final MatrixValidityRequest matrixValidityRequest = requests.MatrixValidityRequest();
			MatrixValidityProxy proxy = null;
			if(matrixValidityVO.getMatrixValidityProxy() != null) {
				// update
				proxy = matrixValidityRequest.edit(matrixValidityVO.getMatrixValidityProxy());
				
			}else {
				// update
				proxy = matrixValidityRequest.create(MatrixValidityProxy.class);
			}
			
			proxy.setAnswerX(matrixValidityVO.getAnswerX().getAnswerProxy());
			proxy.setAnswerY(matrixValidityVO.getAnswerY().getAnswerProxy());
			proxy.setValidity(validity);
			final MatrixValidityProxy proxy2 = proxy;
			matrixValidityRequest.persist().using(proxy).fire(new BMEReceiver<Void>(reciverMap) {

				@Override
				public void onSuccess(Void response) {
					if(matrixValidityVO.getMatrixValidityProxy() == null) {
						requests.MatrixValidityRequest().find(proxy2.stableId()).with("answerX","answerY","answerX.autor","answerX.rewiewer","answerX.comment","answerY.autor","answerY.rewiewer","answerY.comment").fire(new BMEReceiver<Object>() {

							@Override
							public void onSuccess(Object response) {

								if(response instanceof MatrixValidityProxy) {
									function.apply((MatrixValidityProxy) response);			
								}
							}
						});
						
					}else {
						function.apply(proxy2);		
					}
					initMatrixAnswerView();
				}
			});
			
		}else {
			Log.error("Error in saveMatrixValidityValue method");
		}
		
	}*/

	@Override
	public void deletedSelectedAnswer(AnswerProxy answerProxy, Boolean isAnswerX,final Function<Boolean, Void> function) {
		requests.MatrixValidityRequest().deleteAnswerAndItsMatrixValidity(answerProxy.getId(),isAnswerX).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				function.apply(response);
			}		
		});
	}

	/*@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {
		QuestionRequest questionRequest = requests.questionRequest();
		proxy = questionRequest.edit(proxy);
		
		if (userLoggedIn.getIsAdmin())
		{
			proxy.setIsAcceptedAdmin(true);
			
			if (proxy.getIsAcceptedRewiever())
			{
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			}
			else
				proxy.setStatus(Status.ACCEPTED_ADMIN);
		}
		else
		{
			proxy.setIsAcceptedRewiever(true);
			
			if (proxy.getIsAcceptedAdmin())
			{
				proxy.setStatus(Status.ACTIVE);
				proxy.setIsActive(true);
			}
			else
				proxy.setStatus(Status.ACCEPTED_REVIEWER);
		}
		
		questionRequest.persist().using(proxy).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				placeController.goTo(new PlaceAcceptQuestion(""));
			}
		});
	}*/
	
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
	
	private void initMatrixAnswerView()
	{
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
				ActivityQuestionDetails.this.onMatrixAnswerTableRangeChanged();
			}
		});
		
	}
	
	private void onMatrixAnswerTableRangeChanged()
	{
		final Range range = view.getMatrixAnswerListViewImpl().getTable().getVisibleRange();
		requests.MatrixValidityRequest().findAllMatrixValidityForAcceptQuestion(question.getId(), range.getStart(), range.getLength()).with("answerX","answerY").fire(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
				view.getMatrixAnswerListViewImpl().getTable().setRowData(range.getStart(), response);
			}
		});
	}

	@Override
	public void addMatrixNewAnswerClicked() {
		
		if(question.getQuestionType() != null && QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType()) == true) {
			openMatrixAnswerView(null, true, true, true);
			
			/*final MatrixAnswerView matrixAnswerView = new MatrixAnswerViewImpl(question);
			matrixAnswerView.setDelegate(this);
			
//			matrixAnswerView.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
			PersonRequest personRequest = requests.personRequest();
	        personRequest.findAllPeople().with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                matrixAnswerView.setRewiewerPickerValues(values);
	                matrixAnswerView.setAutherPickerValues(values,userLoggedIn,isAdminOrInstitutionalAdmin());
	            }
	        });
	        
	       // answerDialogbox.setAutherPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                matrixAnswerView.setAutherPickerValues(values,userLoggedIn);
	            }
	        });
			
	        MatrixValidityRequest matrixValidityRequest = personRequest.append(requests.MatrixValidityRequest());
			matrixValidityRequest.findAllMatrixValidityForQuestion(question.getId()).with("answerX","answerY","answerX.autor","answerX.rewiewer","answerX.comment","answerY.autor","answerY.rewiewer","answerY.comment").to(new BMEReceiver<List<MatrixValidityProxy>>() {

				@Override
				public void onSuccess(List<MatrixValidityProxy> response) {
										
			        matrixAnswerView.setValues(response);
			        matrixAnswerView.display();
				}
			});			
			
			matrixValidityRequest.fire();*/
		}
	}

	private AnswerDialogbox openAnswerView(final AnswerProxy answer) {
		
		final AnswerDialogbox answerDialogbox = new AnswerDialogboxImpl(question,eventBus,reciverMap);
		answerDialogbox.setDelegate(this);
		
		// because display need to be called after author and reviewer list.  
		/*final Function<Void, Void> sync = new Function<Void, Void>() {
			
			private int count = 0;  
			@Override
			public Void apply(Void input) {
				count++;
				if(count == 2) {
					if(answer != null) {
			        	answerDialogbox.setValues(answer);
			        }
			        answerDialogbox.display(question.getQuestionType().getQuestionType());
				}
				return null;
			}
		};*/ 
		
		
		answerDialogbox.setValidityPickerValues(Arrays.asList(Validity.values()));
		//answerDialogbox.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
		
		PersonRequest personRequest = requests.personRequest();
        personRequest.findAllPeople().with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                answerDialogbox.setRewiewerPickerValues(values);                
                answerDialogbox.setAutherPickerValues(values,userLoggedIn,isAdminOrInstitutionalAdmin());
                
                if(answer != null) {
		        	answerDialogbox.setValues(answer);
		        }
		        answerDialogbox.display(question.getQuestionType().getQuestionType());
               // sync.apply(null);
            }
        });
       /* requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                answerDialogbox.setAutherPickerValues(values,userLoggedIn);
                sync.apply(null);
            }
        });*/
	      
        AnswerRequest answerRequest = null;
        Long answerId = answer != null ? answer.getId() : null;
        Long questionId = question!= null ? question.getId() : null;
        if(question != null && question.getQuestionType()!= null && question.getQuestionType().getQuestionType() != null) {
        	if(QuestionTypes.Textual.equals(question.getQuestionType().getQuestionType()) || QuestionTypes.Sort.equals(question.getQuestionType().getQuestionType())) {
        		
        		answerRequest = personRequest.append(requests.answerRequest());
        		answerRequest.maxDifferenceBetweenAnswerForQuestion(answerId,questionId).to(new BMEReceiver<List<Long>>() {

        			@Override
        			public void onSuccess(List<Long> response) {
        				
        				if(response.size() == 2) {
        					long diff1 = response.get(0);
        					long diff2 = response.get(1);
        					if(diff1 > diff2) {
        						answerDialogbox.setMaxDifferenceBetween(diff1,diff2);	
        					}else {
        						answerDialogbox.setMaxDifferenceBetween(diff2,diff1);
        					}
        						
        				}
        				
        			}
        		});
        	}
        }
        
        if (answerRequest == null)
        	personRequest.fire();
        else
        	answerRequest.fire();
        
        
        return answerDialogbox;
	}
	
	@Override
	public void editAnswerClicked(final AnswerProxy answer) {
		requests.answerToAssQuestionRequest().countAnswerToAssQuestionByAnswer(answer.getId()).fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				if(response != null && response == 0) {
					if (question.getQuestionType() != null && (QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) == true || QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) == true))
					{
						openImageKeyOrShowInInImageAnswerView(answer);
					} else {
						openAnswerView(answer);
					}
				}else {
					ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.answerUsedInAssessment());
				}		
			}
		});
	}

	private void openMatrixAnswerView(final MatrixValidityProxy matrixValidity, final boolean isNew, final boolean isEdit, final boolean isDelete) {
		final MatrixAnswerView matrixAnswerView = new MatrixAnswerViewImpl(question);
		matrixAnswerView.setDelegate(this);
				
		matrixAnswerView.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
		PersonRequest personRequest = requests.personRequest();
        personRequest.findAllPeople().with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                matrixAnswerView.setRewiewerPickerValues(values);
            }
        });
        
       // answerDialogbox.setAutherPickerValues(Collections.<PersonProxy>emptyList());
        PersonRequest newPersonRequest = personRequest.append(requests.personRequest());
        newPersonRequest.findAllPeople().with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).to(new BMEReceiver<List<PersonProxy>>() {

            public void onSuccess(List<PersonProxy> response) {
                List<PersonProxy> values = new ArrayList<PersonProxy>();
                values.add(null);
                values.addAll(response);
                matrixAnswerView.setAutherPickerValues(values,userLoggedIn,isAdminOrInstitutionalAdmin());
            }
        });
		
        MatrixValidityRequest matrixValidityRequest = newPersonRequest.append(requests.MatrixValidityRequest());
        matrixValidityRequest.findAllMatrixValidityForQuestion(question.getId()).with("answerX","answerY","answerX.autor","answerX.rewiewer","answerY.autor","answerY.rewiewer").to(new BMEReceiver<List<MatrixValidityProxy>>() {

			@Override
			public void onSuccess(List<MatrixValidityProxy> response) {
									
		        matrixAnswerView.setValues(response, isNew, isEdit, isDelete);
		        matrixAnswerView.display();
			}
		});
        
        matrixValidityRequest.fire();
	}
	
	@Override
	public void editMatrixValidityClicked(final MatrixValidityProxy matrixValidity) {
		requests.answerToAssQuestionRequest().countAnswerToAssQuestionByMatrixValidity(question.getId()).fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				if(response != null && response == 0) {
					openMatrixAnswerView(matrixValidity,false, true,false);
				}else {
					ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.matrixAnswerUsedInAssessment());
				}		
			}
		});
	}

	@Override
	public void deleteMatrixValidityClicked(MatrixValidityProxy matrixValidity) {
		openMatrixAnswerView(matrixValidity, false, false, true);
	}

	@Override
	public void closedMatrixValidityView() {
		initMatrixAnswerView(); // refresh matrix list view.
	}

	@Override
	public void enableBtnOnLatestClicked() {
		editDeleteBtnFlag = false;
		editDeleteBtnFlag = hasQuestionWriteRights(question);
		view.setVisibleEditAndDeleteBtn(editDeleteBtnFlag);
	}
	
	// updated by subclass ActivityAcceptQuestionDetails
	@Override
	public void acceptQuestionClicked(QuestionProxy proxy) {
	}

	@Override
	public void placeChanged(Place place) {
		//updateSelection(event.getNewPlace());
		// TODO implement
	}

	// updated by subclass ActivityAcceptQuestionDetails
	@Override
	public void onResendToReviewClicked(QuestionProxy proxy) {
	}
	
	//updated by subclass
	@Override
	public void checkForResendToReview() {		
	}

	@Override
	public void forcedActiveClicked() {
		// not to implement here this is for ActivityNotActivatedQuestionDetails.class
	}
	
	QuestionProxy tempQuestionProxy = null;
	private Long mainClassificationId = null;
	private Long classificationTopicId = null;
	private Long topicId = null;
	private Long skillLevelId = null;
	private Long applianceId = null;
	private LearningObjectiveView learningObjectiveView;
	private LearningObjectiveData learningObjective;
	private String temp;
	private List<LearningObjectiveData> learningObjectiveData = new ArrayList<LearningObjectiveData>();
	private AssesmentQuestionProxy assesmentQuestionProxy;
	
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
			requests.keywordRequest().findKeywordByStringOrAddKeyword(text, proxy).with("previousVersion","keywords","questEvent","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<QuestionProxy>() {

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
		
		requests.keywordRequest().deleteKeywordFromQuestion(keyword, proxy).with("previousVersion","keywords","questEvent","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<QuestionProxy>() {

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
	public void saveAllTheValuesToAnswerAndMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList, PersonProxy author, final PersonProxy rewiewer, Boolean submitToReviewComitee, String comment) {
		
		final AnswerRequest answerRequest = requests.answerRequest();
		final MatrixValidityRequest validityRequest = answerRequest.append(requests.MatrixValidityRequest());
		
		Map<Integer,AnswerProxy> answerXIndex = Maps.newHashMap();
		Map<Integer,AnswerProxy> answerYIndex = Maps.newHashMap();
		
		for (Entry<Integer, Map<Integer, MatrixValidityVO>> rowset : matrixList.getAllRows()) {
			
			Integer xIndex = rowset.getKey(); 
			
			for (Entry<Integer, MatrixValidityVO> currentColumn : rowset.getValue().entrySet()) {
				
				Integer yIndex = currentColumn.getKey();
				
				MatrixValidityVO vo = currentColumn.getValue();
				
				Log.info(vo.toString());
				MatrixValidityProxy proxy ;
				if(vo.getMatrixValidityProxy() != null) {
					proxy = validityRequest.edit(vo.getMatrixValidityProxy());
				}else {
					proxy = validityRequest.create(MatrixValidityProxy.class);
				}
				
				proxy.setValidity(vo.getValidity());
				if(proxy.getAnswerX() != null) {
					final AnswerProxy answerX = proxy.getAnswerX();
					answerX.setDateChanged(new Date());
					answerX.setComment(comment);
					
					setMatrixAnswerValues(author, rewiewer, submitToReviewComitee, vo, answerX, vo.getAnswerX());
					answerXIndex.put(xIndex,answerX);
				} else {
					
					if(answerXIndex.containsKey(xIndex) == false) {
					
						AnswerProxy newAnswerProxy = answerRequest.create(AnswerProxy.class);
						newAnswerProxy.setComment(comment);
						
						newAnswerProxy.setDateAdded(new Date());
						setMatrixAnswerValues(author, rewiewer, submitToReviewComitee, vo, newAnswerProxy, vo.getAnswerX());
						
						proxy.setAnswerX(newAnswerProxy);
						answerXIndex.put(xIndex,newAnswerProxy);
						
						answerRequest.persist().using(newAnswerProxy);
					} else {
						proxy.setAnswerX(answerXIndex.get(xIndex));
					}
					
				}
				
				if(proxy.getAnswerY() != null) {
				
					AnswerProxy answerY = proxy.getAnswerY();
					answerY.setDateChanged(new Date());
					answerY.setComment(comment);
					setMatrixAnswerValues(author, rewiewer, submitToReviewComitee, vo, answerY, vo.getAnswerY());
					answerYIndex.put(yIndex,proxy.getAnswerY());
				}else {

					if(answerYIndex.containsKey(yIndex) == false) {
					
						AnswerProxy newAnswerProxy = answerRequest.create(AnswerProxy.class);
						newAnswerProxy.setComment(comment);
						
						proxy.setAnswerY(newAnswerProxy);
						
						newAnswerProxy.setDateAdded(new Date());
						setMatrixAnswerValues(author, rewiewer, submitToReviewComitee, vo, newAnswerProxy, vo.getAnswerY());
						answerYIndex.put(yIndex,newAnswerProxy);
						
						answerRequest.persist().using(newAnswerProxy);

					} else {
						proxy.setAnswerY(answerYIndex.get(yIndex));
					}
										
				}
								
				validityRequest.persist().using(proxy);

			}
		}
		
		validityRequest.fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				if(rewiewer != null)
					Cookies.setCookie(McAppConstant.LAST_SELECTED_ANSWER_REVIEWER, String.valueOf(rewiewer.getId()), ClientUtility.getDateFromOneYear());
				
				Log.info("save done for matrix validity");
				initMatrixAnswerView();
			}
		});
	}

	private void setMatrixAnswerValues(PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, MatrixValidityVO vo, final AnswerProxy answer, AnswerVO answerVO) {
		answer.setAnswerText(answerVO.getAnswer());
		answer.setAutor(author);
		answer.setRewiewer(rewiewer);
		answer.setSubmitToReviewComitee(submitToReviewComitee);
		answer.setIsAnswerAcceptedAdmin(isAdminOrInstitutionalAdmin());
		answer.setIsAnswerAcceptedReviewWahrer(false);
		answer.setIsAnswerAcceptedAutor(userLoggedIn.getId().equals(author.getId()));
		answer.setQuestion(question);
		answer.setValidity(vo.getValidity());
		
		if ((isAdminOrInstitutionalAdmin())) {
			answer.setStatus(Status.ACCEPTED_ADMIN);
		} else {
			answer.setStatus(Status.NEW);
		}
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
		//Added this to show main classification in ASC order - Manish.
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
		// Added this to show all skill level by its level as ASC - Manish.
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
		// Added this to show all appliances by it shortcut as ASC - Manish.
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

	private void initUsedInMCView() {
		RecordChangeEvent.register(requests.getEventBus(), view.getQuestionUsedInMC());
		if (question != null)
		{
			requests.assesmentQuestionRequest().countAllAssesmentQuestionByQuestion(question.getId()).fire(new BMEReceiver<Integer>() {

				@Override
				public void onSuccess(Integer response) {
					view.getQuestionUsedInMC().setTableRowCount(response);
					onUsedInMcTableRangeChanged();
				}
			});
					
			view.getQuestionUsedInMC().addTableRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onUsedInMcTableRangeChanged();
				}

				
			});
			
			ProvidesKey<AssesmentQuestionProxy> keyProvider = ((AbstractHasData<AssesmentQuestionProxy>) view.getQuestionUsedInMC().getTable()).getKeyProvider();
			final SingleSelectionModel<AssesmentQuestionProxy> selectionModel = new SingleSelectionModel<AssesmentQuestionProxy>(keyProvider);
			
			view.getQuestionUsedInMC().getTable().setSelectionModel(selectionModel);		

			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							AssesmentQuestionProxy selectedObject = selectionModel.getSelectedObject();
							if (selectedObject != null) {
								Log.debug(selectedObject.getId() + " selected!");
								showUsedInMcAnswerDetails(selectedObject);
							}
						}
					});

			
		}
	}
	
	protected void showUsedInMcAnswerDetails(AssesmentQuestionProxy assesmentQuestionProxy) {
		
		if(assesmentQuestionProxy != null) {
			this.assesmentQuestionProxy = assesmentQuestionProxy;
			
			if (answerRangeChangeHandler!=null){
				answerRangeChangeHandler.removeHandler();
				answerRangeChangeHandler=null;
			}
			
			requests.answerToAssQuestionRequest().countAllAnswerToAssQuestion(assesmentQuestionProxy.getId()).fire(new BMEReceiver<Long>() {

				@Override
				public void onSuccess(Long response) {
					answerTable.setRowCount(response.intValue(), true);

					onUsedInMcAnswerTableRangeChanged();
				}
			});
			
			
			answerRangeChangeHandler =  answerTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				public void onRangeChange(RangeChangeEvent event) {
					ActivityQuestionDetails.this.onUsedInMcAnswerTableRangeChanged();
				}
			});
			answerListView.hideAddAnswer();
			answerListView.showAllBtn();
			
		}
		
	}

	private void onUsedInMcAnswerTableRangeChanged() {
		
		final Range range = answerTable.getVisibleRange();
		
		requests.answerToAssQuestionRequest().findAllAnswerToAssQuestion(this.assesmentQuestionProxy.getId(), range.getStart(), range.getLength()).with("question","rewiewer","autor","question.questionType").fire( new BMEReceiver<List<AnswerProxy>>(){


			@Override
			public void onSuccess(List<AnswerProxy> response) {
				answerTable.setRowData(range.getStart(), response);
			}
		});
		
	}
	
	private void onUsedInMcTableRangeChanged() {
		if (question != null)
		{
			final Range range = view.getQuestionUsedInMC().getTableVisibleRange();
			requests.assesmentQuestionRequest().findAllAssesmentQuestionByQuestion(question.getId(), range.getStart(), range.getLength()).with(view.getQuestionUsedInMC().getPaths()).fire(new BMEReceiver<List<AssesmentQuestionProxy>>() {

				@Override
				public void onSuccess(List<AssesmentQuestionProxy> response) {
					view.getQuestionUsedInMC().setTableRowData(range.getStart(), response);
				}
			});
		}
		
	}
	@Override
	public void acceptQueAnswersClicked() {}

	@Override
	public void showAllClicked() {
		initAnswerView();
	}

}

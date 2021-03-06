package medizin.client.activites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceBookAssesmentDetails;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AnswerToAssQuestionProxy;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.AnswerToAssQuestionRequest;
import medizin.client.request.AssesmentQuestionRequest;
import medizin.client.request.AssesmentRequest;
import medizin.client.request.QuestionEventRequest;
import medizin.client.request.QuestionSumPerPersonRequest;
import medizin.client.ui.AssesmenBookDialogbox;
import medizin.client.ui.AssesmenBookDialogboxImpl;
import medizin.client.ui.dnd3.ui.AnswerView;
import medizin.client.ui.dnd3.ui.AnswerViewImpl;
import medizin.client.ui.dnd3.ui.EventView;
import medizin.client.ui.dnd3.ui.EventViewImpl;
import medizin.client.ui.dnd3.ui.QuestionTypeDNDView;
import medizin.client.ui.dnd3.ui.QuestionTypeDNDViewImpl;
import medizin.client.ui.dnd3.ui.QuestionView;
import medizin.client.ui.dnd3.ui.QuestionViewImpl;
import medizin.client.ui.view.BookAssesmentDetailsView;
import medizin.client.ui.view.BookAssesmentDetailsViewImpl;
import medizin.client.ui.widget.process.AppLoader;
import medizin.client.ui.widget.process.ApplicationLoadingView;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
/**
 * Provides Activity for BookAssemsentDetailsView, makes extensive use of  gwt-dnd ( <a href="http://code.google.com/p/gwt-dnd">Drag-and-drop Library for Google-Web-toolkit</a>). Allows to
 * sort elements of an assesment-book. All the design elemnts of draggable Elements (questiontype counts(Fragetypen), question events(Themengebiete),questions and answers are excluded in view-classes. 
 * @author masterthesis
 *
 */

public class ActivityBookAssementDetails extends AbstractActivityWrapper implements DragHandler, AssesmenBookDialogbox.Delegate, BookAssesmentDetailsView.Presenter, BookAssesmentDetailsView.Delegate, AnswerView.Delegate, QuestionTypeDNDView.Delegate, EventView.Delegate, QuestionView.Delegate {

	private PlaceBookAssesmentDetails bookAssmentPlace;
	private AcceptsOneWidget widget;
	private BookAssesmentDetailsView view;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private List<QuestionViewImpl> questionsViewContainer = new ArrayList<QuestionViewImpl>();

	@Inject
	public ActivityBookAssementDetails(PlaceBookAssesmentDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.bookAssmentPlace = place;
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
	
	private AssesmentProxy assesment;
	private BookAssesmentDetailsView bookAssesmentViewDetails;
	//private LoadingPopUp loadingPopup = new LoadingPopUp();
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	
	/**
	 * Starts up activity.
	 */
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
	
		/**
		 * Shows pop-up with animated gif during startup.
		 */
		//loadingPopup.show();
		Log.debug("Inside ActivityBookAssementDetails ");
		bookAssesmentViewDetails = new BookAssesmentDetailsViewImpl(requests, placeController);
		bookAssesmentViewDetails.setPresenter(this);
		bookAssesmentViewDetails.setDelegate(this);
		this.widget = widget;
		this.view = bookAssesmentViewDetails;
        widget.setWidget(bookAssesmentViewDetails.asWidget());
        DOM.setElementAttribute(bookAssesmentViewDetails.getScrollContainer().getElement(), "style", "position: absolute; overflow: auto; left: 0px; top: 35px; right: 50px; bottom: 0px;width: 1200px");
        
        /*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {

			}
		});*/
        /**
         * Requests AssesmentProxy for this assesment-book.
         */
		requests.find(bookAssmentPlace.getProxyId()).fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				if(response instanceof AssesmentProxy){
					Log.info(((AssesmentProxy) response).getName());
					assesment = (AssesmentProxy) response;
					init();
				}
			}
		});
				
	}//End public void start
		
		

	@Override
	public void goTo(Place place) {
		 placeController.goTo(place);
	}
	
		
	/**
	 * Load all elements for assesementProxy.
	 */
	protected void init() {
		
		bookAssesmentViewDetails.getWorkingArea().clear();
		AppLoader.setNoLoader();
		bookAssesmentViewDetails.addButtons(assesment.getDisallowSorting());		
		requests.questionTypeCountPerExamRequest().findQuestionTypesCountSortedByAssesmentNonRoo(assesment.getId()).with("questionTypesAssigned").fire(new BMEReceiver<List<QuestionTypeCountPerExamProxy>>() {
			
			@Override
			public void onSuccess(List<QuestionTypeCountPerExamProxy> values) {
				if (view == null) {
					// This activity is dead
					Log.debug("view ist null");
					return;
				}
				
				Log.debug("Liste QuestionTYpeCountPerExamproxy-Size: "+ values.size());
				
				Iterator<QuestionTypeCountPerExamProxy> iterQuestionTypeCount = values.iterator();							
				
				while(iterQuestionTypeCount.hasNext()){
					
					QuestionTypeCountPerExamProxy questionTypeCount = iterQuestionTypeCount.next();
					
					Set<QuestionTypeProxy> questionTypesAssigned = questionTypeCount.getQuestionTypesAssigned();
					
					Log.debug("Set QuestionTypeProxy Gr�sse: "+questionTypesAssigned.size());
																							
					fillQuestionType(questionTypeCount);
				}
				
//				if (questionEventRequest != null)
//					questionEventRequest.fire();
				
				
			if (widget != null) {
		          widget.setWidget(view.asWidget());
				}
			}
		});
		/**
		 * Load all questionTypeCountRequest-objects for this assesment sorted by the attribute sort_order.
		 */
		
	
	}//End init

		

/**
 * Change sort_order of an questionTypeCountPerExamRequest-object.
 * @param questionTypeCount
 */
public void moveQuestionTypeCountPerExamRequestDown(QuestionTypeCountPerExamProxy questionTypeCount) {
	requests.questionTypeCountPerExamRequest().moveUp().using(questionTypeCount).fire(new BMEReceiver<Void>() {
		
          @Override
          public void onSuccess(Void response) {
        	  Log.info("movedDown");
 
          }
          
          /*@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while(iter.hasNext()){
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);

				
			}*/
      });
	
}

/**
 * Change sort_order of an questionTypeCountPerExamRequest-object.
 * @param questionTypeCount
 */
public void moveQuestionTypeCountPerExamRequestUp(QuestionTypeCountPerExamProxy questionTypeCount) {
	requests.questionTypeCountPerExamRequest().moveDown().using(questionTypeCount).fire(new BMEReceiver<Void>() {
		
          @Override
          public void onSuccess(Void response) {
        	  Log.info("movedUp");
          }
          
          /*@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while(iter.hasNext()){
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
				
			}*/
      });
	
}

	
	/**
	 * Insert design-elements for questionTypeCountPerExamProxy
	 * @param questionEventRequest 
	 * 
	 */
	
	
	protected void fillQuestionType(QuestionTypeCountPerExamProxy questionTypeCountProxy){
						
		final AbsolutePanel workingArea = bookAssesmentViewDetails.getWorkingArea();		
		/**
		 * New QuestionTypeDNDViewImpl is used as container for question-types
		 */

		QuestionTypeDNDViewImpl questionTypeView = new QuestionTypeDNDViewImpl();
						
		questionTypeView.setDelegate(this);
		
		questionTypeView.setProxy(questionTypeCountProxy);
		
		questionTypeView.init();
		
		questionTypeView.setEventsContainer(questionTypeView.getEventsContainer());
		
		
		/**
		 * Request all question Events for each questionTypeProxy
		 */
		
		//questionEventRequest.findAllQuestionEventsByQuestionTypeAndAssesmentID(assesment.getId(), questionTypesId  ).to(new BMEReceiver <java.util.List<medizin.client.proxy.QuestionEventProxy>>(){

		/*	@Override
			public void onSuccess(List<QuestionEventProxy> response) {
		
				AssesmentQuestionRequest assesmentQuestionRequest = null;
				Iterator<QuestionEventProxy> iterQuestionEventProxy = response.iterator();
				while(iterQuestionEventProxy.hasNext()){
					QuestionEventProxy questionEventProxy = iterQuestionEventProxy.next();
				
					/**
					 * For each questionEvent in callback setup event-area
					 * /
					
					if (assesmentQuestionRequest == null)
						assesmentQuestionRequest = requests.assesmentQuestionRequest();
					else
						assesmentQuestionRequest = assesmentQuestionRequest.append(requests.assesmentQuestionRequest());
					
					insertQuestionEvents(questionEventProxy,  eventsContainer, /*eventDragController,* / questionTypesId, assesmentQuestionRequest);
				}
				if (assesmentQuestionRequest != null)
					assesmentQuestionRequest.fire();
			} */
			
		//}
	//	);
		
		/**
		 * Add container for question types (Fragetypen) to main panel.
		 */
		workingArea.add(questionTypeView);
		}//End fillQuestion	

		protected void fillAnswers(VerticalPanel questionVert, AnswerToAssQuestionProxy answerToAssQuest, AnswerProxy answerProxy){
							
			AnswerViewImpl answerUi = new AnswerViewImpl();
		
			answerUi.setProxy(answerProxy);
			
			answerUi.setAnswerToAssQueston(answerToAssQuest);
			
			answerUi.init();
			
			answerUi.setDelegate(ActivityBookAssementDetails.this);						 
			
			questionVert.add(answerUi.asWidget());
						
		}
	
		
		
		
		/**initialized answer
		 * */
		protected void initAnswer(AssesmentQuestionProxy assQuestionProxy, QuestionViewImpl questionView){
				findAnswerToAssQuestionByAssesmentQuestion(assQuestionProxy, questionView.getAnswerVerticalPanel());	
		}
		
		/**
		 * 
		 * */		
		protected void findAnswerToAssQuestionByAssesmentQuestion(AssesmentQuestionProxy assQuestionProxy, final VerticalPanel questionVert){		
			
			AnswerToAssQuestionRequest answerToAssQuestionRequest = requests.answerToAssQuestionRequest();
			
			questionVert.clear();
						
			/*questionVert.addStyleName("containerLoadingPopupViewStyle");
			ApplicationLoadingView loadingView = new ApplicationLoadingView();
			loadingView.setVisible(true);
			questionVert.add(loadingView);*/
			AppLoader.setNoLoader();			
			answerToAssQuestionRequest .findAnswerToAssQuestionByAssesmentQuestion(assQuestionProxy.getId()).with("answers").to(new BMEReceiver<List<AnswerToAssQuestionProxy>>() {
			    
				@Override
				public void onSuccess(List<AnswerToAssQuestionProxy> values) {
					
					Iterator<AnswerToAssQuestionProxy> iterAssQuest = values.iterator();
					
					/*questionVert.removeStyleName("containerLoadingPopupViewStyle");
					
					questionVert.clear();*/
					/**
					 * For each AssesmentQuestionProxy get answer and implement view. 
					 */									
					
					while (iterAssQuest.hasNext()){
						
						AnswerToAssQuestionProxy answerToAssQuest = iterAssQuest.next();
						
						AnswerProxy answerProxy = answerToAssQuest.getAnswers();	
											
					    //widgetDragController.makeDraggable(answerUi.asWidget(), answerUi.getLblAnswerText());
						
						fillAnswers(questionVert, answerToAssQuest, answerProxy);
					}			
				 }
		      }).fire();	
		}
	
		/** This method is used to add Question in container/Vertical Panel. 
		 * */
		protected void fillAssesmentQuestions(VerticalPanel questionsContainer, AssesmentQuestionProxy assQuestionProxy){
			
			QuestionViewImpl questionView = new QuestionViewImpl();
			
			questionView.setQuestionProxy(assQuestionProxy);
			
			questionView.init();
					    
			questionView.setDelegate(ActivityBookAssementDetails.this);
			
			questionView.setAnswerVerticalPanel(questionView.getAnswerVerticalPanel());
			
		    questionsContainer.add(questionView);
		    
		    questionsViewContainer.add(questionView);
		    			
		}
	
		/** Find Assessment Question using Assesment Id, QuestionTypes Id and QuestionEvent Id.  
		 * 
		 * 
		 * */
		protected void findAssesmentQuestionsByQuestionEventAssIdQuestType(QuestionEventProxy questionEvent, List<Long> questionTypesId, final VerticalPanel assessmentQuesContainer){			
							
			/**
			 * request all Assesmentquestions by QuestinEvent, Assesment and QuestionType
			 */			
			AssesmentQuestionRequest assesmentQuestionRequest = requests.assesmentQuestionRequest();
			
			assessmentQuesContainer.clear();

			assessmentQuesContainer.addStyleName("containerLoadingPopupViewStyle");
			ApplicationLoadingView loadingView = new ApplicationLoadingView();
			loadingView.setVisible(true);
			assessmentQuesContainer.add(loadingView);
			AppLoader.setNoLoader();
			
			assesmentQuestionRequest.findAssesmentQuestionsByQuestionEventAssIdQuestType(questionEvent.getId(),assesment.getId(),questionTypesId,true,false).with("question","question.questionType").to(new BMEReceiver <java.util.List<medizin.client.proxy.AssesmentQuestionProxy>>(){
				
				@Override
				public void onSuccess(List<AssesmentQuestionProxy> response) {
					
					if (view == null) {						
						// This activity is dead
						Log.debug("view ist null");					
						return;
					}					
					
					assessmentQuesContainer.removeStyleName("containerLoadingPopupViewStyle");
					assessmentQuesContainer.clear();
					
					Iterator<AssesmentQuestionProxy> iterAssQuestionProxy = response.iterator();
														
					while(iterAssQuestionProxy.hasNext()){						
						
						AssesmentQuestionProxy assQuestionProxy = iterAssQuestionProxy.next();
												
						fillAssesmentQuestions(assessmentQuesContainer, assQuestionProxy);					
					}
			}
			}).fire();
	}

		
		/**
		 *  Add EventView object to the eventsContainer.
		 * 
		 * @param  questionEvent   This is question event proxy.
		 * @param  eventsContainer This is container in which view will be inserted. 
		 * @author salim
		 * 
		 * */
		protected void fillQuestionEvent(QuestionEventProxy questionEvent, VerticalPanel eventsContainer, List<Long> questionTypesId){
													
			EventViewImpl eventView = new EventViewImpl();
												
			eventView.setEventProxy(questionEvent);
			
			eventView.setQuestionTypesId(questionTypesId);
			
			eventView.setDelegate(this);
						
			eventView.init();
			
			eventView.setQuestionsContainer(eventView.getQuestionsContainer());
									
			eventsContainer.add(eventView);					
														
		}

		/**
		 * Find Question Event using assessment Id and Question type Id.
		 * 
		 * @param  questionEventRequest   QuestionEvent Request object.
		 * @param  questionTypesId        List of Question Types. 
		 * @param  eventsContainer        represent a container of type VerticalPanel
		 * 
		 * @author salim 			
		 * */
		protected void findAllQuestionEventsByQuestionTypeAndAssesmentID(QuestionEventRequest questionEventRequest, final List<Long> questionTypesId, final VerticalPanel eventsContainer){
			
			eventsContainer.clear();
			eventsContainer.addStyleName("containerLoadingPopupViewStyle");
			ApplicationLoadingView loadingView = new ApplicationLoadingView();			
			loadingView.setVisible(true);					
			eventsContainer.add(loadingView);
			AppLoader.setNoLoader();
			questionEventRequest.findAllQuestionEventsByQuestionTypeAndAssesmentID(assesment.getId(),questionTypesId).to(new BMEReceiver<java.util.List<medizin.client.proxy.QuestionEventProxy>>(){

				@Override
				public void onSuccess(List<QuestionEventProxy> response) {
									
					eventsContainer.removeStyleName("containerLoadingPopupViewStyle");
					eventsContainer.clear();
					Iterator<QuestionEventProxy> iterQuestionEventProxy = response.iterator();
															
					while(iterQuestionEventProxy.hasNext()){
					
						QuestionEventProxy questionEventProxy = iterQuestionEventProxy.next();
														
						fillQuestionEvent(questionEventProxy, eventsContainer, questionTypesId);
					}
				}
			}).fire();
		}
		
		protected void initAssesmentQuestion(QuestionEventProxy questionEvent, EventViewImpl eventView){
								
			findAssesmentQuestionsByQuestionEventAssIdQuestType(questionEvent, eventView.getQuestionTypesId(), eventView.getQuestionsContainer());
		}
		
		
		protected void initQuestionEvent(QuestionTypeCountPerExamProxy proxy, QuestionTypeDNDViewImpl questionTypeView){
						
			VerticalPanel eventsContainer = questionTypeView.getEventsContainer();
											
			Set <QuestionTypeProxy> questionTypeProxySet = proxy.getQuestionTypesAssigned();
			
			Iterator<QuestionTypeProxy> itr = questionTypeProxySet.iterator();
			
			List<Long> questionTypesId = new ArrayList<Long>() ;
			
			while(itr.hasNext()){
				
				questionTypesId.add(itr.next().getId());
				
			}
			
			QuestionEventRequest questionEventRequest = requests.questionEventRequest();
									
			findAllQuestionEventsByQuestionTypeAndAssesmentID(questionEventRequest, questionTypesId, eventsContainer);				
	}
	
	/**
	 * Add QuestionEvent to the eventsContainer of Questiontype.
	 * @param assesmentQuestionRequest 
	 */
	protected void insertQuestionEvents(QuestionEventProxy questionEvent, VerticalPanel eventsContainer, /*PickupDragController eventDragController,*/ List<Long> questionTypesId, AssesmentQuestionRequest assesmentQuestionRequest){
		//final QuestionTypeProxy questionTypeProxy= questionTypesId;
		/**
		 * New DropController that allows dropping eventsContainer inside eventsContainer.
		 */
		//VerticalPanelDropController eventsDropController = new VerticalPanelDropController(eventsContainer);
		
		/*eventDragController.registerDropController(eventsDropController);
		eventDragController.addDragHandler(this);*/
		/**
		 * New EventViewImpl for QuestionEvent-object.
		 */
		final EventViewImpl eventVertical = new EventViewImpl();
		//final AbsolutePanel questionEventContent = eventVertical.getQuestionEventContent();
		final VerticalPanel questionsContainer = eventVertical.getQuestionsContainer();
		eventVertical.setEventProxy(questionEvent);
		//eventDragController.makeDraggable(eventVertical, eventVertical.getHeaderNamelbl());
		eventsContainer.add(eventVertical);
		Log.debug("eventsContain.add(eventVertical) ausgef�llt");
		
		//final PickupDragController questionDragController = new PickupDragController(eventVertical.getQuestionEventContent(),false);
		//VerticalPanelDropController questionsDropController = new VerticalPanelDropController(questionsContainer);
		//questionDragController.registerDropController(questionsDropController);
		//questionDragController.addDragHandler(this);
		/**
		 * request all Assesmentquestions by QuestinEvent, Assesment and QuestionType
		 */
		
		assesmentQuestionRequest.findAssesmentQuestionsByQuestionEventAssIdQuestType(questionEvent.getId(), assesment.getId(), questionTypesId,true,false).with("question","question.questionType").to(new BMEReceiver <java.util.List<medizin.client.proxy.AssesmentQuestionProxy>>(){
			
			@Override
			public void onSuccess(List<AssesmentQuestionProxy> response) {
				
				if (view == null) {
					// This activity is dead
					Log.debug("view ist null");
					return;
				}
				Iterator<AssesmentQuestionProxy> iterAssQuestionProxy = response.iterator();
				
				AnswerToAssQuestionRequest answerToAssQuestionRequest = null; 
				
				/**
				 * For each assessment-question create new QuestionViewImpl.
				 */
				while(iterAssQuestionProxy.hasNext()){
					
					AssesmentQuestionProxy assQuestionProxy = iterAssQuestionProxy.next();
					
					final QuestionViewImpl questionVert = new QuestionViewImpl();
					questionVert.setQuestionProxy(assQuestionProxy);
				    //questionDragController.makeDraggable(questionVert, questionVert.getQuestionTextLbl());
				    questionsContainer.add(questionVert);
				    questionsViewContainer.add(questionVert);
				    

		

				    // initialize our widget drag controller
				    /*final PickupDragController widgetDragController = new PickupDragController(questionEventContent, false);
				    widgetDragController.setBehaviorMultipleSelection(false);
				    widgetDragController.addDragHandler(ActivityBookAssementDetails.this);
				      VerticalPanelDropController widgetDropController = new VerticalPanelDropController(
				    		  questionVert);
				      widgetDragController.registerDropController(widgetDropController);
*/    
				    if(answerToAssQuestionRequest != null) {
				    	  answerToAssQuestionRequest = answerToAssQuestionRequest.append(requests.answerToAssQuestionRequest());
				      }else {
				    	  answerToAssQuestionRequest = requests.answerToAssQuestionRequest();
				      }
				      
				      answerToAssQuestionRequest.findAnswerToAssQuestionByAssesmentQuestion(assQuestionProxy.getId()).with("answers").to(new BMEReceiver<List<AnswerToAssQuestionProxy>>() {
				    
						@Override
						public void onSuccess(List<AnswerToAssQuestionProxy> values) {
							Iterator<AnswerToAssQuestionProxy> iterAssQuest = values.iterator();
							/**
							 * For each AssesmentQuestionProxy get answer and implement view. 
							 */
							while (iterAssQuest.hasNext()){
								
								AnswerToAssQuestionProxy answerToAssQuest = iterAssQuest.next();
								AnswerProxy answerProxy = answerToAssQuest.getAnswers();	
								AnswerView answerUi = new AnswerViewImpl();
								answerUi.setProxy(answerProxy);
								answerUi.setAnswerToAssQueston(answerToAssQuest);
							    answerUi.setDelegate(ActivityBookAssementDetails.this);						 
							    questionVert.add(answerUi.asWidget());
							    //widgetDragController.makeDraggable(answerUi.asWidget(), answerUi.getLblAnswerText());
							}
							/**
							 * Hide Answers at first.
							 */
							Integer widgetCount = questionVert.getWidgetCount();
							for (int i = 1; i < widgetCount; i++){
							questionVert.getWidget(i).setVisible(false);
							}
							//loadingPopup.hide();
						}
				      });

				    
				      //fireGetAnswerToAssQuest(callbackanswerToAssQuest, assQuestionProxy);
				      
				}//End Iteration of Ass Question Proxys
				
				if(answerToAssQuestionRequest != null) {
					answerToAssQuestionRequest.fire();
				}
					
				
		        if (widget != null) {
		          widget.setWidget(view.asWidget());
				}
				
				
			}
			
		});
		
	}//End InsertQuestionEvents
	
	
	

	//Part of Request for answerToAssQuestions	
	/*private void fireGetAnswerToAssQuest( final BMEReceiver<List<AnswerToAssQuestionProxy>> callbackanswerToAssQuest, AssesmentQuestionProxy assesmentQuestionproxy) {
		createfireGetAnswerToAssQuest(assesmentQuestionproxy).fire(callbackanswerToAssQuest);
	}
	
	
	protected Request<java.util.List<medizin.client.proxy.AnswerToAssQuestionProxy>> createfireGetAnswerToAssQuest(AssesmentQuestionProxy assesmentQuestionproxy) {
        return requests.answerToAssQuestionRequest().findAnswerToAssQuestionByAssesmentQuestion(assesmentQuestionproxy.getId()).with("answers");
    }*/
	//End Request for answerToAssQuestions
	

		


	public void answerDropped(EntityProxyId<?> answerId) {
		requests.find(answerId).fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				if(response instanceof AnswerProxy){
					AnswerProxy answer = (AnswerProxy) response;
					Log.info("Answer text: "+answer.getAnswerText());

				
				}

				
			}
		    });
		
	}	
	
	/**
	 * Hash Maps are used to store objects in sorted order.
	 */
	  private Map<EntityProxyId<?>, Integer> proxyMap = new HashMap<EntityProxyId<?>, Integer>();
	  
	  private Map<EntityProxyId<?>, Integer> questionProxyMap = new HashMap<EntityProxyId<?>, Integer>();
	  
	  private Map<EntityProxyId<?>, Integer> eventProxyMap = new HashMap<EntityProxyId<?>, Integer>();

	  
	  //private AnswerToAssQuestionProxy answerToAssQuest;



	  /**
	   * Log the drag end event and loop through all children of drop target(in this case alway a vertical panel) and store them in a hash map. The objects are dropped in a vertical panel.
	   * After the object is dropped, the vertical panel (e.g. eventsContainer)contains the new sort order. every child object of the vertical panel is saved to a hash map in the new sort order.
	   * 
	   *
	   * @param event the event to log
	   */
	  @Override
	  public void onDragEnd(DragEndEvent event) {
	    Log.debug("onDragEnd: " + event);
	    
	    Log.info(event.getSource().getClass().toString());
	    if (event.getSource() instanceof AnswerViewImpl){
	    	AnswerViewImpl answerView = (AnswerViewImpl) event.getSource();
	    	Log.info("Hier: " +answerView.getAnswer().stableId().toString());
	    	answerView.answerDropped(answerView.getAnswer().stableId());
	    }
	    
	    if(event.getSource() instanceof QuestionViewImpl){
	    	QuestionViewImpl questionView = (QuestionViewImpl)event.getSource();
	    	Log.info("ProxyID der gedraggten Frage: "+questionView.getQuestionProxy().getId());
	    }
	    if(event.getSource() instanceof EventViewImpl){
	    	EventViewImpl eventView =(EventViewImpl)event.getSource();
	    	Log.info("ProxyID des gedraggten Events: "+eventView.getEventProxy().getId());
	    }
	    if (event.getContext().finalDropController != null) {
	    	Log.info(event.getContext().finalDropController.getDropTarget().getClass().getName());
	    	VerticalPanel myPanel = (VerticalPanel) event.getContext().finalDropController.getDropTarget();
	    	Iterator<Widget> iter = myPanel.iterator();
	    	int i=0;
	    	int j=0;
	    	int k=0;
	    	while(iter.hasNext()) {
	    		Widget myWidget = iter.next();

	    		if (myWidget instanceof AnswerView){
	    			AnswerViewImpl answView = (AnswerViewImpl) myWidget;
	    			Log.warn(answView.getAnswer().stableId().toString());

	    			proxyMap.put(answView.getAnswerToAssQueston().stableId(), i);
	    			Log.debug("Proxy in proxyMap f�r AnswersView abgelegt: "+i);
	    			i++;
	    			
	    		}
	    		if (myWidget instanceof QuestionViewImpl){
	    			QuestionViewImpl questView = (QuestionViewImpl) myWidget;
	    			questionProxyMap.put(questView.getQuestionProxy().stableId(), k);
	    			Log.debug("Proxy in questionProxyMap abgelegt: "+k);
	    			k++;
	    			
	    			
	    		}
	    		if (myWidget instanceof EventViewImpl){
	    			EventViewImpl eventView =(EventViewImpl) myWidget;
	    			eventProxyMap.put(eventView.getEventProxy().stableId(), j);
	    			Log.debug("Proxy in eventProxyMap abgelegt:"+j);
	    			j++;
	    		}
	    		
	    	}
	    	processMap();
	    	processQuestionsMap();
	    	processEventMap();
	    }


	  }


	  
	  /**
	   * Process HashMap with answers. For each answer in HashMap Request answerProxy, set new sort order and persist object to database.
	   */
	  private void processMap() {
		 Set<Entry<EntityProxyId<?>, Integer>> proxyIds = proxyMap.entrySet();
		 Iterator<Entry<EntityProxyId<?>, Integer>> iter = proxyIds.iterator();
		 while(iter.hasNext()){
			 Entry<EntityProxyId<?>, Integer> entry = iter.next();
			 Log.info("inside process Map iterator!");
			 Log.info("entry.getKey().toString(): "+entry.getKey().toString());
			 Log.info(entry.getValue().toString());
				requests.find(entry.getKey()).fire(new BMEReceiver<Object>() {

					@Override
					public void onSuccess(Object response) {
						if(response instanceof  AnswerToAssQuestionProxy ){
							final int k =proxyMap.get((( AnswerToAssQuestionProxy ) response).stableId());
							AnswerToAssQuestionProxy  answerToAssQuest = (( AnswerToAssQuestionProxy ) response);

							Log.info("answerToAssQuestionRequest wird gleich ausgef�hrt");

							requests.answerToAssQuestionRequest().findAnswerToAssQuestion(answerToAssQuest.getId()).fire(new BMEReceiver<Object>(){

								@Override
								public void onSuccess(Object response) {
						

									AnswerToAssQuestionProxy answerToAssQuest = (AnswerToAssQuestionProxy)response;
									Log.debug("Call successfull! "+answerToAssQuest.getId() );
									Log.debug("old sort order: "+answerToAssQuest.getSortOrder());
									
									AnswerToAssQuestionRequest request = requests.answerToAssQuestionRequest();
									
									
									answerToAssQuest = request.edit(answerToAssQuest);
									answerToAssQuest.setSortOrder(k);
			
									
									request.persist().using(answerToAssQuest).fire(new BMEReceiver<Void>() {
								
										public void onSuccess(Void ignore) {
											Log.debug("SortOrder Sucessfull saved");
										}

										/*@Override
										public void onFailure(ServerFailure error) {
											Log.warn(McAppConstant.ERROR_WHILE_CREATE + " in Questiontype -"
													+ error.getMessage());
											if (error.getMessage().contains("ConstraintViolationException")) {
												Log.debug("Fehler ConstraintViolationException");
											}
										}

										@Override
										public void onViolation(Set<Violation> errors) {
											Log.debug("Fehlen beim erstellen, volation: "
													+ errors.toString());
											Iterator<Violation> iter = errors.iterator();
											String message = "";
											while (iter.hasNext()) {
												message += iter.next().getMessage() + "<br>";
											}
											Log.warn(McAppConstant.ERROR_WHILE_CREATE_VIOLATION
													+ " in Institution -" + message);

										}*/

									});
								
								}

						
						});

						
					}
					}			
					});
				
				
		 }//End Iterator proxyMap
		 
		 
	}//End of processMap()
	  
	  /**
	   * Process HashMap with questions. For each question in HashMap Request answerProxy, set new sort order and persist object to database.
	   */
	 private void processQuestionsMap(){
		 Set<Entry<EntityProxyId<?>, Integer>> questionProxyIds = questionProxyMap.entrySet();
		 Iterator<Entry<EntityProxyId<?>, Integer>> iterQ = questionProxyIds.iterator();
		 while(iterQ.hasNext()){
			 Entry<EntityProxyId<?>, Integer> questionEntry = iterQ.next();
			 Log.debug("inside process questionsMap iterator!");
			 Log.debug("questionEntry().toString(): "+questionEntry.getKey().toString());
			 Log.debug(questionEntry.getValue().toString());
			 

			 
			 requests.find(questionEntry.getKey()).fire(new BMEReceiver<Object>() {

					@Override
					public void onSuccess(Object response) {

						final int l = questionProxyMap.get(((AssesmentQuestionProxy) response).stableId());
						AssesmentQuestionProxy assQuestion = ((AssesmentQuestionProxy) response);
						AssesmentQuestionRequest assQuestionRequest = requests.assesmentQuestionRequest();
						assQuestion = assQuestionRequest.edit(assQuestion);
						assQuestion.setOrderAversion(l);
						assQuestionRequest.persist().using(assQuestion).fire(new BMEReceiver<Void>() {
							
							public void onSuccess(Void ignore) {
								Log.debug("SortOrder of Questions sucessfully saved");
							}

							/*@Override
							public void onFailure(ServerFailure error) {
								Log.warn(McAppConstant.ERROR_WHILE_CREATE + " in Questiontype -"
										+ error.getMessage());
								if (error.getMessage().contains("ConstraintViolationException")) {
									Log.debug("Fehler ConstraintViolationException");
								}
							}

							@Override
							public void onViolation(Set<Violation> errors) {
								Log.debug("Fehlen beim erstellen, volation: "
										+ errors.toString());
								Iterator<Violation> iter = errors.iterator();
								String message = "";
								while (iter.hasNext()) {
									message += iter.next().getMessage() + "<br>";
								}
								Log.warn(McAppConstant.ERROR_WHILE_CREATE_VIOLATION
										+ " in  -" + message);

							}*/

						});
						
					}
			    });
			 
		 }//End Iteration of questionProxyIds
		  
	  } //End processQuestionMap()
	 
	  /**
	   * Process HashMap with question events(Themengruppen). For each question event in HashMap Request questionEventProxy, set new sort order and persist object to database.
	   */
	 private void processEventMap(){
		 Set<Entry<EntityProxyId<?>, Integer>> eventProxyIds = eventProxyMap.entrySet();
		 Iterator<Entry<EntityProxyId<?>, Integer>> iterE = eventProxyIds.iterator();
		 while(iterE.hasNext()){
			 Entry<EntityProxyId<?>, Integer> eventEntry = iterE.next();
			 Log.info("inside process eventProxyIds iterator!");
			 Log.info("eventEntry().toString(): "+eventEntry.getKey().toString());
			 Log.info(eventEntry.getValue().toString());
			 requests.find(eventEntry.getKey()).fire(new BMEReceiver<Object>() {
			
					@Override
					public void onSuccess(Object response) {
						
						final int m = eventProxyMap.get(((QuestionEventProxy) response).stableId());
						QuestionEventProxy questionEvent = ((QuestionEventProxy) response);
						


						requests.questionSumPerPersonRequest().findQuestionSumPerPersonByEventNonRoo(questionEvent.getId()).fire(new BMEReceiver<Object>(){

							/*public void onFailure(ServerFailure error){
								Log.error("questionSumPerPersonRequest()"+error.getMessage());
							}*/
							
							@Override
							public void onSuccess(Object response) {

								QuestionSumPerPersonProxy questionSumProxy = ((QuestionSumPerPersonProxy)response);
								Log.debug("QuestionSumPerPerson ausgelesen: "+questionSumProxy.stableId());
								QuestionSumPerPersonRequest questionSumRequest = requests.questionSumPerPersonRequest();
								questionSumProxy = questionSumRequest.edit(questionSumProxy);
								questionSumProxy.setSort_order(m);
								questionSumRequest.persist().using(questionSumProxy).fire(new BMEReceiver<Void>() {
									/*public void onFailure(ServerFailure error){
										Log.error("persist Sortorder QuestionSumProxy"+error.getMessage());
									}*/
									
									
									@Override
									public void onSuccess(Void response) {
										Log.debug("SortOrder of Event sucessfully saved");
										
									}
									
								});
							}
							
						});
						

						
					}
			    });
			 
		 }//End Iteration of questionProxyIds
		  
	  } //End processQuestionMap()
	  
	/**
	   * Log the drag start event.
	   * 
	   * @param event the event to log
	   */
	 @Override
	  public void onDragStart(DragStartEvent event) {
		  Log.debug("onDragStart: " + event);
	  }

	  /**
	   * Log the preview drag end event.
	   * 
	   * @param event the event to log
	   * @throws VetoDragException exception which may be thrown by any drag handler
	   */
	  @Override
	  public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		  Log.debug("<br>onPreviewDragEnd: " + event);
	  }

	  /**
	   * Log the preview drag start event.
	   * 
	   * @param event the event to log
	   * @throws VetoDragException exception which may be thrown by any drag handler
	   */
	  @Override
	  public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
	   
	    Log.debug("onPreviewDragStart: " + event);
	  }

	  
	@Override
	public void createAssementBook(boolean createAVesion) {
		AssesmenBookDialogbox assementDialog=new AssesmenBookDialogboxImpl();
		final VerticalPanel panel = assementDialog.getExportPanel();
		Integer questionNumber = 1;
		
		SimplePanel panelToAdd = new SimplePanel();
		panelToAdd.setTitle("hallo");
		panel.add(panelToAdd);
		Iterator<Widget> questionTypeIter = bookAssesmentViewDetails.getWorkingArea().iterator();
		
		while (questionTypeIter.hasNext()) {
			Widget widget = (Widget) questionTypeIter.next();
			if (widget instanceof QuestionTypeDNDViewImpl) {
				QuestionTypeDNDViewImpl questionTypeContainer = (QuestionTypeDNDViewImpl) widget;

				QuestionTypeCountPerExamProxy questionType = questionTypeContainer.getProxy();
				Log.debug(questionType.getQuestionTypesAssigned().toString());
						
				Iterator<Widget> iterEvents = questionTypeContainer.getEventsContainer().iterator();
				while (iterEvents.hasNext()) {
					Widget widget2 = (Widget) iterEvents.next();
					if(widget2 instanceof EventViewImpl){
						EventViewImpl eventview = (EventViewImpl) widget2;
						Log.debug(eventview.getEventProxy().getEventName());
						
						Iterator<Widget> iterQuestion = eventview.getQuestionsContainer().iterator();
						while (iterQuestion.hasNext()) {
							Widget widget3 = (Widget) iterQuestion.next();
							if(widget3 instanceof QuestionViewImpl){
								QuestionViewImpl questionView = (QuestionViewImpl)widget3;
								Log.debug(questionView.getQuestionProxy().getQuestion().getQuestionText());
								panel.add(new HTML(questionNumber + ". " +questionView.getQuestionProxy().getQuestion().getQuestionText()));
								questionNumber ++;
								Integer answerCount = 1;
								String htmlString = "<ol  style=\"list-style-type:upper-alpha;\">";
								Iterator<Widget> iterAnswer = questionView.iterator();
								while (iterAnswer.hasNext()) {
									Widget widget4 = (Widget) iterAnswer.next();
									Log.info(widget4.getClass().getName());
									if(widget4 instanceof AnswerViewImpl){
										
										//if(answerCount==1) htmlString+="";
										AnswerViewImpl answer = (AnswerViewImpl) widget4;
										Log.debug(answer.getAnswer().getAnswerText());
										htmlString += "<li>"+answer.getAnswer().getAnswerText()+"</li>";
									}
								}
								if(answerCount==1) htmlString+="</ol>";
								panel.add(new HTML(htmlString));
								
								
								
							}
							
						}
						
						
					}
					
				}
			}
			
		}
		//		requests.assesmentQuestionRequest().findAssementQuestionForAssementBook(assesment.getId()).fire(new Receiver<List<AssesmentQuestionProxy>>() {
//			
//	          @Override
//	          public void onSuccess(List<AssesmentQuestionProxy> response) {
//	        	  Log.info("create Assesment");
//	       
//	          }
//	          
//	          public void onFailure(ServerFailure error){
//	        	  ErrorPanel erorPanel = new ErrorPanel();
//	        	  erorPanel.setErrorMessage(error.getMessage());
//					Log.error(error.getMessage());
//				}
//	          @Override
//				public void onViolation(Set<Violation> errors) {
//					Iterator<Violation> iter = errors.iterator();
//					String message = "";
//					while(iter.hasNext()){
//						message += iter.next().getMessage() + "<br>";
//					}
//					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antworten auflisten -" + message);
//					
//		        	  ErrorPanel erorPanel = new ErrorPanel();
//		        	  erorPanel.setErrorMessage(message);
//
//				}
//		})
;
		
	}
	  
	  /**
	   * Moves a QuestionTypeCountPerExamProxy <b>up</b> one place in sort order.
	   * @param proxy
	   */
		@Override
		public void onUpInOrderClicked(QuestionTypeCountPerExamProxy proxy) {
			QuestionTypeCountPerExamProxy questionTypeCount = (QuestionTypeCountPerExamProxy)proxy;
			moveQuestionTypeCountPerExamRequestUp(questionTypeCount);
			view.reload(assesment);
			Log.debug("upInOrderClicked in Activity");
		} 
		  /**
		   * Moves a QuestionTypeCountPerExamProxy <b>down</b> one place in sort order.
		   * @param proxy
		   */
		
	@Override
	public void downInOrderClicked(QuestionTypeCountPerExamProxy proxy) {
		QuestionTypeCountPerExamProxy questionTypeCount = (QuestionTypeCountPerExamProxy)proxy;
		Log.debug("moveQuestionTypeCountPerExamRequestDown aus Activity proxy: "+questionTypeCount.stableId());
		moveQuestionTypeCountPerExamRequestDown(questionTypeCount);
		view.reload(assesment);
	}

	@Override
	public void placeChanged(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getAssignemtId() {
		return assesment.getId();
	}

	@Override
	public void shuffleAssementQuestionsAnswers(Boolean disallowSorting) {
		
		if(assesment == null) {
			Log.error("Assesment is null");
			return;
		}
		AppLoader.setNoLoader();
		requests.assesmentQuestionRequest().shuffleQuestionsAnswers(assesment.getId(),disallowSorting).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				init();
				
			}
		});
	}

	@Override
	public void saveAllAssesmentQuestionChanges() {
		AssesmentRequest assesmentRequest = requests.assesmentRequest();
		if(assesment.getDisallowSorting() == null || assesment.getDisallowSorting() == false) {
			Boolean disallowSorting = view.getDisallowSorting(assesment.getDisallowSorting());
			AssesmentProxy edit = assesmentRequest.edit(assesment);
			edit.setDisallowSorting(disallowSorting);
			AppLoader.setNoLoader();
			assesmentRequest.persist().using(assesment).to(new BMEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					
				}
			});
		}
		
		AssesmentQuestionRequest assesmentQuestionRequest = assesmentRequest.append(requests.assesmentQuestionRequest());
		for (final QuestionViewImpl questionViewImpl : questionsViewContainer) {
			AssesmentQuestionProxy assesmentQuestionProxy = questionViewImpl.getQuestionProxy();
			if(assesmentQuestionProxy  != null) {
				assesmentQuestionProxy = assesmentQuestionRequest.edit(assesmentQuestionProxy );
				assesmentQuestionProxy.setPercent(questionViewImpl.getPercentValue());
				assesmentQuestionProxy.setPoints(questionViewImpl.getPointsValue());
				assesmentQuestionProxy.setEliminateQuestion(questionViewImpl.getEleminateQuestionValue());
				assesmentQuestionRequest = assesmentQuestionRequest.append(requests.assesmentQuestionRequest());
				assesmentQuestionRequest.persist().using(assesmentQuestionProxy).to(new BMEReceiver<Void>() {

					@Override
					public void onSuccess(Void response) {
					}
				});
			}else {
				Log.error("Proxy is null");
			}
		}
		assesmentQuestionRequest.fire();
	}

	@Override
	public void openQuestionTypeContainer(QuestionTypeCountPerExamProxy proxy,QuestionTypeDNDViewImpl questionTypeView) {
		
			initQuestionEvent(proxy, questionTypeView);
	}

	@Override
	public void openEventContainer(QuestionEventProxy questionEvent, EventViewImpl eventView){
			
			initAssesmentQuestion(questionEvent, eventView);					
	}
	
	@Override
	public void openAssesmentQuestionContainer(AssesmentQuestionProxy assQuestionProxy, QuestionViewImpl questionView){
			
			initAnswer(assQuestionProxy, questionView);
	}

}

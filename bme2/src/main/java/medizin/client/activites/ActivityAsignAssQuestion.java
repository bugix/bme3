package medizin.client.activites;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAsignAssQuestion;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AnswerToAssQuestionProxy;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.request.AssesmentQuestionRequest;
import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.assignquestion.AddQuestionsTabPanel;
import medizin.client.ui.view.assignquestion.AddQuestionsTabPanelImpl;
import medizin.client.ui.view.assignquestion.AnswerView;
import medizin.client.ui.view.assignquestion.AnswerViewImpl;
import medizin.client.ui.view.assignquestion.AsignAssQuestionView;
import medizin.client.ui.view.assignquestion.AsignAssQuestionViewImpl;
import medizin.client.ui.view.assignquestion.AssesmentQuestionPanel;
import medizin.client.ui.view.assignquestion.AssesmentQuestionView;
import medizin.client.ui.view.assignquestion.AssesmentQuestionViewImpl;
import medizin.client.ui.view.assignquestion.AssesmentTabPanel;
import medizin.client.ui.view.assignquestion.AssesmentTabPanelImpl;
import medizin.client.ui.view.assignquestion.QuestionPanel;
import medizin.client.ui.view.assignquestion.QuestionTypeCountViewImpl;
import medizin.client.ui.view.assignquestion.QuestionView;
import medizin.client.ui.view.assignquestion.QuestionViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;

public class ActivityAsignAssQuestion extends AbstractActivityWrapper implements DragHandler, AsignAssQuestionView.Presenter,
AddQuestionsTabPanel.Delegate, QuestionPanel.Delegate, QuestionView.Delegate, AssesmentQuestionView.Delegate, AssesmentTabPanel.Delegate,AssesmentQuestionPanel.Delegate {

	private PlaceAsignAssQuestion asignAssQuestionPlace;
	private AcceptsOneWidget widget;
	private AsignAssQuestionView view;


	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AddQuestionsTabPanel addQuestionsTabPanel;
	private AssesmentQuestionPanel assementQuestionPanel;
	private AssesmentTabPanel assesmentTabPanel;
	private QuestionPanel questionPanel;
	final private HashMap<AssesmentProxy, List<PersonProxy>> examAutorListMap=new HashMap<AssesmentProxy, List<PersonProxy>>();


	@Inject
	public ActivityAsignAssQuestion(PlaceAsignAssQuestion place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.asignAssQuestionPlace = place;
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
//		((SlidingPanel)widget).remove(view.asWidget());


	}
	
	PickupDragController dragController;
	private VerticalPanelDropController assesmentQuestionPanelDropController;
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		AsignAssQuestionView asignAssQuestionView = new AsignAssQuestionViewImpl();
		asignAssQuestionView.setPresenter(this);
		this.widget = widget;
		this.view = asignAssQuestionView;
        widget.setWidget(asignAssQuestionView.asWidget());
        addQuestionsTabPanel = view.getAddQuestionsTabPanel();
        
        
        
        
        questionPanel = view.getQuestionPanel();
        questionPanel.setDelegate(this);
        assementQuestionPanel = view.getAssesmentQuestionPanel();
        assementQuestionPanel.setDelegate(this);
        assesmentTabPanel = view.getAssesmentTabPanel();
        assesmentTabPanel.setDelegate(this);
        
        //initialize the dragcontroler on the widget
        dragController = new PickupDragController(RootPanel.get(),false);
        dragController.addDragHandler(this);
        
		//questionPanelDropController = new VerticalPanelDropController(questionPanel.getQuestionDisplayPanel());
		//dragController.registerDropController(questionPanelDropController);
		assesmentQuestionPanelDropController = new VerticalPanelDropController(assementQuestionPanel.getAssesmentQuestionDisplayPanel());
		
		dragController.registerDropController(assesmentQuestionPanelDropController);
		
		initAddQuestionsTabPanel();
		
        initAssementTabPanel();
       
       
        
        //if admin than disable proposed tab (first tab)
        if(personRightProxy.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())
        {
	        ((AddQuestionsTabPanelImpl)addQuestionsTabPanel).removeTab(0);
	        ((AddQuestionsTabPanelImpl)addQuestionsTabPanel).selectTab(0,false);
	        
	        
        }
        

	}
	
	private void initAddQuestionsTabPanel() {
		
		addQuestionsTabPanel.setDelegate(this);
		
		
	}
	
	private void initAssementTabPanel() {
		requests.assesmentRequest().findAssesmentsOpenBetween().with("mc","questionTypeCountPerExams","questionTypeCountPerExams.questionTypesAssigned").fire(new Receiver<List<AssesmentProxy>>(){

			@Override
			public void onSuccess(List<AssesmentProxy> response) {
				Log.debug("Geholte Assements: " + response.size());
				Iterator<AssesmentProxy> iter = response.iterator();
				while(iter.hasNext()){
					
					AssesmentProxy assesment=iter.next();
					assesmentTabPanel.addAssementTab(assesment);
					
					
				}
				if(((AssesmentTabPanelImpl)assesmentTabPanel).getTabCount() > 0)
				((AssesmentTabPanelImpl)assesmentTabPanel).setSelectedTab(0);
				
				//assesmentTabPanel.setSelectedTab(0);
		        //initAssementQuestionPanel();
		       
				//show author drop down
				/*if(personRightProxy.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())
				{
					populateAuthorListBox(assementQuestionPanel.getAuthorListBox(), assesment);
					
				}
				else
				{
					assementQuestionPanel.getAuthorListBox().removeFromParent();
				}*/
				
				
				
			}
			
		});
		
	}
	private VerticalPanelDropController questionPanelDropController;
	
	
	/**
	 * 
	 * Init Rigt side view
	 * 
	 * */
	private void initQuestionPanel(int action, AssesmentProxy assesment,String questionName,String questionId,String questionType) {
		
		//admin / institutional admin
		if(personRightProxy.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())
		{
			action++;
			
			//populate author list box
			
			if(examAutorListMap.get(assesment) ==null)				
				populateAuthorListBox(questionPanel.getAuthorListBox(),assesment);
			else
			{
				if(examAutorListMap.get(assesment).size()>0)
				questionPanel.getAuthorListBox().setValue(examAutorListMap.get(assesment).get(0));
				else
					questionPanel.getAuthorListBox().setValue(null);
				
				questionPanel.getAuthorListBox().setAcceptableValues(examAutorListMap.get(assesment));
			}
		}
		else
		{
			questionPanel.getAuthorListBox().removeFromParent();
		}
		
		if(assesment==null)
		{
			ConfirmationDialogBox.showOkDialogBox(constants.noTestsAssigned(), constants.noTestsAssigned());
			return;
		}
		
		Log.debug("Assesmet selected " + assesment.getName());
		
		/*if(personRightProxy.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())
		{
			if(action==0)
				action=1;
			
			if(action==1)
				action=2;
		}*/
		
		//proposed Question
		if (action==0){
			requests.assesmentQuestionRequest().findAssesmentQuestionsByMcProposal(assesment.getId(),questionId,questionType,questionName).with("question.rewiewer","question.autor","question.keywords","question.questEvent","question.comment","question.questionType").fire(new Receiver<List<AssesmentQuestionProxy>>() {

				@Override
				public void onSuccess(List<AssesmentQuestionProxy> response) {
					questionPanel.removeAll();
					Iterator<AssesmentQuestionProxy> iter = response.iterator();
					while(iter.hasNext()){
						AssesmentQuestionView assesmentQuestion = new AssesmentQuestionViewImpl();
						assesmentQuestion.setProxy(iter.next(), true);
						assesmentQuestion.setDelegate(ActivityAsignAssQuestion.this);
						
						dragController.makeDraggable(assesmentQuestion.asWidget(), assesmentQuestion.getDragControler());
						questionPanel.addAssesmentQuestion(assesmentQuestion);
					}
					
				}
				
		           @Override
					public void onFailure(ServerFailure error) {
							Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
							if(error.getMessage().contains("ConstraintViolationException")){
								Log.debug("Fehlen beim erstellen: Doppelter name");
								// TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
							}
							
						
					}
					@Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
						
						ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setWarnMessage(message);

						
					}
			});
		}//past Question
		else if (action == 1){
				findPastAssesmentQuestion(assesment,questionName,questionId,questionType);
		}//new Question
		else if (action == 2){
			requests.questionRequest().findQuestionsByMc(assesment.getMc().getId(),questionId,questionType,questionName).with("rewiewer", "questEvent", "autor", "questionType", "keywords").fire(new Receiver<List<QuestionProxy>>() {

				@Override
				public void onSuccess(List<QuestionProxy> response) {
					//questionPanel.getAuthorListBox().setVisible(false);
					questionPanel.removeAll();
					
					Iterator<QuestionProxy> iter = response.iterator();
					while(iter.hasNext()){
						QuestionView question = new QuestionViewImpl();
						question.setProxy(iter.next());
						question.setDelegate(ActivityAsignAssQuestion.this);
						dragController.makeDraggable(question.asWidget(), question.getDragControler());
						questionPanel.addQuestion(question);
					}
					
				}
				
		           @Override
					public void onFailure(ServerFailure error) {
							Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
							if(error.getMessage().contains("ConstraintViolationException")){
								Log.debug("Fehlen beim erstellen: Doppelter name");
								// TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
							}
							
						
					}
					@Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
						
						ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setWarnMessage(message);

						
					}
			});
		}
		
	}

	/*finds past assesment question according to selected author*/
	private void findPastAssesmentQuestion(AssesmentProxy assesment,String questionName,String questionId,String questionType) {
		
		
		
		requests.assesmentQuestionRequest().findAssesmentQuestionsByMc(assesment.getId(),assesment.getMc().getId(),questionId,questionType,questionName).with("question.rewiewer","question.autor","question.keywords","question.questEvent","question.comment","question.questionType").fire(new Receiver<List<AssesmentQuestionProxy>>() {

			@Override
			public void onSuccess(List<AssesmentQuestionProxy> response) {
				questionPanel.removeAll();
				Iterator<AssesmentQuestionProxy> iter = response.iterator();
				while(iter.hasNext()){
					AssesmentQuestionView assesmentQuestion = new AssesmentQuestionViewImpl();
					assesmentQuestion.setProxy(iter.next(), true);
					assesmentQuestion.setDelegate(ActivityAsignAssQuestion.this);
					dragController.makeDraggable(assesmentQuestion.asWidget(), assesmentQuestion.getDragControler());
					questionPanel.addAssesmentQuestion(assesmentQuestion);
				}
				
			}
			
	           @Override
				public void onFailure(ServerFailure error) {
						Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
						if(error.getMessage().contains("ConstraintViolationException")){
							Log.debug("Fehlen beim erstellen: Doppelter name");
							// TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
						}
						
					
				}
				@Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
					
					ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setWarnMessage(message);

					
				}
		});
		
	}

	/*
	 * 
	 * Common to Past Tab and Assesment Tab (left side)
	 * 
	 * Author pull down for admin / institutional Admin
	 * 
	 * Show Author / examiner assigned in particular assessment
	 * */
	private void populateAuthorListBox(final ValueListBox<PersonProxy> authorListBox,final AssesmentProxy assesment) {
		
		requests.assesmentQuestionRequest().findAuthorListByAssesment(assesment).fire(new Receiver<List<PersonProxy>>() {

			@Override
			public void onSuccess( List<PersonProxy> response) {
				
				
				//authorListBox.setValue(null);
				//authorListBox.setAcceptableValues(new ArrayList<PersonProxy>());
				
				
				//if(response.size() >0)
			//	{
				if(response.size() >0)
				{
					authorListBox.setValue(response.get(0));
					initAssementQuestionPanel(response.get(0));
				}
				else
				{
					authorListBox.setValue(null);
					initAssementQuestionPanel(null);
				}
				
					authorListBox.setAcceptableValues(response);
					//authorListBox.setValue(response.get(0),true);
					authorListBox.setVisible(true);
				//}
				
				
				examAutorListMap.put(assesment, response);
			}
		});
		
	}

	/**
	 * left side view
	 * 
	 * Assesment Tabs
	 */
	private void initAssementQuestionPanel(PersonProxy author) {
		
		/*initialize top element from author dropdown shown on left side for admin / institutional admin*/
		if(author!=null)// admin/ institutional admin
		{
			final AssesmentProxy assesmentProxy=assesmentTabPanel.getActiveTab();
			requests.questionSumPerPersonRequest().findPercentageOfQuestionAssignedToExaminer(assesmentProxy, author).with("questionEvent").fire(new Receiver<List<QuestionSumPerPersonProxy>>() {

				@Override
				public void onSuccess(List<QuestionSumPerPersonProxy> response) {
					assementQuestionPanel.getQuestionTypeVP().clear();
					List<QuestionTypeCountPerExamProxy> questionTypeCountPerExamProxys=assesmentProxy.getQuestionTypeCountPerExams();
					
					for(QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy:questionTypeCountPerExamProxys)
					{
						for(QuestionSumPerPersonProxy questionSumPerPersonProxy:response)
						{
							QuestionTypeCountViewImpl questionTypeCountViewImpl=new QuestionTypeCountViewImpl();
							questionTypeCountViewImpl.setQuestionTypeCountPerExamProxy(questionTypeCountPerExamProxy);
							
							String questionTypeStr="";
							
							Set<QuestionTypeProxy> questionTypes=questionTypeCountPerExamProxy.getQuestionTypesAssigned();
							
							for(QuestionTypeProxy questionType:questionTypes)
							{
								questionTypeStr=questionTypeStr+questionType.getShortName();
							}
							questionTypeStr=questionTypeStr+"("+questionSumPerPersonProxy.getQuestionEvent().getEventName()+")";
							questionTypeCountViewImpl.getQuestionTypeLbl().setText(questionTypeStr);
							questionTypeCountViewImpl.setQuestionSumPerPersonProxy(questionSumPerPersonProxy);
							int questionTypeCount=questionTypeCountPerExamProxy.getQuestionTypeCount();
							int percentAllocated=questionSumPerPersonProxy.getQuestionSum();
							Integer totalQuestionAllocated=(int)(questionTypeCount*percentAllocated)/100;
							questionTypeCountViewImpl.getBlockingCounter().setText(totalQuestionAllocated.toString());
							assementQuestionPanel.getQuestionTypeVP().add(questionTypeCountViewImpl);
						}
					}
				}
			});
			
			
		}
		requests.assesmentQuestionRequest().findAssesmentQuestionsByAssesment(assesmentTabPanel.getActiveTab().getId(),author).with("question.rewiewer","question.autor","question.keywords","question.questEvent","question.comment","question.questionType").fire(new Receiver<List<AssesmentQuestionProxy>>() {

			@Override
			public void onSuccess(List<AssesmentQuestionProxy> response) {
				assementQuestionPanel.removeAll();
				Iterator<AssesmentQuestionProxy> iter = response.iterator();
				while(iter.hasNext()){
					AssesmentQuestionView question = new AssesmentQuestionViewImpl();
					
					question.setProxy(iter.next(), false);
					changeAssesmentQuestionColor(question);
					changedeleteAssesmentButtonVisiblilty(question);
					acceptOrForceAcceptButtonVisisble(question);
					
					question.setDelegate(ActivityAsignAssQuestion.this);
					//dragController.makeDraggable(question.asWidget(), question.getDragControler());
					assementQuestionPanel.addAssesmentQuestion(question);
				}
				
			}
			
	           @Override
				public void onFailure(ServerFailure error) {
						Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
						if(error.getMessage().contains("ConstraintViolationException")){
							Log.debug("Fehlen beim erstellen: Doppelter name");
							// TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
						}
						
					
				}
				@Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
					
					ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setWarnMessage(message);

					
				}
		});
		
	}



	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void tabQuestionClicked(int index) {
		initQuestionPanel(index, assesmentTabPanel.getActiveTab(),"","","");
		
	}

	@Override
	public void twistieOpenQuestionClicked(final QuestionView questionView) {
		
		requests.answerRequest().findAnswersByQuestion(questionView.getProxy().getId()).fire(new Receiver<List<AnswerProxy>>() {

			@Override
			public void onSuccess(List<AnswerProxy> response) {

				Iterator<AnswerProxy> iter = response.iterator();

				while (iter.hasNext()) {
					AnswerProxy answerProxy = (AnswerProxy) iter.next();
					AnswerView answer = new AnswerViewImpl();
					answer.setProxy(answerProxy, true);
					questionView.addAnswer(answer);
					
				} 
			

				
			}
		});
	}

	@Override
	public void addAssQuestionClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tabClicked(AssesmentProxy assesment) {
		 
		if(personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin())
		 {
			if(examAutorListMap.get(assesment) ==null)	
				populateAuthorListBox(assementQuestionPanel.getAuthorListBox(),assesment);
			else
			{
				if(examAutorListMap.get(assesment).size()>0)
					assementQuestionPanel.getAuthorListBox().setValue(examAutorListMap.get(assesment).get(0),true);
				else
					assementQuestionPanel.getAuthorListBox().setValue(null,true);
				
				assementQuestionPanel.getAuthorListBox().setAcceptableValues(examAutorListMap.get(assesment));
			}
			
		 }
		 else // not author pull down for examiner
		 {
			 assementQuestionPanel.getAuthorListBox().removeFromParent();
			 initAssementQuestionPanel(null);
		 }
		 
		// initAssementQuestionPanel(assementQuestionPanel.getAuthorListBox().getValue());
		
		//send mail button is visible to admin / institutional admin few days(Assesment.rememberBeforeClosing) before closing date of assesment
		
		Date closingDate=assesment.getDateClosed();
		Date closingDate1=new Date();
		closingDate1.setDate(closingDate.getDate());
		closingDate1.setYear(closingDate1.getYear());
		addDays(closingDate1, assesment.getRememberBeforeClosing());
		Date currentDay=new Date();
		
		if((currentDay.equals(closingDate1) || currentDay.before(closingDate1)) && (personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()))
		{
			assementQuestionPanel.getSendMail().setVisible(true);
		}
		else
		{
			assementQuestionPanel.getSendMail().removeFromParent();
		}
		 initQuestionPanel(addQuestionsTabPanel.getActiveTab(), assesment,"","","");
		 
		 
		
	}
	
	public static void addDays(Date d, int days)
	{
		d.setTime( d.getTime() + days*1000*60*60*24 );
	}

	@Override
	public void onDragEnd( DragEndEvent event) {
		// TODO Auto-generated method stub
		   Log.debug("onDragEnd: " + event);
		   if (event.getContext().finalDropController != null) {
			   Log.info(event.getSource().getClass().toString());
			   if(event.getSource() instanceof AssesmentQuestionView) // from proposed tab to left side by examiner / past to left side by examiner & admin. both logic in same method
			   {
				   final AssesmentQuestionView assesmentQuestionViewAktiv = ((AssesmentQuestionView)event.getSource());
				   Log.debug("Is AssesmentQuestionView");
				   
				   
				   
				   
				   assignQuestionToAssesment(assesmentQuestionViewAktiv,false);
				  
				   
				   
			   }
			   else if (event.getSource() instanceof QuestionView){
				   Log.debug("Is QuestionView");
			   }
			   
		    
		    	Log.info("Droptarget-Target: " + event.getContext().finalDropController.getDropTarget().getParent().getClass().getName());
		    	Log.info("Droptarget-Parent: " + event.getContext().finalDropController.getDropTarget().getParent().getClass().getName());
		    	Log.info("Droptarget-Parent: " + event.getContext().finalDropController.getDropTarget().getParent().getParent().getClass().getName());
		    }
		
	}

	@Override
	public void onDragStart(DragStartEvent event) {
		// TODO Auto-generated method stub
		  Log.debug("onDragStart: " + event);
	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		// TODO Auto-generated method stub
		if(event.getContext().finalDropController!=null){
			  Log.debug("onPreviewDragEnd: " + event);  
			   if(event.getSource() instanceof AssesmentQuestionView)
			   {
				   AssesmentQuestionView assesmentQuestionViewAktiv = ((AssesmentQuestionView)event.getSource());
				   Iterator<Widget> iter = assementQuestionPanel.getAssesmentQuestionDisplayPanel().iterator();
				   
				   if((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin() ) && assementQuestionPanel.getAuthorListBox().getValue()==null) //admin
				   {
					   ConfirmationDialogBox.showOkDialogBox(constants.information(), constants.examinerCannotBeNull());
					   throw new VetoDragException();
					 
				   }
				   
				   
				   while (iter.hasNext()){
					   Widget wid = iter.next();
					   if (wid instanceof AssesmentQuestionView){
						   if (((AssesmentQuestionView) wid).getProxy().getQuestion().getId() == assesmentQuestionViewAktiv.getProxy().getQuestion().getId()){
							  // Log.error("Keine zwei gleichen Fragen!");
							   ErrorPanel erPan = new ErrorPanel();
							   erPan.setErrorMessage("Keine zwei gleichen Fragen pro Prüfung!");
							   throw new VetoDragException();
						   }
					   }
				   }
			   }//propose question if admin
			   //accept question if examiner
			   
			   //new question tab
			   if(event.getSource() instanceof QuestionView)
			   {
				   final QuestionView questionViewAktiv = ((QuestionView)event.getSource());
				   
				   
				   if((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin() ) && assementQuestionPanel.getAuthorListBox().getValue()==null) //admin
				   {
					   ConfirmationDialogBox.showOkDialogBox(constants.information(), constants.examinerCannotBeNull());
					   throw new VetoDragException();
					 
				   }
				   assignNewQuestionToAssesment(questionViewAktiv, false);
			}
		}
	}

	protected void replaceQuestionThroughAssesmentQuestion( final AssesmentProxy assesment) {
		Iterator<Widget> iter = assementQuestionPanel.getAssesmentQuestionDisplayPanel().iterator();
		
		while (iter.hasNext()) {
			Widget widget = (Widget) iter.next();
			   if (widget instanceof QuestionView){
				   final Integer index = new Integer( assementQuestionPanel.getAssesmentQuestionDisplayPanel().getWidgetIndex(widget));
				   
				   requests.assesmentQuestionRequest()
				   			.findAssesmentQuestionByAssesmentAndQuestion(((QuestionView)widget)
						   .getProxy().getId(), assesment.getId()).with("question.rewiewer","question.autor","question.keywords","question.questEvent","question.comment","question.questionType").fire(new Receiver<AssesmentQuestionProxy>(){

							@Override
							public void onSuccess(AssesmentQuestionProxy responce) {
								if(responce==null) Log.warn("responce null ");
								//überprüfen ob reihenfolge nicht geändert hat zur sicherheit
								Widget wid = assementQuestionPanel.getAssesmentQuestionDisplayPanel().getWidget(index);
								if(wid instanceof QuestionView){
									 assementQuestionPanel.getAssesmentQuestionDisplayPanel().remove(wid);
									 AssesmentQuestionView assesmentQuestionView = new AssesmentQuestionViewImpl();
									 assesmentQuestionView.setProxy(responce, false);
									 assesmentQuestionView.setDelegate(ActivityAsignAssQuestion.this);
									 
									 //change color
									 changeAssesmentQuestionColor(assesmentQuestionView);
									 
									 //accept / force accept access criteria
									 acceptOrForceAcceptButtonVisisble(assesmentQuestionView);
									 
									 assementQuestionPanel.addAssesmentQuestion(assesmentQuestionView);
									 assesmentQuestionView.setOpen();
									 
									 
									 changedeleteAssesmentButtonVisiblilty(assesmentQuestionView);
								}
								else {
									replaceQuestionThroughAssesmentQuestion(assesment);
								}

								
							}
							
					           @Override
								public void onFailure(ServerFailure error) {
										Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
										if(error.getMessage().contains("ConstraintViolationException")){
											Log.debug("Fehlen beim erstellen: Doppelter name");
											// TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
										}
										
										
										
									
								}
								@Override
								public void onViolation(Set<Violation> errors) {
									Iterator<Violation> iter = errors.iterator();
									String message = "";
									while(iter.hasNext()){
										message += iter.next().getMessage() + "<br>";
									}
									Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
									
									ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setWarnMessage(message);

									
								}
						   
						   });
				   
				   
				   
				  
			   }
		}
		
//		

		
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
		// TODO Auto-generated method stub
		Log.debug("onPreviewDragStart: " + event);
		
	}

	@Override
	public void twistieOpenAssQuestionClicked(final AssesmentQuestionView questionView, final boolean isInAssement) {
		requests.answerToAssQuestionRequest().findAnswerToAssQuestionByAssesmentQuestion(questionView.getProxy().getId()).with("answers").fire(new Receiver<List<AnswerToAssQuestionProxy>>() {

			@Override
			public void onSuccess(List<AnswerToAssQuestionProxy> response) {

				Iterator<AnswerToAssQuestionProxy> iter = response.iterator();

				while (iter.hasNext()) {
					AnswerToAssQuestionProxy answerToAssQuestionProxy = (AnswerToAssQuestionProxy) iter.next();
					AnswerView answer = new AnswerViewImpl();
					answer.setProxy(answerToAssQuestionProxy, false);
					questionView.addAnswer(answer);
					
				} 
			

				
			}
		});
		
	}

	@Override
	public void addQuestionClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeChanged(Place place) {
		// TODO add place changed code here
		
	}
	
	/*called when admin changes author from drop down*/
	
	@Override
	public void authorValueChanged(PersonProxy value) {
		
		initAssementQuestionPanel(value);
	}

	@Override
	public void forceAccept(final AssesmentQuestionViewImpl assesmentQuestionViewImpl) {
		AssesmentQuestionProxy assesmentQuestion=assesmentQuestionViewImpl.getProxy();
		final AssesmentQuestionRequest request=requests.assesmentQuestionRequest();
		assesmentQuestion=request.edit(assesmentQuestion);
		
		if(assesmentQuestionViewImpl.getForceAcceptButton().getText().equals(constants.forceAccept()))
		{
		assesmentQuestion.setIsForcedByAdmin(true);
		assesmentQuestion.setIsAssQuestionAdminProposal(false);
		}
		else
		{
			assesmentQuestion.setIsForcedByAdmin(false);
			assesmentQuestion.setIsAssQuestionAdminProposal(false);
			assesmentQuestion.setIsAssQuestionAcceptedAdmin(true);
			assesmentQuestion.setIsAssQuestionAcceptedAutor(false);
		}
		request.persist().using(assesmentQuestion).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				
				assesmentQuestionViewImpl.getForceAcceptButton().removeFromParent();	
				requests.assesmentQuestionRequest().findAssesmentQuestion(assesmentQuestionViewImpl.getProxy().getId()).with("question.rewiewer","question.autor","question.keywords","question.questEvent","question.comment","question.questionType").fire(new Receiver<AssesmentQuestionProxy>() {

					@Override
					public void onSuccess(AssesmentQuestionProxy response) {
						assesmentQuestionViewImpl.setProxy(response, false);
						changeAssesmentQuestionColor(assesmentQuestionViewImpl);
						
					}
				});
				
			}
		});
		
		
		
	}

	@Override
	public void loadTemplate() {
		
		requests.assesmentQuestionRequest().loadTemplate().fire(new Receiver<String>() {

			@Override
			public void onSuccess(String response) {
				assementQuestionPanel.getSendMailPopupViewImpl().setMessageContent(response);
				
			}
		});
		
		
	}
	
	/*Send mail to all examiner assigned to assesment*/
	@Override
	public void sendMail(String messageContent) {
		requests.assesmentQuestionRequest().sendMail(examAutorListMap.get(assesmentTabPanel.getActiveTab()),messageContent,constants.mailSubject(),assesmentTabPanel.getActiveTab()).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				
				if(response)
				{
					ConfirmationDialogBox.showOkDialogBox(constants.success(), constants.sendMailSuccess());
				}
				else
				{
					ConfirmationDialogBox.showOkDialogBox(constants.failure(), constants.sendMailFailure());
				}
				assementQuestionPanel.getSendMailPopupViewImpl().hide();
				
			}
		});
		
	}
	/*color code for assesment question panel left side*/
	public void changeAssesmentQuestionColor(AssesmentQuestionView question)
	{
		if(question.getProxy().getIsAssQuestionAcceptedAdmin())//Accepted
		{
			question.getQuestionTable().addClassName("accept");
			question.getQuestionTable().removeClassName("not-accept");
			question.getQuestionTable().removeClassName("force-accept");
		}else if(question.getProxy().getIsForcedByAdmin())//force Accepted
		{
			question.getQuestionTable().addClassName("force-accept");
			question.getQuestionTable().removeClassName("not-accept");
			question.getQuestionTable().removeClassName("accept");
		}
		else //red color
		{
			question.getQuestionTable().addClassName("not-accept");
			question.getQuestionTable().removeClassName("force-accept");
			question.getQuestionTable().removeClassName("accept");
		}
	}
	
	/*force accept/ accept button access criteria left side */
	public void acceptOrForceAcceptButtonVisisble(AssesmentQuestionView question)
	{
		if((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()) && question.getProxy().getIsAssQuestionAdminProposal())
		{
			question.getForceAcceptButton().setVisible(true);
		}
		else if((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()) && question.getProxy().getIsAssQuestionAcceptedAutor())
		{
			question.getForceAcceptButton().setVisible(true);
			question.getForceAcceptButton().setText(constants.accept());
		}
		else
		{
			question.getForceAcceptButton().removeFromParent();
		}
	}
	
	
	/*New Question to Assesment Question*/
	public void assignNewQuestionToAssesment(final QuestionView questionViewAktiv,final boolean viaAddButton)  throws VetoDragException 
	{
		   Iterator<Widget> iter = assementQuestionPanel.getAssesmentQuestionDisplayPanel().iterator();
		   while (iter.hasNext()){
			   Widget wid = iter.next();
			   if (wid instanceof AssesmentQuestionView){
				   if (((AssesmentQuestionView) wid).getProxy().getQuestion().getId() == questionViewAktiv.getProxy().getId()){
					   //Log.error("Keine zwei gleichen Fragen!");
					   ErrorPanel erPan = new ErrorPanel();
					   erPan.setErrorMessage("Keine zwei gleichen Fragen pro Prüfung!");
					   throw new VetoDragException();
				   }
			   }
		   }
		   
		   AssesmentQuestionRequest assesmentQuestionRequest = requests.assesmentQuestionRequest();
		   //AnswerToAssQuestionRequest answerToAssQuestionRequest = requests.answerToAssQuestionRequest();
		   Iterator<Widget> answerPanelIterator = questionViewAktiv.getAnswerPanel().iterator();
		   
		   AssesmentQuestionProxy assQuestion = assesmentQuestionRequest.create(AssesmentQuestionProxy.class);
		   assQuestion.setAssesment(assesmentTabPanel.getActiveTab());
		   assQuestion.setDateAdded(new Date());
		  
		  /* assQuestion.setIsAssQuestionAcceptedAdmin(false);
		   assQuestion.setIsAssQuestionAcceptedAutor(true); //If is Propolsal set false
		   assQuestion.setIsAssQuestionAcceptedRewiever(false);
		   assQuestion.setIsAssQuestionAdminProposal(false); //If is Propolsal set true
*/				   
		   
	    	/*
	    	 * if person is both admin and author
	    	 * */
/*			    	if(questionViewAktiv.getProxy().getAutor().equals(userLoggedIn) && (personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin() ))
	    	{
	    		assQuestion.setIsAssQuestionAcceptedAdmin(true);
	    		assQuestion.setIsAssQuestionAcceptedAutor(false);
	    		assQuestion.setIsAssQuestionAcceptedRewiever(false);
	    		assQuestion.setIsForcedByAdmin(false);
	        	assQuestion.setIsAssQuestionAdminProposal(false);
	    	}
	    	else */if((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin() )) //admin
	    	{
	    		assQuestion.setIsAssQuestionAcceptedAdmin(false);
	    		assQuestion.setIsAssQuestionAcceptedAutor(false);
	        	assQuestion.setIsAssQuestionAcceptedRewiever(false);
	        	assQuestion.setIsForcedByAdmin(false);
	        	assQuestion.setIsAssQuestionAdminProposal(true);
	    	}
	    	else //author / examiner
	    	{
	    		assQuestion.setIsAssQuestionAcceptedAdmin(false);
	    		assQuestion.setIsAssQuestionAcceptedAutor(true);
	    		assQuestion.setIsAssQuestionAcceptedRewiever(false);
	    		assQuestion.setIsForcedByAdmin(false);
	    		assQuestion.setIsAssQuestionAdminProposal(false);
	    	}
		   
		   
		 /*  if(personRightProxy.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin()) //Proposed By Admin
		   {
			   assQuestion.setIsAssQuestionAcceptedAdmin(false);
			   assQuestion.setIsAssQuestionAcceptedAutor(false); //If is Propolsal set false
			   assQuestion.setIsAssQuestionAcceptedRewiever(false);
			   assQuestion.setIsAssQuestionAdminProposal(true); //If is Propolsal set true
			   assQuestion.setIsForcedByAdmin(false);
		   }
		   else //For Examiner
		   {
			   assQuestion.setIsAssQuestionAcceptedAdmin(false);
			   assQuestion.setIsAssQuestionAcceptedAutor(true); //If is Propolsal set false
			   assQuestion.setIsAssQuestionAcceptedRewiever(false);
			   assQuestion.setIsAssQuestionAdminProposal(false); //If is Propolsal set true
			   assQuestion.setIsForcedByAdmin(false);
		   }
		   */
		   assQuestion.setQuestion(questionViewAktiv.getProxy());
		   assQuestion.setRewiewer(questionViewAktiv.getProxy().getRewiewer());
		   
		   PersonProxy author=null;
		   
		   if(viaAddButton)
		   {
			   author=questionPanel.getAuthorListBox().getValue();
		   }
		   else
		   {
			   author=assementQuestionPanel.getAuthorListBox().getValue();
		   }
		   
		   if(author !=null )
		   {
			   assQuestion.setAutor(author); //TODO Ändern auf aktuell eingeloggte Person
		   }
		   else 
		   {
			   assQuestion.setAutor(userLoggedIn); //TODO Ändern auf aktuell eingeloggte Person
		   }
		   
		   Set<AnswerToAssQuestionProxy> answerToAssQuestionProxySet =  new HashSet<AnswerToAssQuestionProxy>();
		   
		   while (answerPanelIterator.hasNext()) {
			Widget widget = (Widget) answerPanelIterator.next();
			if (widget instanceof AnswerView){
				AnswerView answerView = (AnswerView) widget;
				if(answerView.getChecked()){
					AnswerToAssQuestionProxy answerToAssQuestionProxy = assesmentQuestionRequest.create(AnswerToAssQuestionProxy.class);
					answerToAssQuestionProxy.setAnswers(answerView.getProxy());
					answerToAssQuestionProxy.setAssesmentQuestion(assQuestion);
					answerToAssQuestionProxy.setSortOrder(1);
					Log.debug(answerToAssQuestionProxy.toString());
					//answerToAssQuestionRequest.persist().using(answerToAssQuestionProxy);
					answerToAssQuestionProxySet.add(answerToAssQuestionProxy);
				}
			  }
		   }
		   
		   
		   
		   assQuestion.setAnswersToAssQuestion(answerToAssQuestionProxySet);
	     assesmentQuestionRequest.persist().using(assQuestion).with("answersToAssQuestion").fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void ignore) {
					 Log.debug("sucesfull saved");
					 if(viaAddButton)
					 {
						 ((QuestionViewImpl)questionViewAktiv).removeFromParent();
					
						 if(((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()) && assementQuestionPanel.getAuthorListBox().getValue().equals(questionPanel.getAuthorListBox().getValue())))
								assementQuestionPanel.getAssesmentQuestionDisplayPanel().add(questionViewAktiv);
								
								if(!((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()))) //examiner
								{
									assementQuestionPanel.getAssesmentQuestionDisplayPanel().add(questionViewAktiv);
								}
					
					 }
					 replaceQuestionThroughAssesmentQuestion(assesmentTabPanel.getActiveTab());
					 
					
				}
				
		           @Override
					public void onFailure(ServerFailure error) {
							Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
							if(error.getMessage().contains("ConstraintViolationException")){
								Log.debug("Fehlen beim erstellen: Doppelter name");
								// TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
							}
							
							
							
						
					}
					@Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
						
						ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setWarnMessage(message);

						
					}
			});
	}
	
	/*This method is used to assign question from proposed tab and past tab to left side
	 * 
	 * common to both drag and drop and add question to assesment button
	 * */
	public void assignQuestionToAssesment(final AssesmentQuestionView assesmentQuestionViewAktiv,final boolean viaAddButton)
	{
		PersonProxy selectedAuthor=null;
		   
		   if((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()) )
		   {
			   if(!viaAddButton)
				   selectedAuthor=assementQuestionPanel.getAuthorListBox().getValue();
			   else
			   {
				   selectedAuthor=questionPanel.getAuthorListBox().getValue();
			   }
		   }
		  
		  
		   
		   requests.assesmentQuestionRequest().copyAssesmentQuestion(assesmentQuestionViewAktiv.getProxy().getId(), assesmentTabPanel.getActiveTab().getId(),selectedAuthor).with("question.rewiewer","question.autor","question.keywords","question.questEvent","question.comment","question.questionType").fire(new Receiver<AssesmentQuestionProxy>() {

				@Override
				public void onSuccess(AssesmentQuestionProxy response) {
					 Log.debug("sucesfull copied");
					assesmentQuestionViewAktiv.setProxy(response, false);
					changeAssesmentQuestionColor(assesmentQuestionViewAktiv);
					acceptOrForceAcceptButtonVisisble(assesmentQuestionViewAktiv);
					if(viaAddButton)
					{
						((AssesmentQuestionViewImpl)assesmentQuestionViewAktiv).removeFromParent();
						
						if(((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()) && assementQuestionPanel.getAuthorListBox().getValue().equals(response.getAutor())))
						assementQuestionPanel.addAssesmentQuestion(assesmentQuestionViewAktiv);
						
						if(!((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()))) //examiner
						{
							assementQuestionPanel.addAssesmentQuestion(assesmentQuestionViewAktiv);
						}
						
						
					}
					changedeleteAssesmentButtonVisiblilty(assesmentQuestionViewAktiv);
					}
				
		           @Override
					public void onFailure(ServerFailure error) {
							Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
							if(error.getMessage().contains("ConstraintViolationException")){
								Log.debug("Fehlen beim erstellen: Doppelter name");
								// TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
							}
							
						
					}
					@Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while(iter.hasNext()){
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Event -" + message);
						
						ErrorPanel erorPanel = new ErrorPanel();
	        	  erorPanel.setWarnMessage(message);

						
					}
			});
	}
	
	/*Add button clicked from past and proposed tab*/
	@Override
	public void addToAssesmentButtonClicked(
			AssesmentQuestionViewImpl assesmentQuestionViewImpl) {
		assignQuestionToAssesment(assesmentQuestionViewImpl,true);
		
	}
	
	/*Add button clicked from new question tab*/
	@Override
	public void addNewQuestionToAssesment(QuestionViewImpl questionViewImpl)  {
		try{
			assignNewQuestionToAssesment(questionViewImpl, true);
		}
		catch(Exception e)
		{
			
		}
		
	}

	@Override
	public void searchQuestion(String questionshortName, Integer questionId,
			String questionType) {
		String qId="";
		if(questionId !=null)
		{
			qId=questionId.toString();
		}
		initQuestionPanel(addQuestionsTabPanel.getActiveTab(), assesmentTabPanel.getActiveTab(),questionshortName,qId,questionType);
	}

	@Override
	public void deleteAssesmentQuestion(
			final AssesmentQuestionViewImpl assesmentQuestionViewImpl) {
		AssesmentQuestionRequest request=requests.assesmentQuestionRequest();
		AssesmentQuestionProxy a=assesmentQuestionViewImpl.getProxy();
		a=request.edit(a);
		request.remove().using(a).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				assesmentQuestionViewImpl.removeFromParent();
				 initQuestionPanel(addQuestionsTabPanel.getActiveTab(), assesmentTabPanel.getActiveTab(),"","","");

			}
		});
		
		
	}
	/*Accept and force accept Assesment Question cannot be deleted*/
	public void changedeleteAssesmentButtonVisiblilty(AssesmentQuestionView assesmentQuestionViewAktiv)
	{
		if(assesmentQuestionViewAktiv.getProxy().getIsAssQuestionAcceptedAdmin() || assesmentQuestionViewAktiv.getProxy().getIsForcedByAdmin())
		{
			assesmentQuestionViewAktiv.getDeleteFromAssesment().setVisible(false);
		}
		else
		{
			assesmentQuestionViewAktiv.getDeleteFromAssesment().setVisible(true);
		}
	}

}

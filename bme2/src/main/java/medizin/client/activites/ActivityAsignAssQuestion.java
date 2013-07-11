package medizin.client.activites;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import medizin.client.shared.Validity;
import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.assignquestion.AddQuestionsTabPanel;
import medizin.client.ui.view.assignquestion.AddQuestionsTabPanelImpl;
import medizin.client.ui.view.assignquestion.AnswerView;
import medizin.client.ui.view.assignquestion.AnswerViewImpl;
import medizin.client.ui.view.assignquestion.AsignAssQuestionView;
import medizin.client.ui.view.assignquestion.AsignAssQuestionViewImpl;
import medizin.client.ui.view.assignquestion.AssesmentQuestionPanel;
import medizin.client.ui.view.assignquestion.AssesmentQuestionPanelImpl;
import medizin.client.ui.view.assignquestion.AssesmentQuestionView;
import medizin.client.ui.view.assignquestion.AssesmentQuestionViewImpl;
import medizin.client.ui.view.assignquestion.AssesmentTabPanel;
import medizin.client.ui.view.assignquestion.AssesmentTabPanelImpl;
import medizin.client.ui.view.assignquestion.QuestionPanel;
import medizin.client.ui.view.assignquestion.QuestionTypeCountProxy;
import medizin.client.ui.view.assignquestion.QuestionTypeCountViewImpl;
import medizin.client.ui.view.assignquestion.QuestionView;
import medizin.client.ui.view.assignquestion.QuestionViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.shared.BlockingTypes;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;

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
	private Map<String, QuestionTypeCountProxy> questionTypeViewMap=new HashMap<String, QuestionTypeCountProxy>();

	private McAppRequestFactory requests;
	private PlaceController placeController;
	private AddQuestionsTabPanel addQuestionsTabPanel;
	private AssesmentQuestionPanel assementQuestionPanel;
	private AssesmentTabPanel assesmentTabPanel;
	private QuestionPanel questionPanel;
	final private HashMap<AssesmentProxy, List<PersonProxy>> examAutorListMap=new HashMap<AssesmentProxy, List<PersonProxy>>();
	private List<QuestionTypeCountProxy> questionTypeCountProxyList=new ArrayList<QuestionTypeCountProxy>();

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
				
				if(response.size()==0)
				{
					assementQuestionPanel.getTable().setRowCount(0);
				}
				
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
		
		PersonProxy author=null;
		
		//admin / institutional admin
		if(personRightProxy.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())
		{
			action++;
			
			//populate author list box
			
			if(examAutorListMap.get(assesment) ==null)		
			{
				populateAuthorListBox(questionPanel.getAuthorListBox(),assesment,true);
				//questionPanel.getAuthorListBox().setValue(assementQuestionPanel.getAuthorListBox().getValue());
			}
			else
			{
				if(examAutorListMap.get(assesment).size()>0)
				questionPanel.getAuthorListBox().setValue(assementQuestionPanel.getAuthorListBox().getValue());
				else
					questionPanel.getAuthorListBox().setValue(null);
				
				questionPanel.getAuthorListBox().setAcceptableValues(examAutorListMap.get(assesment));
			}
			author=questionPanel.getAuthorListBox().getValue();
		}
		else
		{
			questionPanel.getAuthorListBox().removeFromParent();
			author=userLoggedIn;
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
			requests.questionRequest().findQuestionsByMc(assesment.getMc().getId(),questionId,questionType,questionName,assesment,author).with("rewiewer", "questEvent", "autor", "questionType", "keywords").fire(new Receiver<List<QuestionProxy>>() {

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
		
		PersonProxy author=null;
		
		if(personRightProxy.getIsAdmin() || personRightProxy.getIsInstitutionalAdmin())
		{
			author=questionPanel.getAuthorListBox().getValue();
		}
		else
		{
			author=userLoggedIn;
		}
		requests.assesmentQuestionRequest().findAssesmentQuestionsByMc(assesment.getId(),assesment.getMc().getId(),questionId,questionType,questionName,questionPanel.getAuthorListBox().getValue()).with("question.rewiewer","question.autor","question.keywords","question.questEvent","question.comment","question.questionType").fire(new Receiver<List<AssesmentQuestionProxy>>() {

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
	private void populateAuthorListBox(final ValueListBox<PersonProxy> authorListBox,final AssesmentProxy assesment,final boolean isRightSide) {
		
		requests.assesmentQuestionRequest().findAuthorListByAssesment(assesment).fire(new Receiver<List<PersonProxy>>() {

			@Override
			public void onSuccess( List<PersonProxy> response) {
				
				
				//authorListBox.setValue(null);
				//authorListBox.setAcceptableValues(new ArrayList<PersonProxy>());
				
				
				//if(response.size() >0)
			//	{
				if(response.size() >0)
				{
					if(!isRightSide)
						authorListBox.setValue(response.get(0));
					else
					{
						authorListBox.setValue(assementQuestionPanel.getAuthorListBox().getValue());
					}
					initAssementQuestionPanel(assementQuestionPanel.getAuthorListBox().getValue());
				}
				else
				{
					authorListBox.setValue(null);
					initAssementQuestionPanel(userLoggedIn);
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
	private void initAssementQuestionPanel(final PersonProxy author) {
		//questionTypeCountProxyList=new ArrayList<QuestionTypeCountProxy>();
		questionTypeCountProxyList.clear();
		assementQuestionPanel.getTable().setRowCount(0);
		assementQuestionPanel.getTable().setRowData(questionTypeCountProxyList);
		assementQuestionPanel.getTable().redraw();
		
		/*initialize top element from author dropdown shown on left side for admin / institutional admin*/
		// if(author!=null)// admin/ institutional admin
		//{
			final AssesmentProxy assesmentProxy=assesmentTabPanel.getActiveTab();
			
			
		//}
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
				
				requests.questionSumPerPersonRequest().findPercentageOfQuestionAssignedToExaminer(assesmentProxy, author).with("questionEvent").fire(new Receiver<List<QuestionSumPerPersonProxy>>() {

					@Override
					public void onSuccess(List<QuestionSumPerPersonProxy> response) {
						initTopElement(response);
					}
				});

				
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


	/*This method will display top element: QuestionType count*/
	protected void initTopElement(List<QuestionSumPerPersonProxy> response) {
		//assementQuestionPanel.getQuestionTypeVP().clear();
		List<QuestionTypeCountPerExamProxy> questionTypeCountPerExamProxys=assesmentTabPanel.getActiveTab().getQuestionTypeCountPerExams();
		questionTypeViewMap.clear();
		//questionTypeCountProxyList=new ArrayList<QuestionTypeCountProxy>();
		questionTypeCountProxyList.clear();
		
		//questionTypeCountProxyList.clear();
		assementQuestionPanel.getTable().setRowCount(0);
		assementQuestionPanel.getTable().setRowData(questionTypeCountProxyList);
		assementQuestionPanel.getTable().setVisibleRange(0, 0);
		assementQuestionPanel.getTable().redraw();
		
		for(QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy:questionTypeCountPerExamProxys)
		{
			for(QuestionSumPerPersonProxy questionSumPerPersonProxy:response)
			{
				
				QuestionTypeCountProxy proxy=new QuestionTypeCountProxy();

				
				/*QuestionTypeCountViewImpl questionTypeCountViewImpl=new QuestionTypeCountViewImpl();
				questionTypeCountViewImpl.setQuestionTypeCountPerExamProxy(questionTypeCountPerExamProxy);
				
				String questionTypeStr="";
				*/
				Set<QuestionTypeProxy> questionTypes=questionTypeCountPerExamProxy.getQuestionTypesAssigned();
				//int k=0;
				for(QuestionTypeProxy questionType:questionTypes)
				{
					/*if(k==0)
					questionTypeStr=questionTypeStr+questionType.getShortName();
					else
					{
						questionTypeStr=questionTypeStr+","+questionType.getShortName();

					}
*/					
					questionTypeViewMap.put("q"+questionType.getId()+"s"+questionSumPerPersonProxy.getQuestionEvent().getId(), proxy);
					
				//	k++;	
				}
				
				
			/*	questionTypeStr=questionTypeStr+"("+questionSumPerPersonProxy.getQuestionEvent().getEventName()+")";
				questionTypeCountViewImpl.getQuestionTypeLbl().setText(questionTypeStr);
				questionTypeCountViewImpl.setQuestionSumPerPersonProxy(questionSumPerPersonProxy);
			*/	int questionTypeCount=questionTypeCountPerExamProxy.getQuestionTypeCount();
				int percentAllocated=questionSumPerPersonProxy.getQuestionSum();
				Integer totalQuestionAllocated=(int)(questionTypeCount*percentAllocated)/100;
				Integer questionAssigned=-totalQuestionAllocated;
				
				int count=0;
				for(int i=0;i<assementQuestionPanel.getAssesmentQuestionDisplayPanel().getWidgetCount();i++)
				{
					AssesmentQuestionView question =(AssesmentQuestionViewImpl)assementQuestionPanel.getAssesmentQuestionDisplayPanel().getWidget(i);
					if(questionTypes.contains(question.getProxy().getQuestion().getQuestionType()) && question.getProxy().getQuestion().getQuestEvent().equals(questionSumPerPersonProxy.getQuestionEvent()))
					{						
						questionAssigned++;
						count++;
					}
				}
			/*	questionTypeCountViewImpl.setTotalQuestionAllowed(totalQuestionAllocated);
				questionTypeCountViewImpl.setTotalQuestionAllocated(count);
				questionTypeCountViewImpl.getBlockingCounter().setText(questionAssigned.toString());
			*/	
				
				//assementQuestionPanel.getQuestionTypeVP().add(questionTypeCountViewImpl);
				
				proxy.setQuestionSumPerPersonProxy(questionSumPerPersonProxy);
				proxy.setQuestionTypeCountPerExamProxy(questionTypeCountPerExamProxy);
				proxy.setCount(questionAssigned);
				proxy.setTotalQuestionAllocated(totalQuestionAllocated);
				proxy.setTotalQuestionAllowed(count);
				questionTypeCountProxyList.add(proxy);
				
				
			}
		}
		
		assementQuestionPanel.getTable().setRowCount(questionTypeCountProxyList.size(),true);
		int pageSize=((AssesmentQuestionPanelImpl) assementQuestionPanel).getPageSize();
		assementQuestionPanel.getTable().setVisibleRange(0, pageSize);
		
		if(questionTypeCountProxyList.size()>pageSize)
		{
			List<QuestionTypeCountProxy> sublist=questionTypeCountProxyList.subList(0, pageSize);

			assementQuestionPanel.getTable().setRowData(0,sublist);
		}
		else
		{
		
		assementQuestionPanel.getTable().setRowData(0,questionTypeCountProxyList);
			
		}
		
		
	}
	
	/* Blocking Validations*/
	
	public boolean checkQuestionTypeCountBlocking(QuestionProxy questionProxy)
	{
		QuestionTypeCountProxy questionTypeCountProxy=getQuestionTypeCountProxy(questionProxy);
		
		BlockingTypes blockingTypes=questionTypeCountProxy.getQuestionTypeCountPerExamProxy().getBlockingType();
		
		
		int totalsum=questionTypeCountProxy.getQuestionTypeCountPerExamProxy().getQuestionTypeCount().intValue();
		if(blockingTypes==BlockingTypes.PERSONAL_BLOCKING && questionTypeCountProxy.getCount() >=0)
		{
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.personalBlockingWarning()+" : "+questionTypeCountProxy.getTotalQuestionAllowed());
			return false;
		}
		else if(blockingTypes==BlockingTypes.GLOBAL_BLOCKING && questionTypeCountProxy.getTotalQuestionAllocated() >= totalsum)
		{
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.globalBlockingWarning()+" : "+totalsum);
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	/*public QuestionTypeCountViewImpl getQuestionTypeCountViewImpl(QuestionProxy questionProxy)
	{
		String key="q"+questionProxy.getQuestionType().getId()+"s"+questionProxy.getQuestEvent().getId();
		return questionTypeViewMap.get(key);
		
	}*/
	
	public QuestionTypeCountProxy getQuestionTypeCountProxy(QuestionProxy questionProxy)
	{
		String key="q"+questionProxy.getQuestionType().getId()+"s"+questionProxy.getQuestEvent().getId();
		return questionTypeViewMap.get(key);		
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
				populateAuthorListBox(assementQuestionPanel.getAuthorListBox(),assesment,false);
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
			 initAssementQuestionPanel(userLoggedIn);
		 }
		 
		// initAssementQuestionPanel(assementQuestionPanel.getAuthorListBox().getValue());
		
		//send mail button is visible to admin / institutional admin few days(Assesment.rememberBeforeClosing) before closing date of assesment
		
		Date closingDate=assesment.getDateClosed();
		Date closingDate1=new Date();
		closingDate1.setDate(closingDate.getDate());
		closingDate1.setYear(closingDate1.getYear());
		addDays(closingDate1, assesment.getRememberBeforeClosing());
		Date currentDay=new Date();
		
		if((currentDay.equals(closingDate1) || currentDay.after(closingDate1)) && (personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin()))
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
		d.setTime( d.getTime() - days*1000*60*60*24 );
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
				   
				   
				   
				   
				   if(checkQuestionTypeCountBlocking(assesmentQuestionViewAktiv.getProxy().getQuestion()))
				   {
						incrementBlockingCounter(assesmentQuestionViewAktiv.getProxy().getQuestion());
						   assignQuestionToAssesment(assesmentQuestionViewAktiv,false);
				   }
				  
				   
				   
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
			   if(event.getSource() instanceof AssesmentQuestionView)//from past / proposed tab
			   {
				   AssesmentQuestionView assesmentQuestionViewAktiv = ((AssesmentQuestionView)event.getSource());
				   Iterator<Widget> iter = assementQuestionPanel.getAssesmentQuestionDisplayPanel().iterator();
				   
				   if((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin() ) && assementQuestionPanel.getAuthorListBox().getValue()==null) //admin
				   {
					   ConfirmationDialogBox.showOkDialogBox(constants.information(), constants.examinerCannotBeNull());
					   throw new VetoDragException();
					 
				   }
				   
				   if((personRightProxy.getIsInstitutionalAdmin() || personRightProxy.getIsAdmin() ) && assementQuestionPanel.getAuthorListBox().getValue()!=null) //admin
				   {
					   if(!checkQuestionTypeCountBlocking(assesmentQuestionViewAktiv.getProxy().getQuestion()))
					   {
						   throw new VetoDragException();
					   }
						      
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
				   
				   if(checkQuestionTypeCountBlocking(questionViewAktiv.getProxy()) && validateAssesmentQuestion(questionViewAktiv.getProxy(), (QuestionViewImpl)questionViewAktiv))
					{
						incrementBlockingCounter(questionViewAktiv.getProxy());
						assignNewQuestionToAssesment(questionViewAktiv, false);
					}
				   else
				   {
					   throw new VetoDragException();
				   }
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
		questionPanel.getAuthorListBox().setValue(value);
		
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
		if(checkQuestionTypeCountBlocking(assesmentQuestionViewImpl.getProxy().getQuestion()))
		{
			incrementBlockingCounter(assesmentQuestionViewImpl.getProxy().getQuestion());
			assignQuestionToAssesment(assesmentQuestionViewImpl,true);
		}
		
	}
	
	/*Add button clicked from new question tab*/
	@Override
	public void addNewQuestionToAssesment(QuestionViewImpl questionViewImpl)  {
		try{
			if(checkQuestionTypeCountBlocking(questionViewImpl.getProxy()) && validateAssesmentQuestion(questionViewImpl.getProxy(), questionViewImpl))
			{
				incrementBlockingCounter(questionViewImpl.getProxy());
				assignNewQuestionToAssesment(questionViewImpl, true);
			}
			
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
		final QuestionTypeCountProxy questionTypeCountProxy =getQuestionTypeCountProxy(a.getQuestion());
		a=request.edit(a);
		request.remove().using(a).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				assesmentQuestionViewImpl.removeFromParent();
				decrementBlockingCounter(questionTypeCountProxy);
				initQuestionPanel(addQuestionsTabPanel.getActiveTab(), assesmentTabPanel.getActiveTab(),"","","");

			}
		});
		
		
		
		
	}
	
	public void incrementBlockingCounter(QuestionProxy questionProxy)
	{
		QuestionTypeCountProxy questionTypeCountProxy=getQuestionTypeCountProxy(questionProxy);
		Integer counter=questionTypeCountProxy.getCount()+1;
		questionTypeCountProxy.setCount(counter);
		//questionTypeCountViewImpl.getBlockingCounter().setText(counter.toString());
		assementQuestionPanel.getTable().redraw();
	}
	
	public void decrementBlockingCounter(QuestionTypeCountViewImpl questionTypeCountViewImpl)
	{
		//QuestionTypeCountViewImpl questionTypeCountViewImpl=getQuestionTypeCountViewImpl(questionProxy);
		Integer counter=new Integer(questionTypeCountViewImpl.getBlockingCounter().getText())-1;
		questionTypeCountViewImpl.getBlockingCounter().setText(counter.toString());
	}
	
	public void decrementBlockingCounter(QuestionTypeCountProxy proxy)
	{
		//QuestionTypeCountViewImpl questionTypeCountViewImpl=getQuestionTypeCountViewImpl(questionProxy);
		Integer counter=proxy.getCount()-1;
		proxy.setCount(counter);
		assementQuestionPanel.getTable().redraw();

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

	@Override
	public void authorValueChangedFromRightSide(PersonProxy value) {
		assementQuestionPanel.getAuthorListBox().setValue(value);	
		
		initAssementQuestionPanel(value);
		
		
	}
	
	/*Question Type Validity*/
	public boolean validateAssesmentQuestion(QuestionProxy questionProxy,QuestionViewImpl questionViewImpl)
	{
		//1. Textual Question type Validation during assignment of new question from right side to left side
		if(questionProxy.getQuestionType().getQuestionType()==QuestionTypes.Textual)
		{
			Integer sumOfAnswer=questionProxy.getQuestionType().getSumAnswer();
			Integer sumOfTrueAnsw=questionProxy.getQuestionType().getSumTrueAnswer();
			
			//check true question
			if(questionProxy.getQuestionType().getSumTrueAnswer() > 0)
			{
				int totalAnswers=questionViewImpl.getAnswerPanel().getWidgetCount();
				int trueAnswer=0;
				int totalAnswerSelected=0;
				for(int i=0;i<totalAnswers;i++)
				{
					
					AnswerViewImpl answerViewImpl=(AnswerViewImpl)questionViewImpl.getAnswerPanel().getWidget(i);
					if(answerViewImpl.getChecked() && answerViewImpl.getProxy().getValidity()== Validity.Wahr)
					{
						trueAnswer++;
					}
					if(answerViewImpl.getChecked())
					{
						totalAnswerSelected++;
					}
				}
				
				if(sumOfTrueAnsw==trueAnswer && totalAnswerSelected==sumOfAnswer)
					return true;
				else
				{
					if(sumOfTrueAnsw!=trueAnswer)
					ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.sumOfTrueAnswer() +sumOfTrueAnsw);
					
					if(totalAnswerSelected != sumOfAnswer)
					{
						ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.sumOfAnswer() +sumOfAnswer);

					}
					
					return false;
				}
				
			}
			else//2. all answer should be false
			{
				int totalAnswers=questionViewImpl.getAnswerPanel().getWidgetCount();
				int sumFalseAnswers=questionViewImpl.getProxy().getQuestionType().getSumFalseAnswer();
				int falseAnswerSelected=0;
				for(int i=0;i<totalAnswers;i++)
				{
					AnswerViewImpl answerViewImpl=(AnswerViewImpl)questionViewImpl.getAnswerPanel().getWidget(i);
					
					if(answerViewImpl.getChecked() && answerViewImpl.getProxy().getValidity()== Validity.Falsch)
					{
						falseAnswerSelected++;
					}
					
				}
				
				if(sumFalseAnswers==falseAnswerSelected)
				{
					return true;					
				}
				else
				{
					ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.sumOfFalseAnswer() +sumFalseAnswers);
					return false;
				}
			}
		}//3. Need to validate only sum of answer
		else if(questionProxy.getQuestionType().getQuestionType()==QuestionTypes.Sort)
		{
			int totalAnswers=questionViewImpl.getAnswerPanel().getWidgetCount();
			int totalSelectedAnswers=0;
			int sumOfAnswer=questionProxy.getQuestionType().getSumAnswer();
			for(int i=0;i<totalAnswers;i++)
			{
				AnswerViewImpl answerViewImpl=(AnswerViewImpl)questionViewImpl.getAnswerPanel().getWidget(i);
				
				if(answerViewImpl.getChecked())
				{
					totalSelectedAnswers++;
				}
				
			}
			
			if(sumOfAnswer==totalSelectedAnswers)
			{
				return true;					
			}
			else
			{
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.sumOfAnswer() +sumOfAnswer);
				return false;
			}

		}//image key contain only one answer
		else if(questionProxy.getQuestionType().getQuestionType()==QuestionTypes.Imgkey)
		{
			int totalAnswers=questionViewImpl.getAnswerPanel().getWidgetCount();
			int totalSelectedAnswers=0;
		//	int sumOfAnswer=questionProxy.getQuestionType().getSumAnswer();
			for(int i=0;i<totalAnswers;i++)
			{
				AnswerViewImpl answerViewImpl=(AnswerViewImpl)questionViewImpl.getAnswerPanel().getWidget(i);
				
				if(answerViewImpl.getChecked() )
				{
					totalSelectedAnswers++;
				}
				
			}
			
			if(totalSelectedAnswers==1)
			{
				return true;					
			}
			else
			{
				ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.imageKey());
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void initTopElemnt(int start, int end) {
		
		assementQuestionPanel.getTable().setRowCount(questionTypeCountProxyList.size(),true);
		
		if(start==questionTypeCountProxyList.size()-1)//last
		{
			ArrayList<QuestionTypeCountProxy> temp=new ArrayList<QuestionTypeCountProxy>();
			temp.add(questionTypeCountProxyList.get(start));
		//	assementQuestionPanel.getTable().setRowCount(1,true);

			assementQuestionPanel.getTable().setRowData(start,temp);
			return;

		}
		
		if(end <= questionTypeCountProxyList.size())
		assementQuestionPanel.getTable().setRowData(start,questionTypeCountProxyList.subList(start, end));
	}

}

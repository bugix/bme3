package medizin.client.activites;

import java.util.List;
import java.util.Set;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionSumPerPersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.proxy.StudentToAssesmentProxy;
import medizin.client.request.QuestionSumPerPersonRequest;
import medizin.client.request.QuestionTypeCountPerExamRequest;
import medizin.client.request.StudentToAssesmentRequest;
import medizin.client.ui.view.assesment.AssesmentDetailsView;
import medizin.client.ui.view.assesment.AssesmentDetailsViewImpl;
import medizin.client.ui.view.assesment.QuestionSumPerPersonDialogbox;
import medizin.client.ui.view.assesment.QuestionSumPerPersonDialogboxImpl;
import medizin.client.ui.view.assesment.QuestionSumPerPersonView;
import medizin.client.ui.view.assesment.QuestionTypeCountAddDialogbox;
import medizin.client.ui.view.assesment.QuestionTypeCountAddDialogboxImpl;
import medizin.client.ui.view.assesment.QuestionTypeCountView;
import medizin.client.ui.view.assesment.StudentView;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
/**
 * Activity for Handling AssesementDetailsViews
 * @author masterthesis
 *
 */
public class ActivityAssesmentDetails extends AbstractActivityWrapper implements 
	AssesmentDetailsView.Presenter, AssesmentDetailsView.Delegate, 
	QuestionTypeCountView.Presenter, QuestionTypeCountView.Delegate,
	QuestionTypeCountAddDialogbox.Presenter, QuestionTypeCountAddDialogbox.Delegate,
	QuestionSumPerPersonView.Presenter, QuestionSumPerPersonView.Delegate,
	QuestionSumPerPersonDialogbox.Presenter, QuestionSumPerPersonDialogbox.Delegate,
	StudentView.Presenter, StudentView.Delegate{

	private PlaceAssesmentDetails assesmentPlace;
	private AcceptsOneWidget widget;
	private AssesmentDetailsView view;
	private HandlerRegistration rangeChangeHandler;
	private AssesmentProxy assesment;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private QuestionTypeCountView questionTypeCountView;
	private CellTable<QuestionTypeCountPerExamProxy> questionTypeCountTable;
	private HandlerRegistration rangeQuestionTypeCountChangeHandler;
	private QuestionSumPerPersonView questionSumPerPersonView;
	private CellTable<QuestionSumPerPersonProxy> questionSumPerPersonTable;
	private StudentView studentView;
	private AssesmentDetailsView assesmentDetailsView;
	private int questionTypeCountNextSortOrder;
	private QuestionTypeCountAddDialogbox questionTypeCountAddDialogbox;
	private  RequestFactoryEditorDriver<QuestionTypeCountPerExamProxy,QuestionTypeCountAddDialogboxImpl> driver;
	private QuestionTypeCountPerExamProxy questionTypeCountPerExamProxy;
	private QuestionSumPerPersonDialogbox questionSumPerPersonDialogbox;
	private QuestionSumPerPersonProxy questionSumPerPersonProxy;
	private Integer questionSumPerPersonNextSortOrder;

	
	@Inject
	public ActivityAssesmentDetails(PlaceAssesmentDetails place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.assesmentPlace = place;
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
	
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		assesmentDetailsView = new AssesmentDetailsViewImpl();
		assesmentDetailsView.setName("hallo");
		assesmentDetailsView.setPresenter(this);
		this.widget = widget;
		this.view = assesmentDetailsView;
        widget.setWidget(assesmentDetailsView.asWidget());
		//setTable(view.getTable());
        
		/*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});*/
		//init();
		
		view.setDelegate(this);
		
		requests.find(assesmentPlace.getProxyId()).with("repeFor","mc","institution","questionSumPerPerson").fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				if(response instanceof AssesmentProxy){
					Log.info(((AssesmentProxy) response).getName());
					init((AssesmentProxy) response);
				}
			}
		});

	}

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
    
	private void init(AssesmentProxy assesment) {

		this.assesment = assesment;
		Log.debug("Details für: "+assesment.getName());
		view.setValue(assesment);

		initQuestionTypeCount();
		initQuestionSumPerPerson();
		initStudentView();
	}

	private void initStudentView()
	{
		studentView = assesmentDetailsView.getStudentViewImpl();
		studentView.setDelegate(this);
		studentView.setPresenter(this);
		studentView.getHidden().setValue(assesment.getId().toString());
		
		fireStudentCountRequest();
		
		studentView.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				ActivityAssesmentDetails.this.onStudentRangeChanged();
			}
		});
	}
	
	private void fireStudentCountRequest()
	{
		requests.studentToAssesmentRequest().countStudentToAssesmentByAssesment(assesment.getId()).fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				studentView.getTable().setRowCount(response.intValue(), true);
				
				onStudentRangeChanged();
			}
		});
	}
	
	private void onStudentRangeChanged()
	{
		final Range range = studentView.getTable().getVisibleRange();
		
		requests.studentToAssesmentRequest().findStudentToAssesmentByAssesment(assesment.getId(), range.getStart(), range.getLength()).with("student").fire(new BMEReceiver<List<StudentToAssesmentProxy>>() {

			@Override
			public void onSuccess(List<StudentToAssesmentProxy> response) {
				studentView.getTable().setRowData(range.getStart(), response);
			}
		});
	}

	private void initQuestionTypeCount() {
		questionTypeCountView = assesmentDetailsView.getQuestionTypeCountViewImpl();
		Log.debug("getTable");
		this.questionTypeCountTable = questionTypeCountView.getTable();
		Log.debug("setPresenter");
		questionTypeCountView.setPresenter(this);
		questionTypeCountView.setDelegate(this);
		Log.debug("request");
		requests.questionTypeCountPerExamRequest().countQuestionTypeCountByAssesmentNonRoo(assesment.getId()).fire(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte QuestionTypeCount aus der Datenbank: " + response);
				questionTypeCountNextSortOrder = response.intValue() + 1;
				questionTypeCountTable.setRowCount(response.intValue(), true);
				onQuestionTypeCountChanged();
			}
		});
		
		rangeQuestionTypeCountChangeHandler = questionTypeCountTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityAssesmentDetails.this.onQuestionTypeCountChanged();
			}
		});
	}
	
	private void onQuestionTypeCountChanged() {
		final Range range = questionTypeCountTable.getVisibleRange();
		requests.questionTypeCountPerExamRequest().findQuestionTypeCountByAssesmentNonRoo(assesment.getId(), range.getStart(), range.getLength()).with("questionTypesAssigned").fire(new BMEReceiver<List<QuestionTypeCountPerExamProxy>>() {
		
			@Override
			public void onSuccess(List<QuestionTypeCountPerExamProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				questionTypeCountTable.setRowData(range.getStart(), values);
			}
		});
	}
	 
	private void initQuestionSumPerPerson() {
		questionSumPerPersonView = assesmentDetailsView.getQuestionSumPerPersonViewImpl();
		
		this.questionSumPerPersonTable = questionSumPerPersonView.getTable();
		Log.debug("setPresenter");
		questionSumPerPersonView.setPresenter(this);
		questionSumPerPersonView.setDelegate(this);
		
		requests.questionSumPerPersonRequest().countQuestionSumPerPersonByAssesmentNonRoo(assesment.getId()).fire(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte QuestionSumPerPerson aus der Datenbank: " + response);
				questionSumPerPersonNextSortOrder = response.intValue() +1;
				questionSumPerPersonTable.setRowCount(response.intValue(), true);

				onQuestionSumPerPersonChanged();
			}

			
		});
		

		questionSumPerPersonTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityAssesmentDetails.this.onQuestionSumPerPersonChanged();
			}
		});
	}
    
    private void onQuestionSumPerPersonChanged() {
		final Range range = questionSumPerPersonTable.getVisibleRange();
		requests.questionSumPerPersonRequest().findQuestionSumPerPersonByAssesmentNonRoo(assesment.getId(), range.getStart(), range.getLength()).with("responsiblePerson", "questionEvent").fire(new BMEReceiver<List<QuestionSumPerPersonProxy>>() {
			
			@Override
			public void onSuccess(List<QuestionSumPerPersonProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				questionSumPerPersonTable.setRowData(range.getStart(), values);
			}
		});
	}


	@Override
	public void deleteClicked() {
		requests.assesmentRequest().remove().using(assesment).fire(new BMEReceiver<Void>() {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	placeController.goTo(new PlaceAssesment("PlaceAssesment!DELETED"));
            	
            }
        });
		
	}

	@Override
	public void editClicked() {
		placeController.goTo(new PlaceAssesmentDetails(assesment.stableId(), PlaceAssesmentDetails.Operation.EDIT, assesmentPlace.getHeight()));
	}

	@Override
	public void newClicked(String institutionName) {
		placeController.goTo(new PlaceAssesmentDetails(PlaceAssesmentDetails.Operation.CREATE));
	}
	
	@Override
	public void moveDown(final QuestionTypeCountPerExamProxy questionTypeCount) {
		Log.info("Move down QuestionTypeCountPerExamProxy");
		if(questionTypeCount != null) {
			requests.questionTypeCountPerExamRequest().findQuestionTypeCountByAssesmentAndOrderNonRoo(assesment.getId(), questionTypeCount.getSort_order()-1).fire(new BMEReceiver<QuestionTypeCountPerExamProxy>() {

				@Override
				public void onSuccess(QuestionTypeCountPerExamProxy response) {
					if(response != null) {
						QuestionTypeCountPerExamRequest moveUpRequest = moveUpRequest(response);
						QuestionTypeCountPerExamRequest moveDownRequest = moveDownRequest(questionTypeCount,moveUpRequest);
						moveDownRequest.fire();
					}
				}
			});			
		}
	}
	
	@Override
	public void moveUp(final QuestionTypeCountPerExamProxy questionTypeCount) {
		Log.info("Move up QuestionTypeCountPerExamProxy");
		if(questionTypeCount != null) {
			requests.questionTypeCountPerExamRequest().findQuestionTypeCountByAssesmentAndOrderNonRoo(assesment.getId(), questionTypeCount.getSort_order()+1).fire(new BMEReceiver<QuestionTypeCountPerExamProxy>() {

				@Override
				public void onSuccess(QuestionTypeCountPerExamProxy response) {
					if(response != null) {
						QuestionTypeCountPerExamRequest moveUpRequest = moveUpRequest(questionTypeCount);
						QuestionTypeCountPerExamRequest moveDownRequest = moveDownRequest(response,moveUpRequest);
						moveDownRequest.fire();
					}
				}
			});
		}
	}
	
	private QuestionTypeCountPerExamRequest moveDownRequest(QuestionTypeCountPerExamProxy questionTypeCount, QuestionTypeCountPerExamRequest moveUpRequest){
		QuestionTypeCountPerExamRequest req = moveUpRequest.append(requests.questionTypeCountPerExamRequest());
		QuestionTypeCountPerExamProxy questionTypeCountEditable = req.edit(questionTypeCount);
		questionTypeCountEditable.setSort_order(questionTypeCountEditable.getSort_order()-1);
		req.persist().using(questionTypeCount).to(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				initQuestionTypeCount();
				Log.info("QuestionTypeCountPerExamProxy moved down.");
			}
		});
		return req;
	}
	
	public QuestionTypeCountPerExamRequest moveUpRequest(QuestionTypeCountPerExamProxy questionTypeCount){
		QuestionTypeCountPerExamRequest req = requests.questionTypeCountPerExamRequest();
		QuestionTypeCountPerExamProxy questionTypeCountEditable = req.edit(questionTypeCount);
		questionTypeCountEditable.setSort_order(questionTypeCountEditable.getSort_order()+1);
		req.persist().using(questionTypeCountEditable).to(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				Log.info("QuestionTypeCountPerExamProxy moved up.");
			}
		});
		return req;
	}

	@Override
	public void deleteQuestionTypeCountClicked(QuestionTypeCountPerExamProxy questionTypeCountPerExam) {
		requests.questionTypeCountPerExamRequest().removeAndUpdateOrder(questionTypeCountPerExam).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				
				if(response) {
					initQuestionTypeCount();	
				} else {
					Log.error("Error while trying to remove the QuestionTypeCountPerExamProxy");
				}
			}
		});
	}
	

	@Override
	public void addNewQuestionTypeCountClicked() {
		
		questionTypeCountAddDialogbox = new QuestionTypeCountAddDialogboxImpl();
		questionTypeCountAddDialogbox.display();
		driver = questionTypeCountAddDialogbox.createEditorDriver();
		QuestionTypeCountPerExamRequest request = requests.questionTypeCountPerExamRequest();
		
		questionTypeCountAddDialogbox.setPresenter(this);
		questionTypeCountAddDialogbox.setDelegate(this);
		
		this.questionTypeCountPerExamProxy = request.create(QuestionTypeCountPerExamProxy.class);

	    request.persist().using(questionTypeCountPerExamProxy);
	    driver.edit(questionTypeCountPerExamProxy, request);
	    questionTypeCountPerExamProxy.setAssesment(assesment);
	    questionTypeCountPerExamProxy.setSort_order(questionTypeCountNextSortOrder);
	    driver.flush();

		
		requests.questionTypeRequest().findAllQuestionTypesForInstituteInSession(assesment).fire(new BMEReceiver<List<QuestionTypeProxy>>(){

			@Override
			public void onSuccess(List<QuestionTypeProxy> response) {
				questionTypeCountAddDialogbox.setQuestionTypesAssignedPickerValues(response);
				
			}
			
		});
	}

	@Override
	public void addClicked() {
		driver.flush().fire(new BMEReceiver<Void>() {
			
	          @Override
	          public void onSuccess(Void response) {
	        	  Log.info("fullSaved");
	        	  
	        		initQuestionTypeCount();
	          //	goTo(new PlaceAssesment(person.stableId()));
	          }
	          
	          /*public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
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

					
				}*/
	      }); 
		
	}

	@Override
	public void cancelClicked() {
		
		
	}

	@Override
	public void deleteQuestionSumPerPersonClicked(QuestionSumPerPersonProxy questionSumPerPerson) {
		
		requests.questionSumPerPersonRequest().removeAndUpdateOrder(questionSumPerPerson).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				
				if(response == true) {
					initQuestionSumPerPerson();	
				} else {
					Log.error("Error in deleting QuestionSumPerPersonProxy");
				}
			}
		});
	}

	@Override
	public void addNewQuestionSumPerPersonClicked() {
		questionSumPerPersonDialogbox = new QuestionSumPerPersonDialogboxImpl();
		questionSumPerPersonDialogbox.display();
		//driverQuestSum = questionSumPerPersonDialogbox.createEditorDriver();
		//QuestionSumPerPersonRequest request = requests.questionSumPerPersonRequest();
		
		questionSumPerPersonDialogbox.setPresenter(this);
		questionSumPerPersonDialogbox.setDelegate(this);
		
		//this.questionSumPerPersonProxy = request.create(QuestionSumPerPersonProxy.class);


       // request.persist().using(questionSumPerPersonProxy);
        //driverQuestSum.edit(questionSumPerPersonProxy, request);
        //questionSumPerPersonProxy.setAssesment(assesment);
        //questionSumPerPersonProxy.setSort_order(sort_orderQuestSum);
        //driverQuestSum.flush();

		
		requests.personRequest().findAllPeople().fire(new BMEReceiver<List<PersonProxy>>(){
			

			@Override
			public void onSuccess(List<PersonProxy> response) {
				questionSumPerPersonDialogbox.setResponsiblePersonValues(response);
			}
		});
		
		requests.questionEventRequest().findQuestionEventByInstitution(institutionActive).fire(new BMEReceiver<List<QuestionEventProxy>>(){
			

			@Override
			public void onSuccess(List<QuestionEventProxy> response) {
				questionSumPerPersonDialogbox.setQuestionEventValues(response);
				
			}
		});
		
	}
	
	@Override
	public void addQuestionSumPerPersonClicked(final QuestionSumPerPersonDialogboxImpl questionSumPerPersonDialogboxImpl) {
		
		Set<QuestionSumPerPersonProxy> questionSumPerPersonProxys=assesment.getQuestionSumPerPerson();
		int totalPercent=0;
		for(QuestionSumPerPersonProxy questionSumPerPersonProxy:questionSumPerPersonProxys)
		{
			totalPercent=totalPercent+questionSumPerPersonProxy.getQuestionSum();
		}
		
		if(totalPercent>100)
		{
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.percentNotMoreThan100());
			
			return;
		}
		QuestionSumPerPersonRequest request = requests.questionSumPerPersonRequest();
		this.questionSumPerPersonProxy = request.create(QuestionSumPerPersonProxy.class);
		questionSumPerPersonProxy.setAssesment(assesment);
		questionSumPerPersonProxy.setSort_order(questionSumPerPersonNextSortOrder);
		questionSumPerPersonDialogbox.setValueInProxy(questionSumPerPersonProxy);
		
		//driverQuestSum.flush().fire(new BMEReceiver<Void>() {
		request.persist().using(questionSumPerPersonProxy).fire(new BMEReceiver<Void>() {	
			
	          @Override
	          public void onSuccess(Void response) {
	        	  Log.info("fullSaved");
	        	  questionSumPerPersonDialogboxImpl.hide();
	        		initQuestionSumPerPerson();
	          //	goTo(new PlaceAssesment(person.stableId()));
	          }
	          
	          /*public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
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

					
				}*/
	      }); 
		
	}

	@Override
	public void cancelQuestionSumPerPersonClicked() {
		
		
	}


	@Override
	public void moveQuestionSumPerPersonDown(QuestionSumPerPersonProxy questionSumPerPerson) {
		requests.questionSumPerPersonRequest().moveDown().using(questionSumPerPerson).fire(new BMEReceiver<Void>() {
			
	          @Override
	          public void onSuccess(Void response) {
	        	  Log.info("movedDownp");
	        	  
	        		initQuestionSumPerPerson();
	          //	goTo(new PlaceAssesment(person.stableId()));
	          }
		});
	}

	@Override
	public void moveQuestionSumPerPersonUp(QuestionSumPerPersonProxy questionSumPerPerson) {
		requests.questionSumPerPersonRequest().moveUp().using(questionSumPerPerson).fire(new BMEReceiver<Void>() {
			
	          @Override
	          public void onSuccess(Void response) {
	        	  Log.info("movedUp");
	        	  
	        		initQuestionSumPerPerson();
	          //	goTo(new PlaceAssesment(person.stableId()));
	          }
		});
	}

	@Override
	public void placeChanged(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importClicked() {
		fireStudentCountRequest();
	}

	@Override
	public void deactivateClicked(StudentToAssesmentProxy studentToAssesmentProxy) {
		
		Boolean flag = studentToAssesmentProxy.getIsEnrolled();
		
		StudentToAssesmentRequest studentToAssesmentRequest = requests.studentToAssesmentRequest();
		studentToAssesmentProxy = studentToAssesmentRequest.edit(studentToAssesmentProxy);
		
		studentToAssesmentProxy.setIsEnrolled(!flag);
		
		studentToAssesmentRequest.persist().using(studentToAssesmentProxy).fire(new BMEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				fireStudentCountRequest();
			}
		});
	}	
}

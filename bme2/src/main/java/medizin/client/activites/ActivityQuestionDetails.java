package medizin.client.activites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.AnswerDialogbox;
import medizin.client.ui.view.question.AnswerDialogboxImpl;
import medizin.client.ui.view.question.AnswerListView;
import medizin.client.ui.view.question.AnswerListViewImpl;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionDetailsViewImpl;
import medizin.client.ui.view.question.QuestionDetailsView;
import medizin.client.ui.view.question.QuestionView;
import medizin.client.ui.view.user.EventAccessDialogbox;
import medizin.client.place.PlaceAssesment;
import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AnswerProxy;
import medizin.client.request.AnswerRequest;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.request.QuestionTypeCountPerExamRequest;
import medizin.client.shared.Validity;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;



public class ActivityQuestionDetails extends AbstractActivityWrapper implements 
	QuestionDetailsView.Delegate, QuestionDetailsView.Presenter, AnswerDialogbox.Delegate, 
	 AnswerListView.Delegate{

	private AcceptsOneWidget widget;
	//private QuestionDetailsView view;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private PlaceQuestionDetails questionPlace;
	
	private QuestionProxy question;
	private QuestionDetailsView questionDetailsView;
	private QuestionDetailsView view;
	private AnswerListViewImpl answerListView;
	private CellTable<AnswerProxy> answerTable;
	
	public ActivityQuestionDetails(PlaceQuestionDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;
        this.requests = requests;
        this.placeController = placeController;
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}
	
	private PersonProxy loggedUser;
	private HandlerRegistration answerRangeChangeHandler;
	
	
	@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}
	@Override
	public void start2(AcceptsOneWidget panel, EventBus eventBus) {
		
		questionDetailsView = new QuestionDetailsViewImpl();
		questionDetailsView.setName("hallo");
		questionDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = questionDetailsView;
        widget.setWidget(questionDetailsView.asWidget());
		//setTable(view.getTable());
        
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		//init();
		
		view.setDelegate(this);
		
		this.answerListView = view.getAnswerListViewImpl();
		answerListView.setDelegate(this);
		this.answerTable = answerListView.getTable();
		
		requests.personRequest().myGetLoggedPerson()
				.fire(new Receiver<PersonProxy>() {

					@Override
					public void onSuccess(PersonProxy response) {
						loggedUser = response;
						start2();

					}

					public void onFailure(ServerFailure error) {
						ErrorPanel erorPanel = new ErrorPanel();
						erorPanel.setErrorMessage(error.getMessage());
						Log.error(error.getMessage());
						onStop();
					}

					@Override
					public void onViolation(Set<Violation> errors) {
						Iterator<Violation> iter = errors.iterator();
						String message = "";
						while (iter.hasNext()) {
							message += iter.next().getMessage() + "<br>";
						}
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
								+ " in Antwort löschen -" + message);

						ErrorPanel erorPanel = new ErrorPanel();
						erorPanel.setErrorMessage(message);
						onStop();

					}

				});

	}
	private void start2(){
		if(loggedUser==null) return;
		
		requests.find(questionPlace.getProxyId()).with("previousVersion","keywords","questEvent","comment","questionType","mcs", "rewiewer", "autor").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof QuestionProxy){
					Log.info(((QuestionProxy) response).getQuestionText());
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
		
		initAnswerView();
	}

	private void initAnswerView() {
		
		if (answerRangeChangeHandler!=null){
			answerRangeChangeHandler.removeHandler();
			answerRangeChangeHandler=null;
		}
		
		requests.answerRequest().contAnswersByQuestion(question.getId()).fire( new Receiver<Long>(){


				@Override
				public void onSuccess(Long response) {
					
					answerTable.setRowCount(response.intValue(), true);

					onAnswerTableRangeChanged();
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
						Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort hinzufügen -" + message);
						
			        	  ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setErrorMessage(message);
						

						
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
		
		
		requests.answerRequest().findAnswersEntriesByQuestion(question.getId(), range.getStart(), range.getLength()).fire( new Receiver<List<AnswerProxy>>(){


			@Override
			public void onSuccess(List<AnswerProxy> response) {
				
				answerTable.setRowData(range.getStart(), response);

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
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort hinzufügen -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);
					
					
				}
	      
	});
		
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void deleteClicked() {
		requests.questionRequest().remove().using(question).fire(new Receiver<Void>() {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	placeController.goTo(new PlaceQuestion("PlaceQuestion!DELETED"));
            	
            }
            @Override
			public void onFailure(ServerFailure error) {
					Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Assesment -" + error.getMessage());
					if(error.getMessage().contains("ConstraintViolationException")){
						Log.debug("Fehlen beim erstellen: Doppelter name");
						//TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.EVENT_IS_REFERENCED);
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
				
				//TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

				
			}
            
        });
		
	}

	@Override
	public void editClicked() {
		placeController.goTo(new PlaceQuestionDetails(question.stableId(), PlaceQuestionDetails.Operation.EDIT));

		
	}

	@Override
	public void newClicked() {
		placeController.goTo(new PlaceQuestionDetails(PlaceQuestionDetails.Operation.CREATE));
		
	}
	
	private AnswerDialogbox answerDialogbox;
	private RequestFactoryEditorDriver<AnswerProxy, AnswerDialogboxImpl> answerDriver;
	private AnswerProxy answerProxy;

	@Override
	public void addNewAnswerClicked() {
		
		answerDialogbox = new AnswerDialogboxImpl();
		answerDialogbox.setDelegate(this);
		answerDriver = answerDialogbox.createEditorDriver();
		
		AnswerRequest ansRequest = requests.answerRequest();
		
			AnswerProxy ansProxy=ansRequest.create(AnswerProxy.class);
			
			//this.answerProxy = request.create(AnswerProxy.class);
			this.answerProxy = ansProxy;
			
			
			

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
		
		answerDialogbox.setValidityPickerValues(Arrays.asList(Validity.values()));
		answerDialogbox.setRewiewerPickerValues(Collections.<PersonProxy>emptyList());
	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new Receiver<List<PersonProxy>>() {

	            public void onSuccess(List<PersonProxy> response) {
	                List<PersonProxy> values = new ArrayList<PersonProxy>();
	                values.add(null);
	                values.addAll(response);
	                answerDialogbox.setRewiewerPickerValues(values);
	            }
	        });
//	        answerDialogbox.setAutorPickerValues(Collections.<PersonProxy>emptyList());
//	        requests.personRequest().findPersonEntries(0, 50).with(medizin.client.ui.view.roo.PersonProxyRenderer.instance().getPaths()).fire(new Receiver<List<PersonProxy>>() {
//
//	            public void onSuccess(List<PersonProxy> response) {
//	                List<PersonProxy> values = new ArrayList<PersonProxy>();
//	                values.add(null);
//	                values.addAll(response);
//	                answerDialogbox.setAutorPickerValues(values);
//	            }
//	        });
	        
	        answerDialogbox.display();

		
	}
	
	@Override
	public void addAnswerClicked() {
		answerProxy.setAnswerText(answerDialogbox.getRichtTextHTML());
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
	}
	

	@Override
	public void cancelAnswerClicked() {
		
		
	}

	@Override
	public void deleteAnswerClicked(AnswerProxy answer) {
		
		

		requests.answerRequest().eliminateAnswer().using(answer).fire(new Receiver<Void>(){

			@Override
			public void onSuccess(Void arg0) {
				initAnswerView();
				
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
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Antwort löschen -" + message);
					
		        	  ErrorPanel erorPanel = new ErrorPanel();
		        	  erorPanel.setErrorMessage(message);
					

					
				}
		});
		
	}

}

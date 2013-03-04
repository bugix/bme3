package medizin.client.activites;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.request.QuestionRequest;
import medizin.client.ui.DeclineEmailPopupDelagate;
import medizin.client.ui.ErrorPanel;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.AcceptQuestionView;
import medizin.client.ui.view.AcceptQuestionViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.Violation;

public class ActivityAcceptQuestion extends AbstractActivityWrapper implements AcceptQuestionView.Presenter, DeclineEmailPopupDelagate {

	private PlaceAcceptQuestion questionPlace;

	private AcceptsOneWidget widget;
	private AcceptQuestionView view;


	private McAppRequestFactory requests;
	private PlaceController placeController;

	//protected PersonProxy loggedUser;

	private SingleSelectionModel<QuestionProxy> selectionModel;
	private ActivityManager activityManger;
	private ActivityQuestionMapper activityQuestionMapper;
	
	
	@Inject
	public ActivityAcceptQuestion(PlaceAcceptQuestion place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;	
		this.requests = requests;
        this.placeController = placeController;
        
        this.activityQuestionMapper = new ActivityQuestionMapper(requests,
				placeController);
		this.activityManger = new ActivityManager(activityQuestionMapper,
				requests.getEventBus());
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
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		AcceptQuestionView acceptQuestionView = new AcceptQuestionViewImpl();
		
		acceptQuestionView.setPresenter(this);
		acceptQuestionView.setDelegate(this);
		this.widget = widget;
		this.view = acceptQuestionView;
        widget.setWidget(acceptQuestionView.asWidget());
        
        table=view.getTable();
        
        eventBus.addHandler(PlaceChangeEvent.TYPE,
				new PlaceChangeEvent.Handler() {
					public void onPlaceChange(PlaceChangeEvent event) {

						Place place = event.getNewPlace();
						if (place instanceof PlaceQuestionDetails) {
							init();
						}
					}
				});

		activityManger.setDisplay(view.getDetailsPanel());
        
        ProvidesKey<QuestionProxy> keyProvider = ((AbstractHasData<QuestionProxy>) table).getKeyProvider();
        selectionModel = new SingleSelectionModel<QuestionProxy>(keyProvider);
        table.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
        	
			public void onSelectionChange(SelectionChangeEvent event) {
			
				QuestionProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getQuestionText() + " selected!");
					showDetails(selectedObject);
				}
			}
		});

        init();
        
    	/*requests.personRequest().myGetLoggedPerson()
		.fire(new Receiver<PersonProxy>() {

			@Override
			public void onSuccess(PersonProxy response) {
				loggedUser = response;
				init();

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

		});*/


	}

	protected void showDetails(QuestionProxy question) {
		Log.debug("Question Stable id: " + question.stableId() + " " + PlaceQuestionDetails.Operation.DETAILS);
		placeController.goTo(new PlaceQuestionDetails(question.stableId(), "ACCEPT_QUESTION"));
	}
	

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	
	AbstractHasData<QuestionProxy> table;

	private HandlerRegistration rangeChangeHandler;

	private void init() {
		
		if (rangeChangeHandler!=null){
			rangeChangeHandler.removeHandler();
			rangeChangeHandler=null;
		}
		
		requests.questionRequest().countQuestionsNonAcceptedAdmin().fire(new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Questions (Pr�fungen) aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);
				onRangeChanged();
			}
		});
		
		
		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityAcceptQuestion.this.onRangeChanged();
			}
		});
	}   

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		requests.questionRequest().findQuestionsEntriesNonAcceptedAdmin(range.getStart(), range.getLength()).with(view.getPaths()).fire(new Receiver<List<QuestionProxy>>() {
			@Override
			public void onSuccess(List<QuestionProxy> values) {
				if (view == null) {
					Log.debug("view ist null");
					return;
				}

				table.setRowData(range.getStart(), values);
				

			if (widget != null) {
		          widget.setWidget(view.asWidget());
				}
			}
		});

		
		
	}


	@Override
	public void acceptClicked(EntityProxy entityProxy) {
		Log.debug("acceptClicked" + entityProxy.toString());
		if(entityProxy instanceof QuestionProxy){
			Log.debug("is QUestionProxy");
			QuestionRequest req = requests.questionRequest();
			QuestionProxy questionProxy =  req.edit((QuestionProxy)entityProxy);
			/*if(loggedUser.getIsAdmin()){*/
			if(userLoggedIn.getIsAdmin()){
				questionProxy.setIsAcceptedAdmin(true);
			} else {
				questionProxy.setIsAcceptedRewiever(true);
			}
			
			questionProxy.setIsActive(true);
			req.persistAndSetPreviousInactive().using(questionProxy).fire(new Receiver<Void>(){

				@Override
				public void onSuccess(Void arg0) {
					init();
					
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

	@Override
	public void rejectClicked(EntityProxy entityProxy, String message) {
		if(entityProxy instanceof QuestionProxy){
			Log.debug("is QUestionProxy");
			QuestionRequest req = requests.questionRequest();
			QuestionProxy questionProxy =  (QuestionProxy)entityProxy;
			
			req.remove().using(questionProxy).fire(new Receiver<Void>(){

				@Override
				public void onSuccess(Void arg0) {
					init();
					
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

}

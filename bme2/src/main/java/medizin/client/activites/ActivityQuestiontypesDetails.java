package medizin.client.activites;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.ui.ErrorPanel;
import medizin.client.ui.SlidingPanel;
import medizin.client.ui.McAppConstant;

import medizin.client.ui.view.EventView;
import medizin.client.ui.view.EventViewImpl;
import medizin.client.ui.view.QuestiontypesDetailsView;
import medizin.client.ui.view.QuestiontypesDetailsViewImpl;

import medizin.client.place.PlaceAssesmentDetails;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.place.PlaceQuestiontypesDetails.Operation;
import medizin.client.factory.request.McAppRequestFactory;

import medizin.client.proxy.QuestionTypeProxy;

import medizin.client.request.QuestionEventRequest;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
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
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class ActivityQuestiontypesDetails extends AbstractActivityWrapper implements QuestiontypesDetailsView.Presenter, QuestiontypesDetailsView.Delegate {

	private PlaceQuestiontypesDetails questiontypePlace;

	private AcceptsOneWidget widget;
	private QuestiontypesDetailsView view;
	private HandlerRegistration rangeChangeHandler;
	
	private QuestionTypeProxy questionType;

	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityQuestiontypesDetails(PlaceQuestiontypesDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questiontypePlace = place;
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
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		QuestiontypesDetailsView questionTypeDetailsView = new QuestiontypesDetailsViewImpl();
		questionTypeDetailsView.setName("hallo");
		questionTypeDetailsView.setPresenter(this);
		this.widget = widget;
		this.view = questionTypeDetailsView;
        widget.setWidget(questionTypeDetailsView.asWidget());
		//setTable(view.getTable());
        
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});
		//init();
		
		view.setDelegate(this);
		
		requests.find(questiontypePlace.getProxyId()).with("institution").fire(new Receiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				if(response instanceof QuestionTypeProxy){
					//Log.info(((QuestionTypeProxy) response).getQuestionTypeName());
					init((QuestionTypeProxy) response);
				}

				
			}
			
	          public void onFailure(ServerFailure error){
	        	  ErrorPanel errorPanel = new ErrorPanel();
	        	  errorPanel.setErrorMessage(error.getMessage());
					Log.error(error.getMessage());
				}
	          @Override
				public void onViolation(Set<Violation> errors) {
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while(iter.hasNext()){
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Frage suchen -" + message);
					
		        	  ErrorPanel errorPanel = new ErrorPanel();
		        	  errorPanel.setWarnMessage(message);
					

					
				}
		    });


		//Log.warn(requests.getProxyId(eventPlace.getProxyId().toString()));
		
		// Inherit the view's key provider
		
		//view.setDelegate(this);
		//updateSelection(requests.getWhere());

	}
	
//	protected void showDetails(QuestionEventProxy questionEvent) {
//	//	Log.debug("QuestionEvent Stable id: " + questionEvent.stableId() + " " +  PlaceInstitutionEvent.Operation.DETAILS); 
//	//	mcAppFactory.getPlaceController().goTo(new PlaceInstitutionEvent(questionEvent.stableId(), PlaceInstitutionEvent.Operation.DETAILS));
//	}

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	
//    protected Request<java.util.List<medizin.client.managed.request.QuestionEventProxy>> createRangeRequest(Range range) {
//        return requests.questionEventRequest().findQuestionEventsByInstitutionNonRoo(institution.getId(), range.getStart(), range.getLength());
//    }
//
//    protected void fireCountRequest(Receiver<java.lang.Long> callback) {
//    	requests.questionEventRequest().countQuestionEventsNonRoo(institution.getId()).fire(callback);
//    }
    
	private void init(QuestionTypeProxy questionType) {
		this.questionType = questionType;
		view.setValue(questionType);
	}
	
	@Override
	public void deleteClicked() {
		
		requests.questionTypeRequest().remove().using(questionType).fire(new Receiver<Void>() {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	placeController.goTo(new PlaceQuestiontypes("PlaceQuestiontypes!DELETED"));
            	
            }
            
            @Override
            public void onFailure(ServerFailure error){
          	  ErrorPanel errorPanel = new ErrorPanel();
          	  errorPanel.setErrorMessage(error.getMessage());
  				Log.error(error.getMessage());
  			}
            @Override
  			public void onViolation(Set<Violation> errors) {
  				Iterator<Violation> iter = errors.iterator();
  				String message = "";
  				while(iter.hasNext()){
  					message += iter.next().getMessage() + "<br>";
  				}
  				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION + " in Fragetyp löschen -" + message);
  				
  	        	  ErrorPanel errorPanel = new ErrorPanel();
  	        	  errorPanel.setWarnMessage(message);
  				

  				
  			}
            
        });
		
	}

	@Override
	public void editClicked() {
		placeController.goTo(new PlaceQuestiontypesDetails(questionType.stableId(), PlaceQuestiontypesDetails.Operation.EDIT));
		
	}

	@Override
	public void newClicked() {
		placeController.goTo(new PlaceQuestiontypesDetails(PlaceQuestiontypesDetails.Operation.CREATE));
		
	}



	@Override
	public void newClicked(String institutionName) {
		// TODO Auto-generated method stub
		
	}








}

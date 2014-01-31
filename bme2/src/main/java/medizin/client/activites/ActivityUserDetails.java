package medizin.client.activites;

import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceUser;
import medizin.client.place.PlaceUserDetails;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.UserAccessRightsProxy;
import medizin.client.request.UserAccessRightsRequest;
import medizin.client.ui.view.user.EventAccessDialogbox;
import medizin.client.ui.view.user.EventAccessDialogboxImpl;
import medizin.client.ui.view.user.EventAccessView;
import medizin.client.ui.view.user.InstituteAccessDialogBox;
import medizin.client.ui.view.user.InstituteAccessDialogBoxImpl;
import medizin.client.ui.view.user.InstituteAccessView;
import medizin.client.ui.view.user.QuestionAccessDialogbox;
import medizin.client.ui.view.user.QuestionAccessDialogboxImpl;
import medizin.client.ui.view.user.QuestionAccessView;
import medizin.client.ui.view.user.UserDetailsView;
import medizin.client.ui.view.user.UserDetailsViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.process.AppLoader;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.AccessRights;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Request;
/**
 * Activity for Handling UserDetailsViews.
 * @author masterthesis
 *
 */
public class ActivityUserDetails extends AbstractActivityWrapper implements UserDetailsView.Presenter, UserDetailsView.Delegate,
		EventAccessView.Presenter, EventAccessView.Delegate, QuestionAccessView.Presenter, QuestionAccessView.Delegate,
		 EventAccessDialogbox.Presenter, EventAccessDialogbox.Delegate,  QuestionAccessDialogbox.Presenter, QuestionAccessDialogbox.Delegate,
		 InstituteAccessView.Delegate, InstituteAccessView.Presenter, InstituteAccessDialogBox.Delegate, InstituteAccessDialogBox.Presenter{

	private PlaceUserDetails userPlace;

	private AcceptsOneWidget widget;
	private UserDetailsView view;

	private HandlerRegistration rangeChangeHandler;
	
	private PersonProxy person;
	

	private McAppRequestFactory requests;
	private PlaceController placeController;

//	private ActivityUserDetailsMapper activityUserDetailsMapper;

	private ActivityManager activityManger;

	@Inject
	public ActivityUserDetails(PlaceUserDetails place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.userPlace = place;
        this.requests = requests;
        this.placeController = placeController;
        
//		this.activityUserDetailsMapper = new ActivityUserDetailsMapper(requests, placeController);
//		this.activityManger = new ActivityManager(activityUserDetailsMapper,
//				requests.getEventBus());
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
	
	private UserDetailsView userDetailsView;

	private InstituteAccessView instituteAccessView;


	/**
	 * Function is called from GWT-ActivityManager
	 * the View for user Details will be created an tables are filled with data
	 * calls @see medizin.client.a_nonroo.app.activities.ActivityUserDetails#init(PersonProxy) , 
	 * Overriden funtion from @see com.google.gwt.activity.shared.Activity
	 */
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		userDetailsView = new UserDetailsViewImpl();
		userDetailsView.setName("hallo");
		userDetailsView.setPresenter(this);
		this.widget = widget;
		this.view = userDetailsView;
        widget.setWidget(userDetailsView.asWidget());
		//setTable(view.getTable());
        
		/*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});*/
		//init();
        AppLoader.setCurrentLoader(userDetailsView.getLoadingPopup());
        
		view.setDelegate(this);
		
		requests.find(userPlace.getProxyId()).fire(new BMEReceiver<Object>() {

			/*public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}*/
			@Override
			public void onSuccess(Object response) {
				if(response instanceof PersonProxy){
					Log.info(((PersonProxy) response).getEmail());
					
					person = (PersonProxy) response;
					requests.personRequest().findPerson(person.getId()).with("doctor").fire(new BMEReceiver<PersonProxy>() {

						@Override
						public void onSuccess(PersonProxy responseProxy) {
							init(responseProxy);
						}
					});
					
				}

				
			}
		    });

	}


	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
    
	/**
	 * 
	 * @param PersonProxy person
	 */
	private void init(PersonProxy person) {

		this.person = person;
		Log.debug("Details für: "+person.getEmail());
		view.setValue(person);

		initEventAccess();
		initQuestionAccess();
		
		/*requests.personRequest().myGetLoggedPerson().fire(new Receiver<PersonProxy>() {

			@Override
			public void onSuccess(PersonProxy response) {
			
				if(response!=null && response.getIsAdmin())
				{	
					initInstituteAccess();
				}
				else{
					view.getUserAccessDetailPanel().selectTab(1);
					view.getUserAccessDetailPanel().remove(0);
					
				}
			}
		});*/
		initInstituteAccess();
	}
	
	private EventAccessView eventAccessView;

	private SingleSelectionModel<QuestionEventProxy> selectionModelEventAccess;

	private HandlerRegistration rangeEventAccessChangeHandler;
	
	
	private void initEventAccess() {
		this.eventAccessView = userDetailsView.getEventAccessView();
		this.questionEventTable = eventAccessView.getTable();
		eventAccessView.setPresenter(this);
		eventAccessView.setDelegate(this);
		
		if(rangeEventAccessChangeHandler!=null){
			rangeEventAccessChangeHandler.removeHandler();
			rangeEventAccessChangeHandler = null;
		}

		fireEventAccessCountRequest(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Events aus der Datenbank: " + response);
				questionEventTable.setRowCount(response.intValue(), true);

				onRangeEventAccessChanged();
			}
		});
		
		
		
		rangeEventAccessChangeHandler = questionEventTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityUserDetails.this.onRangeEventAccessChanged();
			}
		});
	}
	
	protected void onRangeEventAccessChanged() {
		
		Log.debug("Im QuestionEvent.onRangeEventAccessChanged");
		final Range range = questionEventTable.getVisibleRange();
		AppLoader.setNoLoader();
		final BMEReceiver<List<UserAccessRightsProxy>> callback = new BMEReceiver<List<UserAccessRightsProxy>>() {
			@Override
			public void onSuccess(List<UserAccessRightsProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					QuestionEventProxy questionEvent = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<QuestionEventProxy> proxyId = (EntityProxyId<QuestionEventProxy>) questionEvent.stableId();
//					idToRow.put(proxyId, row);
//					idToProxy.put(proxyId, questionEvent);
//				}
//				Log.debug("Im ActivityInsitutionEvent.onRangeChanged-before Table");
				questionEventTable.setRowData(range.getStart(), values);
//				Log.debug("Im ActivityInsitutionEvent.onRangeChanged-after Table");
				
//				finishPendingSelection();
//			if (widget != null) {
//		          widget.setWidget(view.asWidget());
//				}
			}
			 /* @Override
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

					
				}*/
		};

		fireEventAccessRangeRequest(range, callback, null, null);
		
	}
	
	private void fireEventAccessRangeRequest(final Range range,
            final BMEReceiver<List<UserAccessRightsProxy>> callback, String proxyId, String eventName) {
			if(proxyId!=null){
				
				requests.find(requests.getProxyId(proxyId)).fire(new BMEReceiver<Object>() {

				/*	public void onFailure(ServerFailure error){
						Log.error(error.getMessage());
					}*/
					@Override
					public void onSuccess(Object response) {
						if(response instanceof InstitutionProxy){
							Log.info(((InstitutionProxy) response).getInstitutionName());
					 	}						
					}
				 });
				
			}
			else{
				createEventAccessRangeRequest(range).fire(callback);
			}
			
}
    protected Request<java.util.List<medizin.client.proxy.UserAccessRightsProxy>> createEventAccessRangeRequest(Range range) {
        return requests.userAccessRightsRequest().findQuestionEventAccessByPersonNonRooNonRoo(person.getId(), range.getStart(), range.getLength()).with("questionEvent");
    }

    protected void fireEventAccessCountRequest(BMEReceiver<java.lang.Long> callback) {
    	requests.userAccessRightsRequest().countQuestionEventAccessByPersonNonRoo(person.getId()).fire(callback);
    }

	private QuestionAccessView questionAccessView;

	private SingleSelectionModel<QuestionProxy> selectionModelQuestionAccess;

	private HandlerRegistration rangeQuestionAccessChangeHandler;

	private CellTable<UserAccessRightsProxy> questionTable;
	
	private void initQuestionAccess() {
		this.questionAccessView = userDetailsView.getQuestionAccessView();
		this.questionTable = questionAccessView.getTable();
		questionAccessView.setPresenter(this);
		questionAccessView.setDelegate(this);
		
		if(rangeQuestionAccessChangeHandler!=null){
			rangeQuestionAccessChangeHandler.removeHandler();
			rangeQuestionAccessChangeHandler = null;
		}

		fireQuestionAccessCountRequest(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Questions aus der Datenbank: " + response);
				questionTable.setRowCount(response.intValue(), true);

				onRangeQuestionAccessChanged();
			}
		});
		
		
		
		rangeQuestionAccessChangeHandler = questionTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityUserDetails.this.onRangeQuestionAccessChanged();
			}
		});
		
	}
	
	///////
	protected void onRangeQuestionAccessChanged() {
		
		final Range range = questionTable.getVisibleRange();		
		AppLoader.setNoLoader();
		final BMEReceiver<List<UserAccessRightsProxy>> callback = new BMEReceiver<List<UserAccessRightsProxy>>() {
			@Override
			public void onSuccess(List<UserAccessRightsProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				questionTable.setRowData(range.getStart(), values);

			}
			/* @Override
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

					
				}*/
		};

		fireQuestionAccessRangeRequest(range, callback);
		
	}
	
	private void fireQuestionAccessRangeRequest(final Range range,
            final BMEReceiver<List<UserAccessRightsProxy>> callback) {
			createQuestionAccessRangeRequest(range).fire(callback);
}
    protected Request<java.util.List<medizin.client.proxy.UserAccessRightsProxy>> createQuestionAccessRangeRequest(Range range) {
        return requests.userAccessRightsRequest().findQuestionAccessQuestionByPersonNonRoo(person.getId(), range.getStart(), range.getLength()).with("question");
    }

    protected void fireQuestionAccessCountRequest(BMEReceiver<java.lang.Long> callback) {
    	requests.userAccessRightsRequest().countQuestionAccessQuestionByPersonNonRoo(person.getId()).fire(callback);
    }
    //////

	/**
	 * CellTable for EntitiesProxis of Type @see medizin.client.a_nonroo.app.request.QuestionAccessProxy
	 * This table is designed for showing acceses to questions oven question events
	 */
	private CellTable<UserAccessRightsProxy> questionEventTable;







	/**
	 * Action when Button delete in @see medizin.client.ui.view.user.UserDetailsViewImpl was clicked
	 * The selected person will be deleted, if isn't refenced by an other entity
	 * @see medizin.client.ui.view.user.UserDetailsView.Delegate#deleteClicked()
	 */
	@Override
	public void deleteClicked() {
		AppLoader.setNoLoader();
		requests.personRequest().remove().using(person).fire(new BMEReceiver<Void>() {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	placeController.goTo(new PlaceUser(PlaceUser.PLACE_USER,userPlace.getHeight()));
            	
            }
            /* @Override
			public void onFailure(ServerFailure error) {
					Log.warn(McAppConstant.ERROR_WHILE_DELETE + " in Institution:Event -" + error.getMessage());
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

				
			}*/
            
        });
		
	}

	/**
	 * This method is fired when the user click the edit button in @see medizin.client.ui.view.user.UserDetailsViewImpl
	 * The Person is edite in a new place @see medizin.client.ui.view.user.UserEditViewImpl
	 * 
	 * Functionality implemented from @see medizin.client.ui.view.user.UserDetailsView.Delegate#editClicked()
	 */
	@Override
	public void editClicked() {		
		placeController.goTo(new PlaceUserDetails(person.stableId(), PlaceUserDetails.Operation.EDIT, userPlace.getHeight()));
	}


	@Override
	public void deleteEventAccessClicked(UserAccessRightsProxy questionAccess) {		
		AppLoader.setCurrentLoader(eventAccessView.getLoadingPopup());
		requests.userAccessRightsRequest().remove()
		.using(questionAccess).fire(new BMEReceiver<Void>() {

			public void onSuccess(Void ignore) {
				Log.debug("Sucessfull deleted");
				initEventAccess();
			}

			/*@Override
			public void onFailure(ServerFailure error) {
				Log.warn(McAppConstant.ERROR_WHILE_DELETE
						+ " in Institution -" + error.getMessage());
				if (error.getMessage().contains(
						"ConstraintViolationException")) {
					Log.debug("Fehlen beim erstellen: Doppelter name");
					//TODO mcAppFactory.getErrorPanel().setErrorMessage(cAppConstant.INSTITUTION_IS_REFERENCED);
				}

			}

			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
						+ " in Institution -" + message);
				
				//TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

			}
*/
		});
		
	}

	EventAccessDialogbox dialogBoxEvent;
	QuestionAccessDialogbox dialogBoxQuestion;
	
	InstituteAccessDialogBox dialogBoxInstitute;
	
	//ListBox institutionListbox;

	private CellTable<QuestionEventProxy> eventAccessTable;

	/**
	 * Range change Handler for the Question Accesses
	 */
	private HandlerRegistration rangeQuestionAccessAllChangeHandler;

	private QuestionEventProxy eventFilter;
	
	
	/* (non-Javadoc)
	 * @see medizin.client.ui.view.user.EventAccessView.Delegate#addNewEventAccessClicked()
	 */
	@Override
	public void addNewEventAccessClicked() {

		institutionFilter=null;
		eventNameFilter="";
		dialogBoxEvent = new EventAccessDialogboxImpl();
		dialogBoxEvent.display();
		Log.info("addNewEventClicked");
		
		this.eventAccessTable = dialogBoxEvent.getTable();
		dialogBoxEvent.setPresenter(this);
		dialogBoxEvent.setDelegate(this);
	
		//final EventAccessDialogbox tempDialogBoxEvent = dialogBoxEvent;
		//institutionListbox = dialogBoxEvent.getSearchInstitution();
		
		// Fill the Institution Box
		final BMEReceiver<List<InstitutionProxy>> callback2 = new BMEReceiver<List<InstitutionProxy>>() {
			@Override
			public void onSuccess(List<InstitutionProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				
				DefaultSuggestOracle<InstitutionProxy> suggestOracle1 = (DefaultSuggestOracle<InstitutionProxy>) dialogBoxEvent.getSearchInstitution().getSuggestOracle();
				suggestOracle1.setPossiblilities(values);
				
				dialogBoxEvent.getSearchInstitution().setSuggestOracle(suggestOracle1);
				dialogBoxEvent.getSearchInstitution().setRenderer(new AbstractRenderer<InstitutionProxy>() {

					@Override
					public String render(InstitutionProxy object) {
						// TODO Auto-generated method stub
						if(object!=null)
						{
							return object.getInstitutionName();
						}
						else
						{
							return "";
						}
					}
				});
				
				/*Iterator<InstitutionProxy> iter = values.iterator();
				institutionListbox.addItem("", "");
				while(iter.hasNext()){
					InstitutionProxy institution = iter.next();
					institutionListbox.addItem(institution.getInstitutionName(), institution.stableId().toString());
				}*/
				
				//eventAccessTable.setRowData( values);

			}
			/* @Override
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

					
				}*/
		};

		fireRequestAllInstitution( callback2);

		initEventAccessDialogbox();
		rangeQuestionAccessAllChangeHandler = eventAccessTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityUserDetails.this.onRangeEventAccessByInstitutionOrEventnameChanged();
			}
		});
	
	}
	
	private void initEventAccessDialogbox() {
		if (person != null)
		{
			AppLoader.setNoLoader();
			requests.userAccessRightsRequest().findQuestionEventAccessByPerson(person.getId()).with("questionEvent").fire(new BMEReceiver<List<UserAccessRightsProxy>>() {

				@Override
				public void onSuccess(List<UserAccessRightsProxy> response) {
					dialogBoxEvent.setUserAccessRightsList(response);
					
					fireQuestionAccessCountByInstitutionOrEventnameRequest(new BMEReceiver<Long>() {
						@Override
						public void onSuccess(Long response) {
							if (view == null) {
								// This activity is dead
								return;
							}
							Log.debug("Geholte Events aus der Datenbank: " + response);
							eventAccessTable.setRowCount(response.intValue(), true);
							
							onRangeEventAccessByInstitutionOrEventnameChanged();
						}
					});
				}
			});
		}				
	}
	private Boolean filterQuestionText = true;
	private Boolean filterKeywords = false;
	
    protected void fireQuestionAccessCountByInstitutionOrEventOrQuestionNameOrKeywordRequest(BMEReceiver<java.lang.Long> callback) {
    	
    	requests.questionRequest().countQuestionByInstitutionOrEventOrQuestionNameOrKeyword(
    			institutionFilter==null? null:institutionFilter.getId(), eventFilter==null? null:eventFilter.getId(), 
    					questiuonStringFilter, filterQuestionText, filterKeywords).fire(callback);
    }
    
    protected void  fireQuestionAccessByInstitutionOrEventOrQuestionNameOrKeywordRequest(Range range, BMEReceiver<List<QuestionProxy>> callback){
    	requests.questionRequest().findQuestionByInstitutionOrEventOrQuestionNameOrKeyword(
    			institutionFilter==null? null:institutionFilter.getId(), eventFilter==null? null:eventFilter.getId(), 
    					questiuonStringFilter, filterQuestionText, filterKeywords, range.getStart(), range.getLength()).fire(callback);
    }
	
	private void initQuestionAccessDialogbox() {
		if (person != null)
		{
			AppLoader.setNoLoader();
			requests.userAccessRightsRequest().findQuestionAccessByPerson(person.getId()).with("question").fire(new BMEReceiver<List<UserAccessRightsProxy>>() {

				@Override
				public void onSuccess(List<UserAccessRightsProxy> response) {
					
					dialogBoxQuestion.setUserAccessRightsList(response);
					
					fireQuestionAccessCountByInstitutionOrEventOrQuestionNameOrKeywordRequest(new BMEReceiver<Long>() {
						@Override
						public void onSuccess(Long response) {
							if (view == null) {
								// This activity is dead
								return;
							}
							Log.debug("Geholte Events aus der Datenbank: " + response);
							questionAccessTable.setRowCount(response.intValue(), true);							
							onRangeQuestionAccessCountByInstitutionOrEventOrQuestionNameOrKeywordRequest();
						}
					});
				}
			});
		}	
	}
	
	protected void onRangeQuestionAccessCountByInstitutionOrEventOrQuestionNameOrKeywordRequest() {
		
		final Range range = questionAccessTable.getVisibleRange();
		AppLoader.setNoLoader();
		final BMEReceiver<List<QuestionProxy>> callback = new BMEReceiver<List<QuestionProxy>>() {
			@Override
			public void onSuccess(List<QuestionProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				questionAccessTable.setRowData(range.getStart(), values);	
			}
		};

		fireQuestionAccessByInstitutionOrEventOrQuestionNameOrKeywordRequest(range, callback);
		
	}

	///////
	protected void onRangeEventAccessByInstitutionOrEventnameChanged() {
		
		final Range range = eventAccessTable.getVisibleRange();
		AppLoader.setNoLoader();
		final BMEReceiver<List<QuestionEventProxy>> callback = new BMEReceiver<List<QuestionEventProxy>>() {
			@Override
			public void onSuccess(List<QuestionEventProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				eventAccessTable.setRowData(range.getStart(), values);

			}
			/* @Override
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

					
				}*/
		};

		fireEventAccessByInstitutionOrEventnameRangeRequest(range, callback);
		
	}
	
	private void fireEventAccessByInstitutionOrEventnameRangeRequest(final Range range,
            final BMEReceiver<List<QuestionEventProxy>> callback) {
			createEventAccessByInstitutionOrEventnameRangeRequest(range).fire(callback);
	}
    protected Request<java.util.List<medizin.client.proxy.QuestionEventProxy>> createEventAccessByInstitutionOrEventnameRangeRequest(Range range) {
        return requests.questionEventRequest().findQuestionEventsByInstitutionOrEvent( institutionFilter==null? null:institutionFilter.getId(), eventNameFilter, range.getStart(), range.getLength());
    }
    
    /**
     * Function for create a Request over all Instituions, is used for fill the ListBox for Filtering question event access
     * @return Request<List<InstitutionProxy>>
     */
    protected Request<java.util.List<medizin.client.proxy.InstitutionProxy>> createRequestAllInstitutions() {
        return requests.institutionRequest().findAllInstitutions();
    }
    
    /**
     * Function for create a Request over all Eventss, is used for fill the ListBox for Filtering question event access
     * @return Request<List<InstitutionProxy>>
     */
    protected Request<java.util.List<medizin.client.proxy.QuestionEventProxy>> createRequestAllEvents() {
        //return requests.questionEventRequest().findAllQuestionEvents();
        
        return requests.questionEventRequest().findAllQuestionEventByLoggedPerson();
    }

    
	/**
	 * Function for fire a Request over all Instituions, is used for fill the ListBox for Filtering question event access
	 * @param final BMEReceiver<List<InstitutionProxy>> callback
	 */
	private void fireRequestAllInstitution( final BMEReceiver<List<InstitutionProxy>> callback) {
		//createRequestAllInstitutions().fire(callback);
		requests.institutionRequest().findInstitutionByLoggedPerson().fire(callback);
	}
	
	/**
	 * Function for fire a Request over all Events, is used for fill the ListBox for Filtering question access
	 * @param final BMEReceiver<List<QuestionEventProxy>> callback
	 */
	private void fireRequestAllEvent( final BMEReceiver<List<QuestionEventProxy>> callback) {
		createRequestAllEvents().fire(callback);
	}
	
	private String eventNameFilter = "";

	private String questiuonStringFilter;

	private ListBox eventListbox;

	private CellTable<QuestionProxy> questionAccessTable;
    protected void fireQuestionAccessCountByInstitutionOrEventnameRequest(BMEReceiver<java.lang.Long> callback) {
    	
    	requests.questionEventRequest().countQuestionEventsByInstitutionOrEvent(institutionFilter==null? null:institutionFilter.getId(), eventNameFilter).fire(callback);
    }
    //////

	@Override
	public void deleteQuestionAccessClicked(UserAccessRightsProxy questionAccess) {
		//AppLoader.setNoLoader();
		AppLoader.setCurrentLoader(questionAccessView.getLoadingPopup());
		requests.userAccessRightsRequest().remove()
		.using(questionAccess).fire(new BMEReceiver<Void>() {

			public void onSuccess(Void ignore) {
				Log.debug("Sucessfull deleted");
				initQuestionAccess();
			}

			/*@Override
			public void onFailure(ServerFailure error) {
				Log.warn(McAppConstant.ERROR_WHILE_DELETE
						+ " in Institution -" + error.getMessage());
				if (error.getMessage().contains(
						"ConstraintViolationException")) {
					Log.debug("Fehlen beim erstellen: Doppelter name");
					//TODO mcAppFactory.getErrorPanel().setErrorMessage(cAppConstant.INSTITUTION_IS_REFERENCED);
				}

			}

			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
						+ " in Institution -" + message);
				
				//TODO mcAppFactory.getErrorPanel().setErrorMessage(message);

			}*/

		});
		
	
		
	}

	@Override
	public void addNewQuestionAccessClicked() {
		institutionFilter=null;
		questiuonStringFilter="";
		dialogBoxQuestion = new QuestionAccessDialogboxImpl();
		Log.info("addNewQuestionClicked");
		this.questionAccessTable = dialogBoxQuestion.getTable();
		dialogBoxQuestion.setPresenter(this);
		dialogBoxQuestion.setDelegate(this);
		
		final BMEReceiver<List<InstitutionProxy>> callback2 = new BMEReceiver<List<InstitutionProxy>>() {
			@Override
			public void onSuccess(List<InstitutionProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				
				DefaultSuggestOracle<InstitutionProxy> suggestOracle1 = (DefaultSuggestOracle<InstitutionProxy>) dialogBoxQuestion.getSearchInstitution().getSuggestOracle();
				suggestOracle1.setPossiblilities(values);
				
				dialogBoxQuestion.getSearchInstitution().setSuggestOracle(suggestOracle1);
				dialogBoxQuestion.getSearchInstitution().setRenderer(new AbstractRenderer<InstitutionProxy>() {

					@Override
					public String render(InstitutionProxy object) {
						// TODO Auto-generated method stub
						if(object!=null)
						{
							return object.getInstitutionName();
						}
						else
						{
							return "";
						}
					}
				});
			}
		};

		fireRequestAllInstitution( callback2);
		
		final BMEReceiver<List<QuestionEventProxy>> callback3 = new BMEReceiver<List<QuestionEventProxy>>() {
			@Override
			public void onSuccess(List<QuestionEventProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				
				DefaultSuggestOracle<QuestionEventProxy> suggestOracle1 = (DefaultSuggestOracle<QuestionEventProxy>) dialogBoxQuestion.getSearchEvent().getSuggestOracle();
				suggestOracle1.setPossiblilities(values);
				
				dialogBoxQuestion.getSearchEvent().setSuggestOracle(suggestOracle1);
				dialogBoxQuestion.getSearchEvent().setRenderer(new AbstractRenderer<QuestionEventProxy>() {

					@Override
					public String render(QuestionEventProxy object) {
						if(object!=null)
						{
							return object.getEventName();
						}
						else
						{
							return "";
						}
					}
				});
				
			}
		};

		fireRequestAllEvent( callback3);

		initQuestionAccessDialogbox();
		rangeQuestionAccessAllChangeHandler = questionAccessTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {				
				ActivityUserDetails.this.onRangeQuestionAccessCountByInstitutionOrEventOrQuestionNameOrKeywordRequest();
			}
		});
		
		dialogBoxQuestion.display();
	}

	@Override
	public void addClicked(AccessRights rights, QuestionEventProxy questionEvent) {
		/*UserAccessRightsRequest request = requests.userAccessRightsRequest();
		UserAccessRightsProxy eventAccess = request.create(UserAccessRightsProxy.class);

		request.persist().using(eventAccess);

		eventAccess.setAccRights(rights);
		eventAccess.setPerson(person);
		eventAccess.setQuestionEvent(questionEvent);
//		eventAccess.setVersion(0);
		
		request.fire(new BMEReceiver<Void>() {
			
	          @Override
	          public void onSuccess(Void response) {
	        	  Log.info("PersonSucesfullSaved");
	        	  
	        		initEventAccess();
	        		initEventAccessDialogbox();
	          //	goTo(new PlaceUser(person.stableId()));
	          }
	          
	          public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
				}
	      }); */
		AppLoader.setCurrentLoader(dialogBoxEvent.getLoadingPopup());
		requests.userAccessRightsRequest().persistQuestionEventAccess(rights, person.getId(), questionEvent.getId()).fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if (response)
				{
					initEventAccess();
					initEventAccessDialogbox();
				}
				else
				{
					ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.accRightsErrorMsg());
				}
			}
		});
		
		
	}

	private InstitutionProxy institutionFilter=null;
//	private InstitutionProxy institutionQuestionFilter=null;


	@Override
	public void filterInstitutionChanged(Long value) {
		
		if (value!=null&&!value.equals(""))
		{
			requests.institutionRequest().findInstitution(value).fire(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					institutionFilter=(InstitutionProxy)response;
					initEventAccessDialogbox();
				}
			});
			
			/*requests.find(requests.getProxyId(value)).fire(new BMEReceiver<Object>() {
	
				
				public void onFailure(ServerFailure error){
					Log.error(error.getMessage());
				}
				@Override
				public void onSuccess(Object response) {
					if(response instanceof InstitutionProxy){
						Log.info(((InstitutionProxy) response).getInstitutionName());
						institutionFilter=(InstitutionProxy)response;
						initEventAccessDialogbox();
					}
	
					
				}
			    });*/
		}
		else {
			this.institutionFilter=null;
			initEventAccessDialogbox();
			
		}
	}
	
	@Override
	public void filterEventChanged(String value) {
		
		
		
		if (value!=null&&!value.equals(""))
		{
			Log.debug(value);
			this.eventNameFilter=value;
//			requests.find(requests.getProxyId(value)).fire(new BMEReceiver<Object>() {
//	
//				
//				public void onFailure(ServerFailure error){
//					Log.error(error.getMessage());
//				}
//				@Override
//				public void onSuccess(Object response) {
//					if(response instanceof QuestionEventProxy){
//						Log.info(((QuestionEventProxy) response).getEventName());
//						eventFilter=(QuestionEventProxy)response;
//						initEventAccessDialogbox();
//					}
//	
//					
//				}
//			    });
		}
		else {
			this.eventNameFilter="";
			
			initEventAccessDialogbox();
			
		}
		
		initEventAccessDialogbox();
			
	
}
		
		@Override
		public void filterEventQuestionChanged(Long value) {
			
			if (value!=null&&!value.equals(""))
			{
				requests.questionEventRequest().findQuestionEvent(value).fire(new BMEReceiver<Object>() {

					@Override
					public void onSuccess(Object response) {
						eventFilter=(QuestionEventProxy)response;
						initQuestionAccessDialogbox();
					}
				});
				
				/*requests.find(requests.getProxyId(value)).fire(new BMEReceiver<Object>() {
		
					
					public void onFailure(ServerFailure error){
						Log.error(error.getMessage());
					}
					@Override
					public void onSuccess(Object response) {
						if(response instanceof QuestionEventProxy){
							Log.info(((QuestionEventProxy) response).getEventName());
							eventFilter=(QuestionEventProxy)response;
							initQuestionAccessDialogbox();
						}
		
						
					}
				    });*/
			}
			else {
				this.eventFilter=null;
				initQuestionAccessDialogbox();
				
			}
			
			initQuestionAccessDialogbox();
				
		
	}

		@Override
		public void addClicked(AccessRights rights, QuestionProxy question) {
			/*Log.debug("im add clicked");
			UserAccessRightsRequest request = requests.userAccessRightsRequest();
			UserAccessRightsProxy eventAccess = request.create(UserAccessRightsProxy.class);

			request.persist().using(eventAccess);

			eventAccess.setAccRights(rights);
			eventAccess.setPerson(person);
			eventAccess.setQuestion(question);
//			eventAccess.setVersion(0);
			
			request.fire(new BMEReceiver<Void>() {
				
		          @Override
		          public void onSuccess(Void response) {
		        	  Log.info("eventAccessnSucesfullSaved");
		        	  
		        		initQuestionAccess();
		        		initQuestionAccessDialogbox();
		          //	goTo(new PlaceUser(person.stableId()));
		          }
		          
		          public void onFailure(ServerFailure error){
						Log.error(error.getMessage());
					}
		      }); */
			
			AppLoader.setCurrentLoader(dialogBoxQuestion.getLoadingPopup());
			requests.userAccessRightsRequest().persistQuestionAccess(rights, person.getId(), question.getId()).fire(new BMEReceiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					if (response)
					{
						initQuestionAccess();
						initQuestionAccessDialogbox();
					}
					else
					{
						ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.accRightsErrorMsg());
					}
				}
			});
		}
		
		public void filterQuestionChanged(String text) {			
			questiuonStringFilter=text;
			initQuestionAccessDialogbox();
		}
			

		@Override
		public void filterInstitutionQuestionChanged(Long value) {
			if (value!=null&&!value.equals(""))
			{
				requests.institutionRequest().findInstitution(value).fire(new BMEReceiver<Object>() {

					@Override
					public void onSuccess(Object response) {
						institutionFilter=(InstitutionProxy)response;
						initQuestionAccessDialogbox();
					}
				});
				
				/*requests.find(requests.getProxyId(value)).fire(new BMEReceiver<Object>() {
		
					
					public void onFailure(ServerFailure error){
						Log.error(error.getMessage());
					}
					@Override
					public void onSuccess(Object response) {
						if(response instanceof InstitutionProxy){
							Log.info(((InstitutionProxy) response).getInstitutionName());
							institutionFilter=(InstitutionProxy)response;
							initQuestionAccessDialogbox();
						}
		
						
					}
				    });*/
			}
			else {
				this.institutionFilter=null;
				initQuestionAccessDialogbox();
				
			}
			
			initQuestionAccessDialogbox();
			
		}

		@Override
		public void filterSearchTextChanged(boolean value) {
			filterQuestionText=value;
			initQuestionAccessDialogbox();
		}

		@Override
		public void filterSearchKeywordChanged(boolean value) {
			filterKeywords=value;
			initQuestionAccessDialogbox();
			
		}

		//spec change
		
		@Override
		public void addClicked(AccessRights rights, final InstitutionProxy institutionProxy) {
			
			AppLoader.setCurrentLoader(dialogBoxInstitute.getLoadingPopup());
			requests.personRequest().myGetLoggedPerson().fire(new BMEReceiver<PersonProxy>() {

				@Override
				public void onSuccess(PersonProxy response) {
					
					UserAccessRightsRequest questionAccessRequest = requests.userAccessRightsRequest();
					UserAccessRightsProxy questionAccessProxy = questionAccessRequest.create(UserAccessRightsProxy.class);
					
					
					questionAccessProxy.setInstitution(institutionProxy);
					questionAccessProxy.setPerson(person);
					
					if (response.getIsAdmin())
						questionAccessProxy.setAccRights(AccessRights.AccPrimaryAdmin);
					else
						questionAccessProxy.setAccRights(AccessRights.AccSecondaryAdmin);
					
					questionAccessRequest.persist().using(questionAccessProxy).fire(new BMEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							initInstituteAccess();
							initInstituteAccessDialogBox();
						}
						
						/*@Override
						public void onFailure(ServerFailure error) {
							Log.info(error.getMessage());
						}*/
					});
				}
			});
			
			
		}
		
		public void initInstituteAccess()
		{
			//this.instituteAccessView = view.getInstituteAccessView();
			this.instituteAccessView = userDetailsView.getInstituteAccessView();
			instituteAccessView.setDelegate(this);
			instituteAccessView.setPresenter(this);
			
			onInstituteAccessRangeChanged();
			
			instituteAccessView.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					
					ActivityUserDetails.this.onInstituteAccessRangeChanged();
				}
			});
		}
		
		public void onInstituteAccessRangeChanged()
		{
			AppLoader.setNoLoader();
			requests.userAccessRightsRequest().countInstiuteAccessByPerson(person.getId()).fire(new BMEReceiver<Long>() {

				@Override
				public void onSuccess(Long response) {
					instituteAccessView.getTable().setRowCount(response.intValue(), true);
					
					final Range range = instituteAccessView.getTable().getVisibleRange();
					requests.userAccessRightsRequest().findInstiuteAccessByPerson(person.getId(), range.getStart(), range.getLength()).with("institution").fire(new BMEReceiver<List<UserAccessRightsProxy>>() {

						@Override
						public void onSuccess(List<UserAccessRightsProxy> response) {
							instituteAccessView.getTable().setRowData(range.getStart(), response);
						}
					});
				}
			});
		}

		@Override
		public void deleteInstituteAccessClicked(UserAccessRightsProxy event) {
			//AppLoader.setNoLoader();
			AppLoader.setCurrentLoader(instituteAccessView.getLoadingPopup());
			requests.userAccessRightsRequest().remove().using(event).fire(new BMEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					initInstituteAccess();
				}
				/*@Override
				public void onFailure(ServerFailure error) {
					Log.info(error.getMessage());
				}*/
			});
		}

		String instituteSearchText = "";
		
		@Override
		public void addNewInstituteAccessClicked() {
			dialogBoxInstitute = new InstituteAccessDialogBoxImpl();
			dialogBoxInstitute.display();
			dialogBoxInstitute.setPresenter(this);
			dialogBoxInstitute.setDelegate(this);
			
			/*requests.institutionRequest().findAllInstitutions().fire(new BMEReceiver<List<InstitutionProxy>>() {

				@Override
				public void onSuccess(List<InstitutionProxy> response) {
					
					dialogBoxInstitute.getTable().setRowCount(response.size(), true);					
					dialogBoxInstitute.getTable().setRowData(dialogBoxInstitute.getTable().getVisibleRange().getStart(), response);
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					Log.info(error.getMessage());
				}
			});*/
			initInstituteAccessDialogBox();
			
			dialogBoxInstitute.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {					
					ActivityUserDetails.this.initInstituteAccessDialogBox();
				}
			});
		}

		public void initInstituteAccessDialogBox()
		{
			if (person != null)
			{
				AppLoader.setNoLoader();
				requests.institutionRequest().countInstitutionByName(instituteSearchText, person.getId()).fire(new BMEReceiver<Long>() {

					@Override
					public void onSuccess(Long response) {
						dialogBoxInstitute.getTable().setRowCount(response.intValue(), true);
						
						final Range range = dialogBoxInstitute.getTable().getVisibleRange();
						AppLoader.setNoLoader();		
						requests.institutionRequest().findInstitutionByName(instituteSearchText, person.getId(), range.getStart(), range.getLength()).fire(new BMEReceiver<List<InstitutionProxy>>() {

							@Override
							public void onSuccess(List<InstitutionProxy> list) {
								dialogBoxInstitute.getTable().setRowData(range.getStart(), list);
							}
						});
					}
				});
			}
		}
		
		@Override
		public void filterInstituteChanged(String text) {
			instituteSearchText = text;
			initInstituteAccessDialogBox();
		}

		@Override
		public void placeChanged(Place place) {
			//updateSelection(event.getNewPlace());
			// TODO implement
		}
}

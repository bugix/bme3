package medizin.client.activites;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceInstitutionEvent;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.request.QuestionEventRequest;
import medizin.client.ui.view.EventView;
import medizin.client.ui.view.EventViewImpl;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.process.AppLoader;

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
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

public class ActivityInstitutionEvent extends AbstractActivityWrapper implements EventView.Presenter, EventView.Delegate {

	private PlaceInstitutionEvent eventPlace;

	private AcceptsOneWidget widget;
	private EventView view;
	
	//private SingleSelectionModel<QuestionEventProxy> selectionModel;


	private HandlerRegistration rangeChangeHandler;
	
	private final Map<EntityProxyId<QuestionEventProxy>, Integer> idToRow = new HashMap<EntityProxyId<QuestionEventProxy>, Integer>();
	private final Map<EntityProxyId<QuestionEventProxy>, QuestionEventProxy> idToProxy = new HashMap<EntityProxyId<QuestionEventProxy>, QuestionEventProxy>();
	private Boolean pendingSelection;
	private InstitutionProxy institution;
	

	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityInstitutionEvent(PlaceInstitutionEvent place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.eventPlace = place;
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

	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/
	
	InstitutionProxy selectedInstitutionProxy = null;
	
	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		EventView eventView = new EventViewImpl(reciverMap);
		eventView.setName("hallo");
		eventView.setPresenter(this);
		this.widget = widget;
		this.view = eventView;
        widget.setWidget(eventView.asWidget());
		setTable(view.getTable());
        
		/*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});*/
		//init();
		AppLoader.setNoLoader();
		requests.find(eventPlace.getProxyId()).fire(new BMEReceiver<Object>() {

			/*public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}*/
			@Override
			public void onSuccess(Object response) {
				if(response instanceof InstitutionProxy){
					Log.info(((InstitutionProxy) response).getInstitutionName());
					selectedInstitutionProxy = (InstitutionProxy) response; 
					init((InstitutionProxy) response);
				}

				
			}
		    });


		//Log.warn(mcAppFactory.getRequestFactory().getProxyId(eventPlace.getProxyId().toString()));
		
		// Inherit the view's key provider
		/*ProvidesKey<QuestionEventProxy> keyProvider = ((AbstractHasData<QuestionEventProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<QuestionEventProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				QuestionEventProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getEventName() + " selected!");
					showDetails(selectedObject);
				}
			}
		});*/
		
		view.setDelegate(this);
		//updateSelection(mcAppFactory.getPlaceController().getWhere());

	}
	
	protected void showDetails(QuestionEventProxy questionEvent) {
	//	Log.debug("QuestionEvent Stable id: " + questionEvent.stableId() + " " +  PlaceInstitutionEvent.Operation.DETAILS); 
	//	mcAppFactory.getPlaceController().goTo(new PlaceInstitutionEvent(questionEvent.stableId(), PlaceInstitutionEvent.Operation.DETAILS));
	}

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	
    protected Request<java.util.List<medizin.client.proxy.QuestionEventProxy>> createRangeRequest(Range range) {
        return requests.questionEventRequest().findQuestionEventsByInstitutionNonRoo(institution.getId(), range.getStart(), range.getLength());
    }

    protected void fireCountRequest(BMEReceiver<java.lang.Long> callback) {
    	requests.questionEventRequest().countQuestionEventsByInstitutionNonRoo(institution.getId()).fire(callback);
    }
    
	private void init(InstitutionProxy institution) {

		this.institution = institution;
		AppLoader.setNoLoader();
		fireCountRequest(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Events aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged();
			}
		});
		
		
		
		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityInstitutionEvent.this.onRangeChanged();
			}
		});
	}
	
	private CellTable<QuestionEventProxy> table;
	
	public CellTable<QuestionEventProxy> getTable(){
		return table;
	}
	public void  setTable(CellTable<QuestionEventProxy> table){
		this.table = table;
	}

	protected void onRangeChanged() {
		
		Log.debug("Im ActivityInsitutionEvent.onRangeChanged");
		final Range range = table.getVisibleRange();

		final BMEReceiver<List<QuestionEventProxy>> callback = new BMEReceiver<List<QuestionEventProxy>>() {
			@Override
			public void onSuccess(List<QuestionEventProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				idToRow.clear();
				idToProxy.clear();
				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
					QuestionEventProxy questionEvent = values.get(i);
					@SuppressWarnings("unchecked")
					// Why is this cast needed?
					EntityProxyId<QuestionEventProxy> proxyId = (EntityProxyId<QuestionEventProxy>) questionEvent.stableId();
					idToRow.put(proxyId, row);
					idToProxy.put(proxyId, questionEvent);
				}
				Log.debug("Im ActivityInsitutionEvent.onRangeChanged-before Table");
				table.setRowData(range.getStart(), values);
				Log.debug("Im ActivityInsitutionEvent.onRangeChanged-after Table");
				
				finishPendingSelection();
			if (widget != null) {
		          widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(range, callback);
		
	}
	
	private void getLastPage() {
		fireCountRequest(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				int rows = response.intValue();
				table.setRowCount(rows, true);
				if (rows > 0) {
					int pageSize = table.getVisibleRange().getLength();
					int remnant = rows % pageSize;
					if (remnant == 0) {
						table.setVisibleRange(rows - pageSize, pageSize);
					} else {
						table.setVisibleRange(rows - remnant, pageSize);
					}
				}
				onRangeChanged();
			}
		});
	}
	private void fireRangeRequest(final Range range,
            final BMEReceiver<List<QuestionEventProxy>> callback) {
			AppLoader.setNoLoader();
			createRangeRequest(range).with(view.getPaths()).fire(callback);
}
	
	/**
	 * Finish selecting a proxy that hadn't yet arrived when
	 * {@link #select(EntityProxyId)} was called.
	 */
	private void finishPendingSelection() {
		if (pendingSelection != null) {
			QuestionEventProxy selectMe = idToProxy.get(pendingSelection);
			pendingSelection = null;
			if (selectMe != null) {
				// TODO  make selection Model
				//selectionModel.setSelected(selectMe, true);
			}
		}
	}

	@Override
	public void deleteClicked(QuestionEventProxy questionEvent) {
/*        if (!view.confirm("Really delete this entry? You cannot undo this change.")) {
            return;
        }
        requests.answerRequest().remove().using(view.getValue()).fire(new Receiver<Void>() {

            public void onSuccess(Void ignore) {
                if (display == null) {
                    return;
                }
                placeController.goTo(getBackButtonPlace());
            }
        });*/
		AppLoader.setCurrentLoader(view.getLoadingPopup());
		requests.questionEventRequest().remove().using(questionEvent).fire(new BMEReceiver<Void>(reciverMap) {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	init(institution);
            	
            }
            
            @Override
            public boolean onReceiverFailure() {
            	
            	ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.questionEventDelError());
            	return false;
            }
        
        });
		
	}

	@Override
	public void editClicked(QuestionEventProxy proxy, String questionEventName) {
		QuestionEventRequest request = requests.questionEventRequest();
		proxy = request.edit(proxy);
		proxy.setEventName(questionEventName);
		AppLoader.setCurrentLoader(view.getLoadingPopup());
		request.persist().using(proxy).fire(new BMEReceiver<Void>(reciverMap) {

            public void onSuccess(Void ignore) {
            	if (selectedInstitutionProxy != null)
            		init(selectedInstitutionProxy);
            }
		});
	}

	@Override
	public void newClicked(String eventName) {
		Log.debug("new QuestionEvent Clicked: " + eventName);
		QuestionEventRequest request = requests.questionEventRequest();
		QuestionEventProxy questionEvent = request.create(QuestionEventProxy.class);
		questionEvent.setEventName(eventName);
		questionEvent.setInstitution(institution);
//		questionEvent.setVersion(0);
		AppLoader.setCurrentLoader(view.getLoadingPopup());
		request.persist().using(questionEvent).fire(new BMEReceiver<Void>(reciverMap) {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull created");
            	getLastPage();
            
            	
            }
			/*@Override
			public void onFailure(ServerFailure error) {
					Log.warn(McAppConstant.ERROR_WHILE_CREATE + " in QuestionEvent -" + error.getMessage());
					if(error.getMessage().contains("ConstraintViolationException")){
						Log.debug("Fehlen beim erstellen: Doppelter name");
						// TODO mcAppFactory.getErrorPanel().setErrorMessage(McAppConstant.CONTENT_NOT_UNIQUE);
					}
				
			}
			@Override
			public void onViolation(Set<Violation> errors) {
				Log.debug("Fehlen beim erstellen, volation: " + errors.toString());
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while(iter.hasNext()){
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_CREATE_VIOLATION + " in QuestionEvent -" + message);
				
				ErrorPanel erorPanel = new ErrorPanel();
			        	  erorPanel.setWarnMessage(message);

				
			}*/
            
        });
	}

	@Override
	public void placeChanged(Place place) {
		//updateSelection(event.getNewPlace());
		// TODO implement
	}
}

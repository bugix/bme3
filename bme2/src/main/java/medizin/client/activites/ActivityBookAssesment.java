package medizin.client.activites;

import java.util.Iterator;
import java.util.List;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceBookAssesment;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.ui.view.BookAssesmentView;
import medizin.client.ui.view.BookAssesmentViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityBookAssesment extends AbstractActivityWrapper implements BookAssesmentView.Presenter ,BookAssesmentView.Delegate{

	//private PlaceBookAssesment bookAssesmentPlace;
	private AcceptsOneWidget widget;
	private BookAssesmentView view;

	//HashmMap for AssesmentProx
	/*private final Map<EntityProxyId<AssesmentProxy>, Integer> idToRow = new HashMap<EntityProxyId<AssesmentProxy>, Integer>();
	
	private final Map<EntityProxyId<AssesmentProxy>, AssesmentProxy> idToProxy = new HashMap<EntityProxyId<AssesmentProxy>, AssesmentProxy>();*/
	
	//private Boolean pendingSelection;
	
	private ActivityManager activityManger;	
	private ActivityBookAssesmentMapper activityBookAssesmentMapper;
	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityBookAssesment(PlaceBookAssesment place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		//this.bookAssesmentPlace = place;
        this.requests = requests;
        this.placeController = placeController;
		this.activityBookAssesmentMapper = new ActivityBookAssesmentMapper(requests,  placeController);
		this.activityManger = new ActivityManager(activityBookAssesmentMapper, requests.getEventBus());
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

		activityManger.setDisplay(null);
	}
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		BookAssesmentView bookAssesmentView = new BookAssesmentViewImpl();
		bookAssesmentView.setPresenter(this);
		this.widget = widget;
		this.view = bookAssesmentView;
        widget.setWidget(bookAssesmentView.asWidget());
        view.setDelegate(this);
        
        /*eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				//updateSelection(event.getNewPlace());
				// TODO implement
			}
		});*/
        /*
         * Start Loading tabs for Assements
         */
        activityManger.setDisplay(view.getDetailsPanel());
        init();
	}

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	
	
		//Request fetching Assesment-objects
		/*protected Request<java.util.List<medizin.client.proxy.AssesmentProxy>> createRangeRequest() {
	        return requests.assesmentRequest().findActiveAssesments();
	    }*/

	    /*protected void fireCountRequest(Receiver<Long> callback) {
	    	requests.assesmentRequest().countAssesments().fire(callback);
	    }*/
	    
	    /**
	     * Init fires fireCountRequest @author adrian.alioski
	     * Returns number of Assesments(Pr�fungen) in database 
	     */
	    
		private void init() {
			
			if(institutionActive != null) {
				
				/* get the list of assessments for given institution*/
	
				requests.assesmentRequest().findActiveAssesments(institutionActive.getId()).fire(new BMEReceiver<List<AssesmentProxy>>() {
					@Override
					public void onSuccess(List<AssesmentProxy> values) {
						if (view == null) {
							// This activity is dead
							Log.debug("view ist null");
							return;
						}
						Iterator<AssesmentProxy> iterAssProxy = values.iterator();
						while(iterAssProxy.hasNext()){
							AssesmentProxy assProxy = iterAssProxy.next();
							view.createTab(assProxy.getName(), assProxy.stableId());
						}
						view.addTabHandler();
						
					if (widget != null) {
				          widget.setWidget(view.asWidget());
						}
					}
				});	
			}else {
				Log.error("institutionActive is null.");
			}
			
			
			//onRangeChanged();
		}
		

		/*protected void onRangeChanged() {
			

			final Receiver<List<AssesmentProxy>> callback = new Receiver<List<AssesmentProxy>>() {
				@Override
				public void onSuccess(List<AssesmentProxy> values) {
					if (view == null) {
						// This activity is dead
						Log.debug("view ist null");
						return;
					}
					Iterator<AssesmentProxy> iterAssProxy = values.iterator();
					while(iterAssProxy.hasNext()){
						AssesmentProxy assProxy = iterAssProxy.next();
						view.createTab(assProxy.getName(), assProxy.stableId());
					}
					
					
				if (widget != null) {
			          widget.setWidget(view.asWidget());
					}
				}
			};

			fireGetRequest(callback);
			
		}*/
		/**End onRange Changed**/
		
		/*private void fireGetRequest( final Receiver<List<AssesmentProxy>> callback) {
				createRangeRequest().fire(callback);

				}*/
	
		
		/**
		 * Finish selecting a proxy that hadn't yet arrived when
		 * {@link #select(EntityProxyId)} was called.
		 */
		/*private void finishPendingSelection() {
			if (pendingSelection != null) {
				AssesmentProxy selectMe = idToProxy.get(pendingSelection);
				pendingSelection = null;
				if (selectMe != null) {
					// TODO  make selection Model
					//selectionModel.setSelected(selectMe, true);
				}
			}
		}*/





		@Override
		public void placeChanged(Place place) {
			//updateSelection(event.getNewPlace());
			// TODO implement
		}

		@Override
		public void yearSelected(String selectedYear) {
			Log.info("getting all assesment of year : " + selectedYear);
			
			requests.assesmentRequest().findAssesmentOfGivenYear(selectedYear).with("mc").fire(new BMEReceiver<List<AssesmentProxy>>() {

				@Override
				public void onSuccess(List<AssesmentProxy> response) {
					Log.info("Total Assesment of this year is : " + response.size());
					((BookAssesmentViewImpl)view).setAssesmentSuggsetBoxValue(response);
				}
			});
			
		}
}

package medizin.client.activites;

import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceUser;
import medizin.client.place.PlaceUserDetails;
import medizin.client.proxy.PersonProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.ui.view.user.UserView;
import medizin.client.ui.view.user.UserViewImpl;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.process.AppLoader;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
/**
 * Activity Handling UserViews.
 * @author masterthesis
 *
 */
public class ActivityUser extends AbstractActivityWrapper implements UserView.Presenter, UserView.Delegate {

	private PlaceUser userPlace;
	private AcceptsOneWidget widget;
	private UserView view;
	private AdvanceCellTable<PersonProxy> table;
	private ActivityManager activityManger;
	private ActivityUserMapper activityUserMapper;
	private SingleSelectionModel<PersonProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityUser activityUser;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private EntityProxyId<?> proxyId = null;
	private String searchValue ="";
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";
	
	@Inject
	public ActivityUser(PlaceUser place, McAppRequestFactory requests,PlaceController placeController) {
		super(place, requests, placeController);
		this.userPlace = place;
        this.requests = requests;
        this.placeController = placeController;
        this.activityUser=this;
        
//        // Filter
//		CachingActivityMapper cached = new CachingActivityMapper(activityUserMapper);
//		FilterForUserDeitalPlaces filterForUserDeitalPlaces = new FilterForUserDeitalPlaces();
//		ActivityMapper masterActivityMap = new FilteredActivityMapper(filterForUserDeitalPlaces, cached);
//		activityManger = new ActivityManager(masterActivityMap, requests.getEventBus());
        
		this.activityUserMapper = new ActivityUserMapper(requests, placeController);
		this.activityManger = new ActivityManager(activityUserMapper,requests.getEventBus());
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
		if(activityManger!=null)
		activityManger.setDisplay(null);
		
		if(view!=null){
			view.setDelegate(null);
			view = null;
		}
		if(rangeChangeHandler!=null){
		
			rangeChangeHandler.removeHandler();
			rangeChangeHandler = null;
		}
		

	}

	public void setTable(AdvanceCellTable<PersonProxy> table) {
		this.table = table;
	}
	
	
	/*@Override
	public void start(final AcceptsOneWidget widget, final EventBus eventBus) {
		requests.questionAccessRequest().checkInstitutionalAdmin().fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
			if(response){
				 start2(widget, eventBus);
				}
			}
		});
		//super.start(widget, eventBus);

	}*/
	
	@Override
	public void start2(final AcceptsOneWidget widget, final EventBus eventBus) {
		
		if (isAdminOrInstitutionalAdmin())
		{
			start3(widget, eventBus);
		}

		/*requests.userAccessRightsRequest().checkInstitutionalAdmin().fire(new BMEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
			if(response){
				 start3(widget, eventBus);
				}
			}
		});*/
	}
	
	public void start3(AcceptsOneWidget widget, EventBus eventBus) {
		Log.info("Activity Person start");
		
		UserView userView = new UserViewImpl();
		
		userView.setPresenter(this);
		this.widget = widget;
		this.view = userView;
		widget.setWidget(this.view.asWidget());
		setTable(view.getTable());
		 
		
		
		SplitLayoutPanel splitLayoutPanel= view.getSplitLayoutPanel();
		
		/*eventBus.addHandler(PlaceChangeEvent.TYPE,
				new PlaceChangeEvent.Handler() {
					public void onPlaceChange(PlaceChangeEvent event) {

						Place place = event.getNewPlace();
						if(place instanceof PlaceUserDetails){
							init();
						}
						
					}
				});*/
		
		RecordChangeEvent.register(requests.getEventBus(), (UserViewImpl)view);
		//adding column mouse out of table.
		((UserViewImpl)view).addColumnOnMouseout();
		
		init();
		
		

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<PersonProxy> keyProvider = ((AbstractHasData<PersonProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<PersonProxy>(keyProvider);
		table.setSelectionModel(selectionModel);
		
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						PersonProxy selectedObject = selectionModel.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getEmail() + " selected!");
							showDetails(selectedObject);
						}
					}
				});

		
		view.setDelegate(this);
	}
	
	private void init() {
		if(rangeChangeHandler!=null){
			rangeChangeHandler.removeHandler();
			rangeChangeHandler = null;
		}

		AppLoader.setNoLoader();
		requests.personRequest().countAllUsersOfGivenSearch(searchValue).fire(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Personen aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged();
			}

		});

		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						ActivityUser.this.onRangeChanged();
					}
				});
	}
	
	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		AppLoader.setNoLoader();
		requests.personRequest().findAllUsersOfGivenSearch(sortname,sortorder,range.getStart(),range.getLength(),searchValue).with(view.getPaths()).fire(new BMEReceiver<List<PersonProxy>>() {
			
			@Override
			public void onSuccess(List<PersonProxy> values) {
				if (values==null ||view == null) {
					// This activity is dead
					return;
				}

				table.setRowData(range.getStart(), values);
				
				selectRow(range);

				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		});

	}

	private void selectRow(final Range range) {
		if (proxyId != null)
		{
			requests.find(proxyId).fire(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if (response != null && response instanceof PersonProxy)
					{
						PersonProxy selectedProxy = (PersonProxy) response;
						selectionModel.setSelected(selectedProxy, true);
						int start = table.getRowCount() - range.getLength();
						table.setPageStart((start < 0 ? 0 : start));
					}
					proxyId = null;
				}
			});
		}
	}

	/*private void getLastPage() {
		requests.personRequest().findAllPersonCount().fire(new BMEReceiver<Long>() {
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
	}*/

	private void showDetails(PersonProxy person) {
		Log.debug("Person Stable id: " + person.stableId() + " " + PlaceUserDetails.Operation.DETAILS);
		placeController.goTo(new PlaceUserDetails(person.stableId(),view.getScrollDetailPanel().getOffsetHeight()));
	}

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}

	@Override
	public void newClicked() {
		placeController.goTo(new PlaceUserDetails(PlaceUserDetails.Operation.CREATE,view.getScrollDetailPanel().getOffsetHeight()));
		
	}

	@Override
	public void placeChanged(Place place) {
		if(place instanceof PlaceUserDetails){
			if (((PlaceUserDetails)place).getProxyId() != null)
				proxyId  = ((PlaceUserDetails)place).getProxyId();
		}
		
		if(place instanceof PlaceUser) {
			init();
		}
	}

	@Override
	public void performSearch(String value) {
		Log.info("search text is" + value);
		
		this.searchValue = value;
		init();
	}

	@Override
	public void columnClickedForSorting(String sortname, Sorting sortorder) {
		this.sortname=sortname;
		this.sortorder=sortorder;
		init();
	}
}
